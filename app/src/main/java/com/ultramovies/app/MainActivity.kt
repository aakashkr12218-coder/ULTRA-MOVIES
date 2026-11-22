
package com.ultramovies.app

import android.net.Uri
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.viewinterop.AndroidView
import androidx.media3.common.MediaItem
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.ui.PlayerView

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            UltraMovies()
        }
    }
}

@Composable
fun UltraMovies() {
    var video by remember { mutableStateOf<Uri?>(null) }

    val picker = rememberLauncherForActivityResult(
        ActivityResultContracts.GetContent()
    ) {
        if (it != null) video = it
    }

    val red = Color(0xFFE50914)
    val black = Color(0xFF101010)

    MaterialTheme(
        colorScheme = darkColorScheme(
            primary = red,
            background = black,
            surface = Color(0xFF222222)
        )
    ) {
        Column(
            Modifier
                .fillMaxSize()
                .background(black)
                .padding(20.dp)
        ) {
            Spacer(Modifier.height(25.dp))

            Text(
                "ULTRA MOVIES",
                color = red,
                fontSize = 28.sp,
                fontWeight = FontWeight.Black
            )

            Text(
                "Entertainment without limits",
                color = Color.LightGray
            )

            Spacer(Modifier.height(30.dp))

            Text(
                "WELCOME",
                color = Color.White,
                fontSize = 25.sp,
                fontWeight = FontWeight.Bold
            )

            Spacer(Modifier.height(15.dp))

            Box(
                Modifier
                    .fillMaxWidth()
                    .height(160.dp)
                    .background(
                        Color(0xFF292929),
                        RoundedCornerShape(14.dp)
                    ),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    "YOUR ENTERTAINMENT HUB",
                    color = Color.White,
                    fontSize = 18.sp
                )
            }

            Spacer(Modifier.height(25.dp))

            Button(
                onClick = { picker.launch("video/*") },
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(
                    containerColor = red
                )
            ) {
                Text("CHOOSE VIDEO")
            }

            Spacer(Modifier.height(20.dp))

            video?.let {
                VideoScreen(it)
            }

            Spacer(Modifier.weight(1f))

            Text(
                "ULTRA MOVIES • AD-FREE PLAYER",
                color = Color.Gray,
                fontSize = 12.sp,
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )
        }
    }
}

@Composable
fun VideoScreen(uri: Uri) {
    val context = androidx.compose.ui.platform.LocalContext.current

    val player = remember(uri) {
        ExoPlayer.Builder(context).build().apply {
            setMediaItem(MediaItem.fromUri(uri))
            prepare()
            playWhenReady = true
        }
    }

    DisposableEffect(player) {
        onDispose { player.release() }
    }

    AndroidView(
        factory = { PlayerView(it).apply {
            this.player = player
        }},
        modifier = Modifier
            .fillMaxWidth()
            .height(230.dp)
    )
}
