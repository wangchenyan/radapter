package me.wcy.radapter

/**
 * 数据到 ViewHolder 的转换器<br>
 * 适用于根据数据同一个数据类型对应多个 ViewHolder 的情况
 *
 * Created by wangchenyan on 2018/9/21.
 */
abstract class RConverter<T> {
    internal fun convert(data: Any): RViewHolderWrap<T> {
        return convert(data as T)
    }

    /**
     * 数据转换为 ViewHolder
     */
    abstract fun convert(data: T): RViewHolderWrap<T>
}