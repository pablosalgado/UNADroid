package co.edu.unadvirtual.computacion.movil.domain;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Modelo de dominio para los objetos de la clase Video.
 */
public class Video {
    private int id;
    private String name;
    private String description;
    private String url;
    private int order;
    private int unitId;
    private String unitName;

    /**
     * Construye una instancia de Video a partir de un objeto {@link JSONObject}
     *
     * @param jsonObject Un objeto {@link JSONObject} con los datos del video.
     * @return Una instancia de {@link Video}
     */
    public static Video fromJSON(JSONObject jsonObject) {
        // Fail-fast
        if (jsonObject == null) {
            throw new IllegalArgumentException("Parameter jsonObject is required");
        }

        try {
            Video ret = new Video();

            ret.id = jsonObject.getInt("id");
            ret.unitName = jsonObject.getString("unitName");
            ret.name = jsonObject.getString("name");
            ret.description = jsonObject.getString("description");
            ret.url = jsonObject.getString("url");
            ret.order = jsonObject.getInt("order");

            return ret;
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Construye una lista de {@link Video} a partir de un arreglo de objetos {@link JSONObject} que
     * se entregan encapsulados en una instancia de {@link JSONArray}
     *
     * @param jsonArray La instancia que encapsula los datos de los videos.
     * @return La lista de {@link Video}
     */
    public static List<Video> fromJSON(JSONArray jsonArray) {
        // Fail-fast
        if (jsonArray == null) {
            throw new IllegalArgumentException("Parameter jsonArray is required");
        }

        try {
            ArrayList<Video> ret = new ArrayList<>();

            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject o = jsonArray.getJSONObject(i);
                ret.add(Video.fromJSON(o));
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

    public String getUrl() {
        return url;
    }

    public int getOrder() {
        return order;
    }

    public int getUnitId() {
        return unitId;
    }

    public String getUnitName() {
        return unitName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Video video = (Video) o;
        return id == video.id;
    }

    @Override
    public int hashCode() {
        return new Integer(id).hashCode();
    }

    @Override
    public String toString() {
        return name;
    }
}
