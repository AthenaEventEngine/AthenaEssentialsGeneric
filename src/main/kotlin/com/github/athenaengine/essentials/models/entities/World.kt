package com.github.athenaengine.essentials.models.entities

import com.github.athenaengine.essentials.models.entities.living.Player
import com.l2jserver.gameserver.instancemanager.InstanceManager
import com.l2jserver.gameserver.model.instancezone.InstanceWorld

class World(instanceFile: String) {

    lateinit var instanceWorld: InstanceWorld

    init {
        try {
            val instanceId = InstanceManager.getInstance().createDynamicInstance(instanceFile)

            InstanceManager.getInstance().getInstance(instanceId).setAllowSummon(false)
            InstanceManager.getInstance().getInstance(instanceId).isPvPInstance = true
            InstanceManager.getInstance().getInstance(instanceId).ejectTime = 10 * 60 * 1000 // Prevent eject death players
            InstanceManager.getInstance().getInstance(instanceId).setEmptyDestroyTime(1000 + 60000L)

            // We closed the doors of the instance if there
            for (door in InstanceManager.getInstance().getInstance(instanceId).doors) door.closeMe()
            val world = InstanceWorld()

            world.instanceId = instanceId
            world.templateId = 100 // TODO hardcode
            world.status = 0

            InstanceManager.getInstance().addWorld(world)
            instanceWorld = world
        } catch (e: Exception) {
            //LOGGER.warning(EventEngineManager::class.java!!.getSimpleName() + ": createNewInstanceWorld() " + e)
            e.printStackTrace()
        }
    }

    fun getId(): Int = instanceWorld.instanceId

    fun addPlayer(player: Player) = instanceWorld.addAllowed(player.objectId)

    fun destroy() = InstanceManager.getInstance().destroyInstance(instanceWorld.instanceId)
}