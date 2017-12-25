package com.github.athenaengine.essentials.models.entities.living

import com.l2jserver.gameserver.model.L2World
import com.l2jserver.gameserver.model.actor.instance.L2MonsterInstance

class Monster(objectId: Int) : Npc(objectId) {

    fun getBoss(): Monster? = Monster(getMonster().leader!!.objectId)

    fun getMinions(): Collection<Monster>? {
        return getMonster().minionList.spawnedMinions!!.map { monster -> Monster(monster.objectId) }
    }

    private fun getMonster(): L2MonsterInstance {
        return L2World.getInstance().findObject(objectId) as L2MonsterInstance
    }
}