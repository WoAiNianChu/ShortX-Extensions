package tornaco.apps.shortx.ext.api.ocr

import android.graphics.Rect

data class TextBlock(
    val text: String,
    val rect: Rect
)