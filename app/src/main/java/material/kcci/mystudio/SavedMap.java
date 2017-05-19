package material.kcci.mystudio;

/**
 * Created by db2 on 2017-05-18.
 */

public class SavedMap
{
    //region imageID

    protected  int _imageID;

        public int getImageID() {
            return _imageID;
        }

        public void setImageID(int imageID) {
            _imageID = imageID;
        }

        //endregion

    //region title

    protected  String _title;

        public String getTitle() {
            return _title;
        }

        public void setTitle(String title) {
            _title = title;
        }
        //endregion

    //region address

    protected  String _address;

        public String getAddress() {
            return _address;
        }

        public void setAddress(String address) {
            _address = address;
        }
        //endregion
}
