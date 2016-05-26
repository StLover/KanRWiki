package com.example.stlover.kanr;

import android.content.res.AssetManager;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.util.Log;

import com.example.stlover.kanr.Equipment.EquipmentFragment;
import com.example.stlover.kanr.Girls.GirlsFragment;
import com.example.stlover.kanr.Waters.WatersFragment;
import com.viewpagerindicator.IconPagerAdapter;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

public class Kanr_activity extends FragmentActivity {

    static  public SQLiteDatabase db;
    private KanRViewPager mViewPager;
    private IconTabPageIndicator mIndicator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kanr_activity);
        initViews();
        copyFilesToSdCard();
        db = SQLiteDatabase.openDatabase("/storage/emulated/0/Android/data/com.example.stlover.kanr/files/KanR.db",null,SQLiteDatabase.OPEN_READONLY);
        //deleteAllFiles(new File("/storage/emulated/0/Android/data/com.example.stlover.kanr/"));
    }

    private void initViews() {
        mViewPager = (KanRViewPager)findViewById(R.id.view_pager);
        mIndicator = (IconTabPageIndicator)findViewById(R.id.indicator);
        List<Fragment> fragments = initFragments();
        FragmentAdapter adapter = new FragmentAdapter(fragments,getSupportFragmentManager());
        mViewPager.setAdapter(adapter);
        mViewPager.setOffscreenPageLimit(4);
        mIndicator.setViewPager(mViewPager);
    }

    private List<Fragment> initFragments() {
        List<Fragment> fragments = new ArrayList<Fragment>();

        Fragment fragment_search = new SearchFragment();
        fragments.add(fragment_search);

        Fragment fragment_waters = new WatersFragment();
        fragments.add(fragment_waters);

        Fragment fragment_girls = new GirlsFragment();
        fragments.add(fragment_girls);

        Fragment fragment_equipments = new EquipmentFragment();
        fragments.add(fragment_equipments);

        return fragments;
    }

    private void copyFilesToSdCard() {
        copyFileOrDir(""); // copy all files in assets folder in my project
    }

    private void copyFileOrDir(String path) {
        AssetManager assetManager = this.getAssets();
        String assets[] = null;
        try {
            assets = assetManager.list(path);
            if (assets.length == 0) {
                copyFile(path);
            } else {
                String fullPath =  getExternalFilesDir(null) + "/" + path;
                File dir = new File(fullPath);
                if (!dir.exists() && !path.startsWith("images") && !path.startsWith("sounds") && !path.startsWith("webkit"))
                    if (!dir.mkdirs())
                for (int i = 0; i < assets.length; ++i) {
                    String p;
                    if (path.equals(""))
                        p = "";
                    else
                        p = path + "/";

                    if (!path.startsWith("images") && !path.startsWith("sounds") && !path.startsWith("webkit"))
                        copyFileOrDir( p + assets[i]);
                }
            }
        } catch (IOException ex) {
        }
    }

    private void copyFile(String filename) {
        AssetManager assetManager = this.getAssets();

        InputStream in = null;
        OutputStream out = null;
        String newFileName = null;
        try {
            Log.i("tag", "copyFile() "+filename);
            in = assetManager.open(filename);
            if (filename.endsWith(".jpg")) // extension was added to avoid compression on APK file
                newFileName = getExternalFilesDir(null) + "/"+ filename.substring(0, filename.length()-4);
            else
                newFileName = getExternalFilesDir(null) + "/"+ filename;
            out = new FileOutputStream(newFileName);

            byte[] buffer = new byte[1024];
            int read;
            while ((read = in.read(buffer)) != -1) {
                out.write(buffer, 0, read);
            }
            in.close();
            in = null;
            out.flush();
            out.close();
            out = null;
        } catch (Exception e) {
            Log.e("tag", "Exception in copyFile() of "+newFileName);
            Log.e("tag", "Exception in copyFile() "+e.toString());
        }
    }
    private void deleteAllFiles(File root) {
        File files[] = root.listFiles();
        if (files != null)
            for (File f : files) {
                if (f.isDirectory()) { // 判断是否为文件夹
                    deleteAllFiles(f);
                    try {
                        f.delete();
                    } catch (Exception e) {
                    }
                } else {
                    if (f.exists()) { // 判断是否存在
                        deleteAllFiles(f);
                        try {
                            f.delete();
                        } catch (Exception e) {
                        }
                    }
                }
            }
    }

    class FragmentAdapter extends FragmentPagerAdapter implements IconPagerAdapter {

        private List<Fragment> mFragments;

        public FragmentAdapter(List<Fragment> fragments,FragmentManager fm) {
            super(fm);
            mFragments = fragments;
        }

        @Override
        public Fragment getItem(int position) {
            return mFragments.get(position);
        }

        @Override
        public int getIconResId(int index) {
            switch(index) {
                case 0:
                    return R.drawable.tab_search;
                case 1:
                    return R.drawable.tab_waters;
                case 2:
                    return R.drawable.tab_girls;
                case 3:
                    return R.drawable.tab_equipments;
                default:
                    return 0;
            }
        }

        @Override
        public int getCount() {
            return mFragments.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch(position) {
                case 0:
                    return "搜索";
                case 1:
                    return "海域";
                case 2:
                    return "舰娘";
                case 3:
                    return "装备";
                default:
                    return "";
            }
        }
    }

}
