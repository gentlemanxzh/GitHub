package com.gentleman.github.utils

import android.widget.TextView
import com.zzhoujay.richtext.RichText

/**
 * @author Gentleman
 * @date 2019/10/6
 * Description
 */

var TextView.markdownText: String
    set(value) {
        RichText.fromMarkdown(value).into(this)
    }
    get() = text.toString()