package com.wuhenzhizao.adapter.example.bean

import com.wuhenzhizao.adapter.extension.stickyHeader.StickyBean

/**
 * Created by liufei on 2017/12/19.
 */
class ShopViewBean(
        var shopId: Int = 0,
        var shopName: String = "",
        stickyId: Long = -1L
) : StickyBean(stickyId)

class ItemViewBean(
        var discount: Double = 0.0,
        var stockState: String = "",
        var propertyTags: PropertyTags = PropertyTags(),
        var stockCode: Int = 0,
        var maxNum: Int = 0,
        var id: String = "",
        var name: String = "",
        var num: Int = 0,
        var price: Double = 0.0,
        var priceShow: String = "",
        var imgUrl: String = "",
        var checkType: Int = 0,
        var shop: Shop,
        stickyId: Long = -1L
) : StickyBean(stickyId)