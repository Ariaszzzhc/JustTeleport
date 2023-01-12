package com.hiarias.just_teleport

import com.hiarias.just_teleport.util.PermUtils
import com.hiarias.just_teleport.util.isPlayer
import net.minecraft.command.argument.EntityArgumentType
import net.minecraft.server.command.CommandManager
import net.minecraft.text.Text
import net.minecraft.util.Formatting
import org.quiltmc.loader.api.ModContainer
import org.quiltmc.qkl.library.brigadier.execute
import org.quiltmc.qkl.library.brigadier.register
import org.quiltmc.qsl.base.api.entrypoint.ModInitializer
import org.quiltmc.qsl.command.api.CommandRegistrationCallback
import org.slf4j.Logger
import org.slf4j.LoggerFactory

object JustTeleportMod : ModInitializer {
    val LOGGER: Logger = LoggerFactory.getLogger("JustTeleport Mod")

    override fun onInitialize(mod: ModContainer) {
        LOGGER.info("Hello Quilt world from {}!", mod.metadata()?.name())

        CommandRegistrationCallback.EVENT.register { dispatcher, _, _ ->
            // tpa
            dispatcher.register(
                CommandManager.literal("tpa").requires(::isPlayer)
                    .requires(PermUtils.require("just_teleport.tpa", true))
                    .then(CommandManager.argument("target", EntityArgumentType.player()))
                    .executes { context ->
                        val player = context.source.player!!
//                        val server = player.server
                        val target = EntityArgumentType.getPlayer(context, "target")

                        // check player
                        if (player == target) {
                            target.sendMessage(
                                Text.literal("You cannot request for you to teleport to yourself!")
                                    .formatted(Formatting.RED), false
                            )
                        }

                        // TODO: cold down check

                        target.sendMessage(
                            Text.literal(player.name.string).formatted(Formatting.GREEN).append(
                                Text.literal(" has requested to teleport to you. Type ").formatted(Formatting.GOLD)
                            ).append(
                                Text.literal("/tpaccept").formatted(Formatting.YELLOW)
                            ).append(Text.literal(" to accept.").formatted(Formatting.GOLD)), false
                        )
                        player.sendMessage(
                            Text.literal("Requested to teleport to ").formatted(Formatting.GOLD)
                                .append(Text.literal(target.name.string).formatted(Formatting.GREEN))
                                .append(Text.literal(".").formatted(Formatting.GOLD)), false
                        )

                        return@executes 1
                    }
            )


            // tpaccept
            dispatcher.register(
                CommandManager.literal("tpaccept").requires(::isPlayer)
                    .requires(PermUtils.require("just_teleport.tpa", true))
                    .executes { context ->


                        return@executes 1
                    }
            )
        }
    }
}
