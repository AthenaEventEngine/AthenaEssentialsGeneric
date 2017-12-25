package com.github.athenaengine.essentials.`interface`

import com.github.athenaengine.essentials.enums.ScoreType
import com.github.athenaengine.essentials.models.holders.EItemHolder
import com.github.athenaengine.essentials.models.packets.GamePacket

interface IParticipant {

    fun getPoints(type: ScoreType): Int

    fun increasePoints(type: ScoreType, points: Int)

    fun giveItems(items: Collection<EItemHolder>)

    fun sendPacket(packet: GamePacket)
}