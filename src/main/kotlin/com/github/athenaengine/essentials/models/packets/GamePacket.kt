package com.github.athenaengine.essentials.models.packets

import com.github.athenaengine.essentials.models.entities.living.Player
import com.github.athenaengine.essentials.providers.L2ObjectProvider
import com.l2jserver.gameserver.model.actor.instance.L2PcInstance
import com.l2jserver.gameserver.network.serverpackets.L2GameServerPacket

abstract class GamePacket {

    open fun send(player: Player) {}

    protected fun send(packet: L2GameServerPacket, receiver: Player) {
        val pc = L2ObjectProvider.find(receiver.objectId) as L2PcInstance
        pc.sendPacket(packet)
    }
}