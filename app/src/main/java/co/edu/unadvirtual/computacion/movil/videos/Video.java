package co.edu.unadvirtual.computacion.movil.videos;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class Video {
    private int id;
    private String unit;
    private String name;
    private String description;
    private String url;
    private int order;

    public int getId() {
        return id;
    }

    public String getUnit() {
        return unit;
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

    @Override
    public String toString() {
        return name;
    }

    public static Video fromJSON(JSONObject o) {
        Video ret = new Video();

        try {
            ret.id = o.getInt("id");
            ret.unit = o.getString("unit");
            ret.name = o.getString("name");
            ret.description = o.getString("description");
            ret.url = o.getString("url");
            ret.order = o.getInt("order");
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }

        return ret;
    }

    public static List<Video> fromJSON(JSONArray jsonArray){
        ArrayList<Video> ret = new ArrayList<>();
        try {
            for(int i = 0; i < jsonArray.length(); i++) {
                JSONObject o = jsonArray.getJSONObject(0);
                ret.add(Video.fromJSON(o));
            }
        } catch (JSONException e){
            throw new RuntimeException(e);
        }

        return ret;
    }
}
