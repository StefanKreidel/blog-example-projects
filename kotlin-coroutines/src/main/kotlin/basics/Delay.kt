package basics

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import mu.KLogger
import mu.KotlinLogging
import kotlin.concurrent.thread

fun main(args: Array<String>) {
  enableCoroutineLogging()
  val log = KotlinLogging.logger { }

  coroutines(log)
  threads(log)
}

private fun coroutines(log: KLogger) {
  log.info("The power of coroutines")

  runBlocking {
    async(Dispatchers.Default) {
      delay(1000)
      log.info("first done")
    }
    async(Dispatchers.IO) {
      delay(1000)
      log.info("second done")
    }
  }

  log.info("complete")
}

fun threads(log: KLogger) {
  log.info("Compared to plain old threads")

  thread {
    Thread.sleep(1000)
    log.info("thread done")
  }.join()

  log.info("complete")
}
