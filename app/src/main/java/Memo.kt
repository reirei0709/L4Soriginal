package app.itakura.reirei.databaserealm

import android.widget.ImageView
import io.realm.RealmObject

open class Memo(
    open var id: String = "",
    open var name: String = "",
    open var memo:String = "",
    open var url: String = "",
    open var Lat: Double = 0.0,
    open var Long: Double = 0.0
):RealmObject()





