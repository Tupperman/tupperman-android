package ch.tupperman.tupperman;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import com.activeandroid.ActiveAndroid;
import com.activeandroid.query.Select;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.List;
import java.util.concurrent.CountDownLatch;

import ch.tupperman.tupperman.data.ServerCall;
import ch.tupperman.tupperman.data.callbacks.LoginCallback;
import ch.tupperman.tupperman.models.Tupper;
import ch.tupperman.tupperman.models.User;

import static org.junit.Assert.assertEquals;

@RunWith(AndroidJUnit4.class)
public class ServerCallTests {

    private static final String mServerAdress = "http://ark-5.citrin.ch:9080/api/";
    private static final String mEmail = "test@test.com";
    private static final String mPassword = "Test123Test.!";
    private User mTestUser;

    private ServerCall mServerCall;

    @Before
    public void setUp() throws Exception {
        Context appContext = InstrumentationRegistry.getTargetContext();
        ActiveAndroid.initialize(appContext);
        mServerCall = new ServerCall(appContext, mServerAdress);
        mTestUser = new User(mEmail, mPassword);

        Assert.assertTrue(hasInternetConnection(appContext));
    }

    @Test
    public void loginTest() throws Exception {
        final CountDownLatch signal = new CountDownLatch(1);
        mServerCall.authenticate(new LoginCallback() {
            @Override
            public void loginSuccess(String token) {
                Assert.assertTrue(true);
                signal.countDown();
            }

            @Override
            public void loginError(Error error) {
                Assert.assertTrue(false);
                signal.countDown();
            }
        }, mTestUser);
        signal.await();
    }

    @Test
    public void loginFailed() throws Exception {
        final CountDownLatch signal = new CountDownLatch(1);
        mServerCall.authenticate(new LoginCallback() {
            @Override
            public void loginSuccess(String token) {
                Assert.assertTrue(false);
                signal.countDown();
            }

            @Override
            public void loginError(Error error) {
                Assert.assertTrue(true);
                signal.countDown();
            }
        }, new User("wrong", "user"));
        signal.await();
    }

    private boolean hasInternetConnection(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        return activeNetwork != null && activeNetwork.isConnectedOrConnecting();
    }

}
