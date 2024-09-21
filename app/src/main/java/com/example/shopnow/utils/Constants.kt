package com.example.shopnow.utils

object Constants {
    const val API_BASE_URL = "https://tutorials.mianasad.com/ecommerce"
    const val GET_CATEGORIES_URL = "$API_BASE_URL/services/listCategory"
    const val GET_PRODUCTS_URL = "$API_BASE_URL/services/listProduct"
    const val GET_OFFERS_URL = "$API_BASE_URL/services/listFeaturedNews"
    const val GET_PRODUCT_DETAILS_URL = "$API_BASE_URL/services/getProductDetails?id="
    const val POST_ORDER_URL = "$API_BASE_URL/services/submitProductOrder"
    const val PAYMENT_URL = "$API_BASE_URL/services/paymentPage?code="

    const val NEWS_IMAGE_URL = "$API_BASE_URL/uploads/news/"
    const val CATEGORIES_IMAGE_URL = "$API_BASE_URL/uploads/category/"
    const val PRODUCTS_IMAGE_URL = "$API_BASE_URL/uploads/product/"
}


