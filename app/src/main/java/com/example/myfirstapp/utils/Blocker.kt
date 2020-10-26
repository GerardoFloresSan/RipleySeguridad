package com.example.myfirstapp.utils

import android.os.Handler

class Blocker {
    private var isBlockClick = false

    @JvmOverloads
    fun block(blockInMillis: Int = BLOCK_TIME): Boolean {
        if (!isBlockClick) {
            isBlockClick = true
            Handler().postDelayed({ isBlockClick = false }, blockInMillis.toLong())
            return false
        }
        return true
    }

    companion object {
        private const val BLOCK_TIME = 2000
    }
}