package app.itakura.reirei.databaserealm

import android.widget.ImageView
import io.realm.RealmObject

open class Memo(
    open var title: String = "",
    open var detail: String = "",
    open var Lat: Double = 0.0,
    open var Long: Double = 0.0
):RealmObject()





