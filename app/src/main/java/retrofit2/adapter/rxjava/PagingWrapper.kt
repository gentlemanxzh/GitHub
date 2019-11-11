package retrofit2.adapter.rxjava

/**
 * @author Gentleman
 * @date 2019/11/11
 * Description
 */

abstract class PagingWrapper<T>{

    abstract fun getElements(): List<T>

    val paging by lazy {
        GitHubPaging<T>().also { it.addAll(getElements()) }
    }
}