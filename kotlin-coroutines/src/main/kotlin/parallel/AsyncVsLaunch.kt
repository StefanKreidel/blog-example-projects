package parallel

import common.enableCoroutineLogging
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.Job
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.job
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import mu.KotlinLogging

fun main(args: Array<String>) {
  LimitedParallelism().compute()
}

class AsyncVsLaunch {

  private val log = KotlinLogging.logger { }

  fun compute() {
    enableCoroutineLogging()

    runBlocking {
      asyncWay()
      launchWay()
    }
  }

  private suspend fun asyncWay() = coroutineScope {
    val result: Deferred<String> = async {
      delay(100)
      "Completed async()"
    }

    val asyncJob: Job = result.job
    log.info("Cancelled async(): ${asyncJob.isCancelled}")

    log.info(result.await())
  }

  private suspend fun launchWay() = coroutineScope {
    val job: Job = launch {
      delay(100)
      log.info("Completed launch()")
    }

    log.info("Cancelled launch(): ${job.isCancelled}")

    job.join()
  }

}
