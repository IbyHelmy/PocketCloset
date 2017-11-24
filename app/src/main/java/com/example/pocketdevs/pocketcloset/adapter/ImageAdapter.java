package com.example.pocketdevs.pocketcloset.adapter;

/**
 * Created by Omar Aldib on 2017-10-30.
 */

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.widget.RecyclerView;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.pocketdevs.pocketcloset.R;
import com.example.pocketdevs.pocketcloset.common.ListClickInterface;
import com.example.pocketdevs.pocketcloset.entity.Clothes;

import java.util.List;

public class ImageAdapter extends RecyclerView.Adapter<ImageAdapter.ViewHolder>{

    private final Context mContext;
    private final List<Clothes> clothes;
    private ListClickInterface listClickInterface;

    // 1
    public ImageAdapter(Context context, List<Clothes> clothes, ListClickInterface listClickInterface) {
        this.mContext = context;
        this.clothes = clothes;
        this.listClickInterface = listClickInterface;

        setHasStableIds(true);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        protected ImageView image;

        public ViewHolder(View view) {
            super(view);

            image = view.findViewById(R.id.image);

            if(listClickInterface!=null){
                view.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        listClickInterface.onItemClick(v, getLayoutPosition());
                    }
                });
            }
        }
    }

    @Override
    public ImageAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View itemView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.row_item_wear, viewGroup, false);

        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int position) {
        Clothes clothes = getItem(position);

        byte[] decodedString = Base64.decode(clothes.getPhotoId(), Base64.DEFAULT);
        Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
        viewHolder.image.setImageBitmap(decodedByte);
    }

    @Override
    public int getItemCount() {
        return clothes.size();
    }

    public Clothes getItem(int position) {
        return clothes.get(position);
    }

    @Override
    public long getItemId(int position) {
        return clothes.get(position).hashCode();
    }
}