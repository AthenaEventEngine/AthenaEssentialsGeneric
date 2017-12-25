package com.github.athenaengine.essentials.models.packets

import com.github.athenaengine.essentials.models.entities.living.Player
import com.l2jserver.gameserver.network.serverpackets.CreatureSay

class CreatureSayPacket(val objectId: Int, val messageValue: Int,
                        val charName: String, val message: String) : GamePacket() {

    override fun send(player: Player) {
        send(CreatureSay(objectId, messageValue, charName, message), player)
    }
}