package com.example.pathfinderapp.Adapters;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.pathfinderapp.Models.Language;
import com.example.pathfinderapp.R;

import java.util.ArrayList;

public class AdapterLanguage
        extends RecyclerView.Adapter<AdapterLanguage.ViewHolderItem>
        implements View.OnClickListener{

    private static final String DEFAULT_COLOR = "#ffffff";
    private static final String SELECTED_COLOR = "#b6fcd5";

    private View view;

    private final ArrayList<Language> languagesList;
    private View.OnClickListener listener;

    public AdapterLanguage(java.util.ArrayList<Language> placesList) {
        this.languagesList = placesList;
    }

    @Override
    public AdapterLanguage.ViewHolderItem onCreateViewHolder(ViewGroup parent, int i) {
        view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.languages_recycler_item, null, false);
        view.setOnClickListener(this);
        return new AdapterLanguage.ViewHolderItem(view);
    }

    @Override
    public void onBindViewHolder(AdapterLanguage.ViewHolderItem viewHolder, int i) {
        String aux = languagesList.get(i).getName(); //+ "," + languagesList.get(i).getCountry();
        viewHolder.info.setText(languagesList.get(i).getCode());
        viewHolder.title.setText(languagesList.get(i).getName());
        viewHolder.picture.setImageResource(languagesList.get(i).getPicture());
        if(languagesList.get(i).isAdded())
            view.setBackgroundColor(Color.parseColor(SELECTED_COLOR));

        //viewHolder.setItemCl
    }

    public void setOnClickListener(View.OnClickListener listener){
        this.listener = listener;
    }

    @Override
    public void onClick(View v) {
        changeColor(v);
        if(listener != null)
            listener.onClick(v);
    }

    private void changeColor(View v){
        Drawable background = v.getBackground();
        int color = ((ColorDrawable) background).getColor();
        if(color == Color.parseColor(DEFAULT_COLOR)){
            v.setBackgroundColor(Color.parseColor(SELECTED_COLOR));
        } else {
            v.setBackgroundColor(Color.parseColor(DEFAULT_COLOR));
        }
    }

    @Override
    public int getItemCount() {
        return languagesList.size();
    }

    public class ViewHolderItem extends RecyclerView.ViewHolder {

        //LinearLayout background;
        final TextView title;
        final TextView info;
        final ImageView picture;

        ViewHolderItem(View itemView) {
            super(itemView);
            //background = itemView.findViewById(R.id.backgroundLayout);
            title = itemView.findViewById(R.id.ItemTitle);
            info = itemView.findViewById(R.id.ItemInfo);
            picture = itemView.findViewById(R.id.imageId);
            //background.setBackgroundColor(Color.parseColor(DEFAULT_COLOR));
        }

        /*public void setBackgroundColor(int background) {
            this.background.setBackgroundColor(background);
        }*/
    }
}
