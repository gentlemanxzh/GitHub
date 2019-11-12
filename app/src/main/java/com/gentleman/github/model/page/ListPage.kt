package com.gentleman.github.model.page

import com.gentleman.common.log.logger
import retrofit2.adapter.rxjava.GitHubPaging
import rx.Observable

abstract class ListPage<DataType> : DataProvider<DataType> {
    companion object {
        const val PAGE_SIZE = 20
    }

    var currentPage = 1
        private set

    val data:GitHubPaging<DataType> = GitHubPaging<DataType>()

    fun loadMore() = getData(currentPage+1)
        .doOnNext {
            currentPage+1

        }
        .doOnError {
            logger.error("loadMore Error",it)
        }
        .map {
            data.mergeData(it)

        }

    fun loadFromFirst(pageCount:Int = currentPage)=
            Observable.range(1,currentPage)
                .concatMap {
                    getData(it)
                }
                .doOnError {
                    logger.error("loadFormFirst,pageCount=$pageCount",it)
                }
                .reduce{acc,page->
                    acc.mergeData(page)
                }.doOnNext {
                    data.clear()
                    data.mergeData(it)
                }
}