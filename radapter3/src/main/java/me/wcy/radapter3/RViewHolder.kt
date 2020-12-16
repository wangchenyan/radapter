package me.wcy.radapter3

import android.content.Context
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding

/**
 * ViewHolder 基类
 */
abstract class RViewHolder<VB : ViewBinding, T>(viewBinding: VB) : RecyclerView.ViewHolder(viewBinding.root) {
    protected val context: Context = viewBinding.root.context
    private lateinit var adapter: RAdapter
    private var data: T? = null
    private var rPosition = 0
    private var isPosExpired = false

    internal fun setAdapter(adapter: RAdapter) {
        this.adapter = adapter
    }

    internal fun setPosition(position: Int) {
        this.rPosition = position
    }

    @Suppress("UNCHECKED_CAST")
    internal fun bindData(data: Any?) {
        this.data = data as T
    }

    internal fun setPosExpired() {
        isPosExpired = true
    }

    /**
     * 刷新界面
     */
    abstract fun refresh()

    /**
     * 获取 adapter
     */
    protected fun adapter(): RAdapter {
        return adapter
    }

    /**
     * 获取数据
     */
    protected fun data(): T {
        return data!!
    }

    /**
     * 获取 item 在列表中的位置
     */
    protected fun position(): Int {
        if (isPosExpired) {
            rPosition = adapter().getDataList().indexOf(data())
            isPosExpired = false
        }
        return rPosition
    }

    /**
     * 获取 adapter.putExtra 设置的额外参数
     */
    protected fun getExtra(key: Int): Any? {
        return adapter.getExtra(key)
    }

    open fun onViewAttachedToWindow() {
    }

    open fun onViewDetachedFromWindow() {
    }
}
