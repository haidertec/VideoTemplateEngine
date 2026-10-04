package com.example.videotemplates

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.recyclerview.widget.RecyclerView
import kotlin.random.Random

/**
 * Advanced Video Template Engine - 5000+ Templates
 * Features:
 * - 5000+ auto-generated templates
 * - Jetpack Compose UI
 * - RecyclerView adapter
 * - Advanced effects system
 * - Animation presets
 * - Production-ready Android structure
 */

// ==================== Data Models ====================

data class EffectPreset(
    val id: String,
    val name: String,
    val intensity: Int,
    val durationMs: Int,
    val type: String,
    val category: String = "visual"
)

data class AnimationPreset(
    val id: String,
    val name: String,
    val durationMs: Int,
    val easing: String,
    val direction: String,
    val delay: Int = 0
)

data class TransitionPreset(
    val id: String,
    val name: String,
    val durationMs: Int,
    val type: String
)

data class VideoTemplate(
    val id: String,
    val name: String,
    val category: String,
    val style: String,
    val resolution: String,
    val aspectRatio: String,
    val durationSec: Int,
    val mood: String,
    val transition: String,
    val colorGrade: String,
    val tags: List<String>,
    val effects: List<EffectPreset>,
    val animations: List<AnimationPreset>,
    val transitions: List<TransitionPreset>,
    val description: String,
    val difficulty: String = "Medium",
    val rating: Double = 4.5
)

data class VideoProjectSettings(
    var projectName: String = "MyVideoProject",
    var resolution: String = "1080p",
    var aspectRatio: String = "9:16",
    var durationSec: Int = 30,
    var musicMood: String = "Cinematic",
    var transition: String = "Fade",
    var colorGrade: String = "Warm Gold",
    var effects: List<String> = emptyList(),
    var animations: List<String> = emptyList(),
    var selectedTemplateId: String = "",
    var frameRate: Int = 30,
    var bitrate: String = "5000k"
)

// ==================== Advanced Catalog ====================

object AdvancedVideoTemplateCatalog {
    
    private val categories = listOf(
        "Travel", "Fashion", "Food", "Beauty", "Fitness", "Wedding",
        "Business", "Product", "Tech", "Nature", "Music", "Lifestyle",
        "RealEstate", "Event", "Marketing", "Automotive", "Pets", "Sports",
        "Art", "Creator", "Podcast", "Gaming", "Education", "Documentary",
        "Photography", "TikTok", "YouTube", "Instagram", "Streaming", "Vlog"
    )

    private val styles = listOf(
        "Cinematic", "Luxury", "Minimal", "Vibrant", "Moody",
        "Dark", "Aesthetic", "Urban", "Soft", "Modern", "Bold", "Editorial",
        "Vintage", "Futuristic", "Retro", "Clean", "Chaotic", "Smooth", "Sharp"
    )

    private val moods = listOf(
        "Cinematic", "Energetic", "Romantic", "Cool", "Warm",
        "Bright", "Dreamy", "Dynamic", "Epic", "Chill", "Happy", "Sad",
        "Mysterious", "Calm", "Intense", "Playful", "Professional"
    )

    private val transitions = listOf(
        "Fade", "Slide", "Zoom", "Whip Pan", "Match Cut",
        "Flash", "Orbit", "Blur", "Wipe", "Cross Dissolve",
        "3D Cube", "Page Turn", "Mosaic", "Ripple", "Glitch"
    )

    private val colorGrades = listOf(
        "Warm Gold", "Teal Orange", "Classic", "Neon", "Moody Violet",
        "Pastel", "Deep Black", "Soft Sunset", "Natural", "Crisp White",
        "Sepia", "Noir", "Cyberpunk", "Forest", "Ocean", "Desert"
    )

    private val resolutions = listOf("720p", "1080p", "2K", "4K", "8K")
    private val aspectRatios = listOf("9:16", "16:9", "1:1", "4:5", "21:9")
    private val difficulties = listOf("Easy", "Medium", "Hard", "Expert")

