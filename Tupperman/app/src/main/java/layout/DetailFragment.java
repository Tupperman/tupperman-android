package layout;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Date;

import ch.tupperman.tupperman.R;
import ch.tupperman.tupperman.models.Tupper;


public class DetailFragment extends Fragment implements DatePickerFragment.OnDateChosenListener {
    private static final String ARG = "tupper";

    private Tupper mTupper;
    private EditText editTextDescription;
    private EditText editTextName;
    private EditText editTextWeight;
    private TextView textViewFreeze;
    private TextView textViewExpire;
    private boolean isCreate;
    private View mView;
    private OnFragmentInteractionListener mListener;
    private SimpleDateFormat mDateFormat;
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
            isCreate = false;
        } else {
            mTupper = new Tupper();
            isCreate = true;
        }
        mDateFormat = new SimpleDateFormat("dd.MM.yyyy");
    }

    private void initializeText() {
        editTextDescription.setText(mTupper.description);
        editTextName.setText(mTupper.name);
        editTextWeight.setText(Integer.toString(mTupper.weight));
        textViewFreeze.setText(mDateFormat.format(mTupper.dateOfFreeze));
        textViewExpire.setText(mDateFormat.format(mTupper.expiryDate));
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_detail, container, false);
        setSaveButton();
        setDeleteButton();
        setFreezeButton();
        setExpireButton();
        editTextDescription = (EditText) mView.findViewById(R.id.editText_description);
        editTextName = (EditText) mView.findViewById(R.id.editText_name);
        editTextWeight = (EditText) mView.findViewById(R.id.editText_weight);
        textViewFreeze = (TextView) mView.findViewById(R.id.textView_freeze);
        textViewExpire = (TextView) mView.findViewById(R.id.textView_expire);
        if (mTupper != null) {
            initializeText();
        }
        return mView;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement InteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
        System.out.println("DetailFragment detach!");
    }

    private void setSaveButton() {
        Button saveButton = (Button) mView.findViewById(R.id.button_save_detail);
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mTupper.name = editTextName.getText().toString();
                mTupper.description = editTextDescription.getText().toString();
                mTupper.weight = Integer.parseInt(editTextWeight.getText().toString());
                mTupper.save();
                if (isCreate) {
                    mListener.onCreate(mTupper);
                } else {
                    mListener.onUpdate(mTupper);
                }
                getActivity().onBackPressed();
            }
        });
    }

    private void setDeleteButton() {
        Button deleteButton = (Button) mView.findViewById(R.id.button_delete_detail);
        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mListener.onDelete(mTupper);
                getActivity().onBackPressed();
            }
        });
    }

    private void setFreezeButton() {
        Button freezeButton = (Button) mView.findViewById(R.id.button_freeze_detail);
        freezeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DialogFragment datePickerFragment = new DatePickerFragment();
                Bundle bundle = new Bundle();
                bundle.putBoolean("freeze", true);
                datePickerFragment.setArguments(bundle);
                datePickerFragment.show(getChildFragmentManager(), "datePicker");
            }
        });
    }

    private void setExpireButton() {
        Button freezeButton = (Button) mView.findViewById(R.id.button_expire_detail);
        freezeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DialogFragment datePickerFragment = new DatePickerFragment();
                Bundle bundle = new Bundle();
                bundle.putBoolean("freeze", false);
                datePickerFragment.setArguments(bundle);
                datePickerFragment.show(getChildFragmentManager(), "datePicker");
            }
        });
    }

    @Override
    public void onFreeze(Date date) {
        mTupper.dateOfFreeze = date;
        TextView freezeView = (TextView) mView.findViewById(R.id.textView_freeze);
        freezeView.setText(mDateFormat.format(date));
    }

    @Override
    public void onExpiry(Date date) {
        mTupper.expiryDate = date;
        TextView expireView = (TextView) mView.findViewById(R.id.textView_expire);
        expireView.setText(mDateFormat.format(date));
    }

    public interface OnFragmentInteractionListener {
        void onCreate(Tupper tupper);
        void onUpdate(Tupper tupper);
        void onDelete(Tupper tupper);
    }
}
