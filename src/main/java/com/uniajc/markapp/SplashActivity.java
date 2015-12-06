package com.uniajc.markapp;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import com.uniajc.markapp.Utilidades.Utility;
import com.uniajc.markapp.webService.WSMarkApp;


public class SplashActivity extends Activity {

    private boolean spActive;
    private boolean spPaused;
    private long spTime = 5000;
    public String resultado;
    public Utility util;
    public WSMarkApp ws;
    public ImageView imagen;
    private Activity activity;

    public SplashActivity(){
        ws = new WSMarkApp(this, this);
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        util = new Utility(this, this);
        util.preferncesClearAll();
        activity = this;

        spPaused = false;
        spActive = true;

        validarIngreso();

        Thread splashTimer = new Thread(){
            public void run(){

                try{
                    long ms = 0;

                    while (spActive && ms < spTime){
                        sleep(100);

                        if (!spPaused){
                            ms += 100;
                        }
                    }

                    if(!getResultado().equals("")){
                        if(Integer.parseInt(getResultado()) != 0){
                            Intent intent = new Intent("com.uniajc.markapp.MainActivity");
                            startActivity(intent);
                        }else{
                            startActivity(new Intent("com.uniajc.markapp.RegisterActivity"));
                        }
                    }else{
                        util.mensajeAsynk("Error.. No se pudieron procesar los datos!");
                    }
                    finish();
                }catch (Exception e){
                    Log.v("asdsad", "" + e.toString());
                    util.mensajeAsynk("Error. No se recibieron datos del usuario");
                    onDestroy();
                    finish();
                }
            }
        };

        util.preferencesAdd("Scan", true);

        splashTimer.start();
        setContentView(R.layout.activity_splash);
        imagen = (ImageView)findViewById(R.id.logo);
        Animation desvanecer = AnimationUtils.loadAnimation(this,R.animator.transparencia);
        Animation zoom = AnimationUtils.loadAnimation(this,R.animator.ampliar_imagen);
        desvanecer.reset();
        zoom.reset();
        imagen.startAnimation(zoom);
        imagen.startAnimation(desvanecer);
        return;
    }

    protected void onStop(){
        super.onStop();
    }

    protected void onPause(){
        super.onPause();
        spPaused = true;
    }

    protected void onResume(){
        super.onResume();
        spPaused = false;
    }

    protected void onDestroy(){
        super.onDestroy();
    }

    public boolean onKeyDown(int KeyCode, KeyEvent event){
        super.onKeyDown(KeyCode, event);
        spActive = false;
        return true;
    }

    private void  validarIngreso(){
        AsyncCallWS task = new AsyncCallWS(this);
        task.execute();
    }

    public String getResultado() {
        return resultado;
    }

    public void setResultado(String resultado) {
        this.resultado = resultado;
    }

    public class AsyncCallWS extends AsyncTask<String, Void, String[]> {

        Activity activity;

        public AsyncCallWS(Activity activity){
            this.activity = activity;
        }

        @Override
        protected String[] doInBackground(String... params) {
            Log.i("tag", "doInBackgroundSplas");
            String[] result = null;
            try {
                result = ws.WSoapIngreso("validarMAC2", util.getMac());
            }catch (Exception ex){
                Log.v("Error", ex.toString());
                util.mensajeAsynk("Error... No se pudo obener información del servidor");
            }

            return result;
        }

        @Override
        protected void onPostExecute(String[] result) {
            Log.i("tag", "onPostExecute");
            resultado = result[0];
            if(result.length > 1){
                resultado = result[0];
                util.preferencesAdd("identifica", result[0]);
                util.preferencesAdd("area", result[2]);

                String jefe = result[1];
                if(jefe != null && jefe.equalsIgnoreCase("S")){
                    util.preferencesAdd("jefe", true);
                }else{
                    util.preferencesAdd("jefe", false);
                }
            }
        }
    }
}
