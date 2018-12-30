package me.wcy.radapter

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.View

abstract class RViewHolder<T>(itemView: View) : RecyclerView.ViewHolder(itemView) {
    protected val context: Context = itemView.context
    private var adapter: RAdapter? = null
    private var data: T? = null
    private var position: Int? = 0
    private var isPosExpired = false

    internal fun setAdapter(adapter: RAdapter) {
        this.adapter = adapter
    }

    internal fun setPosition(position: Int) {
        this.position = position
    }

    internal fun bindData(data: Any?) {
        this.data = data as T
    }

    internal fun setPosExpired() {
        isPosExpired = true
    }

    abstract fun refresh()

    protected fun adapter(): RAdapter {
        return adapter!!
    }

    protected fun data(): T {
        return data!!
    }

    protected fun position(): Int {
        if (isPosExpired) {
            position = adapter().getDataList().indexOf(data())
            isPosExpired = false
        }
        return position!!
    }

    protected fun getExtra(key: Int): Any? {
        return adapter?.getExtra(key)
    }
}
