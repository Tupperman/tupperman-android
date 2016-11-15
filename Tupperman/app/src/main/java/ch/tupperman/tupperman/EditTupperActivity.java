package ch.tupperman.tupperman;

import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import com.activeandroid.ActiveAndroid;
import com.activeandroid.Configuration;
import com.brother.ptouch.sdk.NetPrinter;
import com.brother.ptouch.sdk.Printer;
import com.brother.ptouch.sdk.PrinterInfo;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;

import ch.tupperman.tupperman.data.ServerCall;
import ch.tupperman.tupperman.data.callbacks.CreateOrUpdateTupperCallback;
import ch.tupperman.tupperman.data.callbacks.DeleteTupperCallback;
import ch.tupperman.tupperman.models.Tupper;
import layout.DetailFragment;

public class EditTupperActivity extends AppCompatActivity implements DetailFragment.OnFragmentInteractionListener {
    private FragmentManager mFragmentManager;
    private ServerCall mServerCall;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_tupper);

        mFragmentManager = getSupportFragmentManager();
        mServerCall = ServerCall.newInstance(this);

        updateAuthenticationToken();
        setSupportActionBar((Toolbar) findViewById(R.id.toolbar));

        Tupper tupper = (Tupper) getIntent().getSerializableExtra(getString(R.string.extra_tupper_uuid));

        Configuration.Builder config = new Configuration.Builder(this);
        config.addModelClasses(Tupper.class);
        ActiveAndroid.initialize(config.create());

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        if (tupper == null) {
            setTitle(getString(R.string.edittupperactivity_createtupper));
        }
        getDetailFragment().setTupper(tupper);

    }


    private void updateAuthenticationToken() {
        SharedPreferences preferences = getSharedPreferences(getString(R.string.preferences_file_id), MODE_PRIVATE);
        String mAuthToken = preferences.getString(getString(R.string.preferences_key_auth_token), null);
        mServerCall.setToken(mAuthToken);
    }

    private DetailFragment getDetailFragment() {
        return (DetailFragment) mFragmentManager.findFragmentById(R.id.fragment_edit_tupper);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_details, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_save:
                getDetailFragment().saveTupper();
                return true;
            case R.id.action_delete:
                getDetailFragment().deleteTupper();
                return true;
            case R.id.action_print:
                //TODO implement

                Printer printer = new Printer();
                NetPrinter[] netPrinters = printer.getNetPrinters(PrinterInfo.Model.PT_P750W.name());
                if (netPrinters.length <= 0) {
                    Toast.makeText(this, "No printer found", Toast.LENGTH_LONG).show();
                    return false;
                }
                NetPrinter netPrinter = netPrinters[0];

                PrinterThread printerThread = new PrinterThread(netPrinter);
                printerThread.start();

                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private class PrinterThread extends Thread {

        private NetPrinter netPrinter;
        private Printer printer;

        PrinterThread(NetPrinter netPrinter){
            this.netPrinter = netPrinter;
        }
        @Override
        public void run() {

            printer = new Printer();
            PrinterInfo printerInfo = new PrinterInfo();
            printerInfo.ipAddress = netPrinter.ipAddress;
            printerInfo.macAddress = netPrinter.macAddress;
            printerInfo.printerModel = PrinterInfo.Model.PT_P750W;
            printerInfo.port = PrinterInfo.Port.NET;
            Printer.setUserPrinterInfo(printerInfo);

            printer.startCommunication();

            //TODO print

            try {
                QRCodeWriter qrCodeWriter = new QRCodeWriter();
                BitMatrix bitMatrix= qrCodeWriter.encode(getDetailFragment().getUUID(), BarcodeFormat.QR_CODE, 30, 30);

                int width   = bitMatrix.getWidth ();
                int height  = bitMatrix.getHeight ();

                Bitmap imageBitmap = Bitmap.createBitmap (width, height, Bitmap.Config.ARGB_8888);

                for (int i = 0; i < width; i ++) {
                    for (int j = 0; j < height; j ++) {
                        imageBitmap.setPixel (i, j, bitMatrix.get (i, j) ? Color.BLACK: Color.WHITE);
                    }
                }

                if(imageBitmap != null) {
                    printer.printImage(imageBitmap);
                }

            } catch (WriterException e) { //eek }

                printer.endCommunication();


//            // start message
//            Message msg = mHandle.obtainMessage(Common.MSG_PRINT_START);
//            mHandle.sendMessage(msg);
//
//            mPrintResult = new PrinterStatus();
//
//            mPrinter.startCommunication();
//            if (!mCancel) {
//                doPrint();
//            } else {
//                mPrintResult.errorCode = ErrorCode.ERROR_CANCEL;
//            }
//            mPrinter.endCommunication();
//
//            // end message
//            mHandle.setResult(showResult());
//            mHandle.setBattery(getBattery());
//            msg = mHandle.obtainMessage(Common.MSG_PRINT_END);
//            mHandle.sendMessage(msg);
            }
        }
    }

    @Override
    public void onCreate(final Tupper tupper) {
        mServerCall.postTupper(new CreateOrUpdateTupperCallback() {
            @Override
            public void onSuccess() {
                //TODO update recycler view
                tupper.save();
            }

            @Override
            public void onError(String message) {
                Toast.makeText(EditTupperActivity.this, message, Toast.LENGTH_LONG).show();
            }
        }, tupper);
    }

    @Override
    public void onUpdate(final Tupper tupper) {
        mServerCall.postTupper(new CreateOrUpdateTupperCallback() {
            @Override
            public void onSuccess() {
                //TODO update recycler view
                tupper.save();
            }

            @Override
            public void onError(String message) {
                Toast.makeText(EditTupperActivity.this, message, Toast.LENGTH_LONG).show();
            }
        }, tupper);

    }

    @Override
    public void onDelete(final Tupper tupper) {
        mServerCall.deleteTupper(new DeleteTupperCallback() {
            @Override
            public void onSuccess() {
                tupper.delete();
                //TODO delete in recyclerview
            }

            @Override
            public void onError(String message) {
                Toast.makeText(EditTupperActivity.this, message, Toast.LENGTH_LONG).show();
            }
        }, tupper);
    }
}
