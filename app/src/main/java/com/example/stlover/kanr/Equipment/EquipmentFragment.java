package com.example.stlover.kanr.Equipment;

import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.stlover.kanr.Kanr_activity;
import com.example.stlover.kanr.R;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;

/**
 * Created by StLover on 2016/3/30.
 */
public class EquipmentFragment extends Fragment {


    private ArrayList<Bitmap> pic = new ArrayList<>();
    private ArrayList<String> name = new ArrayList<>();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
    @Override
    public View onCreateView(LayoutInflater inflater,ViewGroup container,Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_equipments,null,false);

        Cursor list = Kanr_activity.db.rawQuery("SELECT * FROM equipment ORDER BY no ASC", null);
        for (int i = 0;i < list.getCount();i++) {
            list.moveToNext();
            File mAppDirectory = getContext().getExternalFilesDir(null);
            String url = mAppDirectory.getAbsolutePath()+"/pic/equipment/"+list.getString(0)+".png";
            FileInputStream fis = null;
            try {
                fis = new FileInputStream(url);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            Bitmap bitmap = BitmapFactory.decodeStream(fis);
            pic.add(bitmap);
            name.add(list.getString(1));
        }

        GridView gridView = (GridView) view.findViewById(R.id.equipments_gridview);
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                EquipmentInfoFragment fragment = new EquipmentInfoFragment();
                Bundle bundle = new Bundle();
                bundle.putInt("rowid", position);
                fragment.setArguments(bundle);
                FragmentTransaction ft = getFragmentManager().beginTransaction();
                ft.replace(R.id.main_activity, fragment);
                ft.addToBackStack(null);
                ft.commit();
            }
        });
        GirlsAdapter adapter = new GirlsAdapter();
        gridView.setAdapter(adapter);

        return view;
    }

    class GirlsAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return pic.size();
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View view = View.inflate(getContext(),R.layout.item_equiments,null);

            TextView text = (TextView)view.findViewById(R.id.equipments_text);
            text.setText(name.get(position));

            ImageView image = (ImageView)view.findViewById(R.id.equipments_image);
            image.setImageBitmap(pic.get(position));

            return view;
        }
    }
}
