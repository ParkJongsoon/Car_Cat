package material.kcci.mystudio;

/**
 * Created by db2 on 2017-05-17.
 */

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;


public class Fragment2 extends Fragment
{
    RecyclerView _recyclerView;


    public Fragment2()
    {
        Log.d("TAG", "Fragment2");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        ViewGroup root_page = (ViewGroup) inflater.inflate(R.layout.fragment_fragment2, container, false);
        _recyclerView = (RecyclerView) root_page.findViewById(R.id.recyclerView);

        getData("http://118.91.118.27/CarCat/select.php");

        //use a linear Layout Manaager -> (여긴 설정하기 나름)레이아웃 매니저 이용하여 객체 연결
        RecyclerView.LayoutManager _layoutManager = new LinearLayoutManager(getActivity());

        //여기가 이제 레이아웃 매니저 붙이는 곳
        _recyclerView.setLayoutManager(_layoutManager);
        _recyclerView.setItemAnimator(new DefaultItemAnimator());

        //android.os.NetworkOnMainThreadException 발생하는 경우 처리 방법...이거 맞을까...이거 아닐꺼같은데
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        return root_page;
    }

    //region Variable

    String myJSON;

    private static final String TAG_RESULTS = "result";
    private static final String TAG_ID = "id";
    private static final String TAG_NAME = "name";
    private static final String TAG_ADD = "address";
    JSONArray _jsonArray = null;

    //endregion

    //region getData
    public void getData(String url)
    {
        class GetDataJSON extends AsyncTask<String, Void, String>
        {
            @Override
            protected String doInBackground(String... params)
            {
                String uri = params[0];

                BufferedReader bufferedReader = null;
                try {
                    URL url = new URL(uri);
                    HttpURLConnection con = (HttpURLConnection) url.openConnection();
                    StringBuilder sb = new StringBuilder();

                    bufferedReader = new BufferedReader(new InputStreamReader(con.getInputStream()));

                    String json;
                    while((json = bufferedReader.readLine())!= null){
                        sb.append(json+"\n");
                    }

                    return sb.toString().trim();

                }
                catch(Exception e)
                {
                    return null;
                }
            }

            @Override
            protected void onPostExecute(String result)
            {
                myJSON = result;
                Log.d("onPostExecute",result);
                //_recents = new ArrayList<Recent>(showList());
                showList();
            }
        }
        GetDataJSON g = new GetDataJSON();
        g.execute(url);
    }
    //endregion

    //region showList
    protected void showList()
    {
        String imageUrl = "http://mblogthumb1.phinf.naver.net/20160220_292/cool71su_1455895502641AGSox_JPEG/attachImage_847099030.jpeg?type=w800";
        ArrayList<Recent> _recents = new ArrayList<>();
        try
        {
            JSONObject jsonObj = new JSONObject(myJSON);
            _jsonArray = jsonObj.getJSONArray(TAG_RESULTS);

            for (int i = 0; i < _jsonArray.length(); i++)
            {
                Recent recent = new Recent();

                JSONObject c = _jsonArray.getJSONObject(i);

                String id = c.getString(TAG_ID);
                String name = c.getString(TAG_NAME);
                String address = c.getString(TAG_ADD);

                recent.set_imageID(getBitmap(imageUrl));
                recent.set_Id(id);
                recent.set_title(address);
                recent.set_info(name);

                _recents.add(recent);
            }

            RecentAdapter reA = new RecentAdapter(_recents);
            _recyclerView.setAdapter(reA);

            //Item삭제 및 이동
            ItemTouchHelper itemTouchHelper = new ItemTouchHelper(new ItemTouchHelperCallback(reA));
            itemTouchHelper.attachToRecyclerView(_recyclerView);

        } catch (JSONException e)
        {
            e.printStackTrace();
        }

    }
    //endregion

    public static Bitmap getBitmap(String src) {
        try {
            URL url = new URL(src);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.connect();

            InputStream input = connection.getInputStream();
            Bitmap myBitmap = BitmapFactory.decodeStream(input);
            return myBitmap;
        } catch (IOException e) {
            // Log exception
            return null;
        }
    }
}


