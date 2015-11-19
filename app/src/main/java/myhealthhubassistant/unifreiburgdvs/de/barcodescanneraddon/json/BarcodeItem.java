package myhealthhubassistant.unifreiburgdvs.de.barcodescanneraddon.json;

/**
 * Created by lukas on 06.08.15.
 */

public class BarcodeItem {
    private String kind;
    private String id;
    private String title;
    private String barcode;
    private String obj_date;
    private String obj_time;
    private String ssb;
    private String amount;
    private String lng;
    private String lat;
    private String barcodeName;

    public BarcodeItem(String kind, String id, String obj_date, String obj_time, String title,
                       String amount, String barcode, String barcodeName, String ssb, String lng, String lat
    ) {
        this.kind = kind;
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

    public String getKind() {
        return kind;
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
