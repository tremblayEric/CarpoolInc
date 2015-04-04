package com.carpool.ui.adapters;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.carpool.ui.fragments.RechercheResultatFragment;

public class SampleAdapter extends FragmentPagerAdapter {
  Context ctxt=null;

  public SampleAdapter(Context ctxt, FragmentManager mgr) {
    super(mgr);
    this.ctxt=ctxt;

     // Bundle b = new Bundle();
     // mgr.getFragment(b, "offreBundle");
     // List<Offre> offreLi = (List<Offre>) b.getSerializable("offres");
     // Log.d("Liste sampleAdapter",String.valueOf(offreLi.size()));

  }

  @Override
  public int getCount() {
    return(2);
  }

  @Override
  public Fragment getItem(int position) {
    return(RechercheResultatFragment.newInstance(position));
  }

  @Override
  public String getPageTitle(int position) {
    return(RechercheResultatFragment.getTitle(position));
  }
}