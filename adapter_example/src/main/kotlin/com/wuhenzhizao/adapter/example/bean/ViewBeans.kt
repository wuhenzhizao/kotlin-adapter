package com.wuhenzhizao.adapter.example.bean

import android.databinding.BaseObservable
import com.google.gson.annotations.SerializedName

/**
 * Created by liufei on 2017/12/7.
 */
data class ProvinceList(
        @SerializedName("provinceList") val provinceList: List<Province> = listOf()
)

data class Province(
        @SerializedName("Id") val id: Int = 0,
        @SerializedName("Name") val name: String = "",
        var checked: Boolean = false
)


data class ProductList(
        @SerializedName("products") val products: List<Product> = listOf()
)

data class Product(
        @SerializedName("id") val id: String = "",
        @SerializedName("name") val name: String = "",
        @SerializedName("imageUrl") val imageUrl: String = "",
        @SerializedName("price") val price: String = "",
        @SerializedName("reviews") val reviews: String = ""
)


data class ContentList(
        @SerializedName("contents") val contents: List<Content> = listOf()
)

data class Content(
        @SerializedName("authorId") val authorId: String = "",
        @SerializedName("authorName") val authorName: String = "",
        @SerializedName("authorPic") val authorPic: String = "",
        @SerializedName("commentNum") val commentNum: Int = 0,
        @SerializedName("hasLiked") val hasLiked: Int = 0,
        @SerializedName("id") val id: String = "",
        @SerializedName("indexImage") val indexImage: String = "",
        @SerializedName("summary") val summary: String = "",
        @SerializedName("title") val title: String = "",
        @SerializedName("pageViewStr") val pageViewStr: String = ""
) : BaseObservable()