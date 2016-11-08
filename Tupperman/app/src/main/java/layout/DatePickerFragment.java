package layout;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.widget.DatePicker;

import java.util.Calendar;
import java.util.Date;

import ch.tupperman.tupperman.R;

import static android.content.ContentValues.TAG;

public class DatePickerFragment extends DialogFragment
        implements DatePickerDialog.OnDateSetListener {

    private static final String ARG = "freeze";
    private OnDateChosenListener mListener;
    private Context mContext;
    private boolean freeze;
    private Calendar mCalender;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        freeze = getArguments().getBoolean(ARG);

        mCalender = Calendar.getInstance();
        int year = mCalender.get(Calendar.YEAR);
        int month = mCalender.get(Calendar.MONTH);
        int day = mCalender.get(Calendar.DAY_OF_MONTH);
        this.mContext = getActivity().getApplicationContext();
        onAttachFragment(getParentFragment());

        return new DatePickerDialog(getActivity(), R.style.DialogTheme, this, year, month, day);
    }

    public static DatePickerFragment newInstance(boolean freeze) {
        DatePickerFragment fragment = new DatePickerFragment();
        Bundle args = new Bundle();
        args.putBoolean(ARG, freeze);
        fragment.setArguments(args);
        return fragment;
    }

    public void onDateSet(DatePicker view, int year, int month, int day) {
        mCalender.set(Calendar.MONTH, month);
        mCalender.set(Calendar.YEAR, year);
        mCalender.set(Calendar.DAY_OF_MONTH, day);
        Date date = new Date(mCalender.getTimeInMillis());
        if (freeze) {
            mListener.onFreeze(date);
        } else {
            mListener.onExpiry(date);
        }
    }

    public void onAttachFragment(Fragment fragment) {
        try {
            mListener = (OnDateChosenListener) fragment;

        } catch (ClassCastException e) {
            throw new ClassCastException(
                    fragment.toString() + " must implement OnPlayerSelectionSetListener");
        }
    }

    public interface OnDateChosenListener {
        void onFreeze(Date date);

        void onExpiry(Date date);
    }
}