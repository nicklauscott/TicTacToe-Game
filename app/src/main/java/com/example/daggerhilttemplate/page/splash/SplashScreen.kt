package com.example.daggerhilttemplate.page.splash

import android.view.animation.OvershootInterpolator
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.daggerhilttemplate.TicTacToeField
import com.example.daggerhilttemplate.constant.Screens
import com.example.daggerhilttemplate.data.GameState
import kotlinx.coroutines.delay

@Composable
fun SplashScreen(navController: NavController) {
    val viewModel = hiltViewModel<SplashScreenViewModel>()
    val scale = remember {
        Animatable(0f)
    }

    LaunchedEffect(key1 = true) {
        scale.animateTo(
            targetValue = 0.5f,
            animationSpec = tween(
                durationMillis = 800,
                easing = {
                    OvershootInterpolator(2f).getInterpolation(it)
                }
            )
        )
        viewModel.pingServer(onFailure = {
            // failure
            navController.navigate(Screens.SettingsScreen.route)
        }) {
            // success
            navController.navigate(Screens.HomeScreen.route)
        }

    }
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .fillMaxSize()
            .background(color = Color.White)
    ) {
        val field = mutableListOf<Char?>(
            'O', null, 'O',
            null, 'X', null,
            'X', 'X', 'O',
        )
        field.shuffle()
        TicTacToeField(state = GameState(
            field = arrayOf(
                arrayOf(field[0], field[1], field[2]),
                arrayOf(field[3], field[4], field[5]),
                arrayOf(field[6], field[7], field[8])
            )
        ),
            onTapInField = { _, _ ->  },
            modifier = Modifier
                .scale(scale.value)
                .fillMaxWidth()
                .aspectRatio(1f)
                .padding(16.dp))
    }
}
