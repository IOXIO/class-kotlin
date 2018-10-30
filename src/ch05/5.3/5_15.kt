val listener = OnClickListener { veiw ->
  val text = when (view.id) {
      R.id.button1 -> "First button"
      R.id.button2 -> "Second Button"
      else -> "Unknown button"
  }
  toast(text)
}

fun main(args: Array<String>) {
  button1.setOnClickListener(listener)
  button2.setOnClickListener(listener)
}