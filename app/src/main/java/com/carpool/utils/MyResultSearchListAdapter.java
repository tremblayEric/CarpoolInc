package com.carpool.utils;

import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.app.FragmentActivity;
import android.transition.Visibility;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.carpool.R;
import com.carpool.model.Offre;
import com.carpool.model.Position;
import com.carpool.model.Reservation;
import com.carpool.ui.activities.ProfilConducteurActivity;
import com.carpool.ui.activities.RechercheResultatActivity;

import com.carpool.ui.fragments.ProfilConducteurFragment;
import com.carpool.ui.fragments.RechercheResultatFragment;

import com.carpool.ui.fragments.WarningConnectionFragment;
import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseInstallation;
import com.parse.ParsePush;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.Serializable;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import android.content.Context;
import java.io.*;
/***
 * Classe qui s'occupe d'afficher les résultats d'une recherche effectuée par un utilisateur
 * dans une liste qui peut être consultée.
 */
public class MyResultSearchListAdapter extends BaseExpandableListAdapter {

    private Activity activity;
    private LayoutInflater inflater;
    public List<Offre> listOffers;
    Context context;
    public static ParseUser userInfoConduct;

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
                holder.txtCompleted = (TextView)convertView.findViewById(R.id.tvCompleted);
                holder.txtAlreadyBooked = (TextView)convertView.findViewById(R.id.tvAlreadyBooked);
            }
            else
                holder = (ListOffersGroupHolder)convertView.getTag();

            DateFormat df = new SimpleDateFormat("hh:mm a");
            Date date = listOffers.get(groupPosition).getHeureDebut();
            holder.txtStartHour.setText(df.format(date));
            Position positionDepart = listOffers.get(groupPosition).getTrajet().getPositionDepart();
            holder.txtStartPoint.setText(getCityNameFromPosition(positionDepart));
            date = listOffers.get(groupPosition).getHeureFin();
            holder.txtEndHour.setText(df.format(date));
            Position positionArrivee = listOffers.get(groupPosition).getTrajet().getPositionArrive();
            holder.txtEndPoint.setText(getCityNameFromPosition(positionArrivee));

            if(listOffers.get(groupPosition).getReservationCount() <= 0)
                holder.txtCompleted.setVisibility(View.VISIBLE);
            else
                holder.txtCompleted.setVisibility(View.GONE);

            // c'est faux
            if(listOffers.get(groupPosition).getUser().fetchIfNeeded().getObjectId().equals(ParseUser.getCurrentUser().getObjectId()))
                holder.txtAlreadyBooked.setVisibility(View.VISIBLE);
            else
                holder.txtAlreadyBooked.setVisibility(View.GONE);

            // Savoir si l'offre a déjà été réservé par l'utilisateur en cours
            if(ParseUser.getCurrentUser() != null){
                ParseQuery<Reservation> query = ParseQuery.getQuery(Reservation.class);
                query.whereEqualTo("offreSource", listOffers.get(groupPosition));
                query.findInBackground(new FindCallback<Reservation>() {
                    @Override
                    public void done(List<Reservation> results, ParseException e) {
                        boolean alreadyBooked = false;
                        for (Reservation reservation : results) {
                            if(reservation.getUserDemandeur().equals(ParseUser.getCurrentUser())){
                                alreadyBooked = true;
                                holder.txtAlreadyBooked.setVisibility(View.VISIBLE);
                            }

                        }
                        if(!alreadyBooked)
                            holder.txtAlreadyBooked.setVisibility(View.INVISIBLE);
                    }
                });
            }
        }
        catch(Exception ex){
        }

        return convertView;
    }

    @Override
    public View getChildView(final int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        final int postition = groupPosition;
        final ViewGroup viewParent = parent;
        try {
            RechercheResultatActivity.offreSelectionne = listOffers.get(groupPosition);
            final ListOffersDetailsHolder holder;
            if (convertView == null) {
                convertView = inflater.inflate(R.layout.listrow_search_offer_detail, null);
                holder = new ListOffersDetailsHolder();
                holder.txtNomConducteur = (TextView) convertView.findViewById(R.id.tvResultSearchNomConducteur);
                holder.txtPlaceDispo = (TextView) convertView.findViewById(R.id.tvResultSearchPlaceDispo);
                holder.txtHeureDepart = (TextView) convertView.findViewById(R.id.tvResultSearchHeure);
                holder.btnReservation = (Button) convertView.findViewById(R.id.btnReservation);
                holder.btnVoirProfil = (Button)convertView.findViewById(R.id.btnProfilCond);

            } else
                holder = (ListOffersDetailsHolder) convertView.getTag();

            holder.txtPlaceDispo.setText(Integer.toString(listOffers.get(groupPosition).getReservationCount()));
            holder.txtHeureDepart.setText("Non spécifié");
            ParseUser user = listOffers.get(groupPosition).getUser().fetchIfNeeded();
            holder.txtNomConducteur.setText(user.get("firstname") + " " + user.get("lastname"));

            // Savoir si l'offre a déjà été réservé par l'utilisateur en cours
            if(ParseUser.getCurrentUser() != null) {
                ParseQuery<Reservation> query = ParseQuery.getQuery(Reservation.class);
                query.whereEqualTo("offreSource", listOffers.get(groupPosition));
                query.findInBackground(new FindCallback<Reservation>() {
                    @Override
                    public void done(List<Reservation> results, ParseException e) {
                        boolean alreadyBooked = false;
                        for (Reservation reservation : results) {
                            if (reservation.getUserDemandeur().equals(ParseUser.getCurrentUser())) {
                                alreadyBooked = true;
                                holder.btnReservation.setVisibility(View.INVISIBLE);
                            }

                        }
                        if (!alreadyBooked)
                            holder.btnReservation.setVisibility(View.VISIBLE);
                    }
                });
            }
            else{
                holder.btnReservation.setVisibility(View.VISIBLE);
            }
            holder.btnReservation.setOnClickListener(new View.OnClickListener() {
                String offreId = listOffers.get(groupPosition).getObjectId();
                @Override
                public void onClick(View v) {
                    if(ParseUser.getCurrentUser() == null){
                        Bundle bundle = new Bundle();
                        bundle.putString("textContent", activity.getString(R.string.content_dialog_result));
                        WarningConnectionFragment wcf = new WarningConnectionFragment();
                        wcf.setArguments(bundle);
                        wcf.show(((FragmentActivity)activity).getSupportFragmentManager(), "Alerte Connexion");
                    }
                    else {
                        //Update le champ reservationCount de l'offre
                        ParseQuery<Offre> query = ParseQuery.getQuery("Offre");
                        query.getInBackground(offreId, new GetCallback<Offre>() {
                            public void done(Offre offre, ParseException e) {
                                if (e == null) {
                                    offre.setReservationCount(offre.getReservationCount() - 1);
                                    offre.saveInBackground();
                                }
                            }
                        });
                        //Créer une nouvelle réservation
                        Reservation reservation = new Reservation();
                        reservation.setReservationStatut(Reservation.ReservationStatut.ATTENTE);
                        reservation.setOffreSource(listOffers.get(postition));
                        reservation.setUserDemandeur(ParseUser.getCurrentUser());
                        reservation.saveInBackground();
                        Integer newCountReservation = listOffers.get(groupPosition).getReservationCount() - 1;
                        holder.txtPlaceDispo.setText(Integer.toString(newCountReservation));
                        holder.btnReservation.setVisibility(View.INVISIBLE);
                        ((TextView)viewParent.findViewById(R.id.tvAlreadyBooked)).setVisibility(View.VISIBLE);
                        if(newCountReservation == 0)
                            ((TextView)viewParent.findViewById(R.id.tvCompleted)).setVisibility(View.VISIBLE);
                        Toast.makeText(activity, "Réservation effectuée",
                                Toast.LENGTH_SHORT).show();

                        // Envoyer un push notification à l'utilisateur qui a posté l'offre
                        ParsePush parsePush = new ParsePush();
                        ParseQuery pQuery = ParseInstallation.getQuery();
                        try {
                            pQuery.whereEqualTo("channels", "Offre");
                            pQuery.whereEqualTo("user", listOffers.get(postition).getUser().fetchIfNeeded());
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                        parsePush.setQuery(pQuery);
                        JSONObject data = new JSONObject();
                        try {
                            data.put("alert", ParseUser.getCurrentUser().getUsername() + " a réservé votre offre");
                            data.put("offreId", listOffers.get(postition).getObjectId());
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        parsePush.setData(data);
                        parsePush.sendInBackground();

                    }
                }
            });
            //RechercheResultatActivity.offreSelectionne = listOffers.get(groupPosition);
            //RechercheResultatFragment.getMapsApiDirectionsUrl();

            // afficher le profil du conduteur
            holder.btnVoirProfil.setOnClickListener(
                    new View.OnClickListener(){
                public void onClick(View v){

                     userInfoConduct = listOffers.get(postition).getUser();

                    //Bundle bundle= new Bundle();

                    ////try{
                       // bundle.putByteArray("conducteurUser", object2Bytes(userInfoConduct));
                        //Log.d("bundle","in MyResultatResearResult");
                    //}catch (IOException e)
                    //{
                      //  Log.d("bundle","out MyResultatResearResult");
                    //}
                    Intent intent = new Intent( activity, ProfilConducteurActivity.class);
//                    intent.putExtra("conducteur", (Parcelable)userInfoConduct);

                   // Bundle bundle = new Bundle();
                    //bundle.putSerializable("my object", myObject);
                     //intent.putExtras(bundle);
                    //myObject = (MyObject) getIntent().getExtras().getSerializable("my object");

                    activity.startActivity(intent);

                }
            });
        }
        catch (Exception ex) {
        }

        return convertView;
    }

     public byte[] object2Bytes( Object o ) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ObjectOutputStream oos = new ObjectOutputStream( baos );
        oos.writeObject( o );
        return baos.toByteArray();
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return false;
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
        String cityName = addresses.get(0).getLocality();
        return  cityName;
    }

    static class ListOffersGroupHolder
    {
        TextView txtStartHour;
        TextView txtStartPoint;
        TextView txtEndHour;
        TextView txtEndPoint;
        TextView txtCompleted;
        TextView txtAlreadyBooked;
    }

    static class ListOffersDetailsHolder
    {
        TextView txtNomConducteur;
        TextView txtPlaceDispo;
        TextView txtHeureDepart;
        Button btnReservation;
        Button btnVoirProfil;
    }
}