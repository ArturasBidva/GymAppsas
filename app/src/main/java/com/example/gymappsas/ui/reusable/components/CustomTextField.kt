package com.example.gymappsas.ui.reusable.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.gymappsas.ui.reusable.quicksandMedium


@Composable
fun CustomTextField(
    value: String?,
    onValueChange: (String) -> Unit = {},
    isError: Boolean = false,
    modifier: Modifier = Modifier,
    hintText: String = "",
    maxLines: Int = 1,
    keyboardType: KeyboardType = KeyboardType.Text,
    painterId: Int? = null,
    isDisabled: Boolean = false,
    onClick: () -> Unit = {},
    isClickable: Boolean = false
) {
    Box(
        modifier = modifier
            .border(
                BorderStroke(1.dp, if (isError) Color.Red else Color(0xFFEDF1F7)),
                shape = RoundedCornerShape(10.dp)
            )
            .padding(horizontal = 10.dp, vertical = 14.dp)
            .let { if (isClickable) it.clickable { onClick() } else it }
    ) {
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(modifier = Modifier.weight(1f)) {
                if (value.isNullOrEmpty()) {
                    Text(
                        text = hintText,
                        color = Color.Gray.copy(alpha = 0.7f),
                        fontFamily = quicksandMedium,
                        modifier = Modifier.align(Alignment.CenterStart)
                    )
                }
                BasicTextField(
                    value = value ?: "",
                    enabled = !isDisabled,
                    onValueChange = {
                        if (it.length <= 30) {
                            onValueChange(it)
                        }
                    },
                    maxLines = maxLines,
                    singleLine = true,
                    keyboardOptions = KeyboardOptions.Default.copy(keyboardType = keyboardType),
                    modifier = Modifier.fillMaxWidth()
                )
            }
            painterId?.let { painterResource(it) }?.let {
                Icon(
                    painter = it,
                    tint = Color(0xFF8F9BB3),
                    contentDescription = null,
                )
            }
        }
    }
}

@Preview
@Composable
fun CustomTextFieldPrev() {
    CustomTextField(
        value = "kazkas",
        onValueChange = {},
        modifier = Modifier
    )
}