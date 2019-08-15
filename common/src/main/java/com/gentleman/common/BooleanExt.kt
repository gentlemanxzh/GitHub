package com.gentleman.common

//密封类,限制在when表达式必须列出该类的所有直接子类,协变
//因为这里WithData子类是携带状态data的，所以这里使用密封类，如果都是像Otherwise不携带状态的就可以使用枚举
sealed class BooleanExt<out T>

//这里相当于单例
object Otherwise : BooleanExt<Nothing>()

class WithData<T>(val data: T) : BooleanExt<T>()


inline fun <T : Any> Boolean.yes(block: () -> T): BooleanExt<T> =
    when {
        this -> WithData(block())
        else -> Otherwise
    }

inline fun <T : Any> Boolean.no(block: () -> T): BooleanExt<T> =
    when {
        this -> Otherwise
        else -> WithData(block())
    }

inline fun <T> BooleanExt<T>.otherwise(block: () -> T): T =
    when (this) {
        is Otherwise -> block()
        is WithData -> this.data
    }
