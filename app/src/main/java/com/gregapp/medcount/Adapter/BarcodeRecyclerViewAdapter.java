package com.gregapp.medcount.Adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.gregapp.medcount.Fagment.BarcodeFragment.OnListFragmentInteractionListener;
import com.gregapp.medcount.Model.BarcodeItem;
import com.gregapp.medcount.R;

import java.util.List;

/**
 * {@link RecyclerView.Adapter} that can display a {@link BarcodeItem} and makes a call to the
 * specified {@link OnListFragmentInteractionListener}.
 * TODO: Replace the implementation with code for your data type.
 */
public class BarcodeRecyclerViewAdapter extends RecyclerView.Adapter<BarcodeRecyclerViewAdapter.ViewHolder> {

    private final List<BarcodeItem> mValues;
    private final OnListFragmentInteractionListener mListener;

    public BarcodeRecyclerViewAdapter(List<BarcodeItem> items, OnListFragmentInteractionListener listener) {
        mValues = items;
        mListener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_barcode, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.mItem = mValues.get(position);
        holder.mBarcodeLabel.setText(mValues.get(position).barcodeContent);
        holder.mDateTimeLabel.setText(mValues.get(position).timeStr);

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
        public final TextView mBarcodeLabel;
        public BarcodeItem mItem;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            mDateTimeLabel = (TextView) view.findViewById(R.id.datetimeLabel);
            mBarcodeLabel = (TextView) view.findViewById(R.id.barcodeLabel);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + mBarcodeLabel.getText() + "'";
        }
    }
}
