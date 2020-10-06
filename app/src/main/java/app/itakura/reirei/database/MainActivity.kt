package app.itakura.reirei.database


import android.app.Activity
import android.content.ContentValues
import android.content.Intent
import android.media.MediaScannerConnection.scanFile
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Parcel
import android.os.Parcelable
import android.provider.MediaStore
import android.view.View
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import app.itakura.reirei.databaserealm.Memo
import com.google.android.material.snackbar.Snackbar
import io.realm.Realm
import kotlinx.android.synthetic.main.activity_main.*



class MainActivity() : AppCompatActivity(), Parcelable {


    val realm = Realm.getDefaultInstance()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val memo: Memo? = read()

        saveButton.setOnClickListener {
            val id:String? = intent.getStringExtra("ID")
            val name: String  = titleEditText.text.toString()
            val memo: String = detail.text.toString()
            val url: String = detail.text.toString()



//            val Lat = intent.getDoubleExtra("Latitude", 0.0)
//            val Long = intent.getDoubleExtra("Longitude", 0.0)
//
//            val title = titleEditText.text.toString()
//            val detail = detail.text.toString()
            save(id,name,memo,url)

            Snackbar.make(container, "登録出来ました！！", Snackbar.LENGTH_SHORT).show()

            val intent = Intent(application, GPS::class.java)


            startActivity(intent)

        }


         // set on-click listener for ImageView
        imageView.setOnClickListener{// your code here
            showGallery()
    }


        if (memo != null) {
            titleEditText.setText(memo.name)
            //detail.setText(memo.memo)
            url.setText(memo.url)


        }


    }


    override fun onDestroy() {
        super.onDestroy()
        realm.close()
    }


    fun read(): Memo? {
        return realm.where(Memo::class.java).findFirst()

    }

    fun save(
        id:String?,
        name:String,
        memo:String,
        url:String
//        Lat: Double,
//        Long: Double,
//        title: String,
//        detail: String
    ) {
        val memo: Memo? = read()

        realm.executeTransaction {
            //if (memo != null) {
            //memo?.Lat = Lat
            // memo?.Long = Long
            //memo?.title = title
            //memo?.detail = detail
            //} else {
            val newMemo: Memo = it.createObject(Memo::class.java)
//            newMemo.Lat = Lat
//            newMemo.Long = Long
            //newMemo.id = id
            newMemo.name = name
            //newMemo.memo = memo
            newMemo.url = url
        }
        //Snackbar.make(container, "登録出来ました！！", Snackbar.LENGTH_SHORT).show()


    }


    private var m_uri: Uri? = null
    private val REQUEST_CHOOSER = 1000

    constructor(parcel: Parcel) : this() {
        m_uri = parcel.readParcelable(Uri::class.java.classLoader)
    }


//override fun onCreate(savedInstanceState: Bundle?) {
//super.onCreate(savedInstanceState)
//setContentView(R.layout.activity_main)
// setViews()
//}

// private fun setViews() {
// val button: Button = findViewById(R.id.buttonPanel) as Button
//button.setOnClickListener(button_onClick)
//}



    fun onClick(dialog: android.content.DialogInterface?, which: kotlin.Int) {
        TODO("Not yet implemented")
    }


    private fun showGallery() {

        //カメラの起動Intentの用意
        val photoName = System.currentTimeMillis().toString() + ".jpg"
        val contentValues = ContentValues()
        contentValues.put(MediaStore.Images.Media.TITLE, photoName)
        contentValues.put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg")
        m_uri = this.contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues)
        val intentCamera = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        intentCamera.putExtra(MediaStore.EXTRA_OUTPUT, m_uri)

        // ギャラリー用のIntent作成
        val intentGallery: Intent
        if (Build.VERSION.SDK_INT < 19) {
            intentGallery = Intent(Intent.ACTION_GET_CONTENT)
            intentGallery.type = "image/*"
        } else {
            intentGallery = Intent(Intent.ACTION_OPEN_DOCUMENT)
            intentGallery.addCategory(Intent.CATEGORY_OPENABLE)
            intentGallery.type = "image/jpeg"
        }
        val intent = Intent.createChooser(intentCamera, "画像の選択")
        intent.putExtra(Intent.EXTRA_INITIAL_INTENTS, arrayOf(intentGallery))
        startActivityForResult(intent, REQUEST_CHOOSER)
    }

    override fun onActivityResult(
        requestCode: Int,
        resultCode: Int,
        data: Intent?
    ) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_CHOOSER) {
            if (resultCode != Activity.RESULT_OK) {
                // キャンセル時
                return
            }
            val resultUri = (if (data != null) data.data else m_uri)
                ?: // 取得失敗
                return

            // ギャラリーへスキャンを促す
            scanFile(
                this,
                arrayOf(resultUri.path),
                arrayOf("image/jpeg"),
                null
            )

            // 画像を設定
            imageView.setImageURI(resultUri)
        }
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeParcelable(m_uri, flags)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<MainActivity> {
        override fun createFromParcel(parcel: Parcel): MainActivity {
            return MainActivity(parcel)
        }

        override fun newArray(size: Int): Array<MainActivity?> {
            return arrayOfNulls(size)
        }
    }
}











