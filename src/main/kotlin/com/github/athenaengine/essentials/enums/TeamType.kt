package com.github.athenaengine.essentials.enums

enum class TeamType(val rgb: Int) {

    WHITE(0xFFFFFF),
    RED(0x0000FF),
    BLUE(0xDF0101),
    // Alt
    PINK(0x9393FF),
    ROSE_PINK(0x7C49FC),
    LEMON_YELOOW(0x97F8FC),
    LILAC(0xFA9AEE),
    COBAL_VIOLET(0xFF5D93),
    MINT_GREEN(0x00FCA0),
    PEACOCK_GREEN(0xA0A601),
    YELLOW_OCHRE(0x7898AF),
    CHOCOLATE(0x486295),
    SILVER(0x999999);

    fun getType(name: String): TeamType? {
        for (type in TeamType.values()) {
            if (type.toString().equals(name, ignoreCase = true)) {
                return type
            }
        }

        return null
    }

}