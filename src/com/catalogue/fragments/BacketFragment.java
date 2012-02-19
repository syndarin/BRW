package com.catalogue.fragments;

import com.catalogue.R;
import com.catalogue.events.OnCatalogueFragmentReadyListener;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class BacketFragment extends Fragment {

	OnCatalogueFragmentReadyListener onCatalogueFragmentListener;
	public void setOnCatalogueFragmentListener(
			OnCatalogueFragmentReadyListener onCatalogueFragmentListener) {
		this.onCatalogueFragmentListener = onCatalogueFragmentListener;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		return inflater.inflate(R.layout.backet_fragment, container, false);
	}

	@Override
	public void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
		onCatalogueFragmentListener.onBacketFragmentReady(this);
	}
	
	

}
