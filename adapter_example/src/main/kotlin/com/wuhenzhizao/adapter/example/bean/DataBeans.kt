package com.wuhenzhizao.adapter.example.bean

import android.databinding.BaseObservable
import com.google.gson.annotations.SerializedName
import com.wuhenzhizao.adapter.extension.stickyHeader.StickyBean

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
) : StickyBean()

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


data class BannerList(
        @SerializedName("banners") val banners: List<Banner> = listOf()
)

data class Banner(
        @SerializedName("id") val id: String = "",
        @SerializedName("title") val title: String = "",
        @SerializedName("imageUrl") val imageUrl: String = "",
        @SerializedName("targetUrl") val targetUrl: String = ""
)

class Promotion

class Divider : StickyBean()

class HeaderLine

data class HeaderLineProductList(val products: List<Product> = listOf())

class Recommend : StickyBean()

data class RecommendProducts(val leftProduct: Product, val rightProduct: Product?) : StickyBean()


data class CountryList(
        @SerializedName("countries") val countries: List<Country> = listOf()
)

data class Country(
        @SerializedName("countryName") val countryName: String = "",
        @SerializedName("countryPinyin") val countryPinyin: String = "",
        @SerializedName("phoneCode") val phoneCode: String = "",
        @SerializedName("countryCode") val countryCode: String = "",
        @SerializedName("letter") var letter: String = ""
) : StickyBean()


data class TopicList(
        @SerializedName("topics") val topics: List<Topic> = listOf()
)

data class Topic(
        @SerializedName("url") val url: String = "",
        @SerializedName("bigImg") val bigImg: String = "",
        @SerializedName("smallImg") val smallImg: String = "",
        @SerializedName("title") val title: String = "" //潮流数码
)


data class ShoppingCartList(
        @SerializedName("carts") val carts: List<Shop> = listOf(),
        @SerializedName("notice") val notice: Notice = Notice(),
        @SerializedName("recommendProductList") val recommendProductList: List<Product> = listOf()
)

data class Shop(
        @SerializedName("shopId") val shopId: Int = 0, //-1
        @SerializedName("shopName") val shopName: String = "",
        @SerializedName("items") val items: List<Item> = listOf()
) : StickyBean()

data class Item(
        @SerializedName("Discount") val discount: Double = 0.0,
        @SerializedName("stockState") val stockState: String = "",
        @SerializedName("propertyTags") val propertyTags: PropertyTags = PropertyTags(),
        @SerializedName("stockCode") val stockCode: Int = 0,
        @SerializedName("maxNum") val maxNum: Int = 0,
        @SerializedName("Id") val id: String = "",
        @SerializedName("Name") val name: String = "",
        @SerializedName("Num") val num: Int = 0,
        @SerializedName("Price") val price: Double = 0.0,
        @SerializedName("PriceShow") val priceShow: String = "",
        @SerializedName("ImgUrl") val imgUrl: String = "",
        @SerializedName("CheckType") val checkType: Int = 0,

        // 拓展屬性，方便本地逻辑操作
        var shop: Shop
) : StickyBean()

data class PropertyTags(
        @SerializedName("b") val b: String = "",
        @SerializedName("a") val a: String = ""
)

data class Notice(
        @SerializedName("imgUrl") val imgUrl: String = "",
        @SerializedName("text") val text: String = ""
) : StickyBean()
