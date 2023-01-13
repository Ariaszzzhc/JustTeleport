package com.hiarias.omniteleport

import net.minecraft.server.network.ServerPlayerEntity

data class TeleportRequest(
    val source: ServerPlayerEntity,
    val destination: ServerPlayerEntity,
    val initiator: ServerPlayerEntity,
)
