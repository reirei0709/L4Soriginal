package app.itakura.reirei.databaserealm

import android.widget.ImageView
import io.realm.RealmObject
import io.realm.annotations.PrimaryKey
import java.util.*

open class Memo(
    @PrimaryKey open var id: String = UUID.randomUUID().toString(),
    open var name: String = "",
    open var memo:String = "",
    open var url: String = "",
    open var Lat: Double = 0.0,
    open var Long: Double = 0.0
):RealmObject()





