package material.kcci.mystudio;

import android.util.Log;

/**
 * Created by db2 on 2017-05-17.
 */

public class Recent
{
    //region id

    protected  String _id;

        public String get_Id() {
            return _id;
        }

        public void set_Id(String id) {
            Log.d("_recent_TAG",id);
            _id = id;
        }
        //endregion

    private int _imageID;

    public int get_imageID()
    {
        return _imageID;
    }

    public void set_imageID(int imageID)
    {
        _imageID = imageID;
    }


    private String _title;

    public String get_title() {
        return _title;
    }

    public void set_title(String title) {
        _title = title;
    }

    private String _info;

    public String get_info() {
        return _info;
    }

    public void set_info(String info) {
        _info = info;
    }
}
