package me.wcy.radapter

/**
 * Created by wangchenyan on 2018/9/21.
 */
abstract class RConverter<T> {
    internal fun convert(data: Any): RViewHolderWrap<T> {
        return convert(data as T)
    }

    abstract fun convert(data: T): RViewHolderWrap<T>
}