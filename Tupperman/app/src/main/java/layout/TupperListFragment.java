package layout;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import ch.tupperman.tupperman.MyTupperRecyclerViewAdapter;
import ch.tupperman.tupperman.R;
import ch.tupperman.tupperman.models.Tupper;

public class TupperListFragment extends Fragment {

    private static final String ARG_COLUMN_COUNT = "column-count";
    private int mColumnCount = 1;
    private List<Tupper> mTupperList = new ArrayList<>();
    private OnListFragmentInteractionListener mListener;
    public MyTupperRecyclerViewAdapter myTupperRecyclerViewAdapter;

    public TupperListFragment() {
    }

    public static TupperListFragment newInstance(List<Tupper> tuppers) {
        TupperListFragment fragment = new TupperListFragment();
        fragment.mTupperList = tuppers;
        Bundle args = new Bundle();
        args.putInt(ARG_COLUMN_COUNT, 1);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            mColumnCount = getArguments().getInt(ARG_COLUMN_COUNT);
        }
        setRetainInstance(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_tupper_list, container, false);
        View recyclerView = view.findViewById(R.id.tupper_list);

        if (recyclerView instanceof RecyclerView) {
            Context context = view.getContext();
            RecyclerView crecyclerView = (RecyclerView) recyclerView;
            if (mColumnCount <= 1) {
                crecyclerView.setLayoutManager(new LinearLayoutManager(context));
            } else {
                crecyclerView.setLayoutManager(new GridLayoutManager(context, mColumnCount));
            }
            myTupperRecyclerViewAdapter = new MyTupperRecyclerViewAdapter(mTupperList, mListener);
            crecyclerView.setAdapter(myTupperRecyclerViewAdapter);
        }
        return view;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if(myTupperRecyclerViewAdapter != null){
            myTupperRecyclerViewAdapter.notifyDataSetChanged();
        }
        if (context instanceof OnListFragmentInteractionListener) {
            mListener = (OnListFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnListFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
        System.out.println("TupperListFragment detach!");
    }

    public interface OnListFragmentInteractionListener {
        void onListFragmentInteraction(Tupper tupper);
    }

    public void setTuppers(List<Tupper> list){
        mTupperList = list;
        if (myTupperRecyclerViewAdapter!=null) {
            myTupperRecyclerViewAdapter.notifyDataSetChanged();
        }
    }

    public void addTupper(Tupper tupper){
        mTupperList.add(tupper);
        //TODO maybe use notifyItemInserted
        myTupperRecyclerViewAdapter.notifyDataSetChanged();
    }

    public boolean exists(Tupper tupper){
        return mTupperList.contains(tupper);
    }

}
