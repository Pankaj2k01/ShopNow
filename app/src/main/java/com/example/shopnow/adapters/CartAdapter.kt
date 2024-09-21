package com.example.shopnow.adapters

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.drawable.ColorDrawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.NonNull
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.shopnow.R
import com.example.shopnow.databinding.ItemCartBinding
import com.example.shopnow.databinding.QuantityDialogBinding
import com.example.shopnow.model.Product
import com.hishd.tinycart.model.Cart
import com.hishd.tinycart.util.TinyCartHelper

class CartAdapter(private val context: Context, private val products: ArrayList<Product>, private val cartListener: CartListener) : RecyclerView.Adapter<CartAdapter.CartViewHolder>() {

    interface CartListener {
        fun onQuantityChanged()
    }

    private val cart: Cart = TinyCartHelper.getCart()

    @NonNull
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CartViewHolder {
        return CartViewHolder(LayoutInflater.from(context).inflate(R.layout.item_cart, parent, false))
    }

    @SuppressLint("ResourceAsColor")
    override fun onBindViewHolder(holder: CartViewHolder, position: Int) {
        val product = products[position]
        Glide.with(context)
            .load(product.image)
            .into(holder.binding.image)

        holder.binding.name.text = product.name
        holder.binding.price.text = "PKR ${product.price}"
        holder.binding.quantity.text = "${product.quantity} item(s)"

        holder.itemView.setOnClickListener {
            val quantityDialogBinding = QuantityDialogBinding.inflate(LayoutInflater.from(context))

            val dialog = AlertDialog.Builder(context)
                .setView(quantityDialogBinding.root)
                .create()

            dialog.window?.setBackgroundDrawable(ColorDrawable(R.color.transparent))

            quantityDialogBinding.productName.text = product.name
            quantityDialogBinding.productStock.text = "Stock: ${product.stock}"
            quantityDialogBinding.quantity.text = product.quantity.toString()
            val stock = product.stock

            quantityDialogBinding.plusBtn.setOnClickListener {
                var quantity = product.quantity
                quantity++

                if (quantity > product.stock) {
                    Toast.makeText(context, "Max stock available: $stock", Toast.LENGTH_SHORT).show()
                    return@setOnClickListener
                } else {
                    product.quantity = quantity
                    quantityDialogBinding.quantity.text = quantity.toString()
                }

                notifyDataSetChanged()
                cart.updateItem(product, product.quantity)
                cartListener.onQuantityChanged()
            }

            quantityDialogBinding.minusBtn.setOnClickListener {
                var quantity = product.quantity
                if (quantity > 1) quantity--
                product.quantity = quantity
                quantityDialogBinding.quantity.text = quantity.toString()

                notifyDataSetChanged()
                cart.updateItem(product, product.quantity)
                cartListener.onQuantityChanged()
            }

            quantityDialogBinding.saveBtn.setOnClickListener {
                dialog.dismiss()
//                notifyDataSetChanged()
//                cart.updateItem(product, product.quantity)
//                cartListener.onQuantityChanged()
            }

            dialog.show()
        }
    }

    override fun getItemCount(): Int {
        return products.size
    }

    inner class CartViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val binding: ItemCartBinding = ItemCartBinding.bind(itemView)
    }
}


