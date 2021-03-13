package dev.sukharev.clipangel.presentation.models

// Clip's category
sealed class Category {
    class All: Category()
    class Favorite: Category()
    class Private: Category()
}
