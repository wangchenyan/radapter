package me.wcy.radapter

import android.support.v7.widget.RecyclerView
import android.util.Log
import android.util.SparseArray
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

class RAdapter(private val dataList: MutableList<*>) : RecyclerView.Adapter<RViewHolder<*>>() {
    private val typePool = RTypeManager()
    private var extras = SparseArray<Any>()
    private var vhList = mutableListOf<RViewHolder<*>>()

    companion object {
        private const val TAG = "RAdapter"
    }

    init {
        registerAdapterDataObserver(DataObserver())
    }

    /**
     * 注册 ViewHolder
     *
     * @param model 数据类
     * @param viewHolder ViewHolder
     * @param layoutResId 指定布局文件
     */
    fun <T> register(model: Class<T>, viewHolder: Class<out RViewHolder<T>>, layoutResId: Int = 0): RAdapter {
        register(model, DefaultConverter(viewHolder, layoutResId))
        return this
    }

    /**
     * 注册 ViewHolder
     *
     * @param model 数据类
     * @param converter 数据到 ViewHolder 的转换器
     */
    fun <T> register(model: Class<T>, converter: RConverter<T>): RAdapter {
        typePool.register(model, converter)
        return this
    }

    /**
     * 设置额外参数，可在 ViewHolder 中读取 ViewHolder.getExtra(key)
     */
    fun putExtra(key: Int, value: Any): RAdapter {
        extras.put(key, value)
        return this
    }

    /**
     * 获取额外参数
     */
    fun getExtra(key: Int): Any? {
        return extras.get(key)
    }

    /**
     * 获取数据列表
     */
    fun getDataList(): MutableList<*> {
        return dataList
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RViewHolder<*> {
        val clazz = typePool.getVHClass(viewType)
        val resId = typePool.getLayoutResId(viewType)
        if (resId <= 0) {
            throw IllegalStateException("can not find view holder layout, have you set?")
        }
        val view = LayoutInflater.from(parent.context).inflate(resId, parent, false)
        val constructor = clazz.getConstructor(View::class.java)
        val viewHolder = constructor.newInstance(view)
        viewHolder.setAdapter(this)
        return viewHolder
    }

    override fun onBindViewHolder(holder: RViewHolder<*>, position: Int) {
        try {
            holder.position = position
            holder.bindData(dataList[position])
            holder.refresh()
        } catch (e: Throwable) {
            Log.e(TAG, "bind view holder error", e)
        }
    }

    override fun getItemCount(): Int {
        return dataList.size
    }

    override fun getItemViewType(position: Int): Int {
        val data = dataList[position]
        val type = typePool.getTypePosition(data)
        if (type < 0) {
            throw IllegalStateException("can not find view type of ${data?.javaClass}, have you register the data?")
        }
        return type
    }

    override fun onViewAttachedToWindow(holder: RViewHolder<*>) {
        super.onViewAttachedToWindow(holder)
        holder.onViewAttachedToWindow()
        vhList.add(holder)
    }

    override fun onViewDetachedFromWindow(holder: RViewHolder<*>) {
        super.onViewDetachedFromWindow(holder)
        holder.onViewDetachedFromWindow()
        vhList.remove(holder)
    }

    /**
     * 数据改变时通知 ViewHolder position 过期
     */
    private inner class DataObserver : RecyclerView.AdapterDataObserver() {
        override fun onItemRangeRemoved(positionStart: Int, itemCount: Int) {
            super.onItemRangeRemoved(positionStart, itemCount)
            notifyVHPosExpired()
        }

        override fun onItemRangeMoved(fromPosition: Int, toPosition: Int, itemCount: Int) {
            super.onItemRangeMoved(fromPosition, toPosition, itemCount)
            notifyVHPosExpired()
        }

        override fun onItemRangeInserted(positionStart: Int, itemCount: Int) {
            super.onItemRangeInserted(positionStart, itemCount)
            notifyVHPosExpired()
        }
    }

    /**
     * 数据顺序改变，通知 ViewHolder position 过期
     */
    private fun notifyVHPosExpired() {
        for (vh in vhList) {
            vh.setPosExpired()
        }
    }
}
