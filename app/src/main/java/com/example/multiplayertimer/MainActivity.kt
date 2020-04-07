package com.example.multiplayertimer

import android.os.Bundle
import android.os.CountDownTimer
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.multiplayertimer.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        enableImmersiveMode()
        setContentView(binding.root)

        object: CountDownTimer(6 * 60 * 1000, 1000) {
            override fun onFinish() {
                AlertDialog.Builder(this@MainActivity)
                    .setTitle("App Timeout")
                    .setMessage("The prototype can only be used for 10 minutes. Relaunch to continue testing")
                    .setCancelable(false)
                    .create()
                    .show()
            }

            override fun onTick(millisUntilFinished: Long) {
                // Nothing
            }
        }.start()
    }

    fun enableImmersiveMode() {
        binding.root.systemUiVisibility =
            (View.SYSTEM_UI_FLAG_IMMERSIVE or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION or View.SYSTEM_UI_FLAG_FULLSCREEN)
    }
}
