package com.example.pathfinderapp.Adapters;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.pathfinderapp.AdapterSearch;
import com.example.pathfinderapp.Models.Post;
import com.example.pathfinderapp.R;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.ramotion.foldingcell.FoldingCell;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

public class AdapterTour extends RecyclerView.Adapter<AdapterTour.ViewHolderItem>
        implements View.OnClickListener {

    private Context context;
    private View.OnClickListener listener;
    private ArrayList<Post> searchList;
    private FragmentManager fragmentManager;

    public AdapterTour(ArrayList<Post> searchList, FragmentManager fragmentManager) {
        this.searchList = searchList;
        this.fragmentManager = fragmentManager;
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
        viewHolder.info.setText(String.valueOf(current.getGuide().getScore()));
        viewHolder.title.setText(current.getGuide().getName());
        //viewHolder.picture.setImageResource(current.getGuide().getImage());

        viewHolder.topInfo.setText(String.valueOf(current.getGuide().getScore()));
        viewHolder.topTitle.setText(current.getGuide().getName());
        //viewHolder.topPicture.setImageResource(current.getGuide().getImage());
        //viewHolder.dateContent.setText(current.getDueTo().toString());
        viewHolder.fromHourNumber.setText(current.getStartHour());
        viewHolder.toHourNumber.setText(current.getEndHour());
        viewHolder.touristAllowedNumber.setText(String.valueOf(current.getNumTourists()));
        viewHolder.priceNumber.setText(String.valueOf(current.getPrice()));
        //TextView dateContent, fromHourNumber, toHourNumber, touristAllowedNumber, priceNumber;
    }

    @Override
    public int getItemCount() {
        return searchList.size();
    }

    public class ViewHolderItem extends RecyclerView.ViewHolder {

        //LinearLayout background;
        TextView dateContent, fromHourNumber, toHourNumber, touristAllowedNumber, priceNumber;
        RecyclerView languages;
        FrameLayout map;

        TextView  title, info;
        ImageView picture;

        TextView  topTitle, topInfo;
        ImageView topPicture;

        FoldingCell foldingCell;

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



            final FoldingCell fc = itemView.findViewById(R.id.folding_cell);
            fc.initialize(30,1000, Color.DKGRAY, 2);
            // attach click listener to folding cell
            fc.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    fc.toggle(false);
                }
            });

            FragmentManager fm = fragmentManager;
            SupportMapFragment supportMapFragment =  SupportMapFragment.newInstance();
            fm.beginTransaction().replace(R.id.map, supportMapFragment).commit();
            supportMapFragment.getMapAsync(new OnMapReadyCallback() {
                @Override
                public void onMapReady(GoogleMap googleMap) {
                    GoogleMap mMap = googleMap;

                }
            });

        }
    }
}
