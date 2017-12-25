package com.github.athenaengine.essentials.providers

import com.l2jserver.gameserver.model.L2World

class L2ObjectProvider {
    companion object {
        fun find(objectId: Int) : Any {
            return L2World.getInstance().findObject(objectId)
        }
    }
}