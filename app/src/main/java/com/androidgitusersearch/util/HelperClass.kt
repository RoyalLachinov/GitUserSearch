package com.androidgitusersearch.util

import android.content.Context
import android.view.View
import android.view.inputmethod.InputMethodManager
import java.text.SimpleDateFormat
import java.util.*

/**
 * Created by Royal Lachinov on 2020-12-24.
 */

object HelperClass {

    fun View.show() {
        this.visibility = View.VISIBLE
    }

    fun View.hide() {
        this.visibility = View.GONE
    }

    fun View.hideKeyboard() {
        val imm = this.context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(this.windowToken, 0)
    }

    fun View.showKeyboard() {
        this.requestFocus()
        val imm: InputMethodManager? =
            this.context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager?
        imm!!.showSoftInput(this, InputMethodManager.SHOW_IMPLICIT)
    }



    fun convertUpdateDate(time: String): String {
        val simpleDateFormat1 = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'")

        simpleDateFormat1.timeZone = TimeZone.getTimeZone("UTC")
        val parse = simpleDateFormat1.parse(time)

        val simpleDateFormat = SimpleDateFormat("MMM d, yyyy hh:mm:ss a")

        return simpleDateFormat.format(parse!!).replace("a.m.", "AM").replace("p.m.","PM")
    }
}