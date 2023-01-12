package com.hiarias.just_teleport.util

import com.mojang.brigadier.exceptions.CommandSyntaxException
import net.minecraft.server.command.ServerCommandSource

fun isPlayer(source: ServerCommandSource): Boolean {
    return try {
        source.player != null
    } catch (e: CommandSyntaxException) {
        false
    }
}
