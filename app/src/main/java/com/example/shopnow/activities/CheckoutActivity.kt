package com.example.shopnow.activities


import android.app.AlertDialog
import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.volley.AuthFailureError
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.example.shopnow.adapters.CartAdapter
import com.example.shopnow.databinding.ActivityCheckoutBinding
import com.example.shopnow.model.Product
import com.example.shopnow.utils.Constants
import com.hishd.tinycart.model.Cart
import com.hishd.tinycart.util.TinyCartHelper
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import java.util.Calendar


class CheckoutActivity : AppCompatActivity() {


    private lateinit var binding: ActivityCheckoutBinding
    private lateinit var adapter: CartAdapter
    private lateinit var products: ArrayList<Product>
    private var totalPrice: Double = 0.0
    private val tax: Int = 11
    private lateinit var progressDialog: ProgressDialog
    private lateinit var cart: Cart


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCheckoutBinding.inflate(layoutInflater)
        setContentView(binding.root)


        progressDialog = ProgressDialog(this)
        progressDialog.setCancelable(false)
        progressDialog.setMessage("Processing...")


        products = ArrayList()


        cart = TinyCartHelper.getCart()


        for ((item, quantity) in cart.getAllItemsWithQty()) {
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


        totalPrice = (cart.getTotalPrice().toDouble() * tax / 100) + cart.getTotalPrice().toDouble()
        binding.total.text = "PKR $totalPrice"


        binding.checkoutBtn.setOnClickListener {
            processOrder()
        }


        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }


    private fun processOrder() {
        progressDialog.show()
        val queue = Volley.newRequestQueue(this)


        val productOrder = JSONObject()
        val dataObject = JSONObject()
        try {
            productOrder.put("address", binding.addressBox.text.toString())
            productOrder.put("buyer", binding.nameBox.text.toString())
            productOrder.put("comment", binding.commentBox.text.toString())
            productOrder.put("created_at", Calendar.getInstance().timeInMillis)
            productOrder.put("last_update", Calendar.getInstance().timeInMillis)
            productOrder.put("date_ship", Calendar.getInstance().timeInMillis)
            productOrder.put("email", binding.emailBox.text.toString())
            productOrder.put("phone", binding.phoneBox.text.toString())
            productOrder.put("serial", "cab8c1a4e4421a3b")
            productOrder.put("shipping", "")
            productOrder.put("shipping_location", "")
            productOrder.put("shipping_rate", "0.0")
            productOrder.put("status", "WAITING")
            productOrder.put("tax", tax)
            productOrder.put("total_fees", totalPrice)


            val productOrderDetail = JSONArray()
            for ((item, quantity) in cart.getAllItemsWithQty()) {
                val product = item as Product
                product.quantity = quantity
                val productObj = JSONObject()
                productObj.put("amount", quantity)
                productObj.put("price_item", product.price)
                productObj.put("product_id", product.id)
                productObj.put("product_name", product.name)
                productOrderDetail.put(productObj)
            }


            dataObject.put("product_order", productOrder)
            dataObject.put("product_order_detail", productOrderDetail)


            Log.e("err", dataObject.toString())
        } catch (e: JSONException) {
            e.message?.let { Log.e("JSONException", it) }
            progressDialog.dismiss()
            Toast.makeText(this, "Error creating JSON object", Toast.LENGTH_SHORT).show()
            return
        }


        val request = object : JsonObjectRequest(Request.Method.POST, Constants.POST_ORDER_URL, dataObject,
            Response.Listener { response ->
                try {
                    if (response.getString("status").equals("success")) {
                        Toast.makeText(this@CheckoutActivity, "Success order.", Toast.LENGTH_SHORT).show()
                        val orderNumber = response.getJSONObject("data").getString("code")
                        AlertDialog.Builder(this@CheckoutActivity)
                            .setTitle("Order Successful")
                            .setMessage("Your order number is: $orderNumber")
                            .setCancelable(false)
                            .setPositiveButton("Pay Now") { _, _ ->
                                val intent = Intent(this@CheckoutActivity, PaymentActivity::class.java)
                                if (orderNumber != null) {
                                    intent.putExtra("orderCode", orderNumber)
                                }
                                startActivity(intent)
                            }
                            .show()
                    } else {
                        AlertDialog.Builder(this@CheckoutActivity)
                            .setTitle("Order Failed")
                            .setMessage("Something went wrong, please try again.")
                            .setCancelable(false)
                            .setPositiveButton("Close") { _, _ -> }
                            .show()
                        Toast.makeText(this@CheckoutActivity, "Failed order.", Toast.LENGTH_SHORT).show()
                    }
                    progressDialog.dismiss()
                    Log.e("res", response.toString())
                } catch (e: Exception) {
                    e.message?.let { Log.e("Exception", it) }
                    progressDialog.dismiss()
                    Toast.makeText(this@CheckoutActivity, "Error parsing response", Toast.LENGTH_SHORT).show()
                }
            },
            Response.ErrorListener { error ->
                error.message?.let { Log.e("VolleyError", it) }
                progressDialog.dismiss()
                Toast.makeText(this@CheckoutActivity, "Error making request", Toast.LENGTH_SHORT).show()
            }) {
            @Throws(AuthFailureError::class)
            override fun getHeaders(): MutableMap<String, String> {
                val headers: MutableMap<String, String> = HashMap()
                headers["Security"] = "secure_code"
                return headers
            }
        }


        queue.add(request)
    }






    override fun onSupportNavigateUp(): Boolean {
        finish()
        return super.onSupportNavigateUp()
    }
}



