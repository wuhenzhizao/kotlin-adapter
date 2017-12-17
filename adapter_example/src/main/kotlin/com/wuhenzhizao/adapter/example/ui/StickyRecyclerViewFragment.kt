package com.wuhenzhizao.adapter.example.ui

import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.widget.TextView
import android.widget.Toast
import com.google.gson.Gson
import com.wuhenzhizao.adapter.clickInterceptor
import com.wuhenzhizao.adapter.example.R
import com.wuhenzhizao.adapter.example.bean.Country
import com.wuhenzhizao.adapter.example.bean.CountryList
import com.wuhenzhizao.adapter.example.databinding.FragmentStickyRecyclerViewBinding
import com.wuhenzhizao.adapter.example.decoration.LinearDividerItemDecoration
import com.wuhenzhizao.adapter.extension.putItems
import com.wuhenzhizao.adapter.extension.stickyHeader.*
import com.wuhenzhizao.adapter.holderBindInterceptor
import com.wuhenzhizao.adapter.holderCreateInterceptor
import com.wuhenzhizao.adapter.match
import java.util.*


/**
 * Created by liufei on 2017/12/13.
 */
class StickyRecyclerViewFragment : BaseFragment<FragmentStickyRecyclerViewBinding>() {
    private lateinit var adapter: StickyRecyclerViewAdapter<Country>
    private lateinit var countryList: List<Country>

    override fun getContentViewId(): Int = R.layout.fragment_sticky_recycler_view

    override fun initViews() {
        initData()
        bindAdapter()
        bindListener()
    }

    private fun initData() {
        val json = getString(R.string.countries)
        countryList = Gson().fromJson<CountryList>(json, CountryList::class.java).countries
        countryList.forEach {
            val letter = it.countryPinyin[0].toUpperCase()
            it.stickyId = letter.toLong()
            it.letter = letter.toString()
        }
        Collections.sort(countryList, { o1, o2 ->
            when {
                o1.stickyId < o2.stickyId -> -1
                o1.stickyId > o2.stickyId -> 1
                else -> 0
            }
        })
    }

    private fun bindListener() {
        binding.slideBar.setOnSelectIndexItemListener {
            for (i in 0 until countryList.size) {
                if (countryList[i].letter == it) {
                    (binding.rv.layoutManager as LinearLayoutManager).scrollToPositionWithOffset(i, 0)
                    return@setOnSelectIndexItemListener
                }
            }
        }
    }

    private fun bindAdapter() {
        adapter = StickyRecyclerViewAdapter<Country>(context)
                .match(Country::class, R.layout.item_sticky_recycler_view)
                .matchHeader(Country::class, R.layout.item_sticky_recycler_view_header)
                .holderCreateInterceptor {

                }
                .holderBindInterceptor { position, item, viewHolder ->
                    viewHolder.get<TextView>(R.id.country_name).text = item.countryName
                }
                .headerClickInterceptor { position, stickyId ->
                    Toast.makeText(context, "sticky header clicked, headerId = $stickyId", Toast.LENGTH_SHORT).show()
                }
                .headerHolderBindInterceptor { position, item, viewHolder ->
                    viewHolder.get<TextView>(R.id.sticky_name).text = item.letter
                }
                .clickInterceptor { position, item, vh ->
                    Toast.makeText(context, "position $position, ${item.countryName} clicked", Toast.LENGTH_SHORT).show()
                }
        binding.rv.adapter = adapter

        binding.rv.layoutManager = LinearLayoutManager(context)
        // TODO 泛型问题
        val stickyDecoration = StickyRecyclerItemDecoration(adapter)
        binding.rv.addItemDecoration(stickyDecoration)
        val helper = StickyHeaderTouchHelper(binding.rv, stickyDecoration)
        binding.rv.addOnItemTouchListener(helper)
        val dividerDecoration = LinearDividerItemDecoration(context, LinearDividerItemDecoration.LINEAR_DIVIDER_VERTICAL)
        dividerDecoration.setDivider(resources.getDrawable(R.drawable.horizontal_divider_1px))
        binding.rv.addItemDecoration(dividerDecoration)

        adapter.registerAdapterDataObserver(object : RecyclerView.AdapterDataObserver() {
            override fun onChanged() {
                stickyDecoration.clearHeaderCache()
            }
        })

        adapter.putItems(countryList)
    }
}