    private val advancedEffects = listOf(
        "Cinematic Glow", "Blur Background", "High Contrast", "Vignette",
        "Sharpen", "Soft Focus", "Light Leak", "Bloom", "Stabilize",
        "Slow Motion", "Duotone", "Neon", "Color Pop", "Noise Grain",
        "HDR", "Lens Flare", "Shadow Lift", "Saturation Boost", "Warm Tint",
        "Cool Tint", "Retro Film", "Texture Overlay", "Motion Blur", "Flash",
        "Glow Edge", "Deep Black", "Color Boost", "Sky Replacement", "Smooth Skin",
        "LUT Application", "Chromatic Aberration", "Pixel Sorting", "Glitch Effect",
        "VHS Distortion", "Film Grain", "Lens Distortion", "Bokeh", "Ambient Light",
        "Bloom Glow", "Color Grading Pro", "Exposure Control", "Highlights Recovery",
        "Shadow Details", "Clarity Boost", "Vibrance", "White Balance", "Tone Mapping",
        "Bilateral Smoothing", "Edge Enhancement", "Texture Synthesis", "Frequency Separation"
    )

    private val advancedAnimations = listOf(
        "Zoom In", "Zoom Out", "Pan Left", "Pan Right", "Float Up",
        "Float Down", "Orbit", "Tilt", "Fade In", "Fade Out",
        "Spin", "Pulse", "Parallax", "Bounce", "Slide In", "Swivel",
        "Flip", "Rotation", "Scale", "Skew", "Perspective", "Morph",
        "Sway", "Wiggle", "Shake", "Wave", "Spiral", "Spring",
        "Elastic", "Back", "Circular", "Cubic", "Quadratic", "Quintic"
    )

    private val advancedTransitions = listOf(
        "Fade", "Slide", "Zoom", "Whip Pan", "Match Cut",
        "Flash", "Orbit", "Blur", "Wipe", "Cross Dissolve",
        "3D Cube", "Page Turn", "Mosaic", "Ripple", "Glitch",
        "Lens Distortion", "Ink Spread", "Light Rays", "Film Gate", "Carousel",
        "Rotate", "Stretch", "Squeeze", "Shatter", "Shred", "Swirl", "Venetian"
    )

    private val descriptiveNouns = listOf(
        "Velvet", "Luxe", "Glow", "Nova", "Wave", "Echo", "Prime",
        "Pulse", "Bloom", "Sky", "Aura", "Peak", "Frame", "Canvas",
        "Flash", "Drift", "Motion", "Crest", "Noir", "Horizon", "Motive",
        "Orbit", "Vibe", "Trail", "Scene", "Flare", "Rush", "Current",
        "Essence", "Spirit", "Soul", "Dream", "Vision", "Quest", "Surge",
        "Nexus", "Zenith", "Summit", "Apex", "Pinnacle", "Glory", "Crown"
    )

    private val descriptiveAdjectives = listOf(
        "Golden", "Urban", "Modern", "Dreamy", "Smooth", "Cinematic",
        "Minimal", "Electric", "Soft", "Dynamic", "Luxury", "Bold",
        "Vibrant", "Clean", "Premium", "Retro", "Futuristic", "Neon", "Warm",
        "Ethereal", "Sublime", "Radiant", "Luminous", "Serene", "Vivid",
        "Sleek", "Silky", "Fluid", "Crystal", "Pristine", "Majestic",
        "Cosmic", "Stellar", "Mystic", "Arcane", "Enigmatic", "Eclectic"
    )

    private val suffixes = listOf(
        "Reel", "Story", "Promo", "Edit", "Loop", "Showcase",
        "Sequence", "Scene", "Film", "Mix", "Cut", "Highlight",
        "Masterpiece", "Genesis", "Odyssey", "Saga", "Chronicles",
        "Opus", "Encore", "Symphony", "Rhapsody", "Sonata"
    )

