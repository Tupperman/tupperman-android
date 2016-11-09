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
import ch.tupperman.tupperman.models.User;

public class LoginFragment extends Fragment implements View.OnClickListener {

    private EditText mEmailField;
    private EditText mPasswordField;
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
        mEmailField = (EditText) view.findViewById(R.id.editText_login_email);
        mPasswordField = (EditText) view.findViewById(R.id.editText_login_password);
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
        mLoginButton.setEnabled(false);
    }

    public void enableUserInterface() {
        mEmailField.setEnabled(true);
        mPasswordField.setEnabled(true);
        mLoginButton.setEnabled(true);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onClick(View view) {
        if(mListener != null) {
            User user = new User(mEmailField.getText().toString(), mPasswordField.getText().toString());
            mListener.onClickLogin(user);
        }
    }

    public interface InteractionListener {
        void onClickLogin(User user);
    }
}
