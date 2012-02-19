package com.catalogue.fragments;

import java.util.ArrayList;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.ViewFlipper;

import com.catalogue.R;
import com.catalogue.adapters.CatalogueGridAdapter;
import com.catalogue.entities.CatalogueModel;
import com.catalogue.events.OnCatalogueFragmentReadyListener;

public class CatalogueFragment extends Fragment {
	private final static int MODELS_ON_SCREEN=9;
	ViewFlipper flipper;
	OnCatalogueFragmentReadyListener onCatalogueFragmentReadyListener;

	public void setOnCatalogueFragmentReadyListener(
			OnCatalogueFragmentReadyListener onCatalogueFragmentReadyListener) {
		this.onCatalogueFragmentReadyListener = onCatalogueFragmentReadyListener;
	}



	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		return inflater.inflate(R.layout.catalogue_fragment, container, false);
	}
	
	
	
	@Override
	public void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
		onCatalogueFragmentReadyListener.onCatalogueFragmentReady(this);
	}

	public void drawCatalogue(ArrayList<CatalogueModel> models) {
		
		int models_to_show=models.size();
		int total_pages=(int)Math.ceil(models_to_show/MODELS_ON_SCREEN);
		
		flipper=(ViewFlipper)getView().findViewById(R.id.grid_flipper);
		flipper.removeAllViewsInLayout();
		
		for(int i=0; i<total_pages; i++) {
			GridView gv=new GridView(getActivity());
			int start_i=i*MODELS_ON_SCREEN;
			int end_i=start_i+MODELS_ON_SCREEN;
			if(end_i>models.size()) {
				end_i=models.size()-1;
			}
			gv.setAdapter(new CatalogueGridAdapter((ArrayList<CatalogueModel>)models.subList(start_i, end_i)));
			flipper.addView(gv);
		}
	}

	
	
}
