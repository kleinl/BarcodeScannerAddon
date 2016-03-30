package myhealthhubassistant.unifreiburgdvs.de.barcodescanneraddon.json;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by lukas on 10.11.15.
 * Class to convert the Barcode Item to a JSON Object for the myHealthHub Database.
 */
public class BarcodeItemToJson {
    public static JSONObject getJSONfromBarcode(BarcodeItem barcodeItem) throws JSONException {
        JSONObject jsonObj = new JSONObject();
        jsonObj.putOpt("KIND", "item");
        jsonObj.putOpt("USER_ID", barcodeItem.getId());
        jsonObj.putOpt("NAME", barcodeItem.getTitle());
        jsonObj.putOpt("BARCODE", barcodeItem.getPop_content());
        jsonObj.putOpt("DATE", barcodeItem.getObj_date());
        jsonObj.putOpt("TIME", barcodeItem.getObj_time());
        jsonObj.putOpt("LONGITUDE", barcodeItem.getLng());
        jsonObj.putOpt("LATITUDE", barcodeItem.getLat());
        jsonObj.putOpt("AMOUNT", barcodeItem.getAmount());
        jsonObj.putOpt("SSB", barcodeItem.getSsb());
        jsonObj.putOpt("BARCODENAME", barcodeItem.getBarcodeName());
        return jsonObj;
    }
}
