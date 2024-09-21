package com.example.shopnow.activities

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.shopnow.adapters.CategoryAdapter
import com.example.shopnow.adapters.ProductAdapter
import com.example.shopnow.databinding.ActivityMainBinding
import com.example.shopnow.model.Category
import com.example.shopnow.model.Product
import com.example.shopnow.utils.Constants
import com.mancj.materialsearchbar.MaterialSearchBar
import org.imaginativeworld.whynotimagecarousel.model.CarouselItem
import org.json.JSONException
import org.json.JSONObject


class MainActivity : AppCompatActivity() {


    private lateinit var binding: ActivityMainBinding
    private lateinit var categoryAdapter: CategoryAdapter
    private lateinit var categories: ArrayList<Category>


    private lateinit var productAdapter: ProductAdapter
    private lateinit var products: ArrayList<Product>


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)


        binding.searchBar.setOnSearchActionListener(object : MaterialSearchBar.OnSearchActionListener {
            override fun onSearchStateChanged(enabled: Boolean) {}


            override fun onSearchConfirmed(text: CharSequence) {
                val intent = Intent(this@MainActivity, SearchActivity::class.java)
                intent.putExtra("query", text.toString())
                startActivity(intent)
            }


            override fun onButtonClicked(buttonCode: Int) {}
        })


        initCategories()
        initProducts()
        initSlider()
    }


    private fun initSlider() {
        getRecentOffers()
    }


    private fun initCategories() {
        categories = ArrayList()
        categoryAdapter = CategoryAdapter(this, categories)


        getCategories()


        val layoutManager = GridLayoutManager(this, 4)
        binding.categoriesList.layoutManager = layoutManager
        binding.categoriesList.adapter = categoryAdapter
    }


    private fun getCategories() {
        val queue = Volley.newRequestQueue(this)


        val url = Constants.GET_CATEGORIES_URL
        val request = StringRequest(Request.Method.GET, url, Response.Listener { response ->
            try {
                val jsonObject = JSONObject(response)
                if (jsonObject.has("status") && jsonObject.getString("status") == "success") {
                    val categoriesArray = jsonObject.getJSONArray("categories")
                    for (i in 0 until categoriesArray.length()) {
                        val childObj = categoriesArray.getJSONObject(i)


                        // Make sure you have these fields in your API response
                        val categoryName = childObj.getString("name")
                        val categoryImage = Constants.CATEGORIES_IMAGE_URL + childObj.getString("icon")
                        val categoryColor = childObj.getString("color")
                        val categoryBrief = childObj.getString("brief")
                        val categoryId = childObj.getInt("id")


                        val category = Category(
                            categoryName,
                            categoryImage,
                            categoryColor,
                            categoryBrief,
                            categoryId
                        )
                        categories.add(category)
                    }
                    categoryAdapter.notifyDataSetChanged()
                }
            } catch (e: JSONException) {
                e.printStackTrace()
            }
        }, Response.ErrorListener { })


        queue.add(request)
    }



    private fun getRecentProducts() {
        val queue = Volley.newRequestQueue(this)


        val url = Constants.GET_PRODUCTS_URL + "?count=8"
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


    private fun getRecentOffers() {
        val queue = Volley.newRequestQueue(this)


        val url = Constants.GET_OFFERS_URL
        val request = StringRequest(Request.Method.GET, url, Response.Listener { response ->
            try {
                val jsonObject = JSONObject(response)
                if (jsonObject.has("status") && jsonObject.getString("status") == "success") {
                    val offerArray = jsonObject.getJSONArray("news_infos")
                    for (i in 0 until offerArray.length()) {
                        val childObj = offerArray.getJSONObject(i)


                        // Make sure you have these fields in your API response
                        val offerImage = Constants.NEWS_IMAGE_URL + childObj.getString("image")
                        val offerTitle = childObj.getString("title")


                        binding.carousel.addData(
                            CarouselItem(
                                offerImage,
                                offerTitle
                            )
                        )
                    }
                }
            } catch (e: JSONException) {
                e.printStackTrace()
            }
        }, Response.ErrorListener { })


        queue.add(request)
    }





    private fun initProducts() {
        products = ArrayList()
        productAdapter = ProductAdapter(this, products)


        getRecentProducts()


        val layoutManager = GridLayoutManager(this, 2)
        binding.productList.layoutManager = layoutManager
        binding.productList.adapter = productAdapter
    }
}



