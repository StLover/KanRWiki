package com.example.stlover.kanr;

import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.example.stlover.kanr.Equipment.EquipmentFragment;
import com.example.stlover.kanr.Girls.GirlsFragment;
import com.example.stlover.kanr.Search.SearchFragment;
import com.example.stlover.kanr.Waters.WatersFragment;
import com.viewpagerindicator.IconPagerAdapter;

import java.io.File;
import java.io.FileNotFoundException;
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
        copyAssets("", getExternalFilesDir("").getAbsolutePath()+"/");
        //deleteAllFiles(new File("/storage/emulated/0/Android/data/com.example.stlover.kanr/"));db = SQLiteDatabase.openDatabase("/storage/emulated/0/Android/data/com.example.stlover.kanr/files/database/KanR.db",null,SQLiteDatabase.OPEN_READONLY);
        db = SQLiteDatabase.openDatabase("/storage/emulated/0/Android/data/com.example.stlover.kanr/files/database/KanR.db",null,SQLiteDatabase.OPEN_READONLY);
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

    private void copyAssets(String assetDir, String dir) {
        String[] files;
        try {
            // 获得Assets一共有几多文件
            files = this.getResources().getAssets().list(assetDir);
        } catch (IOException e1) {
            return;
        }
        File mWorkingPath = new File(dir);
        // 如果文件路径不存在
        if (!mWorkingPath.exists()) {
            // 创建文件夹
            if (!mWorkingPath.mkdirs()) {
                // 文件夹创建不成功时调用
            }
        }
        for (int i = 0; i < files.length; i++) {
            try {
                // 获得每个文件的名字
                String fileName = files[i];

                // 根据路径判断是文件夹还是文件
                if (!fileName.contains(".")) {
                    if (0 == assetDir.length()) {
                        copyAssets(fileName, dir + fileName + "/");
                    } else {
                        copyAssets(assetDir + "/" + fileName, dir + "/"
                                + fileName + "/");
                    }
                    continue;
                }
                File outFile = new File(mWorkingPath, fileName);
                if (outFile.exists())
                    outFile.delete();
                InputStream in = null;
                if (0 != assetDir.length())
                    in = getAssets().open(assetDir + "/" + fileName);
                else
                    in = getAssets().open(fileName);
                OutputStream out = new FileOutputStream(outFile);
                // Transfer bytes from in to out
                byte[] buf = new byte[1024];
                int len;
                while ((len = in.read(buf)) > 0) {
                    out.write(buf, 0, len);
                }
                in.close();
                out.close();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            catch (IOException e) {
                e.printStackTrace();
            }
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
