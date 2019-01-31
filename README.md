# radapter
使用 radapter，你可以方便的构造多种类型的列表视图。

![](https://raw.githubusercontent.com/wangchenyan/radapter/master/art/recycler-view.jpg)

## 前言
三个月没有更新博客了，主要是因为最近换了工作，在新公司还在摸爬滚打，没有时间更新，心想2018年就要结束了，不能留下遗憾，于是趁着元旦假期来一发，biu~ 以后应该会正常更新，感谢关注的朋友们。

2018年对大部分人来说是萧条的一年，我在此祝愿大家2019年红红火火，升职加薪！

不知道还有没有人记得前一篇 [打造一个通用的 RecyclerView Adapter](https://juejin.im/post/5c3810eb6fb9a049dc027510)，这个版本算是比较初级的，因此也没有上传到仓库，经过自己一年来的使用，不断完善，终于推出 2.0 版本了，并且也上传了 JitPack 仓库，下面我就来介绍一下这个 2.0 版本有什么不同吧。

## 使用

### Gradle

**Step 1.** Add the JitPack repository to your build file

Add it in your root build.gradle at the end of repositories:

```
allprojects {
	repositories {
		...
		maven { url 'https://jitpack.io' }
	}
}
```

**Step 2.** Add the dependency

```
dependencies {
    implementation 'com.github.wangchenyan:radapter:2.0.1'
}
```

### 混淆
该库已经默认添加了混淆配置，使用 AAR 依赖是不需要特殊处理的，如果使用 Jar 依赖，需要手动添加以下配置

```
-keepclassmembers class * extends me.wcy.radapter.RViewHolder {
    public <init>(android.view.View);
}
```

## 介绍
原始的使用 Recycler View 的方式我就不赘述了，总之是比较繁琐的，我直接介绍 radapter 如何使用。

对于多种类型的列表，我们一般是根据 `getItemViewType()` 区分类型，来构造、渲染视图，这种繁琐的方式大家应该已经写得要吐了吧，反正我是不想再写了。

radapter 就是来解救你的，它可以让你丢弃 Adapter，丢弃多类型时繁琐的逻辑，只需要保留有用的部分，即 ViewHolder，使 ViewHolder 可以专注于处理自己的业务。

来看一个栗子，一个图文混合的列表

```
// 首先，添加数据，Image 保存了图片资源ID，Text 保存了文本
val dataList = mutableListOf<Any>()
dataList.add(Image(R.mipmap.image1))
dataList.add(Text("渊虹"))
dataList.add(Image(R.mipmap.image2))
dataList.add(Text("鲨齿"))
dataList.add(Image(R.mipmap.image3))
dataList.add(Text("干将莫邪"))
dataList.add(Image(R.mipmap.image4))
dataList.add(Text("墨眉"))

// 使用 radapter，注册数据和 ViewHolder
val adapter = RAdapter(dataList)
adapter.register(Image::class.java, ImageViewHolder::class.java)
adapter.register(Text::class.java, TextViewHolder::class.java)

// 设置 adapter
recycler.layoutManager = LinearLayoutManager(this)
recycler.adapter = adapter
```

使用就这么简单，你只需要实现 ViewHolder 即可，看下 ViewHolder 的实现

```
@RLayout(R.layout.view_holder_image)
class ImageViewHolder(itemView: View) : RViewHolder<Image>(itemView) {
    override fun refresh() {
        itemView.image.setImageResource(data().resId)
    }
}
```

继承 `RViewHolder`，复写 `refresh` 方法刷新视图即可，可以使用父类的方法和变量

|方法/变量|返回值/类型|备注|
|:-:|:-:|:-:|
| context | Context | 上下文 |
| adapter() | RAdapter | 适配器 |
| data() | T | 泛型数据 |
| position() | int | 当前位置，调用 adapter 的 `notifyItemInserted` 或 `notifyItemRemoved` 后会自动更新位置 |
| getExtra(key: Int) | Any | 可以取得 adapter.putExtra() 存放的数据，下文会介绍 |

这些方法/变量应该足够我们使用了。

看下效果

![](https://raw.githubusercontent.com/wangchenyan/radapter/master/art/image01.jpg)

到现在，我们可以根据数据类型区分不同的 ViewHolder，但有时候同一种类型的数据，可能根据不同的属性，也要展示不同的 ViewHolder，怎么办呢？

我们把栗子稍微改一下，还是图文混合，现在可以设置文本的样式，样式不同要使用不同的 ViewHolder，看一下如何实现

```
// 我们给 Text 加上 style 属性
val dataList = mutableListOf<Any>()
dataList.add(Image(R.mipmap.image1))
dataList.add(Text("渊虹", 2))
dataList.add(Image(R.mipmap.image2))
dataList.add(Text("鲨齿", 1))
dataList.add(Image(R.mipmap.image3))
dataList.add(Text("干将莫邪", 2))
dataList.add(Image(R.mipmap.image4))
dataList.add(Text("墨眉", 1))

val adapter = RAdapter(dataList)
adapter.register(Image::class.java, ImageViewHolder::class.java)
// 注册 Text 时使用 RConverter，根据 Text 的属性，使用不同的 ViewHolder
adapter.register(Text::class.java, object : RConverter<Text>() {
    override fun convert(data: Text): RViewHolderWrap<Text> {
        return when (data.style) {
            1 -> RViewHolderWrap(TextViewHolder1::class.java)
            2 -> RViewHolderWrap(TextViewHolder2::class.java)
            else -> RViewHolderWrap(TextViewHolder2::class.java)
        }
    }
})

recycler.layoutManager = LinearLayoutManager(this)
recycler.adapter = adapter
```

仍然很简单，看下效果

![](https://raw.githubusercontent.com/wangchenyan/radapter/master/art/image02.jpg)

看了 ViewHolder 的代码，我们知道，ViewHolder 和 layout 通过注解的方式绑定，但是有些同学说我想在 library 里面使用怎么办，我们知道，library 里面注解不能直接引用 R 文件里面的常量，这个也很简单，注册时使用重载方法即可

```
adapter.register(Image::class.java, ImageViewHolder::class.java, R.layout.view_holder_image)
adapter.register(Text::class.java, object : RConverter<Text>() {
    override fun convert(data: Text): RViewHolderWrap<Text> {
        return when (data.style) {
            1 -> RViewHolderWrap(TextViewHolder2::class.java)
            2 -> RViewHolderWrap(TextViewHolder1::class.java, R.layout.view_holder_text_1)
            else -> RViewHolderWrap(TextViewHolder1::class.java, R.layout.view_holder_text_1)
        }
    }
})
```

有时，我们想在 ViewHolder 里面使用 Activity/Fragment 里面的数据，这是比较麻烦的，这里我们也考虑到了

```
// Activity
adapter.putExtra(100, "any extra")

// ViewHolder
可以取得 adapter.putExtra() 存放的数据
val extra = adapter().getExtra(100)
```

是不是方便了许多。

## 代码

[radapter](https://github.com/wangchenyan/radapter)

## 总结
大家在使用中有什么问题或建议，欢迎提 [Issues](https://github.com/wangchenyan/radapter/issues)
