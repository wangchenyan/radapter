package me.wcy.radapter

/**
 * 列表 item 实体管理器
 *
 * Created by wangchenyan on 2018/9/21.
 */
internal class RTypeManager {
    private val typeList = mutableListOf<RType<*>>()
    private val vhList = mutableListOf<RViewHolderWrap<*>>()

    /**
     * 注册一个实体
     */
    fun <T> register(model: Class<T>, converter: RConverter<T>) {
        val type = RType(model, converter)
        val index = typeList.indexOf(type)
        if (index >= 0) {
            typeList[index] = type
        } else {
            typeList.add(type)
        }
    }

    /**
     * 根据数据获取 item 实体对应的 ViewHolder 位置，即 Adapter.getItemViewType()
     */
    fun getTypePosition(data: Any?): Int {
        if (data == null) {
            return -1
        }
        val clazz = data.javaClass
        var converter: RConverter<*>? = null
        for (type in typeList) {
            if (clazz == type.model) {
                converter = type.converter
                break
            }
        }
        if (converter == null) {
            // 尝试查找子类
            for (type in typeList) {
                if (type.model.isAssignableFrom(clazz)) {
                    converter = type.converter
                    break
                }
            }
        }
        if (converter == null) {
            return -1
        }
        val vhWrap = converter.convert(data)
        if (!vhList.contains(vhWrap)) {
            vhList.add(vhWrap)
        }
        return vhList.indexOf(vhWrap)
    }

    /**
     * 获取 ViewHolder class
     */
    fun getVHClass(viewType: Int): Class<out RViewHolder<*>> {
        return vhList[viewType].getViewHolder()
    }

    /**
     * 获取 ViewHolder 布局
     */
    fun getLayoutResId(viewType: Int): Int {
        return vhList[viewType].getLayoutResId()
    }
}