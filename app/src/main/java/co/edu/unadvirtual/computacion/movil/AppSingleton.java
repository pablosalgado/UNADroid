package co.edu.unadvirtual.computacion.movil;

import android.content.Context;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

/**
 * Módulo Singleton para manejar todas las operaciones de red al servidor de UNADroid. Este módulo
 * hace uso de la biblioteca Volley que permite simplificar el manejo de las operaciones de red.
 */
public class AppSingleton {
    /**
     * Endpoint para las peticiones REST al servidor de UNADroid
     */
    //public final static String UNADROID_SERVER_ENDPOINT = "https://unadroid.tk/api"; /*PRODUCCION*/
    //public final static String UNADROID_SERVER_ENDPOINT = "http://unadroid.tk:3002/api"; /*JUAN*/
    public final static String UNADROID_SERVER_ENDPOINT = "http://unadroid.tk:3003/api"; /*YERSSON*/

    private static AppSingleton instance;
    private RequestQueue requestQueue;
    private static Context context;

    private AppSingleton(Context context) {
        AppSingleton.context = context;
        requestQueue = getRequestQueue();
    }

    public static synchronized AppSingleton getInstance(Context context) {
        if (instance == null) {
            instance = new AppSingleton(context);
        }
        return instance;
    }

    public RequestQueue getRequestQueue() {
        if (requestQueue == null) {
            // getApplicationContext() is key, it keeps you from leaking the
            // Activity or BroadcastReceiver if someone passes one in.
            requestQueue = Volley.newRequestQueue(context.getApplicationContext());
        }
        return requestQueue;
    }

    public <T> void addToRequestQueue(Request<T> req,String tag) {
        req.setTag(tag);
        getRequestQueue().add(req);
    }
}