package com.example.gymappsas.util

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
    fun StyledInputField(
        input: String,
        onInputChange: (String) -> Unit,
        placeholder: String,
        modifier: Modifier = Modifier,
        singleLine: Boolean = true,
        height: Int? = null,
        hasErrors: Boolean = false,
        errorMessage: String = ""
    ) {
        val shape = RoundedCornerShape(8.dp)

        TextField(
            value = input,
            onValueChange = onInputChange,
            modifier = modifier
                .fillMaxWidth()
                .then(if (height != null) Modifier.height(height.dp) else Modifier)
                .border(
                    width = 1.dp,
                    color = if (hasErrors) Color.Red else Color.LightGray,
                    shape = shape
                )
                .clip(shape)
                .background(Color.White),
            placeholder = {
                Text(
                    text = placeholder,
                    color = Color.LightGray,
                    fontSize = 14.sp
                )
            },
            singleLine = singleLine,
            textStyle = TextStyle(
                fontSize = 16.sp,
                color = Color.Black,
                lineHeight = 20.sp // helps top-alignment
            ),
            shape = shape,
            colors = TextFieldDefaults.colors(
                focusedContainerColor = Color.White,
                unfocusedContainerColor = Color.White,
                disabledContainerColor = Color.White,
                errorContainerColor = Color.White,
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
                disabledIndicatorColor = Color.Transparent,
                errorIndicatorColor = Color.Transparent,
                focusedPlaceholderColor = Color.LightGray,
                unfocusedPlaceholderColor = Color.LightGray,
                disabledPlaceholderColor = Color.LightGray,
                errorPlaceholderColor = Color.LightGray,
                focusedTextColor = Color.Black,
                unfocusedTextColor = Color.Black,
                disabledTextColor = Color.Black,
                errorTextColor = Color.Red,
                cursorColor = Color.Black,
                errorCursorColor = Color.Red
            ),
            isError = hasErrors,
            maxLines = if (singleLine) 1 else Int.MAX_VALUE,
            keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Done)
        )

        if (hasErrors) {
            Text(
                text = errorMessage,
                color = Color.Red,
                fontSize = 12.sp,
                modifier = Modifier.padding(start = 16.dp, top = 4.dp)
            )
        }
    }

    @Composable
    fun TopAlignedInputField(
        input: String,
        onInputChange: (String) -> Unit,
        placeholder: String,
        modifier: Modifier = Modifier,
        hasErrors: Boolean = false,
        errorMessage: String = ""
    ) {
        val shape = RoundedCornerShape(8.dp)

        Box(
            modifier = modifier
                .fillMaxWidth()
                .height(144.dp)
                .border(
                    width = 1.dp,
                    color = if (hasErrors) Color.Red else Color.LightGray,
                    shape = shape
                )
                .clip(shape)
                .background(Color.White)
                .padding(12.dp) // padding inside the box
        ) {
            BasicTextField(
                value = input,
                onValueChange = onInputChange,
                textStyle = TextStyle(
                    fontSize = 16.sp,
                    color = Color.Black,
                    lineHeight = 20.sp
                ),
                modifier = Modifier
                    .fillMaxSize(),
                maxLines = Int.MAX_VALUE,
                decorationBox = { innerTextField ->
                    Column {
                        if (input.isEmpty()) {
                            Text(
                                text = placeholder,
                                color = Color.LightGray,
                                fontSize = 14.sp
                            )
                        }
                        innerTextField()
                    }
                }
            )
        }

        if (hasErrors) {
            Text(
                text = errorMessage,
                color = Color.Red,
                fontSize = 12.sp,
                modifier = Modifier.padding(start = 16.dp, top = 4.dp)
            )
        }
    }


