package material.kcci.mystudio;

/**
 * Created by db2 on 2017-05-17.
 */

import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import static android.app.Activity.RESULT_OK;

public class Fragment1 extends Fragment {

    String _intputDestName;/////
    SupportMapFragment mapFragment;
    GoogleMap map;
    private CompassView mCompassView;
    private SensorManager mSensorManager;
    private boolean mCompassEnabled;
    private ViewGroup root_page;
    private Geocoder geocoder;
    private EditText input_destination;
    private TextView searchName;
    private TextView searchAddr;
    private Button findBtn;
    private Button startBtn;
    private List<Address> list;
    int RESULT_SPEECH = 1;
    Button button;
    Intent i;



    public Fragment1() {
        Log.d("TAG","This_is_Fragment1");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container, Bundle savedInstanceState) {

        root_page = (ViewGroup) inflater.inflate(R.layout.fragment_fragment1,container,false);
        geocoder = new Geocoder(getActivity());
        input_destination = (EditText) root_page.findViewById(R.id.editText);
        searchName = (TextView) root_page.findViewById(R.id.textView3);
        searchAddr = (TextView) root_page.findViewById(R.id.textView2);
        findBtn = (Button) root_page.findViewById(R.id.button2);
        startBtn = (Button) root_page.findViewById(R.id.mapStart);

        findBtn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if (v.getId() == R.id.button2)
                {
                    i = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
                    i.putExtra(RecognizerIntent.EXTRA_CALLING_PACKAGE, getActivity().getPackageName());
                    i.putExtra(RecognizerIntent.EXTRA_LANGUAGE, "ko-KR");
                    i.putExtra(RecognizerIntent.EXTRA_PROMPT, "말씀하세요");
                    try
                    {
                        startActivityForResult(i, RESULT_SPEECH);
                    } catch (ActivityNotFoundException e)
                    {
                        Toast.makeText(getActivity(), "지원하지 않습니다.", Toast.LENGTH_LONG).show();
                        e.getStackTrace();
                    }
                }
            }
        });



        //endregion

        //region onclick->DB_INSERT + 현재 위치 표시
        startBtn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                String name = searchName.getText().toString();
                String address = searchAddr.getText().toString();
                insertToDatabase(name,address);

                list = null;
                list=checkaddrss();
                if (list != null) {
                    if (list.size() == 0) {
                        Toast.makeText(getActivity(),"해당되는 주소 정보는 없습니다",Toast.LENGTH_SHORT).show();
                    } else {
//                        // 해당되는 주소로 인텐트 날리기
//                        Address addr = list.get(0);
//                        Log.d("Address_TAG",addr.toString());
//                        double lat = addr.getLatitude();
//                        double lon = addr.getLongitude();
//                        showMyLocationMarker(lat,lon); //double 형으로 매개변수 넘김
                    }
                }
                requestMyLocation();
            }
        });
        //endregion

        mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap googleMap) {
                map = googleMap;
                map.moveCamera(CameraUpdateFactory.newLatLng(new LatLng(37.555744, 126.970431)));
                map.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng,15));
                map.setMyLocationEnabled(true);
            }
        });

        try {
            MapsInitializer.initialize(getActivity());
        } catch(Exception e) {
            e.printStackTrace();
        }


        mSensorManager = (SensorManager) getActivity().getSystemService(Context.SENSOR_SERVICE);
        boolean sideBottom = true;
        mCompassView = new CompassView(getActivity());
        mCompassView.setVisibility(View.VISIBLE);

        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.WRAP_CONTENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT);
        params.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
        params.addRule(sideBottom ? RelativeLayout.ALIGN_PARENT_BOTTOM : RelativeLayout.ALIGN_PARENT_TOP);

        ((ViewGroup)mapFragment.getView()).addView(mCompassView, params);
        mCompassEnabled = true;

        return root_page;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && (requestCode == RESULT_SPEECH))
        {
            ArrayList<String> sstResult = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
            //data.getString() 호출로 음성 인식 결과를 Arraylist로 받음

            String result_sst = sstResult.get(0);
            //결과중 음성과 가장 비슷한 단어부터 0번째 문자열에 저장

            input_destination.setText("" + result_sst);

            list = null;
            list = checkaddrss();
            if (list != null)
            {
                if (list.size() == 0)
                {
                    Toast.makeText(getActivity(), "해당되는 주소 정보는 없습니다", Toast.LENGTH_SHORT).show();
                } else
                {
                    Address addr = list.get(0);
                    String findAddr = addr.getAddressLine(0).toString();
                    searchAddr.setText(findAddr);
                    Log.d("Address_TAG",addr.toString());
                    double lat = addr.getLatitude();
                    double lon = addr.getLongitude();
                    showMyLocationMarker(lat,lon); //double 형으로 매개변수 넘김
                }
            }
        }


    }
    //region insertToDatabase
    private void insertToDatabase(String name,String addr){

        class InsertData extends AsyncTask<String, Void, String>
        {
            ProgressDialog loading;


            @Override
            protected void onPreExecute() {
                super.onPreExecute();

                loading = ProgressDialog.show(getActivity(),"Please Wait", null, true, true);
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);

                loading.dismiss();
                Toast.makeText(getActivity(),s,Toast.LENGTH_LONG).show();
            }

            @Override
            protected String doInBackground(String... params) {

                try{
                    String name = (String)params[0];
                    String addr = (String)params[1];

                    String link="http://118.91.118.27/CarCat/insert.php";

                    String data  = URLEncoder.encode("name", "UTF-8") + "="
                            + URLEncoder.encode(name, "UTF-8");
                    data += "&" + URLEncoder.encode("address", "UTF-8") + "="
                            + URLEncoder.encode(addr, "UTF-8");

                    URL url = new URL(link);
                    URLConnection conn = url.openConnection();

                    conn.setDoOutput(true);
                    OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());

                    wr.write( data );
                    wr.flush();

                    BufferedReader reader = new BufferedReader(
                            new InputStreamReader(conn.getInputStream()));

                    StringBuilder sb = new StringBuilder();
                    String line = null;

                    // Read Server Response
                    while((line = reader.readLine()) != null)
                    {
                        sb.append(line);
                        break;
                    }
                    return sb.toString();
                }
                catch(Exception e){

                    return new String("Exception: " + e.getMessage());
                }

            }
        }

        InsertData task = new InsertData();
        task.execute(name,addr);
    }
    //endregion

    //region MAP
    private void requestMyLocation() {
        LocationManager manager =
                (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);

        try {
            long minTime = 10000;
            float minDistance = 0;
            manager.requestLocationUpdates(LocationManager.GPS_PROVIDER,minTime,minDistance,new LocationListener()
                    {
                        @Override
                        public void onLocationChanged(Location location) {
//                            showCurrentLocation(location);
                        }

                        @Override
                        public void onStatusChanged(String provider, int status, Bundle extras) {
                        }

                        @Override
                        public void onProviderEnabled(String provider) {
                        }

                        @Override
                        public void onProviderDisabled(String provider) {
                        }
                    }
            );

            Location lastLocation = manager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            if (lastLocation != null) {
                showCurrentLocation(lastLocation);
            }

            manager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER,minTime,minDistance,new LocationListener()
                    {
                        @Override
                        public void onLocationChanged(Location location) {
//                            showCurrentLocation(location);
                        }

                        @Override
                        public void onStatusChanged(String provider, int status, Bundle extras) {

                        }

                        @Override
                        public void onProviderEnabled(String provider) {

                        }

                        @Override
                        public void onProviderDisabled(String provider) {

                        }
                    }
            );
        } catch(SecurityException e) {
            e.printStackTrace();
        }
    }

    private void showCurrentLocation(Location location)
    {
        LatLng curPoint = new LatLng(location.getLatitude(), location.getLongitude());
        map.animateCamera(CameraUpdateFactory.newLatLngZoom(curPoint, 15));
    }


    private void showMyLocationMarker(double lat, double lon)
    {
        map.clear();
        LatLng findDest = new LatLng(lat,lon);
        map.addMarker(new MarkerOptions().position(findDest).snippet("Lat:"
                + findDest.latitude
                + "Lng:" + findDest.longitude).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE))
                .title("목적지"));
        map.moveCamera(CameraUpdateFactory.newLatLng(findDest));
    }

    @Override
    public void onPause() {
        super.onPause();

        if (map != null) {
            map.setMyLocationEnabled(false);
        }

        if(mCompassEnabled) {
            mSensorManager.unregisterListener(mListener);
        }
    }

    @Override
    public void onResume() {
        super.onResume();

        if (map != null) {
            map.setMyLocationEnabled(true);
        }

        if(mCompassEnabled) {
            mSensorManager.registerListener(mListener, mSensorManager.getDefaultSensor(Sensor.TYPE_ORIENTATION), SensorManager.SENSOR_DELAY_UI);
        }
    }

    private final SensorEventListener mListener = new SensorEventListener() {
        private int iOrientation = -1;

        public void onAccuracyChanged(Sensor sensor, int accuracy) {

        }

        public void onSensorChanged(SensorEvent event) {
            if (iOrientation < 0) {
                iOrientation = ((WindowManager) getActivity().getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay().getRotation();
            }
            mCompassView.setAzimuth(event.values[0] + 90 * iOrientation);
            mCompassView.invalidate();
        }
    };

    private List<Address> checkaddrss()
    {
        _intputDestName = input_destination.getText().toString();
        List<Address> list = null;

        String destination = input_destination.getText().toString();
        Log.d("destination_TAT",destination);
        searchName.setText(destination);
        try {
            list = geocoder.getFromLocationName(destination,10);
        } catch (IOException e) {
            e.printStackTrace();
            Log.e("test","입출력 오류 - 서버에서 주소변환시 에러발생");
        }
        return list;
    }

}