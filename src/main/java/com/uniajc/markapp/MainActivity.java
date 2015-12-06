package com.uniajc.markapp;

import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.text.Html;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import com.uniajc.markapp.Fragments.UserInfo;
import com.uniajc.markapp.Utilidades.*;
import com.uniajc.markapp.webService.WSMarkApp;

import java.io.IOException;
import java.util.StringTokenizer;

public class MainActivity extends Activity{

    UserInfo fragUserInfo;
    public Utility util;
    public WSMarkApp ws;
    localizacion loc;

    public MainActivity() {
        ws = new WSMarkApp();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        getActionBar().setDisplayShowHomeEnabled(true);
        getActionBar().setTitle(Html.fromHtml("<font face='serif' color='#ffffff' size='9'>arkApp</font>"));

        util = new Utility(this, this);
        loc = new localizacion(this);
        try {
            loc.iniciarLocalizacion();
        } catch (IOException e) {
            e.printStackTrace();
        }

        SharedPreferences sp = getSharedPreferences("MarkAppPref", Context.MODE_PRIVATE);
        boolean cargar = sp.getBoolean("Scan", false);

        if(cargar){
            Intent intent = new Intent("com.uniajc.markapp.SCAN");
            startActivityForResult(intent, 0);
        }else {
            fragUserInfo = new UserInfo();
            Bundle bundle = new Bundle();
            bundle.putString("documento", "");
            fragUserInfo.setArguments(bundle);
            getFragmentManager().beginTransaction().add(R.id.container, fragUserInfo, "Información").commit();
        }
        Log.v("Entro0", "sahhas");
        util.preferenceClear();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);

        if(!util.getJefe()){
            menu.findItem(R.id.empMarcacion).setVisible(false);
            menu.findItem(R.id.autoCambioDisp).setVisible(false);
        }
        menu.findItem(R.id.cambioDisp).setVisible(false);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()){
            case R.id.scan:
                Intent intent = new Intent("com.uniajc.markapp.SCAN");
                startActivityForResult(intent, 0);
                return true;
            case R.id.empMarcacion:
                util.preferencesAdd("mostarLista", true);
                startActivity(new Intent(this, EmpleadosActivity.class));
                return true;
            case R.id.myMarcacion:
                util.preferencesAdd("mylista", true);
                startActivity(new Intent(this, EmpleadosActivity.class));
                return true;
            case R.id.autoCambioDisp:
                util.preferencesAdd("mostarLista", true);
                util.preferencesAdd("cambioD", true);
                startActivity(new Intent(this, EmpleadosActivity.class));
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        if (requestCode == 0){
        	if(resultCode == RESULT_OK){
                Utility util = new Utility(this);

        		String scanContent = intent.getStringExtra("SCAN_RESULT");
                Log.v("Contenido", scanContent);
                String scanFormat = intent.getStringExtra("SCAN_RESULT_FORMAT");
                Log.v("Formato: " , scanFormat);
                Log.v("Mac:" , util.getMac());

                AsyncCallWS task = new AsyncCallWS(this);
                task.execute(scanContent);
        	}else if (resultCode == RESULT_CANCELED){
                loc.pararservicio();
                util.mensajeWS("No se ha recibido datos del scaneo!");
        	}
        }
    }

    public class AsyncCallWS extends AsyncTask<String, Void, String> {

        Context context;

        public AsyncCallWS(Context context) {
            this.context = context;
        }

        @Override
        protected String doInBackground(String... params) {
            String resultado = "";

            try {
                resultado = ws.WsRegistrarMarcacion(util.getMac(), loc.getLatitud(),
                        loc.getLongitud(), loc.getDireccion(), Integer.parseInt(util.getArea()), params[0]);
            } catch (IOException e) {
                util.mensajeAsynk("Error... No se pudo realizar la marcación!");
            }

            return resultado;
        }

        @Override
        protected void onPostExecute(String aVoid) {
            String CurrentString = aVoid.toString();

            try{
                StringTokenizer tokens = new StringTokenizer(CurrentString, "|");
                String codigo = tokens.nextToken();
                String mensaje = tokens.nextToken();

                util.mensajeAsynk(mensaje);
            }catch (Exception e){
                util.mensajeAsynk("No se pudo leer correctamente el codigo");
            }
            loc.pararservicio();
        }
    }
}
