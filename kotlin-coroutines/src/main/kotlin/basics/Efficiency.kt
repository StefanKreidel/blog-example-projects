package basics

import common.enableCoroutineLogging
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import mu.KotlinLogging

fun main(args: Array<String>) {
  enableCoroutineLogging()
  val log = KotlinLogging.logger { }

  log.info("Heating up...")

  runBlocking {
    repeat(100_000) {
      launch {
        delay(1000)
      }
    }
    log.info("Created 100.000 coroutines!")
  }

  log.info("Completed.")

}
