package ch.tupperman.tupperman;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import ch.tupperman.tupperman.models.Tupper;
import layout.DetailFragment;
import layout.TupperFragment.OnListFragmentInteractionListener;

import java.util.ArrayList;
import java.util.List;

/**
 * {@link RecyclerView.Adapter} that can display a {@link Tupper} and makes a call to the
 * specified {@link OnListFragmentInteractionListener}.
 * TODO: Replace the implementation with code for your data type.
 */
public class MyTupperRecyclerViewAdapter extends RecyclerView.Adapter<MyTupperRecyclerViewAdapter.ViewHolder> implements Filterable {

    private List<Tupper> mValues = new ArrayList<>();
    private List<Tupper> mOriginalValues = new ArrayList<>();
    private final OnListFragmentInteractionListener mListener;
    private Context mContext;

    public MyTupperRecyclerViewAdapter(List<Tupper> items, OnListFragmentInteractionListener listener) {
        mValues = items;
        mOriginalValues = items;
        mListener = listener;
    }

    public void setmValues(List<Tupper> list) {
        mValues = list;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        mContext = parent.getContext();
        View view = LayoutInflater.from(mContext)
                .inflate(R.layout.fragment_tupper, parent, false);
        return new ViewHolder(view);

    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.mItem = mValues.get(position);
        holder.mTitleView.setText(mValues.get(position).name);
        holder.mDescriptionView.setText(mValues.get(position).description);

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


    @Override
    public Filter getFilter() {
        return new Filter() {
            @SuppressWarnings("unchecked")
            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                mValues = (List<Tupper>) results.values;
                MyTupperRecyclerViewAdapter.this.notifyDataSetChanged();
            }

            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                List<Tupper> filteredResults = new ArrayList<>();

                if (constraint.length() == 0) {
                    filteredResults = mOriginalValues;
                } else {

                    for (Tupper tupper : mValues) {
                        if (tupper.name.toLowerCase().contains(constraint)) {
                            filteredResults.add(tupper);
                        }
                    }

                }
                FilterResults results = new FilterResults();
                results.values = filteredResults;
                return results;
            }
        };
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final ImageView mImgView;
        public final TextView mTitleView;
        public final TextView mDescriptionView;
        public Tupper mItem;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            mImgView = (ImageView) view.findViewById(R.id.tupper_card_imageview);
            mTitleView = (TextView) view.findViewById(R.id.tupper_card_titleview);
            mDescriptionView = (TextView) view.findViewById(R.id.tupper_card_descriptionview);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + mDescriptionView.getText() + "'";
        }
    }
}
