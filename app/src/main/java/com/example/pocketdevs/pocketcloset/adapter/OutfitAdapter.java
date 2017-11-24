package com.example.pocketdevs.pocketcloset.adapter;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Base64;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.pocketdevs.pocketcloset.App;
import com.example.pocketdevs.pocketcloset.R;
import com.example.pocketdevs.pocketcloset.common.ListClickInterface;
import com.example.pocketdevs.pocketcloset.entity.Clothes;

import java.util.List;


/**
 *
 */
public class OutfitAdapter extends RecyclerView.Adapter<OutfitAdapter.MainViewHolder> {
	private final Context context;

	private int type;
	private List<Clothes> oList;
    private ListClickInterface listClickInterface;

	/**
	 *
	 * @param context
	 */
	public OutfitAdapter(final Activity context, int type, List<Clothes> oList, ListClickInterface listClickInterface) {
		super();
		this.context=context;
		this.type=type;
		this.oList=oList;
		this.listClickInterface=listClickInterface;
	}

	public class MainViewHolder extends RecyclerView.ViewHolder {
		protected ImageView image;

		public MainViewHolder(View v) {
			super(v);

			image = v.findViewById(R.id.image);

            if(listClickInterface!=null){
                v.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        listClickInterface.onItemClick(v, getLayoutPosition());
                    }
                });
            }
		}
	}

	@Override
	public OutfitAdapter.MainViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
		View itemView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.row_item_wear, viewGroup, false);

		return new MainViewHolder(itemView);
	}

	@Override
	public void onBindViewHolder(MainViewHolder viewHolder, int position) {
        byte[] decodedString = Base64.decode(oList.get(position).getPhotoId(), Base64.DEFAULT);
        Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
        viewHolder.image.setImageBitmap(decodedByte);

        //App.getPicassoInstance().load(decodedByte).into(viewHolder.image);
    }

	@Override
	public int getItemCount() {
		return oList.size();
	}
}