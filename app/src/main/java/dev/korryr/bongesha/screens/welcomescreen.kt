package dev.korryr.bongesha.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.internal.composableLambdaInstance
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.modifier.modifierLocalConsumer
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import dev.korryr.bongesha.R
import dev.korryr.bongesha.commons.bongabutton

@Composable
fun BongaWelcome(
    navController: NavController
){
    Column(
        modifier = Modifier.padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally

    ) {
        Image(painter = painterResource(
            id = R.drawable.working_sticker),
            contentDescription = "")

        Spacer(modifier = Modifier.height(24.dp))

        Text(
            text = "One click away from" ,
            fontSize = 46.sp,
            fontStyle = FontStyle.Normal,
            fontWeight = FontWeight.ExtraBold,
            modifier = Modifier.padding(bottom = 16.dp)
        )
        Text(
            text = "splitting the bill",
            fontSize = 46.sp,
            fontStyle = FontStyle.Normal,
            fontWeight = FontWeight.ExtraBold,
            modifier = Modifier.padding(bottom = 16.dp)
        )
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
                        color = Color.Black)
                    .fillMaxWidth()
                    //.align(Alignment.BottomCenter)
                    .height(288.dp),
            ){
                Column(
                    modifier = Modifier.padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ){
                    Text(
                        text = "sit back and never worry about sharing \n bills with your housemates again!",
                        color = Color.White,
                        fontSize = 24.sp
                        )

                    Spacer(modifier = Modifier.height(24.dp))

                    Row (
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ){
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            bongabutton(

                                showArrow = false,
                                label = "Skip",
                                buttonColor = Color.Gray,
                                modifier = Modifier
                                    .wrapContentWidth()
                                    //.weight(1f)
                            ) {
                            }

                            Spacer(modifier = Modifier.width(8.dp))

                            bongabutton(
                                showArrow = false,
                                label = "next",
                                buttonColor = Color.LightGray,
                                modifier = Modifier

                                    .wrapContentWidth()
                            ) {

                            }
                        }
                    }
                }
            }
        }
    }
}