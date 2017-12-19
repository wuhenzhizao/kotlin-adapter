package com.wuhenzhizao.adapter.example.ui

import android.support.v7.widget.LinearLayoutManager
import android.view.View
import android.widget.EditText
import android.widget.ImageButton
import android.widget.RelativeLayout
import android.widget.TextView
import com.google.gson.Gson
import com.scwang.smartrefresh.layout.api.RefreshLayout
import com.scwang.smartrefresh.layout.listener.OnRefreshLoadmoreListener
import com.wuhenzhizao.adapter.clickInterceptor
import com.wuhenzhizao.adapter.example.R
import com.wuhenzhizao.adapter.example.bean.*
import com.wuhenzhizao.adapter.example.databinding.FragmentSwipeMenuRecyclerViewBinding
import com.wuhenzhizao.adapter.example.image.DraweeImageView
import com.wuhenzhizao.adapter.example.image.GImageLoader
import com.wuhenzhizao.adapter.extension.addItems
import com.wuhenzhizao.adapter.extension.putItems
import com.wuhenzhizao.adapter.extension.stickyHeader.*
import com.wuhenzhizao.adapter.extension.swipeMenu.SwipeMenuStickyRecyclerViewAdapter
import com.wuhenzhizao.adapter.holder.RecyclerViewHolder
import com.wuhenzhizao.adapter.holderBindInterceptor
import com.wuhenzhizao.adapter.match

/**
 * Created by liufei on 2017/12/18.
 */
class SwipeMenuRecyclerViewFragment : BaseFragment<FragmentSwipeMenuRecyclerViewBinding>() {
    private lateinit var originData: ShoppingCartList
    private lateinit var list: MutableList<StickyBean>
    private lateinit var adapter: SwipeMenuStickyRecyclerViewAdapter<StickyBean>
    private val PAGE_SIZE = 10
    private var currentPage = 1

    private val ID_RECOMMAND = 8888L

    override fun getContentViewId(): Int = R.layout.fragment_swipe_menu_recycler_view

    override fun initViews() {
        val json = getString(R.string.carts)
        originData = Gson().fromJson<ShoppingCartList>(json, ShoppingCartList::class.java)
        list = mutableListOf()

        binding.rv.layoutManager = LinearLayoutManager(context)

        bindListener()
        bindAdapter()
        binding.refresh.autoRefresh(300)
    }

    private fun translateFirstPageData() {
        list.clear()
        list.add(originData.notice)
        // 接口返回的数据中，店铺持有item信息，在列表展示时，转换为item持有店铺信息
        originData.carts.forEach { shop ->
            val shopViewBean = ShopViewBean(
                    shopId = shop.shopId,
                    shopName = shop.shopName,
                    stickyId = shop.shopId.toLong()
            )
            shop.items.map {
                list.add(ItemViewBean(
                        discount = it.discount,
                        stockState = it.stockState,
                        propertyTags = it.propertyTags,
                        stockCode = it.stockCode,
                        maxNum = it.maxNum,
                        id = it.id,
                        name = it.name,
                        num = it.num,
                        price = it.price,
                        priceShow = it.priceShow,
                        imgUrl = it.imgUrl,
                        checkType = it.checkType,
                        shop = shop,
                        stickyId = shopViewBean.stickyId
                ))
            }
            val divider = Divider()
            divider.stickyId = shopViewBean.stickyId
            list.add(divider)
        }
        val recommendProductList = originData.recommendProductList
        val firstPageRecommendList = (0 until PAGE_SIZE).map {
            val products = RecommendProducts(recommendProductList[it], recommendProductList[it + 1])
            products.stickyId = ID_RECOMMAND
            products
        }
        list.addAll(firstPageRecommendList)
        adapter.putItems(list)
    }