    private fun buildAdvancedEffects(index: Int): List<EffectPreset> {
        val random = Random(index * 47L + 151)
        val count = 4 + (index % 6)
        val selected = mutableSetOf<String>()
        
        while (selected.size < count && selected.size < advancedEffects.size) {
            selected.add(advancedEffects[random.nextInt(advancedEffects.size)])
        }

        return selected.mapIndexed { subIndex, name ->
            EffectPreset(
                id = "fx_${index}_$subIndex",
                name = name,
                intensity = 20 + random.nextInt(80),
                durationMs = 300 + random.nextInt(3000),
                type = when {
                    name.contains("Blur", true) -> "blur"
                    name.contains("Glow", true) || name.contains("Bloom", true) || name.contains("Flare", true) -> "glow"
                    name.contains("Color", true) || name.contains("Tint", true) || name.contains("Grade", true) -> "color"
                    name.contains("Noise", true) || name.contains("Grain", true) || name.contains("Texture", true) -> "grain"
                    name.contains("Slow", true) || name.contains("Motion", true) -> "motion"
                    name.contains("Skin", true) -> "portrait"
                    name.contains("Distortion", true) || name.contains("Glitch", true) -> "distortion"
                    else -> "visual"
                },
                category = when {
                    name.contains("Color", true) || name.contains("Grade", true) -> "Color Grading"
                    name.contains("Motion", true) -> "Motion"
                    name.contains("Blur", true) || name.contains("Focus", true) -> "Focus"
                    else -> "Visual Effects"
                }
            )
        }
    }

    private fun buildAdvancedAnimations(index: Int): List<AnimationPreset> {
        val random = Random(index * 83L + 257)
        val count = 2 + (index % 4)
        val selected = mutableSetOf<String>()
        
        while (selected.size < count && selected.size < advancedAnimations.size) {
            selected.add(advancedAnimations[random.nextInt(advancedAnimations.size)])
        }

        return selected.mapIndexed { subIndex, name ->
            val direction = when {
                name.contains("Left", true) -> "left"
                name.contains("Right", true) -> "right"
                name.contains("Up", true) -> "up"
                name.contains("Down", true) -> "down"
                name.contains("Zoom", true) && name.contains("Out", true) -> "zoom_out"
                name.contains("Zoom", true) -> "zoom_in"
                name.contains("Spin", true) || name.contains("Rotation", true) -> "rotate"
                name.contains("Flip", true) -> "flip"
                name.contains("Scale", true) -> "scale"
                else -> "center"
            }

            AnimationPreset(
                id = "anim_${index}_$subIndex",
                name = name,
                durationMs = 400 + random.nextInt(2400),
                easing = listOf("linear", "easeInOut", "easeIn", "easeOut", "spring", "overshoot", "elastic")[random.nextInt(7)],
                direction = direction,
                delay = random.nextInt(300)
            )
        }
    }

    private fun buildTransitions(index: Int): List<TransitionPreset> {
        val random = Random(index * 127L + 383)
        val count = 1 + (index % 3)
        val selected = mutableSetOf<String>()
        
        while (selected.size < count && selected.size < advancedTransitions.size) {
            selected.add(advancedTransitions[random.nextInt(advancedTransitions.size)])
        }

        return selected.mapIndexed { subIndex, name ->
            TransitionPreset(
                id = "trans_${index}_$subIndex",
                name = name,
                durationMs = 300 + random.nextInt(1700),
                type = when {
                    name.contains("Fade", true) -> "fade"
                    name.contains("Slide", true) || name.contains("Wipe", true) -> "slide"
                    name.contains("Zoom", true) -> "zoom"
                    name.contains("3D", true) -> "3d"
                    else -> "custom"
                }
            )
        }
    }

    private fun makeTemplateName(index: Int): String {
        val a = descriptiveNouns[(index * 5 + 7) % descriptiveNouns.size]
        val b = descriptiveAdjectives[(index * 11 + 13) % descriptiveAdjectives.size]
        val c = suffixes[(index * 7 + 19) % suffixes.size]
        return "$a $b $c"
    }

    fun generateTemplates(count: Int = 5000): List<VideoTemplate> {
        return (1..count).map { index ->
            val category = categories[(index * 19) % categories.size]
            val style = styles[(index * 23) % styles.size]
            val mood = moods[(index * 29) % moods.size]
            val transition = transitions[(index * 31) % transitions.size]
            val colorGrade = colorGrades[(index * 37) % colorGrades.size]
            val resolution = resolutions[(index * 41) % resolutions.size]
            val aspectRatio = aspectRatios[(index * 43) % aspectRatios.size]
            val difficulty = difficulties[(index * 47) % difficulties.size]
            val durationSec = 10 + ((index * 11) % 50)
            val rating = 3.5 + ((index % 50) * 0.01)
            
            val tags = listOf(
                category.lowercase(),
                style.lowercase(),
                mood.lowercase(),
                difficulty.lowercase(),
                transition.lowercase()
            )

            val effects = buildAdvancedEffects(index)
            val animations = buildAdvancedAnimations(index)
            val trans = buildTransitions(index)

            VideoTemplate(
                id = "tpl_${index}",
                name = makeTemplateName(index),
                category = category,
                style = style,
                resolution = resolution,
                aspectRatio = aspectRatio,
                durationSec = durationSec,
                mood = mood,
                transition = transition,
                colorGrade = colorGrade,
                tags = tags,
                effects = effects,
                animations = animations,
                transitions = trans,
                description = "${style} ${category.lowercase()} template with ${mood.lowercase()} mood, perfect for ${difficulty.lowercase()} editors.",
                difficulty = difficulty,
                rating = rating
            )
        }
    }
}

