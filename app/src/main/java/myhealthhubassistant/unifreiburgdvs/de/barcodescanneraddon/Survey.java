package myhealthhubassistant.unifreiburgdvs.de.barcodescanneraddon;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.NumberPicker;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import myhealthhubassistant.unifreiburgdvs.de.barcodescanneraddon.json.SurveyItem;
import myhealthhubassistant.unifreiburgdvs.de.barcodescanneraddon.json.SurveyItemToJson;

/**
 * Created by lukas on 11.11.15.
 */
public class Survey extends AppCompatActivity {
    private TextView question;
    private RadioGroup decicion;
    private RadioButton radioButton1;
    private RadioButton radioButton7;
    private RadioGroup.OnCheckedChangeListener listener;
    private int currentValue = 0;
    private int surveyNumber;
    private int questionNumber = 0;
    private String[] question1;
    private String[] question2;
    private String[] question3;
    private String[] question4;
    private CharSequence[] question5;
    private String[] question6Left;
    private String[] question6Right;
    private int[] answer;
    private NumberPicker minutePicker;
    private SharedPreferences prefs;
    private int time;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.survey);
        Resources res = getResources();

        prefs = this.getSharedPreferences(
                "barcodescanneraddon.sharedPrefs", Context.MODE_PRIVATE);

        question1 = res.getStringArray(R.array.survey1);
        question2 = res.getStringArray(R.array.survey2);
        question3 = res.getStringArray(R.array.survey3);
        question4 = res.getStringArray(R.array.survey4);
        question5 = res.getTextArray(R.array.survey5);
        question6Left = res.getStringArray(R.array.survey6left);
        question6Right = res.getStringArray(R.array.survey6right);
        int survey = getIntent().getIntExtra("survey", -1);
        time = getIntent().getIntExtra("time", -1);
        Log.e("testsurvey", String.valueOf(survey));
        Log.e("testtime", String.valueOf(time));

        question = (TextView) findViewById(R.id.question);
        decicion = (RadioGroup) findViewById(R.id.radioGroup);
        radioButton1 = (RadioButton) findViewById(R.id.radioButton1);
        RadioButton radioButton2 = (RadioButton) findViewById(R.id.radioButton2);
        RadioButton radioButton3 = (RadioButton) findViewById(R.id.radioButton3);
        RadioButton radioButton4 = (RadioButton) findViewById(R.id.radioButton4);
        RadioButton radioButton5 = (RadioButton) findViewById(R.id.radioButton5);
        RadioButton radioButton6 = (RadioButton) findViewById(R.id.radioButton6);
        radioButton7 = (RadioButton) findViewById(R.id.radioButton7);

        minutePicker = (NumberPicker) findViewById(R.id.minutePicker);
        minutePicker.setVisibility(View.GONE);

        Button next = (Button) findViewById(R.id.nextQuestionButton);
        next.setVisibility(View.GONE);

        Intent newIntent = new Intent(Survey.this, AlarmReceiver.class);
        newIntent.putExtra("time", this.time);
        newIntent.putExtra("usage", "delete");
        sendBroadcast(newIntent);

        listener = new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.radioButton1:
                        currentValue = 1;
                        break;
                    case R.id.radioButton2:
                        currentValue = 2;
                        break;
                    case R.id.radioButton3:
                        currentValue = 3;
                        break;
                    case R.id.radioButton4:
                        currentValue = 4;
                        break;
                    case R.id.radioButton5:
                        currentValue = 5;
                        break;
                    case R.id.radioButton6:
                        currentValue = 6;
                        break;
                    case R.id.radioButton7:
                        currentValue = 7;
                        break;
                }
                newQuestion();
            }
        };

        surveyNumber = survey;

        Button.OnClickListener buttonListener = new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (surveyNumber == 4) {
                    currentValue = minutePicker.getValue();
                }
                newQuestion();
            }
        };
        next.setOnClickListener(buttonListener);

        decicion.setOnCheckedChangeListener(listener);
        switch (surveyNumber) {
            case 0:
                radioButton1.setText(res.getString(R.string.survey1button1));
                radioButton7.setText(res.getString(R.string.survey1button7));
                break;
            case 1:
                radioButton1.setText(res.getString(R.string.survey2button1));
                radioButton5.setText(res.getString(R.string.survey2button5));
                radioButton6.setVisibility(View.INVISIBLE);
                radioButton7.setVisibility(View.INVISIBLE);
                break;
            case 2:
                radioButton1.setText(res.getString(R.string.survey3button1));
                radioButton5.setText(res.getString(R.string.survey3button5));
                radioButton6.setVisibility(View.INVISIBLE);
                radioButton7.setVisibility(View.INVISIBLE);
                break;
            case 3:
                radioButton1.setText(res.getString(R.string.survey4button1));
                radioButton2.setText(res.getString(R.string.survey4button2));
                radioButton3.setVisibility(View.INVISIBLE);
                radioButton4.setVisibility(View.INVISIBLE);
                radioButton5.setVisibility(View.INVISIBLE);
                radioButton6.setVisibility(View.INVISIBLE);
                radioButton7.setVisibility(View.INVISIBLE);
                break;
            case 4:
                decicion.setVisibility(View.GONE);
                minutePicker.setVisibility(View.VISIBLE);
                minutePicker.setEnabled(true);
                String[] values=new String[20];
                for(int i=0; i<values.length; i++) {
                    values[i] = Integer.toString(i * 10) + "min";
                }
                minutePicker.setMaxValue(values.length - 1);
                minutePicker.setMinValue(0);
                minutePicker.setDisplayedValues(values);
                next.setVisibility(View.VISIBLE);
                break;
            case 5:
                question.setText(res.getString(R.string.survey6title));
                radioButton1.setText("0");
                radioButton2.setText("1");
                radioButton3.setText("2");
                radioButton4.setText("3");
                radioButton5.setText("4");
                radioButton6.setText("5");
                radioButton7.setText("6");
        }
        newQuestion();
    }

    private void newQuestion() {
        switch (surveyNumber) {
            case 0:
                if (questionNumber == 0) {
                    answer = new int[question1.length];
                }
                if ((0 < questionNumber) && (questionNumber < question1.length + 1)) {
                    answer[questionNumber -1] = currentValue;
                }
                if (questionNumber > answer.length - 1) {
                    saveAndDissmiss();
                    break;
                } else {
                    question.setText(question1[questionNumber]);
                    break;
                }
            case 1:
                if (questionNumber == 0) {
                    answer = new int[question2.length];
                }
                if ((0 < questionNumber) && (questionNumber < question2.length + 1)) {
                    answer[questionNumber -1] = currentValue;
                }
                if (questionNumber > answer.length -1) {
                    saveAndDissmiss();
                    break;
                } else {
                    question.setText(question2[questionNumber]);
                    break;
                }
            case 2:
                if (questionNumber == 0) {
                    answer = new int[question3.length];
                }
                if ((0 < questionNumber) && (questionNumber < question3.length + 1)) {
                    answer[questionNumber -1] = currentValue;
                }
                if (questionNumber > answer.length -1) {
                    saveAndDissmiss();
                    break;
                } else {
                    question.setText(question3[questionNumber]);
                    break;
                }
            case 3:
                if (questionNumber == 0) {
                    answer = new int[question4.length];
                }
                if (questionNumber == 2 && answer[0] == 1) {
                    decicion.check(0);
                }
                if (questionNumber == 3 && answer[1] == 1) {
                    decicion.check(0);
                }
                if ((0 < questionNumber) && (questionNumber < question4.length + 1)) {
                    answer[questionNumber -1] = currentValue;
                }
                if (questionNumber > answer.length -1) {
                    saveAndDissmiss();
                    break;
                } else {
                    question.setText(question4[questionNumber]);
                    break;
                }
            case 4:
                if (questionNumber == 0) {
                    answer = new int[question5.length];
                }
                if ((0 < questionNumber) && (questionNumber < question5.length + 1)) {
                    answer[questionNumber -1] = currentValue;
                    minutePicker.setValue(0);
                }
                if (questionNumber > answer.length -1) {
                    saveAndDissmiss();
                    break;
                } else {
                    question.setText(question5[questionNumber]);
                    break;
                }
            case 5:
                if (questionNumber == 0) {
                    answer = new int[question6Right.length];
                }

                if ((0 < questionNumber) && (questionNumber < question6Right.length + 1)) {
                    answer[questionNumber -1] = currentValue - 1;
                }

                if (questionNumber > answer.length -1) {
                    saveAndDissmiss();
                    break;
                } else {
                    radioButton7.setText("6 (" + question6Right[questionNumber] + ")");
                    radioButton1.setText("0 (" + question6Left[questionNumber] + ")");
                    break;
                }
        }
        decicion.setOnCheckedChangeListener(null);
        decicion.clearCheck();
        decicion.setOnCheckedChangeListener(listener);
        questionNumber++;
    }

    private void saveAndDissmiss() {
        String id = prefs.getString("ID", "");
        String date;
        String time;
        Calendar c = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy", Locale.getDefault());
        date = sdf.format(c.getTime());
        SimpleDateFormat sdf2 = new SimpleDateFormat("HH:mm:ss", Locale.getDefault());
        time = sdf2.format(c.getTime());

        SurveyItem  surveyItem = new SurveyItem("survey", id, date, time, surveyNumber + 1,
               answer);
        JSONObject key;
        try {
            key = SurveyItemToJson.getJSONfromSurvey(surveyItem);
            Intent intent = new Intent(Survey.this, Connection.class);
            intent.putExtra("json", key.toString());
            startActivity(intent);
            if (surveyNumber < 5) {
                Intent intent1 = new Intent(Survey.this, Survey.class);
                intent1.putExtra("survey", surveyNumber + 1);
                intent1.putExtra("time", this.time);
                startActivity(intent1);
            }
            finish();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
