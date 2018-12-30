package me.wcy.radapter

/**
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