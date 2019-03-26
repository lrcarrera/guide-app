package com.example.pathfinderapp.Adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.pathfinderapp.Models.Place;
import com.example.pathfinderapp.R;

import java.util.ArrayList;

public class AdapterPlace
        extends RecyclerView.Adapter<AdapterPlace.ViewHolderItem>
        implements View.OnClickListener{

    private ArrayList<Place> placesList;
    private View.OnClickListener listener;

    public AdapterPlace(ArrayList<Place> placesList) {
        this.placesList = placesList;
    }

    @Override
    public ViewHolderItem onCreateViewHolder(ViewGroup parent, int i) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recycler_location, null, false);
        view.setOnClickListener(this);
        return new ViewHolderItem(view);
    }

    @Override
    public void onBindViewHolder(ViewHolderItem viewHolder, int i) {
        String aux = placesList.get(i).getName() + "," + placesList.get(i).getCountry();
        viewHolder.info.setText(aux);
        viewHolder.title.setText(placesList.get(i).getName());
        viewHolder.picture.setImageResource(placesList.get(i).getPicture());
    }

    public void setOnClickListener(View.OnClickListener listener){
        this.listener = listener;
    }

    @Override
    public void onClick(View v) {
        if(listener != null)
            listener.onClick(v);
    }

    @Override
    public int getItemCount() {
        return placesList.size();
    }

    public class ViewHolderItem extends RecyclerView.ViewHolder {

        TextView title, info;
        ImageView picture;

        public ViewHolderItem(View itemView) {
            super(itemView);

            title = itemView.findViewById(R.id.ItemTitle);
            info = itemView.findViewById(R.id.ItemInfo);
            picture = itemView.findViewById(R.id.imageId);
        }
    }
}