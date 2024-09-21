package com.example.shopnow.activities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import com.android.volley.Request
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.shopnow.adapters.ProductAdapter
import com.example.shopnow.databinding.ActivityCategoryBinding
import com.example.shopnow.model.Product
import com.example.shopnow.utils.Constants
import org.json.JSONException
import org.json.JSONObject

class CategoryActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCategoryBinding
    private lateinit var productAdapter: ProductAdapter
    private lateinit var products: ArrayList<Product>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCategoryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        products = ArrayList()
        productAdapter = ProductAdapter(this, products)

        val catId = intent.getIntExtra("catId", 0)
        val categoryName = intent.getStringExtra("categoryName")

        supportActionBar?.title = categoryName
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        getProducts(catId)

        val layoutManager = GridLayoutManager(this, 2)
        binding.productList.layoutManager = layoutManager
        binding.productList.adapter = productAdapter
    }

    override fun onSupportNavigateUp(): Boolean {
        finish()
        return super.onSupportNavigateUp()
    }

    private fun getProducts(catId: Int) {
        val queue = Volley.newRequestQueue(this)

        val url = Constants.GET_PRODUCTS_URL + "?category_id=$catId"
        val request = StringRequest(Request.Method.GET, url, { response ->
            try {
                val jsonObject = JSONObject(response)
                if (jsonObject.getString("status").equals("success", ignoreCase = true)) {
                    val productsArray = jsonObject.getJSONArray("products")
                    for (i in 0 until productsArray.length()) {
                        val productObject = productsArray.getJSONObject(i)
                        val product = Product(
                            productObject.getString("name"),
                            Constants.PRODUCTS_IMAGE_URL + productObject.getString("image"),
                            productObject.getString("status"),
                            productObject.getDouble("price"),
                            productObject.getDouble("price_discount"),
                            productObject.getInt("stock"),
                            productObject.getInt("id")
                        )
                        products.add(product)
                    }
                    productAdapter.notifyDataSetChanged()
                }
            } catch (e: JSONException) {
                e.printStackTrace()
            }
        }, { error ->
            error.printStackTrace()
        })

        queue.add(request)
    }
}