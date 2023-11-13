package com.example.daggerhilttemplate.page.widgets


import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp


@Composable
fun CustomButton(
    buttonText: String,
    backgroundColor: Color= Color(0xFF0098A6),
    textColor: Color = Color(0xFFE4EAEB),
    modifier: Modifier = Modifier,
    onClick: () -> Unit) {

    Column(modifier = modifier) {
        Button(onClick = { onClick() },
            modifier = modifier
                .padding(3.dp)
                .height(50.dp)
                .fillMaxWidth(),
            shape = RoundedCornerShape(8.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = backgroundColor
            )) {
            Text(text = buttonText,
                modifier = Modifier.padding(4.dp),
                color = textColor,
                style = TextStyle(fontWeight = FontWeight.Bold,
                    fontSize = 18.sp)
            )
        }
    }
}