package com.example.videotemplates

import kotlin.random.Random

/**
 * Professional single-file video template engine
 * - 1000+ generated templates
 * - Effects + animations
 * - ready for Android app use
 */
data class EffectPreset(
    val id: String,
    val name: String,
    val intensity: Int,
    val durationMs: Int,
    val type: String
)

data class AnimationPreset(
    val id: String,
    val name: String,
    val durationMs: Int,
    val easing: String,
    val direction: String
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
    val description: String
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
    var selectedTemplateId: String = ""
)

object VideoTemplateCatalog {
    private val categories = listOf(
        "Travel", "Fashion", "Food", "Beauty", "Fitness", "Wedding",
        "Business", "Product", "Tech", "Nature", "Music", "Lifestyle",
        "RealEstate", "Event", "Marketing", "Automotive", "Pets", "Sports",
        "Art", "Creator", "Podcast", "Gaming", "Education", "Documentary"
    )

    private val styles = listOf(
        "Cinematic", "Luxury", "Minimal", "Vibrant", "Moody",
        "Dark", "Aesthetic", "Urban", "Soft", "Modern", "Bold", "Editorial"
    )

    private val moods = listOf(
        "Cinematic", "Energetic", "Romantic", "Cool", "Warm",
        "Bright", "Dreamy", "Dynamic", "Epic", "Chill"
    )

    private val transitions = listOf(
        "Fade", "Slide", "Zoom", "Whip Pan", "Match Cut",
        "Flash", "Orbit", "Blur", "Wipe", "Cross Dissolve"
    )

    private val colorGrades = listOf(
        "Warm Gold", "Teal Orange", "Classic", "Neon", "Moody Violet",
        "Pastel", "Deep Black", "Soft Sunset", "Natural", "Crisp White"
    )

    private val resolutions = listOf("720p", "1080p", "2K", "4K")
    private val aspectRatios = listOf("9:16", "16:9", "1:1", "4:5")

    private val nouns = listOf(
        "Velvet", "Luxe", "Glow", "Nova", "Wave", "Echo", "Prime",
        "Pulse", "Bloom", "Sky", "Aura", "Peak", "Frame", "Canvas",
        "Flash", "Drift", "Motion", "Crest", "Noir", "Horizon", "Motive",
        "Orbit", "Vibe", "Trail", "Scene", "Flare", "Rush", "Current"
    )

    private val adjectives = listOf(
        "Golden", "Urban", "Modern", "Dreamy", "Smooth", "Cinematic",
        "Minimal", "Electric", "Soft", "Dynamic", "Luxury", "Bold",
        "Vibrant", "Clean", "Premium", "Retro", "Futuristic", "Neon", "Warm"
    )

    private val suffixes = listOf(
        "Reel", "Story", "Promo", "Edit", "Loop", "Showcase",
        "Sequence", "Scene", "Film", "Mix", "Cut", "Highlight"
    )

    private val effectPool = listOf(
        "Cinematic Glow", "Blur Background", "High Contrast", "Vignette",
        "Sharpen", "Soft Focus", "Light Leak", "Bloom", "Stabilize",
        "Slow Motion", "Duotone", "Neon", "Color Pop", "Noise Grain",
        "HDR", "Lens Flare", "Shadow Lift", "Saturation Boost", "Warm Tint",
        "Cool Tint", "Retro Film", "Texture Overlay", "Motion Blur", "Flash",
        "Glow Edge", "Deep Black", "Color Boost", "Sky Replacement", "Smooth Skin"
    )

    private val animationPool = listOf(
        "Zoom In", "Zoom Out", "Pan Left", "Pan Right", "Float Up",
        "Float Down", "Orbit", "Tilt", "Fade In", "Fade Out",
        "Spin", "Pulse", "Parallax", "Bounce", "Slide In", "Swivel"
    )

    private fun typedEffect(name: String): String = when {
        name.contains("Blur", true) -> "blur"
        name.contains("Glow", true) || name.contains("Flare", true) -> "glow"
        name.contains("Color", true) || name.contains("Tint", true) -> "color"
        name.contains("Noise", true) || name.contains("Texture", true) -> "grain"
        name.contains("Slow", true) || name.contains("Motion", true) -> "motion"
        name.contains("Skin", true) -> "portrait"
        else -> "visual"
    }

    private fun buildEffects(index: Int): List<EffectPreset> {
        val random = Random(index * 37L + 101)
        val names = effectPool.shuffled(random).take(3 + (index % 5))

        return names.mapIndexed { subIndex, name ->
            EffectPreset(
                id = "fx_${index}_$subIndex",
                name = name,
                intensity = 25 + random.nextInt(75),
                durationMs = 500 + random.nextInt(2500),
                type = typedEffect(name)
            )
        }
    }

