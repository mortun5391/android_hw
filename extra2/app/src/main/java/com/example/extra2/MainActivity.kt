package com.example.extra2

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {
    private val launcher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { res ->
        val result = res.data?.getStringExtra("result") ?: "нет данных"
        findViewById<TextView>(R.id.resultText).text = "Результат: $result"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val input = findViewById<EditText>(R.id.inputText)
        findViewById<Button>(R.id.btnExplicit).setOnClickListener {
            val intent = Intent(this, ReceiverActivity::class.java).apply {
                putExtra("text", input.text.toString())
                putExtra("imageRes", android.R.drawable.ic_menu_gallery)
            }
            launcher.launch(intent)
        }

        findViewById<Button>(R.id.btnImplicit).setOnClickListener {
            val share = Intent(Intent.ACTION_SEND).apply {
                type = "text/plain"
                putExtra(Intent.EXTRA_TEXT, input.text.toString())
            }
            startActivity(Intent.createChooser(share, "Отправить текст"))
        }
    }
}
