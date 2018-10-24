package ch04.ex4_2_2_CompanionObjectsAPlaceForFactoryMethodsAndStaticMembers1

fun getFacebookName(accountId: Int) = "fb:$accountId"

class User private constructor(val nickname: String){
    companion object {
        fun newSubscribingUser(email: String) =
                User(email.substringBefore('@'))

        fun newFacebookUser(accountId: Int) =
                User(getFacebookName(accountId))
    }
}

fun main(args: Array<String>){
    val subscribingUser = User.newSubscribingUser("seope@gmail.com")
    val facebookUser = User.newFacebookUser(4)
    println(subscribingUser.nickname)
}