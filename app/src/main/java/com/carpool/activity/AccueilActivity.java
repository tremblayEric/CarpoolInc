package com.carpool.activity;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.os.Bundle;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.support.v4.widget.DrawerLayout;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.carpool.design.SlidingTabLayout;
import com.carpool.design.ViewPagerAdapter;
import com.carpool.utils.FloatingActionButton;


import com.parse.ParseUser;



public class AccueilActivity extends ActionBarActivity{
       // implements   { //NavigationDrawerFragment.NavigationDrawerCallbacks {

    /**
     * Fragment managing the behaviors, interactions and presentation of the navigation drawer.
     */
    private NavigationDrawerFragment mNavigationDrawerFragment;

    /**
     * Used to store the last screen title. For use in {link #restoreActionBar()}.
     */
    private CharSequence mTitle;

    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle drawerToggle;

    private ListView mDrawerList;
    private Toolbar toolbar;
    Fragment objFragment = null;
    Activity objActivite = null;
    boolean init = false;
    static public boolean AddOnToolBar = false;

    ViewPager pager;
    private String titles[] = new String[]{"VUE LISTE", "VUE CARTE"};
    SlidingTabLayout slidingTabLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Get current user data from Parse.com
        ParseUser currentUser = ParseUser.getCurrentUser();
        if (currentUser != null) {
            // Send logged in users to Profil
            Fragment objFragment = new ProfilActivity();
            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction()
                    .replace(R.id.container, objFragment)
                    .commit();
        }
        else {
            // Send user to Login
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
            finish();
        }
        /*

        mNavigationDrawerFragment = (NavigationDrawerFragment)
                getSupportFragmentManager().findFragmentById(R.id.navigation_drawer);
        mTitle = getTitle();

        // Set up the drawer.
        mNavigationDrawerFragment.setUp(
                R.id.navigation_drawer,
                (DrawerLayout) findViewById(R.id.drawer_layout));



        if (AddOnToolBar)
        {

            pager = (ViewPager) findViewById(R.id.viewpager);
            pager.setAdapter(new ViewPagerAdapter(getSupportFragmentManager(), titles));
            slidingTabLayout = (SlidingTabLayout)findViewById(R.id.sliding_tabs);

            slidingTabLayout.setViewPager(pager);
            slidingTabLayout.setCustomTabColorizer(new SlidingTabLayout.TabColorizer() {
                @Override
                public int getIndicatorColor(int position) {
                    return Color.WHITE;
                }
            });
        }*/

        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerList = (ListView) findViewById(R.id.navdrawer);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        if (toolbar != null) {
            setSupportActionBar(toolbar);
            //toolbar.setNavigationIcon(R.drawable.ic_ab_drawer);
            toolbar.setLogo(R.drawable.carpool_logo);
            toolbar.setTitle("      CARPOOL INC.       ");
            toolbar.setTitleTextColor(Color.WHITE);
            Log.d("toolbar", "dans la toolbar");
        }
        //pager = (ViewPager) findViewById(R.id.viewpager);
        //slidingTabLayout = (SlidingTabLayout) findViewById(R.id.sliding_tabs);
        //pager.setAdapter(new ViewPagerAdapter(getSupportFragmentManager(), titles));

        //slidingTabLayout.setViewPager(pager);
        //slidingTabLayout.setCustomTabColorizer(new SlidingTabLayout.TabColorizer() {
          //  @Override
            //public int getIndicatorColor(int position) {
              //  return Color.WHITE;
            //}
       // });





        /* checher comment mettre une font sur le profil
        Typeface font = Typeface.createFromAsset( getAssets(), "font-awesome-4.3.0/fonts/fontawesome-webfont.ttf" );

        TextView txtProfil = new TextView(this);
        txtProfil.setText(R.string.strg_profil);
        txtProfil.setPadding(-150,0,0,0);
        txtProfil.setTextSize(20);
        txtProfil.append("     PROFIL");
        txtProfil.setTypeface(font);

        String a = String.valueOf( R.string.strg_profil);
        a.concat("  PROFIL") ;*/

        drawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, toolbar, R.string.app_name, R.string.app_name);
        mDrawerLayout.setDrawerListener(drawerToggle);
        String[] values = new String[]{
                " PROFIL", " POSTER ANNONCE", " RECHERCHE", " MES ANNONCES", " DECONNEXION"
        };



    ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
            android.R.layout.simple_list_item_1, android.R.id.text1, values)
    {
        @Override
         public View getView(int position, View convertView, ViewGroup parent) {
            View view = super.getView(position, convertView, parent);
            TextView text = (TextView) view.findViewById(android.R.id.text1);
            text.setTypeface(null, Typeface.BOLD);
            Color.argb(127, 07, 00, 51);

            return view;
         }

    };



    mDrawerList.setAdapter(adapter);

    mDrawerList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view,
        int position, long id) {
            switch (position) {
                case 0:
                    mDrawerList.setBackgroundColor(getResources().getColor(R.color.material_deep_teal_500));
                    toolbar.setBackgroundColor(getResources().getColor(R.color.material_deep_teal_500));
                    //slidingTabLayout.setBackgroundColor(getResources().getColor(R.color.material_deep_teal_500));
                    mDrawerLayout.closeDrawer(Gravity.START);
                    objFragment = new ProfilActivity();

                    //Intent intent = new Intent(AccueilActivity.this, ProfilActivity.class);

                   // View rootView = inflater.inflate(R.layout.profil_layout, container, false);

                    break;
                case 1:
                    mDrawerList.setBackgroundColor(getResources().getColor(R.color.material_deep_teal_500));
                    toolbar.setBackgroundColor(getResources().getColor(R.color.material_deep_teal_500));
                   // slidingTabLayout.setBackgroundColor(getResources().getColor(R.color.red));
                    mDrawerLayout.closeDrawer(Gravity.START);
                    objFragment = new OffreActivity();
                    break;
                case 2:
                    mDrawerList.setBackgroundColor(getResources().getColor(R.color.material_deep_teal_500));
                    toolbar.setBackgroundColor(getResources().getColor(R.color.material_deep_teal_500));
                    //slidingTabLayout.setBackgroundColor(getResources().getColor(R.color.blue));
                    mDrawerLayout.closeDrawer(Gravity.START);
                    objFragment = new RechercheActivity();
                    break;
                case 3:
                    mDrawerList.setBackgroundColor(getResources().getColor(R.color.material_deep_teal_500));
                    toolbar.setBackgroundColor(getResources().getColor(R.color.material_deep_teal_500));
                    //slidingTabLayout.setBackgroundColor(getResources().getColor(R.color.material_blue_grey_800));
                    mDrawerLayout.closeDrawer(Gravity.START);
                    objFragment = new ConsultationOffreActivity();


                    break;
                case 4: // gerer la deconnexion
                    Intent intent = new Intent(AccueilActivity.this, LoginActivity.class);
                    startActivity(intent);
                    finish();
                    break;

            }

            //ActivityManager activityManager;

            // update the main content by replacing fragments
            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction()
                    .replace(R.id.container, objFragment)
                    .commit();
        }
    });



        if (!init)
        {
            mDrawerList.setBackgroundColor(getResources().getColor(R.color.material_deep_teal_500));

            toolbar.setBackgroundColor(getResources().getColor(R.color.material_deep_teal_500));
            //slidingTabLayout.setBackgroundColor(getResources().getColor(R.color.material_deep_teal_500));
            mDrawerLayout.closeDrawer(Gravity.START);
            objFragment = new ProfilActivity();

            // update the main content by replacing fragments
            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction()
                    .replace(R.id.container, objFragment)
                    .commit();

            init = true;
        }

}

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (drawerToggle.onOptionsItemSelected(item)) {

       //gerer la couleur de l'element qui a ete selectionee

            Log.d("selection item", "un item est selectionne");

            /*

                MenuInflater inflater = getMenuInflater();
                inflater.inflate(R.menu.menu, menu);
                m = menu;
                return true;
             */


            return true;
        }

        switch (item.getItemId()) {
            case android.R.id.home:
                mDrawerLayout.openDrawer(Gravity.START);
                return true;
        }

        return super.onOptionsItemSelected(item);
    }


    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        drawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        drawerToggle.onConfigurationChanged(newConfig);
    }

    /*
    @Override
    public void onNavigationDrawerItemSelected(int position) {

        Fragment objFragment = null;

        switch (position){

            case 0:
                objFragment = new ProfilActivity();
                break;
            case 1:
                objFragment = new OffreActivity();
                break;
            case 2:
               // objFragment = new ResultatRechercheActivity();
                objFragment = new RechercheActivity();
            default:
                break;
            case 3:
                objFragment = new ConsultationOffreActivity();
                break;

        }
        // update the main content by replacing fragments
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.container, objFragment)
                .commit();
    }

    public void onSectionAttached(int number) {
        switch (number) {
            case 1:
                mTitle = getString(R.string.titre_profil);
                break;
            case 2:
                mTitle = getString(R.string.titre_offre);
                break;
            case 3:
                mTitle = getString(R.string.titre_recherche);
                break;
            case 4:
                mTitle = getString(R.string.titre_consultation_offre);
                break;
        }
    }

    public void restoreActionBar() {
        ActionBar actionBar = getSupportActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
        actionBar.setDisplayShowTitleEnabled(true);
        actionBar.setTitle(mTitle);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (!mNavigationDrawerFragment.isDrawerOpen()) {
            // Only show items in the action bar relevant to this screen
            // if the drawer is not showing. Otherwise, let the drawer
            // decide what to show in the action bar.
            getMenuInflater().inflate(R.menu.main, menu);
            restoreActionBar();
            return true;
        }
        return super.onCreateOptionsMenu(menu);
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

    /**
     * A placeholder fragment containing a simple view.
     */
    /*public static class PlaceholderFragment extends Fragment {
        *
         * The fragment argument representing the section number for this
         * fragment.
         */
        /*private static final String ARG_SECTION_NUMBER = "section_number";

        *
         * Returns a new instance of this fragment for the given section
         * number.
         /*
        public static PlaceholderFragment newInstance(int sectionNumber) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        public PlaceholderFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_main, container, false);
            return rootView;
        }

        @Override
        public void onAttach(Activity activity) {
            super.onAttach(activity);
            ((AccueilActivity) activity).onSectionAttached(
                    getArguments().getInt(ARG_SECTION_NUMBER));
        }
    }*/




}
