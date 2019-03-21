package com.example.pathfinderapp;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class AdapterSearch
        extends RecyclerView.Adapter<AdapterSearch.ViewHolderItem>
        implements View.OnClickListener {

    private ArrayList<SearchItem> searchList;
    private View.OnClickListener listener;

    public AdapterSearch(ArrayList<SearchItem> searchList) {
        this.searchList = searchList;
    }

    @Override
    public ViewHolderItem onCreateViewHolder(ViewGroup parent, int i) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recycler_item, null, false);

        view.setOnClickListener(this);
        return new ViewHolderItem(view);
    }

    @Override
    public void onBindViewHolder(ViewHolderItem viewHolder, int i) {
        viewHolder.info.setText(searchList.get(i).getInfo());
        viewHolder.title.setText(searchList.get(i).getTitle());
        viewHolder.picture.setImageResource(searchList.get(i).getPicture());
    }

    @Override
    public int getItemCount() {
        return searchList.size();
    }

    public void setOnClickListener(View.OnClickListener listener){
        this.listener = listener;
    }

    @Override
    public void onClick(View v) {
        if(listener != null)
            listener.onClick(v);
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
