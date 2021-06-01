package com.rfapp.e_statsi

import android.os.AsyncTask
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.github.barteksc.pdfviewer.PDFView
import com.rfapp.e_statsi.model.Berkas
import kotlinx.android.synthetic.main.activity_detail.*
import java.io.BufferedInputStream
import java.io.BufferedReader
import java.io.InputStream
import java.io.InputStreamReader
import java.lang.StringBuilder
import java.net.HttpURLConnection
import java.net.URL

class DetailActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)

        val pdfview = findViewById<PDFView>(R.id.pdf_view)

        val data = intent.getParcelableExtra<Berkas>("data")

        tv_judul.text = data.nama


        iv_back.setOnClickListener {
            finish()
        }

       loadPdf().execute(data.data_url)
    }

    inner class loadPdf() : AsyncTask<String, Void, InputStream>(){
        override fun doInBackground(vararg params: String?): InputStream? {
            try {
                val url = URL(params[0])
                val httpURLConnection = url.openConnection() as HttpURLConnection

               if (httpURLConnection.responseCode == 200){
                    val stream = BufferedInputStream(httpURLConnection.inputStream)
                    progressBar.visibility = View.VISIBLE
                    return stream
                }

            }catch (e: Exception){
                e.printStackTrace()
            }
            return null
        }

        override fun onPostExecute(result: InputStream?) {
            pdf_view.fromStream(result).load()
            progressBar.visibility = View.GONE
       }

   }


}