package com.uniajc.markapp;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import com.uniajc.markapp.Fragments.ListEmpleados;
import com.uniajc.markapp.Fragments.Marcacion;
import com.uniajc.markapp.Utilidades.Utility;

public class EmpleadosActivity extends Activity{

    private ListEmpleados fragList;
    Marcacion fragMarcacion;
    private Utility util;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        util = new Utility(this);
        setContentView(R.layout.activity_empleados);

        getActionBar().setDisplayShowHomeEnabled(true);
        getActionBar().setTitle(Html.fromHtml("<font face='serif' color='#ffffff' size='9'>arkApp</font>"));

        SharedPreferences sp = getSharedPreferences("MarkAppPref", Context.MODE_PRIVATE);
        Boolean lista = sp.getBoolean("mostarLista", false);
        Boolean Mylista = sp.getBoolean("mylista", false);
        Boolean CambioDisp = sp.getBoolean("cambioD", false);

        if (lista){
            Bundle bundle = new Bundle();
            if(CambioDisp){
                bundle.putBoolean("cambioD", true);
            }else {
                bundle.putBoolean("cambioD", false);
            }
            fragList = new ListEmpleados();
            fragList.setArguments(bundle);
            getFragmentManager().beginTransaction().add(R.id.containerList, fragList, "ListaEmpleados").commit();
        }else if(Mylista){
            Bundle bundle = new Bundle();
            bundle.putString("documento", util.getIdentifica());
            fragMarcacion = new Marcacion();
            fragMarcacion.setArguments(bundle);
            getFragmentManager().beginTransaction().add(R.id.containerList, fragMarcacion, "Marcacion").commit();
        }

        util.preferenceClear();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);

        if(!util.getJefe()){
            MenuItem menuItem = menu.findItem(R.id.empMarcacion);
            menuItem.setVisible(false);

        }

        menu.findItem(R.id.autoCambioDisp).setVisible(false);
        menu.findItem(R.id.cambioDisp).setVisible(false);
        menu.findItem(R.id.myMarcacion).setVisible(false);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()){
            case R.id.empMarcacion:
                fragList = new ListEmpleados();
                getFragmentManager().beginTransaction().replace(R.id.containerList, fragList, "ListaEmpleados").commit();
                setContentView(R.layout.activity_empleados);
            default:
                return super.onOptionsItemSelected(item);
        }
    }

}
