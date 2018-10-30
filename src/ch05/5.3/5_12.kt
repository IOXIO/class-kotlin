fun main(args: Array<String>) {
  val naturalNumbers = generateSequence(0) { it + 1 }
  val numbersTo100 = naturalNumbers.takeWhile { it <= 100 }
  println(numbersTo100.sum())
}