package com.github.athenaengine.essentials.models.entities.living

open class Playable(objectId: Int) : Character(objectId) {

    open fun isPlayer(): Boolean = false

    open fun isSummon(): Boolean = false
}