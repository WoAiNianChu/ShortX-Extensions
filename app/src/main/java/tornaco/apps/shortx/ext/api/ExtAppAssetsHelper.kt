package tornaco.apps.shortx.ext.api


import android.content.Context
import android.util.Log
import tornaco.apps.shortx.core.res.AppResources
import java.io.File
import java.io.FileOutputStream

object ExtAppAssetsHelper {
    private const val SHORTX_EXT_APP_PKG_NAME: String = "tornaco.apps.shortx.ext"

    fun copyAssets(androidContext: Context, path: String, outDir: String) {
        runCatching {
            Log.w("TORNACO", "copyAssets $path $outDir")
            val destFile = File(outDir, path)
            if (destFile.exists()) {
                Log.w("TORNACO", "destFile already exists. $destFile")
                return
            }
            destFile.parentFile?.mkdirs()

            val appRes = AppResources(androidContext, SHORTX_EXT_APP_PKG_NAME)
            val appContext = appRes.appContext
            val assets = appContext.assets
            assets.open(path).use { stream ->
                stream.copyTo(FileOutputStream(destFile.also {
                    it.parentFile?.mkdirs()
                }))
            }
            Log.w("TORNACO", "copyAssets done $path $outDir")
        }.onFailure {
            Log.e("TORNACO", "copyAssets err ${Log.getStackTraceString(it)}")
        }
    }
}