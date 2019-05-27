package com.example.pathfinderapp.AsyncStuff;


import android.app.Activity;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.widget.Toast;
import com.example.pathfinderapp.LoginActivity;
import com.google.firebase.auth.FirebaseAuth;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;


public class CheckInternetConnection extends AsyncTask<String, Void, Boolean> {
    private Context context;

    public CheckInternetConnection(Context context) {
        this.context = context;
    }


    private boolean isConnected() {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Service.CONNECTIVITY_SERVICE);

        if (connectivityManager != null) {
            NetworkInfo info = connectivityManager.getActiveNetworkInfo();
            if (info != null)
                return info.getState() == NetworkInfo.State.CONNECTED;
        }
        return false;
    }

    private boolean isWifi() {
        ConnectivityManager connMgr = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        return networkInfo.getType() == ConnectivityManager.TYPE_WIFI;
    }


    @Override
    protected Boolean doInBackground(String... params) {

        boolean hasInternet;
        try {
            Socket socket = new Socket();
            SocketAddress socketAddress = new InetSocketAddress("8.8.8.8", 53);
            socket.connect(socketAddress, 1500);
            socket.close();
            hasInternet = true;
        } catch (IOException e) {
            e.printStackTrace();
            hasInternet = false;
        }

        return hasInternet;
    }

    private void setConnectionUnavailable() {

        FirebaseAuth.getInstance().signOut();
        Toast.makeText(context, " No internet available ", Toast.LENGTH_SHORT).show();
        Intent toLogin = new Intent((Activity) this.context, LoginActivity.class);
        this.context.startActivity(toLogin);
        ((Activity) this.context).finish();

    }

    @Override
    protected void onPostExecute(Boolean hasInternet) {
        if (isConnected() && isWifi()) {
            if (hasInternet) {
                Toast.makeText(context, "  internet available ", Toast.LENGTH_SHORT).show();
            } else {
                setConnectionUnavailable();
            }
        } else {
            setConnectionUnavailable();

        }
        super.onPostExecute(hasInternet);
    }
}
