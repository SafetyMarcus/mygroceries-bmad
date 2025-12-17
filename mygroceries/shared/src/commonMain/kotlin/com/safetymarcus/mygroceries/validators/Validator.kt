package com.safetymarcus.mygroceries.validators

sealed class Validator {
    object Valid : Validator()
    data class Invalid(val message: String) : Validator()

    operator fun plus(other: Validator) = when {
        this is Valid -> other
        other is Valid -> this
        this is Invalid && other is Invalid -> Invalid(
            message = "${this.message}\n${other.message}"
        )
        else -> throw UnsupportedOperationException("Bad state")
    }
}
