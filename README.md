# kotlin-adapter
Base adapter for recyclerView，absListView, support multiple item view type, sticky header(selection), drag & drop, swipe dismiss

[Demo Download](https://www.pgyer.com/cCxm)

特点(Features)
=============
- 语法简单，代码优雅，不需要重写ViewHolder，通过链式调用实现适配器创建；
- 支持多种样式，解决视图复用导致的页面错乱问题；
- 提供一系列拦截器，满足大部分场景下的页面数据刷新；
- 封装ViewHolder，通过高阶函数简化视图数据更新；
- 提供一系列拓展函数对适配器数据进行操作，见[AdapterExtensions](adapter_core/src/main/kotlin/com/wuhenzhizao/adapter/extension/AdapterExtensions.kt)；  
- 支持DataBinding；
- 封装了sticky header, swipe menu等效果，使用方便；

Preview
=======
- **Single Item View Type**

|ListView|RecyclerView|RecyclerView + DataBinding|
|:---:|:---:|:---:|
|<img src="screenshots/20171220_221339.gif"  width="250">|<img src="screenshots/20171220_221506.gif"  width="250">|<img src="screenshots/20171220_221641.gif"  width="250">|

- **Multiple Item View Type**

|ListView|RecyclerView|
|:---:|:---:|
|<img src="screenshots/20171220_221813.gif"  width="250">|<img src="screenshots/20171220_222005.gif"  width="250">|


- **Extension(For RecyclerView)**

|Sticky Header|Swipe Menu|Drag & Drop|
|:---:|:---:|:---:|
|<img src="screenshots/20171220_222107.gif"  width="250">|<img src="screenshots/20171220_222633.gif"  width="250">|<img src="screenshots/20171220_222704.gif"  width="250">|

|Swipe Dismiss|
|:---:|
|<img src="screenshots/20171220_222907.gif"  width="250">|

Setup
=====


 [ ![Download](https://api.bintray.com/packages/wuhenzhizao/maven/kotlin-adapter-core/images/download.svg) ](https://bintray.com/wuhenzhizao/maven/kotlin-adapter-core/_latestVersion) 
 [ ![Download](https://api.bintray.com/packages/wuhenzhizao/maven/kotlin-adapter-extension/images/download.svg) ](https://bintray.com/wuhenzhizao/maven/kotlin-adapter-extension/_latestVersion)

```dsl
dependencies {
    // 核心依赖
    compile 'com.wuhenzhizao:kotlin-adapter-core:1.0.0'
    // 拓展效果依赖
    compile 'com.wuhenzhizao:kotlin-adapter-extension:1.0.0'
}
```

Usage(更多用法见example)
======================

**★ 提供了如下几种适配器实现**  

|类型|介绍|
|:---|:--|  
|[ListViewAdapter](adapter_core/src/main/kotlin/com/wuhenzhizao/adapter/ListViewAdapter.kt)|ListView适配器| 
|[RecyclerViewAdapter](adapter_core/src/main/kotlin/com/wuhenzhizao/adapter/RecyclerViewAdapter.kt)|RecyclerView适配器|  
|[RecyclerViewBindingAdapter](adapter_core/src/main/kotlin/com/wuhenzhizao/adapter/RecyclerViewBindingAdapter.kt)|RecyclerView适配器，使用DataBinding|  
|[StickyRecyclerViewAdapter](adapter_extension/src/main/kotlin/com/wuhenzhizao/adapter/extension/stickyHeader/StickyRecyclerViewAdapter.kt)|支持Sticky Header效果的RecyclerView适配器|  
|[SwipeMenuRecyclerViewAdapter](adapter_extension/src/main/kotlin/com/wuhenzhizao/adapter/extension/swipeMenu/SwipeMenuRecyclerViewAdapter.kt)|支持侧滑菜单效果的RecyclerView适配器|  
|[SwipeMenuStickyRecyclerViewAdapter](adapter_extension/src/main/kotlin/com/wuhenzhizao/adapter/extension/swipeMenu/SwipeMenuStickyRecyclerViewAdapter.kt)|同时支持Sticky Header和侧滑菜单效果的RecyclerView适配器（使用场景：京东购物车）|  
|[DragAndSwipeRecyclerViewAdapter](adapter_extension/src/main/kotlin/com/wuhenzhizao/adapter/extension/dragSwipeDismiss/DragAndSwipeRecyclerViewAdapter.kt)|支持Drag & Drop和Swipe Dismiss效果的RecyclerView适配器|
  
  
**★ 创建适配器（以RecyclerViewAdapter为例）** 
 
- [简单版本](adapter_example/src/main/kotlin/com/wuhenzhizao/adapter/example/ui/SingleTypeRecyclerViewFragment.kt)  

```kotlin
val adapter = RecyclerViewAdapter<Product>(context)
    .match(Product::class, R.layout.item_single_type_recycler_view)
    .holderBindInterceptor { position, viewHolder -> }
    .attach(binding.rv)
```
	
- [完整版本](adapter_example/src/main/kotlin/com/wuhenzhizao/adapter/example/ui/MultipleTypeRecyclerViewFragment.kt)  

```kotlin
val adapter = RecyclerViewAdapter(context, list.provinceList)  
    .match(Time::class, R.layout.item_multiple_type_list_view_time)
    .match(TopNews::class, R.layout.item_multiple_type_list_view_top)
    .match(NormalNews::class, R.layout.item_multiple_type_list_view_normal)
    .holderCreateInterceptor { holder ->
        // 布局创建时回调，用于对布局的处理，比如设置宽高(可省略)
    }
    .holderBindInterceptor { position, holder ->
        // 布局绑定时回调，用于更新Item UI数据，也可以设置UI监听接口
        val province = adapter.getItem(position)
        viewHolder.get<TextView>(R.id.tv, { text = province.name })
        viewHolder.get<CheckBox>(R.id.cb, { isChecked = province.checked })
    }
    .clickInterceptor { position, holder ->
        // Item最外层布局被点击回调(可省略)
    }
    .longClickInterceptor { position, holder ->
        // Item最外层布局Long Click回调(可省略)
    }
    .attach(binding.lv)  // 绑定适配器到ListView
```  
	
- [使用LayoutInterceptor替代match](adapter_example/src/main/kotlin/com/wuhenzhizao/adapter/example/ui/MultipleTypeRecyclerViewFragment.kt)   
  
```kotlin
val adapter = RecyclerViewAdapter<Any>(context)
    .layoutInterceptor {
        when (adapter.getItem(it)) {
             is BannerList -> R.layout.item_multiple_type_recycler_view_banner
             is Promotion -> R.layout.item_multiple_type_recycler_view_promotion
             is Divider -> R.layout.item_multiple_type_recycler_view_divider
             is HeaderLine -> R.layout.item_multiple_type_recycler_view_headine
             is HeaderLineProductList -> R.layout.item_multiple_type_recycler_view_headine_product
             is Recommend -> R.layout.item_multiple_type_recycler_view_recommend
             is RecommendProducts -> R.layout.item_multiple_type_recycler_view_recommend_item
             else -> {
                 0
             }
        }
    }
    .holderCreateInterceptor { holder ->
        onViewHolderCreate(it)
    }
    .holderBindInterceptor { position, viewHolder ->
        onViewHolderBind(position, viewHolder)
    }
    .attach(binding.rv)
```

**★ 更新Item数据**  

```kotlin
holderBindInterceptor { position, holder ->  
    holder.get<DraweeImageView>(R.id.iv_sku_logo, { GImageLoader.displayUrl(context, it, item.imgUrl) })
    holder.get<ImageButton>(R.id.ib_select, { isSelected = item.checkType != 0 })  
    holder.get<TextView>(R.id.tv_shopping_cart_delete, {  
        text = item.name
        setOnClickListener {
            adapter.closeAllItems()
            showToast("${item.name} is deleted")
            adapter.removeItemAt(position)
        }  
    })
}
```  

拓展
===  
**★ [创建支持Sticky Header效果的RecyclerView适配器](adapter_example/src/main/kotlin/com/wuhenzhizao/adapter/example/ui/StickyRecyclerViewFragment.kt)**  

```kotlin
val adapter = StickyRecyclerViewAdapter<Country>(context)
    .match(Country::class, R.layout.item_sticky_recycler_view)
    .matchHeader(Country::class, R.layout.item_sticky_recycler_view_header)
    .headerHolderBindInterceptor { position, holder ->
        val country = adapter.getItem(position)
        holder.get<TextView>(R.id.sticky_name, { text = country.letter })
    }
    .headerClickInterceptor { holder, clickView, position ->
        showToast("sticky header clicked, headerId = ${adapter.getHeaderId(position)}")
    }
    .attach(binding.rv)
```  

**★ [创建支持SwipeMenu效果的RecyclerView适配器](adapter_example/src/main/kotlin/com/wuhenzhizao/adapter/example/ui/SwipeMenuRecyclerViewFragment.kt)**  

```kotlin
val adapter = SwipeMenuRecyclerViewAdapter<StickyBean>(context)
    .match(Notice::class, R.layout.item_shopping_cart_notice)
    .match(Divider::class, R.layout.item_shopping_cart_divider)
    .match(ItemViewBean::class, R.layout.item_shopping_cart_sku)
    .match(RecommendProducts::class, R.layout.item_shopping_cart_recommend)
    .holderBindInterceptor { position, holder ->
        holder.get<TextView>(R.id.tv_shopping_cart_delete, {
            setOnClickListener {
                showToast("${item.name} is deleted")
                adapter.closeAllItems()
                adapter.removeItemAt(position)
            }
        })
    }
    .attach(binding.rv)
```  

**★ [创建支持拖拽效果的RecyclerView适配器](adapter_example/src/main/kotlin/com/wuhenzhizao/adapter/example/ui/SwipeMenuRecyclerViewFragment.kt)**  

```kotlin
val recyclerView = binding.rv
recyclerView.isLongPressDragEnable = true    // 开启长按拖拽
recyclerView.isItemViewSwipeEnable = true    // 开启Swipe Dismiss
recyclerView.dragDirection =                 // 设置拖拽方向
    ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT or ItemTouchHelper.UP or ItemTouchHelper.DOWN

val adapter = DragAndSwipeRecyclerViewAdapter<Topic>(context)
    .match(Topic::class, R.layout.item_drag_recycler_view)
    .dragInterceptor { from, target ->
        showToast("item draged, from ${from.adapterPosition} to ${target.adapterPosition}")
    }
    .swipeInterceptor { viewHolder, direction ->
        showToast("position ${viewHolder.adapterPosition} dismissed")
    }
    .attach(binding.rv)
```

技术交流
======
|QQ交流群|
|:---:|
|<img src="screenshots/qq_group.jpeg" alt="screenshot"  width="200">|