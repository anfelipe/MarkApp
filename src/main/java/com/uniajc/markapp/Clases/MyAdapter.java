package com.uniajc.markapp.Clases;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.uniajc.markapp.R;

import java.util.Vector;

public class MyAdapter extends BaseAdapter{

    private final Activity activity;
    private final Vector<Persona> lista;

    public MyAdapter(Activity activity, Vector<Persona> lista){
        super();
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
        View view  = inflater.inflate(R.layout.fragment_element, null, true);

        TextView textNom = (TextView) view.findViewById(R.id.txtNombreList);
        textNom.setText(lista.elementAt(position).nombre + " " + lista.elementAt(position).apellido);
        TextView textView = (TextView) view.findViewById(R.id.txtDocumentoList);
        textView.setText(lista.elementAt(position).documento);

        return view;
    }


}
