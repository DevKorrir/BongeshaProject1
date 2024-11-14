package dev.korryr.bongesha.screens

import android.location.Location
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import dev.korryr.bongesha.R
import dev.korryr.bongesha.ui.theme.gray01
import dev.korryr.bongesha.ui.theme.green99
import dev.korryr.bongesha.ui.theme.orange28
import dev.korryr.bongesha.viewmodels.CartViewModel

@Composable
fun SummaryScreen(
    cartViewModel: CartViewModel
) {

    var userLocation by remember { mutableStateOf<Location?>(null) }
    var selectedPaymentMethod by remember { mutableStateOf("Mpesa") }
    val deliveryFee = cartViewModel.calculateDeliveryFee(userLocation)

    Column {
        Spacer(Modifier.height(8.dp))

                Text(
                    text = "Payment Method",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.W700
                )

        Spacer(Modifier.height(8.dp))

        //payment method
        Box(
            modifier = Modifier
                .padding(8.dp)
                .shadow(
                    elevation = 8.dp,
                    shape = RoundedCornerShape(12.dp),
                    clip = true
                )
                .background(
                    color = Color.White,
                    shape = RoundedCornerShape(12.dp)
                )
                .border(
                    width = 1.dp,
                    color = Color.Transparent,
                    shape = RoundedCornerShape(12.dp)
                )
        ){
            Column (
                modifier = Modifier
                    .background(
                        color = Color.White,
                        shape = RoundedCornerShape(12.dp)
                    )
                    .padding(12.dp)
            ){

                Row (
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .height(55.dp)
                        .border(
                            width = 1.dp,
                            shape = RoundedCornerShape(12.dp),
                            color = if (selectedPaymentMethod == "Mpesa") green99 else gray01

                        )
                        .background(
                            color = if (selectedPaymentMethod == "Mpesa") green99 else Color.Transparent,
                            shape = RoundedCornerShape(12.dp)
                        )
                        .clip(
                            shape = RoundedCornerShape(12.dp)
                        )
                        .fillMaxWidth()
                        .clickable {
                            selectedPaymentMethod = "Mpesa"
                        }
                ){
                    Row (
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(12.dp)
                    ){
                        Image(
                            painter = painterResource(id = R.drawable.e_payment),
                            contentDescription = null,
                            modifier = Modifier.size(24.dp),
                            colorFilter = ColorFilter.tint(if (selectedPaymentMethod == "Mpesa")
                                orange28 else Color.LightGray )
                        )

                        Spacer(Modifier.width(8.dp))

                        Text(
                            text = "M-pesa",
                            modifier = Modifier.weight(1f),
                            fontSize = 16.sp,
                            color = if (selectedPaymentMethod == "Mpesa") Color.White else Color.Black
                        )

                        RadioButton(
                            selected = selectedPaymentMethod == "Mpesa",
                            onClick = { selectedPaymentMethod = "Mpesa" },
                            colors = RadioButtonDefaults.colors(
                                selectedColor = orange28,
                                unselectedColor = Color.Gray
                            )
                        )

                    }

                }

                Spacer(Modifier.height(10.dp))

                Row (
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .height(55.dp)
                        .clickable {
                            selectedPaymentMethod = "Cash"
                        }
                        .border(
                            width = 1.dp,
                            shape = RoundedCornerShape(12.dp),
                            color = if (selectedPaymentMethod == "Cash") green99 else gray01
                        )
                        .background(
                            color = if (selectedPaymentMethod == "Cash") green99 else Color.Transparent,
                            shape = RoundedCornerShape(12.dp)
                        )
                        .clip(
                            shape = RoundedCornerShape(12.dp)
                        )
                        .fillMaxWidth()
                ){
                    Row (
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(12.dp)
                    ){
                        Image(
                            painter = painterResource(id = R.drawable.cash_method),
                            contentDescription = null,
                            modifier = Modifier.size(24.dp),
                            colorFilter = ColorFilter.tint(if (selectedPaymentMethod == "Cash")
                                orange28 else Color.LightGray )
                        )

                        Spacer(Modifier.width(8.dp))

                        Text(
                            text = "Cash on delivery",
                            modifier = Modifier.weight(1f),
                            fontSize = 16.sp,
                            color = if (selectedPaymentMethod == "Cash") Color.White else Color.Black
                        )

                        RadioButton(
                            selected = selectedPaymentMethod == "Cash",
                            onClick = { selectedPaymentMethod = "Cash"},
                            colors = RadioButtonDefaults.colors(
                                selectedColor = orange28,
                                unselectedColor = Color.Gray
                            )
                        )
                    }

                }
            }
        }

        Spacer(Modifier.height(10.dp))

        //invoice summary
        Box(
            modifier = Modifier
                .padding(8.dp)
                .shadow(
                    elevation = 8.dp,
                    shape = RoundedCornerShape(12.dp),
                    clip = true
                )
                .background(
                    color = Color.White,
                    shape = RoundedCornerShape(12.dp)
                )
                .border(
                    width = 1.dp,
                    color = Color.Transparent,
                    shape = RoundedCornerShape(12.dp)
                )
                .height(100.dp),
            contentAlignment = Alignment.Center
        ){
            Column (
                modifier = Modifier
                    .padding(8.dp)
            ){
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Total Value",
                        style = MaterialTheme.typography.bodyLarge,
                        fontWeight = FontWeight.W700,
                        color = Color.Black,
                    )

                    Spacer(modifier = Modifier.weight(1f))

                    Text(
                        text = " KES",
                        style = MaterialTheme.typography.bodyLarge,
                        fontWeight = FontWeight.Bold,
                        color = Color.Black,
                    )
                }

                Spacer(Modifier.height(4.dp))

                HorizontalDivider(
                    color = Color.LightGray,
                    thickness = 1.dp
                )

                Spacer(Modifier.height(4.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Delivery Fee",
                        style = MaterialTheme.typography.bodyLarge,
                        fontWeight = FontWeight.Bold,
                        color = Color.DarkGray,
                    )

                    Spacer(modifier = Modifier.weight(1f))

                    Text(
                        text = "$deliveryFee KES",
                        style = MaterialTheme.typography.bodyLarge,
                        fontWeight = FontWeight.Bold,
                        color = Color.DarkGray,
                    )
                }

                Spacer(Modifier.height(4.dp))

                HorizontalDivider(
                    color = Color.LightGray,
                    thickness = 1.dp
                )

                Spacer(Modifier.height(4.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Amount Payable",
                        color = orange28,
                        fontWeight = FontWeight.W700
                    )

                    Spacer(modifier = Modifier.weight(1f))

                    Text(
                        text = "$deliveryFee KES",
                        style = MaterialTheme.typography.bodyLarge,
                        fontWeight = FontWeight.Bold,
                        color = orange28,
                    )
                }
            }

        }
    }
}