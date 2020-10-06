package app.itakura.reirei.database

import android.app.Application
import android.os.Parcel
import android.os.Parcelable
import io.realm.Realm
import io.realm.RealmConfiguration

class RealmMemoApplication() : Application() {

    override fun onCreate() {
        super.onCreate()

        Realm.init(this)
        val realmConfig   = RealmConfiguration.Builder()
            .deleteRealmIfMigrationNeeded()
            .build()
        Realm.setDefaultConfiguration(realmConfig)

    }

}
