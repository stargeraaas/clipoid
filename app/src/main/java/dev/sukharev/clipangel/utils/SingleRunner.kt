package dev.sukharev.clipangel.utils

class SingleRunner(private val f: () -> Unit) {

    private var isRun = false

    fun run() {
        if (!isRun)
            f.invoke()
        isRun = true
    }
}