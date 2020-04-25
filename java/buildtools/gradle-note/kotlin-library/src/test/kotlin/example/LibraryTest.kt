package example

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class LibraryTest {
    @Test
    fun testLanguage() {
        assertEquals("Kotlin", Libary().kotlinLanguage().name)
    }
}
