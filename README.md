# kotlin-adapter
Base adapter for RecyclerView，AbsListView, support multiple item view type, Sticky , Drag, Swipe Dismiss

[Demo Download](https://www.pgyer.com/cCxm)

特点(Features)
=============
- 语法简单，代码优雅，不需要重写ViewHolder，通过链式调用实现适配器创建；
- 支持多种样式，解决视图复用导致的页面错乱问题；
- 提供一系列拦截器，满足大部分场景下的页面数据刷新；
- 封装ViewHolder，通过高阶函数简化视图数据更新；
- 提供一系列拓展函数对适配器数据进行操作，见[AdapterExtensions](adapter_core/src/main/kotlin/com/wuhenzhizao/adapter/extension/AdapterExtensions.kt)；  
- 支持DataBinding；
- 封装了Sticky Header, Swipe Menu等效果，使用方便；

Preview
=======
- Single Item View Type

|ListView|RecyclerView|RecyclerView + DataBinding|
|:---:|:---:|:---:|
|<img src="screenshots/20171220_221339.gif"  width="250">|<img src="screenshots/20171220_221506.gif"  width="250">|<img src="screenshots/20171220_221641.gif"  width="250">|

- Multiple Item View Type

|ListView|RecyclerView|
|:---:|:---:|
|<img src="screenshots/20171220_221813.gif"  width="250">|<img src="screenshots/20171220_222005.gif"  width="250">|


- Extension Functions(For RecyclerView)

|Sticky Header|Swipe Menu|Drag Item|
|:---:|:---:|:---:|
|<img src="screenshots/20171220_222107.gif"  width="250">|<img src="screenshots/20171220_222633.gif"  width="250">|<img src="screenshots/20171220_222704.gif"  width="250">|

|Swipe Dismiss|
|:---:|
|<img src="screenshots/20171220_222907.gif"  width="250">|

Setup
=====


[![Download](https://api.bintray.com/packages/wuhenzhizao/maven/titlebar/images/download.svg) ](https://bintray.com/wuhenzhizao/maven/titlebar/_latestVersion)  

```xml
dependencies {
    // 核心依赖
    compile 'com.wuhenzhizao:kotlin-adapter-core:1.0.0'
    // 拓展效果依赖
    compile 'com.wuhenzhizao:kotlin-adapter-extension:1.0.0'
}
```

Usage
=====

- 提供了如下几种适配器实现 

  [ListViewAdapter](adapter_core/src/main/kotlin/com/wuhenzhizao/adapter/ListViewAdapter.kt): ListView适配器  
  [RecyclerViewAdapter](adapter_core/src/main/kotlin/com/wuhenzhizao/adapter/RecyclerViewAdapter): RecyclerView适配器  
  [RecyclerViewBindingAdapter](adapter_core/src/main/kotlin/com/wuhenzhizao/adapter/RecyclerViewBindingAdapter)：RecycerView适配器，使用DataBinding  
  [StickyRecyclerViewAdapter](adapter_core/src/main/kotlin/com/wuhenzhizao/adapter/StickyRecyclerViewAdapter): 支持Sticky Header效果的RecyclerView适配器  
  [SwipeMenuRecyclerViewAdapter](adapter_core/src/main/kotlin/com/wuhenzhizao/adapter/SwipeMenuRecyclerViewAdapter): 支持侧滑菜单效果的RecyclerView适配器  
  [SwipeMenuStickyRecyclerViewAdapter](adapter_core/src/main/kotlin/com/wuhenzhizao/adapter/SwipeMenuStickyRecyclerViewAdapter)：同时支持Sticky Header和侧滑菜单效果的RecyclerView适配器（使用场景：京东购物车）  
  [DragAndSwipeRecyclerViewAdapter](adapter_core/src/main/kotlin/com/wuhenzhizao/adapter/DragAndSwipeRecyclerViewAdapter)：支持拖动和滑动消失效果的RecyclerView适配器
  
  
- 创建适配器（以RecyclerViewAdapter为例）  
  - 简单版本

	 ```kotlin
	val adapter = RecyclerViewAdapter(context, list.provinceList)
		    .match(Province::class, R.layout.item_single_type_list_view)  // 注册ViewBean与Item之间的匹配关系（不可省略）
		    .holderBindInterceptor { position, holder -> }
		    .attach(binding.lv)  
	```
	
	- 完整版本

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
	
  - 使用LayoutInterceptor替代match   
  
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
			.holderCreateInterceptor {
				onViewHolderCreate(it)
			}
			.holderBindInterceptor { position, viewHolder ->
				onViewHolderBind(position, viewHolder)
			}
			.clickInterceptor { position, holder ->
				
			}
			.attach(binding.rv)
  	```

技术交流
======
|QQ交流群|
|:---:|
|<img src="screenshots/qq_group.jpeg" alt="screenshot"  width="200">|