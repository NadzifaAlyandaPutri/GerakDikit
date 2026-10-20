package com.example.lifecyledemo

import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

private const val TAG2 = "LifecycleDemo"

class SecondActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_second)
        Log.i(TAG2, "SecondActivity onCreate")

        val name = intent.getStringExtra("name") ?: getString(R.string.default_name)
        val reps = intent.getIntExtra("reps", 0)
        findViewById<TextView>(R.id.tvOwner).text = "Atlet: $name"
        findViewById<TextView>(R.id.tvStatReps).text = reps.toString()
        findViewById<Button>(R.id.btnBack).setOnClickListener { finish() }
    }

    override fun onStart() { super.onStart(); Log.i(TAG2, "SecondActivity onStart") }
    override fun onResume() { super.onResume(); Log.i(TAG2, "SecondActivity onResume") }
    override fun onPause() { super.onPause(); Log.i(TAG2, "SecondActivity onPause") }
    override fun onStop() { super.onStop(); Log.i(TAG2, "SecondActivity onStop") }
    override fun onRestart() { super.onRestart(); Log.i(TAG2, "SecondActivity onRestart") }
    override fun onDestroy() { super.onDestroy(); Log.i(TAG2, "SecondActivity onDestroy") }
}
