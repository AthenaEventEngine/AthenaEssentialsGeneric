package com.github.athenaengine.essentials.models.entities.living

import com.github.athenaengine.essentials.models.holders.LocationHolder
import com.github.athenaengine.essentials.providers.L2ObjectProvider
import com.l2jserver.gameserver.model.L2World
import com.l2jserver.gameserver.model.Location
import com.l2jserver.gameserver.model.actor.L2Npc

abstract class Npc(objectId: Int) : LivingEntity(objectId) {

    fun getTemplateId() : Int {
        return getL2Npc().id
    }

    fun moveTo(location: LocationHolder, offset: Int) {
        getL2Npc().moveToLocation(location.x, location.y, location.z, offset)
    }

    fun moveTo(location: LocationHolder) {
        moveTo(location, 0)
    }

    fun spawn() {
        getL2Npc().spawn
    }

    fun getSpawn(): LocationHolder? {
        val location = getL2Npc().spawn.location

        if (location != null) {
            return LocationHolder(location.x, location.y, location.z, location.heading, location.instanceId)
        }

        return null
    }

    fun setSpawn(location: LocationHolder) {
        if (getL2Npc().spawn != null) {
            val newLoc = Location(location.x, location.y, location.z, location.heading, location.instanceId)
            getL2Npc().spawn.location = newLoc
        }
    }

    fun talkTo(player: Player, message: String) {
        val pcInstance = L2World.getInstance().getPlayer(player.objectId)
        if (pcInstance != null) pcInstance.sendMessage(message)
    }

    fun stopRespawn() {
        val npc = getL2Npc()
        if (npc.spawn != null) npc.spawn.stopRespawn()
    }

    fun deleteMe() {
        val npc = getL2Npc()
        npc.deleteMe()
    }

    private fun getL2Npc() : L2Npc {
        return L2ObjectProvider.find(objectId) as L2Npc
    }
}