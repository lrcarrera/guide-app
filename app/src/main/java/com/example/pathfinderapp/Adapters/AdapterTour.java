package com.example.pathfinderapp.Adapters;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.pathfinderapp.GuideProfileActivity;
import com.example.pathfinderapp.MainActivity;
import com.example.pathfinderapp.MockValues.DefValues;
import com.example.pathfinderapp.Models.Post;
import com.example.pathfinderapp.Models.Review;
import com.example.pathfinderapp.Models.User;
import com.example.pathfinderapp.ProfileDialog;
import com.example.pathfinderapp.ProfileFragment;
import com.example.pathfinderapp.R;
import com.example.pathfinderapp.ToursFragment;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.ramotion.foldingcell.FoldingCell;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.ListIterator;
import java.util.Locale;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import static android.provider.AlarmClock.EXTRA_MESSAGE;

public class AdapterTour extends RecyclerView.Adapter<AdapterTour.ViewHolderItem>
        implements View.OnClickListener {

    private Context context;
    private View.OnClickListener listener;
    private ArrayList<Post> searchList;
    private final ArrayList<Post> originalSearchList;
    private FragmentManager fragmentManager;
    private final boolean isAdded;
    private final ToursFragment searchFragment;
    //private FragmentManager fragmentManager;

    public AdapterTour(FragmentManager fragmentManager, ArrayList<Post> searchList, boolean isAdded, ToursFragment searchFragment) {
        this.searchList = searchList;
        this.fragmentManager = fragmentManager;
        this.originalSearchList = new ArrayList<>();
        this.isAdded = isAdded;
        this.searchFragment = searchFragment;
        originalSearchList.addAll(searchList);
    }

    public void setToursList(ArrayList<Post> searchList){
        this.searchList = searchList;
    }

    @Override
    public void onClick(View view) {
        if(listener != null)
            listener.onClick(view);
    }

    public void setOnClickListener(View.OnClickListener listener){
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolderItem onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        View view = LayoutInflater.from(context)
                .inflate(R.layout.tour_layout, null, false);

        view.setOnClickListener(this);
        return new AdapterTour.ViewHolderItem(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolderItem viewHolder, int i) {
        Post current = searchList.get(i);
        processPostData(current, viewHolder);
        checkNightMode(current.getStartHour(), current.getEndHour(), viewHolder);
        createLanguagesRecycler(viewHolder, current);
        processProfilePicture(current, viewHolder);
        viewHolder.adapterTour = this;
    }

    private void processPostData(Post current, ViewHolderItem viewHolder){
        viewHolder.setPost(current);
        viewHolder.info.setText(String.valueOf(current.getGuide().getName()));
        viewHolder.title.setText(current.getPlace().getName());
        viewHolder.topInfo.setText(String.valueOf(current.getGuide().getName()));
        viewHolder.topTitle.setText(current.getPlace().getName());
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        viewHolder.dateContent.setText(sdf.format(current.getDueTo()));
        viewHolder.fromHourNumber.setText(current.getStartHour());
        viewHolder.toHourNumber.setText(current.getEndHour());
        touristsAllowed(current, viewHolder);
        viewHolder.priceNumber.setText(String.valueOf(current.getPrice()));
        String auxScore = String.valueOf(current.getGuide().getScore());
        viewHolder.topItemScore.setText(auxScore);
        viewHolder.itemScore.setText(auxScore);
        String aux = "+" + String.valueOf(current.getLanguages().size());
        viewHolder.topItemLanguages.setText(aux);
        viewHolder.itemLanguages.setText(aux);
        viewHolder.mapPosition = current.getPlace().getCoord();
        viewHolder.places = current.getPlaces();
        if(isAdded){
            viewHolder.messageAppearance();
        } else {
            viewHolder.inscriptionAppearance();
        }
    }

    private void touristsAllowed(Post current, ViewHolderItem viewHolder){
        int numTouristsAllowed = current.getNumTourists();
        int currentNumTourists = current.getTourists().size();
        String value = String.valueOf(currentNumTourists) + " / " + String.valueOf(numTouristsAllowed);
        viewHolder.touristAllowedNumber.setText(value);
        viewHolder.inscriptionStatus(currentNumTourists, numTouristsAllowed);
    }

    private void processProfilePicture(Post current, ViewHolderItem viewHolder){
        Bitmap bitmap = null;
        bitmap = BitmapFactory.decodeResource(context.getResources(), current.getGuide().getImage());
        bitmap = CroppedImage.getCroppedBitmap(bitmap);
        viewHolder.picture.setImageBitmap(bitmap);
        viewHolder.topPicture.setImageBitmap(bitmap);
    }

    private void createLanguagesRecycler(ViewHolderItem viewHolder, Post current){
        viewHolder.languages.setLayoutManager(new LinearLayoutManager(context, LinearLayout.HORIZONTAL, false));
        AdapterLanguageHorizontal adapterLanguages = new AdapterLanguageHorizontal(current.getLanguages());
        viewHolder.languages.setAdapter(adapterLanguages);
        viewHolder.languages.setItemAnimator(new DefaultItemAnimator());
    }

    private void checkNightMode(String startHour, String endHour, ViewHolderItem viewHolderItem){
        String[] time;
        time = startHour.split(":");
        if(Integer.parseInt(time[0]) >= 21)
            viewHolderItem.isNight = true;

        time = endHour.split(":");
        if(Integer.parseInt(time[0]) >= 21)
            viewHolderItem.isNight = true;
    }

    @Override
    public int getItemCount() {
        return searchList.size();
    }

    public void filter(String newText) {
        String toSearch = newText.toLowerCase();

        searchList = new ArrayList<>();
        searchList.addAll(originalSearchList);

        if (toSearch.length() == 0)
        {
            searchList = DefValues.getMockPostList();
            notifyDataSetChanged();
            return;
        }

        ListIterator<Post> itr = searchList.listIterator();
        while (itr.hasNext())
        {
            if (itr.next().getPlace().getName().toLowerCase().contains(toSearch))
                continue;

            itr.remove();
        }

        notifyDataSetChanged();
    }

    private void reset(){
        searchList = new ArrayList<>(searchList);
        notifyDataSetChanged();
        searchFragment.resetController();
    }

    public void removeItem(int pos) {
        searchList.remove(pos);
        notifyItemRemoved(pos);
    }

    public class ViewHolderItem extends RecyclerView.ViewHolder implements OnMapReadyCallback {

        //LinearLayout background;
        Post post;
        final TextView dateContent;
        final TextView fromHourNumber;
        final TextView toHourNumber;
        final TextView touristAllowedNumber;
        final TextView priceNumber;
        final TextView itemScore;
        final TextView itemLanguages;
        final RecyclerView languages;
        MapView mapView;
        GoogleMap mMap;
        final FloatingActionButton topMapButton;
        final FloatingActionButton mapButton;
        final FloatingActionButton inscriptionButton;
        final TextView  title;
        final TextView info;
        final ImageView picture;
        final TextView  topTitle;
        final TextView topInfo;
        final TextView topItemScore;
        final TextView topItemLanguages;
        final ImageView topPicture;
        AdapterTour adapterTour;

        FoldingCell foldingCell;
        LatLng mapPosition;
        List<Marker> places;
        boolean isNight = false;
        int currentTourists,  numTouristsAllowed;

        ProfileDialog dialog;
        View view;
        //boolean isAdded = false;

        ViewHolderItem(View itemView) {
            super(itemView);
            dateContent = itemView.findViewById(R.id.dateContent);
            fromHourNumber = itemView.findViewById(R.id.fromHourNumber);
            toHourNumber = itemView.findViewById(R.id.toHourNumber);
            touristAllowedNumber = itemView.findViewById(R.id.touristAllowedNumber);
            priceNumber = itemView.findViewById(R.id.priceNumber);
            languages = itemView.findViewById(R.id.languages);
            title = itemView.findViewById(R.id.ItemTitle);
            info = itemView.findViewById(R.id.ItemInfo);
            picture = itemView.findViewById(R.id.imageId);
            picture.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    profilePopupSettings();
                }
            });
            topTitle = itemView.findViewById(R.id.topItemTitle);
            topInfo = itemView.findViewById(R.id.topItemInfo);
            topPicture = itemView.findViewById(R.id.topImageId);
            topPicture.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    profilePopupSettings();
                }
            });
            itemScore = itemView.findViewById(R.id.ItemScore);
            topItemScore = itemView.findViewById(R.id.topItemScore);
            itemLanguages = itemView.findViewById(R.id.ItemLanguages);
            topItemLanguages = itemView.findViewById(R.id.topItemLanguages);


            mapButton = itemView.findViewById(R.id.mapButton);
            topMapButton = itemView.findViewById(R.id.topMapButton);
            mapButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                   mapPopupSettings();
                }
            });
            topMapButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mapPopupSettings();
                }
            });
            inscriptionButton = itemView.findViewById(R.id.inscribeButton);
            //if(isAdded)
               // messageAppearance();profilePopupSettings()
            final FoldingCell fc = itemView.findViewById(R.id.folding_cell);
            fc.initialize(30,1000, Color.DKGRAY, 2);
            // attach click listener to folding cell
            fc.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    fc.toggle(false);

                    if (adapterTour.searchFragment == null || !adapterTour.searchFragment.swipeController.rectangleActive)
                        return;

                    adapterTour.reset();
                    adapterTour.searchFragment.swipeController.rectangleActive = false;
                }
            });


        }

        private void inscriptionStatus(int currentTourists, int numTouristsAllowed){
            this.currentTourists = currentTourists;
            this.numTouristsAllowed = numTouristsAllowed;
            if(currentTourists == numTouristsAllowed){
                touristAllowedNumber.setTextColor(ContextCompat.getColor(context, R.color.red));
            } else {
                touristAllowedNumber.setTextColor(ContextCompat.getColor(context, R.color.green));
            }
        }

        private void inscriptionAppearance(){
            if(currentTourists == numTouristsAllowed){
                inscriptionButton.hide();
            } else {
                inscriptionButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                       makeSuccessToast();
                       inscribe();
                    }
                });
            }
        }

        private void inscribe(){
            post.addTourist(new User());
            DefValues.AddPostToToursList(post);
            MainActivity mainActivity = (MainActivity) context;
            mainActivity.moveToToursPage();
            inscriptionButton.hide();
        }

        private void makeSuccessToast(){
            Toast toast = Toast.makeText(context, R.string.inscriptionSuccessful, Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
            toast.show();
        }

        private void setPost(Post post){
            this.post = post;
        }

        private void messageAppearance(){
            //inscriptionButton.setBackgroundColor(ContextCompat.getColor(context, R.color.com_facebook_blue));
            //inscriptionButton.setBackgroundTintList(ContextCompat.getColorStateList(context,  R.color.com_facebook_blue));
            inscriptionButton.setImageResource(R.drawable.ic_action_chat);
            inscriptionButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    final Dialog myDialog = new Dialog(context);
                    myDialog.setContentView(R.layout.review_popup);
                    final EditText editText = myDialog.findViewById(R.id.editText);
                    Button close = myDialog.findViewById(R.id.btnCancel);
                    close.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            myDialog.cancel();
                        }
                    });
                    Button accept = myDialog.findViewById(R.id.btnAccept);
                    accept.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            String text = editText.getText().toString();
                            if(text.length() == 0) {
                                TextView errorMessage = myDialog.findViewById(R.id.editText);
                                errorMessage.setVisibility(View.VISIBLE);
                            } else {
                                Review review = new Review(text, DefValues.getUserInContext(), new Date());
                                //Toast toast = Toast.makeText(context, review, Toast.LENGTH_SHORT);
                                /*Falta añadirlo al usuario que hizo el tour*/
                                /*User user = DefValues.getPlayerInContext();
                                user.getReviews().add(new Review(()))*/
                            }

                        }
                    });
                    //addLanguages(myDialog);
           /* checkboxNotifications = (CheckBox) myDialog.findViewById(R.id.checkbox_notifications);

            RadioGroup radioGroupConnectivity = (RadioGroup) myDialog.findViewById(R.id.radio_group_connectivity);
            radioWifi = (RadioButton) myDialog.findViewById(R.id.wifi);
            radioWifiAndMore = (RadioButton) myDialog.findViewById(R.id.wifiandmore);*/


                    myDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                    myDialog.show();
                }
            });



            /*inscriptionButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Toast toast = Toast.makeText(context, "Not implemented yet", Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
                    toast.show();
                }
            });*/
        }

        private void inscribeTour(){

        }

        public void dismiss(View view){
            this.view = view;
            /*FragmentTransaction ft2 = fragmentManager.beginTransaction();
            ft2.remove(fragmentManager.findFragmentByTag("profile_fragment"));
            ft2.commit();*/
            //dismiss();
            dialog.dismiss();
        }

        private void profilePopupSettings(){
            dialog = new ProfileDialog(post.getGuide(), fragmentManager, view, this, post.getPlace());
            //Fragment fragment = ProfileFragment.newInstance(post.getGuide());
            FragmentTransaction ft2 = fragmentManager.beginTransaction();
            //ft2.add(R.id.profile_popup_linear, new ProfileFragment(), "profile_fragment");
            //ft2.commit();
            //dialog.show(fragmentManager, "profile_fragment");
            dialog.show(ft2, "profile_fragment_popup");

            //dialog.sh
            //final Dialog auxDialog = new Dialog(context);

            //auxDialog.setContentView(R.layout.profile_popup);
            //View view = auxDialog.findViewById(R.id.profile_popup_fragment);

           /* Fragment fragment = ProfileFragment.newInstance(post.getGuide());
            fragmentManager
                    .beginTransaction()
                    .replace(R.id.profile_popup_fragment, fragment, "profile_fragment")
                    .commit();
            //View btnClose = auxDialog.findViewById(R.id.profile_popup_fragment);
            /*
            FragmentTransaction ft = getActivity().getFragmentManager().beginTransaction();
            SectionDescriptionFragment bdf = new SectionDescriptionFragment();
            ft.replace(R.id.book_description_fragment, bdf);
            ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
            ft.addToBackStack(null);
            ft.commit();
                        */
            /*ProfileDialog dialog = new ProfileDialog();
            //FragmentManager fragmentManager =
            Fragment fragment = ProfileFragment.newInstance(post.getGuide());
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.add(R.id.profile_popup_linear, fragment);
            dialog.show(fragmentTransaction, "profile_fragment");
            //dialog.show();
            //Intent intent = new Intent(context, GuideProfileActivity.class);
            //intent.putExtra("guide", g);
            //context.startActivity(intent);
            //ProfileDialog dialog = ProfileDialog.newInstance(post.getGuide(), fragmentManager, context);
            //dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            //dialog.setContentView(R.layout.profile_popup);

            //Fragment fragment = ProfileFragment.newInstance(post.getGuide());
            //FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            //fragmentTransaction.add(R.id.profile_layout, fragment);

            //dialog.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            //dialog.show(fragment, "profile_fragment");
            //dialog.aux();
            //dialog.show();
            //fragmentTransaction.commit();
            //dialogFragment.show(fragmentManager, "profile_fragment");
            //Dialog auxDialog = new ProfileDialog(context, fragmentManager);

            //final Dialog auxDialog = new Dialog(context);
            //auxDialog.setContentView(R.layout.profile_popup);
            //View auxFragment = auxDialog.findViewById(R.id.profile_popup);

            //ProfileFragment fragment = new ProfileFragment();
            //FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            //fragmentTransaction.add(R.id.profile_popup, fragment);
            //fragmentTransaction.commit();

            /*FloatingActionButton btnClose = auxDialog.findViewById(R.id.btnClose);
            MapView map = auxDialog.findViewById(R.id.map);
            if (map != null) {
                // Initialise the MapView
                map.onCreate(null);
                // Set the map ready callback to receive the GoogleMap object
                map.getMapAsync(this);
            }*/
            //ViewGroup.LayoutParams l = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            //dialog.addContentView(fragment);
            //auxDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            //auxDialog.show();
            /*btnClose.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    auxDialog.dismiss();
                }
            });*/

        }

        private void mapPopupSettings(){
            final Dialog auxDialog = new Dialog(context);
            auxDialog.setContentView(R.layout.map_popup);
            FloatingActionButton btnClose = auxDialog.findViewById(R.id.btnClose);
            MapView map = auxDialog.findViewById(R.id.map);
            if (map != null) {
                // Initialise the MapView
                map.onCreate(null);
                // Set the map ready callback to receive the GoogleMap object
                map.getMapAsync(this);
            }
            auxDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            auxDialog.show();
            btnClose.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    auxDialog.dismiss();
                }
            });

        }

        @Override
        public void onMapReady(GoogleMap googleMap) {
            mMap = googleMap;
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(mapPosition,11));
            mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
            if(isNight)
                setMapStyle();

            for (Marker marker : places){
                mMap.addMarker(new MarkerOptions().position(marker.getPosition()));
            }
            //new NamedLocation("New York", new LatLng(40.750580, -73.993584)),
        }

        void setMapStyle(){
            mMap.setMapStyle(MapStyleOptions.loadRawResourceStyle(context, R.raw.mapstyle_night));
        }


    }
}
