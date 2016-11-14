package layout;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;


import java.text.SimpleDateFormat;
import java.util.Date;

import ch.tupperman.tupperman.R;
import ch.tupperman.tupperman.models.Tupper;


public class DetailFragment extends Fragment implements DatePickerFragment.OnDateChosenListener {
    private static final String ARG = "tupper";

    private Tupper mTupper;
    private TextInputEditText editTextUUID;
    private TextInputEditText editTextDescription;
    private TextInputEditText editTextName;
    private TextInputEditText editTextWeight;
    private TextInputEditText editTextFreeze;
    private TextInputEditText editTextExpire;
    private TextInputEditText editTextFoodGroups;
    private boolean isCreate;
    private View mView;
    private OnFragmentInteractionListener mListener;
    private SimpleDateFormat mDateFormat;

    public DetailFragment() {
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
        editTextUUID.setText(mTupper.uuid);
        editTextDescription.setText(mTupper.description);
        editTextName.setText(mTupper.name);
        editTextWeight.setText(mTupper.weight == 0 ? null : Integer.toString(mTupper.weight));
        editTextFreeze.setText(mDateFormat.format(mTupper.dateOfFreeze));
        editTextExpire.setText(mDateFormat.format(mTupper.expiryDate));
        editTextFoodGroups.setText(mTupper.foodGroup);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_details, container, false);
        setFreezeClickListener();
        setExpireClickListener();

        editTextUUID = (TextInputEditText) mView.findViewById(R.id.editText_uuid);
        editTextDescription = (TextInputEditText) mView.findViewById(R.id.editText_description);
        editTextName = (TextInputEditText) mView.findViewById(R.id.editText_name);
        editTextWeight = (TextInputEditText) mView.findViewById(R.id.editText_weight);
        editTextFreeze = (TextInputEditText) mView.findViewById(R.id.editText_freezeDate);
        editTextExpire = (TextInputEditText) mView.findViewById(R.id.editText_expiryDate);
        editTextFoodGroups = (TextInputEditText) mView.findViewById(R.id.editText_foodGroup);

        editTextUUID.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                ClipboardManager clipboardManager = (ClipboardManager) getActivity().getSystemService(Context.CLIPBOARD_SERVICE);
                ClipData clipData = ClipData.newPlainText("UUID", editTextUUID.getText().toString());
                clipboardManager.setPrimaryClip(clipData);
                Toast.makeText(mView.getContext(), "Changing UUID is not yet supported. Copied UUID to Clipboard", Toast.LENGTH_LONG).show();
            }
        });

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

    public void saveTupper() {
        if (editTextUUID.getText().toString().equals("")) {
            Toast.makeText(getContext(), "UUID not set!", Toast.LENGTH_LONG).show();
        } else if (editTextName.getText().toString().equals("")) {
            Toast.makeText(getContext(), "Name not set!", Toast.LENGTH_LONG).show();
        } else if (editTextDescription.getText().toString().equals("")) {
            Toast.makeText(getContext(), "Description not set!", Toast.LENGTH_LONG).show();
        } else if (editTextWeight.getText().toString().equals("")) {
            Toast.makeText(getContext(), "Weight not set!", Toast.LENGTH_LONG).show();
        } else if (editTextFreeze.getText().toString().equals("")) {
            Toast.makeText(getContext(), "Freeze date not set!", Toast.LENGTH_LONG).show();
        } else if (editTextExpire.getText().toString().equals("")) {
            Toast.makeText(getContext(), "Expiry date not set!", Toast.LENGTH_LONG).show();
        } else {
            mTupper.uuid = editTextUUID.getText().toString();
            mTupper.name = editTextName.getText().toString();
            mTupper.description = editTextDescription.getText().toString();
            mTupper.weight = editTextWeight.getText().toString().equals("") ? 0 : Integer.parseInt(editTextWeight.getText().toString());
            mTupper.foodGroup = editTextFoodGroups.getText().toString();

            if (isCreate) {
                mListener.onCreate(mTupper);
            } else {
                mListener.onUpdate(mTupper);
            }
            getActivity().onBackPressed();
        }
    }

    public void deleteTupper() {
        mListener.onDelete(mTupper);
        getActivity().onBackPressed();
    }


    private void setFreezeClickListener() {
        if (editTextFreeze == null) {
            editTextFreeze = (TextInputEditText) mView.findViewById(R.id.editText_freezeDate);
        }
        editTextFreeze.setOnClickListener(new View.OnClickListener() {
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

    private void setExpireClickListener() {
        if (editTextExpire == null) {
            editTextExpire = (TextInputEditText) mView.findViewById(R.id.editText_expiryDate);
        }
        editTextExpire.setOnClickListener(new View.OnClickListener() {
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

    public void setTupper(Tupper tupper) {
        if (tupper != null) {
            isCreate = false;
            mTupper = tupper;
            initializeText();
        } else {
            isCreate = true;
        }
    }

    @Override
    public void onFreeze(Date date) {
        mTupper.dateOfFreeze = date;
        TextView freezeView = (TextView) mView.findViewById(R.id.editText_freezeDate);
        freezeView.setText(mDateFormat.format(date));
    }

    @Override
    public void onExpiry(Date date) {
        mTupper.expiryDate = date;
        TextView expireView = (TextView) mView.findViewById(R.id.editText_expiryDate);
        expireView.setText(mDateFormat.format(date));
    }

    public interface OnFragmentInteractionListener {
        void onCreate(Tupper tupper);

        void onUpdate(Tupper tupper);

        void onDelete(Tupper tupper);
    }
}
