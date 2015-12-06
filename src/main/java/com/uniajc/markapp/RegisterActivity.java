package com.uniajc.markapp;

import android.app.Activity;
import android.app.DialogFragment;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import com.uniajc.markapp.Clases.Area;
import com.uniajc.markapp.Utilidades.Dialogs;
import com.uniajc.markapp.Utilidades.Utility;
import com.uniajc.markapp.webService.WSMarkApp;
import java.util.Arrays;
import java.util.StringTokenizer;

public class RegisterActivity extends Activity {

    private EditText identifica, nombres, apellidos;
    private Spinner area;
    public String resultado;
    public Area[] arrArea;
    private Utility util;
    private int opcion;
    private WSMarkApp ws;

    public RegisterActivity() {
        ws = new WSMarkApp(this, this);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        getActionBar().setDisplayShowHomeEnabled(true);
        getActionBar().setTitle(Html.fromHtml("<font face='serif' color='#ffffff' size='9'>arkApp</font>"));
        //getActionBar().setTitle("");
        util = new Utility(this, this);

        identifica = (EditText)findViewById(R.id.identifica);
        nombres = (EditText)findViewById(R.id.nombres);
        apellidos = (EditText)findViewById(R.id.apellidos);
        area = (Spinner)findViewById(R.id.area);

        cargarAreas();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);

        menu.findItem(R.id.scan).setVisible(false);
        menu.findItem(R.id.myMarcacion).setVisible(false);
        menu.findItem(R.id.empMarcacion).setVisible(false);
        menu.findItem(R.id.autoCambioDisp).setVisible(false);

        return  true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()){
            case R.id.cambioDisp:
                Log.v("Entro", "cambioDisp");
                dialogCambioDisp();
                return true;
                default:
                    return super.onOptionsItemSelected(item);
        }
    }

    public void dialogCambioDisp(){
        DialogFragment dialogs = new Dialogs();
        dialogs.show(getFragmentManager(), "Cambio Dispositivo");
    }

    private void cargarAreas(){
        opcion = 0;
        AsyncCallWS task = new AsyncCallWS(this, this);
        task.execute();
    }

    public void registrar (View view){
        util = new Utility(this, this);
        opcion = 1;

        Long pos =  area.getSelectedItemId();
        Area codigo = (Area) area.getItemAtPosition(Integer.parseInt(pos.toString()));
        Integer codigoA = codigo.getCodigo();

        if(validarCampos(codigoA)){
            AsyncCallWS task = new AsyncCallWS(this, this);
            task.execute(identifica.getText().toString(),
                    nombres.getText().toString(), apellidos.getText().toString(), codigoA.toString());
        }else{
            util.mensajeApp("Todos los campos son obligatorios!");
        }
    }

    private Boolean validarCampos(Integer codArea){
        Boolean correcto = true;
        String identidad = identifica.getText().toString();

        if(identidad.isEmpty() || identidad.length() < 6){
            correcto = false;
        }
        if(nombres.getText().toString().isEmpty()){
            correcto = false;
        }
        if(apellidos.getText().toString().isEmpty()){
            correcto = false;
        }

        if(codArea == 0){
            correcto = false;
        }

        return correcto;
    }

    private void limpiarCampos(){
        identifica.setText("");
        nombres.setText("");
        apellidos.setText("");
        area.setSelected(false);
    }

    private void validarResultado(String result){
        String CurrentString = result.toString();

        StringTokenizer tokens = new StringTokenizer(CurrentString, "|");
        String codigo = tokens.nextToken();
        String mensaje = tokens.nextToken();

        util.preferenceClear();

        opcion = 0;
        if(!result.isEmpty()){
            if(codigo.equals("0")){
                util.preferencesAdd("identifica", identifica.getText().toString());
                limpiarCampos();
                this.finish();
                Intent intent = new Intent(this, MainActivity.class);
                startActivity(intent);
                util.mensajeWS(mensaje.toString());
            }else{
                util.mensajeWS(mensaje.toString());
            }
        }else{
            util.mensajeWS("No se pudo almacenar el registro!");
        }
    }

    static <T> T[] append(T[] arr, T element) {
        final int N = arr.length;
        arr = Arrays.copyOf(arr, N + 1);
        arr[N] = element;
        return arr;
    }

    private void cargarSpinnerAreas(){
        try{
            Spinner spinner_area = (Spinner) findViewById(R.id.area);
            ArrayAdapter spinner_adapter = new ArrayAdapter(com.uniajc.markapp.RegisterActivity.this,
                    android.R.layout.simple_spinner_item, arrArea);
            spinner_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinner_area.setAdapter(spinner_adapter);

        }catch (Exception e){
            util.mensajeWS("Error... No se pudieron cargar las areas!");
        }
    }

    public class AsyncCallWS extends AsyncTask<String, Void, String> {

        Context context;
        Activity activity;

        public AsyncCallWS(Context contex, Activity activity){
            context = contex;
            this.activity = activity;
        }

        @Override
        protected String doInBackground(String... params) {
            Log.i("tag", "doInBackground");

            String result = "";
            try{
                if(opcion == 1){ //Registro
                    result = ws.WsRegistrar(util.getMac(), params[0],
                            params[1], params[2], Integer.parseInt(params[3]));
                }else{ //Areas
                    arrArea = ws.WSoapArea();
                }
            }catch (Exception e){
                util.mensajeAsynk("No se pudo realizar el registro!");
            }

            return result;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            util.Loading(activity);
        }

        @Override
        protected void onPostExecute(String result) {
            Log.i("tag", "onPostExecuteRegister");

            cargarSpinnerAreas();

            if(opcion == 1){
                opcion = 0;
                validarResultado(result);
            }

            util.LoadingCancel();
        }
    }

    public String getResultado() {
        return resultado;
    }

    public void setResultado(String resultado) {
        this.resultado = resultado;
    }

}
