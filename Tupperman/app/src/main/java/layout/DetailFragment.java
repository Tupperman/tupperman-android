package layout;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import ch.tupperman.tupperman.R;
import ch.tupperman.tupperman.models.Tupper;


public class DetailFragment extends Fragment {
    private static final String ARG = "tupper";

    private Tupper mTupper;
    private EditText editTextDescription;
    private EditText editTextName;

    private OnFragmentInteractionListener mListener;

    public DetailFragment() {
    }

    public static DetailFragment newInstance(Tupper tupper) {
        DetailFragment fragment = new DetailFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARG, tupper);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mTupper = (Tupper) getArguments().getSerializable(ARG);
        } else {
            mTupper = new Tupper();
        }

    }

    private void initializeText() {
        editTextDescription.setText(mTupper.description);
        editTextName.setText(mTupper.name);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_detail, container, false);
        Button button = (Button) view.findViewById(R.id.button_save_detail);
        editTextDescription = (EditText) view.findViewById(R.id.editText_description);
        editTextName = (EditText) view.findViewById(R.id.editText_name);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mTupper.name = editTextName.getText().toString();
                mTupper.description = editTextDescription.getText().toString();
                mTupper.save();
                mListener.onFragmentInteraction(mTupper);
                getActivity().onBackPressed();
            }
        });
        if (mTupper != null) {
            initializeText();
        }
        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
        System.out.println("DetailFragment detach!");
    }

    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(Tupper tupper);
    }
}
