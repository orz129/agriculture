package tw.tcnr22.q0403;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.StrictMode;
import android.provider.Settings;
import android.text.method.ScrollingMovementMethod;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.ajts.androidmads.fontutils.FontUtils;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Q0403_1 extends AppCompatActivity implements View.OnClickListener {

    private Intent intent = new Intent();
    private ImageView img002,img003;
    private String sqlctl;
    //===================天氣API=====================
    public static String BaseUrl = "https://api.openweathermap.org/";
    public static String AppId = "1115b355ff21fafd11a06d258462fb31";
    public static String lat = "24.1469";
    public static String lon = "120.6839";
    public static String lang = "zh_tw";
    private TextView weatherData;
    private ImageView weatherimg;
    private String iconurl;
    private TextView weatherPic;
    private Typeface typeface;
    private FontUtils fontUtils;
    private LocationManager manager;
    private Location currentLocation;
    //------------------------------------------所需要申請的權限數組
    private static final String[][] permissionsArray = new String[][]{
            {Manifest.permission.ACCESS_FINE_LOCATION, ""},
            {Manifest.permission.WRITE_EXTERNAL_STORAGE, ""},
            {Manifest.permission.CALL_PHONE, ""}};
    private List<String> permissionsList = new ArrayList<String>();
    //------------------------------------------申請權限後的返回碼
    private static final int PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 101;
    private static final int REQUEST_CODE_ASK_PERMISSIONS = 1;
    //------------------------------------------
    private String bestgps;
    //----------------------更新位置頻率的條件--------------
    int minTime = 5000; // 毫秒
    float minDistance = 5; // 公尺
    private String TAG = "tcnr12=>";
    // ----------------------定時更新------------------------
    private Long startTime;
    private Handler handler = new Handler();

    int autotime = 60;// 要幾秒的時間 更新匯入MySQL資料
    int update_time = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        enableStrictMode(this);//---------------------使用暫存堆疊，需要用此方法
        super.onCreate(savedInstanceState);
        setContentView(R.layout.q0100);
        setupViewComponent();
//        //------------------取得裝置權限、位置
//        checkRequiredPermission(this);
//        u_checkgps();
//        //------------------執行天氣API
//        updatePosition();
//        getCurrentData();
//        //------------------
    }

    private void enableStrictMode(Context context) {
        //-------------抓取遠端資料庫設定執行續---------------
        StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder().
                detectDiskReads().
                detectDiskWrites().
                detectNetwork().
                penaltyLog().
                build());
        StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder().
                detectLeakedSqlLiteObjects().
                penaltyLog().
                penaltyDeath().
                build());
    }

    private void setupViewComponent() {
//        //---------------------------------------Intent
//        Intent intent=this.getIntent();
//        String mode_title = intent.getStringExtra("class_title");
//        this.setTitle(mode_title);
        //---------------------------------------宣告
        img002 = (ImageView)findViewById(R.id.q0403_img002);
        img003 = (ImageView)findViewById(R.id.q0403_img003);
        //---------------------------------------監聽
        img002.setOnClickListener(this);
        img003.setOnClickListener(this);

        //===================天氣API=====================
//        weatherData = (TextView) findViewById(R.id.q0403_status);
//        weatherData.setMovementMethod(ScrollingMovementMethod.getInstance());//-----TextView可以上下滾動
//        weatherimg = (ImageView) findViewById(R.id.q0403_img001);

//        fontUtils = new FontUtils();
//        fontUtils.applyFontToView(weatherData, typeface);
//        fontUtils.applyFontToView(weatherPic, typeface);
        //---------------------------------------動態調整layout
        DisplayMetrics displayMetrics = new DisplayMetrics();
        this.getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
//        LinearLayout lay01 = (LinearLayout)findViewById(R.id.q0403_lay01);
//        LinearLayout lay03 = (LinearLayout)findViewById(R.id.q0403_lay03);
//        ImageView img001 = (ImageView)findViewById(R.id.q0403_img001);

//        lay01.getLayoutParams().height=displayMetrics.heightPixels /40*8;
//        lay03.getLayoutParams().height=displayMetrics.heightPixels /40*9;
//        img001.getLayoutParams().height=displayMetrics.heightPixels /40*6;
        img002.getLayoutParams().width=displayMetrics.widthPixels/40*12;
        img003.getLayoutParams().width=displayMetrics.widthPixels/40*12;
//        weatherData.getLayoutParams().width=displayMetrics.widthPixels/10*6;

        //====================設執行緒=======================
//        startTime = System.currentTimeMillis();// 取得目前時間
//        handler.postDelayed(updateTimer, 500);  // 設定Delay的時間
        //-------------------------
    }

    //==========================設定執行續========================
    private Runnable updateTimer = new Runnable() {
        @Override
        public void run() {
            Long spentTime = System.currentTimeMillis() - startTime;
            String hours = String.format("%02d", (spentTime / 1000) / 60 / 60);  // 計算目前已過分鐘數
            String minius = String.format("%02d", ((spentTime / 1000) / 60) % 60);  // 計算目前已過分鐘數
            String seconds = String.format("%02d", (spentTime / 1000) % 60);          // 計算目前已過秒數
            handler.postDelayed(this, autotime * 1000); // 真正延遲的時間
            ++update_time;

        }
    };

