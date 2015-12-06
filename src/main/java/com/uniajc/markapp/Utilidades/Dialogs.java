package com.uniajc.markapp.Utilidades;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import com.uniajc.markapp.R;
import com.uniajc.markapp.RegisterActivity;
import com.uniajc.markapp.webService.WSMarkApp;

import java.util.StringTokenizer;

public class Dialogs extends DialogFragment{

    Context context;
    public EditText identifica;
    WSMarkApp ws;
    Utility util;

    public Dialogs(){
        ws = new WSMarkApp();
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog alertDialog = DialogCambioDisp();
        return  alertDialog;
    }

    public AlertDialog DialogCambioDisp(){
        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = getActivity().getLayoutInflater();

        View v = inflater.inflate(R.layout.dialog_cambiodisp, null);

        builder.setView(v);

        Button aceptar = (Button) v.findViewById(R.id.btnAceptar);
        identifica = (EditText)v.findViewById(R.id.edtDocumento);
        context = v.getContext();

        aceptar.setOnClickListener(
                new View.OnClickListener(){
                    @Override
                    public void onClick(View v) {
                        //Realizar cambio
                        AsyncCallWS task = new AsyncCallWS();
                        task.execute(identifica.getText().toString());
                    }
                }
        );

        return builder.create();
    }

    public class AsyncCallWS extends AsyncTask<String, Void, String> {

        ProgressDialog pd;

        public AsyncCallWS(){
            util = new Utility(context, getActivity());
        }

        @Override
        protected String doInBackground(String... params) {
            Log.i("tag", "doInBackgroundCambioDisp");

            String correcto = ws.WsCambioDispositivo(params[0], util.getMac());

            return correcto;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            pd = new ProgressDialog(context);
            pd.setMessage("loading...");
            pd.show();
        }

        @Override
        protected void onPostExecute(String result) {
            Log.i("tag", "onPostExecuteCambioDisp");

            if (pd != null)
            {
                pd.dismiss();
            }

            if(!result.isEmpty()){
                String CurrentString = result.toString();

                StringTokenizer tokens = new StringTokenizer(CurrentString, "|");
                String codigo = tokens.nextToken();
                String mensaje = tokens.nextToken();

                if(codigo.equals("0") || codigo.equals("2")){
                    getDialog().dismiss();
                }

                util.mensajeApp(mensaje);
            }

        }
    }
}
