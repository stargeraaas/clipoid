package dev.sukharev.clipangel.domain.models


sealed class Result<out T> {

    sealed class Success<T> : Result<T>() {

        abstract val value: T

        override fun toString() = "Success($value)"

        class Value<T>(override val value: T) : Success<T>()

        object Empty : Success<Nothing>() {

            override val value: Nothing get() = error("No value")

            override fun toString() = "Success"
        }
    }

    sealed class Failure<E : Throwable>(open val error: E? = null) : Result<Nothing>() {

        override fun toString() = "Failure($error)"

        class Error(override val error: Throwable?) : Failure<Throwable>(error)
    }

}

typealias EmptyResult = Result<Nothing>



