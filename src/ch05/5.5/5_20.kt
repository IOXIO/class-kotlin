fun createViewWithCustormAttributes(context: Context) = {
  TestView(context).apply {
    text = "Sample Text"
    textSize = 20.0
    stPadding(10, 0, 0, 0)
  }
}