package ch11

import ch04.ex4_2_1_CompanionObjectsAPlaceForFactoryMethodsAndStaticMembers.A

open class Tag

class HTML private constructor(): Tag() {
    fun table(init: TABLE.() -> Unit) {
        /*...*/
    }

    fun dropdown(init: DIV.() -> Unit) {
        /*...*/
    }

    companion object {
        fun newTemplate(): HTML = HTML()
    }
}

class TABLE : Tag() {
    fun tr(init: TR.() -> Unit) {
        /*...*/
    }
}

class TR : Tag() {
    fun td(init: TD.() -> Unit) {
        /*...*/
    }
}

class TD : Tag()

class DIV : Tag() {
    fun button(init: BUTTON.() -> Unit) {
        /*...*/
    }

    fun ul(init: UL.() -> Unit) {
        /*...*/
    }
}

class BUTTON : Tag()

class UL : Tag() {
    fun li(init: LI.() -> Unit) {
        /*...*/
    }
}

class LI : Tag() {
    fun a(href: String, init: A.() -> Unit) {
        /*...*/
    }
}

class A(val href: String) : Tag()

fun createHTML(): HTML = HTML.newTemplate()
