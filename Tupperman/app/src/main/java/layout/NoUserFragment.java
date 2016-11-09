package layout;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import ch.tupperman.tupperman.R;

public class NoUserFragment extends Fragment implements View.OnClickListener {

    private Button mLoginButton;
    private Button mRegisterButton;
    private InteractionListener mListener;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_no_user, container, false);
        mLoginButton = (Button) view.findViewById(R.id.button_login_existing_user);
        mLoginButton.setOnClickListener(this);
        mRegisterButton = (Button) view.findViewById(R.id.button_register_new_user);
        mRegisterButton.setOnClickListener(this);
        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof InteractionListener) {
            mListener = (InteractionListener) context;
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onClick(View view) {
        if (view.equals(mLoginButton)) {
            mListener.onClickLogin();
        } else {
            mListener.onClickRegister();
        }
    }

    public interface InteractionListener {
        void onClickLogin();
        void onClickRegister();
    }
}
