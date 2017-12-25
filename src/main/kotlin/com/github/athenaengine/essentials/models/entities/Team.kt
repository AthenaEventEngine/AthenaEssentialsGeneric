package com.github.athenaengine.essentials.models.entities

import com.github.athenaengine.essentials.`interface`.IParticipant
import com.github.athenaengine.essentials.enums.ScoreType
import com.github.athenaengine.essentials.enums.TeamType
import com.github.athenaengine.essentials.models.entities.living.Player
import com.github.athenaengine.essentials.models.holders.EItemHolder
import com.github.athenaengine.essentials.models.holders.LocationHolder
import com.github.athenaengine.essentials.models.packets.GamePacket
import com.l2jserver.util.Rnd
import java.util.ArrayList
import java.util.concurrent.ConcurrentHashMap

class Team(val name: String, val type: TeamType, var spawns: ArrayList<LocationHolder>) : IParticipant {

    val players = ArrayList<Player>()
    val points = ConcurrentHashMap<ScoreType, Int>()
    var instanceId: Int = 0


    fun getRndSpawn(): LocationHolder? = if (spawns.isEmpty()) null else spawns.get(Rnd.get(spawns.size - 1))

    fun setInstance(id: Int) {
        instanceId = id
        spawns.forEach{ spawn -> spawn.instanceId = id }
    }

    override fun getPoints(type: ScoreType): Int = points[type] ?: 0

    override fun increasePoints(type: ScoreType, points: Int) {
        if (!this.points.containsKey(type)) this.points.put(type, 0)
        this.points.put(type, getPoints(type) + points)
    }

    override fun giveItems(items: Collection<EItemHolder>) = players.forEach{ player -> player.giveItems(items) }

    override fun sendPacket(packet: GamePacket) = players.forEach{ player -> packet.send(player) }

}