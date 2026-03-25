package com.example.extra2

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class ReceiverActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_receiver)

        val text = intent.getStringExtra("text").orEmpty()
        val imageRes = intent.getIntExtra("imageRes", android.R.drawable.ic_menu_gallery)
        findViewById<TextView>(R.id.receivedText).text = text
        findViewById<ImageView>(R.id.receivedImage).setImageResource(imageRes)

        findViewById<Button>(R.id.btnBackResult).setOnClickListener {
            setResult(Activity.RESULT_OK, Intent().putExtra("result", "Получено: $text"))
            finish()
        }
    }
}
