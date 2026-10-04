package com.example.videoshortapp

import android.Manifest
import android.content.ContentValues
import android.content.Context
import android.content.pm.PackageManager
import android.media.MediaRecorder
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.provider.MediaStore
import android.util.Log
import android.view.SurfaceTexture
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.camera.core.*
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.video.FileOutputOptions
import androidx.camera.video.Quality
import androidx.camera.video.QualitySelector
import androidx.camera.video.Recorder
import androidx.camera.video.VideoCapture
import androidx.camera.video.Recording
import androidx.camera.view.PreviewView
import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.compose.ui.zIndex
import androidx.core.content.ContextCompat
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.compose.LocalLifecycleOwner
import java.io.File
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.Executor
import java.util.concurrent.Executors
import kotlin.math.roundToInt

// ========== DATA MODELS ==========

data class VideoEffect(
    val id: String,
    val name: String,
    val color: Color,
    val description: String,
    val type: String = \"overlay\"
)

data class VideoTemplate(
    val id: String,
    val name: String,
    val category: String,
    val style: String,
    val effectName: String,
    val musicTag: String,
    val color: Color,
    val duration: Long = 15000L
)

data class VideoSegment(
    val id: String,
    val filePath: String,
    val duration: Long,
    val effectId: String = \"fx1\",
    val startTime: Long = 0L,
    val endTime: Long = 0L,
    val speedMultiplier: Float = 1f
)

data class TimelineFrame(
    val segmentId: String,
    val position: Long,
    val thumbnail: String = \"\"
)

// ========== CATALOGS ==========

