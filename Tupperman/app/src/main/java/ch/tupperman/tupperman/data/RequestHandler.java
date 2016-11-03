package ch.tupperman.tupperman.data;

import android.content.Context;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.List;

import ch.tupperman.tupperman.MainActivity;
import ch.tupperman.tupperman.models.Tupper;

public class RequestHandler {
    private ServerCall mServerCall;
    private Context mContext;
    private RequestHandlerListener mRequestHandlerListener;
    public RequestHandler(Context context){
        mServerCall = new ServerCall(context);
    }

    public void create(final Tupper tupper){
        mServerCall.createTupper(new ServerCallback() {
            @Override
            public void onSuccess(JSONObject jsonObject) {
                mRequestHandlerListener.created(tupper);
                Toast.makeText(mContext, jsonObject.toString(), Toast.LENGTH_LONG).show();
            }

            @Override
            public void onSuccessArray(JSONArray jsonObject) {

            }


            @Override
            public void onError(String message) {
                Toast.makeText(mContext, message, Toast.LENGTH_LONG).show();
            }
        }, tupper.toJSON());

    }


    public void getTuppers(){

    }

    public interface RequestHandlerListener {
        void created(Tupper tupper);
        void updated(Tupper tupper);
        void deleted(Tupper tupper);
        void gotTuppers(List<Tupper> tuppers);
    }


}