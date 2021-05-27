package com.rfapp.e_statsi.adapter

import android.app.DownloadManager
import android.content.Context
import android.net.Uri
import android.os.Environment.DIRECTORY_DOWNLOADS
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.rfapp.e_statsi.R
import com.rfapp.e_statsi.model.Berkas
import java.util.*
import kotlin.collections.ArrayList

class HomeAdapter(private var data: MutableList<Berkas>, private val listener: (Berkas) -> Unit) : RecyclerView.Adapter<HomeAdapter.ViewHolder>(), Filterable{

    private lateinit var contextAdapter : Context
    private lateinit var sReference : StorageReference
    private  var filterList : ArrayList<Berkas>

    init {
        filterList = data as ArrayList<Berkas>
    }

    override fun getFilter(): Filter {
        return object : Filter(){
            override fun performFiltering(constraint: CharSequence?): FilterResults {
                val charSearch = constraint.toString()
                if (charSearch.isEmpty()){
                    filterList = data as ArrayList<Berkas>
                }else{
                    val resultList = ArrayList<Berkas>()
                    for (row in data){
                        if (row.nama!!.toLowerCase(Locale.ROOT).contains(constraint.toString().toLowerCase(
                                Locale.ROOT))){
                            resultList.add(row)
                        }
                    }
                    filterList = resultList
                }

                val filterResult = FilterResults()
                filterResult.values = filterList
                return filterResult
            }

            @Suppress("UNCHECKED_CAST")
            override fun publishResults(constarint: CharSequence?, result: FilterResults?) {
                filterList = result?.values as ArrayList<Berkas>
                notifyDataSetChanged()
            }

        }
    }

    class ViewHolder(view : View) : RecyclerView.ViewHolder(view){

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
       holder.bindItem(filterList[position], listener, contextAdapter, position)
    }

    override fun getItemCount(): Int {
        return filterList.size
    }

}