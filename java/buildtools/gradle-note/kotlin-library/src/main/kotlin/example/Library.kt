package example

data class Language(val name: String, val hotness: Int)

class Library {
    fun kotlinLanguage(): Language = Language("Kotlin", 10)
}