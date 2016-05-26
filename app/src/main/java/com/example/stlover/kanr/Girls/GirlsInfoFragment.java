package com.example.stlover.kanr.Girls;

import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;

import com.example.stlover.kanr.Kanr_activity;
import com.example.stlover.kanr.R;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

/**
 * Created by StLover on 2016/4/21.
 */
public class GirlsInfoFragment extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.view_girls, null, false);
        String rowid = String.valueOf(getArguments().getInt("rowid"));
        String[] args = {rowid};
        Cursor list = Kanr_activity.db.rawQuery("SELECT * FROM girls ORDER BY no LIMIT 1 OFFSET ? ",args);
        list.moveToNext();
        ScrollView scrollView = (ScrollView) view.findViewById(R.id.girls_info_fragment);
        ScrollView.LayoutParams lp = new ScrollView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        scrollView.setLayoutParams(lp);
        TextView no_text = (TextView)view.findViewById(R.id.girls_no);
        TextView name_text = (TextView)view.findViewById(R.id.girls_name);
        TextView type_text = (TextView)view.findViewById(R.id.girls_type);
        TextView firepower_text = (TextView)view.findViewById(R.id.girls_firepower);
        TextView torpedo_text = (TextView)view.findViewById(R.id.girls_torpedo);
        TextView armor_text = (TextView)view.findViewById(R.id.girls_armor);
        TextView aa_text = (TextView)view.findViewById(R.id.girls_aa);
        ImageView pic = (ImageView)view.findViewById(R.id.girls_pic);
        no_text.setText(list.getString(0));
        name_text.setText(list.getString(1));
        type_text.setText(list.getString(2));
        firepower_text.setText(list.getString(3));
        torpedo_text.setText(list.getString(4));
        armor_text.setText(list.getString(5));
        aa_text.setText(list.getString(6));
        File mAppDirectory = getContext().getExternalFilesDir(null);
        String url = mAppDirectory.getAbsolutePath()+"/pic/girls/"+list.getInt(0)+".png";
        FileInputStream fis = null;
        try {
            fis = new FileInputStream(url);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        Bitmap bitmap = BitmapFactory.decodeStream(fis);
        Matrix matrix = new Matrix();
        matrix.setScale(0.5f,0.5f);
        Bitmap resizeBitmap = Bitmap.createBitmap(bitmap,0,0,bitmap.getWidth(),bitmap.getHeight(),matrix,true);
        pic.setImageBitmap(resizeBitmap);
        return view;
    }
}