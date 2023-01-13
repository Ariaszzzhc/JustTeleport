package com.hiarias.omniteleport

import com.hiarias.omniteleport.util.PermUtils
import com.hiarias.omniteleport.util.isPlayer
import com.mojang.brigadier.context.CommandContext
import net.minecraft.command.argument.EntityArgumentType
import net.minecraft.entity.ai.brain.Schedule
import net.minecraft.server.command.CommandManager
import net.minecraft.server.command.ServerCommandSource
import net.minecraft.server.network.ServerPlayerEntity
import net.minecraft.text.Text
import net.minecraft.util.Formatting
import org.quiltmc.loader.api.ModContainer
import org.quiltmc.qsl.base.api.entrypoint.ModInitializer
import org.quiltmc.qsl.command.api.CommandRegistrationCallback
import org.quiltmc.qsl.lifecycle.api.event.ServerTickEvents
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.util.LinkedList

object OmniTeleport : ModInitializer {
    val LOGGER: Logger = LoggerFactory.getLogger("JustTeleport Mod")

    private val activeRequests = LinkedList<TeleportRequest>()

    override fun onInitialize(mod: ModContainer) {
        LOGGER.info("Hello Quilt world from {}!", mod.metadata()?.name())

        ServerTickEvents.START.register { server ->
            Scheduler.saveServerTick(server)

            Scheduler.schedule()
        }

        CommandRegistrationCallback.EVENT.register { dispatcher, _, _ ->
            // tpa
            dispatcher.register(
                CommandManager.literal("tpa").requires(::isPlayer)
                    .requires(PermUtils.require("omniteleport.tpa", true))
                    .then(CommandManager.argument("target", EntityArgumentType.player())
                        .executes { context ->
                            val player = context.source.player!!
                            val target = EntityArgumentType.getPlayer(context, "target")

                            // check player
                            if (player == target) {
                                target.sendMessage(
                                    Text.literal("You cannot request for you to teleport to yourself!")
                                        .formatted(Formatting.RED), false
                                )
                            }

                            if (activeRequests.any {
                                    it.initiator == player
                                }) {
                                player.sendMessage(
                                    Text.literal("There is already an ongoing request like this!")
                                        .formatted(Formatting.RED), false
                                )

                                return@executes 1
                            }

                            val request = TeleportRequest(player, target, player)
                            activeRequests.add(request)
                            Scheduler.scheduleDelayedTask(100) {
                                activeRequests.remove(request)

                                request.run {
                                    initiator.sendMessage(
                                        Text.literal("Your teleport request to ").formatted(Formatting.RED).append(
                                            Text.literal(destination.entityName).formatted(Formatting.GREEN)
                                        ).append(
                                            Text.literal(" has timed out!")
                                                .formatted(Formatting.RED)
                                        ), false
                                    )

                                    destination.sendMessage(
                                        Text.literal("Teleport request from ").formatted(Formatting.RED)
                                            .append(Text.literal(source.entityName).formatted(Formatting.GREEN)).append(
                                                Text.literal(" has timed out!")
                                                    .formatted(Formatting.RED)
                                            ), false
                                    )
                                }
                            }

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
                    ))


            // tpaccept
            dispatcher.register(
                CommandManager.literal("tpaccept").requires(::isPlayer)
                    .requires(PermUtils.require("omniteleport.tpaccept", true))
                    .then(CommandManager.argument("target", EntityArgumentType.player())
                        .executes { context ->
                            val player = context.source.player!!
                            val target = EntityArgumentType.getPlayer(context, "target")



                            return@executes 1
                        }
                    ).executes {

                        return@executes 1
                    }
            )
        }
    }

//    private fun tpAccept(context: CommandContext<ServerCommandSource>, target: ServerPlayerEntity?) {
//        if (target != null) {
//
//        }
//    }
}
