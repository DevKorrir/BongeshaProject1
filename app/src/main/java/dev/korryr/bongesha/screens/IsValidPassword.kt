package dev.korryr.bongesha.screens


fun yisValidPassword(
    password: String
): Boolean {
    val passwordPattern = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=!])(?=\\S+$).{8,}$"
    val pattern = Regex(passwordPattern)
    return pattern.matches(password)
}

fun isValidPassword(password: String): Boolean {
    // Check for minimum length, number, uppercase, lowercase, and special character
    return password.length >= 8 &&
            password.any { it.isDigit() } &&
            password.any { it.isUpperCase() } &&
            password.any { it.isLowerCase() } &&
            password.any { !it.isLetterOrDigit() }
}
