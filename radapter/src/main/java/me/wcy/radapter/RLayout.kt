package me.wcy.radapter

import android.support.annotation.LayoutRes
import java.lang.annotation.Retention
import java.lang.annotation.RetentionPolicy

/**
 * ViewHolder 设置布局文件的注解
 */
@Target(AnnotationTarget.CLASS, AnnotationTarget.FILE)
@Retention(RetentionPolicy.RUNTIME)
annotation class RLayout(@LayoutRes val value: Int)
