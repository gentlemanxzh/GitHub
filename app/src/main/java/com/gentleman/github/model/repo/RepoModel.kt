package com.gentleman.github.model.repo

import com.gentleman.github.model.page.ListPage
import com.gentleman.github.network.entities.Repository
import com.gentleman.github.network.entities.User
import com.gentleman.github.network.services.RepositoryApi
import com.gentleman.github.utils.format
import retrofit2.adapter.rxjava.GitHubPaging
import rx.Observable
import java.util.*

class RepoListModel(val owner: User?) : ListPage<Repository>() {
    override fun getData(page: Int): Observable<GitHubPaging<Repository>> {
        return if (owner == null) {
            RepositoryApi.RepositoryService.allRepositories(page, "push<" + Date().format("yyyy-MM-dd"))
                .map { it.paging }
        } else {
            RepositoryApi.RepositoryService.listRepositoriesOfUser(owner.login, page)
        }
    }

}