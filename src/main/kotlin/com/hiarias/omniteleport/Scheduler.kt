package com.hiarias.omniteleport

import net.minecraft.server.MinecraftServer
import java.util.concurrent.ForkJoinPool

object Scheduler {
    private var currentTicks = 0
    private val tasks = LinkedHashMap<Int, MutableList<Runnable>>()

    fun saveServerTick(server: MinecraftServer) {
        currentTicks = server.ticks
    }

    fun schedule() {
        val ts = tasks[currentTicks]

        ts?.forEach {
            it.run()
        }

        tasks.remove(currentTicks)
    }

    fun scheduleDelayedTask(timeout: Int = 0, runnable: Runnable) {
        val targetTicks = currentTicks + timeout

        tasks.compute(targetTicks) { _, value ->
            val newValue = value ?: mutableListOf()

            newValue.apply {
                add(runnable)
            }
        }
    }

//    fun scheduleAsyncDelayedTask(timeout: Int = 0, runnable: Runnable) {
//        val asyncTask = Runnable {
//            ForkJoinPool.commonPool().execute(runnable)
//        }
//
//        scheduleDelayedTask(timeout + 1, asyncTask)
//    }
}
