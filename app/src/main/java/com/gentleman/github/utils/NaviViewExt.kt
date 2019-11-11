package com.gentleman.github.utils

import android.support.design.widget.NavigationView
import android.support.v4.view.ViewCompat
import android.view.View
import com.gentleman.common.ext.otherwise
import com.gentleman.common.ext.yes

/**
 * @author Gentleman
 * @date 2019/9/30
 * Description
 */

inline fun NavigationView.doOnLayoutAvailable(crossinline block: () -> Unit) {
    ViewCompat.isLaidOut(this).yes {
        block()
    }.otherwise {
        addOnLayoutChangeListener(object : View.OnLayoutChangeListener {
            override fun onLayoutChange(
                v: View?,
                left: Int,
                top: Int,
                right: Int,
                bottom: Int,
                oldLeft: Int,
                oldTop: Int,
                oldRight: Int,
                oldBottom: Int
            ) {
                removeOnLayoutChangeListener(this)
                block()
            }
        })
    }
}