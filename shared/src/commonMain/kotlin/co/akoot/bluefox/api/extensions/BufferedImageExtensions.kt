package co.akoot.bluefox.api.extensions

import co.akoot.bluefox.api.util.ColorUtil
import java.awt.image.BufferedImage
import kotlin.math.roundToInt


val BufferedImage.pixels: Array<IntArray> get() {
    val result = Array(height) { IntArray(width) }
    for (y in 0 until height) {
        for (x in 0 until width) {
            result[y][x] = getRGB(x, y)
        }
    }
    return result
}

fun BufferedImage.getMostFrequentColor(threshold: Double = 0.1): Int {
    val colors = HashMap<Int, Int>()
    val hues = HashMap<Int, Int>()
    for (y in pixels.indices) {
        for (x in pixels[y].indices) {
            val pixel = pixels[y][x]
            if (!ColorUtil.isGray(pixel)) {
                val hue = ((ColorUtil.getHue(pixel) * threshold).roundToInt() / threshold).roundToInt()
                val count = hues[hue] ?: 0
                hues[hue] = count + 1
                colors[hue] = pixel
            }
        }
    }

    return if (hues.isEmpty()) {
        0xaaaaaa
    } else {
        val mostFrequentHue = hues.toSortedMap().maxBy { it.value }.key
        colors[mostFrequentHue]!!
    }
}

val BufferedImage.mostFrequentColor: Int get() = getMostFrequentColor()