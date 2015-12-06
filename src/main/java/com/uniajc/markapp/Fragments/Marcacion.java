package com.uniajc.markapp.Fragments;

import android.app.Activity;
import android.app.Fragment;
import android.content.pm.ActivityInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
import com.uniajc.markapp.R;
import com.uniajc.markapp.Utilidades.Utility;
import com.uniajc.markapp.webService.WSMarkApp;

import java.util.Arrays;
import java.util.Vector;

public class Marcacion extends Fragment{

    private com.uniajc.markapp.Clases.Marcacion[] arrMarcacion;
    public WSMarkApp ws;
    ListView lista;
    public Utility util;

    public Marcacion(){
        ws = new WSMarkApp(getActivity(), getActivity());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_list_marcacion, container, false);
        return view;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        if(getFragmentManager().findFragmentByTag("Marcación").isVisible()){
            MenuItem menuItem = menu.findItem(R.id.empMarcacion);
            menuItem.setVisible(true);
        }
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if(isVisibleToUser){
            Activity activity = getActivity();
            if(activity != null){
                activity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
            }
        }
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        String strDocument =  getArguments().getString("documento");
        util = new Utility(getActivity(), getActivity());

        AsyncCallWS task = new AsyncCallWS();
        task.execute(strDocument);
    }

    public void llenarGrilla(){
        lista = (ListView) getView().findViewById(R.id.lstViewMarcacion);
        lista.setAdapter(new GridAdapter(getActivity(), listaElementosV()));
    }

    public Vector<com.uniajc.markapp.Clases.Marcacion> listaElementosV(){
        Vector<com.uniajc.markapp.Clases.Marcacion> vector;
        vector = new Vector<com.uniajc.markapp.Clases.Marcacion>(Arrays.asList(arrMarcacion));

        return vector;
    }

    public class AsyncCallWS extends AsyncTask<String, Void, Void> {

        @Override
        protected Void doInBackground(String... params) {

            try {
                arrMarcacion = ws.getMarcacion(params[0]);
            }catch (Exception e){
                util.mensajeAsynk("Error. No se pudieron obtener las marcaciones!");
            }

            return null;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            util.Loading(getActivity());
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            llenarGrilla();
            util.LoadingCancel();
        }
    }

    public class GridAdapter extends BaseAdapter {

        private final Activity activity;
        private final Vector<com.uniajc.markapp.Clases.Marcacion> lista;

        public GridAdapter(Activity activity, Vector<com.uniajc.markapp.Clases.Marcacion> lista) {
            this.activity = activity;
            this.lista = lista;
        }

        @Override
        public int getCount() {
            return lista.size();
        }

        @Override
        public Object getItem(int position) {
            return lista.elementAt(position);
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            LayoutInflater inflater = activity.getLayoutInflater();
            View view  = inflater.inflate(R.layout.fragment_marcacion, null, true);

            try {
                TextView textNom = (TextView) view.findViewById(R.id.txtFecha);
                TextView textView = (TextView) view.findViewById(R.id.txtHoraEntrada);
                TextView textHs = (TextView) view.findViewById(R.id.txtHoraSalida);
                textNom.setText(lista.elementAt(position).fecha);
                textView.setText(lista.elementAt(position).horaEntrada);
                textHs.setText(lista.elementAt(position).hora);
            }catch (Exception e){
                util.mensajeApp("Error. No se pudieron procesar los datos!");
            }

            return view;
        }
    }
}
