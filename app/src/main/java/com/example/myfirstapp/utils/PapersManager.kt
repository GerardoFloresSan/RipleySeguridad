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

    var username: String
        set(value) {
            Paper.book(BuildConfig.FLAVOR).write("username", value)
        }
        get() {
            return Paper.book(BuildConfig.FLAVOR).read("username", "")
        }

    var parametersAll: ArrayList<Parameter>
        set(value) {
            Paper.book(BuildConfig.FLAVOR).write("parameters", value)
        }
        get() {
            return Paper.book(BuildConfig.FLAVOR).read("parameters", arrayListOf())
        }

    //username
    var login: Boolean
        set(value) {
            Paper.book(BuildConfig.FLAVOR).write("login", value)
        }
        get() {
            return Paper.book(BuildConfig.FLAVOR).read("login", false)
        }
    var device: ArrayList<String>
        set(value) {
            Paper.book(BuildConfig.FLAVOR).write("device", value)
        }
        get() {
            return Paper.book(BuildConfig.FLAVOR).read("device", arrayListOf())
        }

    //i9000s  y el tr150h
}