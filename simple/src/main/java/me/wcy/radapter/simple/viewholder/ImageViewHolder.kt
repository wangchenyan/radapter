package me.wcy.radapter.simple.viewholder

import me.wcy.radapter.simple.databinding.ViewHolderImageBinding
import me.wcy.radapter.simple.model.Image
import me.wcy.radapter3.RViewHolder

class ImageViewHolder(private val viewBinding: ViewHolderImageBinding) : RViewHolder<ViewHolderImageBinding, Image>(viewBinding) {

    override fun refresh() {
        /**
         * 可以取得 adapter.putExtra() 存放的数据
         */
        val extra = adapter().getExtra(100)
        viewBinding.image.setImageResource(data().resId)
    }
}