package material.kcci.mystudio;

/**
 * Created by db2 on 2017-05-17.
 */

import android.os.AsyncTask;
import android.os.Bundle;
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
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;


public class Fragment2 extends Fragment
{
    RecyclerView _recyclerView;
    private ArrayList<Recent> _recents;

    public Fragment2()
    {
        Log.d("TAG", "Fragment2");
        // Required empty public constructor
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

        return root_page;
    }

    private ArrayList<Recent> loadData()
    {
        ArrayList<Recent> _recents = new ArrayList<>();

        for (int i = 0; i < 10; i++)
        {
            Recent recent = new Recent();
            recent.set_imageID(R.drawable.jpark);
            recent.set_title("주소 :" + (i + 1));
            recent.set_info("정보 : " + i);

            //꼭 데이터 넣어라 시발년아
            _recents.add(recent);
        }

        //위의 데이터를 쳐넣은 ArrayList 리턴
        return _recents;


    }


    //region Variable

    String myJSON;

    private static final String TAG_RESULTS = "result";
    private static final String TAG_NAME = "name";
    private static final String TAG_ADD = "address";
    JSONArray _jsonArray = null;
    ArrayList<HashMap<String, String>> personList;
    RecentAdapter myAdapter;

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
        Log.d("showList_TAG","in showList");
        ArrayList<Recent> _recents = new ArrayList<>();
        try
        {
            JSONObject jsonObj = new JSONObject(myJSON);
            _jsonArray = jsonObj.getJSONArray(TAG_RESULTS);

            for (int i = 0; i < _jsonArray.length(); i++)
            {
                Recent recent = new Recent();

                JSONObject c = _jsonArray.getJSONObject(i);


                String name = c.getString(TAG_NAME);
                String address = c.getString(TAG_ADD);

                if(i%2==0)
                {
                    recent.set_imageID(R.drawable.gray);
                    recent.set_title(address);
                    recent.set_info(name);
                }
                else
                {
                    recent.set_imageID(R.drawable.jpark);
                    recent.set_title(address);
                    recent.set_info(name);
                }
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

}




