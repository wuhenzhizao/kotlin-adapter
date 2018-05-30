package com.wuhenzhizao.adapter.example.ui

import android.widget.TextView
import co.metalab.asyncawait.async
import com.wuhenzhizao.adapter.*
import com.wuhenzhizao.adapter.example.R
import com.wuhenzhizao.adapter.example.bean.NormalNews
import com.wuhenzhizao.adapter.example.bean.Time
import com.wuhenzhizao.adapter.example.bean.TopNews
import com.wuhenzhizao.adapter.example.databinding.FragmentMultipleTypeListViewBinding
import com.wuhenzhizao.adapter.example.image.DraweeImageView
import com.wuhenzhizao.adapter.example.image.GImageLoader
import java.text.SimpleDateFormat
import java.util.*

/**
 * Created by liufei on 2017/12/13.
 */
class MultipleTypeListViewFragment : BaseFragment<FragmentMultipleTypeListViewBinding>() {
    private lateinit var adapter: ListViewAdapter<Any>
    private val list = mutableListOf<Any>()

    override fun getContentViewId(): Int = R.layout.fragment_multiple_type_list_view

    override fun initViews() {
        async {
            await {
                simulateData()
            }
            bindAdapter()
        }
    }

    private fun simulateData() {
        val calendar = Calendar.getInstance()

        calendar.add(Calendar.MONTH, -1)
        list.add(Time(calendar.timeInMillis))
        list.add(TopNews("https://m.360buyimg.com/mobilecms/jfs/t3259/200/1881262978/59297/8af0faa4/57d6905dN1ffc5c28.jpg!q70.jpg.webp"))
        list.add(NormalNews(
                "每个人应该都有在外住宿的体验，不管是出去旅游还是出差，都会在酒店住宿。不同价位的酒店，虽然服务和硬件设备有区别，但是里面有些东西，再怎么消毒清洁还是不能碰。一个长期在酒店工作的保洁阿姨说，酒店的有些东西千万别碰，出门在外还是自己备着好，下面我们看看有哪些东西不能碰。",
                "https://m.360buyimg.com/mobilecms/jfs/t4579/8/330047376/64569/24fedc0/58ce03e3Nd68eb93b.jpg!q70.jpg.webp"))
        list.add(NormalNews(
                "苹果怒怼微信，腾讯也会被“欺负”，马化腾哭晕在厕所",
                "https://m.360buyimg.com/mobilecms/jfs/t5464/259/869575759/49444/b45ea8ff/590820b7Na25cffff.jpg!q70.jpg.webp"))
        list.add(NormalNews(
                "喜欢日本文化的人对于日本的国服：和服一定不会陌生。日本女生一般到重要的日子都会换上大大的和服，然而和服那么华丽，一般女生们在穿和服时，她们在和服里面又会穿些什么呢？",
                "https://m.360buyimg.com/mobilecms/jfs/t9292/288/2559796147/62939/4c3b913/59db3c8aN3dae0b2f.jpg!q70.jpg.webp"))

        calendar.add(Calendar.DAY_OF_MONTH, 10)
        calendar.add(Calendar.HOUR_OF_DAY, 5)
        list.add(Time(calendar.timeInMillis))
        list.add(TopNews("https://m.360buyimg.com/mobilecms/jfs/t7852/301/620762985/163224/e6371f9/5995218bN8ce2101c.jpg!q70.jpg.webp"))
        list.add(NormalNews(
                "苹果悄悄地在官网带来了128GB版本的iPhone SE，还有国内用户非常期待的iPhone 7红色版本，此外，还有升级A9处理器的全新iPad，取代iPad Air 2，而iPad Air 2已经下架。让人惊喜的是，全新配色的Apple Watch表带和iPhone保护壳也出现了，买买买走起来！",
                "https://m.360buyimg.com/mobilecms/jfs/t3211/232/9416074881/129498/f8eab885/58d20a97Nc13d4cca.JPG!q70.jpg.webp"))
        list.add(NormalNews(
                "七年磨一剑，广角+长焦双摄、四曲面、6G内存、满血骁龙835的小米6一经发布，便赢得新一代性能怪兽的地位和旗舰机里超高的性价比极佳口碑。雷军也在微博上截图了福布斯的话：“小米6上手体验：苹果7的照相技术，三星S8的配置，但只有一半的价钱。”",
                "https://m.360buyimg.com/mobilecms/jfs/t5269/337/308409086/76445/3f15f62a/58fc8dedN4c9c1210.jpg!q70.jpg.webp"))


        calendar.add(Calendar.DAY_OF_MONTH, 5)
        calendar.add(Calendar.HOUR_OF_DAY, 9)
        list.add(Time(calendar.timeInMillis))
        list.add(TopNews("https://m.360buyimg.com/mobilecms/jfs/t3571/336/2122312153/130467/c6cf6893/583f8cb5N0986804a.jpg!q70.jpg.webp"))
        list.add(NormalNews(
                "今年的一加3异常火爆，主要是2499元的高性价比搭配骁龙820处理器以及6G运存和64G内存，优质的相机素质。所以这部手机能够被炒火，这正是一加经历了一加2和一加x的失败后，发展的好时机。",
                "https://m.360buyimg.com/mobilecms/jfs/t3079/321/3999053678/31382/91437c0e/57fddb16Nb75d1d7e.jpg!q70.jpg.webp"))
        list.add(NormalNews(
                "这是手机时代，16GB存储空间的手机根本没法用，早已被时代淘汰。然而你想去买个个运行快、续航时间久、有超大内存的手机，实在太贵了！超过你的经济支付能力。",
                "https://m.360buyimg.com/mobilecms/jfs/t6028/268/2258813157/191425/c4c8dfc6/593e3fe5N5b899c94.jpg!q70.jpg.webp"))
        list.add(NormalNews(
                "如果问谁会是智能手机里，“寿命最长”的型号？大家都必须承认苹果的iPhone 手机有着先天的长寿体质，这点从苹果iPhone 4依旧在很多人手中服役，即可得出论据。然而小米手机表示不服，有网友说：小米MIX绝对会比iPhone 7 Plus更长寿。",
                "https://m.360buyimg.com/mobilecms/jfs/t3988/270/2505941410/158663/6204bb2c/58a9b3f7N4d4d38a7.jpg!q70.jpg.webp"))
        list.add(NormalNews(
                "提起上半年的旗舰机，凡搭载高通骁龙835处理器的手机，款款都能惊艳四座；当然，下半年还会有很多很重量级的产品登场，比如配备联发科X30处理器刚发布不久的魅族Pro 7，搭载了A11处理器的iPhone新机，即将登场搭载海思麒麟970处理器的华为Mate 10等。",
                "https://m.360buyimg.com/mobilecms/jfs/t9490/25/973382396/45869/49145324/59b20745Nbcefd1e2.jpg!q70.jpg.webp"))

        calendar.add(Calendar.DAY_OF_MONTH, 1)
        calendar.add(Calendar.HOUR_OF_DAY, 1)
        list.add(Time(calendar.timeInMillis))
        list.add(TopNews("https://m.360buyimg.com/mobilecms/jfs/t3049/281/5926894390/222837/e4f798a3/58931bbdN6a676c54.jpg!q70.jpg.webp"))
        list.add(NormalNews(
                "骁龙820，今年手机界的一颗“当红”处理器，凭借着超高的性能，更好的功耗和发热控制，成为了今年绝对的发烧级处理器。虽然现在最新的骁龙821已经正式上市，不过真正架构、制程、工艺和骁龙820都极为相似，所以骁龙820还是目前高配发烧机的必备处理器。最近采用标准电压版骁龙820的ZUK 2和乐Max 2都作出了极大的价格下调，甚至下探到千元机的价位段，是否会对千元机市场造成极大的影响呢？",
                "https://m.360buyimg.com/mobilecms/jfs/t3640/77/525928577/467707/b506aab/580b9530Ncf40a962.png.webp"))
    }

    private fun bindAdapter() {
        adapter = ListViewAdapter(context!!, list)
                .match(Time::class, R.layout.item_multiple_type_list_view_time)
                .match(TopNews::class, R.layout.item_multiple_type_list_view_top)
                .match(NormalNews::class, R.layout.item_multiple_type_list_view_normal)
                .holderCreateListener {

                }
                .holderBindListener { holder, position ->
                    val item = adapter.getItem(position)
                    when (item) {
                        is Time -> {
                            holder.withView<TextView>(R.id.tv_time, { text = SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault()).format(item.time) })
                        }
                        is TopNews -> {
                            holder.withView<DraweeImageView>(R.id.iv_image, { GImageLoader.displayUrl(context, it, item.imageUrl) })
                        }
                        is NormalNews -> {
                            holder.withView<TextView>(R.id.tv_content, { text = item.content })
                                    .withView<DraweeImageView>(R.id.iv_image, { GImageLoader.displayUrl(context, it, item.imageUrl) })
                        }
                    }
                }
                .clickListener { holder, position ->

                }
                .attach(binding.lv)
    }
}