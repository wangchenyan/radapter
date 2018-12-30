package me.wcy.radapter.simple.viewholder

import android.view.View
import kotlinx.android.synthetic.main.view_holder_text_1.view.*
import me.wcy.radapter.RLayout
import me.wcy.radapter.RViewHolder
import me.wcy.radapter.simple.R
import me.wcy.radapter.simple.model.Text

@RLayout(R.layout.view_holder_text_1)
class TextViewHolder1(itemView: View) : RViewHolder<Text>(itemView) {

    override fun refresh() {
        itemView.text1.text = data().text
    }
}