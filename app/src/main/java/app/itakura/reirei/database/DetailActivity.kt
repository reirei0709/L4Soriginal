package app.itakura.reirei.database

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import app.itakura.reirei.databaserealm.Memo
import com.google.android.gms.tasks.Task
import io.realm.Realm
import kotlinx.android.synthetic.main.activity_main.*

class DetailActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)



        class DetailActivity : AppCompatActivity() {
//     val realm: Realm = Realm.getDefaultInstance()

            private val realm: Realm by lazy {
                Realm.getDefaultInstance()
            }

            private var memoId: String = ""
            private var memo: Memo? = null




            override fun onCreate(savedInstanceState: Bundle?) {
                super.onCreate(savedInstanceState)
                setContentView(R.layout.activity_detail)

                memoId = intent.getStringExtra("id") ?: ""

                val realmData = getRealmData()

                Log.d("getkey", "Intent")

                Log.d("setText", "setText")

            }

            override fun onResume() {
                super.onResume()
                memo = read(memoId)

            }

            override fun onDestroy() {
                super.onDestroy()
                realm.close()
            }

            private fun read(taskId: String): Memo? {
                return realm.where(Memo::class.java).equalTo("id", taskId).findFirst()
            }


            fun getRealmData(): Memo? {
                // プライマリーキーをもとに該当のデータを取得
                val id = intent.getStringExtra("id")
                val target = realm.where(Memo::class.java)
                    .equalTo("id", id)
                    .findFirst()

                return target

            }


        }

    }
}