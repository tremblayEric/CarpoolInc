package com.carpool.activity;

import android.app.Activity;
import android.location.Address;
import android.location.Geocoder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

import com.carpool.model.Offre;
import com.carpool.model.Position;
import com.carpool.model.User;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.jar.JarEntry;

/**
 * Created by Gaëlle on 3/16/2015.
 */
public class MyResultSearchListAdapter extends BaseExpandableListAdapter {

    private Activity activity;
    private LayoutInflater inflater;
    private List<Offre> listOffers;

    public MyResultSearchListAdapter(Activity act, List<Offre> offers) {
        activity = act;
        inflater = act.getLayoutInflater();
        listOffers = offers;
    }

    @Override
    public int getGroupCount() {
        return listOffers.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return 1;
    }

    @Override
    public Object getGroup(int groupPosition) {
        return listOffers.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return listOffers.get(groupPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return 0;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return 0;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        try {
            final ListOffersGroupHolder holder;
            if (convertView == null) {
                convertView = inflater.inflate(R.layout.listrow_search_offer_group, null);
                holder = new ListOffersGroupHolder();
                holder.txtStartHour = (TextView)convertView.findViewById(R.id.tvResultSearchStartHour);
                holder.txtStartPoint = (TextView)convertView.findViewById(R.id.tvResultSearchStartPoint);
                holder.txtEndHour = (TextView)convertView.findViewById(R.id.tvResultSearchEndHour);
                holder.txtEndPoint = (TextView)convertView.findViewById(R.id.tvResultSearchEndPoint);
            }
            else
                holder = (ListOffersGroupHolder)convertView.getTag();

            DateFormat df = new SimpleDateFormat("hh:'00' a");
            Date date = listOffers.get(groupPosition).getHeureDebut();
            holder.txtStartHour.setText(df.format(date));
            Position positionDepart = listOffers.get(groupPosition).getTrajet().getPositionDepart();
            holder.txtStartPoint.setText(getCityNameFromPosition(positionDepart));
            date = listOffers.get(groupPosition).getHeureDebut();
            holder.txtEndHour.setText(df.format(date));
            Position positionArrivee = listOffers.get(groupPosition).getTrajet().getPositionArrive();
            holder.txtEndPoint.setText(getCityNameFromPosition(positionArrivee));
        }
        catch(Exception ex){

        }

        return convertView;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        try {
            final ListOffersDetailsHolder holder;
            if (convertView == null) {
                convertView = inflater.inflate(R.layout.listrow_search_offer_detail, null);
                holder = new ListOffersDetailsHolder();
                holder.txtRendezvous = (TextView) convertView.findViewById(R.id.tvResultSearchRendezvous);
                holder.txtChutePoint = (TextView) convertView.findViewById(R.id.tvResultSearchChutePoint);
                holder.txtDriver = (TextView) convertView.findViewById(R.id.tvResultSearchDriver);
            } else
                holder = (ListOffersDetailsHolder) convertView.getTag();

            holder.txtRendezvous.setText("Non spécifié");
            holder.txtChutePoint.setText("Non spécifié");
            User user = (User)listOffers.get(groupPosition).getUser();
            holder.txtDriver.setText(user.getFirstname() + " " + user.getLasttname());
        }
        catch (Exception ex) {

        }

        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return false;
    }

    static class ListOffersGroupHolder
    {
        TextView txtStartHour;
        TextView txtStartPoint;
        TextView txtEndHour;
        TextView txtEndPoint;
    }

    private String getCityNameFromPosition(Position position)
    {
        Geocoder geocoder = new Geocoder(activity, Locale.getDefault());
        List<Address> addresses = null;
        try {
            addresses = geocoder.getFromLocation(position.getLatitude(), position.getLongitude(), 1);
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        String cityName = addresses.get(0).getAddressLine(0);
        //String stateName = addresses.get(0).getAddressLine(1);
        //String countryName = addresses.get(0).getAddressLine(2);
        return  cityName;
    }

    static class ListOffersDetailsHolder
    {
        TextView txtRendezvous;
        TextView txtChutePoint;
        TextView txtDriver;
    }



}
