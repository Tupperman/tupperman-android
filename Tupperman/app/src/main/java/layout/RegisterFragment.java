package layout;


import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import ch.tupperman.tupperman.R;
import ch.tupperman.tupperman.models.User;

public class RegisterFragment extends Fragment implements View.OnClickListener {

    private TextInputEditText mEmailField;
    private TextInputEditText mPasswordField;
    private TextInputEditText mConfirmPasswordField;
    private TextInputEditText mServerUrlField;
    private Button mRegisterButton;
    private InteractionListener mListener;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_register, container, false);
        mEmailField = (TextInputEditText) view.findViewById(R.id.editText_register_email);
        mConfirmPasswordField = (TextInputEditText) view.findViewById(R.id.editText_register_repeatPassword);
        mPasswordField = (TextInputEditText) view.findViewById(R.id.editText_register_password);
        mServerUrlField = (TextInputEditText) view.findViewById(R.id.editText_register_serverUrl);
        mRegisterButton = (Button) view.findViewById(R.id.button_register);
        mRegisterButton.setOnClickListener(this);

        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if(context instanceof InteractionListener) {
            mListener = (InteractionListener) context;
        }
    }

    @Override
    public void onClick(View view) {
        if(mListener != null) {
            SharedPreferences.Editor editor = getActivity().getSharedPreferences(getString(R.string.preferences_file_id), Activity.MODE_PRIVATE).edit();
            if (!mServerUrlField.getText().toString().isEmpty()) {
                editor.putString(getString(R.string.preferences_key_server_url), mServerUrlField.getText().toString());
            } else {
                editor.putString(getString(R.string.preferences_key_server_url), getString(R.string.preferences_default_url));
            }
            editor.apply();

            if (mPasswordField.getText().toString().equals(mConfirmPasswordField.getText().toString())) {
                User user = new User(mEmailField.getText().toString(), mPasswordField.getText().toString());
                mListener.onClickRegister(user);
            } else {
                Toast.makeText(this.getContext(),"Passwords do not match.", Toast.LENGTH_LONG).show();
            }
        }
    }

    public void disableUserInterface() {
        mEmailField.setEnabled(false);
        mPasswordField.setEnabled(false);
        mConfirmPasswordField.setEnabled(false);
        mServerUrlField.setEnabled(false);
        mRegisterButton.setEnabled(false);
    }

    public void enableUserInterface() {
        mEmailField.setEnabled(true);
        mPasswordField.setEnabled(true);
        mConfirmPasswordField.setEnabled(true);
        mServerUrlField.setEnabled(true);
        mRegisterButton.setEnabled(true);
    }

    public interface InteractionListener {
        void onClickRegister(User user);
    }

}
