package me.wcy.radapter

import androidx.annotation.LayoutRes

/**
 * ViewHolder 设置布局文件的注解
 */
@Target(AnnotationTarget.CLASS)
@Retention(AnnotationRetention.RUNTIME)
annotation class RLayout(@LayoutRes val value: Int)
