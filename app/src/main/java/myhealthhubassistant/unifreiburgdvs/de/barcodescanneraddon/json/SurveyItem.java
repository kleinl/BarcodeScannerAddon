package myhealthhubassistant.unifreiburgdvs.de.barcodescanneraddon.json;

/**
 * Created by lukas on 06.08.15.
 * Class to hold a SurveyItem.
 */
public class SurveyItem {
    private final String id;
    private final String date;
    private final String time;
    private final int survey;
    private final int day;
    private final int signal;
    private int[] values;
    public SurveyItem(String id, String date, String time, int survey, int day, int signal, int[] values) {
        this.id = id;
        this.date = date;
        this.time = time;
        this.survey = survey;
        this.values = new int[values.length];
        this.values = values;
        this.day = day;
        this.signal = signal;
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

    public String getId() {
        return id;
    }

    public String getDate() {
        return date;
    }

    public int getDay() { return day; }

    public int getSignal() { return signal; }
}
