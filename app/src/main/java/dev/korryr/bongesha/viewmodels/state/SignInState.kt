package dev.korryr.bongesha.viewmodels.state

data class SignInStatet(
    val isSignInSuccessful: Boolean = false,
    val signInError: String? = null
)