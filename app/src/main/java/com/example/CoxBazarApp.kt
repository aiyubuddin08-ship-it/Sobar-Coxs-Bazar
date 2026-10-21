package com.example

import android.app.Application
import com.example.data.local.CoxBazarDatabase
import com.example.data.repository.CoxRepository

class CoxBazarApp : Application() {

    lateinit var database: CoxBazarDatabase
        private set

    lateinit var repository: CoxRepository
        private set

    override fun onCreate() {
        super.onCreate()
        instance = this
        database = CoxBazarDatabase.getInstance(this)
        repository = CoxRepository(database)
    }

    companion object {
        lateinit var instance: CoxBazarApp
            private set
    }
}
