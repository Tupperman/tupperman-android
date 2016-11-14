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

import ch.tupperman.tupperman.R;
import ch.tupperman.tupperman.models.User;

public class LoginFragment extends Fragment implements View.OnClickListener {

    private TextInputEditText mEmailField;
    private TextInputEditText mPasswordField;
    private TextInputEditText mServerUrlField;
    private Button mLoginButton;
    private InteractionListener mListener;

    public LoginFragment() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_login, container, false);
        mEmailField = (TextInputEditText) view.findViewById(R.id.editText_login_email);
        mPasswordField = (TextInputEditText) view.findViewById(R.id.editText_login_password);
        mServerUrlField = (TextInputEditText) view.findViewById(R.id.editText_login_serverUrl);
        mLoginButton = (Button) view.findViewById(R.id.button_login);
        mLoginButton.setOnClickListener(this);

        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof InteractionListener) {
            mListener = (InteractionListener) context;
        }
    }

    public void disableUserInterface() {
        mEmailField.setEnabled(false);
        mPasswordField.setEnabled(false);
        mServerUrlField.setEnabled(false);
        mLoginButton.setEnabled(false);
    }

    public void enableUserInterface() {
        mEmailField.setEnabled(true);
        mPasswordField.setEnabled(true);
        mServerUrlField.setEnabled(true);
        mLoginButton.setEnabled(true);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onClick(View view) {
        SharedPreferences.Editor editor = getActivity().getSharedPreferences(getString(R.string.preferences_file_id), Activity.MODE_PRIVATE).edit();
        if (!mServerUrlField.getText().toString().isEmpty()) {
            editor.putString(getString(R.string.preferences_key_server_url), mServerUrlField.getText().toString());
        } else {
            editor.putString(getString(R.string.preferences_key_server_url), getString(R.string.preferences_default_url));
        }
        editor.apply();

        if (mListener != null) {
            User user = new User(mEmailField.getText().toString(), mPasswordField.getText().toString());
            mListener.onClickLogin(user);
        }
    }

    public interface InteractionListener {
        void onClickLogin(User user);
    }
}
