package com.example.amitrai.demoopentokandroid;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.amitrai.demoopentokandroid.bean.InitializerBean;
import com.example.amitrai.demoopentokandroid.bean.SignalMessage;
import com.example.amitrai.demoopentokandroid.listeners.OpenTokListener;
import com.example.amitrai.demoopentokandroid.opentok_custom.ConnectionManager;
import com.example.amitrai.demoopentokandroid.utility.Appconstants;

import pub.devrel.easypermissions.EasyPermissions;

public class MainActivity extends AppCompatActivity implements OpenTokListener {


    private final String TAG = getClass().getSimpleName();

    private Button btn_send = null;
    private EditText edt_message = null;
    private InitializerBean bean = null;
    private ConnectionManager connectionManager = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        init();
        initializeOpenTok();
    }

    /**
     * initializes opentok
     */
    private void initializeOpenTok(){
        bean = new InitializerBean();
        bean.setApiKey(Appconstants.API_KEY);
        bean.setSessionId(Appconstants.SESSION_ID);
        bean.setToken(Appconstants.TOKEN);

        connectionManager = new ConnectionManager(this, bean, this);
        connectionManager.initializeSession();
    }


    /**
     * initialize view elements
     */
    private void init(){
        btn_send = (Button) findViewById(R.id.btn_send);
        edt_message = (EditText) findViewById(R.id.edt_message);

        btn_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String message = edt_message.getText().toString();
                if (message != null && !message.isEmpty()) {
                    SignalMessage signalMessage = new SignalMessage(Appconstants.MESSAGE_TYPE_CHAT, message);
                    if (connectionManager.isConnected)
                        connectionManager.sendMessage(signalMessage);
                }else
                    Toast.makeText(MainActivity.this, "message can not be empty", Toast.LENGTH_LONG).show();

            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }


    @Override
    public void onError(String error_message) {
        Log.e(TAG, "error "+ error_message);
    }

    @Override
    public void onSuccess(String message) {
        Log.e(TAG, "success message "+ message);
    }

    @Override
    public void onMessageReceived(String message_type, String message) {
        Log.e(TAG, "message  "+ message);
    }
}
