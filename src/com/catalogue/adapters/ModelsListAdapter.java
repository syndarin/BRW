package com.catalogue.adapters;

import com.catalogue.R;

import android.graphics.Color;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

public class ModelsListAdapter extends BaseExpandableListAdapter {

	private String[] groups = { "Мебель модульная", "Модульные наборы", "Классическая", "Гостиные/Стенки", "Детская мебель" };
    private String[][] children = {
            { "AUGUST","BAWARIA","IDIANA","LARGO CLASSIC", "ORLAND", "VIS A VIS"},
            { "AFFI BRW/Аффи БРВ","AFFI BRW/Аффи БРВ","AFFI BRW/Аффи БРВ","AFFI BRW/Аффи БРВ"},
            { "AFFI BRW/Аффи БРВ","AFFI BRW/Аффи БРВ","AFFI BRW/Аффи БРВ","AFFI BRW/Аффи БРВ"},
            { "AFFI BRW/Аффи БРВ","AFFI BRW/Аффи БРВ","AFFI BRW/Аффи БРВ","AFFI BRW/Аффи БРВ"},
            { "AFFI BRW/Аффи БРВ","AFFI BRW/Аффи БРВ","AFFI BRW/Аффи БРВ","AFFI BRW/Аффи БРВ"}
    };
	
	@Override
	public Object getChild(int groupPosition, int childPosition) {
		// TODO Auto-generated method stub
		return children[groupPosition][childPosition];
	}

	@Override
	public long getChildId(int groupPosition, int childPosition) {
		// TODO Auto-generated method stub
		return childPosition;
	}

	@Override
	public View getChildView(int groupPosition, int childPosition,
			boolean isLastChild, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		TextView textView = getGenericView(parent);
        textView.setText(getChild(groupPosition, childPosition).toString());
        textView.setTextColor(Color.BLACK);
        textView.setBackgroundResource(R.drawable.child_back);
        return textView;
	}

	@Override
	public int getChildrenCount(int groupPosition) {
		// TODO Auto-generated method stub
		return children[groupPosition].length;
	}

	@Override
	public Object getGroup(int groupPosition) {
		// TODO Auto-generated method stub
		return groups[groupPosition];
	}

	@Override
	public int getGroupCount() {
		// TODO Auto-generated method stub
		return groups.length;
	}

	@Override
	public long getGroupId(int groupPosition) {
		// TODO Auto-generated method stub
		return groupPosition;
	}

	@Override
	public View getGroupView(int groupPosition, boolean isExpanded,
			View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		TextView textView = getGenericView(parent);
        textView.setText(getGroup(groupPosition).toString());
        textView.setTextColor(Color.BLACK);
        textView.setBackgroundResource(R.drawable.back);
        return textView;
	}

	@Override
	public boolean hasStableIds() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isChildSelectable(int groupPosition, int childPosition) {
		// TODO Auto-generated method stub
		return true;
	}
	
	public TextView getGenericView(ViewGroup parent) {
        // Layout parameters for the ExpandableListView
        AbsListView.LayoutParams lp = new AbsListView.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, 64);

        TextView textView = new TextView(parent.getContext());
        textView.setLayoutParams(lp);
        // Center the text vertically
        textView.setGravity(Gravity.CENTER_VERTICAL | Gravity.LEFT);
        // Set the text starting position
        textView.setPadding(36, 0, 0, 0);
        return textView;
    }

}
