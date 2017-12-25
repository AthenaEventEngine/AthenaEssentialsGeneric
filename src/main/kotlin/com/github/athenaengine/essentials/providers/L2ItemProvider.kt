package com.github.athenaengine.essentials.providers

import com.github.athenaengine.essentials.models.entities.Item
import com.github.athenaengine.essentials.models.entities.living.Player
import com.l2jserver.gameserver.datatables.ItemTable
import com.l2jserver.gameserver.model.L2World
import com.l2jserver.gameserver.model.actor.instance.L2PcInstance
import com.l2jserver.gameserver.model.items.instance.L2ItemInstance

class L2ItemProvider {
    companion object {

        fun createItem(id: Int, player: Player): Item {
            val l2Item = ItemTable.getInstance().createItem("", id, 1,
                    L2ObjectProvider.find(player.objectId) as L2PcInstance, null)
            return Item(l2Item.objectId)
        }

        private fun getL2Item(item: Item): L2ItemInstance {
            return L2World.getInstance().findObject(item.objectId) as L2ItemInstance
        }
    }
}