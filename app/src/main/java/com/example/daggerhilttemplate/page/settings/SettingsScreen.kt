package com.example.daggerhilttemplate.page.settings

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.twotone.CheckCircle
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.navigation.NavController
import com.example.daggerhilttemplate.constant.ServerAddressPreference
import com.example.daggerhilttemplate.page.widgets.CustomButton
import com.example.daggerhilttemplate.page.widgets.InputText

@Composable
fun SettingsScreen(navController: NavController, viewModel: SettingsViewModel) {
    val context = LocalContext.current
    val serverName = viewModel.repository.serverPreference
    val isServerActive = remember {
        mutableStateOf(false)
    }
    val serverAddress = remember {
        mutableStateOf(if (serverName.isLocalHost) "${serverName.host}:${serverName.port}/${serverName.path}"
        else serverName.webHost)
    }
    val changeAddress = remember {
        mutableStateOf(false)
    }
    val changeLocalAddress = remember {
        mutableStateOf(false)
    }
    val changeWebAddress = remember {
        mutableStateOf(false)
    }

    if (changeAddress.value) {
            ChooseOptionDialog(onDismiss = { changeAddress.value = false },
                onLocalHost = {
                    changeAddress.value = false
                    changeLocalAddress.value = true
                }) {
                changeAddress.value = false
                changeWebAddress.value = true
            }
    }
    if (changeLocalAddress.value) {
        UpdateLocalAddressDialog(serverAddressPreference = viewModel.repository.serverPreference,
            onDismiss = { changeLocalAddress.value = false }) { newAddress ->
            viewModel.updateLocalAddress(newAddress, onFailure = {
                isServerActive.value = false
                Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
            }) { newUrl ->
                isServerActive.value = true
                serverAddress.value = newUrl
            }
            changeLocalAddress.value = false
        }
    }

    if (changeWebAddress.value) {
        UpdateWebAddressDialog(serverAddressPreference = viewModel.repository.serverPreference,
            onDismiss = { changeWebAddress.value = false }) { newAddress ->
            viewModel.updateWebAddress(newAddress, onFailure = {
                isServerActive.value = false
                Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
            }) { newUrl ->
                isServerActive.value = true
                serverAddress.value = newUrl
            }
            changeWebAddress.value = false
        }
    }
    Surface(
        modifier = Modifier
            .background(Color.White)
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Spacer(modifier = Modifier.height(25.dp))

        Column(
            modifier = Modifier
                .background(Color.White)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(55.dp)
                    .background(Color(0xFFEEEFF1))
                    .clip(RoundedCornerShape(24.dp))
                    .padding(start = 8.dp, end = 8.dp)
                    .clickable { changeAddress.value = true },
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Start
            ) {
                Box {
                    Text(text = serverAddress.value,
                        color = Color(0xFF000000).copy(alpha = 0.5f),
                        maxLines = 1,
                        style = TextStyle(
                            fontWeight = FontWeight.Light,
                            fontSize = 23.sp
                        )
                    )
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.End
                    ) {
                        Column(
                            modifier = Modifier
                                .size(35.dp)
                                .padding(5.dp)
                                .background(Color(0xFFEEEFF1))
                        ) {
                            Icon(imageVector = Icons.TwoTone.CheckCircle,
                                contentDescription = "Status Icon",
                                modifier = Modifier
                                    .size(25.dp),
                                tint = if (isServerActive.value) Color(0xFF4CAF50).copy(alpha = 1f) else Color.Red.copy(alpha = 0.5f)
                            )
                        }
                    }
                }
            }
        }
    }
}


