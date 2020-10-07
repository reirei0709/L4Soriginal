package app.itakura.reirei.databaserealm.app.itakura.reirei.databas
//
//import android.content.Context
//import android.location.Geocoder
//
//import android.os.AsyncTask
//import java.util.*
//
//
//abstract class ObtainGeoSpotTask(context: Context?, listener: OnObtainGeoSpotListener?) :
//    AsyncTask<String?, Void?, GeoSpot?>() {
//    /** コンテキスト  */
//    private var _context: Context? = null
//
//    /**
//     * 地点獲得イベントのインターフェースクラス
//     *
//     */
//    interface OnObtainGeoSpotListener {
//        fun onObtainGeoSpot(geoSpot: GeoSpot?)
//    }
//
//    /** リスナー  */
//    private var _listener: OnObtainGeoSpotListener? = null
//
//    // バックグラウンドで実行する処理
//    protected fun doInBackground(vararg extraTexts: String): GeoSpot? {
//
//
//        // パラメータが無効である場合は未処理
//        if (extraTexts == null || extraTexts.size <= 0) {
//            return null
//        }
//        val geocoder = Geocoder(_context, Locale.getDefault())
//        val extraText = extraTexts[0]
//
//        // 短縮URL取得
//        val shortUrl: String = GoogleMapOperator.obtainShortUrlFromTextExtra(extraText)
//
//        // 地点情報取得
//        var geoSpot: GeoSpot = GoogleMapOperator.obtainSpotFromShortUrl(shortUrl)
//        if (geoSpot == null) {
//            Log.e(TAG, "EXTRA_TEXT:$extraText")
//            if (!StringUtil.isEmpty(extraText)) {
//                var locatioName: String = Constants.STRING_EMPTY
//                val params = extraText.split("\n|,".toRegex()).toTypedArray()
//                for (i in params.indices.reversed()) {
//                    locatioName = params[i]
//                    Log.e(TAG, "locatioName:$locatioName")
//                    if (!locatioName.matches(Constants.MATCH_URL)) {
//                        Log.e(TAG, "MATCH_URL:$locatioName")
//                        geoSpot =
//                            GoogleMapOperator.obtainSpotFromLocationName(geocoder, locatioName)
//                        if (geoSpot != null) {
//                            break
//                        }
//                    }
//                }
//            }
//        }
//        return geoSpot
//    }
//
//    // メインスレッドで実行する処理
//    override fun onPostExecute(geoSpot: GeoSpot?) {
//        _listener?.onObtainGeoSpot(geoSpot)
//    }
//
//    companion object {
//        private val TAG = ObtainGeoSpotTask::class.java.simpleName
//    }
//
//    // コンストラクタ
//    init {
//        _context = context
//        _listener = listener
//    }
//}