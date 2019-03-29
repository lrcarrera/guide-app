package com.example.pathfinderapp.Adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.pathfinderapp.AdapterSearch;
import com.example.pathfinderapp.MockValues.DefValues;
import com.example.pathfinderapp.Models.Post;
import com.example.pathfinderapp.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.ramotion.foldingcell.FoldingCell;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.ListIterator;
import java.util.Locale;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class AdapterTour extends RecyclerView.Adapter<AdapterTour.ViewHolderItem>
        implements View.OnClickListener {

    private Context context;
    private View.OnClickListener listener;
    private ArrayList<Post> searchList;
    private ArrayList<Post> originalSearchList;
    private FragmentManager fragmentManager;

    public AdapterTour(ArrayList<Post> searchList, FragmentManager fragmentManager) {
        this.searchList = searchList;
        this.fragmentManager = fragmentManager;
        this.originalSearchList = new ArrayList<>();

        originalSearchList.addAll(searchList);
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
    }

    private void processPostData(Post current, ViewHolderItem viewHolder){
        viewHolder.info.setText(String.valueOf(current.getGuide().getName()));
        viewHolder.title.setText(current.getPlace().getName());
        viewHolder.topInfo.setText(String.valueOf(current.getGuide().getName()));
        viewHolder.topTitle.setText(current.getPlace().getName());
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        viewHolder.dateContent.setText(sdf.format(current.getDueTo()));
        viewHolder.fromHourNumber.setText(current.getStartHour());
        viewHolder.toHourNumber.setText(current.getEndHour());
        viewHolder.touristAllowedNumber.setText(String.valueOf(current.getNumTourists()));
        viewHolder.priceNumber.setText(String.valueOf(current.getPrice()));
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

    public class ViewHolderItem extends RecyclerView.ViewHolder implements OnMapReadyCallback {

        //LinearLayout background;
        TextView dateContent, fromHourNumber, toHourNumber, touristAllowedNumber, priceNumber;
        RecyclerView languages;
        MapView mapView;
        GoogleMap mMap;

        TextView  title, info;
        ImageView picture;

        TextView  topTitle, topInfo;
        ImageView topPicture;

        FoldingCell foldingCell;
        boolean isNight = false;

        public ViewHolderItem(View itemView) {
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
            topTitle = itemView.findViewById(R.id.topItemTitle);
            topInfo = itemView.findViewById(R.id.topItemInfo);
            topPicture = itemView.findViewById(R.id.topImageId);
            mapView = itemView.findViewById(R.id.map);
            if (mapView != null) {
                // Initialise the MapView
                mapView.onCreate(null);
                // Set the map ready callback to receive the GoogleMap object
                mapView.getMapAsync(this);
            }


            final FoldingCell fc = itemView.findViewById(R.id.folding_cell);
            fc.initialize(30,1000, Color.DKGRAY, 2);
            // attach click listener to folding cell
            fc.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    fc.toggle(false);
                }
            });


        }

        @Override
        public void onMapReady(GoogleMap googleMap) {
            mMap = googleMap;
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(40.750580, -73.993584),11));
            mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
            if(isNight)
                setMapStyle();
            //new NamedLocation("New York", new LatLng(40.750580, -73.993584)),
        }

        public void setMapStyle(){
            mMap.setMapStyle(MapStyleOptions.loadRawResourceStyle(context, R.raw.mapstyle_night));
        }
    }
}
