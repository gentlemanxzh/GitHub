package com.gentleman.github.network.entities

import com.gentleman.common.anno.PoKo
import retrofit2.adapter.rxjava.PagingWrapper

/**
 * @author Gentleman
 * @date 2019/11/11
 * Description
 */

@PoKo
data class SearchRepositories(var total_count: Int,
                              var incomplete_results: Boolean,
                              var items: List<Repository>) : PagingWrapper<Repository>() {

    override fun getElements() = items
}