package dynobjx.com.jrs.dao;

/**
 * Created by jobellebanez on 7/31/15.
 */
public class International {

    private Long id;
    private Integer internationalId;
    private String internationalName;

    public String getInternationalName() {
        return internationalName;
    }

    public void setInternationalName(String internationalName) {
        this.internationalName = internationalName;
    }

    public International() {
    }

    public International(Long id) {
        this.id = id;
    }

    public International(Long id, Integer internationalId, String internationalName) {
        this.id = id;
        this.internationalId = internationalId;
        this.internationalName = internationalName;
    }



}
