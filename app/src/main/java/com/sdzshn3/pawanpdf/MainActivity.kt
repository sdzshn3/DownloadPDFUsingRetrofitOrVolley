package com.sdzshn3.pawanpdf

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.Request
import com.android.volley.toolbox.HurlStack
import com.android.volley.toolbox.Volley
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.ResponseBody
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        /**
         * Volley
         */
        val request = InputStreamVolleyRequest(
            Request.Method.POST,
            "http://213.188.253.139:5000/downloadResult",
            {
                CoroutineScope(Dispatchers.IO).launch {
                    val outputStream =
                        FileOutputStream(File(getExternalFilesDir(null), "results.pdf"), true)
                    outputStream.write(it)
                    outputStream.close()
                }
            },
            {

            },
            hashMapOf(
                "JSESSIONID" to "aaaaaaaa-bbbb-cccc-dddd-eeeeeeeeeeee",
                "sem" to "2"
            )
        )

        val requestQueue = Volley.newRequestQueue(applicationContext, HurlStack())
        requestQueue.add(request)

        /**
         * Retrofit
         */
        /*val retrofit = Retrofit.Builder().apply {
            baseUrl("http://213.188.253.139:5000")
            addConverterFactory(GsonConverterFactory.create())
            client(OkHttpClient())
        }.build()

        val service = retrofit.create(Service::class.java)

        val request = Request(
            JSESSIONID = "aaaaaaaa-bbbb-cccc-dddd-eeeeeeeeeeee",
            sem = "2"
        )
        CoroutineScope(Dispatchers.IO).launch {
            val response = service.downloadPdf(request).body()
            saveFile(response, File(getExternalFilesDir(null), "results.pdf").path)
        }*/
    }

    private suspend fun saveFile(body: ResponseBody?, s: String) {
        if (body == null) {
            return
        }

        var input: InputStream? = null
        try {
            input = body.byteStream()

            val fos = FileOutputStream(s)
            fos.use { output ->
                val buffer = ByteArray(4 * 1024)
                var read: Int
                while (input.read(buffer).also { read = it } != -1) {
                    output.write(buffer, 0, read)
                }
                output.flush()
            }
        } catch (e: Exception) {
            e.printStackTrace()
            withContext(Dispatchers.Main) {
                Toast.makeText(this@MainActivity, "failed", Toast.LENGTH_SHORT).show()
            }
        } finally {
            input?.close()
        }
    }
}