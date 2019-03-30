package com.example.pathfinderapp.Adapters;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.pathfinderapp.Models.Language;
import com.example.pathfinderapp.R;

import java.util.ArrayList;

public class AdapterLanguageHorizontal extends RecyclerView.Adapter<AdapterLanguageHorizontal.ViewHolderItem>
        implements View.OnClickListener {

    private final ArrayList<Language> languagesList;
    private View.OnClickListener listener;

    public  AdapterLanguageHorizontal(java.util.ArrayList<Language> placesList){
        this.languagesList = placesList;
    }

    @NonNull
    @Override
    public ViewHolderItem onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.languages_recycler_horizontal_item, null, false);
        view.setOnClickListener(this);
        return new AdapterLanguageHorizontal.ViewHolderItem(view);
    }

    @Override
    public void onBindViewHolder(AdapterLanguageHorizontal.ViewHolderItem viewHolder, int i) {
        String aux = languagesList.get(i).getCode(); // + "," + languagesList.get(i).getCode();
        viewHolder.title.setText(aux);
        viewHolder.picture.setImageResource(languagesList.get(i).getPicture());
    }

    @Override
    public int getItemCount() {
        return languagesList.size();
    }

    @Override
    public void onClick(View view) {
        if(listener != null)
            listener.onClick(view);

    }

    public class ViewHolderItem extends RecyclerView.ViewHolder {

        final TextView title;
        final ImageView picture;

        ViewHolderItem(View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.languageName);
            picture = itemView.findViewById(R.id.imageId);
        }
    }
}
