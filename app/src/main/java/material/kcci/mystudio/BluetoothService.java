package material.kcci.mystudio;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
import android.os.Handler;
import android.util.Log;

/**
 * Created by User on 2017-05-25.
 */

public class BluetoothService {


    private static final String TAG = "BluetoothService";

    // Intent request code
    private static final int REQUEST_CONNECT_DEVICE = 1;
    private static final int REQUEST_ENABLE_BT = 2;

    private BluetoothAdapter btAdapter;

    private Activity mActivity;
    private Handler mHandler;

    // Constructors
    public BluetoothService(Activity ac, Handler h) {
        mActivity = ac;
        mHandler = h;

        // BluetoothAdapter 얻기
        btAdapter = BluetoothAdapter.getDefaultAdapter();
    }


    //etDeviceState() 라는 메소드를 만들어서 기기의 블루투스 지원여부를 확인 한다.
    //BluetoothAdapter가 null일 경우 블루투스 통신을 지원하지 않는 기기이다.
    public boolean getDeviceState() {
        Log.d(TAG, "Check the Bluetooth support");

        if(btAdapter == null) {
            Log.d(TAG, "Bluetooth is not available");

            return false;

        } else {
            Log.d(TAG, "Bluetooth is available");

            return true;
        }
    }


    //bluethoothservice 클래스의 getDeviceState()가 true를 반환할 경우 블루투스 활성화를 요청한다
    //블루투스 활성화 요청을 위해서 enableBluetooth()라는 메소드를 만듦
    public void enableBluetooth() {
        Log.i(TAG, "Check the enabled Bluetooth");


        if(btAdapter.isEnabled()) {
            // 기기의 블루투스 상태가 On인 경우
            Log.d(TAG, "Bluetooth Enable Now");

            // Next Step
        } else {
            // 기기의 블루투스 상태가 Off인 경우
            Log.d(TAG, "Bluetooth Enable Request");

            Intent i = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            mActivity.startActivityForResult(i, REQUEST_ENABLE_BT);
        }
    }

}
