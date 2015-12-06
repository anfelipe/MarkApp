package com.uniajc.markapp.Fragments;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Fragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import com.uniajc.markapp.Clases.MyAdapter;
import com.uniajc.markapp.Clases.Persona;
import com.uniajc.markapp.MainActivity;
import com.uniajc.markapp.R;
import com.uniajc.markapp.Utilidades.Utility;
import com.uniajc.markapp.webService.WSMarkApp;
import java.util.Arrays;
import java.util.StringTokenizer;
import java.util.Vector;

public class ListEmpleados extends Fragment{

    ListView lista;

    public WSMarkApp ws;
    public Persona[] arrPersona;
    public Utility util;
    Marcacion fragMarcacion;

    public ListEmpleados(){
        ws = new WSMarkApp();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_list_emp, container, false);
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        util = new Utility(getActivity(), getActivity());
        setHasOptionsMenu(true);
        util = new Utility(getActivity(), getActivity());
        AsyncCallWS task = new AsyncCallWS();
        task.execute("1");
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        if(getFragmentManager().findFragmentByTag("ListaEmpleados").isVisible()){
            MenuItem menuItem = menu.findItem(R.id.empMarcacion);
            menuItem.setVisible(false);
            menu.findItem(R.id.autoCambioDisp).setVisible(false);
        }
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);

        if(isVisibleToUser){
            Activity activity = getActivity();
            if(activity != null){
                activity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_FULL_SENSOR);
            }
        }
    }

    private void llenarLista(){
        lista = (ListView) getView().findViewById(R.id.lstViewEmpleados);
        lista.setAdapter(new MyAdapter(getActivity(), listaElementosV()));

        lista.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, final View view, int position, long id) {
                final Persona item = (Persona) parent.getItemAtPosition(position);
                final SharedPreferences sp = getActivity().getSharedPreferences("MarkAppPref", Context.MODE_PRIVATE);
                Boolean cambio = sp.getBoolean("cambioD", false);

                if (cambio) {
                    util.preferenceEdit(false, "cambioD");
                    String nombre = item.nombre + " " + item.apellido;
                    String documento = item.documento;
                    AutorizarCambio(nombre, documento);
                } else {
                    view.animate().setDuration(1000).alpha(0)
                            .withEndAction(new Runnable() {
                                @Override
                                public void run() {
                                    Bundle bundle = new Bundle();
                                    bundle.putString("documento", item.documento);
                                    Log.v("documento", item.documento);
                                    fragMarcacion = new Marcacion();
                                    fragMarcacion.setArguments(bundle);
                                    getFragmentManager().beginTransaction().replace(R.id.containerList, fragMarcacion, "Marcacion").commit();
                                    Activity activity = getActivity();
                                    activity.setContentView(R.layout.activity_empleados);
                                }
                            });
                }
            }
        });

        if(arrPersona.length == 0){
            getActivity().startActivity(new Intent(getActivity(), MainActivity.class));
            getActivity().finish();
            util.mensajeApp("No hay empleados registrados en el area!");
        }
    }

    public Vector<Persona> listaElementosV(){
        Vector<Persona> vector;
        vector = new Vector<Persona>(Arrays.asList(arrPersona));

        return vector;
    }

    private void AutorizarCambio(final String nombre, final String documento){
        AlertDialog dialog = new AlertDialog.Builder(getActivity())
                .setTitle("Información")
                .setMessage("Desea autorizar el cambio de equipo para: " + nombre)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        AsyncCallWS task = new AsyncCallWS();
                        task.execute("0", documento);
                    }
                })
                .setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                }).create();
        dialog.show();
    }

    public class AsyncCallWS extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {
            String strResult = "";
            try {
                if(params[0].equalsIgnoreCase("1")){
                    arrPersona = ws.getPersonal(Integer.parseInt(util.getArea()));
                }else if (params[0].equalsIgnoreCase("0")){
                    strResult = ws.WsAutoCambioDispositivo(params[1], util.getIdentifica());
                }
            }catch (Exception e){
                util.mensajeAsynk("Error. el servidor no responde!");
            }

            return strResult;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            util.Loading(getActivity());
        }

        @Override
        protected void onPostExecute(String aVoid) {

            if(aVoid.isEmpty()){
                llenarLista();
            }else{
                String CurrentString = aVoid.toString();

                StringTokenizer tokens = new StringTokenizer(CurrentString, "|");
                String codigo = tokens.nextToken();
                String mensaje = tokens.nextToken();

                util.mensajeAsynkDialog(mensaje);
            }

            util.LoadingCancel();
        }
    }
}
