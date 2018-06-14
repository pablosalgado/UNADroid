package co.edu.unadvirtual.computacion.movil.domain;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class Evaluation {
    private int id;
    private int unitId;
    private String description;
    private String createdAt;
    private String updatedAt;

    public int getId() {
        return id;
    }

    public int getUnitId() {
        return unitId;
    }

    public String getDescription() {
        return description;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    /**
     * Construye una instancia de Evalutaion a partir de un objeto {@link JSONObject}
     *
     * @param jsonObject Un objeto {@link JSONObject} con los datos de la Evaluacion.
     * @return Una instancia de {@link Evaluation}
     */
    public static Evaluation fromJSON(JSONObject jsonObject) {
        // Fail-fast
        if (jsonObject == null) {
            throw new IllegalArgumentException("Parameter jsonObject is required");
        }

        try {
            Evaluation e = new Evaluation();

            e.id = jsonObject.getInt("id");
            e.unitId = jsonObject.getInt("unit_id");
            e.description = jsonObject.getString("description");
            e.createdAt = jsonObject.getString("createdAt");
            e.updatedAt = jsonObject.getString("updatedAt");

            return e;
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Construye una lista de {@link Evaluation} a partir de un arreglo de objetos {@link JSONObject} que
     * se entregan encapsulados en una instancia de {@link JSONArray}
     *
     * @param jsonArray La instancia que encapsula los datos de los units.
     * @return La lista de {@link Evaluation}
     */
    public static List<Evaluation> fromJSON(JSONArray jsonArray) {
        // Fail-fast
        if (jsonArray == null) {
            throw new IllegalArgumentException("Parameter jsonArray is required");
        }

        try {
            ArrayList<Evaluation> le = new ArrayList<>();

            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject o = jsonArray.getJSONObject(i);
                le.add(Evaluation.fromJSON(o));
            }

            return le;
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Evaluation eval = (Evaluation) o;
        return id == eval.id;
    }

    @Override
    public int hashCode() {
        return new Integer(id).hashCode();
    }

    @Override
    public String toString() {
        return "Evaluation{" +
                "id='" + id + "',unit_id='" + unitId + "',description='" + description + "',createdAt='" + createdAt + "',updatedAt='" + updatedAt + "'" +
                '}';
    }
}
