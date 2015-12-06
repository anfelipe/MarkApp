package com.uniajc.markapp.Fragments;

import android.app.Fragment;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.uniajc.markapp.R;
import com.uniajc.markapp.Utilidades.Utility;
import com.uniajc.markapp.webService.WSMarkApp;

public class UserInfo extends Fragment {

    Utility util;
    public WSMarkApp ws;
    TextView nombre;
    TextView apellido;
    TextView documento;
    TextView area;
    TextView txtUltimaMarc;

    public UserInfo(){
        ws = new WSMarkApp(getActivity());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_user_info, container, false);
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        nombre = (TextView) getView().findViewById(R.id.txtNombreInfo);
        apellido = (TextView) getView().findViewById(R.id.txtApellidoInfo);
        documento = (TextView) getView().findViewById(R.id.txtIdentificaInfo);
        area = (TextView) getView().findViewById(R.id.txtAreaInfo);
        txtUltimaMarc = (TextView) getView().findViewById(R.id.txtUltimaMarc);

        util = new Utility(getActivity(), getActivity());
        String strDocument =  getArguments().getString("documento");

        if(strDocument == null){
            getPersona(util.getIdentifica());
        }else{
            getPersona(strDocument);
        }
    }

    public class AsyncCallWS extends AsyncTask<String, Void, String[]> {

        @Override
        protected String[] doInBackground(String... params) {
            Log.i("tag", "doInBackgroundUserInfo");
            String[] resultado = null;

            if (params[0].isEmpty()){
                resultado = ws.WsGetPersona(util.getIdentifica());
            }else{
                resultado = ws.WsGetPersona(params[0]);
            }

            return resultado;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            util.Loading(getActivity());
        }

        @Override
        protected void onPostExecute(String[] result) {
            if(!result.toString().isEmpty()){
                documento.setText(result[0]);
                nombre.setText(result[1]);
                apellido.setText(result[2]);
                area.setText(result[3]);
                txtUltimaMarc.setText(result[4]);
            }

           util.LoadingCancel();
        }
    }

    private void  getPersona(String documento){
        AsyncCallWS task = new AsyncCallWS();
        task.execute(documento);
    }
}
