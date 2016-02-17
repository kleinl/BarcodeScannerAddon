package myhealthhubassistant.unifreiburgdvs.de.barcodescanneraddon.json;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import myhealthhubassistant.unifreiburgdvs.de.barcodescanneraddon.MainActivity;

/**
 * Created by lukas on 11.11.15.
 */
public class SurveyItemToJson {
    public static JSONObject getJSONfromSurvey(SurveyItem surveyItem) throws JSONException {

        JSONObject jsonObj = new JSONObject();
        jsonObj.putOpt("KIND", "survey");
        jsonObj.putOpt("USER_ID", surveyItem.getId());
        jsonObj.putOpt("TIME", surveyItem.getTime());
        jsonObj.putOpt("DATE", surveyItem.getDate());
        jsonObj.putOpt("SURVEY", surveyItem.getSurvey());
        jsonObj.putOpt("DAY", surveyItem.getDay());
        jsonObj.putOpt("SIGNAL", surveyItem.getSignal());
        JSONArray jsonArray = new JSONArray();
        for (int i = 0; i < surveyItem.getValues().length; i++) {
            jsonArray.put(surveyItem.getValues()[i]);
        }
        jsonObj.putOpt("RESULT", jsonArray);
        return jsonObj;
    }
}
