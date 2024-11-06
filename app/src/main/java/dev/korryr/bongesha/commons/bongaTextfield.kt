package dev.korryr.bongesha.commons

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonColors
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldColors
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import dev.korryr.bongesha.R
import dev.korryr.bongesha.ui.theme.orange28

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Bongatextfield(
    label: String? = null,
    isPassword: Boolean = false,
    showPassword: Boolean = false,
    boldLabel: Boolean = true,
    fieldDescription: String,
    isValid: Boolean = true,
    isLongText: Boolean = false,
    input: String,
    trailing: Painter? = null,
    leading: Painter? = null,
    onTrailingIconClicked: (() -> Unit)? = null,
    hint: String,
    onChange: (String) -> Unit,
    enabled: Boolean = true,
    readOnly: Boolean = false,
    onDone: () -> Unit = {},
    keyboardOptions: KeyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
    keyboardActions: KeyboardActions? = null,
    colors: TextFieldColors = TextFieldDefaults.colors(
        focusedContainerColor = Color.White,
        unfocusedContainerColor = Color.White,
        disabledContainerColor = Color.White,
        errorContainerColor = Color.White,
        unfocusedTextColor = Color.Black,
        disabledTextColor = Color.Black,
        errorTextColor = Color.Red,
        errorIndicatorColor = Color.Red,
        unfocusedIndicatorColor = Color.Transparent,
        focusedIndicatorColor = orange28,
        focusedLeadingIconColor = orange28,
        unfocusedLeadingIconColor = Color.Black,
        cursorColor = orange28,
        focusedLabelColor = orange28
    )
) {
    val current = LocalFocusManager.current
    val softwareKeyboard = LocalSoftwareKeyboardController.current
    var passwordVisible by rememberSaveable { mutableStateOf(showPassword) }

    Column(
        modifier = Modifier,
        horizontalAlignment = Alignment.Start,
        verticalArrangement = Arrangement.Center,
    ) {
        if (label != null) {
            Text(
                text = label,
                color = Color.DarkGray,
                fontWeight = if (boldLabel) FontWeight.Bold else FontWeight.Normal,
            )
        }
        if (fieldDescription.isNotEmpty()) {
            Text(
                text = fieldDescription,
                color = if (isValid) Color.Green else Color.Red
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            OutlinedTextField(
                singleLine = !isLongText,
                value = input,
                enabled = enabled,
                readOnly = readOnly,
                isError = !isValid,
                onValueChange = {
                    onChange(it)
                },
                modifier = Modifier
                    .weight(1f)
                    .height(50.dp)
                    .fillMaxWidth()
                    .testTag(label + "1"),
                colors = colors,
                shape = RoundedCornerShape(12.dp),
                keyboardOptions = if (isPassword && passwordVisible) {
                    keyboardOptions.copy(keyboardType = KeyboardType.Text)
                } else if (isPassword) {
                    keyboardOptions.copy(keyboardType = KeyboardType.Password)
                } else {
                    keyboardOptions
                },
                visualTransformation = if (isPassword && !passwordVisible) {
                    PasswordVisualTransformation()
                } else {
                    VisualTransformation.None
                },
                placeholder = {
                    Text(
                        text = hint,
                        textAlign = TextAlign.Center,
                        color = Color.LightGray
                    )
                },
                keyboardActions = keyboardActions ?: KeyboardActions(
                    onNext = {
                        current.moveFocus(FocusDirection.Next)
                    },
                    onDone = {
                        softwareKeyboard?.hide()
                        onDone()
                    }
                ),
                trailingIcon = if (isPassword) {
                    {
                        IconButton(
                            colors = IconButtonDefaults.iconButtonColors(
                                contentColor = orange28,
                                ),
                            modifier = Modifier
                                .size(24.dp),
                            onClick = {
                                passwordVisible = !passwordVisible
                            }
                        ) {
                            Icon(
                                painter = if (passwordVisible) {
                                    painterResource(id = R.drawable.hide_icon)
                                } else {
                                    trailing!!
                                },
                                contentDescription = if (passwordVisible) {
                                    "Hide password"
                                } else {
                                    "Show password"
                                }
                            )
                        }
                    }
                } else null,
                leadingIcon =
                if (leading == null) null else {
                    {
                        Image(
                            painter = leading,
                            contentDescription = "",
                            modifier = Modifier
                                .size(24.dp)
                                .clickable {
                                    onTrailingIconClicked?.invoke()
                                },
                            colorFilter = ColorFilter.tint(orange28),
                        )
                    }
                }
            )
        }
    }
}
