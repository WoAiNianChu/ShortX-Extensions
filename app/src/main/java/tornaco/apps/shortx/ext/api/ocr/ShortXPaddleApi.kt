package tornaco.apps.shortx.ext.api.ocr

import android.content.Context
import android.graphics.Bitmap
import autojs.api.OcrPaddle
import autojs.image.ImageWrapper
import tornaco.apps.shortx.core.proto.toProtoRect
import tornaco.apps.shortx.core.util.Logger
import tornaco.apps.shortx.ext.api.ExtAppAssetsHelper

@Deprecated("Native crash on some Android device.")
class ShortXPaddleApi(private val context: Context) {
    private val logger = Logger("ShortXPaddleApi")

    private val assetsFiles = listOf(
        "labels/ppocr_keys_v1.txt",

        "models/ocr_v3_for_cpu/cls_opt.nb",
        "models/ocr_v3_for_cpu/det_opt.nb",
        "models/ocr_v3_for_cpu/rec_opt.nb",

        "models/ocr_v3_for_cpu(slim)/cls_opt.nb",
        "models/ocr_v3_for_cpu(slim)/det_opt.nb",
        "models/ocr_v3_for_cpu(slim)/rec_opt.nb",
    )

    private val ocr by lazy {
        OcrPaddle(context).apply {
            assetsFiles.forEach {
                ExtAppAssetsHelper.copyAssets(context, it, context.cacheDir.absolutePath)
            }
            val init = init(false)
            logger.d("Init: $init")
        }
    }

    fun detect(
        image: Bitmap,
        text: String
    ): List<ByteArray> {
        val result = ocr.detect(ImageWrapper.ofBitmap(image), 4, true)
        logger.d("Detect: $result")
        val textBlocks = result.map {
            TextBlock(
                it.label,
                it.bounds
            )
        }
        val matched = findBoundingRects(textBlocks, text)
        return matched.map { it.toProtoRect().toByteArray() }
    }

    fun recognizeText(
        image: Bitmap
    ): Array<String> {
        val result = ocr.recognizeText(ImageWrapper.ofBitmap(image), 4, true)
        logger.d("recognizeText: $result")
        return result
    }
}