package pl.edu.uj.ii.skwarczek.productlist

import android.app.Application
import io.realm.Realm
import io.realm.RealmConfiguration

class InitRealm : Application() {

    override fun onCreate(){
        super.onCreate()
        Realm.init(this)
        val config = RealmConfiguration.Builder()
            .allowWritesOnUiThread(true)
            .allowQueriesOnUiThread(true)
            .build()
        Realm.setDefaultConfiguration(config)
    }
}