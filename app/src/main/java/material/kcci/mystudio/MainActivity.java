package material.kcci.mystudio;

import android.hardware.Camera;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    private final int FRAGMENT1 = 1;
    private final int FRAGMENT2 = 2;

    private Button bt_tab1, bt_tab2;

    private Camera camera = null;
    public static boolean STATE = false;
    TextView valWindow;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        camera = Camera.open(1);
        camera.setFaceDetectionListener(new FaceDetect());


        // 위젯에 대한 참조
        bt_tab1 = (Button)findViewById(R.id.bt_tab1);
        bt_tab2 = (Button)findViewById(R.id.bt_tab2);

        // 탭 버튼에 대한 리스너 연결
        bt_tab1.setOnClickListener(this);
        bt_tab2.setOnClickListener(this);

        //arduino 거리 값에 따른 색상 변경 TextView
        valWindow = (TextView) findViewById(R.id.stateWindow);

        // 임의로 액티비티 호출 시점에 어느 프레그먼트를 프레임레이아웃에 띄울 것인지를 정함
        callFragment(FRAGMENT1);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.bt_tab1 :
                // '버튼1' 클릭 시 '프래그먼트1' 호출
                callFragment(FRAGMENT1);
                break;

            case R.id.bt_tab2 :
                // '버튼2' 클릭 시 '프래그먼트2' 호출
                callFragment(FRAGMENT2);
                break;
        }
    }

    private void callFragment(int frament_no){

        // 프래그먼트 사용을 위해
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

        switch (frament_no){
            case 1:
                // '프래그먼트1' 호출
                Fragment1 fragment1 = new Fragment1();
                transaction.replace(R.id.fragment_container, fragment1);
                transaction.commit();
                break;

            case 2:
                // '프래그먼트2' 호출
                Fragment2 fragment2 = new Fragment2();
                transaction.replace(R.id.fragment_container, fragment2);
                transaction.commit();
                break;
        }

    }

    @Override
    protected void onStart() {
        super.onStart();
        camera.startFaceDetection();
        camera.startPreview();
        STATE = true;
    }

    @Override
    protected void onPause() {
        super.onPause();
        camera.stopFaceDetection();
        camera.stopPreview();
        STATE = false;
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (STATE != true) {
            camera.startFaceDetection();
            camera.startPreview();
            STATE = true;
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        camera.stopFaceDetection();
        camera.stopPreview();
        STATE = false;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        camera.release();
        camera = null;
        STATE = false;
    }

    protected  void  distanceValue(String distance)
    {
        float inputValue = Float.parseFloat(distance);


        if(inputValue>30)
        {
            Log.d("inputValue","Safe");
            valWindow.setBackgroundColor(051);
        }
        else if(inputValue<30&&inputValue>10)
        {
            Log.d("inputValue","Waring");
            valWindow.setBackgroundColor(000);
        }
        else
        {
            Log.d("inputValue","OMG");
            valWindow.setBackgroundColor(000);
        }
    }

    class FaceDetect implements Camera.FaceDetectionListener{

        @Override
        public void onFaceDetection(Camera.Face[] faces, Camera camera) {
            if (faces.length > 0){
                Log.d("FaceDetection", "face detected: "+ faces.length +
                        " Face 1 Location X: " + faces[0].rect.centerX() +
                        "Y: " + faces[0].rect.centerY() );
            }
        }

    }






}