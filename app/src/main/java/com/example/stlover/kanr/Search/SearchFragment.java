package com.example.stlover.kanr.Search;

import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.stlover.kanr.Equipment.EquipmentInfoFragment;
import com.example.stlover.kanr.Girls.GirlsInfoFragment;
import com.example.stlover.kanr.Kanr_activity;
import com.example.stlover.kanr.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by StLover on 2016/3/30.
 */
public class SearchFragment extends Fragment implements SearchView.SearchViewListener {

    private ListView lvResults;

    private SearchView searchView;

    private List<SearchItem> dbData;
    private List<SearchItem> resultData;

    private SearchAdapter resultAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater,ViewGroup container,Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_search,container,false);
        initData();
        initViews(view);
        return view;
    }

    private void initViews(View view) {
        lvResults = (ListView)view.findViewById(R.id.search_results);
        searchView = (SearchView)view.findViewById(R.id.search_bar);

        searchView.setSearchViewListener(this);

        lvResults.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if(resultData.get(position).getTypeId() != 0) {
                    GirlsInfoFragment fragment = new GirlsInfoFragment();
                    Bundle bundle = new Bundle();
                    bundle.putInt("rowid", resultData.get(position).getNo());
                    bundle.putBoolean("type", true);
                    fragment.setArguments(bundle);
                    FragmentTransaction ft = getFragmentManager().beginTransaction();
                    ft.replace(R.id.main_activity, fragment);
                    ft.addToBackStack(null);
                    ft.commit();
                } else {
                    EquipmentInfoFragment fragment = new EquipmentInfoFragment();
                    Bundle bundle = new Bundle();
                    bundle.putInt("rowid", resultData.get(position).getNo());
                    bundle.putBoolean("type",true);
                    fragment.setArguments(bundle);
                    FragmentTransaction ft = getFragmentManager().beginTransaction();
                    ft.replace(R.id.main_activity, fragment);
                    ft.addToBackStack(null);
                    ft.commit();
                }
            }
        });
    }

    private void initData() {
        getDbData();
        getResultData(null);
    }

    private void getDbData() {
        int size = 100;
        dbData = new ArrayList<>(size);
        Cursor list = Kanr_activity.db.rawQuery("SELECT * FROM girls ORDER BY no ASC",null);
        for(int i = 0;i < list.getCount();i++) {
            list.moveToNext();
            dbData.add(new SearchItem(1,list.getInt(0),list.getString(1)));
        }
        list = Kanr_activity.db.rawQuery("SELECT * FROM equipment ORDER BY no ASC",null);
        for(int i = 0;i < list.getCount();i++) {
            list.moveToNext();
            dbData.add(new SearchItem(0, list.getInt(0),list.getString(1)));
        }
    }

    private void getResultData(String text) {
        if (resultData == null) {
            resultData = new ArrayList<>();
        } else {
            resultData.clear();
            for (int i = 0; i < dbData.size(); i++) {
                if (dbData.get(i).getName().contains(text.trim())) {
                    resultData.add(dbData.get(i));
                }
            }
        }
        if (resultAdapter == null) {
            resultAdapter = new SearchAdapter(getContext(), resultData, R.layout.item_search);
        } else {
            resultAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onSearch(String text) {
        getResultData(text);
        lvResults.setVisibility(View.VISIBLE);
        if(lvResults.getAdapter() == null) {
            lvResults.setAdapter(resultAdapter);
        } else {
            resultAdapter.notifyDataSetChanged();
        }
    }
}
