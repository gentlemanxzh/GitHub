package com.gentleman.github

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.gentleman.mvp.impl.Main2Fragment

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val mainFragment = Main2Fragment()
        Log.d("MainActivity",mainFragment.toString())
    }
}
