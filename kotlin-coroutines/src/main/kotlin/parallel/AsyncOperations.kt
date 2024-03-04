package parallel

import common.enableCoroutineLogging
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.runBlocking
import mu.KotlinLogging
import kotlin.system.measureTimeMillis


fun main(args: Array<String>) {
  AsyncOperations().compute()
}

class AsyncOperations {

  private val log = KotlinLogging.logger { }

  fun compute() {
    enableCoroutineLogging()

    runBlocking {
      twoTasks()
      twoTasksAsync()
    }
  }

  private fun twoTasks() {
    measureTimeMillis {
      fibonacci(1_000_000_000)
      fibonacci(1_000_000_001)
    }.apply {
      log.info("Sync took $this ms")
    }
  }

  private suspend fun twoTasksAsync() = coroutineScope {
    measureTimeMillis {
      val first = async(Dispatchers.Default) { fibonacci(1_000_000_000) }
      val second = async(Dispatchers.Default) { fibonacci(1_000_000_001) }
      awaitAll(first, second)
    }.apply {
      log.info("Async took $this ms")
    }
  }


  private fun fibonacci(iterations: Int): Long {
    var f1 = 0L
    var f2 = 1L

    log.info("Calculating $iterations fibonacci iterations")

    repeat(iterations) {
      val sum = f1 + f2
      f1 = f2
      f2 = sum
    }

    return f2
  }

}
