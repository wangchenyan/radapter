package me.wcy.radapter.simple.viewholder

import android.view.View
import kotlinx.android.synthetic.main.view_holder_image.view.*
import me.wcy.radapter.RLayout
import me.wcy.radapter.RViewHolder
import me.wcy.radapter.simple.R
import me.wcy.radapter.simple.model.Image

@RLayout(R.layout.view_holder_image)
class ImageViewHolder(itemView: View) : RViewHolder<Image>(itemView) {

    override fun refresh() {
        /**
         * 可以取得 adapter.putExtra() 存放的数据
         */
        val extra = adapter().getExtra(100)
        itemView.image.setImageResource(data().resId)
    }
}