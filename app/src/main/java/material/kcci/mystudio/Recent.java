package material.kcci.mystudio;

import android.graphics.Bitmap;

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
            _id = id;
        }
        //endregion

    private Bitmap _imageID;

    public Bitmap get_imageID()
    {
        return _imageID;
    }

    public void set_imageID(Bitmap imaImageID)
    {
        _imageID = imaImageID;
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
