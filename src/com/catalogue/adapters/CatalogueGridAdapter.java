package com.catalogue.adapters;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.catalogue.R;
import com.catalogue.entities.CatalogueModel;

public class CatalogueGridAdapter extends BaseAdapter {
	
	private ArrayList<CatalogueModel> cat=null;

	public CatalogueGridAdapter(ArrayList<CatalogueModel> cat) {
		super();
		this.cat = cat;
	}
	
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return cat.size();
	}

	@Override
	public Object getItem(int arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		CatalogueModel item=cat.get(position);
		
		Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        ViewGroup vg = (ViewGroup)inflater.inflate(R.layout.grid_item, null);

        TextView model_name = (TextView)vg.findViewById(R.id.grid_item_model_name);
        ImageView model_image = (ImageView)vg.findViewById(R.id.grid_item_model_icon);
        TextView model_price=(TextView)vg.findViewById(R.id.grid_item_model_price);
        
        model_name.setText(item.getSystem_name()+" "+item.getDescription());
        model_image.setImageResource(item.getMid_pic_id());
        model_price.setText("Цена: "+item.getPrice().toString());

        return vg;
	}

}
