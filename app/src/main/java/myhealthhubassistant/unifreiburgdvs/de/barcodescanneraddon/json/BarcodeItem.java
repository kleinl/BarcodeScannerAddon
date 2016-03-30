package myhealthhubassistant.unifreiburgdvs.de.barcodescanneraddon.json;

/**
 * Created by lukas on 06.08.15.
 * Class to hold Barcode Information.
 */

public class BarcodeItem {
    private final String id;
    private final String title;
    private final String barcode;
    private final String obj_date;
    private final String obj_time;
    private final String ssb;
    private final String amount;
    private final String lng;
    private final String lat;
    private final String barcodeName;

    public BarcodeItem(String id, String obj_date, String obj_time, String title,
                       String amount, String barcode, String barcodeName, String ssb, String lng, String lat
    ) {
        this.id = id;
        this.title = title;
        this.barcode = barcode;
        this.obj_date = obj_date;
        this.obj_time = obj_time;
        this.amount = amount;
        this.ssb = ssb;
        this.lng = lng;
        this.lat = lat;
        this.barcodeName = barcodeName;
    }

    public String getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getPop_content() {
        return barcode;
    }

    public String getObj_date() {
        return obj_date;
    }

    public String getObj_time() {
        return obj_time;
    }

    public String getAmount() {
        return amount;
    }

    public String getSsb() {
        return ssb;
    }

    public String getLng() {
        return lng;
    }

    public String getLat() {
        return lat;
    }

    public String getBarcodeName() {
        return barcodeName;
    }
}
