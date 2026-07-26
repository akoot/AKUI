package co.akoot.bluefox.api.util

import java.awt.Color
import java.awt.color.ColorSpace
import java.util.ArrayList
import kotlin.math.max
import kotlin.math.min
import kotlin.math.roundToInt
import kotlin.random.Random

object ColorUtil {

    /**
     * Check if a color is a shade of gray
     *
     * @param color The color
     * @param tolerance The threshold of what is considered "gray" (0-255).
     * Example: tolerance=10, r=100, g=110 b=90, isGray=true. If tolerance was 5, isGray=false
     * @return Whether a color is gray or not
     */
    fun isGray(color: Int, tolerance: Int = 10): Boolean {
        val col = Color(color)
        val rgDiff = col.red - col.green
        val rbDiff = col.red - col.blue
        if (rgDiff > tolerance || rgDiff < -tolerance) if (rbDiff > tolerance || rbDiff < -tolerance) return false
        return true
    }

    /**
     * Get the hue of a color
     *
     * @param color The color
     * @return The hue of a color in degrees (0f to 360f)
     */
    fun getHue(color: Int): Float {
        val col = Color(color)
        val min = col.red.coerceAtMost(col.green).coerceAtMost(col.blue).toFloat()
        val max = col.red.coerceAtLeast(col.green).coerceAtLeast(col.blue).toFloat()

        if (min == max) return 0f

        val hue: Float = when (max) {
            col.red.toFloat() -> (col.green - col.blue) / (max - min)
            col.green.toFloat() -> 2f + (col.blue - col.red) / (max - min)
            else -> 4f + (col.red - col.green) / (max - min)
        }

        return (hue * 60).let { if (it < 0) it + 360 else it }
    }

    /**
     * Get the brightness of a color
     *
     * @param color The color
     * @return The brightness of a color from 0f to 1f
     */
    fun getBrightness(color: Int): Float {
        val col = Color(color)
        val min = col.red.coerceAtMost(col.green).coerceAtMost(col.blue) / 255f
        val max = col.red.coerceAtLeast(col.green).coerceAtLeast(col.blue) / 255f
        return (max + min) / 2f
    }

    /**
     * Get the saturation of a color
     *
     * @param color The color
     * @return The saturation of a color from 0f to 1f
     */
    fun getSaturation(color: Int): Float {
        val col = Color(color)
        val r = col.red
        val g = col.green
        val b = col.blue
        val max = max(r, max(g, b))
        val min = min(r, min(g, b))
        if (max == min) return 0f
        val d = max - min
        return when (max) {
            r -> (g - b) / d + (if (g < b) 6f else 0f)
            g -> (b - r) / d + 2f
            b -> (r - g) / d + 4f
            else -> 0f
        } * 60f
    }

    /**
     * Gets a hex string representation of a color
     *
     * @param color The color
     * @param lowercase Whether the hex string should be lowercase
     * @return A hex string representation of the color
     */
    fun getHexString(color: Int, lowercase: Boolean = true): String {
        val col = Color(color)
        return String.format("#%02x%02x%02x", col.red, col.green, col.blue)
            .let { if (lowercase) it.lowercase() else it.uppercase() }
    }

    /**
     * Lightens a color
     *
     * @param color The color to darken
     * @param percentage How dark it should be
     * @return A lighter color
     */
    fun brighten(color: Int, percentage: Double = 0.1): Int {
        val col = Color(color)
        val min = (255 * percentage.coerceAtMost(1.0)).toInt()
        return Color(
            (col.red + (col.red * percentage)).toInt().coerceAtMost(255).coerceAtLeast(min),
            (col.green + (col.green * percentage)).toInt().coerceAtMost(255).coerceAtLeast(min),
            (col.blue + (col.blue * percentage)).toInt().coerceAtMost(255).coerceAtLeast(min)
        ).rgb
    }

    /**
     * Darkens a color
     *
     * @param color The color to darken
     * @param percentage How dark it should be
     * @return A darker color
     */
    fun darken(color: Int, percentage: Double = 0.1): Int {
        val col = Color(color).darker()
        val factor = 1 - percentage
        return Color(
            (col.red * factor).toInt().coerceAtLeast(0),
            (col.green * factor).toInt().coerceAtLeast(0),
            (col.blue * factor).toInt().coerceAtLeast(0)
        ).rgb
    }

    /**
     * Get a random color
     *
     * @param saturation The desired saturation of the color
     * @param brightness The desired brightness of the color
     * @return A random color
     */
    fun randomColor(
        saturation: Float = 0.9f,
        brightness: Float = 0.9f,
        minHue: Float = 0f,
        maxHue: Float = 1f
    ): Int {
        return Color.getHSBColor(Random.nextFloat() * (maxHue - minHue), saturation, brightness).rgb
    }

    /**
     * Get a random color as a hex string
     *
     * @param saturation The desired saturation of the color
     * @param brightness The desired brightness of the color
     * @return A random color in the form of a hex string
     */
    fun randomColorHex(saturation: Float = 0.9f, brightness: Float = 0.9f): String {
        return getHexString(randomColor(saturation, brightness))
    }

    /**
     * Mix 2 colors
     *
     * @param color1 Color 1
     * @param color2 Color 2
     * @return The resulting color of mixing color1 and color2
     */
    fun mix(color1: Int, color2: Int, percent: Double = 0.5): Int {
        val r = lerp(Color(color1).red, Color(color2).red, percent)
        val g = lerp(Color(color1).green, Color(color2).green, percent)
        val b = lerp(Color(color1).blue, Color(color2).blue, percent)
        return Color(r, g, b).rgb
    }

    fun lerp(a: Int, b: Int, t: Double): Int =
        (a + (b - a) * t).roundToInt()

    private val cie = ColorSpace.getInstance(ColorSpace.CS_CIEXYZ)
    private val sRGB = ColorSpace.getInstance(ColorSpace.CS_sRGB)

    /**
     * Generates a gradient based on the color points provided.
     * This method mixes the colors in the CIE color space, which
     * results in more vibrant colors
     *
     * @param size The number of colors to generate
     * @param points The colors to mix
     * @return A list of colors
     */
    fun getGradient(size: Int, vararg points: Int?): MutableList<Int> {
        val gradient = ArrayList<Int>(size)

        val x = points.size
        val y = x - 1

        val a = size - x
        val b = a / y
        var c = a - (b * y)

        // precompute CIE XYZ for all points
        val ciePoints = Array(x) { i ->
            val point = points[i] ?: 0x000000
            val rgb = Color(point).getRGBColorComponents(null)
            cie.fromRGB(rgb)
        }

        // build gradient
        for (i in 0 until x) {
            val point = points[i] ?: 0x000000
            gradient += point
            if (i == y) break

            val from = ciePoints[i]
            val to = ciePoints[i + 1]

            val k = if (c-- > 0) b + 1 else b
            val m = k + 1
            val step = 1.0f / m

            val cieMid = FloatArray(3)

            for (j in 0 until k) {
                val t = (j + 1) * step
                cieMid[0] = from[0] + t * (to[0] - from[0])
                cieMid[1] = from[1] + t * (to[1] - from[1])
                cieMid[2] = from[2] + t * (to[2] - from[2])

                val rgb = sRGB.fromCIEXYZ(cieMid)
                gradient += Color(rgb[0], rgb[1], rgb[2]).rgb
            }
        }

        return gradient
    }
}