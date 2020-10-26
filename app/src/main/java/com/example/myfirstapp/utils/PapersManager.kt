package com.example.myfirstapp.utils

import com.example.myfirstapp.data.response.*
import io.paperdb.BuildConfig
import io.paperdb.Paper

object PapersManager {

    var newUsers: Boolean
        set(value) {
            Paper.book(BuildConfig.FLAVOR).write("newUser", value)
        }
        get() {
            return Paper.book(BuildConfig.FLAVOR).read("newUser", true)
        }

    var subsidiary: Subsidiary
        set(value) {
            Paper.book(BuildConfig.FLAVOR).write("subsidiary", value)
        }
        get() {
            return Paper.book(BuildConfig.FLAVOR).read("subsidiary", Subsidiary())
        }

    var parametersAll: ArrayList<Parameter>
        set(value) {
            Paper.book(BuildConfig.FLAVOR).write("parameters", value)
        }
        get() {
            return Paper.book(BuildConfig.FLAVOR).read("parameters", arrayListOf())
        }

    var shoppingCart: CheckPricesResponse
        set(value) {
            Paper.book(BuildConfig.FLAVOR).write("shoppingCart", value)
        }
        get() {
            return Paper.book(BuildConfig.FLAVOR).read("shoppingCart", CheckPricesResponse())
        }

    var locationUser: LocationUser
        set(value) {
            Paper.book(BuildConfig.FLAVOR).write("locationUser", value)
        }
        get() {
            return Paper.book(BuildConfig.FLAVOR).read("locationUser", LocationUser())
        }

    var gpsStatus: Boolean
        set(value) {
            Paper.book(BuildConfig.FLAVOR).write("gpsStatus", value)
        }
        get() {
            return Paper.book(BuildConfig.FLAVOR).read("gpsStatus", false)
        }

    var userLocal: User
        set(value) {
            Paper.book(BuildConfig.FLAVOR).write("user", value)
        }
        get() {
            return Paper.book(BuildConfig.FLAVOR).read("user", User())
        }

    //
}