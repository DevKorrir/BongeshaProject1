//package dev.korryr.bongesha.screens
//
//import android.widget.Toast
//import androidx.compose.foundation.layout.Column
//import androidx.compose.foundation.layout.Spacer
//import androidx.compose.foundation.layout.fillMaxWidth
//import androidx.compose.foundation.layout.height
//import androidx.compose.foundation.layout.padding
//import androidx.compose.foundation.text.KeyboardOptions
//import androidx.compose.material3.Button
//import androidx.compose.material3.CircularProgressIndicator
//import androidx.compose.material3.OutlinedTextField
//import androidx.compose.material3.Text
//import androidx.compose.runtime.Composable
//import androidx.compose.runtime.LaunchedEffect
//import androidx.compose.runtime.collectAsState
//import androidx.compose.runtime.getValue
//import androidx.compose.runtime.mutableStateOf
//import androidx.compose.runtime.remember
//import androidx.compose.runtime.setValue
//import androidx.compose.ui.Alignment
//import androidx.compose.ui.Modifier
//import androidx.compose.ui.graphics.Color
//import androidx.compose.ui.platform.LocalContext
//import androidx.compose.ui.text.input.KeyboardType
//import androidx.compose.ui.unit.dp
//import androidx.compose.ui.unit.sp
//import com.google.firebase.firestore.FirebaseFirestore
//import dev.korryr.bongesha.commons.BongaButton
//import dev.korryr.bongesha.ui.theme.orange28
//import dev.korryr.bongesha.viewmodels.AuthState
//import dev.korryr.bongesha.viewmodels.AuthViewModel
//
//@Composable
//fun VerifyEmailScreen(
//    userId: String,
//    onVerified: () -> Unit,
//    authViewModel: AuthViewModel
//) {
//    val authState by authViewModel.authState.collectAsState()
//    var code by remember { mutableStateOf("") }
//    val context = LocalContext.current
//    val firestore = FirebaseFirestore.getInstance()
//
//    Column(
//        modifier = Modifier.padding(16.dp),
//        horizontalAlignment = Alignment.CenterHorizontally
//    ) {
//        Text("Enter the 6-digit verification code sent to your email", fontSize = 16.sp)
//
//        Spacer(modifier = Modifier.height(16.dp))
//
//        //var verificationCode by remember { mutableStateOf("") }
//
//        OutlinedTextField(
//            value = code,
//            onValueChange = { code = it },
//            label = { Text("Verification Code") },
//            keyboardOptions = KeyboardOptions.Default.copy(
//                keyboardType = KeyboardType.Number
//            )
//        )
//
//        Spacer(modifier = Modifier.height(16.dp))
//
//        BongaButton(
//            modifier = Modifier.fillMaxWidth(),
//            label = "Verify",
//            color = Color.White,
//            onClick = {
//                authViewModel.verifyCode(userId, code,
//                    onSuccess = {
//                        Toast.makeText(context, "Email verified successfully", Toast.LENGTH_SHORT)
//                            .show()
//                        onVerified()
//                    },
//                    onFailure = { errorMessage ->
//                        Toast.makeText(context, errorMessage, Toast.LENGTH_SHORT).show()
//                    }
//                )
//            },
//            buttonColor = orange28
//        )
//
//        when (authState) {
//            is AuthState.Success -> {
//                Text(text = "Verification Successful!")
//                LaunchedEffect(Unit) {
//                    onVerified()
//                }
//            }
//            is AuthState.Error -> {
//                Text(text = (authState as AuthState.Error).message, color = Color.Red)
//            }
//            AuthState.Loading -> {
//                CircularProgressIndicator()
//            }
//            else -> Unit
//        }
//    }
//}
//    private fun generateVerificationCode(): String {
//        val random = java.util.Random()
//        return (100000 + random.nextInt(900000)).toString()  // Generates a random 6-digit number
//    }
//
//    // Stores the verification code and expiry time in Firestore
//    private fun storeVerificationCode(userId: String, code: String) {
//        val firestore = FirebaseFirestore.getInstance()
//        val verificationData = mapOf(
//            "code" to code,
//            "expiry" to System.currentTimeMillis() + 600_000, // 10-minute expiry
//            "verified" to false
//        )
//
//        firestore.collection("verificationCodes").document(userId)
//            .set(verificationData)
//            .addOnSuccessListener { Log.d("Firestore", "Verification code stored") }
//            .addOnFailureListener { e -> Log.e("Firestore", "Error storing verification code", e) }
//    }
//
//    // Function to send an email (simplified, typically use an email service for this)
//
//    // Placeholder example, replace with actual API request to send an email
//    private fun sendVerificationEmail(email: String, code: String) {
//        // Call your backend or third-party API to send email
//        println("Sending verification code $code to email: $email")
//    }
//
//
//    fun verifyCode(uid: String, inputCode: String, onSuccess: () -> Unit, onFailure: (String) -> Unit) {
//        val firestore = FirebaseFirestore.getInstance()
//
//        firestore.collection("verificationCodes").document(uid)
//            .get()
//            .addOnSuccessListener { document ->
//                if (document != null && document.exists()) {
//                    val storedCode = document.getString("code")
//                    val expiry = document.getLong("expiry") ?: 0
//                    val verified = document.getBoolean("verified") ?: false
//
//                    if (verified) {
//                        onFailure("Email already verified.")
//                    } else if (System.currentTimeMillis() > expiry) {
//                        onFailure("Code expired.")
//                    } else if (inputCode == storedCode) {
//                        firestore.collection("verificationCodes").document(uid)
//                            .update("verified", true)
//                            .addOnSuccessListener {
//                                onSuccess()
//                            }
//                            .addOnFailureListener { e ->
//                                onFailure("Failed to update verification status: ${e.message}")
//                            }
//                    } else {
//                        onFailure("Invalid code.")
//                    }
//                } else {
//                    onFailure("Invalid code request.")
//                }
//            }
//            .addOnFailureListener { e ->
//                onFailure("Verification failed: ${e.message}")
//            }
//    }
