package com.example.shopnow.activities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.shopnow.databinding.ActivityPaymentBinding
import com.example.shopnow.utils.Constants

class PaymentActivity : AppCompatActivity() {

    private lateinit var binding: ActivityPaymentBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPaymentBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val orderCode = intent.getStringExtra("orderCode")

        binding.webview.settings.javaScriptEnabled = true
        binding.webview.loadUrl(Constants.PAYMENT_URL + orderCode)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    override fun onSupportNavigateUp(): Boolean {
        finish()
        return super.onSupportNavigateUp()
    }
}


