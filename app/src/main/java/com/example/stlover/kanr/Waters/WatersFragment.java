package com.example.stlover.kanr.Waters;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnChildClickListener;
import android.widget.ExpandableListView.OnGroupClickListener;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.stlover.kanr.R;

import java.util.ArrayList;
import java.util.List;

public class WatersFragment extends Fragment {

    private ExpandableListView listView;

    private List<String> group_list = new ArrayList<>();
    private List<String> item_list = new ArrayList<>();
    private List<List<String>> item_all = new ArrayList<>();

    private String[] group_text = {"第一章", "第二章", "第三章", "第四章", "第五章", "第六章"};
    private String[] item_text = {"母港附近海域", "东北防线海域", "仁州附近海域", "深海仁州基地"};

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_waters, null);

        for (int i = 0; i < group_text.length; i++) {
            group_list.add(group_text[i]);
        }
        for (int i = 0; i < item_text.length; i++) {
            item_list.add(item_text[i]);
        }
        for (int i = 0; i < group_text.length; i++) {
            item_all.add(item_list);
        }
        listView = (ExpandableListView) view.findViewById(R.id.expendlist);
        listView.setGroupIndicator(null);

        listView.setOnGroupClickListener(new OnGroupClickListener() {
            @Override
            public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id) {
                return false;
            }
        });

        listView.setOnChildClickListener(new OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
                WatersDungeonFragment fragment = new WatersDungeonFragment();
                Bundle bundle = new Bundle();
                bundle.putInt("act",groupPosition);
                bundle.putInt("scene",childPosition);
                fragment.setArguments(bundle);
                FragmentTransaction ft = getFragmentManager().beginTransaction();
                ft.replace(R.id.main_activity, fragment);
                ft.addToBackStack(null);
                ft.commit();
                return true;
            }
        });
        WatersAdapter adapter = new WatersAdapter(getContext());
        listView.setAdapter(adapter);
        return view;
    }

    class WatersAdapter extends BaseExpandableListAdapter {

        private Context context;

        public WatersAdapter(Context c) {
            this.context = c;
        }

        @Override
        public int getGroupCount() {
            return group_list.size();
        }

        @Override
        public int getChildrenCount(int groupPosition) {
            return item_all.get(groupPosition).size();
        }

        @Override
        public Object getGroup(int groupPosition) {
            return group_list.get(groupPosition);
        }

        @Override
        public Object getChild(int groupPosition, int childPosition) {
            return item_all.get(groupPosition).get(childPosition);
        }

        @Override
        public long getGroupId(int groupPosition) {
            return groupPosition;
        }

        @Override
        public long getChildId(int groupPosition, int childPosition) {
            return childPosition;
        }

        @Override
        public boolean hasStableIds() {
            return true;
        }

        @Override
        public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent)
        {
            GroupHolder groupHolder = null;
            if (convertView == null) {
                convertView = LayoutInflater.from(context).inflate(R.layout.first_waters, null);
                groupHolder = new GroupHolder();
                groupHolder.text = (TextView)convertView.findViewById(R.id.waters_group_text);
                groupHolder.image = (ImageView)convertView.findViewById(R.id.waters_group_image);
                convertView.setTag(groupHolder);
            } else {
                groupHolder = (GroupHolder)convertView.getTag();
            }

            if (isExpanded) {
                groupHolder.image.setBackgroundResource(R.drawable.group_down);
            } else {
                groupHolder.image.setBackgroundResource(R.drawable.group_right);
            }
            groupHolder.text.setText(group_list.get(groupPosition));
            return convertView;
        }

        @Override
        public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent)
        {
            ItemHolder itemHolder = null;
            if (convertView == null)
            {
                convertView = LayoutInflater.from(context).inflate(R.layout.second_waters, null);
                itemHolder = new ItemHolder();
                itemHolder.text = (TextView)convertView.findViewById(R.id.waters_item_text);
                convertView.setTag(itemHolder);
            } else {
                itemHolder = (ItemHolder)convertView.getTag();
            }
            itemHolder.text.setText(item_all.get(groupPosition).get(childPosition));
            return convertView;
        }

        @Override
        public boolean isChildSelectable(int groupPosition, int childPosition) {
            return true;
        }

        class GroupHolder {
            public TextView text;
            public ImageView image;
        }

        class ItemHolder {
            public TextView text;
        }
    }
}
