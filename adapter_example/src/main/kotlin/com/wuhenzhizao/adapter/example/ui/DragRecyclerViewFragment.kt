package com.wuhenzhizao.adapter.example.ui

import android.graphics.Color
import android.support.v7.widget.GridLayoutManager
import android.widget.TextView
import android.widget.Toast
import com.gome.common.image.GImageLoader
import com.google.gson.Gson
import com.wuhenzhizao.adapter.AbsRecyclerViewAdapter
import com.wuhenzhizao.adapter.example.R
import com.wuhenzhizao.adapter.example.bean.Topic
import com.wuhenzhizao.adapter.example.bean.TopicList
import com.wuhenzhizao.adapter.example.databinding.FragmentDragRecyclerViewBinding
import com.wuhenzhizao.adapter.example.decoration.GridDividerItemDecoration
import com.wuhenzhizao.adapter.example.widget.RatioImageView
import com.wuhenzhizao.adapter.extension.drag_swipe.DragAndSwipeRecyclerViewAdapter
import com.wuhenzhizao.adapter.extension.putItems
import com.wuhenzhizao.titlebar.utils.ScreenUtils

/**
 * Created by liufei on 2017/12/13.
 */
class DragRecyclerViewFragment : BaseFragment<FragmentDragRecyclerViewBinding>() {
    private lateinit var adapter: AbsRecyclerViewAdapter<Topic, *>
    private lateinit var topicList: List<Topic>

    override fun getContentViewId(): Int = R.layout.fragment_drag_recycler_view

    override fun initViews() {
        val json = getString(R.string.topicList)
        topicList = Gson().fromJson<TopicList>(json, TopicList::class.java).topics

        binding.rv.layoutManager = GridLayoutManager(context, 3, GridLayoutManager.VERTICAL, false)
        bindAdapter()
    }

    private fun bindAdapter() {
        adapter = DragAndSwipeRecyclerViewAdapter<Topic>(context)
                .match<Topic>(R.layout.item_drag_recycler_view)
                .viewHolderCreateInterceptor {

                }
                .viewHolderBindInterceptor { position, item, viewHolder ->
                    val imageView = viewHolder.get<RatioImageView>(R.id.iv)
                    GImageLoader.displayUrl(context, imageView, item.smallImg)
                    viewHolder.get<TextView>(R.id.name).text = item.title
                }
                .clickInterceptor { position, item, vh ->
                    Toast.makeText(context, "position $position, ${item.title} clicked", Toast.LENGTH_SHORT).show()
                }
        // TODO 泛型问题
        binding.rv.setAdapter(adapter as DragAndSwipeRecyclerViewAdapter<*>)
        adapter.putItems(topicList)
    }
}