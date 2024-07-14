package dev.korryr.bongesha.screens
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Call
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import dev.korryr.bongesha.R
import dev.korryr.bongesha.commons.BongaRow
import dev.korryr.bongesha.commons.Route


@Composable
fun BongaHelp(
    navController: NavController
) {
    Column (
        modifier = Modifier
            .fillMaxWidth()
            .padding(12.dp)
    ){
        Row (
            horizontalArrangement = Arrangement.Start,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
        ){
            Box {
                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription =  null,
                    modifier = Modifier
                        .clickable(
                            onClick = {
                                navController.navigateUp()
                            }
                        )
                )
            }

            Spacer(modifier = Modifier.width(50.dp))

            Text(
                text = "Complaints and Suggestions",
                fontWeight = FontWeight(weight = 700)
            )
        }

        Spacer(modifier = Modifier.height(50.dp))

        Text(
            text = "How can we help you?",
            fontWeight = FontWeight(weight = 700)
        )

        Spacer(modifier = Modifier.height(12.dp))

        BongaRow(
            text = "Account Settings",
            imageVectorleading = painterResource(id = R.drawable.setting),
            modifier = Modifier
                .clickable(
                    onClick = {
                        navController.navigate(Route.Home.AccountSettings)
                    }
                )
                .padding(24.dp)
                .fillMaxWidth()
                .background(
                    color = Color.White,
                    shape = RoundedCornerShape(8.dp)
                )
            ,
        )

        Spacer(modifier = Modifier.height(16.dp))

        BongaRow(
            text = "Problem With Payment",
            imageVectorleading = painterResource(id = R.drawable.problem_solving),
            modifier = Modifier
                .padding(24.dp)
                .fillMaxWidth()
                .background(
                    color = Color.White,
                    shape = RoundedCornerShape(8.dp)
                )
            ,
            //onClick = { /*TODO*/ }
        )

        Spacer(modifier = Modifier.height(16.dp))

        BongaRow(
            text = "Suggestions",
            imageVectorleading = painterResource(id = R.drawable.suggestion_icon),
            modifier = Modifier
                .padding(24.dp)
                .fillMaxWidth()
                .background(
                    color = Color.White,
                    shape = RoundedCornerShape(8.dp)
                )
            ,
            //onClick = { /*TODO*/ }
        )

        Spacer(modifier = Modifier.height(16.dp))

        //val sheetState = rememberModalBottomSheetState(initialValue = ModalBottomSheetValue.Hidden)
        val coroutineScope = rememberCoroutineScope()
        val context = LocalContext.current

        BongaRow(
            text = "Contact Us",
            imageVectorleading = painterResource(id = R.drawable.headphones),
            modifier = Modifier
                .clickable(
                    onClick = {
                        navController.navigate(Route.Home.Callsheet)

                        //bottomSheetDialog.show()
                    }
                )
                .padding(24.dp)
                .fillMaxWidth()
                .background(
                    Color.White,
                    shape = RoundedCornerShape(8.dp)
                )
            ,
        )
    }
}

@Composable
fun BottomSheetContent(
    navController: NavController
) {
    Column(
        modifier = Modifier.padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(Icons.Filled.Call, contentDescription = "Call")
        Spacer(modifier = Modifier.height(16.dp))
        Text(text = "Make a Call", style = MaterialTheme.typography.bodyLarge)
        IconButton(onClick = { /* Handle call action */ }) {
            Icon(Icons.Filled.Call, contentDescription = "Call")
        }
    }
}
