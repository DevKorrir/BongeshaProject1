package dev.korryr.bongesha.commons

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
//noinspection UsingMaterialAndMaterial3Libraries
import androidx.compose.material.Icon
//noinspection UsingMaterialAndMaterial3Libraries
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import dev.korryr.bongesha.ui.theme.gray01
import dev.korryr.bongesha.ui.theme.green07
import dev.korryr.bongesha.ui.theme.green99

@Composable
fun CategoryField(
    //item: CategoryItem,
    name:String,
    painter: Painter,
    text: String
){
    Column (
        modifier = Modifier.fillMaxWidth()
    ){
        Box (
            modifier = Modifier
                .padding(8.dp)
                .background(
                    shape = RoundedCornerShape(12.dp),
                    color = gray01,
                ),
            contentAlignment = Alignment.Center,

        ){
            Row (
                modifier = Modifier
                    .padding(8.dp)
                    .fillMaxWidth(),
                //horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ){
                Box (
                    modifier = Modifier
                        .size(70.dp)
                        .background(
                            color = Color.White,
                            shape = RoundedCornerShape(12.dp)
                        ),
                    contentAlignment = Alignment.Center
                ){
                    Image(
                        painter = painter,
                        contentDescription = null,
                        contentScale = ContentScale.Fit,
                        alignment = Alignment.Center
                    )
                }

                Spacer(modifier = Modifier.width(12.dp))

                Column {
                    Text(
                        text = name
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = text,
                        color = green99
                    )
                }

                Spacer(modifier = Modifier.weight(1f))

                Box (
                    modifier = Modifier
                        .size(32.dp)
                        .background(
                            color = green07,
                            shape = RoundedCornerShape(24.dp)
                        ),
                    contentAlignment = Alignment.Center
                ){
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = "",
                        tint = green99
                    )

                }
            }
        }
    }
}