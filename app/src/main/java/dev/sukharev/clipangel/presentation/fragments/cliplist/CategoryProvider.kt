package dev.sukharev.clipangel.presentation.fragments.cliplist

import dev.sukharev.clipangel.presentation.models.Category

interface CategoryProvider {
    fun currentCategory(): Category
}