package co.edu.unadvirtual.computacion.movil.domain;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class Topic {
    private int id;
    private String name;
    private String description;
    private int order;
    private int unitId;

    /**
     * Construye una instancia de Topic a partir de un objeto {@link JSONObject}
     *
     * @param jsonObject Un objeto {@link JSONObject} con los datos del topic.
     * @return Una instancia de {@link Topic}
     */
    public static Topic fromJSON(JSONObject jsonObject) {
        // Fail-fast
        if (jsonObject == null) {
            throw new IllegalArgumentException("Parameter jsonObject is required");
        }

        try {
            Topic ret = new Topic();

            ret.id = jsonObject.getInt("id");
            ret.name = jsonObject.getString("name");
            ret.description = jsonObject.getString("description");
            ret.order = jsonObject.getInt("order");
            ret.unitId = jsonObject.getInt("unitId");

            return ret;
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Construye una lista de {@link Topic} a partir de un arreglo de objetos {@link JSONObject} que
     * se entregan encapsulados en una instancia de {@link JSONArray}
     *
     * @param jsonArray La instancia que encapsula los datos de los topics.
     * @return La lista de {@link Topic}
     */
    public static List<Topic> fromJSON(JSONArray jsonArray) {
        // Fail-fast
        if (jsonArray == null) {
            throw new IllegalArgumentException("Parameter jsonArray is required");
        }

        try {
            ArrayList<Topic> ret = new ArrayList<>();

            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject o = jsonArray.getJSONObject(i);
                ret.add(Topic.fromJSON(o));
            }

            return ret;
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public int getOrder() {
        return order;
    }

    public int getUnitId() {
        return unitId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Topic topic = (Topic) o;
        return id == topic.id;
    }

    @Override
    public int hashCode() {
        return new Integer(id).hashCode();
    }

    @Override
    public String toString() {
        return "Topic{" +
                "name='" + name + '\'' +
                '}';
    }
}