class AdvancedVideoTemplateManager {
    private val templates: List<VideoTemplate> = AdvancedVideoTemplateCatalog.generateTemplates(5000)

    fun all(): List<VideoTemplate> = templates
    fun count(): Int = templates.size
    fun getByCategory(category: String): List<VideoTemplate> = templates.filter { it.category.equals(category, ignoreCase = true) }
    fun getById(id: String): VideoTemplate? = templates.firstOrNull { it.id.equals(id, ignoreCase = true) }
    fun getByDifficulty(difficulty: String): List<VideoTemplate> = templates.filter { it.difficulty.equals(difficulty, ignoreCase = true) }
    fun topRated(limit: Int = 50): List<VideoTemplate> = templates.sortedByDescending { it.rating }.take(limit)
    fun search(query: String): List<VideoTemplate> = templates.filter { it.name.contains(query, ignoreCase = true) || it.description.contains(query, ignoreCase = true) }
    fun categories(): List<String> = templates.map { it.category }.distinct().sorted()

    fun applyTemplate(template: VideoTemplate, settings: VideoProjectSettings = VideoProjectSettings()): VideoProjectSettings {
        return settings.copy(
            resolution = template.resolution,
            aspectRatio = template.aspectRatio,
            durationSec = template.durationSec,
            musicMood = template.mood,
            transition = template.transition,
            colorGrade = template.colorGrade,
            effects = template.effects.map { it.name },
            animations = template.animations.map { it.name },
            selectedTemplateId = template.id
        )
    }

    fun getPreview(template: VideoTemplate): Map<String, Any> = mapOf(
        "id" to template.id,
        "name" to template.name,
        "category" to template.category,
        "difficulty" to template.difficulty,
        "rating" to template.rating,
        "effects" to template.effects.size,
        "animations" to template.animations.size,
        "description" to template.description
    )
}

// ==================== RecyclerView Adapter ====================

class TemplateViewHolder(parent: ViewGroup) : RecyclerView.ViewHolder(
    LayoutInflater.from(parent.context).inflate(R.layout.item_template, parent, false)
) {
    private val titleView = itemView.findViewById<TextView>(R.id.template_title)
    private val categoryView = itemView.findViewById<TextView>(R.id.template_category)
    private val difficultyView = itemView.findViewById<TextView>(R.id.template_difficulty)
    private val ratingView = itemView.findViewById<TextView>(R.id.template_rating)
    private val effectsView = itemView.findViewById<TextView>(R.id.template_effects)

    fun bind(template: VideoTemplate, onClick: (VideoTemplate) -> Unit) {
        titleView.text = template.name
        categoryView.text = "Category: ${template.category}"
        difficultyView.text = "Difficulty: ${template.difficulty}"
        ratingView.text = "★ ${String.format("%.1f", template.rating)}"
        effectsView.text = "Effects: ${template.effects.size} | Animations: ${template.animations.size}"
        
        itemView.setOnClickListener { onClick(template) }
    }
}

class TemplateAdapter(
    private val templates: List<VideoTemplate>,
    private val onItemClick: (VideoTemplate) -> Unit
) : RecyclerView.Adapter<TemplateViewHolder>() {
    
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TemplateViewHolder =
        TemplateViewHolder(parent)
    
    override fun onBindViewHolder(holder: TemplateViewHolder, position: Int) {
        holder.bind(templates[position], onItemClick)
    }
    
    override fun getItemCount(): Int = templates.size
}

// ==================== Compose UI ====================

