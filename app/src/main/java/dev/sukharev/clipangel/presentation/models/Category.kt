package dev.sukharev.clipangel.presentation.models

// Clip's category
sealed class Category(val id: Int) {
    class All: Category(1)
    class Favorite: Category(2)
    class Private: Category(3)

    companion object {
        fun getById(id: Int): Category? = when (id) {
            1 -> All()
            2 -> Favorite()
            3 -> Private()
            else -> null
        }
    }


}


