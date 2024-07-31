package dev.korryr.bongesha.screens

import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.google.firebase.auth.FirebaseAuth
import dev.korryr.bongesha.R
import dev.korryr.bongesha.commons.BongaButton
import dev.korryr.bongesha.commons.Bongatextfield
import dev.korryr.bongesha.ui.theme.orange01
import dev.korryr.bongesha.ui.theme.orange28

@Composable
fun BongaForgotPassword(
    navController: NavController,
    auth: FirebaseAuth = FirebaseAuth.getInstance()
) {
    var email by remember { mutableStateOf("") }
    val context = LocalContext.current
    var isPasswordResetEmailSent by remember { mutableStateOf(false) }
    val auth = FirebaseAuth.getInstance()


    Column(
        modifier = Modifier
            .padding(16.dp)
            .fillMaxSize()
    ) {
    Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Start
        ) {
            Box (
                modifier = Modifier
                    .clip(RoundedCornerShape(24.dp))
                    .padding(8.dp)
                    .background(
                        color = orange01,
                        shape = RoundedCornerShape(24.dp)
                    )
                    .border(
                        1.dp,
                        shape = RoundedCornerShape(24.dp),
                        color = Color.White
                    )
                    .clickable {
                    navController.navigateUp()
                }
            ){
                Icon(
                    imageVector = Icons.Default.KeyboardArrowLeft,
                    tint = Color.Gray,
                    contentDescription = ""
                )
            }
        }
        Spacer(modifier = Modifier.height(24.dp))

    Column(
        modifier = Modifier
            .padding(16.dp)
    ) {
        Text(
            text = "Forgot Password",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            color = Color.Black
        )

        Spacer(modifier = Modifier.height(16.dp))

        Bongatextfield(
            label = "E-mail address",
            fieldDescription = "Enter your email address to reset your password",
            input = email,
            leading = painterResource(id = R.drawable.image_sec_icon),
            hint = "Yourname@gmail.com",
            onChange = { email = it },
            keyboardOptions = KeyboardOptions.Default.copy(
                keyboardType = KeyboardType.Email,
                imeAction = ImeAction.Done
            )
        )

        Spacer(modifier = Modifier.height(24.dp))

        BongaButton(
            modifier = Modifier.fillMaxWidth(),
            label = "Reset Password",
            color = Color.White,
            buttonColor = orange28,
            onClick = {
                if (email.isNotBlank()) {
                    isPasswordResetEmailSent = true
                    resetPassword(email, context,navController){
                        isPasswordResetEmailSent = false
                    }
                } else {
                    Toast.makeText(context, "Please enter your email", Toast.LENGTH_SHORT).show()
                }
            }
            //enabled = !isPasswordResetEmailSent
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "Remember your password? Sign in.",
            color = Color.Black,
            textDecoration = TextDecoration.Underline,
            modifier = Modifier.clickable {
                navController.navigateUp()
            }
        )
    }
}
}


private fun resetPassword(
    email: String,
    context: Context,
    navController: NavController,
    onSuccess: () -> Unit
) {
    val auth = FirebaseAuth.getInstance()
    auth.sendPasswordResetEmail(email)
        .addOnCompleteListener { task ->
            onSuccess()
            if (task.isSuccessful) {
                Toast.makeText(context, "Password reset email sent", Toast.LENGTH_SHORT).show()
                navController.navigateUp()
            } else {
                Toast.makeText(context, "Failed to send password reset email", Toast.LENGTH_SHORT).show()
            }
        }
}
