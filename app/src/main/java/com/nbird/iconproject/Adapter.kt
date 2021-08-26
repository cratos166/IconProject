package com.nbird.iconproject

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.model.GlideUrl
import org.json.JSONObject
import org.json.JSONTokener
import java.lang.Exception
import java.util.*

internal class MainAdapter(
    private val context: MainActivity,
    private val arrayLogo: ArrayList<Model>,
    private val responce: String,
) :
    BaseAdapter() {
    private var layoutInflater: LayoutInflater? = null
    private lateinit var imageView: ImageView
    private lateinit var downloadImg: ImageView
    private lateinit var tag: ImageView
    private lateinit var textView: TextView
    private lateinit var cost: TextView
    private lateinit var cardView: CardView

    override fun getCount(): Int {
        return arrayLogo.size
    }
    override fun getItem(position: Int): Any? {
        return null
    }
    override fun getItemId(position: Int): Long {
        return 0
    }
    override fun getView(
            position: Int,
            convertView: View?,
            parent: ViewGroup
    ): View? {
        var convertView = convertView
        if (layoutInflater == null) {
            layoutInflater =
                context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        }
        if (convertView == null) {
            convertView = layoutInflater!!.inflate(R.layout.card_item, null)
        }
        imageView = convertView!!.findViewById(R.id.icon_img)
        textView = convertView.findViewById(R.id.icon_name)
        cardView=convertView.findViewById(R.id.cardview_id)
        tag=convertView.findViewById(R.id.tag)
        cost=convertView.findViewById(R.id.cost)
        downloadImg=convertView.findViewById(R.id.downloadicon)
        textView.setText(arrayLogo[position].name.toUpperCase())

        try{
            val glideUrl = GlideUrl(arrayLogo[position].imageUrl) { mapOf(Pair("Authorization", "Bearer bQKz5nqdDnQTmxpvbkoBsQEhmDNcXt5kRJ79TprvqK9dO9sG2VbVOCUTdtrP8aJC")) }
            Glide.with(context)
                    .load(glideUrl).fitCenter()
                    .error(Glide.with(context).load(glideUrl))
                    .into(imageView)
        }catch (e:Exception){

        }
        if(arrayLogo[position].tag){
            downloadImg.alpha=0.0f
            cost.setText("RS "+arrayLogo[position].cost.toUpperCase())
        }else{
            cost.alpha=0.0f
            tag.alpha=0.0f
        }

        cardView.setOnClickListener {
            val intent = Intent(context, CardItemDetails::class.java)
            intent.putExtra("imageUrl",arrayLogo[position].imageUrl)
            intent.putExtra("tag",arrayLogo[position].tag)
            intent.putExtra("name",arrayLogo[position].name.toUpperCase())
            intent.putExtra("cost",arrayLogo[position].cost)
            intent.putExtra("tagTextView",arrayLogo[position].tagTextView)
            intent.putExtra("style",arrayLogo[position].style.toUpperCase())
            intent.putExtra("type",arrayLogo[position].type.toUpperCase())
            intent.putExtra("iconID",arrayLogo[position].iconId)
            intent.putExtra("date",arrayLogo[position].date)
            context.startActivity(intent)
        }
        return convertView
    }
}