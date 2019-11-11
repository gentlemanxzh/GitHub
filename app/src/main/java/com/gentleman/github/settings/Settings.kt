package com.gentleman.github.settings

import com.gentleman.common.sharedpreferences.Preference
import com.gentleman.github.AppContext

object Settings{
    var email:String by Preference(AppContext, "email", "")
    var password:String by Preference(AppContext, "password", "")
}