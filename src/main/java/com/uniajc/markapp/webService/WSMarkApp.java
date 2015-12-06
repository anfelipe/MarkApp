package com.uniajc.markapp.webService;

import android.app.Activity;
import android.content.Context;
import android.util.Log;

import com.uniajc.markapp.Clases.Area;
import com.uniajc.markapp.Clases.Marcacion;
import com.uniajc.markapp.Clases.Persona;
import com.uniajc.markapp.Utilidades.Utility;
import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

public class WSMarkApp {

    private final String NAMESPACE = "http://servicios.markApp.co.edu.uniajc/"; //"http://prueba.markApp/";//
    private final String URL = "http://192.168.1.5:30901/WebMarkApp/WebServicesMarkApp?wsdl"; //"http://10.10.28.116:30901/WebMarkApp/MyWebServices?wsdl";//
    private String SOAP_ACTION = "http://servicios.markApp.co.edu.uniajc/"; //"http://prueba.markApp/";//
    private String METHOD_NAME = "";
    private Utility util;
    Context Wscontext;
    Activity activity;

    public WSMarkApp(){}

    public WSMarkApp(Context context){
        Wscontext = context;
        util = new Utility(Wscontext);
    }

    public WSMarkApp(Context context, Activity activity){
        this.Wscontext = context;
        this.activity = activity;
        util = new Utility(Wscontext, activity);
    }

