package com.example.shopnow.model

import com.hishd.tinycart.model.Item
import java.io.Serializable
import java.math.BigDecimal

data class Product(
    var name: String,
    var image: String,
    var status: String,
    var price: Double,
    var discount: Double,
    var stock: Int,
    var id: Int,
    var quantity: Int = 0
) : Item, Serializable {

    override fun getItemPrice(): BigDecimal {
        return BigDecimal(price)
    }

    override fun getItemName(): String {
        return name
    }

}





