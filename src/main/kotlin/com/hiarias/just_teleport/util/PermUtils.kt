package com.hiarias.just_teleport.util

import me.lucko.fabric.api.permissions.v0.Permissions
import net.minecraft.server.command.ServerCommandSource
import org.quiltmc.loader.api.QuiltLoader
import java.util.function.Predicate

object PermUtils {
    private val IS_FABRIC_PERMISSION_API_LOADED = QuiltLoader.isModLoaded("fabric-permissions-api-v0")
    fun require(permission: String, defaultValue: Boolean): Predicate<ServerCommandSource> {
        if (IS_FABRIC_PERMISSION_API_LOADED) {
            return requireImpl(permission, defaultValue)
        }

        return Predicate<ServerCommandSource> { defaultValue }
    }

    fun require(permission: String, opLevel: Int): Predicate<ServerCommandSource> {
        if (IS_FABRIC_PERMISSION_API_LOADED) {
            return requireImpl(permission, opLevel)
        }

        return Predicate<ServerCommandSource> { player -> player.hasPermissionLevel(opLevel) }
    }
    fun check(source: ServerCommandSource, permission: String, fallback: Boolean): Boolean {
        if (IS_FABRIC_PERMISSION_API_LOADED) {
            return checkImpl(source, permission, fallback)
        }

        return fallback
    }

    private fun requireImpl(permission: String, defaultValue: Boolean): Predicate<ServerCommandSource> {
        return Permissions.require(permission, defaultValue)
    }

    private fun requireImpl(permission: String, opLevel: Int): Predicate<ServerCommandSource> {
        return Permissions.require(permission, opLevel)
    }

    private fun checkImpl(source: ServerCommandSource, permission: String, fallback: Boolean): Boolean {
        return Permissions.check(source, permission, fallback)
    }
}
