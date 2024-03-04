package suspend

import common.enableCoroutineLogging
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.suspendCancellableCoroutine
import mu.KotlinLogging
import java.util.concurrent.CompletableFuture
import java.util.concurrent.CompletionStage


fun main(args: Array<String>) {
  Suspend().compute()
}

class Suspend {

  private val log = KotlinLogging.logger { }

  fun compute() {
    enableCoroutineLogging()

    log.info(executeBlocking())
    runBlocking {
      log.info(executeSuspending())
    }
  }

  private fun executeBlocking(): String {
    val future = CompletableFuture.supplyAsync {
      Thread.sleep(1000)
      log.info("blocking operation complete")
      return@supplyAsync "blocking result"
    }

    log.info("executing future")
    return future.get()
  }

  private suspend fun executeSuspending(): String {
    val future = CompletableFuture.supplyAsync {
      Thread.sleep(1000)
      log.info("blocking operation complete")
      return@supplyAsync "suspending result"
    }

    log.info("suspending function")
    return future.awaitSuspending()
  }

  @OptIn(ExperimentalCoroutinesApi::class)
  suspend fun <T> CompletionStage<T>.awaitSuspending(): T {
    val future = this.toCompletableFuture()

    return suspendCancellableCoroutine { continuation ->
      continuation.resume(future.get()) { throwable ->
        log.error("future could not be executed because `${throwable.message}`")
      }
    }
  }

}
