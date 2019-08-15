package com.gentleman.github

import com.gentleman.common.Preference

object Settings{
    var email:String by Preference(AppContext,"email","")
    var password:String by Preference(AppContext,"password","")
}