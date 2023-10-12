package common

fun enableCoroutineLogging() {
  System.getProperties().setProperty("kotlinx.coroutines.debug", "on")
}
