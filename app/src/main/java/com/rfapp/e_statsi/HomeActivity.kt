package com.rfapp.e_statsi

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.ArrayAdapter
import android.widget.SearchView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.*
import com.google.firebase.storage.StorageReference
import com.rfapp.e_statsi.Utils.Preferences
import com.rfapp.e_statsi.adapter.HomeAdapter
import com.rfapp.e_statsi.databinding.ActivityMainBinding
import com.rfapp.e_statsi.model.Berkas
import kotlinx.android.synthetic.main.activity_home.*

class HomeActivity : AppCompatActivity() {

    private lateinit var databaseReference: DatabaseReference
    private lateinit var dataRecyclerView: RecyclerView
    private var dataArrayList: ArrayList<Berkas> = arrayListOf()
    private var matchData:ArrayList<Berkas> = arrayListOf()
    private var _binding: ActivityMainBinding? = null
    private val binding get() = _binding!!



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(R.layout.activity_home)

        dataRecyclerView = findViewById(R.id.rv_data)
        dataRecyclerView.layoutManager = LinearLayoutManager(this)
        dataRecyclerView.setHasFixedSize(true)
        dataArrayList = arrayListOf<Berkas>()
        getData()


    }

    private fun getData() {
        databaseReference = FirebaseDatabase.getInstance().getReference("Data")
        databaseReference.addValueEventListener(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()){

                    for (userSnapshot in snapshot.children){
                        val user = userSnapshot.getValue(Berkas::class.java)
                        dataArrayList.add(user!!)
                    }
                }

                dataRecyclerView.adapter = HomeAdapter(dataArrayList){
                    val intent = Intent(this@HomeActivity, DetailActivity::class.java).putExtra("data", it)
                    startActivity(intent)
                }

            }

            override fun onCancelled(error: DatabaseError) {

            }

        })

    }
}