package com.example.amitrai.demoopentokandroid;

import android.Manifest;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.example.amitrai.demoopentokandroid.bean.SignalMessage;
import com.example.amitrai.demoopentokandroid.utility.Appconstants;
import com.opentok.android.Connection;
import com.opentok.android.OpentokError;
import com.opentok.android.PublisherKit;
import com.opentok.android.Session;
import com.opentok.android.Stream;

import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.EasyPermissions;

import static android.R.attr.data;
import static com.example.amitrai.demoopentokandroid.utility.Appconstants.API_KEY;
import static com.example.amitrai.demoopentokandroid.utility.Appconstants.SESSION_ID;
import static com.example.amitrai.demoopentokandroid.utility.Appconstants.TOKEN;

public class MainActivity extends AppCompatActivity implements Session.SessionListener,
        PublisherKit.PublisherListener, Session.SignalListener {

    private static final int RC_VIDEO_APP_PERM = 101;

    private final String LOG_TAG = getClass().getSimpleName();
    private Session mSession = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initOpenTok();
        requestPermissions();
    }


    /**
     * initialize opentok
     */
    private void initOpenTok() {
        mSession = new Session.Builder(this, API_KEY, SESSION_ID).build();
        mSession.setSessionListener(this);
        mSession.connect(TOKEN);
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }


    @AfterPermissionGranted(RC_VIDEO_APP_PERM)
    private void requestPermissions() {
        String[] perms = { Manifest.permission.INTERNET, Manifest.permission.CAMERA, Manifest.permission.RECORD_AUDIO };
        if (EasyPermissions.hasPermissions(this, perms)) {
            // initialize view objects from your layout
//            mPublisherViewContainer = (FrameLayout) findViewById(R.id.publisher_container);
//            mSubscriberViewContainer = (FrameLayout) findViewById(R.id.subscriber_container);

            // initialize and connect to the session
//            mSession = new Session.Builder(this, API_KEY, SESSION_ID).build();
//            mSession.setSessionListener(this);
//            mSession.connect(TOKEN);

        } else {
            EasyPermissions.requestPermissions(this, "This app needs access to your camera and mic to make video calls", RC_VIDEO_APP_PERM, perms);
        }
    }

    @Override
    public void onConnected(Session session) {
        Log.e(LOG_TAG, "Session Connected");
        mSession.setSignalListener(this);

        sendMessage("this is a text message");
    }

    @Override
    public void onDisconnected(Session session) {
        Log.e(LOG_TAG, "Session Disconnected");
    }

    @Override
    public void onStreamReceived(Session session, Stream stream) {
        Log.e(LOG_TAG, "Stream Received");
    }

    @Override
    public void onStreamDropped(Session session, Stream stream) {
        Log.e(LOG_TAG, "Stream Dropped");
    }

    @Override
    public void onError(Session session, OpentokError opentokError) {
        Log.e(LOG_TAG, "Session error: " + opentokError.getMessage());
    }

    @Override
    public void onStreamCreated(PublisherKit publisherKit, Stream stream) {
        Log.e(LOG_TAG, "stream created: ");
    }

    @Override
    public void onStreamDestroyed(PublisherKit publisherKit, Stream stream) {
        Log.e(LOG_TAG, "stream destroyed" );
    }

    @Override
    public void onError(PublisherKit publisherKit, OpentokError opentokError) {
        Log.e(LOG_TAG, "error in stream " );
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        try{
            if (mSession != null)
                mSession.disconnect();
        }catch (Exception exp){
            exp.printStackTrace();
        }


    }


    private void sendMessage(String message) {
        SignalMessage signal = new SignalMessage(message);
        mSession.sendSignal(Appconstants.SIGNAL_TYPE, signal.getMessage());

    }

    @Override
    public void onSignalReceived(Session session, String message_type, String message, Connection connection) {
        boolean remote = !connection.equals(mSession.getConnection());
        if (message_type != null && message_type.equalsIgnoreCase(Appconstants.SIGNAL_TYPE)) {
//            showMessage(data, remote);
            Log.e(LOG_TAG, "data is "+data+" message is "+remote+" message type "+message_type+
                    " message  "+message);


        }
    }
}
