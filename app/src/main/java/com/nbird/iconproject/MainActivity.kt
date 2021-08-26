package com.nbird.iconproject

import android.app.Dialog
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import org.json.JSONObject
import org.json.JSONTokener
import java.io.IOException
import java.util.*
import javax.security.auth.callback.Callback
import kotlin.collections.ArrayList

class MainActivity : AppCompatActivity() {
    var arrayList_details:ArrayList<Model> = ArrayList()
    private lateinit var toolbar: Toolbar
    val client = OkHttpClient()
    lateinit var gridView: GridView
    var loadingDialog: Dialog? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        title = "IconProject"
        toolbar = findViewById(R.id.toolbar)
        gridView = findViewById(R.id.gridView)
        loadingDialog = Dialog(this)
        loadingDialog!!.setContentView(R.layout.loading_screen)
        loadingDialog!!.window!!.setLayout(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT)
        loadingDialog!!.setCancelable(false)
        loadingDialog!!.show()

        run("https://api.iconfinder.com/v4/icons/search?query=cars")
    }
    fun run(url: String) {

        val request = Request.Builder()
            .url(url).header("Authorization", "Bearer bQKz5nqdDnQTmxpvbkoBsQEhmDNcXt5kRJ79TprvqK9dO9sG2VbVOCUTdtrP8aJC")
            .build()

        client.newCall(request).enqueue(object : Callback, okhttp3.Callback {

            override fun onFailure(call: okhttp3.Call, e: IOException) {
                Toast.makeText(this@MainActivity, "Failed", Toast.LENGTH_SHORT).show()
            }

            override fun onResponse(call: okhttp3.Call, response: Response) {
                var str_response = response.body!!.string()
                Log.d("yo", str_response.toString())
                val jsonObject = JSONTokener(str_response).nextValue() as JSONObject

                val jsonArray = jsonObject.getJSONArray("icons")

                for (i in 0 until jsonArray.length()) {
                    var model: Model = Model();
                    // ID
                    // Employee
                    val jsonStyle = JSONTokener(jsonArray[i].toString()).nextValue() as JSONObject
                    val jsonArrayStyle = jsonStyle.getJSONArray("categories")
                    if (jsonArrayStyle.length() > 0) {
                        model.name = jsonArrayStyle.getJSONObject(0).getString("name")
                    }

                    val is_premium = jsonArray.getJSONObject(i).getBoolean("is_premium")
                    if (is_premium) {
                        val jsonPrice = JSONTokener(jsonArray[i].toString()).nextValue() as JSONObject
                        val jsonPriceArray = jsonPrice.getJSONArray("prices")
                        if (jsonPriceArray.length() > 0) {
                            model.cost = jsonPriceArray.getJSONObject(0).getString("price")
                        }
                        model.tag = true
                    } else {

                        model.tag = false
                    }

                    try {
                        val jsonpicUrl = JSONTokener(jsonArray[i].toString()).nextValue() as JSONObject
                        val jsonpicUrlArray = jsonpicUrl.getJSONArray("raster_sizes")

                        val jsonpicVectorIcon = JSONTokener(jsonArray[i].toString()).nextValue() as JSONObject
                        val jsonpicVectorIconArray = jsonpicVectorIcon.getJSONArray("vector_sizes")
                        if (jsonpicVectorIconArray.length() > 0) {
                            val jsontarget_sizes = JSONTokener(jsonpicVectorIconArray[0].toString()).nextValue() as JSONObject
                            val jsontarget_sizesArray = jsontarget_sizes.getJSONArray("target_sizes").getJSONArray(0).length()
                            if (jsontarget_sizesArray >= 8) {
                                val jsonpicUrlSecondary = JSONTokener(jsonpicUrlArray[8 - 1].toString()).nextValue() as JSONObject
                                val jsonpicUrlSecondaryArray = jsonpicUrlSecondary.getJSONArray("formats")
                                if (jsonpicUrlSecondaryArray.length() > 0) {
                                    model.imageUrl = jsonpicUrlSecondaryArray.getJSONObject(0).getString("preview_url")
                                    Log.i("url", model.imageUrl)
                                }
                            } else {
                                val jsonpicUrlSecondary = JSONTokener(jsonpicUrlArray[jsontarget_sizesArray - 1].toString()).nextValue() as JSONObject
                                val jsonpicUrlSecondaryArray = jsonpicUrlSecondary.getJSONArray("formats")
                                if (jsonpicUrlSecondaryArray.length() > 0) {
                                    model.imageUrl = jsonpicUrlSecondaryArray.getJSONObject(0).getString("preview_url")
                                    Log.i("url", model.imageUrl)
                                }
                            }
                        }
                    }catch (e:Exception){
                        Log.d("Error",e.toString())
                    }

                    model.type = jsonArray.getJSONObject(i).getString("type")
                    Log.i("type", model.type)

                    model.tagTextView = jsonArray.getJSONObject(i).getString("tags")
                    Log.i("tags", model.tagTextView)

                    model.iconId = jsonArray.getJSONObject(i).getInt("icon_id")
                    Log.i("tags", model.iconId.toString())

                    model.date = jsonArray.getJSONObject(i).getString("published_at")
                    Log.i("tags", model.date)


                    val jsonstyle = JSONTokener(jsonArray[i].toString()).nextValue() as JSONObject
                    val jsonstyleArray = jsonstyle.getJSONArray("styles")
                    if (jsonstyleArray.length() > 0) {
                        model.style = jsonstyleArray.getJSONObject(0).getString("name")
                    }
                    arrayList_details.add(model)

                }
                runOnUiThread {
                    loadingDialog!!.dismiss()
                    val mainAdapter = MainAdapter(this@MainActivity, arrayList_details, str_response)
                    gridView.adapter = mainAdapter
                }
            }
        })
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu, menu)
        val search = menu.findItem(R.id.appSearchBar)
        val searchView = search.actionView as SearchView
        searchView.queryHint = "Search"
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                if (query != null) {
                    arrayList_details.clear()
                    loadingDialog!!.show()
                    run("https://api.iconfinder.com/v4/icons/search?query=$query")
                }
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {

                return true
            }
        })
        return super.onCreateOptionsMenu(menu)
    }

}