package ch11

fun DIV.dropdownButton(block: BUTTON.() -> Unit) {
    button(block)
}

fun DIV.dropdownMenu(block: UL.() -> Unit) {
    ul(block)
}

fun UL.dropdownHeader(text: String) {
    li { /*...*/ }
}

fun UL.divider() {
    li { /*...*/ }
}

fun UL.item(href: String, name: String) {
    li {
        a(href) { /*...*/ }
    }
}