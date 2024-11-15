package dev.korryr.bongesha.screens

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import dev.korryr.bongesha.R
import dev.korryr.bongesha.commons.BongaRow
import dev.korryr.bongesha.commons.Route
import dev.korryr.bongesha.ui.theme.orange01
import kotlinx.coroutines.launch


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BongaHelp(
    navController: NavController,
) {
    var isShowBottomSheet by remember { mutableStateOf(false) }
    val sheetState = rememberModalBottomSheetState()
    val scope = rememberCoroutineScope()
    val context = LocalContext.current

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
            onClick = {},
            text = "Account Settings",
            imageVectorleading = painterResource(id = R.drawable.setting),
            modifier = Modifier
                .clickable(
                    onClick = {
                        navController.navigate(Route.Home.ACCOUNT_SETTINGS)
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
            onClick = { /*TODO*/ }
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
            onClick = { /*TODO*/ }
        )

        Spacer(modifier = Modifier.height(16.dp))


        BongaRow(
            text = "Contact Us",
            imageVectorleading = painterResource(id = R.drawable.headphones),
            modifier = Modifier
                .clickable {
                    isShowBottomSheet = true
                    scope.launch {
                        sheetState.show()
                    }
                }
                .padding(24.dp)
                .fillMaxWidth()
                .background(
                    Color.White,
                    shape = RoundedCornerShape(8.dp)
                ),
            onClick = {},
        )

        if (isShowBottomSheet) {
            ModalBottomSheet(
                sheetState = sheetState,
                onDismissRequest = {
                    isShowBottomSheet = false
                }
                ) {
                BottomSheetContent {
                    isShowBottomSheet = false
                    val intent = Intent(Intent.ACTION_DIAL)
                    intent.data = Uri.parse("tel:" + "+254719227769")
                    context.startActivity(intent)
                }
            }
        }
    }
}

@Composable
private fun BottomSheetContent(
    onClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(16.dp))

        Row (
            modifier = Modifier
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.Start
        ){
            Text(
                text = "Call our team!",
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.Bold
            )
        }


        Spacer(modifier = Modifier.height(24.dp))

        Box (
            modifier = Modifier
                .size(200.dp)
                .padding(16.dp)
                .background(
                    color = orange01,

                    shape = CircleShape
                )
                .border(
                    width = 1.dp,
                    color = Color.Transparent,
                    shape = CircleShape
                ),
            contentAlignment = Alignment.Center
        ){
            IconButton(
                modifier = Modifier
                    .size(100.dp),
                onClick = onClick

            ) {
                Image(
                    painter = painterResource(id = R.drawable.call_icon),
                    contentDescription = "Call",
                    colorFilter = ColorFilter.tint(
                        color = Color.Blue
                    ),
                    contentScale = ContentScale.FillBounds,
                )
            }
        }
        Spacer(modifier = Modifier.height(50.dp))
    }
}
///////////////////////////////////////////////////////////////////////////
// mwishow
///////////////////////////////////////////////////////////////////////////
