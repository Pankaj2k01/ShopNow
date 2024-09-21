package com.example.shopnow.adapters

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.NonNull
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.shopnow.R
import com.example.shopnow.activities.ProductDetailActivity
import com.example.shopnow.databinding.ItemProductBinding
import com.example.shopnow.model.Product

class ProductAdapter(private val context: Context, private val products: ArrayList<Product>) : RecyclerView.Adapter<ProductAdapter.ProductViewHolder>() {

    @NonNull
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
        return ProductViewHolder(LayoutInflater.from(context).inflate(R.layout.item_product, parent, false))
    }

    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
        val product = products[position]
        Glide.with(context)
            .load(product.image)
            .into(holder.binding.image)
        holder.binding.label.text = product.name
        holder.binding.price.text = "PKR ${product.price}"

        holder.itemView.setOnClickListener {
            val intent = Intent(context, ProductDetailActivity::class.java)
            intent.putExtra("name", product.name)
            intent.putExtra("image", product.image)
            intent.putExtra("id", product.id)
            intent.putExtra("price", product.price)
            context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int {
        return products.size
    }

    inner class ProductViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val binding: ItemProductBinding = ItemProductBinding.bind(itemView)
    }
}


