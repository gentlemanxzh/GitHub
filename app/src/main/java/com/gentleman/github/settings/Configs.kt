package com.gentleman.github.settings

import com.gentleman.common.log.logger
import com.gentleman.github.AppContext
import com.gentleman.github.utils.deviceId

object Configs {

    object Account{

        val SCOPES = listOf("user", "repo", "notifications", "gist", "admin:org")
        const val clientId  = "9efbe3c13dddec5dbb99"
        const val clientSecret  = "af7548ab5ae5d26cba97d4cb605441fece8529e0"
        const val note = "kotliner.cn"
        const val noteUrl = "http://www.kotliner.cn"

        val fingerPrint by lazy {
            (AppContext.deviceId + clientId).also { logger.info("fingerPrint: "+it) }
        }
    }

}