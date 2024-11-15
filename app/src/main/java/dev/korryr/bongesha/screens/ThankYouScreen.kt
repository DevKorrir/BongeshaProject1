package dev.korryr.bongesha.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import dev.korryr.bongesha.R
import dev.korryr.bongesha.commons.Route
import dev.korryr.bongesha.ui.theme.blue88
import dev.korryr.bongesha.ui.theme.orange28
import dev.korryr.bongesha.ui.theme.green99

@Composable
fun ThankYouScreen(navController: NavController) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Thank You Image
        Box (
            modifier = Modifier
                .clip(
                    shape = RoundedCornerShape(8.dp)
                )
                .shadow(
                    elevation = 4.dp,
                    shape = RoundedCornerShape(8.dp),
                    clip = true,
                    spotColor = blue88,
                    ambientColor = blue88
                )
                .size(200.dp)
                .padding(bottom = 16.dp)
        ){
            Image(
                painter = painterResource(id = R.drawable.thank_you),
                contentDescription = null,
                modifier = Modifier
                    .clip(
                        shape = RoundedCornerShape(8.dp)

                    )
                    .size(200.dp)
                    .padding(bottom = 16.dp)
            )
        }

        // Thank You Text
        Text(
            text = "Thanks for your purchase!",
            fontSize = 32.sp,
            fontWeight = FontWeight.Bold,
            color = green99,
            textAlign = TextAlign.Center
        )
        Text(
            text = "The order confirmation has been sent to your mail: user email",
            fontSize = 18.sp,
            color = Color.Gray,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(horizontal = 16.dp)
        )

        Spacer(modifier = Modifier.height(8.dp))

        // Confirmation Text
        Text(
            text = "Your order has been successfully placed.\nWe appreciate your trust in us!",
            fontSize = 16.sp,
            color = Color.Gray,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(horizontal = 16.dp)
        )

        Spacer(modifier = Modifier.height(24.dp))

        // Button to go back to the home screen
        Button(
            onClick = {
                navController.navigate(Route.Home.HOME)
                      },
            shape = RoundedCornerShape(8.dp),
            colors = ButtonDefaults.buttonColors(containerColor = orange28),
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
        ) {
            Text(
                text = "Back to Home",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Order Details Button
        Button(
            onClick = {
                navController.navigate(Route.Home.ORDER)
                      },
            shape = RoundedCornerShape(8.dp),
            colors = ButtonDefaults.buttonColors(containerColor = green99),
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
        ) {
            Text(
                text = "View Order Details",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )
        }
    }
}
