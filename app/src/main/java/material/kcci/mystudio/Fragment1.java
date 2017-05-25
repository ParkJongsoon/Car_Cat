package material.kcci.mystudio;

/**
 * Created by db2 on 2017-05-17.
 */
//너무 너무 무서워


import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.sackcentury.shinebuttonlib.ShineButton;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static android.app.Activity.RESULT_OK;

public class Fragment1 extends Fragment {

    String _intputDestName;
    SupportMapFragment mapFragment;
    GoogleMap map;
    private ViewGroup root_page;
    private Geocoder geocoder;
    private TextView searchName;
    private TextView searchAddr;
    private ShineButton findBtn;
    private ShineButton startBtn;
    private List<Address> list;
    int RESULT_SPEECH = 1;
    Intent i;
    Map mymap;
    double lat;
    double lon;

    public Fragment1()
    {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container, Bundle savedInstanceState) {
        root_page = (ViewGroup) inflater.inflate(R.layout.fragment_fragment1,container,false);
        geocoder = new Geocoder(getActivity());
        searchName = (TextView) root_page.findViewById(R.id.textView3);
        searchAddr = (TextView) root_page.findViewById(R.id.textView2);
        findBtn = (ShineButton) root_page.findViewById(R.id.button2);
        startBtn = (ShineButton) root_page.findViewById(R.id.mapStart);
        mymap = new Map(getActivity(),map);

        findBtn.setOnClickListener(new View.OnClickListener()
        {

            @Override
            public void onClick(View v)
            {
                findBtn.setChecked(false);

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

        startBtn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                startBtn.setChecked(false);

                String name = searchName.getText().toString();
                String address = searchAddr.getText().toString();

                list = null;
                list = checkaddrss();
                if (list != null) {
                    if (list.size() == 0) {
                        Toast.makeText(getActivity(),"해당되는 주소 정보는 없습니다",Toast.LENGTH_SHORT).show();
                    } else {
                        Address addr = list.get(0);
                        double lat = addr.getLatitude();
                        double lon = addr.getLongitude();
                        mymap.showMyLocationMarker(lat,lon);
                        Toast.makeText(getActivity(),"Success",Toast.LENGTH_SHORT).show();
                    }
                }
                ConnectPHP conPhp = new ConnectPHP();
                conPhp.insertToDatabase(name,address);
                mymap.requestMyLocation();
            }
        });

        mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap googleMap) {
                map = googleMap;
                LatLng initialization = new LatLng(37.555744, 126.970431);
                map.animateCamera(CameraUpdateFactory.newLatLngZoom(initialization, 11));
                map.setMyLocationEnabled(true);
            }
        });

        try {
            MapsInitializer.initialize(getActivity());
        } catch(Exception e) {
            e.printStackTrace();
        }

        return root_page;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        mymap = new Map(getActivity(),map);
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && (requestCode == RESULT_SPEECH))
        {
            //data.getString() 호출로 음성 인식 결과를 Arraylist로 받음
            ArrayList<String> sstResult = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);

            //결과중 음성과 가장 비슷한 단어부터 0번째 문자열에 저장
            String result_sst = sstResult.get(0);

            searchName.setText(result_sst);

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
                    lat = addr.getLatitude();
                    lon = addr.getLongitude();
                    mymap.showMyLocationMarker(lat,lon);
                }
            }
        }
    }

    private List<Address> checkaddrss()
    {
        _intputDestName = searchName.getText().toString();
        List<Address> list = null;
        String destination = _intputDestName;
        try {
            list = geocoder.getFromLocationName(destination,10);
            Log.d("destination_TAT",list.toString());
        } catch (IOException e) {
            e.printStackTrace();
            Log.e("test","입출력 오류 - 서버에서 주소변환시 에러발생");
        }
        return list;
    }
}