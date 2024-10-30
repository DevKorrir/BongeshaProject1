package dev.korryr.bongesha.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import dev.korryr.bongesha.R
import dev.korryr.bongesha.commons.BongaButton
import dev.korryr.bongesha.commons.Route

@Composable
fun BongaWelcome(
    navController: NavController
){
    Column(
        modifier = Modifier
            .padding(8.dp)
            .fillMaxWidth()
    ) {
        Box(
            modifier = Modifier
                .clickable {
                    navController.popBackStack()
                }
                .border(
                    1.dp,
                    color = Color.Transparent,
                    shape = RoundedCornerShape(24.dp)
                )
                .size(50.dp)
                .background(Color.Transparent),
            contentAlignment = Alignment.Center
        ){
            Icon(
                Icons.Default.ArrowBack,
                tint = Color.Gray,
                contentDescription = ""
            )
        }
    }

    Column(
        modifier = Modifier
            .fillMaxHeight()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center

    ) {
        Image(painter = painterResource(
            id = R.drawable.working_sticker),
            contentDescription = "")

        Spacer(modifier = Modifier.height(24.dp))

        Text(
            text = "One click away from\n\n splitting the bill" ,
            fontSize = 46.sp,
            fontStyle = FontStyle.Normal,
            fontWeight = FontWeight.ExtraBold,
            modifier = Modifier.padding(vertical = 16.dp),
            //modifier = Modifier.padding(16.dp),
            lineHeight = 20.sp,
            textAlign = TextAlign.Center
        )
        /*
        Text(
            text = "splitting the bill",
            fontSize = 46.sp,
            fontStyle = FontStyle.Normal,
            fontWeight = FontWeight.ExtraBold,
            modifier = Modifier.padding(bottom = 16.dp)
        )

         */
        Spacer(modifier = Modifier.weight(1f))

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Bottom
        ) {
            Box(
                contentAlignment = Alignment.BottomCenter,
                modifier = Modifier
                    .background(
                        shape = RoundedCornerShape(12.dp),
                        color = Color.Gray
                    )
                    .fillMaxWidth()
                    //.align(Alignment.BottomCenter)
                    .height(288.dp),
            ){
                Column(
                    modifier = Modifier.padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ){

                    val slides = listOf(
                        "slide 1: This is the content of slide 1",
                        "Sit back and never worry about sharing\nbills with your housemates again!",
                        "slide 3: This is the content of slide 3"
                    )

                    var currentSlideIndex by remember {
                        mutableStateOf(0) }

                    BongaSlideText(
                        text = slides[currentSlideIndex]
                    )
                        /*
                    Text(
                        text = "sit back and never worry about sharing \n bills with your housemates again!",
                        color = Color.White,
                        fontSize = 24.sp
                        )*/

                    Spacer(modifier = Modifier.height(24.dp))

                    Row (
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceEvenly,
                        verticalAlignment = Alignment.CenterVertically
                    ){
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceEvenly,
                            verticalAlignment = Alignment.CenterVertically
                        ) {


                            BongaButton(
                                showArrow = false,
                                label = "Skip",
                                color = Color.White,
                                buttonColor = Color.Gray,
                                modifier = Modifier
                                    .width(176.dp),
                                onClick = {
                                    navController.navigate(Route.Home.SIGN_IN)
                                }
                            )

                            Spacer(modifier = Modifier.width(8.dp))

                            BongaButton(
                                showArrow = false,
                                label = "Next",
                                color = Color.Black,
                                buttonColor = Color.LightGray,
                                modifier = Modifier
                                    .width(176.dp),
                                onClick = {
                                    currentSlideIndex = (currentSlideIndex + 1) % slides.size
                                }
                            )
                        }
                    }
                }
            }
        }
    }
}