package dynobjx.com.jrs.dao;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT. Enable "keep" sections if you want to edit. 
/**
 * Entity mapped to table DESTINATION.
 */
public class Destination {

    private Long id;
    private Integer destinationId;
    private String destinationName;

    public Destination() {
    }

    public Destination(Long id) {
        this.id = id;
    }

    public Destination(Long id, Integer destinationId, String destinationName) {
        this.id = id;
        this.destinationId = destinationId;
        this.destinationName = destinationName;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getDestinationId() {
        return destinationId;
    }

    public void setDestinationId(Integer destinationId) {
        this.destinationId = destinationId;
    }

    public String getDestinationName() {
        return destinationName;
    }

    public void setDestinationName(String destinationName) {
        this.destinationName = destinationName;
    }

}
