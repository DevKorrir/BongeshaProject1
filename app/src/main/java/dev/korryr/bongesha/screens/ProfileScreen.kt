package dev.korryr.bongesha.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.google.firebase.auth.FirebaseAuth
import dev.korryr.bongesha.R
import dev.korryr.bongesha.commons.BongaRow
import dev.korryr.bongesha.commons.Route
import dev.korryr.bongesha.ui.theme.blue88
import dev.korryr.bongesha.ui.theme.orange100
import dev.korryr.bongesha.viewmodels.AuthViewModel
import java.util.Calendar

@Composable
fun UserProfile(
    navController: NavController,
    authViewModel: AuthViewModel,
) {
    val context = LocalContext.current
    val auth = FirebaseAuth.getInstance()
    val user = auth.currentUser
    val displayName = user?.displayName ?: "User"
    val email = user?.email ?: "No email"

    val currentHour = Calendar.getInstance().get(Calendar.HOUR_OF_DAY)
    val greeting = when (currentHour) {
        in 2..11 -> "Good morning"
        in 12..15 -> "Good afternoon"
        in 16..20 -> "Good evening"
        else -> "Good night😴"
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(8.dp)
    ) {
        Row(
            modifier = Modifier
                .background(
                    color = blue88,
                    shape = RoundedCornerShape(12.dp)
                )
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.Start,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                modifier = Modifier
                    .padding(12.dp)
            ) {
                Text(
                    text = "My profile".uppercase(),
                    style = MaterialTheme.typography.headlineLarge,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = "$greeting, $displayName!",
                    fontWeight = FontWeight.W700,
                    fontSize = 16.sp,
                    color = orange100,
                    overflow = TextOverflow.Ellipsis,
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = "Email: $email",
                    color = orange100
                )

                Spacer(modifier = Modifier.height(24.dp))
            }

        }

        Spacer(modifier = Modifier.height(30.dp))

        Text(
            text = "my bongesha account".uppercase(),
            fontSize = 12.sp,
            fontWeight = FontWeight.Thin,
            color = Color.Gray,
            style = MaterialTheme.typography.titleSmall,
        )


        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    color = Color.Transparent,
                    shape = RoundedCornerShape(4.dp)
                )
                .verticalScroll(rememberScrollState())
        ) {
            Spacer(modifier = Modifier.height(8.dp))

            BongaRow(
                text = "Orders",
                imageVectorleading = painterResource(id = R.drawable.purchase_order_icon),
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxWidth()
                    .background(
                        color = Color.White,
                        shape = RoundedCornerShape(8.dp)
                    ),
                imageVectortrailing = Icons.Default.KeyboardArrowRight,
                //onClick = {}
            )

            Spacer(modifier = Modifier.height(12.dp))

            BongaRow(
                text = "Chat",
                imageVectorleading = painterResource(id = R.drawable.bubble_chat),
                modifier = Modifier
                    .clickable(
                        onClick = {
                            navController.navigate(Route.Home.INBOX)
                        }
                    )
                    .padding(16.dp)
                    .fillMaxWidth()
                    .background(
                        color = Color.White,
                        shape = RoundedCornerShape(8.dp)
                    ),
                imageVectortrailing = Icons.Default.KeyboardArrowRight,

            )

            Spacer(modifier = Modifier.height(12.dp))

            BongaRow(
                text = "Reviews",
                imageVectorleading = painterResource(id = R.drawable.rating),
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxWidth()
                    .background(
                        color = Color.White,
                        shape = RoundedCornerShape(8.dp)
                    ),
                imageVectortrailing = Icons.Default.KeyboardArrowRight,
                //onClick = {                }
            )

            Spacer(modifier = Modifier.height(12.dp))

            BongaRow(
                text = "Saved Items",
                imageVectorleading = painterResource(id = R.drawable.shopping_wishlish),
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxWidth()
                    .background(
                        color = Color.White,
                        shape = RoundedCornerShape(8.dp)
                    ),
                imageVectortrailing = Icons.Default.KeyboardArrowRight,
                //onClick = {}
            )

            Spacer(modifier = Modifier.height(12.dp))

            BongaRow(
                text = "Help and Support",
                imageVectorleading = painterResource(id = R.drawable.headphones),
                modifier = Modifier
                    .clip(
                        RoundedCornerShape(8.dp)
                    )
                    .clickable(
                        onClick = {
                            navController.navigate(Route.Home.HELP_SUPPORT)
                        }
                    )
                    .padding(16.dp)
                    .fillMaxWidth()
                    .background(
                        color = Color.White,
                        shape = RoundedCornerShape(8.dp)
                    ),
                imageVectortrailing = Icons.Default.KeyboardArrowRight,
                //onClick = {
                    //navController.navigate(Route.Home.HelpSupport)

            )

            Spacer(modifier = Modifier.height(12.dp))

            BongaRow(
                text = "Settings",
                imageVectorleading = painterResource(id = R.drawable.setting),
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxWidth()
                    .background(
                        color = Color.White,
                        shape = RoundedCornerShape(8.dp)
                    ),
                imageVectortrailing = Icons.Default.KeyboardArrowRight,
                //onClick = {}
            )

            Spacer(modifier = Modifier.height(24.dp))

            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.Center
            ) {
                Button(
                    colors = ButtonDefaults.buttonColors(
                        containerColor = blue88,
                        contentColor = Color.White
                    ),
                    modifier = Modifier
                        .padding(12.dp),
                    onClick = {
                        authViewModel.signOut(navController)
                    }
                ) {
                    Text(
                        text = "logout".uppercase(),
                    )
                }
            }
        }
    }
}

