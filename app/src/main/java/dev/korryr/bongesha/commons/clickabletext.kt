package dev.korryr.bongesha.commons

import androidx.compose.foundation.clickable
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.sp
import java.lang.reflect.Modifier

@Composable
fun eSignInText(
    onClick: () -> Unit,
) {
    Text(
        text = AnnotatedString("Sign in"),
        //modifier = Modifier.clickable({onClick}),
        fontSize = 16.sp, // Adjust font size as needed
        //style = SpanStyle(textDecoration = TextDecoration.Underline)
    )
}