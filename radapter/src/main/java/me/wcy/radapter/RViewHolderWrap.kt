package me.wcy.radapter

/**
 * ViewHolder 的包装类，包含 ViewHolder 类型和布局 ID
 *
 * Created by wangchenyan on 2018/9/21.
 */
class RViewHolderWrap<T>(private val viewHolder: Class<out RViewHolder<T>>, private val layoutResId: Int = 0) {

    override fun hashCode(): Int {
        return viewHolder.hashCode()
    }

    override fun equals(other: Any?): Boolean {
        return other is RViewHolderWrap<*> && viewHolder == other.viewHolder
    }

    /**
     * 获取 ViewHolder 类型
     */
    fun getViewHolder(): Class<out RViewHolder<T>> {
        return viewHolder
    }

    /**
     * 获取布局 ID
     */
    fun getLayoutResId(): Int {
        if (layoutResId > 0) {
            return layoutResId
        }
        return getLayoutResIdByAnno(viewHolder)
    }

    /**
     * 根据 ViewHolder RLayout 注解获取布局 ID
     */
    private fun getLayoutResIdByAnno(clazz: Class<*>): Int {
        if (clazz == RViewHolder::class.java) {
            return 0
        }
        val layout = clazz.getAnnotation(RLayout::class.java)
        // 找不到去父类找
        if (layout == null) {
            return getLayoutResIdByAnno(clazz.superclass)
        }
        return layout.value
    }
}