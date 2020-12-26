package com.androidgitusersearch

import java.io.File

/**
 * Created by Royal Lachinov on 2020-12-24.
 */

object JsonConverterUnitTest {

    fun getJsonFile(path: String): String {
        val uri = javaClass.classLoader?.getResource(path)
        val file = File(uri?.path!!)
        return String(file.readBytes())
    }

}