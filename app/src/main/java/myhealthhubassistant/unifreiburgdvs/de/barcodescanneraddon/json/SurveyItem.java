package myhealthhubassistant.unifreiburgdvs.de.barcodescanneraddon.json;

/**
 * Created by lukas on 06.08.15.
 */
public class SurveyItem {
    private String kind;
    private String id;
    private String date;
    private String time;
    private int survey;
    private int[] values;
    public SurveyItem(String kind, String id, String date, String time, int survey, int[] values) {
        this.kind = kind;
        this.id = id;
        this.date = date;
        this.time = time;
        this.survey = survey;
        this.values = new int[values.length];
        this.values = values;
    }

    public String getTime() {
        return time;
    }

    public int getSurvey() {
        return survey;
    }

    public int[] getValues() {
        return values;
    }

    public String getKind() {
        return kind;
    }

    public String getId() {
        return id;
    }

    public String getDate() {
        return date;
    }
}
