package com.github.athenaengine.essentials.models.entities.living

import com.github.athenaengine.essentials.models.holders.LocationHolder
import com.github.athenaengine.essentials.providers.L2ObjectProvider
import com.l2jserver.gameserver.model.L2World
import com.l2jserver.gameserver.model.actor.L2Character
import com.l2jserver.gameserver.network.serverpackets.MagicSkillUse

open class Character(objectId: Int) : LivingEntity(objectId) {

    open fun getName(): String {
        return getL2Character().name
    }

    open fun getTitle(): String {
        return getL2Character().title
    }

    open fun setTitle(title: String) {
        getL2Character().title = title
    }

    fun getHP(): Double {
        return getL2Character().currentHp
    }

    fun getMP(): Double {
        return getL2Character().currentMp
    }

    fun getMaxHP(): Double {
        return getL2Character().maxHp.toDouble()
    }

    fun getMaxMP(): Double {
        return getL2Character().maxMp.toDouble()
    }

    fun setHP(hp: Double) {
        getL2Character().currentHp = hp
    }

    fun setMP(mp: Double) {
        getL2Character().currentMp = mp
    }

    fun die(killer: Character) {
        val l2Character = L2World.getInstance().findObject(killer.objectId) as L2Character?
        if (l2Character != null) getL2Character().doDie(l2Character)
    }

    fun attack(character: Character) {
        val target = L2World.getInstance().findObject(character.objectId) as L2Character?
        if (target != null) getL2Character().doAttack(target)
    }

    fun stopAttack() {
        getL2Character().abortAttack()
    }

    fun castSkill(target: Character, skillId: Int, skillLevel: Int, hitTime: Int, reuseDelay: Int) {
        val l2Character = getL2Character()
        val l2Target = L2World.getInstance().findObject(target.objectId) as L2Character
        l2Character.broadcastPacket(MagicSkillUse(l2Character, l2Target, skillId, skillLevel, hitTime, reuseDelay))
    }

    fun getLocation(): LocationHolder {
        val loc = getL2Character().location
        return LocationHolder(loc.x, loc.y, loc.z, loc.heading, loc.instanceId)
    }

    private fun getL2Character(): L2Character {
        return L2ObjectProvider.find(objectId) as L2Character
    }
}