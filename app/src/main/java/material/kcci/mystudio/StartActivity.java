package material.kcci.mystudio;

import android.app.Activity;
import android.content.Intent;
import android.icu.text.SimpleDateFormat;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.Date;

@RequiresApi(api = Build.VERSION_CODES.N)
public class StartActivity extends AppCompatActivity {
    TextView dateNow;

    Thread t = new Thread() {
        @Override
        public void run()
        {
            try
            {
                while (!isInterrupted()) {
                    Thread.sleep(1000);
                    runOnUiThread(new Runnable()
                    {
                        @Override
                        public void run()
                        {
                            update();
                        }
                    });
                }
            } catch (InterruptedException e) {

            }
        }
    };



    private void update()
    {
        long now = System.currentTimeMillis();
        Date date = new Date(now);
        SimpleDateFormat sdfNow = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        String formatDate = sdfNow.format(date);
        dateNow.setText(formatDate);
    }

    private static MediaPlayer mp;
    private ImageButton startbtn;

    //region bluetooth

    private static final String TAG = "Main";

    // Intent request code
    private static final int REQUEST_CONNECT_DEVICE = 1;
    private static final int REQUEST_ENABLE_BT = 2;

    private BluetoothService btService = null;

    private final Handler mHandler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
        }

    };
    //endregion



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);

        dateNow = (TextView) findViewById(R.id.dateNow);


        mp = MediaPlayer.create(this,R.raw.depapepe_start);
        mp.setLooping(true);
        mp.start();
        t.start();

        startbtn = (ImageButton) findViewById(R.id.startbtn);
        startbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
                mp.stop();
            }
        });

        // BluetoothService 클래스 생성
        if(btService == null) {
            btService = new BluetoothService(this, mHandler);}

        if(btService.getDeviceState()) {
            // 블루투스가 지원 가능한 기기일 때
            btService.enableBluetooth();
        } else {
            finish();
        }
    }

    @Override
    public void onBackPressed()
    {
        android.os.Process.killProcess(android.os.Process.myPid());
    }

    @Override
    protected void onUserLeaveHint()
    {
        super.onUserLeaveHint();
        mp = MediaPlayer.create(this,R.raw.depapepe_start);
        mp.stop();
    }

    //기기의 블루투스 상태가 Off일 경우 블루투스 활성화를 요청하는 알림창을 띄운다.
    //알림창에서 확인/취소를 선택할 경우 결과는 MainActivity에 onActivityResult()메소드로 들어온다.
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        switch (requestCode) {

            case REQUEST_CONNECT_DEVICE:
                // When DeviceListActivity returns with a device to connect
                if (resultCode == Activity.RESULT_OK) {
                    btService.getDeviceInfo(data);
                }
                break;

            case REQUEST_ENABLE_BT:
                // When the request to enable Bluetooth returns
                if (resultCode == Activity.RESULT_OK) {
                    // Next Step
                    btService.scanDevice();
                } else {

                    Log.d(TAG, "Bluetooth is not enabled");
                }
                break;
        }
    }
}
