package me.wcy.radapter

/**
 * 默认的数据到 ViewHolder 的转换器
 *
 * Created by wangchenyan on 2018/9/21.
 */
class DefaultConverter<T>(
        private val viewHolder: Class<out RViewHolder<T>>,
        private val layoutResId: Int = 0
) : RConverter<T>() {

    override fun convert(data: T): RViewHolderWrap<T> {
        return RViewHolderWrap(viewHolder, layoutResId)
    }
}