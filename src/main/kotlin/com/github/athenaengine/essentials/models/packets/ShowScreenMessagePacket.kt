package com.github.athenaengine.essentials.models.packets

import com.github.athenaengine.essentials.models.entities.living.Player
import com.l2jserver.gameserver.network.serverpackets.ExShowScreenMessage

class ShowScreenMessagePacket(val text: String, val time: Int) : GamePacket() {

    override fun send(player: Player) {
        send(ExShowScreenMessage(text, time), player)
    }
}