    private fun buildAnimations(index: Int): List<AnimationPreset> {
        val random = Random(index * 73L + 202)
        val names = animationPool.shuffled(random).take(2 + (index % 4))

        return names.mapIndexed { subIndex, name ->
            val direction = when {
                name.contains("Left", true) -> "left"
                name.contains("Right", true) -> "right"
                name.contains("Up", true) -> "up"
                name.contains("Down", true) -> "down"
                name.contains("Zoom", true) && name.contains("Out", true) -> "zoom_out"
                name.contains("Zoom", true) -> "zoom_in"
                name.contains("Spin", true) -> "rotate"
                else -> "center"
            }

            AnimationPreset(
                id = "anim_${index}_$subIndex",
                name = name,
                durationMs = 400 + random.nextInt(2200),
                easing = listOf("linear", "easeInOut", "spring", "overshoot")[random.nextInt(4)],
                direction = direction
            )
        }
    }

    private fun makeTemplateName(index: Int): String {
        val a = nouns[(index * 3 + 5) % nouns.size]
        val b = adjectives[(index * 7 + 11) % adjectives.size]
        val c = suffixes[(index * 5 + 17) % suffixes.size]
        return "$a $b $c"
    }

    fun generateTemplates(count: Int = 1200): List<VideoTemplate> {
        return (1..count).map { index ->
            val category = categories[(index * 13) % categories.size]
            val style = styles[(index * 17) % styles.size]
            val mood = moods[(index * 19) % moods.size]
            val transition = transitions[(index * 23) % transitions.size]
            val colorGrade = colorGrades[(index * 29) % colorGrades.size]
            val resolution = resolutions[(index * 31) % resolutions.size]
            val aspectRatio = aspectRatios[(index * 37) % aspectRatios.size]
            val durationSec = 12 + ((index * 7) % 48)
            val tags = listOf(
                category.lowercase(),
                style.lowercase(),
                mood.lowercase(),
                transition.lowercase(),
                colorGrade.lowercase().replace(" ", "_")
            )

            val effects = buildEffects(index)
            val animations = buildAnimations(index)

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
                description = "${style} ${category.lowercase()} template with ${mood.lowercase()} mood, ${transition.lowercase()} transition and ${colorGrade.lowercase()} grading."
            )
        }
    }

    fun getByCategory(category: String, list: List<VideoTemplate>): List<VideoTemplate> {
        return list.filter { it.category.equals(category, ignoreCase = true) }
    }

    fun findById(id: String, list: List<VideoTemplate>): VideoTemplate? {
        return list.firstOrNull { it.id.equals(id, ignoreCase = true) }
    }

    fun findByName(name: String, list: List<VideoTemplate>): VideoTemplate? {
        return list.firstOrNull { it.name.equals(name, ignoreCase = true) }
    }
}

class VideoTemplateManager {
    private val templates: List<VideoTemplate> = VideoTemplateCatalog.generateTemplates(1200)

    fun all(): List<VideoTemplate> = templates

    fun count(): Int = templates.size

    fun getByCategory(category: String): List<VideoTemplate> =
        VideoTemplateCatalog.getByCategory(category, templates)

    fun getById(id: String): VideoTemplate? =
        VideoTemplateCatalog.findById(id, templates)

    fun getByName(name: String): VideoTemplate? =
        VideoTemplateCatalog.findByName(name, templates)

    fun preview(template: VideoTemplate): Map<String, Any> {
        return mapOf(
            "id" to template.id,
            "name" to template.name,
            "category" to template.category,
            "style" to template.style,
            "resolution" to template.resolution,
            "aspectRatio" to template.aspectRatio,
            "durationSec" to template.durationSec,
            "mood" to template.mood,
            "transition" to template.transition,
            "effects" to template.effects.map { it.name },
            "animations" to template.animations.map { it.name }
        )
    }

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

    fun categories(): List<String> = templates.map { it.category }.distinct().sorted()
}

fun main() {
    val manager = VideoTemplateManager()
    val templates = manager.all()

    println("Total Template Count: ${templates.size}")
    println("First Template: ${templates.first().name}")
    println("Categories: ${manager.categories().take(10)}")

    val template = templates.first()
    val project = manager.applyTemplate(template)
    println("Applied Project Settings: $project")
}

/**
 * Example usage in Android app:
 *
 * val manager = VideoTemplateManager()
 * val list = manager.all()
 * val first = list.first()
 * val project = manager.applyTemplate(first)
 *
 * // Use project.resolution, project.effect list, project.animation list etc.
 */
