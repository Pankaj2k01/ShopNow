package com.example.shopnow.activities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.shopnow.adapters.ProductAdapter
import com.example.shopnow.databinding.ActivitySearchBinding
import com.example.shopnow.model.Product
import com.example.shopnow.utils.Constants
import org.json.JSONException
import org.json.JSONObject

class SearchActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySearchBinding
    private lateinit var productAdapter: ProductAdapter
    private lateinit var products: ArrayList<Product>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySearchBinding.inflate(layoutInflater)
        setContentView(binding.root)

        products = ArrayList()
        productAdapter = ProductAdapter(this, products)

        val query = intent.getStringExtra("query")

        supportActionBar?.title = query
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        getProducts(query.toString())

        val layoutManager = GridLayoutManager(this, 2)
        binding.productList.layoutManager = layoutManager
        binding.productList.adapter = productAdapter
    }

    override fun onSupportNavigateUp(): Boolean {
        finish()
        return super.onSupportNavigateUp()
    }

    private fun getProducts(query: String) {
        val queue = Volley.newRequestQueue(this)

        val url = Constants.GET_PRODUCTS_URL + "?q=" + query
        val request = StringRequest(Request.Method.GET, url, Response.Listener { response ->
            try {
                val jsonObject = JSONObject(response)
                if (jsonObject.has("status") && jsonObject.getString("status") == "success") {
                    val productsArray = jsonObject.getJSONArray("products")
                    for (i in 0 until productsArray.length()) {
                        val childObj = productsArray.getJSONObject(i)

                        // Make sure you have these fields in your API response
                        val productName = childObj.getString("name")
                        val productImage = Constants.PRODUCTS_IMAGE_URL + childObj.getString("image")
                        val productStatus = childObj.getString("status")
                        val productPrice = childObj.getDouble("price")
                        val productDiscountPrice = childObj.getDouble("price_discount")
                        val productStock = childObj.getInt("stock")
                        val productId = childObj.getInt("id")

                        val product = Product(
                            productName,
                            productImage,
                            productStatus,
                            productPrice,
                            productDiscountPrice,
                            productStock,
                            productId
                        )
                        products.add(product)
                    }
                    productAdapter.notifyDataSetChanged()
                }
            } catch (e: JSONException) {
                e.printStackTrace()
            }
        }, Response.ErrorListener { })

        queue.add(request)
    }
}


