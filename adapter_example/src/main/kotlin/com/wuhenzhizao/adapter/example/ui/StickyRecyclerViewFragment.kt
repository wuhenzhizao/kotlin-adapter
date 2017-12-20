package com.wuhenzhizao.adapter.example.ui

import android.support.v7.widget.LinearLayoutManager
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
        binding.rv.layoutManager = LinearLayoutManager(context)
        val dividerDecoration = LinearDividerItemDecoration(context, LinearDividerItemDecoration.LINEAR_DIVIDER_VERTICAL)
        dividerDecoration.setDivider(resources.getDrawable(R.drawable.horizontal_divider_1px))
        binding.rv.addItemDecoration(dividerDecoration)

        adapter = StickyRecyclerViewAdapter<Country>(context)
                .match(Country::class, R.layout.item_sticky_recycler_view)
                .matchHeader(Country::class, R.layout.item_sticky_recycler_view_header)
                .holderCreateInterceptor {

                }
                .holderBindInterceptor { position, holder ->
                    val country = adapter.getItem(position)
                    holder.get<TextView>(R.id.country_name, {
                        text = country.countryName
                    })

                }
                .headerHolderBindInterceptor { position, holder ->
                    val country = adapter.getItem(position)
                    holder.get<TextView>(R.id.sticky_name, {
                        text = country.letter
                    })
                }
                .headerClickInterceptor { holder, clickView, position ->
                    showToast("sticky header clicked, headerId = ${adapter.getHeaderId(position)}")
                }
                .clickInterceptor { position, holder ->
                    val country = adapter.getItem(position)
                    showToast("position $position, ${country.countryName} clicked")
                }
                .attach(binding.rv)
        adapter.putItems(countryList)
    }
}