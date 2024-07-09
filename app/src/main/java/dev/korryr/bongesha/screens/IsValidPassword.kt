package dev.korryr.bongesha.screens


fun isValidPassword(password: String): Boolean {
    val passwordRegex = Regex("^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[!@#\$%^&*(),.?\":{}|<>]).{8,}\$")
    return passwordRegex.matches(password)
}