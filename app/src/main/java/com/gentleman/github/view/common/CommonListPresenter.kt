package com.gentleman.github.view.common

import com.gentleman.github.model.page.ListPage
import com.gentleman.mvp.impl.BasePresenter
import rx.Subscription

abstract class CommonListPresenter<DataType, out View : CommonListFragment<DataType, CommonListPresenter<DataType, View>>> :
    BasePresenter<View>() {

    abstract val listPage: ListPage<DataType>

    private var firstInView = false
    private val subcriptionList = ArrayList<Subscription>()

    fun initData() {
        listPage.loadFromFirst()
            .subscribe({
                if (it.isEmpty()) view.onDataInitWithNothing() else view.onDataInit(it)
            }, {
                view.onDataInitWithError(it.message ?: it.toString())
            })
            .let { subcriptionList.add(it) }
    }

    fun refreshData() {
        listPage.loadFromFirst()
            .subscribe(
                {
                    if (it.isEmpty()) view.onDataInitWithNothing() else view.onDataRefresh(it)
                },
                {
                    view.onDataRefreshWithError(it.message ?: it.toString())
                })
            .let { subcriptionList.add(it) }
    }

    fun loadMore() {
        listPage.loadMore()
            .subscribe(
                {
                    view.onMoreDataLoaded(it)
                },
                {
                    view.onMoreDataLoadedWithError(it.message ?: it.toString())
                })
            .let { subcriptionList.add(it) }
    }

    override fun onResume() {
        super.onResume()
        if (!firstInView) {
            refreshData()
        }
        firstInView = false
    }

    override fun onDestroy() {
        super.onDestroy()
        subcriptionList.forEach(Subscription::unsubscribe)
        subcriptionList.clear()
    }
}