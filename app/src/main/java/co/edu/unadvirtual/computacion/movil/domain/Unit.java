package co.edu.unadvirtual.computacion.movil.domain;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Modelo de dominio para los objetos de la clase Unit.
 */
public class Unit {
    private int id;
    private String name;
    private String description;
    private String iconName;
    private int order;

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public String getIconName() {
        return iconName;
    }

    public int getOrder() {
        return order;
    }

    /**
     * Construye una instancia de Unit a partir de un objeto {@link JSONObject}
     *
     * @param jsonObject Un objeto {@link JSONObject} con los datos de la unidad.
     * @return Una instancia de {@link Unit}
     */
    public static Unit fromJSON(JSONObject jsonObject) {
        // Fail-fast
        if (jsonObject == null) {
            throw new IllegalArgumentException("Parameter jsonObject is required");
        }

        try {
            Unit ret = new Unit();

            ret.id = jsonObject.getInt("id");
            ret.name = jsonObject.getString("name");
            ret.description = jsonObject.getString("description");
            ret.iconName = jsonObject.getString("iconName");
            ret.order = jsonObject.getInt("order");

            return ret;
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Construye una lista de {@link Unit} a partir de un arreglo de objetos {@link JSONObject} que
     * se entregan encapsulados en una instancia de {@link JSONArray}
     *
     * @param jsonArray La instancia que encapsula los datos de los units.
     * @return La lista de {@link Unit}
     */
    public static List<Unit> fromJSON(JSONArray jsonArray) {
        // Fail-fast
        if (jsonArray == null) {
            throw new IllegalArgumentException("Parameter jsonArray is required");
        }

        try {
            ArrayList<Unit> ret = new ArrayList<>();

            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject o = jsonArray.getJSONObject(i);
                ret.add(Unit.fromJSON(o));
            }

            return ret;
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Unit unit = (Unit) o;
        return id == unit.id;
    }

    @Override
    public int hashCode() {
        return new Integer(id).hashCode();
    }

    @Override
    public String toString() {
        return "Unit{" +
                "name='" + name + '\'' +
                '}';
    }
}
