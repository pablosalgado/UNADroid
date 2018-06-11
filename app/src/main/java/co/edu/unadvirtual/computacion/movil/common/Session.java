package co.edu.unadvirtual.computacion.movil.common;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class Session {
    /**
     * Determina si el usuario ha iniciado sesión en el dispositivo.
     *
     * @param context
     * @return <code>true</code> si el usuario ha iniciado sesión.
     */
    public static Boolean isLoggedIn(Context context) {
        SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(context);
        return !settings.getString("email", "").isEmpty();
    }

    /**
     * Retorna el email del usuario en la sesión.
     *
     * @param context
     * @return
     * @throws RuntimeException si no se ha inicidado sesión.
     */
    public static String getUserEmail(Context context) {
        if (!isLoggedIn(context)) {
            throw new RuntimeException("No se ha iniciado sesión.");
        }

        SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(context);
        return settings.getString("email", "");
    }

    /**
     * Inicia la sesión y guarda el email del usuario.
     *
     * @param context
     * @param email
     */
    public static void putUserEmail(Context context, String email) {
        SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = settings.edit();
        editor.putString("email", email);
        editor.apply();
    }

    /**
     * Elimina los datos de sesión.
     *
     * @param context
     */
    public static void logOut(Context context) {
        SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = settings.edit();
        editor.remove("email");
        editor.apply();
    }
}
