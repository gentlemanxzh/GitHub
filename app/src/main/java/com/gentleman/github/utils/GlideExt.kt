package com.gentleman.github.utils

import android.widget.ImageView
import cn.carbs.android.avatarimageview.library.AvatarImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions

/**
 * @author Gentleman
 * @date 2019/9/30
 * Description
 */
fun AvatarImageView.loadWithGlide(url: String, textPlaceHolder: Char, requestOptions: RequestOptions = RequestOptions()){
    textPlaceHolder.toString().let {
        setTextAndColorSeed(it.toUpperCase(), it.hashCode().toString())
    }

    Glide.with(this.context)
        .load(url)
        .apply(requestOptions)
        .into(this)
}