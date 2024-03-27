package dev.korryr.bongesha.screens.category

//noinspection UsingMaterialAndMaterial3Libraries
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import dev.korryr.bongesha.MainViewModel
import dev.korryr.bongesha.R
import dev.korryr.bongesha.commons.BongaBox
import dev.korryr.bongesha.commons.BongaSearch
import dev.korryr.bongesha.ui.theme.orange100

@Composable
fun BongaCategory(
    navController: NavController,
    onClick: () -> Unit,
){
    Column(
        modifier = Modifier
            .padding(24.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
        ){
            Icon(
                imageVector = Icons.Default.Menu,
                contentDescription = "",
                tint = orange100
            )
            
            Spacer(modifier = Modifier.weight(1f))

            Icon(
                imageVector = Icons.Default.Person,
                contentDescription = "",
                tint = orange100
            )

        }
        BongaSearch(
            //query = "Search",
            onChange = {
                newQuery ->
            }
        ) {

        }

        Spacer(modifier = Modifier.height(24.dp))

        Text(
            text = "Category",
            fontSize = 36.sp,
            fontWeight = FontWeight.W700
        )

        Spacer(modifier = Modifier.height(24.dp))

        Row(
            modifier = Modifier
                .fillMaxSize()
                .horizontalScroll(rememberScrollState())
        ) {
            BongaBox(
                modifier = Modifier
                    .clickable {
                        onClick()
                    }
                    .border(
                        1.dp,
                        color = Color.Transparent,
                        shape = RoundedCornerShape(12.dp)
                    )
                    .background(
                        color = Color.Transparent,
                        shape = RoundedCornerShape(12.dp)
                    )
                    .size(70.dp)
                    //.width(80.dp)
                    //.height(60.dp)
                        ,
                painter = painterResource(id = R.drawable.signup_image),
                text = "Beverages"
            )
            Spacer(modifier = Modifier.width(12.dp))
            BongaBox(
                modifier = Modifier
                    .border(
                        1.dp,
                        color = Color.Transparent,
                        shape = RoundedCornerShape(12.dp)
                    )
                    .background(
                        color = Color.Transparent,
                        shape = RoundedCornerShape(12.dp)
                    )
                    .size(70.dp),
                        //.width(80.dp)
                        //.height(60.dp),
                painter = painterResource(id = R.drawable.signup_image),
                text = "Home Care"
            )
            Spacer(modifier = Modifier.width(12.dp))
            BongaBox(
                modifier = Modifier
                    .border(
                        1.dp,
                        color = Color.Transparent,
                        shape = RoundedCornerShape(12.dp)
                    )
                    .background(
                        color = Color.Transparent,
                        shape = RoundedCornerShape(12.dp)
                    )
                    .size(70.dp),
                        //.width(80.dp)
                        //.height(60.dp)
                painter = painterResource(id = R.drawable.signup_image),
                text = "Personal\nCare"
            )
            Spacer(modifier = Modifier.width(12.dp))
            BongaBox(
                modifier = Modifier
                    .border(
                        1.dp,
                        color = Color.Transparent,
                        shape = RoundedCornerShape(12.dp)
                    )
                    .background(
                        color = Color.Transparent,
                        shape = RoundedCornerShape(12.dp)
                    )
                    .size(70.dp),
                        //.width(80.dp)
                        //.height(60.dp)
                painter = painterResource(id = R.drawable.signup_image),
                text = "Home &\nKitchen"
            )
            Spacer(modifier = Modifier.width(12.dp))
            BongaBox(
            modifier = Modifier
                .border(
                    1.dp,
                    color = Color.Transparent,
                    shape = RoundedCornerShape(12.dp)
                )
                .background(
                    color = Color.Transparent,
                    shape = RoundedCornerShape(12.dp)
                )
                .size(70.dp),
                    //.width(80.dp)
                    //.height(60.dp)
            painter = painterResource(id = R.drawable.signup_image),
            text = "Bread &\nSpread"
        )
            Spacer(modifier = Modifier.width(12.dp))
            BongaBox(
                modifier = Modifier
                    .border(
                        1.dp,
                        color = Color.Transparent,
                        shape = RoundedCornerShape(12.dp)
                    )
                    .background(
                        color = Color.Transparent,
                        shape = RoundedCornerShape(12.dp)
                    )
                    .size(70.dp)
                    .clickable {  },
                        //.width(80.dp)
                        //.height(60.dp)
                painter = painterResource(id = R.drawable.signup_image),
                text = "Rice, Pulse\n& pasta"
            )
            Spacer(modifier = Modifier.width(12.dp))

            BongaBox(
                modifier = Modifier
                    .border(
                        1.dp,
                        color = Color.Transparent,
                        shape = RoundedCornerShape(12.dp)
                    )
                    .background(
                        color = Color.Transparent,
                        shape = RoundedCornerShape(12.dp)
                    )
                    .size(70.dp),
                        //.width(80.dp)
                        //.height(60.dp)
                painter = painterResource(id = R.drawable.signup_image),
                text = "Snacks"
            )
            Spacer(modifier = Modifier.width(12.dp))

            BongaBox(
                modifier = Modifier
                    .border(
                        1.dp,
                        color = Color.Transparent,
                        shape = RoundedCornerShape(12.dp)
                    )
                    .background(
                        color = Color.Transparent,
                        shape = RoundedCornerShape(12.dp)
                    )
                    .size(70.dp),
                        //.width(80.dp)
                        //.height(60.dp)
                painter = painterResource(id = R.drawable.signup_image),
                text = "Milk & Eggs"
            )
            Spacer(modifier = Modifier.width(12.dp))

            BongaBox(
                modifier = Modifier
                    .border(
                        1.dp,
                        color = Color.Transparent,
                        shape = RoundedCornerShape(12.dp)
                    )
                    .background(
                        color = Color.Transparent,
                        shape = RoundedCornerShape(12.dp)
                    )
                    .size(70.dp),
                        //.width(80.dp)
                        //.height(60.dp)
                painter = painterResource(id = R.drawable.signup_image),
                text = "Flour & \nSugar"
            )
            Spacer(modifier = Modifier.width(12.dp))

            BongaBox(
                modifier = Modifier
                    .border(
                        1.dp,
                        color = Color.Transparent,
                        shape = RoundedCornerShape(12.dp)
                    )
                    .background(
                        color = Color.Transparent,
                        shape = RoundedCornerShape(12.dp)
                    )
                    .size(70.dp),
                        //.width(80.dp)
                        //.height(60.dp)
                painter = painterResource(id = R.drawable.signup_image),
                text = "Oil & fats"
            )
            Spacer(modifier = Modifier.width(12.dp))

            BongaBox(
                modifier = Modifier
                    .border(
                        1.dp,
                        color = Color.Transparent,
                        shape = RoundedCornerShape(12.dp)
                    )
                    .background(
                        color = Color.Transparent,
                        shape = RoundedCornerShape(12.dp)
                    )
                    .size(70.dp),
                        //.width(80.dp)
                        //.height(60.dp)
                painter = painterResource(id = R.drawable.signup_image),
                text = "Baby"
            )
            Spacer(modifier = Modifier.width(12.dp))

        }
    }
}