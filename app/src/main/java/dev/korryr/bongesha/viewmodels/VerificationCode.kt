package dev.korryr.bongesha.viewmodels

import com.google.firebase.firestore.FirebaseFirestore
import dev.korryr.bongesha.commons.VerificationCode

fun generateVerificationCode(): String {
    val charset = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ"
    return (1..6)
        .map { charset.random() }
        .joinToString("")
}

fun sendVerificationEmail(email: String, code: String) {
    // Use an email service to send the email
    val subject = "Verify your email address"
    val message = """
        Hello,

        Use the following code to verify your email address: $code

        If you didn’t ask to verify this address, you can ignore this email.

        Thanks,
        Your Bongesha team
    """.trimIndent()

    // Use an email service or SMTP to send the email
    // Example: EmailService.send(email, subject, message)
    //sendEmail(email, subject,message)
}

fun storeVerificationCode(uid: String, email: String, code: String) {
    val firestore = FirebaseFirestore.getInstance()
    val expiryTime = System.currentTimeMillis() + 15 * 60 * 1000 // Code expires in 15 minutes
    val verificationCode = VerificationCode(email, code, expiryTime)

    firestore.collection("verificationCodes")
        .document(uid)
        .set(verificationCode)
        .addOnSuccessListener {
            // Handle success
        }
        .addOnFailureListener { e ->
            // Handle failure
        }
}
