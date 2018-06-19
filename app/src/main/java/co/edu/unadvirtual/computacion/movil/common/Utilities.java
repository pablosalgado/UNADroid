package co.edu.unadvirtual.computacion.movil.common;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

public class Utilities {


    /**
     * Método para verificar si existe un conexion activa a internet.
     *
     * @param context un instancia de la clase android.content.Context.
     */
    public static boolean connectionExist(Context context) {
        boolean connected = false;
        try {
            ConnectivityManager connectivityManager;
            connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
            connected = networkInfo != null && networkInfo.isAvailable() && networkInfo.isConnected();
            return connected;

        } catch (Exception e) {
            Log.v("connectivity", e.toString());
        }
        return connected;
    }

    /**
     * La expresión regular para validar las contraseña. Esta expresión regular exige 8 caracteres
     * mínimo con al menos una letra minúscula, una letra mayúscula y un número.
     *
     * @return La expresión regular.
     */
    public static String passwordRegex(){
        return "(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)[a-zA-Z\\d]{8,}";
    }

    /**
     * La expresión regular para validar nombres y apellidos. Esta expresión regular exige al menos
     * 3 caracteres y solo acepta letras y espacios.
     *
     * @return La expresión regular.
     */
    public static String namesRegex() {
        return "[a-zA-Z\\s]{3,}";
    }
}
