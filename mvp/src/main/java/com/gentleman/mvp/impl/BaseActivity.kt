package com.gentleman.mvp.impl

import android.app.Activity
import android.os.Bundle
import com.gentleman.mvp.IPresenter
import com.gentleman.mvp.IView
import java.lang.reflect.ParameterizedType
import java.lang.reflect.Type
import kotlin.reflect.KClass
import kotlin.reflect.full.isSubclassOf
import kotlin.reflect.full.primaryConstructor
import kotlin.reflect.jvm.jvmErasure

abstract class BaseActivity<out Presenter : BasePresenter<BaseActivity<Presenter>>> : Activity(), IView<Presenter> {
    private val TAG = this@BaseActivity.javaClass.simpleName

    override val presenter: Presenter

    init {
        presenter = createPresenterKt()
        presenter.view = this
    }

    private fun createPresenterKt(): Presenter {
        sequence {
            var thisClass: KClass<*> = this@BaseActivity::class
            while (true) {
                //遍历当前类型的所有父类
                yield(thisClass.supertypes)
                //supertypes里面是包含接口的
                thisClass = thisClass.supertypes.firstOrNull()?.jvmErasure ?: break
            }
        }.flatMap {
            //把父类的泛型实参的序列
            println(it)
            it.flatMap { it.arguments }.asSequence()
        }.first {
            //那第一个泛型实参，并且判断他是不是IPresenter的实例
            it.type?.jvmErasure?.isSubclassOf(IPresenter::class) ?: false
        }.let {
            //然后实例化这个Presenter
            return it.type!!.jvmErasure.primaryConstructor!!.call() as Presenter
        }
    }

    private fun createPresenter(): Presenter {
        sequence<Type> {
            var thisClass: Class<*> = this@BaseActivity.javaClass
            while (true) {
                yield(thisClass.genericSuperclass)
                thisClass = thisClass.superclass ?: break
            }
        }.filter {
            it is ParameterizedType
        }.flatMap {
            (it as ParameterizedType).actualTypeArguments.asSequence()
        }.first {
            it is Class<*> && IPresenter::class.java.isAssignableFrom(it)
        }.let {
            return (it as Class<Presenter>).newInstance()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        presenter.onCreate(savedInstanceState)
    }

    override fun onStart() {
        super.onStart()
        presenter.onStart()
    }

    override fun onPause() {
        super.onPause()
        presenter.onPause()
    }

    override fun onResume() {
        super.onResume()
        presenter.onResume()
    }

    override fun onStop() {
        super.onStop()
        presenter.onStop()
    }

    override fun onDestroy() {
        super.onDestroy()
        presenter.onDestroy()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        presenter.onSaveInstanceState(outState)
    }

    override fun onViewStateRestored(savedInstanceState: Bundle?) {
        super.onRestoreInstanceState(savedInstanceState)
        presenter.onViewStateRestored(savedInstanceState)
    }
}