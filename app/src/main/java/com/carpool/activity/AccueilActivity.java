package com.carpool.activity;

import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.Typeface;
import android.support.v7.app.ActionBarActivity;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.os.Bundle;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.support.v4.widget.DrawerLayout;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import com.parse.ParseUser;

/***
 * Cette classe représente l'activité de démarrage. Elle redirige vers la page de login si
 * l'utilisateur se connecte pour la première fois ou s'il s'est déconnecté la dernière fois.
 * Autrement, l'activité de recherche se lance.
 * Aussi, l'initialisation du tiroir se fait à ce niveau également.
 */
public class AccueilActivity extends ActionBarActivity{

    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle drawerToggle;

    private ListView mDrawerList;
    private Toolbar toolbar;
    Fragment objFragment = null;
    boolean init = false;

    ParseUser currentUser ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        currentUser = ParseUser.getCurrentUser();

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

        drawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, toolbar, R.string.app_name, R.string.app_name);
        mDrawerLayout.setDrawerListener(drawerToggle);

        createTiroir();

        mDrawerList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                switch (position) {
                    case 0:
                        if (currentUser != null) {
                            mDrawerList.setBackgroundColor(getResources().getColor(R.color.material_deep_teal_500));
                            toolbar.setBackgroundColor(getResources().getColor(R.color.material_deep_teal_500));
                            mDrawerLayout.closeDrawer(Gravity.START);
                            objFragment = new ProfilActivity();
                        }
                        else {
                            mDrawerList.setBackgroundColor(getResources().getColor(R.color.material_deep_teal_500));
                            toolbar.setBackgroundColor(getResources().getColor(R.color.material_deep_teal_500));
                            mDrawerLayout.closeDrawer(Gravity.START);
                            objFragment = new LoginActivity();
                        }
                        break;
                    case 1:
                        mDrawerList.setBackgroundColor(getResources().getColor(R.color.material_deep_teal_500));
                        toolbar.setBackgroundColor(getResources().getColor(R.color.material_deep_teal_500));
                        mDrawerLayout.closeDrawer(Gravity.START);
                        objFragment = new OffreActivity();
                        break;
                    case 2:
                        mDrawerList.setBackgroundColor(getResources().getColor(R.color.material_deep_teal_500));
                        toolbar.setBackgroundColor(getResources().getColor(R.color.material_deep_teal_500));
                        mDrawerLayout.closeDrawer(Gravity.START);
                        objFragment = new RechercheActivity();
                        break;
                    case 3:
                        if (currentUser != null) {
                            mDrawerList.setBackgroundColor(getResources().getColor(R.color.material_deep_teal_500));
                            toolbar.setBackgroundColor(getResources().getColor(R.color.material_deep_teal_500));
                            mDrawerLayout.closeDrawer(Gravity.START);
                            objFragment = new ConsultationOffreActivity();
                        }
                        break;
                    case 4: // gerer la deconnexion
                        if (currentUser != null) {
                            ParseUser.logOut();
                            currentUser = ParseUser.getCurrentUser();
                            createTiroir();
                            mDrawerList.setBackgroundColor(getResources().getColor(R.color.material_deep_teal_500));
                            toolbar.setBackgroundColor(getResources().getColor(R.color.material_deep_teal_500));
                            mDrawerLayout.closeDrawer(Gravity.START);
                            objFragment = new LoginActivity();
                        }
                        break;
                }

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
            mDrawerLayout.closeDrawer(Gravity.START);
            objFragment = new RechercheActivity();

            // update the main content by replacing fragments
            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction()
                    .replace(R.id.container, objFragment)
                    .commit();

            init = true;
        }

}
    private void createTiroir(){
        String[] values = new String[]{
                " PROFIL", " POSTER ANNONCE", " RECHERCHE", " MES ANNONCES", " DECONNEXION"
        };

        String[] valuesNotConnected = new String[]{
                " CONNEXION", " POSTER ANNONCE", " RECHERCHE"
        };


        ArrayAdapter<String> adapter = null;

        // Get current user data from Parse.com

        if (currentUser != null) {
            adapter = new ArrayAdapter<String>(this,
                    android.R.layout.simple_list_item_1, android.R.id.text1, values)
            {
                @Override
                public View getView(int position, View convertView, ViewGroup parent) {
                    View view = super.getView(position, convertView, parent);
                    TextView text = (TextView) view.findViewById(android.R.id.text1);
                    text.setTypeface(null, Typeface.BOLD);
                    return view;
                }

            };
        }
        else {
            adapter = new ArrayAdapter<String>(this,
                    android.R.layout.simple_list_item_1, android.R.id.text1, valuesNotConnected)
            {
                @Override
                public View getView(int position, View convertView, ViewGroup parent) {
                    View view = super.getView(position, convertView, parent);
                    TextView text = (TextView) view.findViewById(android.R.id.text1);
                    text.setTypeface(null, Typeface.BOLD);
                    return view;
                }

            };
        }

        mDrawerList.setAdapter(adapter);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (drawerToggle.onOptionsItemSelected(item)) {

       //gerer la couleur de l'element qui a ete selectionee
            Log.d("selection item", "un item est selectionne");
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
}
