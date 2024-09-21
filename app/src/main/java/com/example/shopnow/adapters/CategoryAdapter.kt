package com.example.shopnow.adapters

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.text.Html
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import androidx.annotation.NonNull
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.shopnow.R
import com.example.shopnow.activities.CategoryActivity
import com.example.shopnow.databinding.ItemCategoriesBinding
import com.example.shopnow.model.Category

class CategoryAdapter(private val context: Context, private val categories: ArrayList<Category>) : RecyclerView.Adapter<CategoryAdapter.CategoryViewHolder>() {

    @NonNull
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryViewHolder {
        return CategoryViewHolder(LayoutInflater.from(context).inflate(R.layout.item_categories, parent, false))
    }

    override fun onBindViewHolder(holder: CategoryViewHolder, position: Int) {
        val category = categories[position]
        holder.binding.label.text = Html.fromHtml(category.name)
        Glide.with(context)
            .load(category.icon)
            .into(holder.binding.image)

        holder.binding.image.setBackgroundColor(Color.parseColor(category.color))

        holder.itemView.setOnClickListener {
            val intent = Intent(context, CategoryActivity::class.java)
            intent.putExtra("catId", category.id)
            intent.putExtra("categoryName", category.name)
            context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int {
        return categories.size
    }

    inner class CategoryViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val binding: ItemCategoriesBinding = ItemCategoriesBinding.bind(itemView)
    }
}


