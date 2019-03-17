package com.example.pathfinderapp;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

public class AdapterSearch extends RecyclerView.Adapter<AdapterSearch.ViewHolderItem> {

    private ArrayList<String> searchList;

    public AdapterSearch(ArrayList<String> searchList) {
        this.searchList = searchList;
    }

    @Override
    public ViewHolderItem onCreateViewHolder(ViewGroup parent, int i) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recycler_item, null, false);

        return new ViewHolderItem(view);
    }

    @Override
    public void onBindViewHolder(ViewHolderItem viewHolder, int i) {
        viewHolder.asignData(searchList.get(i));
    }

    @Override
    public int getItemCount() {
        return searchList.size();
    }

    public class ViewHolderItem extends RecyclerView.ViewHolder {

        TextView data;

        public ViewHolderItem(View itemView) {
            super(itemView);

            data = itemView.findViewById(R.id.SearchListItem);
        }

        public void asignData(String s) {
            data.setText(s);
        }
    }
}
