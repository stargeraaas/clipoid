package dev.sukharev.clipangel.utils.string

object TextFinder {

    fun getFirstMatch(input: String, text: String): Pair<Int, Int>? {
        val regex = Regex(input, setOf(RegexOption.MULTILINE, RegexOption.IGNORE_CASE))
        return regex.find(text)?.let {
            if (it.groups.isNotEmpty())
                Pair(it.range.first, it.range.last + 1)
            else
                null
        }
    }
}