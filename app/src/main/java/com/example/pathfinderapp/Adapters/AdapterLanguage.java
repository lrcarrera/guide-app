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

    public AdapterLanguage(java.util.ArrayList<Language> languages) {
        this.languagesList = languages;
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
        viewHolder.info.setText(languagesList.get(i).getCode());
        viewHolder.title.setText(languagesList.get(i).getName());
        viewHolder.setPicture(languagesList.get(i).getCode());

        if(languagesList.get(i).isAdded())
            view.setBackgroundColor(Color.parseColor(SELECTED_COLOR));
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

    class ViewHolderItem extends RecyclerView.ViewHolder {

        final TextView title;
        final TextView info;
        final ImageView picture;

        ViewHolderItem(View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.ItemTitle);
            info = itemView.findViewById(R.id.ItemInfo);
            picture = itemView.findViewById(R.id.imageId);
        }

        public void setPicture(String code) {
            switch (code){
                case "FR":
                    picture.setImageResource(R.drawable.french_flag);
                    break;
                case "DE":
                    picture.setImageResource(R.drawable.german_flag);
                    break;
                case "EN":
                    picture.setImageResource(R.drawable.britain_flag);
                    break;
                case "IT":
                    picture.setImageResource(R.drawable.italy_flag);
                    break;
                case "ES":
                    picture.setImageResource(R.drawable.spain_flag);
                    break;
                case "JP":
                    picture.setImageResource(R.drawable.japanese_flag);
                    break;
                default:
                    break;
            }
        }
    }
}
