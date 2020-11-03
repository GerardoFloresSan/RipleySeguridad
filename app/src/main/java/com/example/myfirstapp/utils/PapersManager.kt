package com.example.myfirstapp.utils

import com.example.myfirstapp.data.response.*
import io.paperdb.BuildConfig
import io.paperdb.Paper

object PapersManager {

    var loginAccess: LoginResponse
        set(value) {
            Paper.book(BuildConfig.FLAVOR).write("loginAccess", value)
        }
        get() {
            return Paper.book(BuildConfig.FLAVOR).read("loginAccess", LoginResponse())
        }

    var login: Boolean
        set(value) {
            Paper.book(BuildConfig.FLAVOR).write("login", value)
        }
        get() {
            return Paper.book(BuildConfig.FLAVOR).read("login", false)
        }
}