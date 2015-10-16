package dynobjx.com.jrs.models;

import java.util.Date;

/**
 * Created by rsbulanon on 6/13/15.
 */
public class FacebookFeeds {

    private String postedBy;
    private String picURL;
    private String message;
    private String datePosted;

    public FacebookFeeds(String postedBy, String picURL, String message, String datePosted) {
        this.postedBy = postedBy;
        this.picURL = "https://graph.facebook.com/"+picURL+"/picture?width=50&height=50";
        this.message = message;
        this.datePosted = datePosted;
    }

    public String getPostedBy() {
        return postedBy;
    }

    public void setPostedBy(String from) {
        this.postedBy = postedBy;
    }

    public String getPicURL() {
        return picURL;
    }

    public void setPicURL(String picURL) {
        this.picURL = picURL;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getDatePosted() {
        return datePosted;
    }

    public void setDatePosted(String datePosted) {
        this.datePosted = datePosted;
    }
}
