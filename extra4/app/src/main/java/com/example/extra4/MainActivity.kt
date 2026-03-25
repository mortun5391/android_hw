package com.example.extra4

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import java.lang.ref.WeakReference
import java.util.ArrayDeque

class MainActivity : AppCompatActivity() {
    private val stack = ArrayDeque<String>()
    private val queue = ArrayDeque<String>()
    private var leakSafeRef: WeakReference<ByteArray>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val input = findViewById<EditText>(R.id.input)
        val state = findViewById<TextView>(R.id.state)

        fun render() { state.text = "Stack=$stack\nQueue=$queue" }

        findViewById<Button>(R.id.pushBtn).setOnClickListener { stack.addLast(input.text.toString()); render() }
        findViewById<Button>(R.id.enqueueBtn).setOnClickListener { queue.addLast(input.text.toString()); render() }
        findViewById<Button>(R.id.popBtn).setOnClickListener { if (stack.isNotEmpty()) stack.removeLast(); render() }
        findViewById<Button>(R.id.dequeueBtn).setOnClickListener { if (queue.isNotEmpty()) queue.removeFirst(); render() }

        findViewById<Button>(R.id.clearBtn).setOnClickListener {
            stack.clear(); queue.clear()
            leakSafeRef = WeakReference(ByteArray(0))
            Runtime.getRuntime().gc()
            render()
        }
        render()
    }

    override fun onDestroy() {
        super.onDestroy()
        stack.clear(); queue.clear(); leakSafeRef?.clear()
    }
}
