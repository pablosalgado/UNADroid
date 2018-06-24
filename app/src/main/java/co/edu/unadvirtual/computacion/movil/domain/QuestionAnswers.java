package co.edu.unadvirtual.computacion.movil.domain;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class QuestionAnswers {
    private int id;
    private int evaluationQuestionId;
    private String answer;
    private int correct;
    private String createdAt;
    private String updatedAt;

    public int getId() {
        return id;
    }

    public int getEvaluationQuestionId() {
        return evaluationQuestionId;
    }

    public String getAnswer() {
        return answer;
    }

    public int getCorrect() {
        return correct;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    /**
     * Construye una instancia de QuestionAnswers a partir de un objeto {@link JSONObject}
     *
     * @param jsonObject Un objeto {@link JSONObject} con los datos de las QuestionAnswers.
     * @return Una instancia de {@link QuestionAnswers}
     */
    public static QuestionAnswers fromJSON(JSONObject jsonObject) {
        // Fail-fast
        if (jsonObject == null) {
            throw new IllegalArgumentException("Parameter jsonObject is required");
        }

        try {
            QuestionAnswers q = new QuestionAnswers();

            q.id = jsonObject.getInt("id");
            q.evaluationQuestionId = jsonObject.getInt("evaluation_question_id");
            q.answer = jsonObject.getString("answer");
            q.correct = jsonObject.getInt("correct");
            q.createdAt = jsonObject.getString("createdAt");
            q.updatedAt = jsonObject.getString("updatedAt");

            return q;
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Construye una lista de {@link QuestionAnswers} a partir de un arreglo de objetos {@link JSONObject} que
     * se entregan encapsulados en una instancia de {@link JSONArray}
     *
     * @param jsonArray La instancia que encapsula los datos de los Question.
     * @return La lista de {@link QuestionAnswers}
     */
    public static List<QuestionAnswers> fromJSON(JSONArray jsonArray) {
        // Fail-fast
        if (jsonArray == null) {
            throw new IllegalArgumentException("Parameter jsonArray is required");
        }

        try {
            ArrayList<QuestionAnswers> lqa = new ArrayList<>();

            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject o = jsonArray.getJSONObject(i);
                lqa.add(QuestionAnswers.fromJSON(o));
            }

            return lqa;
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        QuestionAnswers ans = (QuestionAnswers) o;
        return id == ans.id;
    }

    @Override
    public int hashCode() {
        return new Integer(id).hashCode();
    }

    @Override
    public String toString() {
        return "Evaluation{" +
                "id='" + id + "',evaluationQuestonId='" + evaluationQuestionId+ "',answer='" + answer+ "',correct='" + correct+ "',createdAt='" + createdAt + "',updatedAt='" + updatedAt + "'" +
                '}';
    }
}
