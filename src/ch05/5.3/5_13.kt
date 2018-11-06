fun File.isInsideHiddenDirectory() = generateSequence(this) { it.parentFile }.any { it.isHidden }

fun main(args: Array<String>) {
  val fil = File("/Users/svtk/.HiddenDir/a.txt")
  println(file.isInsideHiddenDirectory())
}