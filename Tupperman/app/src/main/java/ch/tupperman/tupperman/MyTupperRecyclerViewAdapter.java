package ch.tupperman.tupperman;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import layout.TupperFragment.OnListFragmentInteractionListener;
import ch.tupperman.tupperman.dummy.DummyContent.DummyItem;

import java.util.ArrayList;
import java.util.List;

/**
 * {@link RecyclerView.Adapter} that can display a {@link DummyItem} and makes a call to the
 * specified {@link OnListFragmentInteractionListener}.
 * TODO: Replace the implementation with code for your data type.
 */
public class MyTupperRecyclerViewAdapter extends RecyclerView.Adapter<MyTupperRecyclerViewAdapter.ViewHolder> implements Filterable {

    private List<DummyItem> mValues = new ArrayList<>();
    private final OnListFragmentInteractionListener mListener;


    public MyTupperRecyclerViewAdapter(List<DummyItem> items, OnListFragmentInteractionListener listener) {
        mValues = items;
        mListener = listener;
    }

    public void setmValues(List<DummyItem> list){
        mValues = list;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_tupper, parent, false);
        return new ViewHolder(view);

    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.mItem = mValues.get(position);
        holder.mImgView.setImageDrawable(mValues.get(position).image);
        holder.mTitleView.setText(mValues.get(position).id);
        holder.mDescriptionView.setText(mValues.get(position).content);

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
                mValues = (List<DummyItem>) results.values;
                MyTupperRecyclerViewAdapter.this.notifyDataSetChanged();
            }

            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                List<DummyItem> filteredResults = new ArrayList<>();
                if (constraint.length() == 0) {
                    filteredResults = mValues;
                } else {

                    for(DummyItem dummyItem: mValues){
                        if(dummyItem.content.toLowerCase().contains(constraint)){
                            filteredResults.add(dummyItem);
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
        public DummyItem mItem;

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
