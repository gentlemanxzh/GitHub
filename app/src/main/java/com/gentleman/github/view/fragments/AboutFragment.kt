package com.gentleman.github.view.fragments

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.gentleman.github.R
import com.gentleman.github.utils.markdownText
import org.jetbrains.anko.*
import org.jetbrains.anko.sdk15.listeners.onClick
import org.jetbrains.anko.support.v4.UI
import org.jetbrains.anko.support.v4.nestedScrollView

class AboutFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return AboutFragmentUI().createView(AnkoContext.Companion.create(context!!, this))
    }
}

class AboutFragmentUI : AnkoComponent<AboutFragment> {
    override fun createView(ui: AnkoContext<AboutFragment>): View = ui.apply {
        nestedScrollView {
            verticalLayout {
                imageView {
                    imageResource = R.mipmap.ic_launcher
                }.lparams(width = wrapContent, height = wrapContent) {
                    gravity = Gravity.CENTER_HORIZONTAL
                }
                themedTextView("Github",R.style.detail_title) {
                    textColorResource = android.R.color.black
                }.lparams(width = wrapContent, height = wrapContent) {
                    gravity = Gravity.CENTER_HORIZONTAL
                }
                themedTextView("By Gentleman",R.style.detail_description) {
                    textColorResource = android.R.color.black
                }.lparams(width = wrapContent, height = wrapContent) {
                    gravity = Gravity.CENTER_HORIZONTAL
                }
                themedTextView(R.string.open_source_licenses,R.style.detail_description) {
                    textColorResource = android.R.color.black
                    onClick {
                        alert {
                            customView {
                                scrollView {
                                    textView {
                                        padding = dip(10)
                                        markdownText = context.assets.open("licenses.md").bufferedReader().readText()
                                    }
                                }
                            }
                        }.show()
                    }
                }.lparams(width = wrapContent, height = wrapContent) {
                    gravity = Gravity.CENTER_HORIZONTAL
                }
            }.lparams(width = wrapContent, height = wrapContent) {
                gravity = Gravity.CENTER
            }
        }
    }.view

}