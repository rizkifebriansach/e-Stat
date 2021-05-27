package com.rfapp.e_statsi
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.appcompat.widget.SearchView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.*
import com.rfapp.e_statsi.adapter.HomeAdapter
import com.rfapp.e_statsi.databinding.ActivityMainBinding
import com.rfapp.e_statsi.model.Berkas
import kotlinx.android.synthetic.main.activity_home.*

class HomeActivity : AppCompatActivity() {

    private lateinit var databaseReference: DatabaseReference
    private var dataRecyclerView: RecyclerView? = null
    private var _binding: ActivityMainBinding? = null
    private var adapter: HomeAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(R.layout.activity_home)

        dataRecyclerView?.adapter = adapter
        dataRecyclerView = findViewById(R.id.rv_data)
        dataRecyclerView?.layoutManager = LinearLayoutManager(this)
        dataRecyclerView?.setHasFixedSize(true)
        getData()

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(query: String?): Boolean {
                adapter?.filter?.filter(query)
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                Log.d("onQueryTextChange", "query :"+newText)
                adapter?.filter?.filter(newText)
                return true
            }

        })

    }

    fun getData() {
        val dataArrayList = ArrayList<Berkas>()
        databaseReference = FirebaseDatabase.getInstance().getReference("Data")
        databaseReference.addValueEventListener(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()){

                    for (userSnapshot in snapshot.children){
                        val user = userSnapshot.getValue(Berkas::class.java)
                        dataArrayList.add(user!!)
                    }
                }

                dataRecyclerView?.adapter = HomeAdapter(dataArrayList){
                    val intent = Intent(this@HomeActivity, DetailActivity::class.java).putExtra("data", it)
                    startActivity(intent)
                }

            }

            override fun onCancelled(error: DatabaseError) {

            }

        })

    }
}