//====================intent====================
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.q0403_img002:
                intent.putExtra("class_title", getString(R.string.q0403_b003));
                intent.setClass(Q0403_1.this, Q0403.class);
                break;
            case R.id.q0403_img003:
                intent.putExtra("class_title", getString(R.string.q0403_b006));
                intent.setClass(Q0403_1.this, Q0422.class);
                break;

        }
        startActivity(intent);
    }

//    //===================天氣API=====================
//    private void getCurrentData() {
////********設定轉圈圈進度對話盒*****************************
//        final ProgressDialog pd = new ProgressDialog(Q0403_1.this);
//
//        pd.setProgressStyle(ProgressDialog.STYLE_SPINNER);
//        pd.setTitle("Loading");
//        pd.setMessage("天氣連線中...");
//        pd.setIndeterminate(false);
//        pd.show();
////***************************************************************
///*
//        Retrofit 是一套由 Square 所開發維護，將 RESTful API 寫法規範和模組化的函式庫。
//        底層也使用他們的 okHttp ，okHttp 用法參考 okHttp 章節。
//        Retrofit 預設回傳的處理器是現代化 API 最流行的 JSON，如果你要處理別的要另外實作 Converter。
//        如果需要實作 Server 驗證，建議做好另外接上 okHttpClient 去設 Interceptor。
//        在 Retrofit 1.9.0 的 Interceptor 中能做的有限。
//*/
////***************************************************************
//
//        Retrofit retrofit = new Retrofit.Builder()
//                .baseUrl(BaseUrl)
//                .addConverterFactory(GsonConverterFactory.create())
//                .build();
//        Q0403_1_WeatherServer service = retrofit.create(Q0403_1_WeatherServer.class);
//        Call<Q0403_1_WeatherResponse> call = service.getCurrentWeatherData(lat ,lon ,lang, AppId);
//        call.enqueue(new Callback<Q0403_1_WeatherResponse>() {
//            @Override
//            public void onResponse(@NonNull Call<Q0403_1_WeatherResponse> call, @NonNull Response<Q0403_1_WeatherResponse> response) {
//                if (response.code() == 200) {
//                    Q0403_1_WeatherResponse weatherResponse = response.body();
//                    assert weatherResponse != null;
//                    //getString(R.string.q0403_country) +weatherResponse.sys.country
//                    //getString(R.string.q0403_Pressure) +weatherResponse.main.pressure
//                    //getString(R.string.q0403_weather_lat) + (lat)
//                    //getString(R.string.q0403_weather_lon) + (lon)
//                    String stringBuilder =
//                            getString(R.string.q0403_areaname) + weatherResponse.name +
//                            "\n" +
//                            getString(R.string.q0403_Temperature) +
//                            // ---------------    K°凱氏轉攝氏C°-------------------
//                            (int) (Float.parseFloat("" + weatherResponse.main.temp) - 273.15) +
//                            "\n" +
//                            getString(R.string.q0403_Temperature_Min) +
//                            (int) (Float.parseFloat("" + weatherResponse.main.temp_min) - 273.15) +
//                            "\n" +
//                            getString(R.string.q0403_Temperature_Max) +
//                            (int) (Float.parseFloat("" + weatherResponse.main.temp_max) - 273.15) +
//                            "\n" +
//                            getString(R.string.q0403_Humidity) + weatherResponse.main.humidity ;
//                    weatherData.setText(stringBuilder);  //描述
//                    //====填入座標==============
//                    //weatherLat.setText(getString(R.string.weather_lat) + (lat));
//                    //weatherLon.setText(getString(R.string.weather_lon) + (lon));
//                    //======抓取 Internet 圖片==================
//                    int b_id = weatherResponse.weather.get(0).id;
//                    String b_main = weatherResponse.weather.get(0).main;
//                    String b_description = weatherResponse.weather.get(0).description;
//                    String b_icon = weatherResponse.weather.get(0).icon;
//                    iconurl = "https://openweathermap.org/img/wn/" + b_icon + "@2x.png";
//                    //iconurl = "https://openweathermap.org/img/wn/" + b_icon + "@2x.png";
//                    //https://openweathermap.org/img/wn/50n@2x.png
//                    int cc = 1;
//                    String weather = b_description;
//                    // +"\n" +getString(R.string.w_icon) +"\n" +iconurl
//                    //=========================
//                    TextView content = (TextView)findViewById(R.id.q0403_t003);
//                    content.setText(weather);
//
////*****************使用 AyncTask  非同步執行續**********************************
////                    AsyncTask非同步任務，或稱異步任務，是一個相當常用的類別，是專門用來處理背景任務與UI的類別。
////                    Android 4.0 之後，有明文規定所有的網路行為都不能在主執行緒(Main Thread)執行，
////                    主執行緒又稱UI執行緒(UI Thread)，任何有關UI的東西都在主執行緒中執行，若是你的程式佔據主執行緒很久，使用者體驗會非常的差。
////                    想像一下，按了一個按鈕後，整個App停住五秒會是怎樣的感覺，因此許多耗時的程式建議寫在背景執行，而其中最常見的就是網路的功能。
////                    AsyncTask<Params, Progress, Result>，這是基本的架構，使用泛型來定義參數，
////                    泛型意思是，你可以定義任意的資料型態給他。
////                    Params ： 參數，你要餵什麼樣的參數給它。
////                    Progress ： 進度條，進度條的資料型態要用哪種
////                    Result ： 結果，你希望這個背景任務最後會有什麼樣的結果回傳給你。
////                    此外，AsyncTask會有四個步驟。
////
////                    onPreExecute ： 執行前，一些基本設定可以在這邊做。
////                    doInBackground ： 執行中，在背景做任務。
////                    onProgressUpdate ： 執行中，當你呼叫publishProgress的時候會到這邊，可以告知使用者進度。
////                    onPostExecute ： 執行後，最後的結果會在這邊。
////                    https://developer.android.com/reference/android/os/AsyncTask
//////This class was deprecated in API level 30.
//////Use the standard java.util.concurrent or Kotlin concurrency utilities instead.
//
//                    new AsyncTask<String, Void, Bitmap>() {
//
//                        @Override
//                        protected Bitmap doInBackground(String... strings) {
//                            String url = iconurl;
//                            return getBitmapFromURL(url);
//                        }
//                        @Override
//                        protected void onPostExecute(Bitmap result) //當doinbackground完成後
//                        {
//                            weatherimg.setImageBitmap(result);
//                            pd.cancel();
//                            super.onPostExecute(result);
//                        }
//                    }.execute(iconurl);
////      ***************************************************************************
//                }
//            }
//            @Override
//            public void onFailure(@NonNull Call<Q0403_1_WeatherResponse> call, @NonNull Throwable t) {
//                weatherData.setText(t.getMessage());
//            }
//        });
//    }
//
//    //----------------讀取網路圖片，型態為Bitmap-----------------
//    private static Bitmap getBitmapFromURL(String imageUrl) {
//        try {
//            URL url = new URL(imageUrl);
//            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
//            connection.setDoInput(true);
//            connection.connect();
//            InputStream input = connection.getInputStream();
//            Bitmap bitmap = BitmapFactory.decodeStream(input);
//            return bitmap;
//        } catch (IOException e) {
//            e.printStackTrace();
//            return null;
//        }
//    }
//
//    //============================================
//    // 更新現在的位置
//    private void updatePosition() {
//
//        if (currentLocation == null) {
//            lat = "24.1469";
//            lon = "120.6839";
//
//        } else {
//            lat = Double.toString(currentLocation.getLatitude());
//            lon = Double.toString(currentLocation.getLongitude());
//        }
//    }
//
//    //------------------------------------------------
//    private void u_checkgps() {
//        // 取得系統服務的LocationManager物件
//        manager = (LocationManager) getSystemService(LOCATION_SERVICE);
//        // 檢查是否有啟用GPS
//        if (!manager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
//            // 顯示對話方塊啟用GPS
//            AlertDialog.Builder builder = new AlertDialog.Builder(this);
//            builder.setTitle("定位管理")
//                    .setMessage("GPS目前狀態是尚未啟用.\n"
//                            + "請問你是否現在就設定啟用GPS?")
//                    .setPositiveButton("啟用", new DialogInterface.OnClickListener() {
//                        @Override
//                        public void onClick(DialogInterface dialog, int which) {
//                            // 使用Intent物件啟動設定程式來更改GPS設定
//                            Intent i = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
//                            startActivity(i);
//                        }
//                    })
//                    .setNegativeButton("不啟用", null).create().show();
//        }
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M &&
//                checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) !=
//                        PackageManager.PERMISSION_GRANTED) {
//            requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
//                    PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);
//        }
//    }
//
//    private LocationListener listener = new LocationListener() {
//        @Override
//        public void onLocationChanged(Location location) {
//            currentLocation = location;
//            updatePosition();
//        }
//        @Override
//        public void onStatusChanged(String provider, int status, Bundle extras) {
//        }
//        @Override
//        public void onProviderEnabled(String provider) {
//        }
//        @Override
//        public void onProviderDisabled(String provider) {
//        }
//    };
//
//    //=================================================
//    private void checkRequiredPermission(final Activity activity) {
////        permissionsArray[0][1]=getString(R.string.dialog_msg1);
////        permissionsArray[1][1]=getString(R.string.dialog_msg2);
////        permissionsArray[2][1]=getString(R.string.dialog_msg3);
////        String permission_check= String[i][0] permission;
//        for (int i = 0; i < permissionsArray.length; i++) {
//            if (ContextCompat.checkSelfPermission(activity, permissionsArray[i][0]) != PackageManager.PERMISSION_GRANTED) {
//                permissionsList.add(permissionsArray[i][0]);
//            }
//        }
//        if (permissionsList.size() != 0) {
//            ActivityCompat.requestPermissions(activity, permissionsList.toArray(new
//                    String[permissionsList.size()]), REQUEST_CODE_ASK_PERMISSIONS);
//        }
//    }
//
//    /*** request需要的權限*/
//    private void requestNeededPermission() {
//        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_CODE_ASK_PERMISSIONS);
//    }
//
//    //所需要申請的權限數組
//    @Override
//    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
//        switch (requestCode) {
//            case REQUEST_CODE_ASK_PERMISSIONS:
//                for (int i = 0; i < permissions.length; i++) {
//                    if (grantResults[i] == PackageManager.PERMISSION_GRANTED) {
////                        Toast.makeText(getApplicationContext(), permissionsArray[i][1] + "權限申請成功!", Toast.LENGTH_LONG).show();
//                    } else {
////                        Toast.makeText(getApplicationContext(), "權限被拒絕： " + permissionsArray[i][1], Toast.LENGTH_LONG).show();
//                        //------------------
//                        if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_FINE_LOCATION)) {
//                            Q0403_1_Util.showDialog(this, R.string.q0403_dialog_msg1, android.R.string.ok, new DialogInterface.OnClickListener() {
//                                @Override
//                                public void onClick(DialogInterface dialog, int which) {
//                                    requestNeededPermission();
//                                }
//                            });
//                        } else {
//                            // 否則就直接request
//                            requestNeededPermission();
//                        }
//                    }
//                }
//                break;
//            default:
//                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
//        }
//    }
//
//    //===========================生命週期==========================
//    @Override
//    protected void onResume() {
//        super.onResume();
//        // 取得最佳的定位提供者
//        Criteria criteria = new Criteria();
//        bestgps = manager.getBestProvider(criteria, true);
//
//        try {
//            if (bestgps != null) { // 取得快取的最後位置,如果有的話
//                currentLocation = manager.getLastKnownLocation(bestgps);
//                manager.requestLocationUpdates(bestgps, minTime, minDistance, listener);
//            } else { // 取得快取的最後位置,如果有的話
//                currentLocation = manager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
//                manager.requestLocationUpdates(LocationManager.GPS_PROVIDER,
//                        minTime, minDistance, listener);
//            }
//        } catch (SecurityException e) {
//            Log.e(TAG, "GPS權限失敗..." + e.getMessage());
//        }
//        updatePosition(); // 更新位置
//    }
//
//    @Override
//    protected void onPause() {
//        super.onPause();
//        handler.removeCallbacks(updateTimer);
//
//    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onBackPressed() {
        //super.onBackPressed();  禁用返回鍵
    }
    //------------------------------------------Menu--------------------------------
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.q0403_1main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_settings:
                finish();
                break;

        }
        return super.onOptionsItemSelected(item);
    }

 
}