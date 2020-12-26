package com.androidgitusersearch.util

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.androidgitusersearch.R
import com.google.android.material.snackbar.BaseTransientBottomBar

/**
 * Created by Royal Lachinov on 2020-12-24.
 */


class CustomSnackbar(
    parent: ViewGroup,
    content: CustomSnackbarView
) : BaseTransientBottomBar<CustomSnackbar>(parent, content, content) {

    init {
        getView().setBackgroundColor(ContextCompat.getColor(view.context, R.color.light_grey))
        getView().setPadding(10, 10, 10, 10)
    }

    companion object {

        fun make(viewGroup: ViewGroup, updateDate: String, starCount: Int, forkCount: Int): CustomSnackbar {
            val customView = LayoutInflater.from(viewGroup.context).inflate(
                R.layout.layout_snack_bar, viewGroup, false
            ) as CustomSnackbarView
            val tvUpdate = customView.findViewById<TextView>(R.id.text_view_last_update_value)
            val tvStarCount = customView.findViewById<TextView>(R.id.text_view_stars_value)
            val tvForksCount = customView.findViewById<TextView>(R.id.text_view_forks_value)

            tvUpdate.text = updateDate
            tvStarCount.text = starCount.toString()
            tvForksCount.text = forkCount.toString()

            return CustomSnackbar(viewGroup, customView)
        }

    }

}