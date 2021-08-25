package com.nbird.iconproject

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.telecom.Call
import android.widget.GridView
import android.widget.ListView
import android.widget.Toast
import com.google.android.material.textfield.TextInputEditText
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import org.json.JSONArray
import org.json.JSONObject
import java.io.IOException
import java.util.*

import javax.security.auth.callback.Callback

class MainActivity : AppCompatActivity() {
    lateinit var listView_details: ListView
    var arrayList_details:ArrayList<Model> = ArrayList();

    val client = OkHttpClient()
    lateinit var gridView: GridView
    lateinit var search_bar: TextInputEditText
    private var playerNames = arrayOf("Cristiano Ronaldo", "Joao Felix", "Bernado Silva", "Andre    Silve", "Bruno Fernandez", "William Carvalho", "Nelson Semedo", "Pepe", "Rui Patricio")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        gridView = findViewById(R.id.gridView)
        search_bar=findViewById(R.id.search_bar)
        val mainAdapter = MainAdapter(this@MainActivity, arrayList_details)
        gridView.adapter = mainAdapter


       run("dfd")

    }


    fun run(url: String) {
      //  progress.visibility = View.VISIBLE
        val request = Request.Builder()
            .url(url)
            .build()

        client.newCall(request).enqueue(object : Callback, okhttp3.Callback {
             fun onFailure(call: Call, e: IOException) {
              //  progress.visibility = View.GONE
            }

            fun onResponse(call: Call, response: Response) {
                var str_response = response.body!!.string()
                //creating json object
                val json_contact:JSONObject = JSONObject(str_response)
                //creating json array
                var jsonarray_info:JSONArray= json_contact.getJSONArray("info")
                var i:Int = 0
                var size:Int = jsonarray_info.length()
                arrayList_details= ArrayList();
                for (i in 0.. size-1) {
                    var json_objectdetail:JSONObject=jsonarray_info.getJSONObject(i)
                    var model:Model= Model();
                    model.imageUrl=json_objectdetail.getString("id")
                    model.name=json_objectdetail.getString("name")
                    arrayList_details.add(model)
                }

                runOnUiThread {
                    //stuff that updates ui
                    val mainAdapter = MainAdapter(this@MainActivity, arrayList_details)
                    gridView.adapter = mainAdapter
                }
             //   progress.visibility = View.GONE
            }

            override fun onFailure(call: okhttp3.Call, e: IOException) {
                Toast.makeText(this@MainActivity, "Failed", Toast.LENGTH_SHORT).show()
             //   TODO("Not yet implemented")
            }

            override fun onResponse(call: okhttp3.Call, response: Response) {
                Toast.makeText(this@MainActivity, "No Response", Toast.LENGTH_SHORT).show()
             //   TODO("Not yet implemented")
            }
        })
    }

}