@Composable
fun TemplateListScreen(manager: AdvancedVideoTemplateManager) {
    var selectedCategory by remember { mutableStateOf("All") }
    var searchQuery by remember { mutableStateOf("") }
    var selectedTemplate by remember { mutableStateOf<VideoTemplate?>(null) }

    val categories = listOf("All") + manager.categories()
    val filteredTemplates = when {
        searchQuery.isNotEmpty() -> manager.search(searchQuery)
        selectedCategory == "All" -> manager.all()
        else -> manager.getByCategory(selectedCategory)
    }

    Column(modifier = Modifier.fillMaxSize()) {
        // Header
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            color = Color(0xFF1F1F1F),
            shape = RoundedCornerShape(12.dp)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    "Video Template Engine",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
                Text(
                    "5000+ Professional Templates",
                    fontSize = 14.sp,
                    color = Color(0xFFB0B0B0)
                )
            }
        }

        // Search Bar
        OutlinedTextField(
            value = searchQuery,
            onValueChange = { searchQuery = it },
            label = { Text("Search templates...") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            trailingIcon = { Icon(Icons.Default.Settings, contentDescription = null) }
        )

        // Category Chips
        LazyRow(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(categories) { category ->
                CategoryChip(
                    category,
                    isSelected = category == selectedCategory,
                    onClick = { selectedCategory = category }
                )
            }
        }

        // Templates List
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(filteredTemplates) { template ->
                TemplateCard(
                    template,
                    onClick = { selectedTemplate = template }
                )
            }
        }
    }

    // Detail Dialog
    selectedTemplate?.let { template ->
        TemplateDetailDialog(template) { selectedTemplate = null }
    }
}

@Composable
fun CategoryChip(label: String, isSelected: Boolean, onClick: () -> Unit) {
    Surface(
        modifier = Modifier
            .clickable(onClick = onClick)
            .padding(4.dp),
        color = if (isSelected) Color(0xFF6200EE) else Color(0xFF333333),
        shape = RoundedCornerShape(20.dp)
    ) {
        Text(
            label,
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
            color = Color.White,
            fontSize = 12.sp
        )
    }
}

@Composable
fun TemplateCard(template: VideoTemplate, onClick: () -> Unit) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        color = Color(0xFF2A2A2A),
        shape = RoundedCornerShape(12.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    template.name,
                    fontWeight = FontWeight.Bold,
                    color = Color.White,
                    modifier = Modifier.weight(1f),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Text(
                    "★ ${String.format("%.1f", template.rating)}",
                    color = Color(0xFFFFD700),
                    fontSize = 12.sp
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Chip("${template.category}")
                Chip(template.difficulty)
                Chip("${template.resolution}")
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                template.description,
                color = Color(0xFFB0B0B0),
                fontSize = 12.sp,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )

            Spacer(modifier = Modifier.height(8.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    "FX: ${template.effects.size} | Anim: ${template.animations.size}",
                    color = Color(0xFF888888),
                    fontSize = 11.sp
                )
                Button(
                    onClick = onClick,
                    modifier = Modifier
                        .height(32.dp)
                        .width(100.dp)
                ) {
                    Text("Apply", fontSize = 12.sp)
                }
            }
        }
    }
}

@Composable
fun Chip(text: String) {
    Surface(
        color = Color(0xFF404040),
        shape = RoundedCornerShape(16.dp),
        modifier = Modifier.padding(2.dp)
    ) {
        Text(
            text,
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
            color = Color(0xFFDDDDDD),
            fontSize = 11.sp
        )
    }
}

@Composable
fun TemplateDetailDialog(template: VideoTemplate, onDismiss: () -> Unit) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(template.name) },
        text = {
            Column {
                Text("Category: ${template.category}")
                Text("Style: ${template.style}")
                Text("Resolution: ${template.resolution}")
                Text("Effects: ${template.effects.size}")
                Text("Animations: ${template.animations.size}")
            }
        },
        confirmButton = {
            Button(onClick = onDismiss) { Text("Close") }
        }
    )
}

// ==================== Main Function ====================

fun main() {
    val manager = AdvancedVideoTemplateManager()
    println("Total Templates: ${manager.count()}")
    println("Categories: ${manager.categories().size}")
    println("Top Rated: ${manager.topRated(5).map { it.name }}")
}
