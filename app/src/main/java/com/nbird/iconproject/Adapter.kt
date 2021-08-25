package com.nbird.iconproject

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.cardview.widget.CardView
import java.util.*

internal class MainAdapter(
    private val context: MainActivity,
    private val numbersInWords: ArrayList<Model>,
) :
    BaseAdapter() {
    private var layoutInflater: LayoutInflater? = null
    private lateinit var imageView: ImageView
    private lateinit var textView: TextView
    private lateinit var cardView: CardView
    override fun getCount(): Int {
        return numbersInWords.size
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
       // imageView.setImageResource(numberImage[position])
      //  textView.text = numbersInWords[position]

        cardView.setOnClickListener {  Toast.makeText(
            context, "You CLicked " + numbersInWords.get(+position),
            Toast.LENGTH_SHORT
        ).show() }



        return convertView
    }
}