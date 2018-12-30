package me.wcy.radapter.simple.viewholder

import android.view.View
import kotlinx.android.synthetic.main.view_holder_text_2.view.*
import me.wcy.radapter.RViewHolder
import me.wcy.radapter.simple.model.Text

class TextViewHolder2(itemView: View) : RViewHolder<Text>(itemView) {

    override fun refresh() {
        itemView.text2.text = data().text
    }
}