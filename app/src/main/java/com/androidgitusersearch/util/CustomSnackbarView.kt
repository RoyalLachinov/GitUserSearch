package com.androidgitusersearch.util

import android.content.Context
import android.util.AttributeSet
import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import com.androidgitusersearch.R
import com.google.android.material.snackbar.ContentViewCallback

/**
 * Created by Royal Lachinov on 2020-12-24.
 */

class CustomSnackbarView @JvmOverloads constructor(
    context: Context,
    attributeSet: AttributeSet? = null,
    defaultStyle: Int = 0
) : ConstraintLayout(context, attributeSet, defaultStyle), ContentViewCallback {

    init {
        View.inflate(context, R.layout.item_repo_detail, this)
    }

    override fun animateContentIn(delay: Int, duration: Int) {
        // TODO("Use some animation")
    }

    override fun animateContentOut(delay: Int, duration: Int) {
        // TODO("Use some animation")
    }

}