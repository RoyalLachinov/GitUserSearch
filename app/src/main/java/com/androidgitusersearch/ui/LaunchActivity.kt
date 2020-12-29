package com.androidgitusersearch.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.androidgitusersearch.R

/**
 * Created by Royal Lachinov on 2020-12-24.
 */

class LaunchActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_launch)
        startActivity(Intent(this,MainActivity::class.java))
        finish()
    }
}