package com.ultramovies.app

import android.net.Uri
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.media3.common.MediaItem
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.ui.PlayerView

private val Black = Color(0xFF101010)
private val Panel = Color(0xFF202020)
private val Red = Color(0xFFE50914)
private val Muted = Color(0xFFB8B8B8)

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent { UltraMoviesApp() }
    }
}

@Composable
fun UltraMoviesApp() {
    var videoUri by remember { mutableStateOf<Uri?>(null) }
    var videoUrl by remember { mutableStateOf("") }
    var showUrlDialog by remember { mutableStateOf(false) }
    var search by remember { mutableStateOf("") }
    val picker = rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) {
        if (it != null) videoUri = it
    }

    MaterialTheme(colorScheme = darkColorScheme(
        primary = Red, background = Black, surface = Panel,
        onBackground = Color.White, onSurface = Color.White
    )) {
        Column(
            Modifier.fillMaxSize().background(Black).padding(horizontal = 18.dp)
        ) {
            Spacer(Modifier.height(22.dp))
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text("ULTRA", color = Red, fontSize = 27.sp, fontWeight = FontWeight.Black)
                Text(" MOVIES", color = Color.White, fontSize = 27.sp, fontWeight = FontWeight.Black)
                Spacer(Modifier.weight(1f))
                Icon(Icons.Default.PlayArrow, contentDescription = null, tint = Red, modifier = Modifier.size(30.dp))
            }
            Spacer(Modifier.height(18.dp))
            Text("Unlimited entertainment. Your way.", color = Muted, fontSize = 14.sp)
            Spacer(Modifier.height(18.dp))
            OutlinedTextField(
                value = search, onValueChange = { search = it },
                modifier = Modifier.fillMaxWidth(),
                placeholder = { Text("Search your library") },
                leadingIcon = { Icon(Icons.Default.Search, contentDescription = "Search") },
                singleLine = true,
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = Red, unfocusedBorderColor = Color.DarkGray
                )
            )
            Spacer(Modifier.height(22.dp))
            Text("FEATURED", color = Red, fontWeight = FontWeight.Bold, fontSize = 12.sp)
            Spacer(Modifier.height(8.dp))
            Box(
                Modifier.fillMaxWidth().height(170.dp)
                    .background(Panel, RoundedCornerShape(14.dp)).padding(18.dp),
                contentAlignment = Alignment.CenterStart
            ) {
                Column {
                    Text("YOUR SCREEN.\nYOUR STORIES.", color = Color.White,
                        fontSize = 25.sp, lineHeight = 29.sp, fontWeight = FontWeight.ExtraBold)
                    Spacer(Modifier.height(8.dp))
                    Text("Play videos you own or have permission to stream.",
                        color = Muted, fontSize = 12.sp)
                }
            }
            Spacer(Modifier.height(20.dp))
            Text("WATCH", color = Color.White, fontSize = 18.sp, fontWeight = FontWeight.Bold)
            Spacer(Modifier.height(12.dp))
            Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                Button(
                    onClick = { picker.launch("video/*") },
                    colors = ButtonDefaults.buttonColors(containerColor = Red),
                    modifier = Modifier.weight(1f)
                ) {
                    Icon(Icons.Default.Add, contentDescription = null)
                    Spacer(Modifier.width(5.dp))
                    Text("Choose video")
                }
                OutlinedButton(
                    onClick = { showUrlDialog = true },
                    modifier = Modifier.weight(1f),
                    colors = ButtonDefaults.outlinedButtonColors(contentColor = Color.White)
                ) { Text("Add video URL") }
            }
            Spacer(Modifier.height(16.dp))
            if (videoUri != null) {
                Text("NOW PLAYING", color = Red, fontWeight = FontWeight.Bold, fontSize = 12.sp)
                Spacer(Modifier.height(8.dp))
                VideoPlayer(uri = videoUri!!)
            } else {
                Box(
                    Modifier.fillMaxWidth().weight(1f)
                        .background(Panel, RoundedCornerShape(12.dp)).padding(18.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text("Your video library is ready.\nChoose a video or add a direct video URL.",
                        color = Muted, fontSize = 14.sp, lineHeight = 22.sp)
                }
            }
            Spacer(Modifier.height(14.dp))
            Text("Ad-free player • Local videos • Direct legal streams",
                color = Muted, fontSize = 11.sp, modifier = Modifier.align(Alignment.CenterHorizontally))
            Spacer(Modifier.height(18.dp))
        }
        if (showUrlDialog) {
            AlertDialog(
                onDismissRequest = { showUrlDialog = false },
                title = { Text("Add a video URL") },
                text = {
                    Column {
                        Text("Paste a direct link to a video file or stream you are allowed to access.",
                            color = Muted, fontSize = 13.sp)
                        Spacer(Modifier.height(10.dp))
                        OutlinedTextField(
                            value = videoUrl, onValueChange = { videoUrl = it },
                            label = { Text("https://…") },
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Uri),
                            singleLine = true
                        )
                    }
                },
                confirmButton = {
                    TextButton(onClick = {
                        val trimmed = videoUrl.trim()
                        if (trimmed.startsWith("https://") || trimmed.startsWith("http://")) {
                            videoUri = Uri.parse(trimmed)
                            showUrlDialog = false
                        }
                    }) { Text("Play", color = Red) }
                },
                dismissButton = {
                    TextButton(onClick = { showUrlDialog = false }) { Text("Cancel") }
                },
                containerColor = Panel
            )
        }
    }
}

@Composable
fun VideoPlayer(uri: Uri) {
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
        factory = { ctx -> PlayerView(ctx).apply { this.player = player } },
        modifier = Modifier.fillMaxWidth().height(220.dp)
    )
}
