package com.example.stlover.kanr.Waters;

import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;

import com.example.stlover.kanr.Kanr_activity;
import com.example.stlover.kanr.R;
import com.example.stlover.kanr.Waters.TableAdapter.TableCell;
import com.example.stlover.kanr.Waters.TableAdapter.TableRow;
import com.polites.android.GestureImageView;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;

/**
 * Created by StLover on 2016/4/1.
 */
public class WatersDungeonFragment extends Fragment {

    View view;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        view = inflater.inflate(R.layout.view_waters, null, false);
        String act = String.valueOf(getArguments().getInt("act") + 1);
        String scene = String.valueOf(getArguments().getInt("scene") + 1);

        File mAppDirectory = getContext().getExternalFilesDir(null);
        String url = mAppDirectory+"/pic/waters/"+act+"-"+scene+".png";
        FileInputStream fis = null;
        try {
            fis = new FileInputStream(url);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        Bitmap bitmap = BitmapFactory.decodeStream(fis);
        GestureImageView imageView=(GestureImageView)view.findViewById(R.id.waters_dungeon_image);
        imageView.setImageBitmap(bitmap);

        String[] args = {act,scene};
        initBranchingList(args);
        initDropList(args);
        initEnemyList(args);
        return view;
    }

    private void initBranchingList(String[] args) {
        Cursor list = Kanr_activity.db.rawQuery("SELECT * FROM node WHERE act=? AND scene=? ORDER BY area ASC",args);
        ListView branchingList;
        branchingList = (ListView)view.findViewById(R.id.branching_rules);
        ArrayList<TableRow> table = new ArrayList<>();
        TableCell[] titles = new TableCell[3];
        int Width = this.getActivity().getWindowManager().getDefaultDisplay().getWidth();
        titles[0] = new TableCell("位置",Width*1/5,LinearLayout.LayoutParams.WRAP_CONTENT,TableCell.STRING);
        titles[1] = new TableCell("条件判定",Width*2/5,LinearLayout.LayoutParams.WRAP_CONTENT,TableCell.STRING);
        titles[2] = new TableCell("概率判定",Width*2/5,LinearLayout.LayoutParams.WRAP_CONTENT,TableCell.STRING);
        table.add(new TableRow(titles));
        while(list.moveToNext()) {
            if(list.getString(7) != null || list.getString(8) != null) {
                TableCell[] cells = new TableCell[3];
                cells[0] = new TableCell(list.getString(2), Width * 1 / 5, LinearLayout.LayoutParams.WRAP_CONTENT, TableCell.STRING);
                cells[1] = new TableCell(list.getString(7), Width * 2 / 5, LinearLayout.LayoutParams.WRAP_CONTENT, TableCell.STRING);
                cells[2] = new TableCell(list.getString(8), Width * 2 / 5, LinearLayout.LayoutParams.WRAP_CONTENT, TableCell.STRING);
                table.add(new TableRow(cells));
            }
        }
        TableAdapter adapter =new TableAdapter(getContext(),table);
        branchingList.setAdapter(adapter);
        setListViewHeightBasedOnChildren(branchingList);
    }

    private void initDropList(String[] args) {
        Cursor list = Kanr_activity.db.rawQuery("SELECT * FROM node WHERE act=? AND scene=? ORDER BY area ASC", args);
        ListView dropList;
        dropList = (ListView)view.findViewById(R.id.drop_list);
        ArrayList<TableRow> table = new ArrayList<>();
        TableCell[] titles = new TableCell[2];
        int Width = this.getActivity().getWindowManager().getDefaultDisplay().getWidth();
        titles[0] = new TableCell("位置",Width*1/5,LinearLayout.LayoutParams.WRAP_CONTENT,TableCell.STRING);
        titles[1] = new TableCell("掉落列表",Width*4/5,LinearLayout.LayoutParams.WRAP_CONTENT,TableCell.STRING);
        table.add(new TableRow(titles));
        list.moveToNext();
        for(int i = 0;i < list.getCount()-1;i++) {
            TableCell[] cells = new TableCell[2];
            cells[0] = new TableCell(list.getString(2), Width * 1 / 5, LinearLayout.LayoutParams.WRAP_CONTENT, TableCell.STRING);
            String dropName = parseDropList(list.getString(6));
            cells[1] = new TableCell(dropName, Width * 4 / 5, LinearLayout.LayoutParams.WRAP_CONTENT, TableCell.STRING);
            table.add(new TableRow(cells));
            list.moveToNext();
        }
        TableAdapter adapter = new TableAdapter(getContext(),table);
        dropList.setAdapter(adapter);
        setListViewHeightBasedOnChildren(dropList);
    }

    private String parseDropList(String string) {
        String[] numList = string.split(",");
        StringBuilder nameList = new StringBuilder();
        for(String number : numList) {
            String[] args = {number};
            Cursor list = Kanr_activity.db.rawQuery("SELECT * FROM girls WHERE no=?",args);
            list.moveToNext();
            nameList.append(list.getString(1)+",");
        }
        nameList.deleteCharAt(nameList.length()-1);
        return nameList.toString();
    }

    private void initEnemyList(String[] args) {
        Cursor list = Kanr_activity.db.rawQuery("SELECT * FROM node WHERE act=? AND scene=? ORDER BY area ASC",args);
        ListView enemyList;
        enemyList = (ListView)view.findViewById(R.id.enemy_encounters);
        ArrayList<TableRow> table = new ArrayList<>();
        TableCell[] titles = new TableCell[2];
        int Width = this.getActivity().getWindowManager().getDefaultDisplay().getWidth();
        titles[0] = new TableCell("位置",Width*1/5,LinearLayout.LayoutParams.WRAP_CONTENT,TableCell.STRING);
        titles[1] = new TableCell("敌军配置",Width*4/5,LinearLayout.LayoutParams.WRAP_CONTENT,TableCell.STRING);
        table.add(new TableRow(titles));
        list.moveToNext();
        for(int i = 0;i < list.getCount()-1;i++) {
            TableCell[] cells = new TableCell[2];
            cells[0] = new TableCell("\n"+list.getString(2)+"\n", Width * 1 / 5, LinearLayout.LayoutParams.WRAP_CONTENT, TableCell.STRING);
            cells[1] = new TableCell(list.getString(3)+"\n"+list.getString(4)+"\n"+list.getString(5), Width * 4 / 5, LinearLayout.LayoutParams.WRAP_CONTENT, TableCell.STRING);
            table.add(new TableRow(cells));
            list.moveToNext();
        }
        TableAdapter adapter = new TableAdapter(getContext(),table);
        enemyList.setAdapter(adapter);
        setListViewHeightBasedOnChildren(enemyList);
    }

    public static void setListViewHeightBasedOnChildren(ListView listView) {
        if(listView == null) return;

        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null) {
            // pre-condition
            return;
        }

        int totalHeight = 0;
        for (int i = 0; i < listAdapter.getCount(); i++) {
            View listItem = listAdapter.getView(i, null, listView);
            listItem.measure(0, 0);
            totalHeight += listItem.getMeasuredHeight();
        }

        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        listView.setLayoutParams(params);
    }

}
