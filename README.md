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