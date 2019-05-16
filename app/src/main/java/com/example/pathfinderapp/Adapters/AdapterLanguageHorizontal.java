package com.example.pathfinderapp.Adapters;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.pathfinderapp.Models.Language;
import com.example.pathfinderapp.R;

import java.util.ArrayList;
import java.util.List;

public class AdapterLanguageHorizontal extends RecyclerView.Adapter<AdapterLanguageHorizontal.ViewHolderItem>
        implements View.OnClickListener {

    private final ArrayList<Language> languagesList;
    private View.OnClickListener listener;
    private boolean isSettings;
    private boolean[] isChecked;
    private boolean[] previousCheckedState;

    public  AdapterLanguageHorizontal(java.util.ArrayList<Language> placesList){
        this.languagesList = placesList;
        this.isSettings = false;
    }

    public AdapterLanguageHorizontal(java.util.ArrayList<Language> placesList, boolean isSettings, boolean[] prev){
        this.languagesList = placesList;
        this.isSettings = isSettings;
        this.previousCheckedState = prev;
    }

    public boolean[] getCheckBoxesStatus()
    {
        boolean[]aux = previousCheckedState;

        return isChecked;
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
        if(!isSettings)
            viewHolder.checkBox.setVisibility(View.INVISIBLE);
        if(isSettings) {
            isChecked = previousCheckedState;
            addCheckBoxChangeStatusListener(viewHolder, i);
        }
    }

    private void addCheckBoxChangeStatusListener(AdapterLanguageHorizontal.ViewHolderItem viewHolder, int postion){
        final int mypos = postion;
        viewHolder.checkBox.setChecked(previousCheckedState[postion]);
        viewHolder.checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                isChecked[mypos] = compoundButton.isChecked();
            }
        });
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
        final CheckBox checkBox;

        ViewHolderItem(View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.languageName);
            picture = itemView.findViewById(R.id.imageId);
            checkBox = itemView.findViewById(R.id.checkboxId);
        }


    }
}
