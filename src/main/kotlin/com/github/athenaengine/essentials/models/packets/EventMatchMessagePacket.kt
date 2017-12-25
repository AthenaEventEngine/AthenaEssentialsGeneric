package com.github.athenaengine.essentials.models.packets

import com.github.athenaengine.essentials.models.entities.living.Player
import com.l2jserver.gameserver.network.serverpackets.ExEventMatchMessage

class EventMatchMessagePacket(val type: Int, val msg: String) : GamePacket() {

    override fun send(player: Player) {
        send(ExEventMatchMessage(type, msg), player)
    }
}