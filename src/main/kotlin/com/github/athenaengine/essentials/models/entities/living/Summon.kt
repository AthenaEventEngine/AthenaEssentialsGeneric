package com.github.athenaengine.essentials.models.entities.living

import com.l2jserver.gameserver.model.L2World
import com.l2jserver.gameserver.model.actor.L2Summon

class Summon(objectId: Int) : Playable(objectId) {

    override fun isSummon(): Boolean = true

    fun getOwner(): Player? = Player(getSummon()!!.owner.objectId)

    fun unsummon() = getSummon()!!.unSummon(getSummon()!!.owner)

    fun returnToOwner() = getSummon()!!.followOwner()

    private fun getSummon(): L2Summon? {
        return L2World.getInstance().findObject(objectId) as L2Summon
    }
}