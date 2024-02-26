package dev.korryr.bongesha.commons

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import dev.korryr.bongesha.ui.theme.orange100


@OptIn(ExperimentalComposeUiApi::class, ExperimentalMaterial3Api::class)
@Composable
fun Bongatextfield(
    label: String,
    isPassword: Boolean,
    boldlabel: Boolean = true,
    fieldDescription: String,
    isValid: Boolean = true,
    isLongText:Boolean = false,
    input: String,
    hint: String,
    onChange: (String) -> Unit,
    errorMessage: String = "Error occur",
    enabled: Boolean = true,
    readOnly: Boolean = false,
    onDone: () -> Unit = {},
    keyboardOptions: KeyboardOptions= KeyboardOptions(keyboardType = KeyboardType.Text),
    keyboardActions: KeyboardActions? = null,
    trailingIcon: @Composable (() -> Unit)? = null

){
    val current = LocalFocusManager.current
    val softwareKeyboard = LocalSoftwareKeyboardController.current
    Column(
        modifier = Modifier,
        horizontalAlignment = Alignment.Start,
        verticalArrangement = Arrangement.Center,
    ) {

        Text(
            text = label,
            color = Color.Transparent,
            fontWeight = if (boldlabel) FontWeight.Bold else FontWeight.Normal,
        )
        if (fieldDescription.isNotEmpty()){
            Text(text = fieldDescription,
                color = if (isValid) Color.Green else Color.Red
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            trailingIcon?.let { icon ->
            OutlinedTextField(
                visualTransformation = if (isPassword) PasswordVisualTransformation()
                else VisualTransformation.None,
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
                    .height(if (isLongText) 150.dp else TextFieldDefaults.MinHeight)
                    .fillMaxWidth()
                    .testTag(label + "1"),
                colors = TextFieldDefaults.outlinedTextFieldColors(
                    disabledPlaceholderColor = Color.Red,
                    focusedBorderColor = orange100,
                    unfocusedBorderColor = Color.Gray,
                    containerColor = Color.Transparent,
                    unfocusedLabelColor = Color.Black,
                    focusedTextColor = Color.Black,
                    unfocusedTextColor = Color.Blue,
                    disabledTextColor = Color.Red,
                    errorTextColor = Color.Red,
                    errorContainerColor = Color.Red,
                    cursorColor = Color.Black,
                    errorCursorColor = Color.Red,
                    disabledBorderColor = Color.Red,
                    errorBorderColor = Color.Red,
                    focusedLeadingIconColor = Color.Gray,
                    unfocusedLeadingIconColor = Color.LightGray,
                    disabledLeadingIconColor = Color.Red,
                    errorLeadingIconColor = Color.Red,
                    focusedTrailingIconColor = Color.Red,
                    unfocusedTrailingIconColor = Color.Red,
                    disabledTrailingIconColor = Color.Red,
                    errorTrailingIconColor = Color.Red,
                    focusedLabelColor = Color.Red,
                    disabledLabelColor = Color.Red,
                    errorLabelColor = Color.Red,
                    focusedPlaceholderColor = Color.Gray,
                    unfocusedPlaceholderColor = Color.DarkGray,
                    errorPlaceholderColor = Color.Red,
                    focusedSupportingTextColor = Color.Red,
                    unfocusedSupportingTextColor = Color.Red,
                    disabledSupportingTextColor = Color.Red,
                    errorSupportingTextColor = Color.Red,
                    focusedPrefixColor = Color.Red,
                    unfocusedPrefixColor = Color.Red,
                    disabledPrefixColor = Color.Red,
                    errorPrefixColor = Color.Red,
                    focusedSuffixColor = Color.Red,
                    unfocusedSuffixColor = Color.Red,
                    disabledSuffixColor = Color.Red,
                    errorSuffixColor = Color.Red
                ),
                shape = RoundedCornerShape(12.dp),
                keyboardOptions = keyboardOptions,
                placeholder = {
                    Text(
                        text = hint,
                        textAlign = TextAlign.Center,
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
                trailingIcon = icon,
            )
        }
        }
    }
}