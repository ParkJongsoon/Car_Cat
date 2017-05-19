package material.kcci.mystudio;

/**
 * Created by db2 on 2017-05-17.
 */

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Iterator;


public class Fragment1 extends Fragment {

    private String _intputDestName;
    private String searchValue;
    public Fragment1() {
        Log.d("TAG","Fragment1");
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        final ViewGroup root_page = (ViewGroup) inflater.inflate(R.layout.fragment_fragment1,container,false);

        Button searchBtn = (Button) root_page.findViewById(R.id.button2);
        Log.d("onClick_TAG2","please");
        searchBtn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                HashMap<String, String> SavedMap = new HashMap<String, String>();

                SavedMap.put("Hanmo","인천");
                SavedMap.put("Taewoo","안양바른사나이");
                SavedMap.put("jongsoun","화곡");

                Iterator<String> findMap = SavedMap.keySet().iterator();

                while(findMap.hasNext())
                {
                    String keys = (String) findMap.next();
                    if(keys.equals(_intputDestName))
                    {
                        searchValue = SavedMap.get(keys);
                        Log.d("TEST_VALUE",searchValue);
                        Log.d("TEST_TAG",_intputDestName+SavedMap.get(keys));
                        insertToDatabase(_intputDestName,searchValue);
                    }
                }

                EditText input_destination = (EditText) root_page.findViewById(R.id.editText);
                _intputDestName = input_destination.getText().toString();
                Log.d("onClick_TAG1",_intputDestName);
                TextView searchName = (TextView) root_page.findViewById(R.id.textView3);
                TextView searchAddr = (TextView) root_page.findViewById(R.id.textView2);
                Log.d("onClick_TAG3","searchClick_TAG");
                searchName.setText(_intputDestName);
                searchAddr.setText(searchValue);
            }
        });
        return root_page;
    }

    private void insertToDatabase(String name,String addr){

        class InsertData extends AsyncTask<String, Void, String>
        {
            ProgressDialog loading;


            @Override
            protected void onPreExecute() {
                super.onPreExecute();

                loading = ProgressDialog.show(getActivity(),"Please Wait", null, true, true);
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);

                loading.dismiss();
                Toast.makeText(getActivity(),s,Toast.LENGTH_LONG).show();
            }

            @Override
            protected String doInBackground(String... params) {

                try{
                    String name = (String)params[0];
                    String addr = (String)params[1];

                    String link="http://118.91.118.27/CarCat/insert.php";
                    String data  = URLEncoder.encode("name", "UTF-8") + "="
                            + URLEncoder.encode(name, "UTF-8");
                    data += "&" + URLEncoder.encode("address", "UTF-8") + "="
                            + URLEncoder.encode(addr, "UTF-8");

                    URL url = new URL(link);
                    URLConnection conn = url.openConnection();

                    conn.setDoOutput(true);
                    OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());

                    wr.write( data );
                    wr.flush();

                    BufferedReader reader = new BufferedReader(
                            new InputStreamReader(conn.getInputStream()));

                    StringBuilder sb = new StringBuilder();
                    String line = null;

                    // Read Server Response
                    while((line = reader.readLine()) != null)
                    {
                        sb.append(line);
                        break;
                    }
                    return sb.toString();
                }
                catch(Exception e){

                    return new String("Exception: " + e.getMessage());
                }

            }
        }

        InsertData task = new InsertData();
        task.execute(name,addr);
    }
}

