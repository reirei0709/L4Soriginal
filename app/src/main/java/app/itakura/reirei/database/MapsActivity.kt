package app.itakura.reirei.database


import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.*


class MapsActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap
    private var latlng: LatLng? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_maps)
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)



    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        val latitude = 35.68
        val longitude = 139.76

        latlng = LatLng(latitude, longitude)

        // Add a marker in Sydney and move the camera
        //val sydney = LatLng(-34.0, 151.0)
       // mMap.addMarker(MarkerOptions().position(sydney).title("Marker in Sydney"))
        //mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney))

        setIcon(latitude, longitude);

        //googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(LatLng(35.68, 139.76), 12f));

        val cUpdate = CameraUpdateFactory.newLatLngZoom(
           LatLng(35.68, 139.76), 12f
       )
       mMap.moveCamera(cUpdate)
    }

    private fun setMarker(latitude: Double, longitude: Double) {
        val markerOptions = MarkerOptions()
        latlng?.let { markerOptions.position(it) }
        markerOptions.title("Marker")
        mMap.addMarker(markerOptions)

        // ズーム
        zoomMap(latitude, longitude)
    }


    private fun setIcon(latitude: Double, longitude: Double) {

        // マップに貼り付ける BitmapDescriptor生成
        // 画像は自分で適当に用意します。ここではmipmapから持ってきましたが
        val descriptor =
            BitmapDescriptorFactory.fromResource(R.drawable.star)

        // 貼り付設定
        val overlayOptions = GroundOverlayOptions()
        overlayOptions.image(descriptor)

        //　public GroundOverlayOptions anchor (float u, float v)
        // (0,0):top-left, (0,1):bottom-left, (1,0):top-right, (1,1):bottom-right
        overlayOptions.anchor(0.5f, 0.5f)

        // 張り付け画像の大きさ メートル単位
        // public GroundOverlayOptions	position(LatLng location, float width, float height)
        overlayOptions.position(latlng, 300f, 300f)

        // マップに貼り付け・アルファを設定
        val overlay = mMap.addGroundOverlay(overlayOptions)
        // ズーム
        zoomMap(latitude, longitude)

        // 透明度
        overlay.transparency = 0.0f
    }


    private fun zoomMap(latitude: Double, longitude: Double) {
        // 表示する東西南北の緯度経度を設定
        val south = latitude * (1 - 0.00005)
        val west = longitude * (1 - 0.00005)
        val north = latitude * (1 + 0.00005)
        val east = longitude * (1 + 0.00005)

        // LatLngBounds (LatLng southwest, LatLng northeast)
        val bounds = LatLngBounds.builder()
            .include(LatLng(south, west))
            .include(LatLng(north, east))
            .build()
        val width = resources.displayMetrics.widthPixels
        val height = resources.displayMetrics.heightPixels

        // static CameraUpdate.newLatLngBounds(LatLngBounds bounds, int width, int height, int padding)
        mMap.moveCamera(CameraUpdateFactory.newLatLngBounds(bounds, width, height, 0))
    }








}
