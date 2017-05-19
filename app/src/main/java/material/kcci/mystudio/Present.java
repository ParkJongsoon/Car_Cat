package material.kcci.mystudio;

import android.util.Log;

import java.util.ArrayList;

/**
 * Created by db2 on 2017-05-18.
 */

public class Present
{
    private String _searchName;


    public Present(String searchName)
    {
        _searchName = searchName;
        findMap(_searchName);
    }




    public void findMap(String searchName)
    {
        ArrayList<SavedMap> savedMap = loadData();
        int savedMapSize = savedMap.size();
        for (int i = 0; i < savedMapSize; i++)
        {
            if (searchName.equals(savedMap))
            {
                Log.d("TAG", "Hello");
            }
        }
    }


    private ArrayList<SavedMap> loadData()
    {
        ArrayList<SavedMap> savedMaps = new ArrayList<>();

        SavedMap savedMap = new SavedMap();

        savedMap.setTitle("title"+"J_PARK");
        savedMap.setAddress("address"+"AOMG_");
        savedMap.setImageID(R.drawable.jpark);
        savedMaps.add(savedMap);

        savedMap.setTitle("title"+"SIMON_D");
        savedMap.setAddress("address"+"AOMG_1");
        savedMap.setImageID(R.drawable.simondominic);
        savedMaps.add(savedMap);

        savedMap.setTitle("title"+"Gray");
        savedMap.setAddress("address"+"AOMG_2");
        savedMap.setImageID(R.drawable.gray);
        savedMaps.add(savedMap);

        return savedMaps;
    }
}
