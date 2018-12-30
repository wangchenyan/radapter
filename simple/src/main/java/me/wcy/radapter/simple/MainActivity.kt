package me.wcy.radapter.simple

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.activity_main.*
import me.wcy.radapter.RAdapter
import me.wcy.radapter.RConverter
import me.wcy.radapter.RViewHolderWrap
import me.wcy.radapter.simple.model.Image
import me.wcy.radapter.simple.model.Text
import me.wcy.radapter.simple.viewholder.ImageViewHolder
import me.wcy.radapter.simple.viewholder.TextViewHolder2
import me.wcy.radapter.simple.viewholder.TextViewHolder1

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val dataList = mutableListOf<Any>()
        dataList.add(Image(R.mipmap.image1))
        dataList.add(Text("渊虹", 2))
        dataList.add(Image(R.mipmap.image2))
        dataList.add(Text("鲨齿", 1))
        dataList.add(Image(R.mipmap.image3))
        dataList.add(Text("干将莫邪", 2))
        dataList.add(Image(R.mipmap.image4))
        dataList.add(Text("墨眉", 1))
        dataList.add(Image(R.mipmap.image5))
        dataList.add(Text("水寒", 2))
        dataList.add(Image(R.mipmap.image6))
        dataList.add(Text("太阿", 1))

        val adapter = RAdapter(dataList)
        adapter.register(Image::class.java, ImageViewHolder::class.java)
        adapter.register(Text::class.java, object : RConverter<Text>() {
            override fun convert(data: Text): RViewHolderWrap<Text> {
                return when (data.style) {
                    1 -> RViewHolderWrap(TextViewHolder1::class.java)
                    2 -> RViewHolderWrap(TextViewHolder2::class.java, R.layout.view_holder_text_2)
                    else -> RViewHolderWrap(TextViewHolder2::class.java, R.layout.view_holder_text_2)
                }
            }
        })

        adapter.putExtra(100, "any extra")

        recycler.layoutManager = LinearLayoutManager(this)
        recycler.addItemDecoration(DividerItemDecoration(this, DividerItemDecoration.VERTICAL))
        recycler.adapter = adapter
    }
}
