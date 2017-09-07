package com.example.amitrai.demoopentokandroid.opentok_custom;

import android.app.Activity;
import android.util.Log;

import com.example.amitrai.demoopentokandroid.bean.InitializerBean;
import com.example.amitrai.demoopentokandroid.bean.SignalMessage;
import com.example.amitrai.demoopentokandroid.listeners.OpenTokListener;
import com.example.amitrai.demoopentokandroid.utility.Appconstants;
import com.opentok.android.Connection;
import com.opentok.android.OpentokError;
import com.opentok.android.PublisherKit;
import com.opentok.android.Session;
import com.opentok.android.Stream;

import static android.R.attr.data;
import static com.example.amitrai.demoopentokandroid.utility.Appconstants.API_KEY;
import static com.example.amitrai.demoopentokandroid.utility.Appconstants.SESSION_ID;
import static com.example.amitrai.demoopentokandroid.utility.Appconstants.TOKEN;

/**
 * Created by amitrai on 7/9/17.
 * User for :-
 */

public class ConnectionManager implements Session.SessionListener,
        PublisherKit.PublisherListener, Session.SignalListener{

    private InitializerBean initializerBean = null;
    private Session mSession = null;
    private Activity activity;
    private OpenTokListener listener = null;

    private final String TAG = getClass().getSimpleName();

    public ConnectionManager(Activity activity, InitializerBean initializerBean, OpenTokListener listener){
        this.initializerBean = initializerBean;
        this.listener = listener;
        this.activity = activity;
    }

    public void initializeSession(){
        try {
            if (initializerBean.getApiKey() != null && !initializerBean.getApiKey().isEmpty() ){
                if (initializerBean.getSessionId() != null && !initializerBean.getSessionId().isEmpty() ){
                    if (initializerBean.getToken() != null && !initializerBean.getToken().isEmpty() ){
                        mSession = new Session.Builder(activity, API_KEY, SESSION_ID).build();
                        mSession.setSessionListener(this);
                        mSession.connect(TOKEN);
                    }else
                        listener.onError(Appconstants.TOKEN_ERROR);
                }else
                    listener.onError(Appconstants.SESSION_ID_ERROR);
            }else
                listener.onError(Appconstants.API_KEY_ERROR);
        }catch (Exception exp){
            exp.printStackTrace();
            if (exp.getMessage() != null)
                listener.onError(Appconstants.UNKNOWN_ERROR+" "+exp.getMessage());
            else
                listener.onError(Appconstants.UNKNOWN_ERROR);
        }

    }

    @Override
    public void onStreamCreated(PublisherKit publisherKit, Stream stream) {

    }

    @Override
    public void onStreamDestroyed(PublisherKit publisherKit, Stream stream) {

    }

    @Override
    public void onError(PublisherKit publisherKit, OpentokError opentokError) {

    }

    @Override
    public void onConnected(Session session) {

    }

    @Override
    public void onDisconnected(Session session) {

    }

    @Override
    public void onStreamReceived(Session session, Stream stream) {

    }

    @Override
    public void onStreamDropped(Session session, Stream stream) {

    }

    @Override
    public void onError(Session session, OpentokError opentokError) {

    }

    @Override
    public void onSignalReceived(Session session, String message_type, String message, Connection connection) {
        Log.e(TAG, "session is "+session+" message type is "+message_type +" message "+message +" connection is "+connection );

        boolean remote = !connection.equals(mSession.getConnection());
        if (message_type != null && message_type.equalsIgnoreCase(Appconstants.SIGNAL_TYPE)) {
            Log.e(TAG, "data is "+data+" message is "+remote+" message type "+message_type+
                    " message  "+message);
        }

        if (remote && listener !=null){
            try {
                listener.onMessageReceived(""+message_type, ""+message);
            }catch (Exception exp){
                exp.printStackTrace();
                if (listener != null)
                    listener.onError(Appconstants.MESSAGE_ERROR);
            }
        }

    }


    public void sendMessage(SignalMessage signalMessage) {
        try {
            if (signalMessage != null && signalMessage.getMessage() != null &&
                    !signalMessage.getMessage().isEmpty()){
                mSession.sendSignal(signalMessage.getMessage_type(), signalMessage.getMessage());
            }
        }catch (Exception exp){
            exp.printStackTrace();
        }
    }
}
