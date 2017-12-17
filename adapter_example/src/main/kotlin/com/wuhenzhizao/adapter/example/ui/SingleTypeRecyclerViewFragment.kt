package com.wuhenzhizao.adapter.example.ui

import android.support.v7.widget.LinearLayoutManager
import android.view.View
import android.widget.TextView
import android.widget.Toast
import com.gome.common.image.DraweeImageView
import com.gome.common.image.GImageLoader
import com.google.gson.Gson
import com.scwang.smartrefresh.layout.api.RefreshLayout
import com.scwang.smartrefresh.layout.listener.OnRefreshLoadmoreListener
import com.wuhenzhizao.adapter.*
import com.wuhenzhizao.adapter.example.R
import com.wuhenzhizao.adapter.example.bean.Product
import com.wuhenzhizao.adapter.example.bean.ProductList
import com.wuhenzhizao.adapter.example.databinding.FragmentSingleTypeRecyclerViewBinding
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
                binding.refresh.postDelayed({
                    currentPage = 1
                    adapter.putItems(productList.subList(0, PAGE_SIZE))
                    refreshlayout.finishRefresh()
                    refreshlayout.isEnableLoadmore = true
                    refreshlayout.resetNoMoreData()
                }, 1500)
            }

            override fun onLoadmore(refreshlayout: RefreshLayout) {
                binding.refresh.postDelayed({
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
                }, 1500)
            }
        })
    }

    private fun bindAdapter() {
        adapter = RecyclerViewAdapter<Product>(context)
                .match(Product::class, R.layout.item_single_type_recycler_view)
                .holderCreateInterceptor {

                }
                .holderBindInterceptor { position, item, viewHolder ->
                    val imageView = viewHolder.get<DraweeImageView>(R.id.image)
                    GImageLoader.displayUrl(context, imageView, item.imageUrl)

                    viewHolder.get<TextView>(R.id.name).text = item.name
                    viewHolder.get<TextView>(R.id.price).text = "¥ ${item.price}"
                    viewHolder.get<TextView>(R.id.reviews).text = item.reviews

                    viewHolder.get<View>(R.id.divider).visibility = if (position == adapter.itemCount - 1) {
                        View.GONE
                    } else {
                        View.VISIBLE
                    }
                }
                .clickInterceptor { position, item, vh ->
                    Toast.makeText(context, "position $position, ${item.name} clicked", Toast.LENGTH_SHORT).show()
                }
        binding.rv.adapter = adapter
    }
}