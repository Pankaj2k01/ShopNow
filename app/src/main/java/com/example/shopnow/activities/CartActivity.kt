package com.example.shopnow.activities

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.shopnow.adapters.CartAdapter
import com.example.shopnow.databinding.ActivityCartBinding
import com.example.shopnow.model.Product
import com.hishd.tinycart.util.TinyCartHelper

class CartActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCartBinding
    private lateinit var adapter: CartAdapter
    private lateinit var products: ArrayList<Product>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCartBinding.inflate(layoutInflater)
        setContentView(binding.root)

        products = ArrayList()

        val cart = TinyCartHelper.getCart()

        for ((item, quantity) in cart.getAllItemsWithQty().entries) {
            val product = item as Product
            product.quantity = quantity

            products.add(product)
        }

        adapter = CartAdapter(this, products, object : CartAdapter.CartListener {
            override fun onQuantityChanged() {
                binding.subtotal.text = String.format("PKR %.2f", cart.getTotalPrice())
            }
        })

        val layoutManager = LinearLayoutManager(this)
        val itemDecoration = DividerItemDecoration(this, layoutManager.orientation)
        binding.cartList.layoutManager = layoutManager
        binding.cartList.addItemDecoration(itemDecoration)
        binding.cartList.adapter = adapter

        binding.subtotal.text = String.format("PKR %.2f", cart.getTotalPrice())

        binding.continueBtn.setOnClickListener {
            startActivity(Intent(this, CheckoutActivity::class.java))
        }

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    override fun onSupportNavigateUp(): Boolean {
        finish()
        return super.onSupportNavigateUp()
    }
}