@Composable
fun UpdateLocalAddressDialog(serverAddressPreference: ServerAddressPreference,
                        onDismiss: () -> Unit,
                        onSubmit: (ServerAddressPreference) -> Unit) {
    val host = remember {
        mutableStateOf(serverAddressPreference.host)
    }
    val port = remember {
        mutableStateOf(serverAddressPreference.port)
    }
    val path = remember {
        mutableStateOf(serverAddressPreference.path)
    }

    val interactHost = remember {
        MutableInteractionSource()
    }
    val interactPort = remember {
        MutableInteractionSource()
    }
    val interactPath = remember {
        MutableInteractionSource()
    }
    val isHostBlank = remember {
        mutableStateOf(false)
    }
    val isPortBlank = remember {
        mutableStateOf(false)
    }
    val isPathBlank = remember {
        mutableStateOf(false)
    }

    Dialog(onDismissRequest = { onDismiss() }) {
        Surface(modifier = Modifier
            .fillMaxWidth()
            .heightIn()
            .background(MaterialTheme.colorScheme.background.copy(alpha = 0.0f)),
            shape = RoundedCornerShape(12.dp)
        ) {
            Column(modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)) {
                val inputFieldsModifier = Modifier
                    .fillMaxHeight()
                    .fillMaxWidth()
                    .background(
                        shape = RoundedCornerShape(corner = CornerSize(6.dp)),
                        color = Color.Transparent
                    )

                Spacer(modifier = Modifier.height(10.dp))

                Column(modifier = Modifier
                    .fillMaxWidth()
                    .padding(5.dp),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(text = "Update Local Server Address", fontSize = 18.sp,
                        color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.8f),
                        style = MaterialTheme.typography.headlineSmall)
                }

                Spacer(modifier = Modifier.height(10.dp))

                Column(modifier = Modifier
                    .fillMaxWidth()
                    .height(55.dp)) {
                    val keyboardType = KeyboardType.Number
                    InputText(text = host.value,
                        label = if (host.value.isEmpty()) "Host" else "",
                        keyboardType = keyboardType,
                        modifier = inputFieldsModifier,
                        interactionSource = interactHost,
                        isFieldBlank = isHostBlank.value,
                        onTextChange = {
                            if (it.all { char ->
                                    char.isDigit() || char.isLetter() || char == '.'
                                }) host.value = it
                            isHostBlank.value = false
                        })
                }

                Spacer(modifier = Modifier.height(10.dp))

                Column(modifier = Modifier
                    .fillMaxWidth()
                    .height(55.dp)
                    .background(
                        shape = RoundedCornerShape(corner = CornerSize(6.dp)),
                        color = Color(0xFFEEEFF1)
                    )) {
                    val keyboardType = KeyboardType.Number
                    InputText(text = port.value,
                        label = if (port.value.isEmpty()) "Port" else "",
                        keyboardType = keyboardType,
                        modifier = inputFieldsModifier,
                        interactionSource = interactPort,
                        isFieldBlank = isPortBlank.value,
                        onTextChange = {
                            if (it.all { char ->
                                    char.isDigit()
                                }) port.value = it
                            isPortBlank.value = false
                        })
                }

                Spacer(modifier = Modifier.height(10.dp))

                Column(modifier = Modifier
                    .fillMaxWidth()
                    .height(55.dp)) {
                    val keyboardType = KeyboardType.Text
                    InputText(text = path.value,
                        label = if (path.value.isEmpty()) "Path" else "",
                        keyboardType = keyboardType,
                        modifier = inputFieldsModifier,
                        interactionSource = interactPath,
                        isFieldBlank = isPathBlank.value,
                        onTextChange = {
                            path.value = it
                            isPathBlank.value = false
                        })
                }

                Spacer(modifier = Modifier.height(20.dp))

                Column(modifier = Modifier
                    .fillMaxWidth()
                    .height(80.dp)) {
                    val buttonModifier = Modifier.height(60.dp)
                    CustomButton(buttonText = "Done",
                        modifier = buttonModifier) {
                        if (host.value.isBlank()) isHostBlank.value = true
                        if (port.value.isBlank()) isPortBlank.value = true
                        if (path.value.isBlank()) isPathBlank.value = true
                        if (host.value.isNotBlank() && port.value.isNotBlank() && path.value.isNotBlank()) {
                            onSubmit(ServerAddressPreference(isLocalHost = true, host = host.value, port = port.value, path = path.value))
                        }
                    }
                }
            }
        }
    }
}


