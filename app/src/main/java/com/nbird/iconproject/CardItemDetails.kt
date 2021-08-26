package com.nbird.iconproject

import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.cardview.widget.CardView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.model.GlideUrl
import java.io.File
import java.io.FileOutputStream
import java.io.OutputStream
import java.util.*
import java.io.IOException as IOException1


class CardItemDetails : AppCompatActivity() {
    private lateinit var toolbar: Toolbar
    private lateinit var tagImageView: ImageView
    private lateinit var iconImg: ImageView
    private lateinit var downloadIcon: CardView
    private lateinit var categoryTextView: TextView
    private lateinit var tagTextView: TextView
    private lateinit var styleTextView: TextView
    private lateinit var typeTextView: TextView
    private lateinit var IconIDTextView: TextView
    private lateinit var publishingDateTextView: TextView
    private lateinit var cardText:TextView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_card_item_details)

        title = "Icon Details"
        toolbar = findViewById(R.id.toolbar)
        tagImageView=findViewById(R.id.tag)
        iconImg=findViewById(R.id.iconImg)
        downloadIcon=findViewById(R.id.downloadIcon)
        categoryTextView=findViewById(R.id.category)
        tagTextView=findViewById(R.id.tagTextView)
        styleTextView=findViewById(R.id.styleTextView)
        cardText=findViewById(R.id.cardText)
        typeTextView=findViewById(R.id.typeTextView)
        IconIDTextView=findViewById(R.id.IconIDTextView)
        publishingDateTextView=findViewById(R.id.publishingDateTextView)

        if(intent.getBooleanExtra("tag", true)){
            cardText.text="RS "+intent.getStringExtra("cost")

            downloadIcon.setOnClickListener {Toast.makeText(this, "Pay RS "+intent.getStringExtra("cost"), Toast.LENGTH_SHORT).show()}
        }else{
            tagImageView.alpha=0.0f
            downloadIcon.setOnClickListener {
                iconImg.buildDrawingCache()
                val bmap: Bitmap = iconImg.getDrawingCache()

                val uri:Uri = saveImageToExternalStorage(bmap)
            }
        }

        val glideUrl = GlideUrl(intent.getStringExtra("imageUrl")) { mapOf(
            Pair(
                "Authorization",
                "Bearer bQKz5nqdDnQTmxpvbkoBsQEhmDNcXt5kRJ79TprvqK9dO9sG2VbVOCUTdtrP8aJC"
            )
        ) }
        Glide.with(this)
            .load(glideUrl).fitCenter()
            .error(Glide.with(this).load(glideUrl))
            .into(iconImg)

        categoryTextView.setText("Category : " + intent.getStringExtra("name"))
        tagTextView.setText("TAGS : " + intent.getStringExtra("tagTextView"))
        styleTextView.setText("Style : " + intent.getStringExtra("style"))
        typeTextView.setText("Type : " + intent.getStringExtra("type"))
        IconIDTextView.setText("Icon ID : " + intent.getIntExtra("iconID", 0).toString())
        publishingDateTextView.setText("Published Date : " + intent.getStringExtra("date"))
    }
    private fun saveImageToExternalStorage(bitmap: Bitmap):Uri{

        val sdCard = Environment.getExternalStorageDirectory().toString()
        val dir = File(sdCard + "/Android/")
        val file = File(dir, "${UUID.randomUUID()}.png")
        try {
            val stream: OutputStream = FileOutputStream(file)
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream)
            stream.flush()
            stream.close()
            toast("Image saved successfully in Android Directory of Internal Memory")
        } catch (e: IOException1){ // Catch the exception
            e.printStackTrace()
            toast("Error to save image.")
        }
        return Uri.parse(file.absolutePath)
    }
    fun Context.toast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}