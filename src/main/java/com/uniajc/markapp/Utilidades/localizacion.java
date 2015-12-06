package com.uniajc.markapp.Utilidades;

import android.content.Context;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class localizacion implements LocationListener{
    LocationManager handle;

    Context context;
    Location loc;

    public localizacion(Context context){
        this.context = context;
    }

    public void iniciarLocalizacion() throws IOException {
        //Objeto que gestiona las localizaciones
        handle = (LocationManager)context.getSystemService(Context.LOCATION_SERVICE);

        Criteria c = new Criteria();
        c.setAccuracy(Criteria.ACCURACY_FINE);

        //mejor posicion posible
        String provider = handle.getBestProvider(c, true);
        Log.v("Mejor posicion", provider);

        //Activar las notificaciones
        handle.requestLocationUpdates(provider, 10000, 1, this);

        //ultima posicion conocida dada por el provedor
        loc =  handle.getLastKnownLocation(provider);
        //posicionAtual(loc);
    }

    public void posicionAtual(Location loc) throws IOException {
        if(loc == null){
            //no se encontro localización
            Log.v("Sin localizacion", "No se encontraron datos");
        }else{
            Log.v("latitud", String.valueOf(loc.getLatitude()));
            Log.v("longitud", String.valueOf(loc.getLongitude()));

            /*Geocoder geocoder = new Geocoder(context, Locale.getDefault());
            List<Address> list = null;
                    list = geocoder.getFromLocation(loc.getLatitude(), loc.getLongitude(), 1);

            Log.v("Dirección", list.get(0).getAddressLine(0));
            Log.v("Ciudad", list.get(0).getLocality());
            Log.v("Departamento", list.get(0).getAdminArea());
            Log.v("Pais", list.get(0).getCountryName());
            Log.v("Codigo Postal", list.get(0).getPostalCode());*/
        }
    }

    public String getLatitud(){
        return String.valueOf(loc.getLatitude());
    }

    public String getLongitud(){
        return String.valueOf(loc.getLongitude());
    }

    public String getDireccion()throws IOException{
        /*Geocoder geocoder = new Geocoder(context, Locale.getDefault());
        List<Address> list = null;
        list = geocoder.getFromLocation(loc.getLatitude(), loc.getLongitude(), 1);

        return list.get(0).getAddressLine(0);*/

        return  "Casa";
    }

    public void pararservicio(){
        handle.removeUpdates(this);
    }

    @Override
    public void onLocationChanged(Location location) {
        try {
            posicionAtual(location);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }
}
