package com.gentleman.github.utils


import com.gentleman.common.sharedpreferences.Preference
import com.gentleman.github.AppContext
import kotlin.reflect.jvm.jvmName

inline fun <reified R,T>R.pref(default: T) = Preference(AppContext,"",default,R::class.jvmName)