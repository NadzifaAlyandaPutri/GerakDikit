package com.example.lifecyledemo

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

private const val TAG = "LifecycleDemo"

class MainActivity : AppCompatActivity() {

    private var reps = 0
    private lateinit var tvName: TextView
    private lateinit var tvSport: TextView
    private lateinit var tvReps: TextView
    private lateinit var tvLevel: TextView
    private lateinit var tvState: TextView
    private lateinit var etName: EditText
    private lateinit var etSport: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        Log.i(TAG, "MainActivity onCreate (savedInstanceState=${savedInstanceState != null})")

        tvName = findViewById(R.id.tvName)
        tvSport = findViewById(R.id.tvSport)
        tvReps = findViewById(R.id.tvReps)
        tvLevel = findViewById(R.id.tvLevel)
        tvState = findViewById(R.id.tvState)
        etName = findViewById(R.id.etName)
        etSport = findViewById(R.id.etSport)

        // Pulihkan hitungan jika Activity dibuat ulang (mis. rotasi layar)
        reps = savedInstanceState?.getInt("reps") ?: 0
        updateCounter()

        findViewById<Button>(R.id.btnPushUp).setOnClickListener {
            reps++
            updateCounter()
        }
        findViewById<Button>(R.id.btnRest).setOnClickListener {
            Toast.makeText(this, "Istirahat dulu, minum air! 💧", Toast.LENGTH_SHORT).show()
        }
        findViewById<Button>(R.id.btnSave).setOnClickListener {
            val name = etName.text.toString().trim()
            val sport = etSport.text.toString().trim()
            if (name.isNotEmpty()) tvName.text = name
            if (sport.isNotEmpty()) tvSport.text = sport
            Toast.makeText(this, "Profil disimpan", Toast.LENGTH_SHORT).show()
        }
        findViewById<Button>(R.id.btnReset).setOnClickListener {
            reps = 0
            updateCounter()
            etName.text.clear()
            etSport.text.clear()
            tvName.setText(R.string.default_name)
            tvSport.setText(R.string.default_sport)
        }
        findViewById<Button>(R.id.btnPortfolio).setOnClickListener {
            val i = Intent(this, SecondActivity::class.java)
            i.putExtra("name", tvName.text.toString())
            i.putExtra("reps", reps)
            startActivity(i)
        }
    }

    private fun updateCounter() {
        tvReps.text = "💪 Push-up: $reps"
        tvLevel.text = "🔥 Level: ${reps / 10 + 1}"
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putInt("reps", reps)
        Log.i(TAG, "MainActivity onSaveInstanceState")
    }

    override fun onStart() {
        super.onStart()
        tvState.text = "State: Started (onStart)"
        Log.i(TAG, "MainActivity onStart")
    }

    override fun onResume() {
        super.onResume()
        tvState.text = "State: Active (onResume)"
        Log.i(TAG, "MainActivity onResume")
    }

    override fun onPause() {
        super.onPause()
        Log.i(TAG, "MainActivity onPause")
    }

    override fun onStop() {
        super.onStop()
        Log.i(TAG, "MainActivity onStop")
    }

    override fun onRestart() {
        super.onRestart()
        Log.i(TAG, "MainActivity onRestart")
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.i(TAG, "MainActivity onDestroy (isChangingConfigurations=$isChangingConfigurations)")
    }
}
