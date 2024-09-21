package com.example.shopnow.activities

import android.content.Intent
import android.os.Bundle
import android.text.Html
import android.view.Menu
import android.view.MenuItem
import androidx.annotation.NonNull
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.bumptech.glide.Glide
import com.example.shopnow.R
import com.example.shopnow.databinding.ActivityProductDetailBinding
import com.example.shopnow.model.Product
import com.example.shopnow.utils.Constants
import com.hishd.tinycart.util.TinyCartHelper
import org.json.JSONException
import org.json.JSONObject


class ProductDetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityProductDetailBinding
    private lateinit var currentProduct: Product

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProductDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val name = intent.getStringExtra("name")
        val image = intent.getStringExtra("image")
        val id = intent.getIntExtra("id", 0)
        val price = intent.getDoubleExtra("price", 0.0)

        Glide.with(this)
            .load(image)
            .into(binding.productImage)

        getProductDetails(id)

        supportActionBar?.title = name

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val cart = TinyCartHelper.getCart()

        binding.addToCartBtn.setOnClickListener {
            cart.addItem(currentProduct, 1)
            binding.addToCartBtn.isEnabled = false
            binding.addToCartBtn.text = "Added in cart"
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.cart, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(@NonNull item: MenuItem): Boolean {
        if (item.itemId == R.id.cart) {
            startActivity(Intent(this, CartActivity::class.java))
        }
        return super.onOptionsItemSelected(item)
    }

    private fun getProductDetails(id: Int) {
        val queue = Volley.newRequestQueue(this)

        val url = Constants.GET_PRODUCT_DETAILS_URL + id

        val request = StringRequest(Request.Method.GET, url, Response.Listener { response ->
            try {
                val jsonObject = JSONObject(response)
                if (jsonObject.getString("status") == "success") {
                    val product = jsonObject.getJSONObject("product")
                    val description = product.getString("description")
                    binding.productDescription.text = Html.fromHtml(description)

                    currentProduct = Product(
                        product.getString("name"),
                        Constants.PRODUCTS_IMAGE_URL + product.getString("image"),
                        product.getString("status"),
                        product.getDouble("price"),
                        product.getDouble("price_discount"),
                        product.getInt("stock"),
                        product.getInt("id")
                    )
                }
            } catch (e: JSONException) {
                e.printStackTrace()
            }
        }, Response.ErrorListener { })

        queue.add(request)
    }

    override fun onSupportNavigateUp(): Boolean {
        finish()
        return super.onSupportNavigateUp()
    }
}


