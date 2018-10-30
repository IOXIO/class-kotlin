fun createAllConeRunnable():Runnable {
  return Runnable { println("All done!") }
}

fun main(args: Array<String>) {
  createAllConeRunnable().run()
}