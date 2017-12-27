package com.wuhenzhizao.adapter.example.ui

import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.helper.ItemTouchHelper
import android.widget.TextView
import co.metalab.asyncawait.async
import com.google.gson.Gson
import com.wuhenzhizao.adapter.*
import com.wuhenzhizao.adapter.example.R
import com.wuhenzhizao.adapter.example.bean.Topic
import com.wuhenzhizao.adapter.example.bean.TopicList
import com.wuhenzhizao.adapter.example.databinding.FragmentDragRecyclerViewBinding
import com.wuhenzhizao.adapter.example.image.GImageLoader
import com.wuhenzhizao.adapter.example.widget.RatioImageView
import com.wuhenzhizao.adapter.extension.dragSwipeDismiss.DragAndSwipeRecyclerViewAdapter
import com.wuhenzhizao.adapter.extension.dragSwipeDismiss.dragListener
import com.wuhenzhizao.adapter.extension.putItems

/**
 * Created by liufei on 2017/12/13.
 */
class DragRecyclerViewFragment : BaseFragment<FragmentDragRecyclerViewBinding>() {
    private lateinit var adapter: DragAndSwipeRecyclerViewAdapter<Topic>
    private lateinit var topicList: List<Topic>

    override fun getContentViewId(): Int = R.layout.fragment_drag_recycler_view

    override fun initViews() {
        binding.rv.isLongPressDragEnable = true
        binding.rv.isItemViewSwipeEnable = false
        binding.rv.dragDirection = ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT or ItemTouchHelper.UP or ItemTouchHelper.DOWN
        binding.rv.layoutManager = GridLayoutManager(context, 3, GridLayoutManager.VERTICAL, false)

        async {
            await {
                val json = getString(R.string.topicList)
                topicList = Gson().fromJson<TopicList>(json, TopicList::class.java).topics
            }
            bindAdapter()
        }
    }

    private fun bindAdapter() {
        adapter = DragAndSwipeRecyclerViewAdapter<Topic>(context)
                .match(Topic::class, R.layout.item_drag_recycler_view)
                .holderCreateListener {

                }
                .holderBindListener { holder, position ->
                    val topic = adapter.getItem(position)
                    holder.withView<RatioImageView>(R.id.iv, { GImageLoader.displayUrl(context, this, topic.smallImg) })
                            .withView<TextView>(R.id.name, { text = topic.title })
                }
                .clickListener { holder, position ->
                    val topic = adapter.getItem(position)
                    showToast("position $position, ${topic.title} clicked")
                }
                .dragListener { from, target ->
                    showToast("item is dragged, from $from to $target")
                }
                .attach(binding.rv)
        adapter.putItems(topicList)
    }
}