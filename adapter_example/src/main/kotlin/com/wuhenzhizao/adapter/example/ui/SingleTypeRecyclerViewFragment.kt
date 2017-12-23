package com.wuhenzhizao.adapter.example.ui

import android.support.v7.widget.LinearLayoutManager
import android.view.View
import android.widget.TextView
import com.google.gson.Gson
import com.scwang.smartrefresh.layout.api.RefreshLayout
import com.scwang.smartrefresh.layout.listener.OnRefreshLoadmoreListener
import com.wuhenzhizao.adapter.*
import com.wuhenzhizao.adapter.example.R
import com.wuhenzhizao.adapter.example.bean.Product
import com.wuhenzhizao.adapter.example.bean.ProductList
import com.wuhenzhizao.adapter.example.databinding.FragmentSingleTypeRecyclerViewBinding
import com.wuhenzhizao.adapter.example.image.DraweeImageView
import com.wuhenzhizao.adapter.example.image.GImageLoader
import com.wuhenzhizao.adapter.extension.addItems
import com.wuhenzhizao.adapter.extension.putItems

/**
 * Created by liufei on 2017/12/13.
 */
class SingleTypeRecyclerViewFragment : BaseFragment<FragmentSingleTypeRecyclerViewBinding>() {
    private lateinit var adapter: RecyclerViewAdapter<Product>
    private lateinit var productList: List<Product>
    private val PAGE_SIZE = 10
    private var currentPage = 1

    override fun getContentViewId(): Int = R.layout.fragment_single_type_recycler_view

    override fun initViews() {
        val json = getString(R.string.products)
        productList = Gson().fromJson<ProductList>(json, ProductList::class.java).products

        binding.rv.layoutManager = LinearLayoutManager(context)

        bindListener()
        bindAdapter()

        binding.refresh.autoRefresh(300)
    }

    private fun bindListener() {
        binding.refresh.setOnRefreshLoadmoreListener(object : OnRefreshLoadmoreListener {
            override fun onRefresh(refreshlayout: RefreshLayout) {
                simulateLoadData {
                    currentPage = 1
                    adapter.putItems(productList.subList(0, PAGE_SIZE))
                    refreshlayout.finishRefresh()
                    refreshlayout.isEnableLoadmore = true
                    refreshlayout.resetNoMoreData()
                }
            }

            override fun onLoadmore(refreshlayout: RefreshLayout) {
                simulateLoadData {
                    val pageStartIndex = currentPage * PAGE_SIZE
                    val pageEndIndex = Math.min((currentPage + 1) * PAGE_SIZE, productList.size)
                    adapter.addItems(productList.subList(pageStartIndex, pageEndIndex))
                    if (pageEndIndex < productList.size) {
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
        adapter = RecyclerViewAdapter<Product>(context)
                .match(Product::class, R.layout.item_single_type_recycler_view)
                .holderCreateListener {

                }
                .holderBindListener { holder, position ->
                    val product = adapter.getItem(position)
                    holder.withView<DraweeImageView>(R.id.image, { GImageLoader.displayUrl(context, this, product.imageUrl) })
                            .withView<TextView>(R.id.name, { text = product.name })
                            .withView<TextView>(R.id.price, { text = "¥ ${product.price}" })
                            .withView<TextView>(R.id.reviews, { text = product.reviews })
                            .withView<View>(R.id.divider, {
                                visibility = if (position == adapter.itemCount - 1) {
                                    View.GONE
                                } else {
                                    View.VISIBLE
                                }
                            })
                }
                .clickListener { holder, position ->
                    val product = adapter.getItem(position)
                    showToast("position $position, ${product.name} clicked")
                }
                .attach(binding.rv)
    }
}