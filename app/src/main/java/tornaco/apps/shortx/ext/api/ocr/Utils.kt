package tornaco.apps.shortx.ext.api.ocr

import android.graphics.Rect

fun findBoundingRects(rectAndTexts: List<TextBlock>, target: String): List<Rect> {
    val results = mutableListOf<Rect>()

    for (i in rectAndTexts.indices) {
        if (rectAndTexts[i].text == target.first().toString()) {
            val positions = mutableListOf(rectAndTexts[i].rect)

            for (j in 1 until target.length) {
                val nextIndex = i + j
                if (nextIndex >= rectAndTexts.size ||
                    rectAndTexts[nextIndex].text != target[j].toString()
                ) {
                    break
                }
                positions.add(rectAndTexts[nextIndex].rect)
            }

            if (positions.size == target.length) {
                val left = positions.minOf { it.left }
                val top = positions.minOf { it.top }
                val right = positions.maxOf { it.right }
                val bottom = positions.maxOf { it.bottom }
                results.add(Rect(left, top, right, bottom))
            }
        }
    }
    return results
}