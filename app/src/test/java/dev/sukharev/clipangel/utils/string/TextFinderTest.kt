package dev.sukharev.clipangel.utils.string

import org.junit.Test

import org.junit.Assert.*

class TextFinderTest {

    val text = """
         Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt 
         ut labore et dolore magna aliqua.
    """.trimIndent()

    @Test
    fun `success text matches`() {
        val needToFind = "sit amet"
        val searchResult = TextFinder.getFirstMatch(needToFind, text)
        assertSame(searchResult, Pair(18,25))
    }

    @Test
    fun `failure text matches`() {
        val needToFind = "extra global"
        val searchResult = TextFinder.getFirstMatch(needToFind, text)
        assertSame(searchResult, null)
    }
}