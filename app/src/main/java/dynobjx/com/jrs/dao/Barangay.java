package dynobjx.com.jrs.dao;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT. Enable "keep" sections if you want to edit. 
/**
 * Entity mapped to table BARANGAY.
 */
public class Barangay {

    private Long id;
    private Integer brgyId;
    private String brgyName;
    private Integer provinceId;
    private Integer municipalityId;

    public Barangay() {
    }

    public Barangay(Long id) {
        this.id = id;
    }

    public Barangay(Long id, Integer brgyId, String brgyName, Integer provinceId, Integer municipalityId) {
        this.id = id;
        this.brgyId = brgyId;
        this.brgyName = brgyName;
        this.provinceId = provinceId;
        this.municipalityId = municipalityId;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getBrgyId() {
        return brgyId;
    }

    public void setBrgyId(Integer brgyId) {
        this.brgyId = brgyId;
    }

    public String getBrgyName() {
        return brgyName;
    }

    public void setBrgyName(String brgyName) {
        this.brgyName = brgyName;
    }

    public Integer getProvinceId() {
        return provinceId;
    }

    public void setProvinceId(Integer provinceId) {
        this.provinceId = provinceId;
    }

    public Integer getMunicipalityId() {
        return municipalityId;
    }

    public void setMunicipalityId(Integer municipalityId) {
        this.municipalityId = municipalityId;
    }

}
