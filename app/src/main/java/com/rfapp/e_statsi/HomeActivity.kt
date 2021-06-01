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
import com.rfapp.e_statsi.model.Berkas
import kotlinx.android.synthetic.main.activity_home.*
import java.util.*
import kotlin.collections.ArrayList

class HomeActivity : AppCompatActivity() {

    private lateinit var databaseReference: DatabaseReference
    private  var dataRecyclerView: RecyclerView? = null
    private  var adapter: HomeAdapter? = null
    private lateinit var myList : ArrayList<Berkas>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        val searchView = findViewById<SearchView>(R.id.searchView)
        databaseReference = FirebaseDatabase.getInstance().getReference("Data")
        dataRecyclerView?.adapter = adapter
        dataRecyclerView = findViewById(R.id.rv_data)
        dataRecyclerView?.layoutManager = LinearLayoutManager(this)
        dataRecyclerView?.setHasFixedSize(true)
        getData()

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String): Boolean {
                Log.d("onQueryTextChange", "query :"+newText)
                firebaseSearch(newText)
                return true
            }

        })

    }

    private fun getData() {
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

                dataRecyclerView!!.adapter = HomeAdapter(dataArrayList){
                    val intent = Intent(this@HomeActivity, DetailActivity::class.java).putExtra("data", it)
                    startActivity(intent)
                }

            }

            override fun onCancelled(error: DatabaseError) {

            }

        })

    }

    private fun firebaseSearch(searchText : String){

        val dataArrayList = ArrayList<Berkas>()
        databaseReference = FirebaseDatabase.getInstance().getReference("Data")
        databaseReference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {

                    for (userSnapshot in snapshot.children) {
                        val userModel = userSnapshot.getValue(Berkas::class.java)

                        if (userModel?.nama!!.toLowerCase(Locale.ROOT).contains(searchText.toLowerCase(
                                Locale.ROOT))){
                            dataArrayList.add(userModel)
                        }
                    }


                val userAdapter = HomeAdapter(dataArrayList){
                    val intent = Intent(this@HomeActivity, DetailActivity::class.java).putExtra("data", it)
                    startActivity(intent)
                }
                dataRecyclerView?.adapter = userAdapter
                userAdapter.notifyDataSetChanged()

            }

            override fun onCancelled(error: DatabaseError) {

            }
        })
    }

}