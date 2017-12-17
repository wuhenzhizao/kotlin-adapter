package com.wuhenzhizao.adapter.example.ui

import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.helper.ItemTouchHelper
import android.widget.TextView
import android.widget.Toast
import com.google.gson.Gson
import com.wuhenzhizao.adapter.*
import com.wuhenzhizao.adapter.example.R
import com.wuhenzhizao.adapter.example.bean.Topic
import com.wuhenzhizao.adapter.example.bean.TopicList
import com.wuhenzhizao.adapter.example.databinding.FragmentDragRecyclerViewBinding
import com.wuhenzhizao.adapter.example.decoration.LinearOffsetsItemDecoration
import com.wuhenzhizao.adapter.example.image.DraweeImageView
import com.wuhenzhizao.adapter.example.image.GImageLoader
import com.wuhenzhizao.adapter.extension.dragSwipeDismiss.DragAndSwipeRecyclerViewAdapter
import com.wuhenzhizao.adapter.extension.dragSwipeDismiss.swipeInterceptor
import com.wuhenzhizao.adapter.extension.putItems
import com.wuhenzhizao.titlebar.utils.ScreenUtils

/**
 * Created by liufei on 2017/12/13.
 */
class SwipeDismissRecyclerViewFragment : BaseFragment<FragmentDragRecyclerViewBinding>() {
    private lateinit var adapter: DragAndSwipeRecyclerViewAdapter<Topic>
    private lateinit var topicList: List<Topic>

    override fun getContentViewId(): Int = R.layout.fragment_drag_recycler_view

    override fun initViews() {
        val json = getString(R.string.topicList)
        topicList = Gson().fromJson<TopicList>(json, TopicList::class.java).topics

        binding.rv.isLongPressDragEnable = true
        binding.rv.isItemViewSwipeEnable = true
        binding.rv.dragDirection = ItemTouchHelper.UP or ItemTouchHelper.DOWN
        binding.rv.swipeDirection = ItemTouchHelper.LEFT
        binding.rv.layoutManager = LinearLayoutManager(context)
        val decoration = LinearOffsetsItemDecoration(LinearOffsetsItemDecoration.LINEAR_OFFSETS_VERTICAL)
        decoration.setItemOffsets(ScreenUtils.dp2PxInt(context, 10f))
        decoration.setOffsetEdge(true)
        decoration.setOffsetLast(true)
        binding.rv.addItemDecoration(decoration)
        bindAdapter()


        // 1. drag swipeDismiss 控制开关  回调 closed
        // TODO 1. 回调参数统一
        // TODO 2. swipeMenu页面
        // TODO 3. multipleListView页面
    }

    private fun bindAdapter() {
        adapter = DragAndSwipeRecyclerViewAdapter<Topic>(context)
                .match(Topic::class, R.layout.item_swipe_dismiss_recycler_view)
                .holderCreateInterceptor {

                }
                .holderBindInterceptor { position, item, viewHolder ->
                    val imageView = viewHolder.get<DraweeImageView>(R.id.iv)
                    GImageLoader.displayUrl(context, imageView, item.bigImg)
                    viewHolder.get<TextView>(R.id.name).text = item.title
                }
                .clickInterceptor { position, item, vh ->
                    Toast.makeText(context, "position $position, ${item.title} clicked", Toast.LENGTH_SHORT).show()
                }
                .swipeInterceptor { viewHolder, direction ->
                    Toast.makeText(context, "position ${viewHolder.adapterPosition} dismissed", Toast.LENGTH_SHORT).show()
                }
                .attach(binding.rv)
        adapter.putItems(topicList)
    }
}