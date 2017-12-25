package com.github.athenaengine.essentials.models.entities.living

import com.github.athenaengine.essentials.`interface`.IParticipant
import com.github.athenaengine.essentials.enums.InventoryItemType
import com.github.athenaengine.essentials.enums.ScoreType
import com.github.athenaengine.essentials.models.entities.Item
import com.github.athenaengine.essentials.models.entities.Team
import com.github.athenaengine.essentials.models.entities.World
import com.github.athenaengine.essentials.models.holders.EItemHolder
import com.github.athenaengine.essentials.models.holders.LocationHolder
import com.github.athenaengine.essentials.models.packets.GamePacket
import com.github.athenaengine.essentials.providers.L2ObjectProvider
import com.l2jserver.gameserver.instancemanager.InstanceManager
import com.l2jserver.gameserver.model.L2Party
import com.l2jserver.gameserver.model.Location
import com.l2jserver.gameserver.model.actor.instance.L2PcInstance
import com.l2jserver.gameserver.model.holders.SkillHolder
import com.l2jserver.gameserver.model.items.instance.L2ItemInstance
import com.l2jserver.gameserver.network.serverpackets.SkillCoolTime
import com.l2jserver.gameserver.taskmanager.DecayTaskManager
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.ConcurrentMap

class Player(objectId: Int) : Playable(objectId), IParticipant {

    var protectionTimeEnd: Long = 0

    private var eventPlayerStatus: EventPlayerStatus? = null
    private var worldInstanceId = 0

    override fun isPlayer(): Boolean = true

    override fun getName(): String = getPc().getName()

    override fun getTitle(): String = getPc().getTitle()

    override fun setTitle(title: String) {
        getPc().setTitle(title)
        getPc().updateAndBroadcastStatus(2)
    }

    fun getCP(): Double = getPc().currentCp

    fun getMaxCP(): Double = getPc().maxCp.toDouble()

    fun setCP(cp: Double) { getPc().currentCp = cp }

    fun getPvpKills(): Int = getPc().pvpKills

    fun setPvpKills(value: Int) { getPc().pvpKills = value }

    fun getPkKills(): Int = getPc().pkKills

    fun setPkKills(value: Int) { getPc().pkKills = value }

    fun getFame(): Int = getPc().fame

    fun setFame(value: Int) { getPc().fame = value }

    fun getReturnLocation() = eventPlayerStatus!!.returnLocation

    fun getTeam() = eventPlayerStatus!!.team

    fun addToEvent(team: Team) = setTeam(team)

    fun removeFromEvent() {
        // Recovers player's title and color
        getPc().title = eventPlayerStatus!!.oriTitle
        getPc().appearance.titleColor = eventPlayerStatus!!.oriColorTitle

        // Remove the player from world instance
        InstanceManager.getInstance().getPlayerWorld(getPc()).removeAllowed(objectId)
        getPc().instanceId = 0
        worldInstanceId = 0

        // TODO: fix this
        // Remove the player from event listener (it's used to deny the manual res)
        //getPc().removeEventListener(EventEngineListener::class.java)

        eventPlayerStatus = null
    }

    fun teleportTo(loc: LocationHolder) {
        getPc().teleToLocation(Location(loc.x, loc.y, loc.z, loc.heading, loc.instanceId))
    }

    fun revive(spawnProtectionTime: Int) {
        if (getPc().isDead()) {
            DecayTaskManager.getInstance().cancel(getPc())
            getPc().doRevive()
            setCP(getMaxCP())
            setHP(getMaxHP())
            setMP(getMaxMP())
            protectionTimeEnd = System.currentTimeMillis() + spawnProtectionTime * 1000 // Milliseconds
        }
    }

    fun isProtected(): Boolean {
        return protectionTimeEnd > System.currentTimeMillis()
    }

    fun containsItem(itemId: Int): Boolean {
        return getPc().inventory.getItemByItemId(itemId) != null
    }

    fun equipItem(item: Item) {
        val l2Item = L2ObjectProvider.find(item.objectId) as L2ItemInstance
        getPc().useEquippableItem(l2Item, true)
    }

    fun unequipItem(type: InventoryItemType) {
        val pc: L2PcInstance = getPc()
        pc.useEquippableItem(pc.inventory.getPaperdollItem(type.value)!!, true)
    }

    fun giveBuffs(buffs: Collection<SkillHolder>) {
        buffs.forEach { buff -> buff.skill.applyEffects(getPc(), getPc()) }
    }

    fun setInstanceWorld(world: World) {
        world.addPlayer(this)
        worldInstanceId = world.getId()
    }

    fun removeItems(items: Collection<EItemHolder>) {
        // TODO: implement it
    }

    fun cancelAllEffects() {
        val pc = getPc()

        // Stop all effects
        pc.stopAllEffects()

        // Check Transform
        if (pc.isTransformed) pc.untransform()

        // Check Summon's and pets
        if (pc.hasSummon()) {
            pc.summon.stopAllEffectsExceptThoseThatLastThroughDeath()
            pc.summon.abortAttack()
            pc.summon.abortCast()
            pc.summon.unSummon(pc)
        }

        // Cancel all character cubics
        for (cubic in pc.cubics.values) {
            cubic.stopAction()
            cubic.cancelDisappear()
        }

        // Stop any cubic that has been given by other player
        pc.stopCubicsByOthers()

        // Remove player from his party
        pc.party!!.removePartyMember(pc, L2Party.messageType.Expelled)

        // Remove Agathion
        if (pc.agathionId > 0) {
            pc.agathionId = 0
            pc.broadcastUserInfo()
        }

        // Remove reuse delay skills
        pc.allSkills.forEach { skill -> pc.enableSkill(skill) }

        // Check Skills
        pc.sendSkillList()
        pc.sendPacket(SkillCoolTime(pc))
    }

    fun cancelAllActions() {
        // Cancel target
        getPc().setTarget(null)
        // Cancel any attack in progress
        getPc().breakAttack()
        // Cancel any skill in progress
        getPc().breakCast()
    }

    fun sendMessage(message: String) = getPc().sendMessage(message)

    override fun getPoints(type: ScoreType) = eventPlayerStatus!!.points[type] ?: 0

    override fun increasePoints(type: ScoreType, amount: Int) {
        val points = eventPlayerStatus!!.points[type] ?: 0
        eventPlayerStatus!!.points.put(type, points + amount)
    }

    override fun sendPacket(packet: GamePacket) = packet.send(this)

    override fun giveItems(items: Collection<EItemHolder>) {
        items.forEach { reward -> getPc().addItem(
                "eventReward", reward.id, reward.amount,
                null, true
        ) }
    }

    private fun setTeam(team: Team) {
        val location = LocationHolder(getPc().location.x, getPc().location.y,
                getPc().location.z, getPc().location.heading)

        eventPlayerStatus = EventPlayerStatus(
                oriColorTitle = getPc().appearance.titleColor,
                oriTitle = getPc().title,
                team = team,
                returnLocation = location)
    }

    private fun getPc(): L2PcInstance {
        return L2ObjectProvider.find(objectId) as L2PcInstance
    }

    class EventPlayerStatus(var points: ConcurrentMap<ScoreType, Int> = ConcurrentHashMap(),
                            val oriColorTitle: Int,
                            val oriTitle: String,
                            val team: Team,
                            var returnLocation: LocationHolder? = null)
}