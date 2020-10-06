package app.itakura.reirei.database
import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import app.itakura.reirei.databaserealm.Memo
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import io.realm.Realm
import io.realm.RealmObject
import io.realm.RealmResults
import kotlinx.android.synthetic.main.activity_g_p_s.*
import kotlinx.android.synthetic.main.activity_main.*


class GPS : AppCompatActivity(), LocationListener,OnMapReadyCallback {

    // lateinit: late initialize to avoid checking null
    private lateinit var locationManager: LocationManager

    private var map: GoogleMap? = null

    private var mylocation: Location? = null

    val realm = Realm.getDefaultInstance()


    fun read(): Memo? {
        return realm.where(Memo::class.java).findFirst()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_g_p_s)

        val MainPage = Intent(application, MainActivity::class.java)

        // Figure out what to do based on the intent type
        if (intent?.type?.startsWith("image/") == true) {
            // Handle intents with image data ...
        } else if (intent?.type == "text/plain") {
            // Handle intents with text ...
            val infoText: String = intent.getStringExtra(Intent.EXTRA_TEXT)
            val infoTextArr = infoText.split("\n")
            // putExtra等々処理を入れる
            MainPage.putExtra("Mode", "0")  // 他アプリから追加
            MainPage.putExtra("shopname", infoTextArr[0])
            MainPage.putExtra("memo", infoTextArr[2])
            MainPage.putExtra("url", infoTextArr[3])

            //お店の情報の登録画面を呼び出す(追加登録)
            startActivity(MainPage)


            startActivity(MainPage)
        }






        //val mapFragment = supportFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        //mapFragment.getMapAsync(this)

        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                1000
            )
        } else {
            locationStart()

            if (::locationManager.isInitialized) {
                locationManager.requestLocationUpdates(
                    LocationManager.GPS_PROVIDER,
                    1000,
                    50f,
                    this
                )
            }


        }
        button.setOnClickListener {

            //val yourspot = "Latitude" + mylocation.getLatitude()
            //val yourspot1 = "Longitude" + mylocation.getLongitude()




            if (mylocation != null) {
                val MainActivity = Intent(this, MainActivity::class.java)

                MainActivity.putExtra("Latitude", mylocation!!.getLatitude())
                MainActivity.putExtra("Longitude", mylocation!!.getLongitude())

                //LatLng(
                // location.getLatitude(),location.getLongitude()
                //)

                startActivity(MainActivity)
            }

        }

        val mapFragment = supportFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

    }


    override fun onMapReady(googleMap: GoogleMap?) {
        map = googleMap
        val memo: Memo? = read()




        if (memo != null) {

            val place = LatLng(
                memo.Lat, memo.Long
            )

            val cameraPosition = CameraPosition.Builder()
                .zoom(16f)
                .target(place)
                .build()
            map?.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition))
        }
        val realmData = realm.where(Memo::class.java).findAll()

        for (memo: Memo in realmData) {


            if (memo != null) {
            }
            val place = LatLng(
                memo.Lat, memo.Long
            )

            map?.addMarker(MarkerOptions().position(place).title(memo.name))

        }
    }


        //val tokyo = LatLng(35.689521, 139.691704)


        private fun locationStart() {
            Log.d("debug", "locationStart()")

            // Instances of LocationManager class must be obtained using Context.getSystemService(Class)
            locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager

            val locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager

            if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                Log.d("debug", "location manager Enabled")
            } else {
                // to prompt setting up GPS
                val settingsIntent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
                startActivity(settingsIntent)
                Log.d("debug", "not gpsEnable, startActivity")
            }

            if (ContextCompat.checkSelfPermission(
                    this,
                    Manifest.permission.ACCESS_FINE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), 1000
                )

                Log.d("debug", "checkSelfPermission false")
                return
            }

            locationManager.requestLocationUpdates(
                LocationManager.GPS_PROVIDER,
                1000, 50f, this
            )

        }

        /**
         * Android Quickstart:
         * https://developers.google.com/sheets/api/quickstart/android
         *
         * Respond to requests for permissions at runtime for API 23 and above.
         * @param requestCode The request code passed in
         * requestPermissions(android.app.Activity, String, int, String[])
         * @param permissions The requested permissions. Never null.
         * @param grantResults The grant results for the corresponding permissions
         * which is either PERMISSION_GRANTED or PERMISSION_DENIED. Never null.
         */
        override fun onRequestPermissionsResult(
            requestCode: Int, permissions: Array<String>, grantResults: IntArray
        ) {
            if (requestCode == 1000) {
                // 使用が許可された
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Log.d("debug", "checkSelfPermission true")

                    locationStart()

                } else {
                    // それでも拒否された時の対応
                    val toast = Toast.makeText(
                        this,
                        "これ以上なにもできません", Toast.LENGTH_SHORT
                    )
                    toast.show()
                }
            }
        }

        override fun onStatusChanged(provider: String, status: Int, extras: Bundle) {
            /* API 29以降非推奨
        when (status) {
            LocationProvider.AVAILABLE ->
                Log.d("debug", "LocationProvider.AVAILABLE")
            LocationProvider.OUT_OF_SERVICE ->
                Log.d("debug", "LocationProvider.OUT_OF_SERVICE")
            LocationProvider.TEMPORARILY_UNAVAILABLE ->
                Log.d("debug", "LocationProvider.TEMPORARILY_UNAVAILABLE")
        }
         */
        }

        override fun onProviderEnabled(provider: String?) {
            TODO("Not yet implemented")
        }

        override fun onProviderDisabled(provider: String?) {
            TODO("Not yet implemented")
        }

        override fun onLocationChanged(location: Location) {

            val yourspot = "Latitude" + location.getLatitude()
            val yourspot1 = "Longitude" + location.getLongitude()

            mylocation = location


        }

    override fun onDestroy() {
        super.onDestroy()
        realm.close()
    }







    fun save(
        Lat: Double,
        Long: Double,
        name: String,
        memo: String,
        url:String
    ) {
        val memo: Memo? = read()

        realm.executeTransaction {
            //if (memo != null) {
            //memo?.Lat = Lat
            //memo?.Long = Long
            //memo?.title = title
            //memo?.detail = detail
            //} else {
            val newMemo: Memo = it.createObject(Memo::class.java)
             newMemo.Lat = Lat
            newMemo.Long = Long
            newMemo.name = name
            newMemo.url = url
            //}
            //Snackbar.make(container, "登録出来ました！！", Snackbar.LENGTH_SHORT).show()


        }

    }



}




