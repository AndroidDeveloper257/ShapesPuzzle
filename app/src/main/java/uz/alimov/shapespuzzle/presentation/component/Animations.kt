package uz.alimov.shapespuzzle.presentation.component

import androidx.annotation.RawRes
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.animateLottieCompositionAsState
import com.airbnb.lottie.compose.rememberLottieComposition
import kotlinx.coroutines.delay
import uz.alimov.shapespuzzle.R

@Composable
fun DisplayLottieAnimation(
    @RawRes resId: Int,
    duration: Long = 2000L,
    onDismiss: () -> Unit
) {
    val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(resId))
    val progress by animateLottieCompositionAsState(
        composition,
        iterations = 1,
        speed = 1.5f
    )

    LaunchedEffect(Unit) {
        delay(duration)
        onDismiss()
    }

    Box(
        modifier = Modifier
            .fillMaxSize(),
        contentAlignment = Alignment.BottomCenter
    ) {
        LottieAnimation(
            modifier = Modifier
                .size(150.dp)
                .padding(bottom = 40.dp),
            composition = composition,
            progress = progress
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Congratulate(
    onBack: () -> Unit
) {
    val triumphComposition by rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.victory))
    val confettiComposition by rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.confetti))

    val triumphProgress by animateLottieCompositionAsState(
        composition = triumphComposition,
        iterations = LottieConstants.IterateForever,
        speed = 1.5f
    )

    val confettiProgress by animateLottieCompositionAsState(
        composition = confettiComposition,
        iterations = LottieConstants.IterateForever,
        speed = 1f
    )

    ModalBottomSheet(
        modifier = Modifier.fillMaxWidth(),
        onDismissRequest = onBack
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 10.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                modifier = Modifier
                    .size(300.dp)
                    .fillMaxWidth()
            ) {
                LottieAnimation(
                    composition = confettiComposition,
                    progress = confettiProgress,
                    modifier = Modifier.fillMaxSize()
                )
                LottieAnimation(
                    composition = triumphComposition,
                    progress = triumphProgress,
                    modifier = Modifier.fillMaxSize()
                )
            }
            Button(
                modifier = Modifier.fillMaxWidth(),
                onClick = onBack
            ) {
                Icon(
                    imageVector = Icons.Filled.KeyboardArrowLeft,
                    contentDescription = null
                )
            }
        }
    }
}