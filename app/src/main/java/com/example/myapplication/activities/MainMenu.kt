package com.example.myapplication.activities

import android.content.Intent
import android.os.Bundle
import android.speech.RecognitionListener
import android.speech.RecognizerIntent
import android.speech.SpeechRecognizer
import android.widget.Button
import android.Manifest
import android.content.pm.PackageManager
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.fragment.app.Fragment
import com.example.myapplication.R
import com.example.myapplication.fragments.Configuration
import com.example.myapplication.fragments.ContactUs
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.button.MaterialButton

class MainMenu : TrackedAppCompatActivity() {

    companion object {
        private const val REQUEST_RECORD_AUDIO = 100
    }

    private lateinit var mbRegistroDiario: Button
    private lateinit var mbWeightHistory : MaterialButton
    private lateinit var mbVoiceAction : MaterialButton
    private lateinit var bottomNavigation: BottomNavigationView
    private lateinit var recognizer: SpeechRecognizer
    private lateinit var recognizerIntent : Intent

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main_menu)
        initComponents()
        initListeners()

    }

    private fun initComponents(){
        bottomNavigation = findViewById(R.id.bottom_navigation)
        mbRegistroDiario = findViewById(R.id.mbRegistroDiario)
        mbWeightHistory = findViewById(R.id.mbWeightHistory)
        mbVoiceAction = findViewById(R.id.mbVoiceAction)
        recognizer = SpeechRecognizer.createSpeechRecognizer(this)
        recognizerIntent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH).apply {
            putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM)
            putExtra(RecognizerIntent.EXTRA_LANGUAGE, "es-ES")
        }
        recognizer.setRecognitionListener(object : RecognitionListener {
            override fun onResults(results: Bundle?) {
                val spokenText = results
                    ?.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION)
                    ?.get(0)
                    ?.lowercase()

                handleVoiceCommand(spokenText)
            }

            override fun onError(error: Int) {}
            override fun onReadyForSpeech(params: Bundle?) {}
            override fun onBeginningOfSpeech() {}
            override fun onRmsChanged(rmsdB: Float) {}
            override fun onBufferReceived(buffer: ByteArray?) {}
            override fun onEndOfSpeech() {}
            override fun onPartialResults(partialResults: Bundle?) {}
            override fun onEvent(eventType: Int, params: Bundle?) {}
        })
    }


    private fun initListeners() {

        mbRegistroDiario.setOnClickListener {
            val intent = Intent(this, DailyReports::class.java)
            startActivity(intent)
        }

        mbVoiceAction.setOnClickListener {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO)
                == PackageManager.PERMISSION_GRANTED) {
                // Ya tenemos permiso, escuchamos directamente
                recognizer.startListening(recognizerIntent)
                Toast.makeText(this, "Escuchando...", Toast.LENGTH_SHORT).show()
            } else {
                // Pedimos el permiso
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(Manifest.permission.RECORD_AUDIO),
                    REQUEST_RECORD_AUDIO
                )
            }
        }

        bottomNavigation.setOnItemSelectedListener { item ->
            when (item.itemId) {

                R.id.home_fragment -> {
                    startActivity(Intent(this, MainMenu::class.java))
                    true
                }

                R.id.dashboard_fragment -> {
                    replaceFragment(ContactUs())
                    true
                }

                R.id.settings_fragment -> {
                    replaceFragment(Configuration())
                    true
                }
                R.id.notifications_fragment -> {
                    startActivity(Intent(this, SignInOrLogInActivity::class.java))
                    true
                }

                else -> false
            }
        }
        mbWeightHistory.setOnClickListener {
            val intent = Intent(this, historial_de_pesos::class.java)
            startActivity(intent)
        }
    }


    // Voice command
    private fun handleVoiceCommand(command: String?) {
        when {
            command?.contains("tiempo de uso") == true -> {
                startActivity(Intent(this, UsageReportsActivity::class.java))
            }
            command?.contains("historial de uso") == true -> {
                startActivity(Intent(this, HeightAndWeight::class.java))
            }
            command?.contains("registro") == true -> {
                startActivity(Intent(this, SignInActivity::class.java))
            }
        }
    }

    private fun replaceFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, fragment)
            .commit()
    }


}