    private fun bindListener() {
        binding.refresh.setOnRefreshLoadmoreListener(object : OnRefreshLoadmoreListener {
            override fun onRefresh(refreshlayout: RefreshLayout) {
                simulateLoadData {
                    translateFirstPageData()
                    currentPage = 1
                    refreshlayout.finishRefresh()
                    refreshlayout.isEnableLoadmore = true
                    refreshlayout.resetNoMoreData()
                }

            }

            override fun onLoadmore(refreshlayout: RefreshLayout) {
                simulateLoadData {
                    val list = mutableListOf<RecommendProducts>()
                    val recommendProductList: List<Product> = originData.recommendProductList
                    val pageStartIndex = currentPage * PAGE_SIZE
                    val pageEndIndex = Math.min((currentPage + 1) * PAGE_SIZE, recommendProductList.size)
                    (pageStartIndex until pageEndIndex step 2)
                            .mapTo(list) {
                                val products: RecommendProducts = if (it + 1 < recommendProductList.size) {
                                    RecommendProducts(recommendProductList[it], recommendProductList[it + 1])
                                } else {
                                    RecommendProducts(recommendProductList[it], null)
                                }
                                products.stickyId = ID_RECOMMAND
                                products
                            }
                    adapter.addItems(list)
                    if (pageEndIndex < recommendProductList.size) {
                        refreshlayout.isEnableLoadmore = true
                        refreshlayout.finishLoadmore()
                    } else {
                        refreshlayout.isEnableLoadmore = false
                        refreshlayout.finishLoadmoreWithNoMoreData()
                    }
                    currentPage++
                }
            }
        })
    }

    private fun bindAdapter() {
        adapter = SwipeMenuStickyRecyclerViewAdapter<StickyBean>(context)
                .match(Notice::class, R.layout.item_shopping_cart_notice)
                .match(Divider::class, R.layout.item_shopping_cart_divider)
                .match(ItemViewBean::class, R.layout.item_shopping_cart_sku)
                .match(RecommendProducts::class, R.layout.item_shopping_cart_recommend)
                .matchHeader(ItemViewBean::class, R.layout.item_shopping_cart_shop)
                .matchHeader(RecommendProducts::class, R.layout.item_shopping_cart_recommend_header)
                .holderBindInterceptor { position, vh ->
                    onHolderBind(position, vh)
                }
                .clickInterceptor { position, vh ->

                }
                .headerHolderBindInterceptor { position, vh ->
                    val item = adapter.getItem(position)
                    when (item) {
                        is ItemViewBean -> {
                            vh.get<TextView>(R.id.tv_shop_name).text = item.shop.shopName
                        }
                    }
                }
                .headerClickInterceptor { vh, clickView, position ->
                    showToast("sticky header clicked, headerId = ${adapter.getHeaderId(position)}")
                }
                .attach(binding.rv)
    }

    private fun onHolderBind(position: Int, vh: RecyclerViewHolder) {
        val item = adapter.getItem(position)
        when (item) {
            is Notice -> {
                vh.get<DraweeImageView>(R.id.iv_notice, { GImageLoader.displayUrl(context, this, item.imgUrl) })
                vh.get<TextView>(R.id.tv_notice, { text = item.text })
            }
            is ItemViewBean -> {
                vh.get<DraweeImageView>(R.id.iv_sku_logo, { GImageLoader.displayUrl(context, it, item.imgUrl) })
                vh.get<ImageButton>(R.id.ib_select, { isSelected = item.checkType != 0 })
                vh.get<TextView>(R.id.tv_sku_name, { text = item.name })
                vh.get<TextView>(R.id.tv_sku_attributes, { text = "${item.propertyTags.a}, ${item.propertyTags.b}" })
                vh.get<TextView>(R.id.tv_sku_price, { text = item.priceShow })
                vh.get<EditText>(R.id.et_sku_quantity_input, { setText(item.num.toString()) })
                vh.get<TextView>(R.id.tv_shopping_cart_delete, {
                    setOnClickListener {
                        adapter.closeAllItems()
                    }
                })
            }
            is RecommendProducts -> {
                vh.get<DraweeImageView>(R.id.left_iv, { GImageLoader.displayUrl(context, this, item.leftProduct.imageUrl) })
                vh.get<TextView>(R.id.left_name, { text = item.leftProduct.name })
                vh.get<TextView>(R.id.left_price, { text = "¥ ${item.leftProduct.price}" })
                vh.get<TextView>(R.id.left_reviews, { text = item.leftProduct.reviews })

                if (item.rightProduct != null) {
                    vh.get<DraweeImageView>(R.id.right_iv, { GImageLoader.displayUrl(context, this, item.rightProduct.imageUrl) })
                    vh.get<TextView>(R.id.right_name, { text = item.rightProduct.name })
                    vh.get<TextView>(R.id.right_price, { text = "¥ ${item.rightProduct.price}" })
                    vh.get<RelativeLayout>(R.id.right_view, { visibility = View.VISIBLE })
                } else {
                    vh.get<RelativeLayout>(R.id.right_view, { visibility = View.INVISIBLE })
                }
            }
        }
    }
}