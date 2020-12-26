package com.androidgitusersearch.util

import java.io.BufferedReader
import java.io.IOException
import java.io.InputStream
import java.io.InputStreamReader
import java.text.SimpleDateFormat
import java.util.*

/**
 * Created by Royal Lachinov on 2020-12-24.
 */

object ConvertHelper {

    fun convertStreamToString(`is`: InputStream): String {
        val reader = BufferedReader(InputStreamReader(`is`))
        val sb = StringBuilder()
        var line: String? = null
        try {
            while (reader.readLine().also { line = it } != null) {
                sb.append(line).append('\n')
            }
        } catch (e: IOException) {
            e.printStackTrace()
        } finally {
            try {
                `is`.close()
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
        return sb.toString()
    }

    fun convertUpdateDate(time: String): String {
        val simpleDateFormat1 = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'")

        simpleDateFormat1.timeZone = TimeZone.getTimeZone("UTC")
        val parse = simpleDateFormat1.parse(time)

        val simpleDateFormat = SimpleDateFormat("MMM d, yyyy hh:mm:ss a")

        return simpleDateFormat.format(parse!!).replace("a.m.", "AM").replace("p.m.","PM")
    }
}