object VideoEffectsCatalog {
    val list = listOf(
        VideoEffect(\"fx1\", \"Glow\", Color(0xFF66BB6A), \"Soft atmospheric glow\", \"glow\"),
        VideoEffect(\"fx2\", \"Neon\", Color(0xFF00E5FF), \"Electric colorful glow\", \"neon\"),
        VideoEffect(\"fx3\", \"Blur\", Color(0xFF9E9E9E), \"Depth blur for cinematic look\", \"blur\"),
        VideoEffect(\"fx4\", \"Matrix\", Color(0xFF4CAF50), \"Cyber look\", \"matrix\"),
        VideoEffect(\"fx5\", \"VHS\", Color(0xFFFF7043), \"Old tape distortion\", \"vhs\"),
        VideoEffect(\"fx6\", \"Mirror\", Color(0xFF64B5F6), \"Reflective mirror frame\", \"mirror\"),
        VideoEffect(\"fx7\", \"Golden\", Color(0xFFFFC107), \"Golden warm highlight\", \"golden\"),
        VideoEffect(\"fx8\", \"Depth\", Color(0xFF8E24AA), \"3D depth focus\", \"depth\"),
        VideoEffect(\"fx9\", \"B&W\", Color(0xFFBDBDBD), \"Classic monochrome\", \"bw\"),
        VideoEffect(\"fx10\", \"Smooth\", Color(0xFFAB47BC), \"Soft polish\", \"smooth\"),
        VideoEffect(\"fx11\", \"Sepia\", Color(0xFFA1887F), \"Vintage sepia tone\", \"sepia\"),
        VideoEffect(\"fx12\", \"Glitch\", Color(0xFFFF00FF), \"Digital glitch effect\", \"glitch\")
    )
}

object VideoTemplateCatalog {
    val list = listOf(
        VideoTemplate(\"t1\", \"Trend Glow\", \"Trending\", \"Cinematic\", \"Glow\", \"Viral\", Color(0xFF5D5FEF)),
        VideoTemplate(\"t2\", \"Snap Neon\", \"Snap\", \"Neon\", \"Neon\", \"Dance\", Color(0xFF00E676)),
        VideoTemplate(\"t3\", \"Clarity FX\", \"Beauty\", \"Clean\", \"HD\", \"Soft\", Color(0xFF4FC3F7)),
        VideoTemplate(\"t4\", \"Matrix Cut\", \"Tech\", \"Dark\", \"Matrix\", \"Cyber\", Color(0xFF00BCD4)),
        VideoTemplate(\"t5\", \"Golden Hour\", \"Lifestyle\", \"Warm\", \"Golden\", \"Travel\", Color(0xFFFFB300)),
        VideoTemplate(\"t6\", \"Dream Blur\", \"Beauty\", \"Soft\", \"Blur\", \"Romantic\", Color(0xFFEC407A)),
        VideoTemplate(\"t7\", \"VHS Pulse\", \"Retro\", \"Scratch\", \"VHS\", \"Retro\", Color(0xFFFF7043)),
        VideoTemplate(\"t8\", \"Cinematic 3D\", \"Cinematic\", \"3D\", \"Depth\", \"Epic\", Color(0xFF7C4DFF)),
        VideoTemplate(\"t9\", \"Night Mirror\", \"Snap\", \"Mirror\", \"Mirror\", \"Club\", Color(0xFF1E88E5)),
        VideoTemplate(\"t10\", \"Sunset Loop\", \"Travel\", \"Warm\", \"Sunset\", \"Summer\", Color(0xFFFFA726))
    )
}

// ========== MAIN ACTIVITY ==========

class MainActivity : ComponentActivity() {

    private val cameraPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { granted ->
        val allGranted = granted.all { it.value }
        if (!allGranted) {
            Toast.makeText(this, \"Permissions required\", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val required = arrayOf(
                Manifest.permission.CAMERA,
                Manifest.permission.RECORD_AUDIO,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            )
            val missing = required.filter {
                ContextCompat.checkSelfPermission(this, it) != PackageManager.PERMISSION_GRANTED
            }
            if (missing.isNotEmpty()) {
                cameraPermissionLauncher.launch(missing.toTypedArray())
            }
        }

        setContent {
            VideoShortFormApp()
        }
    }
}

// ========== MAIN APP SCREEN ==========

@Composable
fun VideoShortFormApp() {
    var currentScreen by remember { mutableStateOf(\"record\") }
    var selectedTemplate by remember { mutableStateOf(VideoTemplateCatalog.list.first()) }
    var selectedEffect by remember { mutableStateOf(VideoEffectsCatalog.list.first()) }
    var videoSegments by remember { mutableStateOf(listOf<VideoSegment>()) }

    Box(modifier = Modifier.fillMaxSize().background(Color.Black)) {
        when (currentScreen) {
            \"record\" -> RecordingScreen(
                selectedTemplate = selectedTemplate,
                selectedEffect = selectedEffect,
                onTemplateChange = { selectedTemplate = it },
                onEffectChange = { selectedEffect = it },
                onScreenChange = { currentScreen = it },
                onVideoSegmentAdded = { videoSegments = videoSegments + it }
            )
            \"edit\" -> TimelineEditorScreen(
                segments = videoSegments,
                onScreenChange = { currentScreen = it }
            )
            \"gallery\" -> GalleryScreen(
                onScreenChange = { currentScreen = it }
            )
        }
    }
}

// ========== RECORDING SCREEN ==========

@Composable
fun RecordingScreen(
    selectedTemplate: VideoTemplate,
    selectedEffect: VideoEffect,
    onTemplateChange: (VideoTemplate) -> Unit,
    onEffectChange: (VideoEffect) -> Unit,
    onScreenChange: (String) -> Unit,
    onVideoSegmentAdded: (VideoSegment) -> Unit
) {
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current

    var isRecording by remember { mutableStateOf(false) }
    var isFrontCamera by remember { mutableStateOf(true) }
    var zoomLevel by remember { mutableFloatStateOf(1f) }
    var recordingTime by remember { mutableIntStateOf(0) }
    var recordingFile: File? by remember { mutableStateOf(null) }

    val effectList = VideoEffectsCatalog.list
    val templateList = VideoTemplateCatalog.list

    LaunchedEffect(isRecording) {
        if (isRecording) {
            while (isRecording) {
                delay(1000)
                recordingTime += 1
            }
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
    ) {
        CameraPreviewComposable(
            modifier = Modifier.fillMaxSize(),
            isFrontCamera = isFrontCamera,
            zoom = zoomLevel,
            effect = selectedEffect
        )

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .statusBarsPadding()
                .padding(12.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Surface(
                    color = Color(0xCC1A1A1A),
                    shape = RoundedCornerShape(18.dp)
                ) {
                    Text(
                        text = \"VideoApp\",
                        modifier = Modifier.padding(12.dp, 6.dp),
                        color = Color.White,
                        fontWeight = FontWeight.Bold
                    )
                }

                Surface(
                    color = if (isRecording) Color(0xFFEF5350) else Color(0xCC1A1A1A),
                    shape = RoundedCornerShape(18.dp)
                ) {
                    Text(
                        text = if (isRecording) \"REC \${recordingTime}s\" else \"READY\",
                        modifier = Modifier.padding(12.dp, 6.dp),
                        color = Color.White,
                        fontWeight = FontWeight.Bold,
                        fontSize = 12.sp
                    )
                }
            }
        }

        Column(
            modifier = Modifier
                .align(Alignment.CenterEnd)
                .padding(end = 12.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            FloatingActionChip(icon = Icons.Default.FlashOn, label = \"Flash\") { }
            FloatingActionChip(icon = Icons.Default.Timer, label = \"Timer\") { }
            FloatingActionChip(icon = Icons.Default.SwitchCamera, label = \"Flip\") {
                isFrontCamera = !isFrontCamera
            }
            FloatingActionChip(icon = Icons.Default.AutoFixHigh, label = \"Beauty\") { }
            FloatingActionChip(icon = Icons.Default.Settings, label = \"More\") { }
        }

        Column(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
                .padding(bottom = 18.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 12.dp)
            ) {
                items(templateList) { template ->
                    TemplateChip(
                        template = template,
                        selected = selectedTemplate.id == template.id,
                        onSelect = { onTemplateChange(template) }
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 12.dp)
            ) {
                items(effectList) { effect ->
                    EffectChip(
                        effect = effect,
                        selected = selectedEffect.id == effect.id,
                        onSelect = { onEffectChange(effect) }
                    )
                }
            }

            Spacer(modifier = Modifier.height(18.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Surface(
                    modifier = Modifier.size(52.dp),
                    shape = CircleShape,
                    color = Color(0xCC1C1C1C)
                ) {
                    Box(contentAlignment = Alignment.Center, modifier = Modifier.clickable {
                        onScreenChange(\"gallery\")
                    }) {
                        Icon(
                            imageVector = Icons.Default.Collections,
                            contentDescription = null,
                            tint = Color.White
                        )
                    }
                }

                Box(
                    modifier = Modifier
                        .size(82.dp)
                        .clickable {
                            isRecording = !isRecording
                            recordingTime = 0
                        }
                        .background(
                            if (isRecording) Color(0xFFEF5350) else Color.White,
                            CircleShape
                        )
                        .border(8.dp, Color(0xFF212121), CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    if (!isRecording) {
                        Box(
                            modifier = Modifier
                                .size(52.dp)
                                .background(Color(0xFFEF5350), CircleShape)
                        )
                    }
                }

                Surface(
                    modifier = Modifier.size(52.dp),
                    shape = CircleShape,
                    color = Color(0xCC1C1C1C)
                ) {
                    Box(contentAlignment = Alignment.Center, modifier = Modifier.clickable {
                        if (recordingTime > 0) {
                            onScreenChange(\"edit\")
                        }
                    }) {
                        Icon(
                            imageVector = Icons.Default.Edit,
                            contentDescription = null,
                            tint = if (recordingTime > 0) Color.White else Color(0xFF666666)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Surface(
                color = Color(0xAA000000),
                shape = RoundedCornerShape(20.dp),
                modifier = Modifier.padding(horizontal = 12.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(12.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = \"Zoom \${zoomLevel.roundToInt()}x\",
                        color = Color.White,
                        fontSize = 12.sp
                    )

                    Slider(
                        value = zoomLevel,
                        onValueChange = { zoomLevel = it },
                        valueRange = 1f..4f,
                        modifier = Modifier.width(100.dp)
                    )
                }
            }
        }
    }
}

// ========== TIMELINE EDITOR SCREEN ==========

@Composable
fun TimelineEditorScreen(
    segments: List<VideoSegment>,
    onScreenChange: (String) -> Unit
) {
    var selectedSegment by remember { mutableStateOf<VideoSegment?>(null) }
    var editMode by remember { mutableStateOf(\"trim\") }

    Box(modifier = Modifier.fillMaxSize().background(Color.Black)) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color(0xFF1A1A1A))
                    .padding(12.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(\"Timeline Editor\", color = Color.White, fontWeight = FontWeight.Bold)
                Row {
                    Button(onClick = { onScreenChange(\"record\") }) {
                        Text(\"Back\")
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Button(onClick = { }) {
                        Text(\"Export\")
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(12.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                listOf(\"trim\", \"effect\", \"speed\").forEach { mode ->
                    Button(
                        onClick = { editMode = mode },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = if (editMode == mode) Color(0xFF5D5FEF) else Color(0xFF2A2A2A)
                        ),
                        modifier = Modifier.weight(1f)
                    ) {
                        Text(mode.uppercase())
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(240.dp)
                    .padding(12.dp),
                color = Color(0xFF1A1A1A),
                shape = RoundedCornerShape(12.dp)
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(
                            brush = androidx.compose.ui.graphics.Brush.linearGradient(
                                listOf(
                                    Color(0xFF2A2A2A),
                                    Color(0xFF1A1A1A)
                                )
                            )
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    if (segments.isNotEmpty()) {
                        Text(
                            text = \"Preview: \${segments.size} clips\",
                            color = Color.White,
                            fontWeight = FontWeight.Bold
                        )
                    } else {
                        Text(
                            text = \"No video segments\",
                            color = Color(0xFF888888)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(100.dp)
                    .padding(12.dp),
                color = Color(0xFF1A1A1A),
                shape = RoundedCornerShape(12.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(8.dp)
                ) {
                    Text(
                        text = \"Timeline - \${segments.size} segments\",
                        color = Color.White,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold
                    )

                    LazyRow(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(top = 8.dp),
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        items(segments.size) { index ->
                            Surface(
                                modifier = Modifier
                                    .size(60.dp, 50.dp)
                                    .clickable { selectedSegment = segments[index] },
                                color = if (selectedSegment?.id == segments[index].id) Color(0xFF5D5FEF) else Color(0xFF2A2A2A),
                                shape = RoundedCornerShape(4.dp)
                            ) {
                                Box(contentAlignment = Alignment.Center) {
                                    Text(
                                        text = \"C\${index + 1}\",
                                        color = Color.White,
                                        fontSize = 10.sp
                                    )
                                }
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            if (selectedSegment != null) {
                Surface(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(12.dp),
                    color = Color(0xFF1A1A1A),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp)
                    ) {
                        Text(
                            text = \"Segment: \${selectedSegment!!.id}\",
                            color = Color.White,
                            fontWeight = FontWeight.Bold
                        )

                        Spacer(modifier = Modifier.height(12.dp))

                        when (editMode) {
                            \"trim\" -> {
                                Text(\"Duration: \${selectedSegment!!.duration}ms\", color = Color(0xFF888888))
                                Slider(
                                    value = selectedSegment!!.startTime.toFloat(),
                                    onValueChange = { },
                                    valueRange = 0f..selectedSegment!!.duration.toFloat(),
                                    modifier = Modifier.fillMaxWidth()
                                )
                            }
                            \"effect\" -> {
                                Text(\"Effect: \${selectedSegment!!.effectId}\", color = Color(0xFF888888))
                                LazyRow(
                                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    items(VideoEffectsCatalog.list.take(6)) { effect ->
                                        Surface(
                                            modifier = Modifier
                                                .size(60.dp)
                                                .clickable { },
                                            color = if (selectedSegment!!.effectId == effect.id) effect.color else Color(0xFF2A2A2A),
                                            shape = RoundedCornerShape(8.dp)
                                        ) {
                                            Box(contentAlignment = Alignment.Center) {
                                                Text(
                                                    text = effect.name,
                                                    color = Color.White,
                                                    fontSize = 10.sp
                                                )
                                            }
                                        }
                                    }
                                }
                            }
                            \"speed\" -> {
                                Text(\"Speed: \${selectedSegment!!.speedMultiplier}x\", color = Color(0xFF888888))
                                Slider(
                                    value = selectedSegment!!.speedMultiplier,
                                    onValueChange = { },
                                    valueRange = 0.5f..2f,
                                    modifier = Modifier.fillMaxWidth()
                                )
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(32.dp))
        }
    }
}

// ========== GALLERY SCREEN ==========

@Composable
fun GalleryScreen(onScreenChange: (String) -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color(0xFF1A1A1A))
                    .padding(12.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(\"Gallery\", color = Color.White, fontWeight = FontWeight.Bold)
                Button(onClick = { onScreenChange(\"record\") }) {
                    Text(\"Back\")
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(12.dp)
                    .height(400.dp)
                    .background(Color(0xFF1A1A1A), RoundedCornerShape(12.dp)),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = \"No saved videos yet\\nRecord a video to see it here\",
                    color = Color(0xFF888888),
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}

// ========== CAMERA PREVIEW ==========

@Composable
fun CameraPreviewComposable(
    modifier: Modifier = Modifier,
    isFrontCamera: Boolean,
    zoom: Float,
    effect: VideoEffect
) {
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current

    AndroidView(
        modifier = modifier,
        factory = { ctx ->
            val previewView = PreviewView(ctx).apply {
                implementationMode = PreviewView.ImplementationMode.PERFORMANCE
                scaleType = PreviewView.ScaleType.FILL_CENTER
            }

            val cameraProviderFuture = ProcessCameraProvider.getInstance(ctx)
            cameraProviderFuture.addListener(({
                try {
                    val cameraProvider = cameraProviderFuture.get()
                    val preview = Preview.Builder().build().also {
                        it.setSurfaceProvider(previewView.surfaceProvider)
                    }

                    val selector = if (isFrontCamera) {
                        CameraSelector.DEFAULT_FRONT_CAMERA
                    } else {
                        CameraSelector.DEFAULT_BACK_CAMERA
                    }

                    cameraProvider.unbindAll()
                    cameraProvider.bindToLifecycle(
                        lifecycleOwner as LifecycleOwner,
                        selector,
                        preview
                    )
                } catch (e: Exception) {
                    Log.e(\"CameraPreview\", \"Camera bind failed\", e)
                }
            }), ContextCompat.getMainExecutor(ctx))

            previewView
        }
    )

    Box(modifier = modifier) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            drawRect(
                color = effect.color.copy(alpha = 0.15f),
                size = Size(size.width, size.height)
            )

            when (effect.type) {
                \"neon\" -> {
                    drawRoundRect(
                        color = Color(0x6600E5FF),
                        size = Size(size.width, size.height * 0.4f),
                        topLeft = Offset(0f, size.height * 0.25f),
                        cornerRadius = androidx.compose.ui.geometry.CornerRadius(40f)
                    )
                }
                \"matrix\" -> {
                    for (i in 0..30) {
                        drawLine(
                            color = Color(0xFF4CAF50).copy(alpha = 0.2f),
                            start = Offset(i * 30f, 0f),
                            end = Offset(i * 30f + 20f, size.height),
                            strokeWidth = 2f
                        )
                    }
                }
                \"vhs\" -> {
                    for (i in 0..18) {
                        drawLine(
                            color = Color.White.copy(alpha = 0.08f),
                            start = Offset(0f, i * 25f),
                            end = Offset(size.width, i * 25f + 10f),
                            strokeWidth = 2f
                        )
                    }
                }
                \"glow\" -> {
                    drawCircle(
                        color = Color(0xFF66BB6A).copy(alpha = 0.18f),
                        radius = size.minDimension * 0.65f,
                        center = Offset(size.width * 0.5f, size.height * 0.5f)
                    )
                }
            }
        }
    }
}

// ========== UI COMPONENTS ==========

@Composable
fun TemplateChip(
    template: VideoTemplate,
    selected: Boolean,
    onSelect: () -> Unit
) {
    val bg = if (selected) template.color else Color(0xCC212121)
    Surface(
        color = bg,
        shape = RoundedCornerShape(16.dp),
        modifier = Modifier
            .clickable { onSelect() }
            .padding(2.dp)
    ) {
        Text(
            text = template.name,
            color = Color.White,
            fontSize = 12.sp,
            modifier = Modifier.padding(12.dp, 8.dp)
        )
    }
}

@Composable
fun EffectChip(
    effect: VideoEffect,
    selected: Boolean,
    onSelect: () -> Unit
) {
    val bg = if (selected) effect.color else Color(0xCC212121)
    Surface(
        color = bg,
        shape = RoundedCornerShape(16.dp),
        modifier = Modifier
            .clickable { onSelect() }
            .padding(2.dp)
    ) {
        Text(
            text = effect.name,
            color = Color.White,
            fontSize = 11.sp,
            modifier = Modifier.padding(12.dp, 8.dp)
        )
    }
}

@Composable
fun FloatingActionChip(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    label: String,
    onClick: () -> Unit
) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Surface(
            shape = CircleShape,
            color = Color(0xCC1C1C1C),
            modifier = Modifier
                .size(44.dp)
                .clickable { onClick() }
        ) {
            Box(contentAlignment = Alignment.Center) {
                Icon(
                    imageVector = icon,
                    contentDescription = label,
                    tint = Color.White
                )
            }
        }
        Spacer(modifier = Modifier.height(6.dp))
        Text(text = label, color = Color.White, fontSize = 10.sp)
    }
}

@Composable
fun delay(millis: Long) {
    LaunchedEffect(Unit) {
        kotlinx.coroutines.delay(millis)
    }
}
