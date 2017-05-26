package material.kcci.mystudio;

import android.util.Log;

/**
 * Created by db2 on 2017-05-26.
 */

public class Access
{
    //region distanceValue
    protected  float _distanceValue;

        public float getDistanceValue() {
            Log.d("getDistanceValue",String.valueOf(_distanceValue));
            return _distanceValue;
        }

        public void setDistanceValue(float distanceValue) {
            _distanceValue = distanceValue;
            Log.d("setDistanceValue",String.valueOf(_distanceValue));
        }

    //endregion
}
