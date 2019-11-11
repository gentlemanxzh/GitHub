package com.gentleman.common.unused

import java.io.File
import java.io.FileInputStream
import java.lang.Exception
import java.net.URL
import java.util.*
import kotlin.reflect.KClass
import kotlin.reflect.KProperty
import kotlin.reflect.full.isSuperclassOf

class PropertiesDelegate(private val path: String) {
    private lateinit var url: URL

    //懒加载，线程安全
    private val properties: Properties by lazy {
        val prop = Properties()
        url = try {
            //读取文件流，use操作会自动把流操作后关闭流
            javaClass.getResourceAsStream(path).use {
                prop.load(it)
            }
            javaClass.getResource(path)
        } catch (e: Exception) {
            try {
                ClassLoader.getSystemClassLoader().getResourceAsStream(path).use {
                    prop.load(it)
                }
                ClassLoader.getSystemClassLoader().getResource(path)
            } catch (e: Exception) {
                FileInputStream(path).use {
                    prop.load(it)
                }
                URL("file://${File(path).canonicalPath}")
            }
        }
        prop
    }

    operator fun <T> getValue(thisRef: Any, property: KProperty<*>): T {
        val value = properties[property.name]
        //反射
        val classOfT = property.returnType.classifier as KClass<*>
        return when {
            Boolean::class == classOfT -> value.toString().toBoolean()
            //如果是Number的子类
            Number::class.isSuperclassOf(classOfT) -> {
                //Java反射
                classOfT.javaObjectType.getDeclaredMethod("parse${classOfT.simpleName}", String::class.java)
                    .invoke(null, value)
            }
            String::class == classOfT -> value
            else -> throw IllegalArgumentException("Unsupported type.")
        } as T
    }
}

abstract class AbsProperties(path: String) {
    protected val prop = PropertiesDelegate(path)
}