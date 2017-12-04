package com.wuhenzhizao.adapter

import kotlin.reflect.KClass

/**
 * Created by liufei on 2017/12/3.
 */
class ItemTypeChain(private val classType: KClass<*>, val itemLayoutId: Int) {

    fun isTypeForView(classType: KClass<*>): Boolean = this.classType == classType
}