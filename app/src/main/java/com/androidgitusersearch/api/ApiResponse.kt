package com.androidgitusersearch.api

/**
 * Created by Royal Lachinov on 2020-12-24.
 */

class ApiResponse constructor() {

    var successBody: Any? = null
    var errorBody: String? = null

    constructor(successBody: Any) : this() {
        this.successBody = successBody
        this.errorBody = null
    }

    constructor(errorBody: String) : this() {
        this.errorBody = errorBody
        this.successBody = null
    }
}