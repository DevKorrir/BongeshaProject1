package dev.korryr.bongesha.faceBookLogin

import android.app.Activity
import android.content.Intent
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import com.facebook.CallbackManager
import com.facebook.FacebookCallback
import com.facebook.FacebookException
import com.facebook.login.LoginManager
import com.facebook.login.LoginResult
import com.google.firebase.auth.FacebookAuthProvider
import com.google.firebase.auth.FirebaseAuth
import dev.korryr.bongesha.commons.BongaButton
import dev.korryr.bongesha.ui.theme.orange28
import retrofit2.http.Tag

@Composable
fun FacebookSignInButton(
    onSignIn: (Boolean) -> Unit
) {
    val context = LocalContext.current
    val auth = remember { FirebaseAuth.getInstance() }
    val callbackManager = remember { CallbackManager.Factory.create() }

    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult()
    ) { result ->
        callbackManager.onActivityResult(result.resultCode, result.resultCode, result.data)
    }

    LoginManager.getInstance().registerCallback(callbackManager,
        object : FacebookCallback<LoginResult> {
            override fun onSuccess(result: LoginResult?) {
                Log.d("Facebook", "Login successful")
                val token = result?.accessToken
                val credential = FacebookAuthProvider.getCredential(token?.token!!)
                auth.signInWithCredential(credential)
                    .addOnCompleteListener { task ->
                        onSignIn(task.isSuccessful)
                    }
            }

            override fun onCancel() {
                Log.d("Facebook", "Login canceled")
                onSignIn(false)
            }

            override fun onError(error: FacebookException?) {
                Log.d("Facebook", "Login error: ${error?.message}")
                onSignIn(false)
            }
        })
    // ....
//    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
//        super.onActivityResult(requestCode, resultCode, data)
//
//        // Pass the activity result back to the Facebook SDK
//        callbackManager.onActivityResult(requestCode, resultCode, data)
//    }

//    BongaButton(
//        label = "Sign in with Facebook",
//        color = Color.White,
//        buttonColor = orange28,
//        onClick = {
//            val signInIntent = LoginManager.getInstance().logInWithReadPermissions(
//                context as Activity,
//                listOf("email", "public_profile")
//            )
//            launcher.launch(signInIntent)
//        }
//    )
}


