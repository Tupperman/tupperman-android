package layout;


import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import ch.tupperman.tupperman.R;
import ch.tupperman.tupperman.models.User;

public class RegisterFragment extends Fragment implements View.OnClickListener {

    private TextInputEditText mEmailField;
    private TextInputEditText mPasswordField;
    private Button mRegisterButton;
    private InteractionListener mListener;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_register, container, false);
        mEmailField = (TextInputEditText) view.findViewById(R.id.editText_register_email);
        mPasswordField = (TextInputEditText) view.findViewById(R.id.editText_register_password);
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
            User user = new User(mEmailField.getText().toString(), mPasswordField.getText().toString());
            mListener.onClickRegister(user);
        }
    }

    public void disableUserInterface() {
        mEmailField.setEnabled(false);
        mPasswordField.setEnabled(false);
        mRegisterButton.setEnabled(false);
    }

    public void enableUserInterface() {
        mEmailField.setEnabled(true);
        mPasswordField.setEnabled(true);
        mRegisterButton.setEnabled(true);
    }

    public interface InteractionListener {
        void onClickRegister(User user);
    }

}
