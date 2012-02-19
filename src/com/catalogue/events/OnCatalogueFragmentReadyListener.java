package com.catalogue.events;

import com.catalogue.fragments.BacketFragment;
import com.catalogue.fragments.CatalogueFragment;
import com.catalogue.fragments.DetailedViewFragment;
import com.catalogue.fragments.FavouriteFragment;
import com.catalogue.fragments.FilterFragment;

public interface OnCatalogueFragmentReadyListener {
	public void onCatalogueFragmentReady(CatalogueFragment cf);
	public void onDetailedFragmentReady(DetailedViewFragment dvf);
	public void onBacketFragmentReady(BacketFragment bf);
	public void onFavouriteFragmentReady(FavouriteFragment ff);
	public void onFilterFragmentReady(FilterFragment fif);
}
