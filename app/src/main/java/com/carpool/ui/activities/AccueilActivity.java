package com.carpool.ui.activities;

import android.animation.ArgbEvaluator;
import android.animation.ObjectAnimator;
import android.animation.TypeEvaluator;
import android.animation.ValueAnimator;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.v4.view.ViewCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.DecelerateInterpolator;
import android.widget.AbsListView;
import android.widget.TextView;


import com.carpool.R;
import com.carpool.ui.design.MultiSwipeRefreshLayout;
import com.carpool.ui.design.ScrimInsetsScrollView;
import com.carpool.utils.UIUtils;
import com.parse.ParseUser;

import java.util.ArrayList;

public abstract class AccueilActivity extends ActionBarActivity implements
        SharedPreferences.OnSharedPreferenceChangeListener,
        MultiSwipeRefreshLayout.CanChildScrollUpCallback {

    private static final String TAG = "LollipopTAG";

    // Navigation drawer:
    private DrawerLayout mDrawerLayout;

    // Helper methods for L APIs
    private UIUtils mLUtils;

    private ObjectAnimator mStatusBarColorAnimator;
    private ViewGroup mDrawerItemsListContainer;
    private Handler mHandler;

    // When set, these components will be shown/hidden in sync with the action bar
    // to implement the "quick recall" effect (the Action Bar and the header views disappear
    // when you scroll down a list, and reappear quickly when you scroll up).
    private ArrayList<View> mHideableHeaderViews = new ArrayList<View>();

    // Durations for certain animations we use:
    private static final int HEADER_HIDE_ANIM_DURATION = 300;
    private static final int ACCOUNT_BOX_EXPAND_ANIM_DURATION = 200;

    // symbols for navdrawer items (indices must correspond to array below). This is
    // not a list of items that are necessarily *present* in the Nav Drawer; rather,
    // it's a list of all possible items.
    ParseUser currentUser = ParseUser.getCurrentUser();



   // protected static final int NAVDRAWER_ITEM_ACTIVITY2 = 1;
    //protected static final int NAVDRAWER_ITEM_ACTIVITY3 = 2;
    //protected static final int NAVDRAWER_ITEM_ACTIVITY4 = 3;
    //protected static final int NAVDRAWER_ITEM_ACTIVITY5 = 4;

    protected static final int NAVDRAWER_ITEM_CONNEXION = 0;
    protected static final int NAVDRAWER_ITEM_PROFIL_ACTIVITY1 = 0;

    protected static final int NAVDRAWER_ITEM_POSTER_ANNONCE =1; //verifier au besoin quon ai besoin de connexion
    protected static final int NAVDRAWER_ITEM_RECHERCHE = 2;
    protected static final int NAVDRAWER_ITEM_MES_ANNONCE = 3;
    protected static final int NAVDRAWER_ITEM_MES_RESERVATIONS = 4;
    protected static final int NAVDRAWER_ITEM_NOTIFICATIONS = 5;
    protected static final int NAVDRAWER_ITEM_DECONNEXION= 6;

    protected static final int NAVDRAWER_ITEM_INVALID = -1;
    protected static final int NAVDRAWER_ITEM_SEPARATOR = -2;
    protected static final int NAVDRAWER_ITEM_SEPARATOR_SPECIAL = -3;

    // titles for navdrawer items (indices must correspond to the above)
    private static final int[] NAVDRAWER_TITLE_RES_ID = new int[]{
            R.string.title_section1,
            R.string.title_section2,
            R.string.title_section3,
            R.string.title_section4,
            R.string.title_section5,
            R.string.title_section6,
            R.string.title_section7,

    };

    // icons for navdrawer items (indices must correspond to above array)
    private static final int[] NAVDRAWER_ICON_RES_ID = new int[]{
            R.drawable.ic_settings,  // Section 1
            R.drawable.ic_settings,  // Section 2
            R.drawable.ic_settings, // Section 3
            R.drawable.ic_settings, // section 4
            R.drawable.ic_settings, //section 5
            R.drawable.ic_settings, //section 6
            R.drawable.ic_settings, //section 7
    };

    private static final String[] NAVDRAWER_FONT_ICON = new String[]{
            "\uf007", //profile
            "\uf0a1", // poster annonce
            "\uf002", //recherche
            "\uf06e", //mes annonces
            "\uf0f2",  //reservation
            "\uf0f3;",  //notification
            "\uf08b", //deconnexion
    };
    // title if not connected
    private static final int[] NAVDRAWER_TITLE_RES_ID_IF_NOT_CONNECTED = new int[]{
            R.string.title_section1_not_connected,
            R.string.title_section2_not_connected,
            R.string.title_section3_not_connected,
    };

    // icons for navdrawer items (indices must correspond to above array)
    private static final int[] NAVDRAWER_ICON_RES_ID_IF_NOT_CONNECTED = new int[]{
            R.drawable.ic_settings,  // Section 1
            R.drawable.ic_settings,  // Section 2
            R.drawable.ic_settings, // Section 3
    };

    private static final String[] NAVDRAWER_FONT_ICON_NOT_CONNECTED = new String[]{
            "\uf090", //connexion
            "\uf0a1", // poster annonce
            "\uf002",//recherche
    };

    // delay to launch nav drawer item, to allow close animation to play
    private static final int NAVDRAWER_LAUNCH_DELAY = 250;

    // fade in and fade out durations for the main content when switching between
    // different Activities of the app through the Nav Drawer
    private static final int MAIN_CONTENT_FADEOUT_DURATION = 150;
    private static final int MAIN_CONTENT_FADEIN_DURATION = 250;

    // list of navdrawer items that were actually added to the navdrawer, in order
    private ArrayList<Integer> mNavDrawerItems = new ArrayList<Integer>();

    // views that correspond to each navdrawer item, null if not yet created
    private View[] mNavDrawerItemViews = null;

    // SwipeRefreshLayout allows the user to swipe the screen down to trigger a manual refresh
    private SwipeRefreshLayout mSwipeRefreshLayout;

    // Primary toolbar and drawer toggle
    private Toolbar mActionBarToolbar;

    // variables that control the Action Bar auto hide behavior (aka "quick recall")
    private boolean mActionBarAutoHideEnabled = false;
    private int mActionBarAutoHideSensivity = 0;
    private int mActionBarAutoHideMinY = 0;
    private int mActionBarAutoHideSignal = 0;
    private boolean mActionBarShown = true;

    // A Runnable that we should execute when the navigation drawer finishes its closing animation
    private Runnable mDeferredOnDrawerClosedRunnable;
    private int mThemedStatusBarColor;
    private int mNormalStatusBarColor;
    private int mProgressBarTopWhenActionBarShown;
    private static final TypeEvaluator ARGB_EVALUATOR = new ArgbEvaluator();
    private ActionBarDrawerToggle mDrawerToggle;





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutResource());
        getActionBarToolbar();
        mHandler = new Handler();

        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(this);
        sp.registerOnSharedPreferenceChangeListener(this);

        ActionBar ab = getSupportActionBar();
        if (ab != null) {
            ab.setDisplayHomeAsUpEnabled(true);
        }

        mLUtils = UIUtils.getInstance(this);
        mThemedStatusBarColor = getResources().getColor(R.color.material_deep_teal_200);
        mNormalStatusBarColor = mThemedStatusBarColor;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    protected abstract int getLayoutResource();

    /*
    private void trySetupSwipeRefresh() {
        mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh_layout);
        if (mSwipeRefreshLayout != null) {
            mSwipeRefreshLayout.setColorSchemeResources(
                    R.color.refresh_progress_1,
                    R.color.refresh_progress_2,
                    R.color.refresh_progress_3);
            mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                @Override
                public void onRefresh() {
                    requestDataRefresh();
                }
            });

            if (mSwipeRefreshLayout instanceof MultiSwipeRefreshLayout) {
                MultiSwipeRefreshLayout mswrl = (MultiSwipeRefreshLayout) mSwipeRefreshLayout;
                mswrl.setCanChildScrollUpCallback(this);
            }
        }
    }*/

    protected void setProgressBarTopWhenActionBarShown(int progressBarTopWhenActionBarShown) {
        mProgressBarTopWhenActionBarShown = progressBarTopWhenActionBarShown;
        updateSwipeRefreshProgressBarTop();
    }

    private void updateSwipeRefreshProgressBarTop() {
        if (mSwipeRefreshLayout == null) {
            return;
        }

        int progressBarStartMargin = getResources().getDimensionPixelSize(
                R.dimen.swipe_refresh_progress_bar_start_margin);
        int progressBarEndMargin = getResources().getDimensionPixelSize(
                R.dimen.swipe_refresh_progress_bar_end_margin);
        int top = mActionBarShown ? mProgressBarTopWhenActionBarShown : 0;
        mSwipeRefreshLayout.setProgressViewOffset(false,
                top + progressBarStartMargin, top + progressBarEndMargin);
    }

    /**
     * Returns the navigation drawer item that corresponds to this Activity. Subclasses
     * of BaseActivity override this to indicate what nav drawer item corresponds to them
     * Return NAVDRAWER_ITEM_INVALID to mean that this Activity should not have a Nav Drawer.
     */
    protected int getSelfNavDrawerItem() {
        return NAVDRAWER_ITEM_INVALID;
    }

    /**
     * Sets up the navigation drawer as appropriate. Note that the nav drawer will be
     * different depending on whether the attendee indicated that they are attending the
     * event on-site vs. attending remotely.
     */
    private void setupNavDrawer() {
        // What nav drawer item should be selected?
        int selfItem = getSelfNavDrawerItem();

        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (mDrawerLayout == null) {
            Log.e("Skeleton", "DrawerLayout is null");
            return;
        }
        mDrawerLayout.setStatusBarBackgroundColor(
                getResources().getColor(R.color.material_deep_teal_500));
        ScrimInsetsScrollView navDrawer = (ScrimInsetsScrollView)
                mDrawerLayout.findViewById(R.id.navdrawer);
        if (selfItem == NAVDRAWER_ITEM_INVALID) {
            // do not show a nav drawer
            Log.e("Skeleton", "returning");
            if (navDrawer != null) {
                ((ViewGroup) navDrawer.getParent()).removeView(navDrawer);
            }
            mDrawerLayout = null;
            return;
        }


    if (mActionBarToolbar != null) {

            mActionBarToolbar.setLogo(getResources().getDrawable(R.drawable.carpool_logo));
            mActionBarToolbar.setNavigationIcon(R.drawable.ic_drawer);
            mActionBarToolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mDrawerLayout.openDrawer(Gravity.START);
                }
            });
        }


        mDrawerToggle = new ActionBarDrawerToggle(
                this,                  /* host Activity */
                mDrawerLayout,         /* DrawerLayout object */
                (Toolbar) findViewById(R.id.toolbar_actionbar),  /* toolbar object */
                R.string.app_name,  /* "open drawer" description */
                R.string.app_name  /* "close drawer" description */
        ) {

            /** Called when a drawer has settled in a completely closed state. */
            public void onDrawerClosed(View view) {
                super.onDrawerClosed(view);
                if (mDeferredOnDrawerClosedRunnable != null) {
                    mDeferredOnDrawerClosedRunnable.run();
                    mDeferredOnDrawerClosedRunnable = null;
                }

                onNavDrawerStateChanged(false, false);
            }

            /** Called when a drawer has settled in a completely open state. */
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                onNavDrawerStateChanged(true, false);
            }

            @Override
            public void onDrawerStateChanged(int newState) {
                super.onDrawerStateChanged(newState);
                onNavDrawerStateChanged(isNavDrawerOpen(), newState != DrawerLayout.STATE_IDLE);
            }

            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {
                super.onDrawerSlide(drawerView, slideOffset);
                onNavDrawerSlide(slideOffset);
            }


        };


        // Set the drawer toggle as the DrawerListener
        mDrawerLayout.setDrawerListener(mDrawerToggle);


        mDrawerLayout.setDrawerShadow(R.drawable.drawer_shadow, Gravity.START);

        // populate the nav drawer with the correct items
        if (currentUser != null)
        populateNavDrawer();
        else
            populateNavDrawerNotConnected();

    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mDrawerToggle.onConfigurationChanged(newConfig);
    }


    // Subclasses can override this for custom behavior
    protected void onNavDrawerStateChanged(boolean isOpen, boolean isAnimating) {
        if (mActionBarAutoHideEnabled && isOpen) {
            autoShowOrHideActionBar(true);
        }
    }

    protected void onNavDrawerSlide(float offset) {
    }

    protected boolean isNavDrawerOpen() {
        return mDrawerLayout != null && mDrawerLayout.isDrawerOpen(Gravity.START);
    }

    protected void closeNavDrawer() {
        if (mDrawerLayout != null) {
            mDrawerLayout.closeDrawer(Gravity.START);
        }
    }

    /**
     * Populates the navigation drawer with the appropriate items.
     */
    private void populateNavDrawer() {
        mNavDrawerItems.clear();

        mNavDrawerItems.add(NAVDRAWER_ITEM_PROFIL_ACTIVITY1);

        mNavDrawerItems.add(NAVDRAWER_ITEM_SEPARATOR);

        mNavDrawerItems.add(NAVDRAWER_ITEM_POSTER_ANNONCE);

        mNavDrawerItems.add(NAVDRAWER_ITEM_SEPARATOR_SPECIAL);

        mNavDrawerItems.add(NAVDRAWER_ITEM_RECHERCHE);

        mNavDrawerItems.add(NAVDRAWER_ITEM_SEPARATOR);

        mNavDrawerItems.add(NAVDRAWER_ITEM_MES_ANNONCE);

        mNavDrawerItems.add(NAVDRAWER_ITEM_SEPARATOR_SPECIAL);

        mNavDrawerItems.add(NAVDRAWER_ITEM_MES_RESERVATIONS);

        mNavDrawerItems.add(NAVDRAWER_ITEM_SEPARATOR);

        // - Commenté l'activité NOTIFICATIONS - Gaëlle
        //mNavDrawerItems.add(NAVDRAWER_ITEM_NOTIFICATIONS);

        //mNavDrawerItems.add(NAVDRAWER_ITEM_SEPARATOR_SPECIAL);

        mNavDrawerItems.add(NAVDRAWER_ITEM_DECONNEXION);

        createNavDrawerItems();
    }

    // populate NAVDRAWER not connected

    private void populateNavDrawerNotConnected(){

        mNavDrawerItems.clear();

        mNavDrawerItems.add(NAVDRAWER_ITEM_CONNEXION);

        mNavDrawerItems.add(NAVDRAWER_ITEM_SEPARATOR);

        mNavDrawerItems.add(NAVDRAWER_ITEM_POSTER_ANNONCE);

        mNavDrawerItems.add(NAVDRAWER_ITEM_SEPARATOR_SPECIAL);

        mNavDrawerItems.add(NAVDRAWER_ITEM_RECHERCHE);

        createNavDrawerItems();

    }


    private void createNavDrawerItems() {
        mDrawerItemsListContainer = (ViewGroup) findViewById(R.id.navdrawer_items_list);
        if (mDrawerItemsListContainer == null) {
            return;
        }

        mNavDrawerItemViews = new View[mNavDrawerItems.size()];
        mDrawerItemsListContainer.removeAllViews();
        int i = 0;
        for (int itemId : mNavDrawerItems) {
            mNavDrawerItemViews[i] = makeNavDrawerItem(itemId, mDrawerItemsListContainer);
            mDrawerItemsListContainer.addView(mNavDrawerItemViews[i]);
            ++i;
        }
    }

    /**
     * Sets up the given navdrawer item's appearance to the selected state. Note: this could
     * also be accomplished (perhaps more cleanly) with state-based layouts.
     */
    private void setSelectedNavDrawerItem(int itemId) {
        if (mNavDrawerItemViews != null) {
            for (int i = 0; i < mNavDrawerItemViews.length; i++) {
                if (i < mNavDrawerItems.size()) {
                    int thisItemId = mNavDrawerItems.get(i);
                    formatNavDrawerItem(mNavDrawerItemViews[i], thisItemId, itemId == thisItemId);
                }
            }
        }
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {

        Log.d(TAG, "Attendee at venue preference changed, repopulating nav drawer and menu.");
        if (currentUser != null )
            populateNavDrawer();
        else
            populateNavDrawerNotConnected();

        invalidateOptionsMenu();
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        setupNavDrawer();

        //trySetupSwipeRefresh();
        updateSwipeRefreshProgressBarTop();

        View mainContent = findViewById(R.id.main_content);
        if (mainContent != null) {
            mainContent.setAlpha(0);
            mainContent.animate().alpha(1).setDuration(MAIN_CONTENT_FADEIN_DURATION);
        } else {
            Log.w(TAG, "No view with ID main_content to fade in.");
        }

        mDrawerToggle.syncState();
    }

    @Override
    public void onBackPressed() {
        if (isNavDrawerOpen()) {
            closeNavDrawer();
        } else {
            super.onBackPressed();
        }
    }

    protected void requestDataRefresh() {

        Log.d(TAG, "Requesting manual data refresh.");
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                onRefreshingStateChanged(false);
            }
        }, 3000);
    }

    // en mode connecte
    protected void goToNavDrawerItem(int item) {
        switch(item){
            case AccueilActivity.NAVDRAWER_ITEM_PROFIL_ACTIVITY1:
                startActivity(new Intent(this, ProfilLoginActivity.class));
                finish();
                break;
            case AccueilActivity.NAVDRAWER_ITEM_POSTER_ANNONCE:
                startActivity(new Intent(this, OffreActivity.class));
                finish();
                break;
            case AccueilActivity.NAVDRAWER_ITEM_RECHERCHE:
                startActivity(new Intent(this, RechercheActivity.class));
                finish();
                break;
            case AccueilActivity.NAVDRAWER_ITEM_MES_ANNONCE:
                startActivity(new Intent(this, ConsultationOffreActivity.class));
                finish();
                break;
            case AccueilActivity.NAVDRAWER_ITEM_MES_RESERVATIONS:
                startActivity(new Intent(this, ReservationActivity.class));
                break;
            case AccueilActivity.NAVDRAWER_ITEM_NOTIFICATIONS:
                startActivity(new Intent(this, NotificationsActivity.class));
                break;
            case AccueilActivity.NAVDRAWER_ITEM_DECONNEXION:
                startActivity(new Intent(this, DeconnexionActivity.class));
                finish();
                break;
        }
    }

    //en mode non connecte

    protected void goToNavDrawerItemNotConnected(int item) {
        switch(item){
            case AccueilActivity.NAVDRAWER_ITEM_CONNEXION:
                startActivity(new Intent(this, ProfilLoginActivity.class));
                finish();
                break;
            case AccueilActivity.NAVDRAWER_ITEM_POSTER_ANNONCE:
                //startActivity(new Intent(this, Activity2.class));
                startActivity(new Intent(this, OffreActivity.class));
                finish();
                break;
            case AccueilActivity.NAVDRAWER_ITEM_RECHERCHE:
                startActivity(new Intent(this, RechercheActivity.class));
                finish();
                break;
        }
    }


    private void onNavDrawerItemClicked(final int itemId) {
        if (itemId == getSelfNavDrawerItem()) {
            mDrawerLayout.closeDrawer(Gravity.START);
            return;
        }

        if (isSpecialItem(itemId)) {
            if (currentUser != null)
                goToNavDrawerItem(itemId);
            else
                goToNavDrawerItemNotConnected(itemId);
        } else {

            // ici on switch entre les item pour savoir ces quel item qui a ete selectionne
             final View mainContent = findViewById(R.id.session_fragment);
            //if (itemId == NAVDRAWER_ITEM_ACTIVITY2)
            //{
                //mainContent = findViewById(R.id.profil_fragment);

            // launch the target Activity after a short delay, to allow the close animation to play
            mHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    if (currentUser != null)
                        goToNavDrawerItem(itemId);
                    else
                        goToNavDrawerItemNotConnected(itemId);
                }
            }, NAVDRAWER_LAUNCH_DELAY);

            // change the active item on the list so the user can see the item changed
            setSelectedNavDrawerItem(itemId);
        //}
            // fade out the main content

            if (mainContent != null) {
                mainContent.animate().alpha(0).setDuration(MAIN_CONTENT_FADEOUT_DURATION);
            }
        }

        mDrawerLayout.closeDrawer(Gravity.START);
    }

    /**
     * Initializes the Action Bar auto-hide (aka Quick Recall) effect.
     */
    private void initActionBarAutoHide() {
        mActionBarAutoHideEnabled = true;
        mActionBarAutoHideMinY = getResources().getDimensionPixelSize(
                R.dimen.action_bar_auto_hide_min_y);
        mActionBarAutoHideSensivity = getResources().getDimensionPixelSize(
                R.dimen.action_bar_auto_hide_sensivity);
    }

    /*
    public void showButterBar(String messageText, String actionText, long timeout,
                              View.OnClickListener listener){
        UIUtils.setUpButterBar(findViewById(R.id.butter_bar), messageText, actionText, timeout, listener);
    }

    public void hideButterBar(){
        findViewById(R.id.butter_bar).setVisibility(View.GONE);
    }*/

    /**
     * Indicates that the main content has scrolled (for the purposes of showing/hiding
     * the action bar for the "action bar auto hide" effect). currentY and deltaY may be exact
     * (if the underlying view supports it) or may be approximate indications:
     * deltaY may be INT_MAX to mean "scrolled forward indeterminately" and INT_MIN to mean
     * "scrolled backward indeterminately".  currentY may be 0 to mean "somewhere close to the
     * start of the list" and INT_MAX to mean "we don't know, but not at the start of the list"
     */
    private void onMainContentScrolled(int currentY, int deltaY) {
        if (deltaY > mActionBarAutoHideSensivity) {
            deltaY = mActionBarAutoHideSensivity;
        } else if (deltaY < -mActionBarAutoHideSensivity) {
            deltaY = -mActionBarAutoHideSensivity;
        }

        if (Math.signum(deltaY) * Math.signum(mActionBarAutoHideSignal) < 0) {
            // deltaY is a motion opposite to the accumulated signal, so reset signal
            mActionBarAutoHideSignal = deltaY;
        } else {
            // add to accumulated signal
            mActionBarAutoHideSignal += deltaY;
        }

        boolean shouldShow = currentY < mActionBarAutoHideMinY ||
                (mActionBarAutoHideSignal <= -mActionBarAutoHideSensivity);
        autoShowOrHideActionBar(shouldShow);
    }

    protected Toolbar getActionBarToolbar() {
        if (mActionBarToolbar == null) {
            mActionBarToolbar = (Toolbar) findViewById(R.id.toolbar_actionbar);
            if (mActionBarToolbar != null) {
                setSupportActionBar(mActionBarToolbar);
            }
        }
        return mActionBarToolbar;
    }

    protected void autoShowOrHideActionBar(boolean show) {
        if (show == mActionBarShown) {
            return;
        }

        mActionBarShown = show;
        onActionBarAutoShowOrHide(show);
    }

    protected void enableActionBarAutoHide(final AbsListView listView) {
        initActionBarAutoHide();
        listView.setOnScrollListener(new AbsListView.OnScrollListener() {
            final static int ITEMS_THRESHOLD = 3;
            int lastFvi = 0;

            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                onMainContentScrolled(firstVisibleItem <= ITEMS_THRESHOLD ? 0 : Integer.MAX_VALUE,
                        lastFvi - firstVisibleItem > 0 ? Integer.MIN_VALUE :
                                lastFvi == firstVisibleItem ? 0 : Integer.MAX_VALUE
                );
                lastFvi = firstVisibleItem;
            }
        });
    }

    private View makeNavDrawerItem(final int itemId, ViewGroup container) {
        boolean selected = getSelfNavDrawerItem() == itemId;
        int layoutToInflate = 0;
        if (itemId == NAVDRAWER_ITEM_SEPARATOR) {
            layoutToInflate = R.layout.navdrawer_separator;
        } else if (itemId == NAVDRAWER_ITEM_SEPARATOR_SPECIAL) {
            layoutToInflate = R.layout.navdrawer_separator;
        } else {
            layoutToInflate = R.layout.navdrawer_item;
        }
        View view = getLayoutInflater().inflate(layoutToInflate, container, false);

        if (isSeparator(itemId)) {
            // we are done
            setAccessibilityIgnore(view);
            return view;
        }

        TextView iconView = (TextView) view.findViewById(R.id.icon);
        TextView titleView = (TextView) view.findViewById(R.id.title);

        int item_id = itemId;
        if (currentUser != null) {
            int iconId = itemId >= 0 && itemId < NAVDRAWER_ICON_RES_ID.length ?
                    NAVDRAWER_ICON_RES_ID[itemId] : 0;
            int titleId = itemId >= 0 && itemId < NAVDRAWER_TITLE_RES_ID.length ?
                    NAVDRAWER_TITLE_RES_ID[itemId] : 0;

             // set icon and text
            iconView.setVisibility(iconId > 0 ? View.VISIBLE : View.GONE);
            if (iconId > 0) {

                Typeface font = Typeface.createFromAsset( getAssets(),
                        "font-awesome-4.3.0/fonts/fontawesome-webfont.ttf" );
                Log.d("iconId non conn", String.valueOf(item_id));
                iconView.setText(NAVDRAWER_FONT_ICON[item_id]);
                iconView.setTypeface(font);
             }
            titleView.setText(getString(titleId));

            formatNavDrawerItem(view, itemId, selected);

            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onNavDrawerItemClicked(itemId);
                }
            });
        }
        else
        {

            int iconId = itemId >= 0 && itemId < NAVDRAWER_ICON_RES_ID_IF_NOT_CONNECTED.length ?
                    NAVDRAWER_ICON_RES_ID_IF_NOT_CONNECTED[itemId] : 0;
            int titleId = itemId >= 0 && itemId < NAVDRAWER_TITLE_RES_ID_IF_NOT_CONNECTED.length ?
                    NAVDRAWER_TITLE_RES_ID_IF_NOT_CONNECTED[itemId] : 0;

            // set icon and text
            iconView.setVisibility(iconId > 0 ? View.VISIBLE : View.GONE);
            if (iconId > 0) {

                Typeface font = Typeface.createFromAsset( getAssets(),
                        "font-awesome-4.3.0/fonts/fontawesome-webfont.ttf" );
                Log.d("iconId  conn", String.valueOf(item_id));
                iconView.setText(NAVDRAWER_FONT_ICON_NOT_CONNECTED[item_id]);
                iconView.setTypeface(font);
            }
            titleView.setText(getString(titleId));

            formatNavDrawerItem(view, itemId, selected);

            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onNavDrawerItemClicked(itemId);
                }
            });
        }



        return view;
    }

    private boolean isSpecialItem(int itemId) {
        return itemId == -99;
    }

    private boolean isSeparator(int itemId) {
        return itemId == NAVDRAWER_ITEM_SEPARATOR || itemId == NAVDRAWER_ITEM_SEPARATOR_SPECIAL;
    }

    private void formatNavDrawerItem(View view, int itemId, boolean selected) {
        if (isSeparator(itemId)) {
            // not applicable
            return;
        }

        TextView iconView = (TextView) view.findViewById(R.id.icon);
        TextView titleView = (TextView) view.findViewById(R.id.title);

        // configure its appearance according to whether or not it's selected
        titleView.setTextColor(selected ?
                getResources().getColor(R.color.material_deep_teal_500):
                getResources().getColor(R.color.navdrawer_text_color));
        iconView.setTextColor(selected ?
                getResources().getColor(R.color.material_deep_teal_500) :
                getResources().getColor(R.color.navdrawer_icon_tint));
        iconView.setTextSize(20);
    }

    protected void onRefreshingStateChanged(boolean refreshing) {
        if (mSwipeRefreshLayout != null) {
            mSwipeRefreshLayout.setRefreshing(refreshing);
        }
    }

    protected void enableDisableSwipeRefresh(boolean enable) {
        if (mSwipeRefreshLayout != null) {
            mSwipeRefreshLayout.setEnabled(enable);
        }
    }

    protected void registerHideableHeaderView(View hideableHeaderView) {
        if (!mHideableHeaderViews.contains(hideableHeaderView)) {
            mHideableHeaderViews.add(hideableHeaderView);
        }
    }

    protected void deregisterHideableHeaderView(View hideableHeaderView) {
        if (mHideableHeaderViews.contains(hideableHeaderView)) {
            mHideableHeaderViews.remove(hideableHeaderView);
        }
    }

    public UIUtils getLUtils() {
        return mLUtils;
    }

    public int getThemedStatusBarColor() {
        return mThemedStatusBarColor;
    }

    public void setNormalStatusBarColor(int color) {
        mNormalStatusBarColor = color;
        if (mDrawerLayout != null) {
            mDrawerLayout.setStatusBarBackgroundColor(mNormalStatusBarColor);
        }
    }

    protected void onActionBarAutoShowOrHide(boolean shown) {
        if (mStatusBarColorAnimator != null) {
            mStatusBarColorAnimator.cancel();
        }
        mStatusBarColorAnimator = ObjectAnimator.ofInt(
                (mDrawerLayout != null) ? mDrawerLayout : mLUtils,
                (mDrawerLayout != null) ? "statusBarBackgroundColor" : "statusBarColor",
                shown ? Color.BLACK : mNormalStatusBarColor,
                shown ? mNormalStatusBarColor : Color.BLACK)
                .setDuration(250);
        if (mDrawerLayout != null) {
            mStatusBarColorAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator valueAnimator) {
                    ViewCompat.postInvalidateOnAnimation(mDrawerLayout);
                }
            });
        }
        mStatusBarColorAnimator.setEvaluator(ARGB_EVALUATOR);
        mStatusBarColorAnimator.start();

        updateSwipeRefreshProgressBarTop();

        for (View view : mHideableHeaderViews) {
            if (shown) {
                view.animate()
                        .translationY(0)
                        .alpha(1)
                        .setDuration(HEADER_HIDE_ANIM_DURATION)
                        .setInterpolator(new DecelerateInterpolator());
            } else {
                view.animate()
                        .translationY(-view.getBottom())
                        .alpha(0)
                        .setDuration(HEADER_HIDE_ANIM_DURATION)
                        .setInterpolator(new DecelerateInterpolator());
            }
        }
    }


    @Override
    public boolean canSwipeRefreshChildScrollUp() {
        return false;
    }

    public static void setAccessibilityIgnore(View view) {
        view.setClickable(false);
        view.setFocusable(false);
        view.setContentDescription("");
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            view.setImportantForAccessibility(View.IMPORTANT_FOR_ACCESSIBILITY_NO);
        }
    }


}