package com.example.pathfinderapp;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.pathfinderapp.Models.Post;

import java.util.ArrayList;

public class AdapterSearch
        extends RecyclerView.Adapter<AdapterSearch.ViewHolderItem>
        implements View.OnClickListener {

    private ArrayList<Post> searchList;
    private View.OnClickListener listener;
    private Context context;

    public AdapterSearch(ArrayList<Post> searchList) {
        this.searchList = searchList;
    }

    @Override
    public ViewHolderItem onCreateViewHolder(ViewGroup parent, int i) {
        context = parent.getContext();
        View view = LayoutInflater.from(context)
                .inflate(R.layout.recycler_item, null, false);

        view.setOnClickListener(this);
        return new ViewHolderItem(view);
    }

    @Override
    public void onBindViewHolder(ViewHolderItem viewHolder, int i) {
        viewHolder.info.setText(Float.toString(searchList.get(i).getGuide().getScore()));
        viewHolder.title.setText(searchList.get(i).getGuide().getName());
        viewHolder.picture.setImageResource(searchList.get(i).getGuide().getImage());

        Bitmap bitmap = null;

        bitmap = BitmapFactory.decodeResource(context.getResources(), searchList.get(i).getGuide().getImage());
        bitmap = getCroppedBitmap(bitmap);
        viewHolder.picture.setImageBitmap(bitmap);
    }

    private Bitmap getCroppedBitmap(Bitmap bitmap) {
        Bitmap output = Bitmap.createBitmap(bitmap.getWidth(),
                bitmap.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(output);

        final int color = 0xff424242;
        final Paint paint = new Paint();
        final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());

        paint.setAntiAlias(true);
        canvas.drawARGB(0, 0, 0, 0);
        paint.setColor(color);

        canvas.drawCircle((bitmap.getWidth()) / 3, bitmap.getHeight() / 2 ,
                (int) (bitmap.getWidth() / 2.8), paint);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(bitmap, rect, rect, paint);
        return output;
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
