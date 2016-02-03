package myhealthhubassistant.unifreiburgdvs.de.barcodescanneraddon;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import de.tudarmstadt.dvs.myhealthassistant.myhealthhub.events.AbstractChannel;
import de.tudarmstadt.dvs.myhealthassistant.myhealthhub.events.Event;
import de.tudarmstadt.dvs.myhealthassistant.myhealthhub.events.management.JSONDataExchange;

public class CommBroadcastReceiver {
    private static final String TAG = CommBroadcastReceiver.class
            .getSimpleName();
    private int evtCounter = 0;
    private final Context context;

    public CommBroadcastReceiver(Context context) {
        this.context = context;
    }

    /**
     * Store encoded JSONObject in database by sending JSONObject to myHealthHub
     * with STORE-Request
     */
    public void storeEntry(JSONObject jObj) {
        messageContrll(JSONDataExchange.JSON_STORE, jObj);
    }

    private void messageContrll(String jsonRequest, JSONObject jObj) {
        Log.e(TAG, "broadcast request: " + jsonRequest);

        evtCounter++;
        JSONArray jObjArray = new JSONArray();
        JSONObject jEncodedData = new JSONObject();
        try {
            jObjArray.put(jObj);
            // request to store encoded data as a json Array to db
            jEncodedData.putOpt(JSONDataExchange.JSON_REQUEST, jsonRequest);
            jEncodedData
                    .putOpt("jArray", jObjArray); // fixme

            JSONDataExchange eData = new JSONDataExchange(TAG + evtCounter,
                    getTimestamp(), TAG, context.getPackageName(),
                    JSONDataExchange.EVENT_TYPE, jEncodedData.toString());

            // Publishes a management event to myHealthHub
            publishEvent(eData, AbstractChannel.MANAGEMENT);

            Toast.makeText(context, "save successful!", Toast.LENGTH_SHORT)
                    .show();

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * Publishes an event on a specific myHealthHub channel.
     *
     * @param event
     *            that shall be published.
     * @param channel
     *            on which the event shall be published.
     */
    private void publishEvent(Event event, String channel) {
        Intent i = new Intent();
        // add event
        i.putExtra(Event.PARCELABLE_EXTRA_EVENT_TYPE, event.getEventType());
        i.putExtra(Event.PARCELABLE_EXTRA_EVENT, event);

        // set channel as Management
        i.setAction(channel);

        // set receiver package
        i.setPackage("de.tudarmstadt.dvs.myhealthassistant.myhealthhub");

        // sent intent
        context.sendBroadcast(i);
    }

    /**
     * Returns the current time
     *
     * @return timestamp
     */
    private String getTimestamp() {
        return (String) android.text.format.DateFormat.format(
                "yyyy-MM-dd\nkk:mm:ss", new java.util.Date());
    }
}
