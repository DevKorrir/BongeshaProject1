package dev.korryr.bongesha.commons

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.selection.toggleable
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.Blue
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import dev.korryr.bongesha.ui.theme.orange28

@Composable
fun BongaCheckbox(
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    modifier: Modifier = Modifier,
    shape: RoundedCornerShape = CircleShape,
    checkedColor: Color = Blue,
    uncheckedColor: Color = Color.Gray,
    size: Dp = 24.dp
) {
    val toggleableModifier = Modifier.toggleable(
        value = checked,
        onValueChange = onCheckedChange
    )

    Box(
        modifier = modifier
            .size(size)
            .clip(shape)
            .then(toggleableModifier)
            .border(
                width = 1.dp,
                color = if (checked) checkedColor else Color.Gray,
                shape = shape
            )
            .background(
                color = if (checked) checkedColor else uncheckedColor,
                shape = shape
            ),
        contentAlignment = Alignment.Center
    ) {
        if (checked) {
            Icon(
                imageVector = Icons.Default.Check,
                contentDescription = "Checked",
                tint = Color.White
            )
        }
    }
}
