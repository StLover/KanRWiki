package com.example.stlover.kanr.Girls;

import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.stlover.kanr.Kanr_activity;
import com.example.stlover.kanr.R;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by StLover on 2016/3/30.
 */
public class GirlsFragment extends Fragment {

    private ArrayList<Bitmap> pic = new ArrayList<>();
    private ArrayList<String> name = new ArrayList<>();
    private List<View> mGirlViews = new ArrayList<>();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater,ViewGroup container,Bundle savedInstanceState) {
        View view =inflater.inflate(R.layout.fragment_girls, null, false);
        Cursor list = Kanr_activity.db.rawQuery("SELECT * FROM girls ORDER BY no ASC",null);
        for (int i = 0;i < list.getCount();i++) {
            list.moveToNext();
            File mAppDirectory = getContext().getExternalFilesDir(null);
            String url = mAppDirectory.getAbsolutePath()+"/pic/girls/"+list.getInt(0)+".png";
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

        for(int i = 0 ; i < name.size() ; i++) {
            LinearLayout v = new LinearLayout(getContext());
            LinearLayout.LayoutParams mParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
             v.setOrientation(LinearLayout.VERTICAL);
            v.setGravity(Gravity.CENTER);
            ImageView imageView = new ImageView(getContext());
            imageView.setImageBitmap(pic.get(i));
            v.addView(imageView, mParams);
            TextView textView = new TextView(getContext());
            textView.setText(name.get(i));
            mParams.setMargins(0,50,0,0);
            v.addView(textView, mParams);
            mGirlViews.add(v);
        }
        list.close();

        final ViewPager mPager = (ViewPager)view.findViewById(R.id.girls_pager);

        view.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return mPager.dispatchTouchEvent(event);
            }
        });

        mPager.setPageTransformer(true, new GirlsPageTransformer());

        mPager.setOffscreenPageLimit(5);
        mPager.setPageMargin(10);
        mPager.setAdapter(new PagerAdapter() {
            @Override
            public int getCount() {
                return pic.size();
            }

            @Override
            public boolean isViewFromObject(View view, Object object) {
                return view == object;
            }

            @Override
            public Object instantiateItem(ViewGroup container, final int position) {
                View girlView = mGirlViews.get(position);
                container.addView(girlView);
                girlView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        GirlsInfoFragment fragment = new GirlsInfoFragment();
                        Bundle bundle = new Bundle();
                        bundle.putInt("rowid",position);
                        fragment.setArguments(bundle);
                        FragmentTransaction ft = getFragmentManager().beginTransaction();
                        ft.replace(R.id.main_activity, fragment);
                        ft.addToBackStack(null);
                        ft.commit();
                    }
                });
                return mGirlViews.get(position);
            }

            @Override
            public void destroyItem(ViewGroup container, int position, Object object) {
                container.removeView(mGirlViews.get(position));
            }
        });
        return view;
    }
}
