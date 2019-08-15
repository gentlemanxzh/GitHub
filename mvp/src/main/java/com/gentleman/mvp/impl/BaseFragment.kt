package com.gentleman.mvp.impl

import android.os.Bundle
import android.os.CancellationSignal
import android.support.v4.app.Fragment
import android.util.Log
import android.view.View
import com.gentleman.mvp.IPresenter
import com.gentleman.mvp.IView
import com.gentleman.mvp.mvps
import java.lang.reflect.ParameterizedType
import java.lang.reflect.Type
import kotlin.reflect.KClass
import kotlin.reflect.full.isSubclassOf
import kotlin.reflect.full.primaryConstructor
import kotlin.reflect.jvm.jvmErasure

abstract class BaseFragment<out Presenter : BasePresenter<BaseFragment<Presenter>>> : Fragment(), IView<Presenter> {
    private val TAG = BaseFragment::class.java.simpleName
    override val presenter: Presenter

    init {
        presenter = createPresenterKt()
        presenter.view = this
    }

   private fun createPresenterKt(): Presenter {
        sequence {
            var thisClass: KClass<*> = this@BaseFragment::class
            while (true) {
                //遍历当前类型的所有父类
                yield(thisClass.supertypes)
                //TODO
                Log.d(TAG,"1"+thisClass.supertypes)
                //supertypes里面是包含接口的
                thisClass = thisClass.supertypes.firstOrNull()?.jvmErasure ?: break
                Log.d(TAG,"2"+thisClass.supertypes)
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

    private fun createPresenter():Presenter{
        sequence<Type>{
            var thisClass:Class<*> = this@BaseFragment.javaClass
            while (true){
                yield(thisClass.genericSuperclass)
                thisClass = thisClass.superclass?:break
            }
        }.filter {
            it is ParameterizedType
        }.flatMap {
            (it as ParameterizedType).actualTypeArguments.asSequence()
        }.first{
            it is Class<*> &&IPresenter::class.java.isAssignableFrom(it)
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

    override fun onResume() {
        super.onResume()
        presenter.onResume()
    }

    override fun onPause() {
        super.onPause()
        presenter.onPause()
    }

    override fun onStop() {
        super.onStop()
        presenter.onStop()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        presenter.onSaveInstanceState(outState)
    }

    override fun onViewStateRestored(savedInstanceState: Bundle?) {
        super.onViewStateRestored(savedInstanceState)
        presenter.onViewStateRestored(savedInstanceState)
    }

    override fun onDestroy() {
        super.onDestroy()
        presenter.onDestroy()
    }

}
class Main2Fragment:MainFragment<MainPresenter>(),View.OnClickListener{
    override fun onClick(v: View?) {

    }

}
open class MainFragment<out  Presenter:BasePresenter<MainFragment<Presenter>>> :BaseFragment<Presenter>(),CancellationSignal.OnCancelListener{
    override fun onCancel() {

    }

}
class MainPresenter :BasePresenter<MainFragment<MainPresenter>>()