    public String[] WSoapIngreso(String metodo, String Mac){
        String[] resultado = null;

        METHOD_NAME = metodo;
        SOAP_ACTION.concat(metodo);

        SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);
        request.addProperty("param1", Mac);

        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
                SoapEnvelope.VER11);
        envelope.dotNet = false;
        envelope.setOutputSoapObject(request);
        HttpTransportSE androidHttpTransport = new HttpTransportSE(URL);

        try {
            androidHttpTransport.call(SOAP_ACTION, envelope);
            SoapObject resSoap =(SoapObject)envelope.bodyIn;

            resultado = new String[resSoap.getPropertyCount()];

            for(int i = 0; i < resultado.length; i++){
                resultado[i] = resSoap.getProperty(i).toString();
            }

        } catch (Exception e) {
            Log.v("Entro", e.toString());
            util.mensajeAsynk("No se pudo establecer conexion con el servidor");
            resultado = null;
        }

        return resultado;
    }

    public Area[] WSoapArea(){
        Area[] arrArea = null;

        METHOD_NAME = "consultarAreas";
        SOAP_ACTION.concat("consultarAreas");

        SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);

        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
                SoapEnvelope.VER11);
        envelope.dotNet = false;
        envelope.setOutputSoapObject(request);

        HttpTransportSE androidHttpTransport = new HttpTransportSE(URL);

        try {
            androidHttpTransport.call(SOAP_ACTION, envelope);

            SoapObject resSoap =(SoapObject)envelope.bodyIn;
            arrArea = new Area[resSoap.getPropertyCount()];
            arrArea[0] = new Area(0, "Seleccione");

            for(int i = 1; i < arrArea.length; i++){
                Area area = new Area();
                SoapObject result = (SoapObject) resSoap.getProperty(i);
                area.codigo = Integer.parseInt(result.getProperty("codigo").toString());
                area.nombre = result.getProperty("nombre").toString();
                arrArea[i] = area;
            }

        } catch (Exception e) {
            util.mensajeAsynk("No se pudo establecer conexion con el servidor");
        }

        return arrArea;
    }

    public String WsRegistrar(String mac, String identifica,String nombres, String apellidos, int area){
        String resultado = "";

        METHOD_NAME = "registrarEmpleado";
        SOAP_ACTION.concat("registrarEmpleado");

        SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);
        request.addProperty("param1", identifica);
        request.addProperty("param2", nombres);
        request.addProperty("param3", apellidos);
        request.addProperty("param4", area);
        request.addProperty("param5", mac);

        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
                SoapEnvelope.VER11);
        envelope.dotNet = false;
        envelope.setOutputSoapObject(request);
        HttpTransportSE androidHttpTransport = new HttpTransportSE(URL);

        try {
            androidHttpTransport.call(SOAP_ACTION, envelope);
            SoapPrimitive response = (SoapPrimitive) envelope.getResponse();

            resultado = response.toString();

        } catch (Exception e) {
            util.mensajeAsynk("No se pudo establecer conexion con el servidor");
        }
        return resultado;
    }

    public String WsCambioDispositivo(String identifica, String mac){
        String correcto = "";

        METHOD_NAME = "registrarCambioMAC";
        SOAP_ACTION.concat("registrarCambioMAC");

        SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);
        request.addProperty("param1", identifica);
        request.addProperty("param2", mac);

        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
                SoapEnvelope.VER11);
        envelope.dotNet = false;
        envelope.setOutputSoapObject(request);
        HttpTransportSE androidHttpTransport = new HttpTransportSE(URL);

        try {
            androidHttpTransport.call(SOAP_ACTION, envelope);
            SoapPrimitive response = (SoapPrimitive) envelope.getResponse();

            correcto = response.toString();

        } catch (Exception e) {
            util.mensajeAsynk("No se pudo establecer conexion con el servidor");
        }

        return  correcto;
    }

    public String[] WsGetPersona(String identifica){
        String[] resultado = null;

        METHOD_NAME = "getEmpleado2";
        SOAP_ACTION.concat("getEmpleado2");

        SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);
        request.addProperty("param1", identifica);

        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
                SoapEnvelope.VER11);
        envelope.dotNet = false;
        envelope.setOutputSoapObject(request);
        HttpTransportSE androidHttpTransport = new HttpTransportSE(URL);

        try {
            androidHttpTransport.call(SOAP_ACTION, envelope);
            SoapObject resSoap =(SoapObject)envelope.bodyIn;

            resultado = new String[resSoap.getPropertyCount()];

            for(int i = 0; i < resultado.length; i++){
                resultado[i] = resSoap.getProperty(i).toString();
            }

        } catch (Exception e) {
            util.mensajeAsynk("No se pudo establecer conexion con el servidor");
        }

        return  resultado;
    }

    public Persona[] getPersonal(int codigo){
        Persona[] arrPersona = null;

        METHOD_NAME = "getEmpleadosxCodigoArea";
        SOAP_ACTION.concat("getEmpleadosxCodigoArea");

        SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);
        request.addProperty("param1", codigo);

        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
                SoapEnvelope.VER11);
        envelope.dotNet = false;
        envelope.setOutputSoapObject(request);

        HttpTransportSE androidHttpTransport = new HttpTransportSE(URL);

        try {
            androidHttpTransport.call(SOAP_ACTION, envelope);

            SoapObject resSoap =(SoapObject)envelope.bodyIn;
            arrPersona = new Persona[resSoap.getPropertyCount()];

            for(int i = 0; i < arrPersona.length; i++){
                Persona persona = new Persona();
                SoapObject result = (SoapObject) resSoap.getProperty(i);
                persona.documento = result.getProperty("documento").toString();
                persona.nombre = result.getProperty("nombres").toString();
                persona.apellido = result.getProperty("apellidos").toString();
                arrPersona[i] = persona;
            }

        } catch (Exception e) {
            util.mensajeAsynk("No se pudo establecer conexion con el servidor");
        }

        return arrPersona;
    }

    public Marcacion[] getMarcacion(String identificacion){
        Marcacion[] arrMarcacion = null;

        METHOD_NAME = "consultarMarcacionesxEmpleado";
        SOAP_ACTION.concat("consultarMarcacionesxEmpleado");

        SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);
        request.addProperty("param1", identificacion);

        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
                SoapEnvelope.VER11);
        envelope.dotNet = false;
        envelope.setOutputSoapObject(request);

        HttpTransportSE androidHttpTransport = new HttpTransportSE(URL);

        try {
            androidHttpTransport.call(SOAP_ACTION, envelope);

            SoapObject resSoap =(SoapObject)envelope.bodyIn;
            arrMarcacion = new Marcacion[resSoap.getPropertyCount()];

            Log.v("index","" + resSoap.getPropertyCount());

            for(int i = 0; i < arrMarcacion.length; i++){
                Marcacion marcacion = new Marcacion();
                SoapObject result = (SoapObject) resSoap.getProperty(i);
                marcacion.fecha = result.getProperty("fecha").toString();
                marcacion.horaEntrada = result.getProperty("horaEntrada").toString();
                marcacion.hora = result.getProperty(3).toString();
                arrMarcacion[i] = marcacion;
            }

        } catch (Exception e) {
            util.mensajeAsynk("No se pudo establecer conexion con el servidor");
        }

        return arrMarcacion;
    }

    public String WsRegistrarMarcacion(String mac, String latitud, String longiud, String direccion, int codArea, String contenido){
        String correcto = "";

        METHOD_NAME = "registrarMarcacionXCodigo"; //registrarMarcacion
        SOAP_ACTION.concat("registrarMarcacionXCodigo");

        //mac, longitud, latitud, direccion, codigoArea

        SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);
        request.addProperty("mac", mac);
        request.addProperty("latitud", latitud);
        request.addProperty("longitud", longiud);
        request.addProperty("direccion", direccion);
        request.addProperty("codigoArea", codArea);
        request.addProperty("codigoQR", contenido);

        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
                SoapEnvelope.VER11);
        envelope.dotNet = false;
        envelope.setOutputSoapObject(request);
        HttpTransportSE androidHttpTransport = new HttpTransportSE(URL);

        try {
            androidHttpTransport.call(SOAP_ACTION, envelope);
            SoapPrimitive response = (SoapPrimitive) envelope.getResponse();

            correcto = response.toString();

        } catch (Exception e) {
            util.mensajeAsynk("No se pudo establecer conexion con el servidor");
        }

        return  correcto;
    }

    public String WsAutoCambioDispositivo(String identifica, String jefe){
        String correcto = "";

        METHOD_NAME = "autorizarCambioMAC";
        SOAP_ACTION.concat("autorizarCambioMAC");

        SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);
        request.addProperty("param1", identifica);
        request.addProperty("param2", jefe);

        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
                SoapEnvelope.VER11);
        envelope.dotNet = false;
        envelope.setOutputSoapObject(request);
        HttpTransportSE androidHttpTransport = new HttpTransportSE(URL);

        try {
            androidHttpTransport.call(SOAP_ACTION, envelope);
            SoapPrimitive response = (SoapPrimitive) envelope.getResponse();

            correcto = response.toString();

        } catch (Exception e) {
            util.mensajeAsynk("No se pudo establecer conexion con el servidor");
        }

        return  correcto;
    }

}
