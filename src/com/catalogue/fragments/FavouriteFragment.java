package com.catalogue.fragments;

import com.catalogue.R;
import com.catalogue.events.OnCatalogueFragmentReadyListener;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class FavouriteFragment extends Fragment {
	
	OnCatalogueFragmentReadyListener onCatalogueFragmentReadyListener;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		return inflater.inflate(R.layout.favourite_fragment, container, false);
	}

	@Override
	public void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
		onCatalogueFragmentReadyListener.onFavouriteFragmentReady(this);
	}

	public void setOnCatalogueFragmentReadyListener(
			OnCatalogueFragmentReadyListener onCatalogueFragmentReadyListener) {
		this.onCatalogueFragmentReadyListener = onCatalogueFragmentReadyListener;
	}
	
	

}
