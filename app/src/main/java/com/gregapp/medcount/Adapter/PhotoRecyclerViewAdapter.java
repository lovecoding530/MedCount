package com.gregapp.medcount.Adapter;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.gregapp.medcount.Fagment.PhotoFragment.OnListFragmentInteractionListener;
import com.gregapp.medcount.Helper.MyHelper;
import com.gregapp.medcount.Model.PhotoItem;
import com.gregapp.medcount.R;
import com.gregapp.medcount.dummy.DummyContent.DummyItem;

import java.io.File;
import java.util.List;

/**
 * {@link RecyclerView.Adapter} that can display a {@link PhotoItem} and makes a call to the
 * specified {@link OnListFragmentInteractionListener}.
 * TODO: Replace the implementation with code for your data type.
 */
public class PhotoRecyclerViewAdapter extends RecyclerView.Adapter<PhotoRecyclerViewAdapter.ViewHolder> {

    private final List<PhotoItem> mValues;
    private final OnListFragmentInteractionListener mListener;

    public PhotoRecyclerViewAdapter(List<PhotoItem> items, OnListFragmentInteractionListener listener) {
        mValues = items;
        mListener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_photo, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.mItem = mValues.get(position);
        holder.mPhotoUrlLabel.setText(mValues.get(position).photoUrl);
        holder.mDateTimeLabel.setText(mValues.get(position).timeStr);
        if(mValues.get(position).bitmap == null){
            mValues.get(position).bitmap = MyHelper.getScaledWBitMap(mValues.get(position).photoUrl, holder.mPhotoView.getWidth());
        }
        holder.mPhotoView.setImageBitmap(mValues.get(position).bitmap);
        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mListener) {
                    // Notify the active callbacks interface (the activity, if the
                    // fragment is attached to one) that an item has been selected.
                    mListener.onListFragmentInteraction(holder.mItem);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final TextView mDateTimeLabel;
        public final TextView mPhotoUrlLabel;
        public final ImageView mPhotoView;
        public PhotoItem mItem;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            mDateTimeLabel = (TextView) view.findViewById(R.id.datetimeLabel);
            mPhotoUrlLabel = (TextView) view.findViewById(R.id.photoUrlLabel);
            mPhotoView = (ImageView) view.findViewById(R.id.imageView);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + mPhotoUrlLabel.getText() + "'";
        }
    }
}
