package com.catalogue.fragments;

import com.catalogue.R;
import com.catalogue.events.OnCatalogueFragmentReadyListener;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class DetailedViewFragment extends Fragment {
	
	OnCatalogueFragmentReadyListener onCatalogueFragmentReadyListener;

	public void setOnCatalogueFragmentReadyListener(
			OnCatalogueFragmentReadyListener onCatalogueFragmentReadyListener) {
		this.onCatalogueFragmentReadyListener = onCatalogueFragmentReadyListener;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		return inflater.inflate(R.layout.detailed_view_fragment, container, false);
	}

	@Override
	public void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
		onCatalogueFragmentReadyListener.onDetailedFragmentReady(this);
	}
	
	

}
