package com.carpool.activity;

import android.graphics.Color;
import android.location.Location;
import android.support.v4.app.Fragment;
import android.support.annotation.Nullable;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.ExpandableListView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.TimePicker;

import com.carpool.design.SlidingTabLayout;
import com.carpool.design.ViewPagerAdapter;
import com.carpool.model.Offre;
import com.carpool.model.Position;
import com.carpool.model.Trajet;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;


public class ResultatRechercheActivity extends Fragment {

    View rootview = null;
    final ArrayList<Offre> listOffers = new ArrayList<Offre>();
    private ListView mDrawerList;
    ViewPager pager;
    private String titles[] = new String[]{"               VUE LISTE               ", "               VUE CARTE               "};
    SlidingTabLayout slidingTabLayout;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootview = inflater.inflate(R.layout.activity_resultat_recherche,container,false);

        pager = (ViewPager) rootview.findViewById(R.id.viewpager);
        pager.setAdapter(new ViewPagerAdapter(getActivity().getSupportFragmentManager(), titles));
        slidingTabLayout = (SlidingTabLayout) rootview.findViewById(R.id.sliding_tabs);

        slidingTabLayout.setViewPager(pager);

        slidingTabLayout.setCustomTabColorizer(new SlidingTabLayout.TabColorizer() {
            @Override
            public int getIndicatorColor(int position) {
                return Color.WHITE;
            }
        });



        return rootview;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        final ExpandableListView listView = (ExpandableListView) rootview.findViewById(R.id.lvResultSearch);

        Bundle bundle = getArguments();
        List<Offre> offresAcceptables = (List<Offre>) bundle.getSerializable("offres");
        MyResultSearchListAdapter adapter = new MyResultSearchListAdapter(getActivity(), offresAcceptables);
        listView.setAdapter(adapter);
        listView.setBackgroundColor(Color.WHITE);

        /*
        if (offresAcceptables.size() == 0)
        {
            TextView texte = (TextView)rootview.findViewById(R.id.existC);
            texte.setText("!! Aucun covoiturage trouve !!");
        }*/





    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

}
