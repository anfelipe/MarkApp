package com.uniajc.markapp.Utilidades;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.util.Log;
import android.widget.Toast;

public class Utility{

    Context context;
    SharedPreferences sp;
    Activity activity;
    ProgressDialog pd;

    public Utility(Context context){
        this.context = context;
    }

    public Utility(Context context, Activity activity){
        this.context = context;
        this.activity = activity;
    }

    public String getMac(){
        WifiManager manager = (WifiManager)context.getSystemService(Context.WIFI_SERVICE);
        WifiInfo info = manager.getConnectionInfo();
        String address = info.getMacAddress();
        return address;
    }

    public void mensajeWS(String mensaje){
       Toast toast = Toast.makeText(context.getApplicationContext(),
               mensaje, Toast.LENGTH_SHORT);
        toast.show();
    }

    public void mensajeApp(String mensaje){
        AlertDialog dialog = new AlertDialog.Builder(activity)
                .setTitle("Información")
                .setMessage(mensaje)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                }).create();
        dialog.show();
    }

    public void mensajeAsynk(final String mensaje){
        activity.runOnUiThread(new Runnable() {
            public void run() {
                Toast.makeText(activity, mensaje, Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void mensajeAsynkDialog(final String mensaje){
        activity.runOnUiThread(new Runnable() {
            public void run() {
                mensajeApp(mensaje);
            }
        });
    }

    public void Loading(Activity activity){
        pd = new ProgressDialog(activity);
        pd.setMessage("loading...");
        pd.show();
    }

    public void LoadingCancel(){
        if (pd != null)
        {
            pd.dismiss();
        }
    }

    public void preferencesAdd(String name, Boolean valor){
        sp = context.getSharedPreferences("MarkAppPref", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putBoolean(name, valor);
        editor.commit();
    }

    public void preferencesAdd(String name, String valor){
        sp = context.getSharedPreferences("MarkAppPref", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString(name, valor);
        editor.commit();
    }

    public void preferenceClear(){
        sp = context.getSharedPreferences("MarkAppPref", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.clear();
        editor.putString("identifica", getIdentifica());
        editor.putBoolean("jefe", getJefe());
        editor.putString("area", getArea());
        editor.putBoolean("cambioD", getCambio());
        editor.commit();
    }

    public void preferenceEdit(Boolean cambio, String nombre){
        sp = context.getSharedPreferences("MarkAppPref", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putBoolean(nombre, cambio);
        editor.commit();
    }

    public void preferncesClearAll(){
        sp = context.getSharedPreferences("MarkAppPref", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.clear();
        editor.commit();
    }

    public String getIdentifica(){
        SharedPreferences sp = context.getSharedPreferences("MarkAppPref", Context.MODE_PRIVATE);
        String identfica = sp.getString("identifica", "0");

        return identfica;
    }

    public Boolean getJefe(){
        SharedPreferences sp = context.getSharedPreferences("MarkAppPref", Context.MODE_PRIVATE);
        Boolean jefe = sp.getBoolean("jefe", false);

        return jefe;
    }

    public String getArea(){
        SharedPreferences sp = context.getSharedPreferences("MarkAppPref", Context.MODE_PRIVATE);
        String area = sp.getString("area", "0");

        return area;
    }

    public Boolean getCambio(){
        SharedPreferences sp = context.getSharedPreferences("MarkAppPref", Context.MODE_PRIVATE);
        Boolean cambio = sp.getBoolean("cambioD", false);

        return cambio;
    }

}
