package com.rfapp.e_statsi.adapter

import android.app.DownloadManager
import android.content.Context
import android.net.Uri
import android.os.Environment.DIRECTORY_DOWNLOADS
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.android.gms.tasks.OnFailureListener
import com.google.android.gms.tasks.OnSuccessListener
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.rfapp.e_statsi.DetailActivity
import com.rfapp.e_statsi.HomeActivity
import com.rfapp.e_statsi.R
import com.rfapp.e_statsi.model.Berkas
import java.io.File
import java.lang.Exception
import java.util.*
import kotlin.collections.ArrayList

class HomeAdapter(var data: List<Berkas>, private val listener: (Berkas) -> Unit) : RecyclerView.Adapter<HomeAdapter.ViewHolder>(){

    private lateinit var contextAdapter : Context
    private lateinit var sReference : StorageReference

    class ViewHolder(view : View) : RecyclerView.ViewHolder(view){
        private lateinit var sReference : StorageReference
        private lateinit var reference: StorageReference
        var dataList = ArrayList<Berkas>()

        val tvNama:TextView = view.findViewById(R.id.tv_nama)
        val tvLink:TextView = view.findViewById(R.id.tv_link)
        val ivDownload:ImageView = view.findViewById(R.id.iv_download)

        fun bindItem(data: Berkas, listener: (Berkas) -> Unit, context : Context, position : Int) {

            tvNama.text = data.nama
            tvLink.text = data.data_url

            itemView.setOnClickListener {
               listener(data)
            }

            ivDownload.setOnClickListener {
                downloadFiles(this.tvNama.context, ".pdf", "${data.nama}", DIRECTORY_DOWNLOADS, "${data.data_url}")
            }

        }

        private fun downloadFiles(context: Context, fileExtension: String, fileName: String, destinationDirectory: String, url: String) {
            val downloadManager = context.getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
            val uri = Uri.parse(url)
            val request = DownloadManager.Request(uri)

            request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
            request.setDestinationInExternalFilesDir(context, destinationDirectory, fileName + fileExtension)

            downloadManager.enqueue(request)
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int):ViewHolder {
        sReference = FirebaseStorage.getInstance().getReference("Data")
        val layoutInflater = LayoutInflater.from(parent.context)
        contextAdapter = parent.context
        val inflatedView = layoutInflater.inflate(R.layout.item_row_pdf, parent, false)
        return ViewHolder(inflatedView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
       holder.bindItem(data[position], listener, contextAdapter, position)
    }

    override fun getItemCount(): Int {
        return data.size
    }
}