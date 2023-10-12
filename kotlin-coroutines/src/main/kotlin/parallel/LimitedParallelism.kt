package parallel

import common.enableCoroutineLogging
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.sync.Semaphore
import kotlinx.coroutines.sync.withPermit
import mu.KotlinLogging
import kotlin.random.Random
import kotlin.system.measureTimeMillis

fun main(args: Array<String>) {
  LimitedParallelism().compute()
}

@OptIn(ExperimentalCoroutinesApi::class)
class LimitedParallelism {

  private val log = KotlinLogging.logger { }

  fun compute() {
    enableCoroutineLogging()

    runBlocking {
      measureTimeMillis {
        fetchResults()
      }.apply {
        log.info("Completed async in $this ms")
      }

      measureTimeMillis {
        fetchResultsLimited()
      }.apply {
        log.info("Completed limited async in $this ms")
      }
    }
  }

  private suspend fun fetchResults(): List<String> = coroutineScope {
    getKeys()
      .map { key ->
        async(Dispatchers.Default) { hashKey(key) }
      }
      .awaitAll()
  }

  private suspend fun fetchResultsLimited(): List<String> = coroutineScope {
    val coroutineLimiter = Semaphore(10)

    getKeys()
      .map { key ->
        async(Dispatchers.Default) {
          coroutineLimiter.withPermit { hashKey(key) }
        }
      }
      .awaitAll()
  }

  private fun getKeys(): List<String> {
    return (1..100).map {
      Random(it).toString()
    }
  }

  private suspend fun hashKey(key: String): String {
    delay(100)
    return key.hashCode().toString()
  }

}
