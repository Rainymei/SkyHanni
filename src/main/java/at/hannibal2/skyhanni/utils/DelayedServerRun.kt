package at.hannibal2.skyhanni.utils

import at.hannibal2.skyhanni.test.command.ErrorManager
import at.hannibal2.skyhanni.utils.ServerTimeMark.Companion.fromServerNow
import at.hannibal2.skyhanni.utils.collection.CollectionUtils.drainTo
import java.util.concurrent.ConcurrentLinkedQueue
import kotlin.time.Duration

object DelayedServerRun {

    private val tasks = mutableListOf<Pair<() -> Any, ServerTimeMark>>()
    private val futureTasks = ConcurrentLinkedQueue<Pair<() -> Any, ServerTimeMark>>()

    fun runDelayed(duration: Duration, run: () -> Unit): ServerTimeMark {
        val time = duration.fromServerNow()
        futureTasks.add(run to time)
        return time
    }

    fun <T> runDelayedReturning(duration: Duration, run: () -> T): Pair<ServerTimeMark, () -> T> {
        val time = duration.fromServerNow()
        val runnable = { run() }
        @Suppress("UNCHECKED_CAST")
        futureTasks.add((runnable as () -> Any) to time)
        return time to runnable
    }

    fun checkRuns() {
        tasks.removeIf { (runnable, time) ->
            val inPast = time.isInPast()
            if (inPast) {
                try {
                    runnable()
                } catch (e: Exception) {
                    ErrorManager.logErrorWithData(e, "DelayedServerRun task crashed while executing: ${e.message}")
                }
            }
            inPast
        }
        futureTasks.drainTo(tasks)
    }
}
