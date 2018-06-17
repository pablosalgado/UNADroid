package co.edu.unadvirtual.computacion.movil.domain;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.List;

public class Resource {

    private int id;
    private int resourceTypeId;
    private int unitId;
    private int topicId;
    private String name;
    private String description;
    private String url;
    private int order;
    private String iconName;
    private String createdAt;
    private String updatedAt;

    public int getId() {
        return id;
    }

    public int getResourceTypeId() {
        return resourceTypeId;
    }

    public int getUnitId() {
        return unitId;
    }

    public int getTopicId() {
        return topicId;
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

    public String getIconName() {
        return iconName;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    /**
     * Construye una instancia de Resource a partir de un objeto {@link JSONObject}
     *
     * @param jsonObject Un objeto {@link JSONObject} con los datos de la Resource.
     * @return Una instancia de {@link Resource}
     */
    public static Resource fromJSON(JSONObject jsonObject) {
        // Fail-fast
        if (jsonObject == null) {
            throw new IllegalArgumentException("Parameter jsonObject is required");
        }

        try {
            Resource r = new Resource();

            r.id = jsonObject.getInt("id");
            r.resourceTypeId = jsonObject.getInt("resource_type_id");
            r.unitId = jsonObject.getInt("unit_id");
            r.unitId = jsonObject.getInt("topic_id");
            r.name = jsonObject.getString("name");
            r.description = jsonObject.getString("description");
            r.url = jsonObject.getString("url");
            r.order = jsonObject.getInt("order");
            r.iconName = jsonObject.getString("iconName");
            r.createdAt = jsonObject.getString("createdAt");
            r.updatedAt = jsonObject.getString("updatedAt");

            return r;
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Construye una lista de {@link Resource} a partir de un arreglo de objetos {@link JSONObject} que
     * se entregan encapsulados en una instancia de {@link JSONArray}
     *
     * @param jsonArray La instancia que encapsula los datos de los units.
     * @return La lista de {@link Resource}
     */
    public static List<Resource> fromJSON(JSONArray jsonArray) {
        // Fail-fast
        if (jsonArray == null) {
            throw new IllegalArgumentException("Parameter jsonArray is required");
        }

        try {
            ArrayList<Resource> lr = new ArrayList<>();

            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject o = jsonArray.getJSONObject(i);
                lr.add(Resource.fromJSON(o));
            }

            return lr;
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Resource res = (Resource) o;
        return id == res.id;
    }

    @Override
    public int hashCode() {
        return new Integer(id).hashCode();
    }

    @Override
    public String toString() {
        return "Resource{" +
                "id='" + id + "',unit_id='" + unitId + "',description='" + description + "',createdAt='" + createdAt + "',updatedAt='" + updatedAt + "'" +
                '}';
    }
}