@Composable
fun UpdateWebAddressDialog(serverAddressPreference: ServerAddressPreference,
                             onDismiss: () -> Unit,
                             onSubmit: (ServerAddressPreference) -> Unit) {
    val webHost = remember {
        mutableStateOf(serverAddressPreference.webHost)
    }
    val interactWebHost = remember {
        MutableInteractionSource()
    }
    val isWebHostBlank = remember {
        mutableStateOf(false)
    }

    Dialog(onDismissRequest = { onDismiss() }) {
        Surface(modifier = Modifier
            .fillMaxWidth()
            .heightIn()
            .background(MaterialTheme.colorScheme.background.copy(alpha = 0.0f)),
            shape = RoundedCornerShape(12.dp)
        ) {
            Column(modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)) {
                val inputFieldsModifier = Modifier
                    .fillMaxHeight()
                    .fillMaxWidth()
                    .background(
                        shape = RoundedCornerShape(corner = CornerSize(6.dp)),
                        color = Color.Transparent
                    )

                Spacer(modifier = Modifier.height(10.dp))

                Column(modifier = Modifier
                    .fillMaxWidth()
                    .padding(5.dp),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(text = "Update Web Server Address", fontSize = 18.sp,
                        color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.8f),
                        style = MaterialTheme.typography.headlineSmall)
                }

                Spacer(modifier = Modifier.height(10.dp))

                Column(modifier = Modifier
                    .fillMaxWidth()
                    .height(55.dp)) {
                    val keyboardType = KeyboardType.Text
                    InputText(text = webHost.value,
                        label = if (webHost.value.isEmpty()) "Web Host" else "",
                        keyboardType = keyboardType,
                        modifier = inputFieldsModifier,
                        interactionSource = interactWebHost,
                        isFieldBlank = isWebHostBlank.value,
                        onTextChange = {
                            webHost.value = it
                            isWebHostBlank.value = false
                        })
                }

                Spacer(modifier = Modifier.height(20.dp))

                Column(modifier = Modifier
                    .fillMaxWidth()
                    .height(80.dp)) {
                    val buttonModifier = Modifier.height(60.dp)
                    CustomButton(buttonText = "Done",
                        modifier = buttonModifier) {
                        if (webHost.value.isBlank()) isWebHostBlank.value = true
                        if (webHost.value.isNotBlank()) {
                            onSubmit(ServerAddressPreference(isLocalHost = false, webHost = webHost.value))
                        }
                    }
                }
            }
        }
    }
}


@Composable
fun ChooseOptionDialog(onDismiss: () -> Unit,
                           onLocalHost: () -> Unit,
                       onWebHost: () -> Unit) {

    Dialog(onDismissRequest = { onDismiss() }) {
        Surface(modifier = Modifier
            .fillMaxWidth()
            .heightIn()
            .background(MaterialTheme.colorScheme.background.copy(alpha = 0.0f)),
            shape = RoundedCornerShape(12.dp)
        ) {
            Column(modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)) {

                Spacer(modifier = Modifier.height(10.dp))

                Column(modifier = Modifier
                    .fillMaxWidth()
                    .padding(5.dp),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(text = "Select An Option", fontSize = 23.sp,
                        color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.8f),
                        style = MaterialTheme.typography.headlineSmall)
                }

                Spacer(modifier = Modifier.height(18.dp))

                Column(modifier = Modifier
                    .fillMaxWidth()
                    .height(80.dp),
                    horizontalAlignment = Alignment.CenterHorizontally) {
                    val buttonModifier = Modifier
                        .height(60.dp)
                    CustomButton(buttonText = "Local Host",
                        modifier = buttonModifier) {
                        onLocalHost()
                    }
                }

                Column(modifier = Modifier
                    .fillMaxWidth()
                    .height(80.dp),
                    horizontalAlignment = Alignment.CenterHorizontally) {
                    val buttonModifier = Modifier
                        .height(60.dp)
                    CustomButton(buttonText = "Web Host",
                        modifier = buttonModifier) {
                        onWebHost()
                    }
                }
            }
        }
    }
}