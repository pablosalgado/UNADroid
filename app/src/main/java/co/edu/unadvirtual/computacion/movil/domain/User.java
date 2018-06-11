package co.edu.unadvirtual.computacion.movil.domain;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Modelo de dominio para los objetos de la clase User.
 */
public class User {
    private int id;
    private String email;
    private String password;
    private String firstName;
    private String lastName;

    /**
     * Construye una instancia de User a partir de un objeto {@link JSONObject}
     *
     * @param jsonObject Un objeto {@link JSONObject} con los datos del usuario.
     * @return Una instancia de {@link User}
     */
    public static User fromJSON(JSONObject jsonObject) {
        // Fail-fast
        if (jsonObject == null) {
            throw new IllegalArgumentException("Parameter jsonObject is required");
        }

        try {
            User ret = new User();

            ret.id = jsonObject.getInt("id");
            ret.email = jsonObject.getString("email");
            ret.password = jsonObject.getString("password");
            ret.firstName = jsonObject.getString("firstName");
            ret.lastName = jsonObject.getString("lastName");

            return ret;
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Construye una lista de {@link User} a partir de un arreglo de objetos {@link JSONObject} que
     * se entregan encapsulados en una instancia de {@link JSONArray}
     *
     * @param jsonArray La instancia que encapsula los datos de los Users.
     * @return La lista de {@link User}
     */
    public static List<User> fromJSON(JSONArray jsonArray) {
        // Fail-fast
        if (jsonArray == null) {
            throw new IllegalArgumentException("Parameter jsonArray is required");
        }

        try {
            ArrayList<User> ret = new ArrayList<>();

            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject o = jsonArray.getJSONObject(i);
                ret.add(User.fromJSON(o));
            }

            return ret;
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
    }

    public int getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return email.equals(user.email);
    }

    @Override
    public int hashCode() {
        return email.hashCode();
    }

    @Override
    public String toString() {
        return "User{" +
                "firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                '}';
    }
}
