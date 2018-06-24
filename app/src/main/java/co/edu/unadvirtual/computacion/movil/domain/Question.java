package co.edu.unadvirtual.computacion.movil.domain;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class Question {
    private int id;
    private int evaluationId;
    private int questionTypeId;
    private String question;
    private String createdAt;
    private String updatedAt;

    public int getId() {
        return id;
    }

    public int getEvaluationId() {
        return evaluationId;
    }

    public int getQuestionTypeId() {
        return questionTypeId;
    }

    public String getQuestion() {
        return question;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }


    /**
     * Construye una instancia de Question a partir de un objeto {@link JSONObject}
     *
     * @param jsonObject Un objeto {@link JSONObject} con los datos de las Preguntas.
     * @return Una instancia de {@link Question}
     */
    public static Question fromJSON(JSONObject jsonObject) {
        // Fail-fast
        if (jsonObject == null) {
            throw new IllegalArgumentException("Parameter jsonObject is required");
        }

        try {
            Question q = new Question();

            q.id = jsonObject.getInt("id");
            q.evaluationId = jsonObject.getInt("evaluation_id");
            q.questionTypeId = jsonObject.getInt("question_type_id");
            q.question = jsonObject.getString("question");
            q.createdAt = jsonObject.getString("createdAt");
            q.updatedAt = jsonObject.getString("updatedAt");

            return q;
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Construye una lista de {@link Question} a partir de un arreglo de objetos {@link JSONObject} que
     * se entregan encapsulados en una instancia de {@link JSONArray}
     *
     * @param jsonArray La instancia que encapsula los datos de los Question.
     * @return La lista de {@link Question}
     */
    public static List<Question> fromJSON(JSONArray jsonArray) {
        // Fail-fast
        if (jsonArray == null) {
            throw new IllegalArgumentException("Parameter jsonArray is required");
        }

        try {
            ArrayList<Question> lq = new ArrayList<>();

            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject o = jsonArray.getJSONObject(i);
                lq.add(Question.fromJSON(o));
            }

            return lq;
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Question ques = (Question) o;
        return id == ques.id;
    }

    @Override
    public int hashCode() {
        return new Integer(id).hashCode();
    }

    @Override
    public String toString() {
        return "Evaluation{" +
                "id='" + id + "',evaluationId='" + evaluationId+ "',questionTypeId='" + questionTypeId+ "',question='" + question+ "',createdAt='" + createdAt + "',updatedAt='" + updatedAt + "'" +
                '}';
    }
}
