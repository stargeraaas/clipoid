package dev.sukharev.clipangel.utils.string

import android.text.Spannable
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import junit.framework.Assert.assertSame
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class SpannableCropperTest {

    val text = """
         Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt 
         ut labore et dolore magna aliqua.
    """.trimIndent()

    @Before
    fun setUp() {

    }

    @Test
    fun span() {
        val spannableCropper = SpannableCropper(InstrumentationRegistry.getInstrumentation().context)
        val inputText = "sit amet"
        val textMatches = TextFinder.getFirstMatch(inputText, text)

        val spannable: Spannable? = textMatches?.let {
            spannableCropper.span(inputText, text)
        }

        assertSame(Pair(spannable?.indices?.first, spannable?.indices?.last), textMatches)

    }
}