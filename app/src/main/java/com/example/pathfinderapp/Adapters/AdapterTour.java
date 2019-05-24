package com.example.pathfinderapp.Adapters;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.util.Log;
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
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.protobuf.compiler.PluginProtos;
import com.ramotion.foldingcell.FoldingCell;

import java.io.Console;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.ListIterator;
import java.util.Locale;
import java.util.Map;

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
    private MainActivity activity;
    //private FragmentManager fragmentManager;

    public AdapterTour(MainActivity mainActivity, FragmentManager fragmentManager, ArrayList<Post> searchList, boolean isAdded, ToursFragment searchFragment) {
        this.searchList = searchList;
        this.fragmentManager = fragmentManager;
        this.originalSearchList = new ArrayList<>();
        this.isAdded = isAdded;
        this.searchFragment = searchFragment;
        originalSearchList.addAll(searchList);
        this.activity = mainActivity;
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
        viewHolder.markers = current.getMarkerOptions();

        String postUid = current.getGuide().getUid();
        if(DefValues.getUserInContext() == null)
            return;

        String currentUserId = DefValues.getUserInContext().getUid();

        if(postUid != null && currentUserId != null){
            if(!postUid.equals(currentUserId) ){
                if(isAdded){
                    viewHolder.messageAppearance();
                } else {
                    viewHolder.inscriptionAppearance();
                }
            } else {
                viewHolder.setInscriptionButtonVisibility();
            }
        }
    }

    private void touristsAllowed(Post current, ViewHolderItem viewHolder){
        int numTouristsAllowed = current.getNumTourists();
        int currentNumTourists = current.getTourists().size();
        String value = String.valueOf(currentNumTourists) + " / " + String.valueOf(numTouristsAllowed);
        viewHolder.touristAllowedNumber.setText(value);
        viewHolder.inscriptionStatus(currentNumTourists, numTouristsAllowed);
    }

    private void processProfilePicture(Post current, final ViewHolderItem viewHolder){
        //Bitmap bitmap = null;

        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageRef = storage.getReferenceFromUrl("gs://pathfinder-50817.appspot.com").child(current.getGuide().getImage() + ".png");
        try {
            final File localFile = File.createTempFile("images", "png");
            storageRef.getFile(localFile).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                    Bitmap bitmap = BitmapFactory.decodeFile(localFile.getAbsolutePath());
                    bitmap = CroppedImage.getCroppedBitmap(bitmap);
                    viewHolder.picture.setImageBitmap(bitmap);
                    viewHolder.topPicture.setImageBitmap(bitmap);
                    //mImageView.setImageBitmap(bitmap);

                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception exception) {
                }
            });
        } catch (IOException e ) {
            //System.out.println("Vergassso");
        }
        /*bitmap = BitmapFactory.decodeResource(context.getResources());
        if(bitmap == null){
            bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.ic_user);
        }*/
    }

    /*private void getUserProfilePicture(int image){
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageRef = storage.getReferenceFromUrl("gs://pathfinder-50817.appspot.com").child(image + ".png");
    }*/

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
        //MainActivity activity;
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
        List<MarkerOptions> markers;
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

        private void setInscriptionButtonVisibility(){
            inscriptionButton.hide();
            //inscriptionButton.setVisibility(View.INVISIBLE);
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
                       //makeSuccessToast();
                       inscribe();
                    }
                });
            }
        }

        private void inscribe(){
            post.addTourist(new User());
            DefValues.AddPostToToursList(post);
            Dialog confirmDialog = new AlertDialog.Builder(context)
                    .setTitle("Confirmación")
                    .setMessage("Quieres entrar?")
                    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            final Map<String, Object> newUser;
                            User user = DefValues.getUserInContext();
                            user.addPost(post.getUuid());
                            newUser = DefValues.getUserInContext().addToHashMap();
                            FirebaseFirestore db = FirebaseFirestore.getInstance();
                            DefValues.getUserInContextDocument().update("user", newUser);
                            DefValues.addUserRelatedPost(post);
                            /*Se tiene que sacar esta solo para guardar mierda posts de ejemplo */
                            //db.collection("posts").add(post);

                        }
                    })

                    // A null listener allows the button to dismiss the dialog and take no further action.
                    .setNegativeButton(android.R.string.no, null)
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .show();

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

                                User user = DefValues.getUserInContext();
                                Review review = new Review(text, DefValues.getUserInContext().getUid(), new Date());
                                final User guide = post.getGuide();
                                guide.addReview(review);
                                FirebaseFirestore db = FirebaseFirestore.getInstance();
                                db.collection("users").whereEqualTo("user.uid", guide.getUid() )
                                        .get()
                                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                            @Override
                                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                                if (task.isSuccessful()) {
                                                    for (QueryDocumentSnapshot document : task.getResult()) {
                                                        document.getReference().update("user",guide.addToHashMap());
                                                        myDialog.dismiss();
                                                        //DefValues.setDocumentReference(document.getReference());
                                                        //DefValues.setUserInContext(document);
                                                    }
                                                } else {
                                                    Log.w("ERRORDOCUMENT", "Error getting documents.", task.getException());
                                                }
                                            }
                                        });
                                db.collection("posts").whereEqualTo("uuid", post.getUuid() )
                                        .get()
                                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                            @Override
                                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                                if (task.isSuccessful()) {
                                                    for (QueryDocumentSnapshot document : task.getResult()) {
                                                        document.getReference().update(post.addToHashMap());
                                                        /*document.getReference().update("user",guide);
                                                        myDialog.dismiss();*/
                                                        //DefValues.setDocumentReference(document.getReference());
                                                        //DefValues.setUserInContext(document);
                                                    }
                                                } else {
                                                    Log.w("ERRORDOCUMENT", "Error getting documents.", task.getException());
                                                }
                                            }
                                        });
                                /*FirebaseFirestore db = FirebaseFirestore.getInstance();
                                DocumentReference documentReference = db.collection("users").document("A8Eq03Drre2YCTSVXtTQ");
                                documentReference.set(review);*/
                                /*Review review = null;
                                user.addReview(review);
                                FirebaseFirestore db = FirebaseFirestore.getInstance();
                                DocumentReference documentReference = db.collection("users").document("A8Eq03Drre2YCTSVXtTQ");
                                documentReference.set(review);*/
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
            if (DefValues.getUserInContext() == null)
                return;

            if(post.getGuide().getUid().equals(DefValues.getUserInContext().getUid())){
                this.adapterTour.activity.moveToProfilePage();
            } else {
                dialog = new ProfileDialog(post.getGuide(), fragmentManager, view, this, post.getPlace());
                FragmentTransaction ft2 = fragmentManager.beginTransaction();
                dialog.show(ft2, "profile_fragment_popup");
            }
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

            if(places == null){
               for(MarkerOptions marker: markers){
                   mMap.addMarker(marker);
               }
            } else {
                for (Marker marker : places){
                    mMap.addMarker(new MarkerOptions().position(marker.getPosition()));
                }
            }


            //new NamedLocation("New York", new LatLng(40.750580, -73.993584)),
        }

        void setMapStyle(){
            mMap.setMapStyle(MapStyleOptions.loadRawResourceStyle(context, R.raw.mapstyle_night));
        }


    }
}
