package me.wcy.radapter

/**
 * Created by wangchenyan on 2018/9/21.
 */
internal class RType<T>(
        internal val model: Class<T>,
        internal val converter: RConverter<T>) {

    override fun hashCode(): Int {
        return model.hashCode()
    }

    override fun equals(other: Any?): Boolean {
        return other is RType<*> && model == other.model
    }
}