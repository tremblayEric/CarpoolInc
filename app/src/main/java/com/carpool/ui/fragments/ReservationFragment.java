package com.carpool.ui.fragments;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.internal.widget.CompatTextView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.RadioButton;
import android.widget.TextView;

import com.carpool.R;
import com.carpool.model.Offre;
import com.carpool.model.User;
import com.carpool.ui.activities.RechercheActivity;
import com.carpool.ui.activities.RechercheResultatActivity;
import com.carpool.ui.design.CallbackFragment;
import com.carpool.utils.MyResultSearchListAdapter;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.parse.SignUpCallback;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;

public class ReservationFragment extends Fragment {

    private SupportMapFragment fragment;
    private GoogleMap map;

    private static final String KEY_POSITION = "position";
    View result;
    int position_;

    public static ReservationFragment newInstance(int position) {
        ReservationFragment frag = new ReservationFragment();
        Bundle args = new Bundle();

        args.putInt(KEY_POSITION, position);
        frag.setArguments(args);

        return (frag);
    }

    public static String getTitle(int position) {

        switch (position)
        {
            case 0: return String.format("EN COURS");
            case 1: return String.format("ACCEPTEE");
            case 2: return String.format("ANNULEE");
        }
        // gerer la position
        return ""; //(String.format("position at %d", position + 1));
    }

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState) {

        int position = getArguments().getInt(KEY_POSITION, -1);
        position_ = position;

        CompatTextView textView;
        switch (position)
        {
            case 0:
                result = inflater.inflate(R.layout._reservation_encours_layout, null);
                return (result);

            case 1:
                result = inflater.inflate(R.layout._reservation_acceptee_layout, null);

                return (result);
            case 2:
                result = inflater.inflate(R.layout._reservation_annulee_layout, null);
                return result;
        }
        return null;
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