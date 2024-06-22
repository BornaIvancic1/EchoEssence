package hr.algebra.echoessence.ui.extra

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Color
import androidx.palette.graphics.Palette

class ColorExtractor(private val context: Context) {

    fun extractColors(bitmap: Bitmap, listener: ColorExtractionListener) {
        Palette.from(bitmap).generate { palette ->
            palette?.let {
                val defaultColor = Color.BLACK
                val dominantColor = palette.getDominantColor(defaultColor)
                val vibrantColor = palette.getVibrantColor(defaultColor)

                val darkenedDominantColor = darkenColor(dominantColor, 0.5f)
                val darkenedVibrantColor = darkenColor(vibrantColor, 0.5f)

                listener.onColorsExtracted(darkenedDominantColor, darkenedVibrantColor)
            }
        }
    }

    private fun darkenColor(color: Int, factor: Float): Int {
        val a = Color.alpha(color)
        val r = Math.round(Color.red(color) * factor).coerceAtMost(255)
        val g = Math.round(Color.green(color) * factor).coerceAtMost(255)
        val b = Math.round(Color.blue(color) * factor).coerceAtMost(255)
        return Color.argb(a, r, g, b)
    }

    interface ColorExtractionListener {
        fun onColorsExtracted(dominantColor: Int, vibrantColor: Int)
    }

}
