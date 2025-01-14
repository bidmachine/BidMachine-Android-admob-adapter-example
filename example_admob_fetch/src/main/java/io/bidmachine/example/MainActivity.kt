package io.bidmachine.example

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import io.bidmachine.example.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater).apply {
            setContentView(root)
            bPrebid.setOnClickListener {
                startActivity(Intent(this@MainActivity, PrebidAdIntegrationActivity::class.java))
            }
            bWaterfall.setOnClickListener {
                startActivity(Intent(this@MainActivity, WaterfallAdIntegrationActivity::class.java))
            }
        }
    }
}