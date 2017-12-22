package com.wuhenzhizao.adapter.example.ui

import android.support.v7.widget.LinearLayoutManager
import com.google.gson.Gson
import com.scwang.smartrefresh.layout.api.RefreshLayout
import com.scwang.smartrefresh.layout.listener.OnRefreshLoadmoreListener
import com.wuhenzhizao.adapter.*
import com.wuhenzhizao.adapter.example.R
import com.wuhenzhizao.adapter.example.bean.Content
import com.wuhenzhizao.adapter.example.bean.ContentList
import com.wuhenzhizao.adapter.example.databinding.FragmentSingleTypeRecyclerViewBindingBinding
import com.wuhenzhizao.adapter.example.databinding.ItemSingleTypeRecyclerViewBindingBinding
import com.wuhenzhizao.adapter.example.decoration.LinearOffsetsItemDecoration
import com.wuhenzhizao.adapter.extension.addItems
import com.wuhenzhizao.adapter.extension.putItems
import com.wuhenzhizao.titlebar.utils.ScreenUtils
import kotlinx.coroutines.experimental.launch

/**
 * Created by liufei on 2017/12/13.
 */
class SingleTypeRecyclerViewBindingFragment : BaseFragment<FragmentSingleTypeRecyclerViewBindingBinding>() {
    private lateinit var adapter: RecyclerViewBindingAdapter<Content>
    private lateinit var contentList: List<Content>
    private val PAGE_SIZE = 5
    private var currentPage = 1

    override fun getContentViewId(): Int = R.layout.fragment_single_type_recycler_view_binding

    override fun initViews() {
        val json = getString(R.string.contents)
        contentList = Gson().fromJson<ContentList>(json, ContentList::class.java).contents

        binding.rv.layoutManager = LinearLayoutManager(context)
        val decoration = LinearOffsetsItemDecoration(LinearOffsetsItemDecoration.LINEAR_OFFSETS_VERTICAL)
        decoration.setItemOffsets(ScreenUtils.dp2PxInt(context, 10f))
        decoration.setOffsetEdge(false)
        binding.rv.addItemDecoration(decoration)

        bindListener()
        bindAdapter()

        binding.refresh.autoRefresh(300)
    }

    private fun bindListener() {
        binding.refresh.setOnRefreshLoadmoreListener(object : OnRefreshLoadmoreListener {
            override fun onRefresh(refreshlayout: RefreshLayout) {
                simulateLoadData {
                    currentPage = 1
                    adapter.putItems(contentList.subList(0, PAGE_SIZE))
                    refreshlayout.finishRefresh()
                    refreshlayout.isEnableLoadmore = true
                    refreshlayout.resetNoMoreData()
                }
            }

            override fun onLoadmore(refreshlayout: RefreshLayout) {
                simulateLoadData {
                    val pageStartIndex = currentPage * PAGE_SIZE
                    val pageEndIndex = Math.min((currentPage + 1) * PAGE_SIZE, contentList.size)
                    adapter.addItems(contentList.subList(pageStartIndex, pageEndIndex))
                    if (pageEndIndex < contentList.size) {
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

    private val cardWidth: Float by lazy {
        ScreenUtils.getScreenWidth(context) - ScreenUtils.dp2Px(context, 30f)
    }

    private fun bindAdapter() {
        adapter = RecyclerViewBindingAdapter<Content>(context)
                .match(Content::class, R.layout.item_single_type_recycler_view_binding, BR.vm)
                .holderCreateInterceptor {
                    // 演示在创建时，动态修改布局高度
                    val layoutParams = it.convert<ItemSingleTypeRecyclerViewBindingBinding>().imageUrl.layoutParams
                    layoutParams.height = (cardWidth * 2 / 3).toInt()
                }
                .clickInterceptor { position, holder ->
                    val content = adapter.getItem(position)
                    showToast("position $position, ${content.title}")
                }
                .attach(binding.rv)
    }
}