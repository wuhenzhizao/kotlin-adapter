package com.wuhenzhizao.adapter

import kotlin.reflect.KClass

/**
 * Created by liufei on 2017/12/3.
 */
class ItemType(private val classType: KClass<*>, val itemLayoutId: Int, val variableId: Int) {

    constructor(classType: KClass<*>, itemLayoutId: Int) : this(classType, itemLayoutId, 0)
}