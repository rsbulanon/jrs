package dynobjx.com.jrs.dao;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT. Enable "keep" sections if you want to edit. 
/**
 * Entity mapped to table SERVICE_DEFINITION.
 */
public class ServiceDefinition {

    private Long id;
    private Integer definitionId;

    public ServiceDefinition() {
    }

    public ServiceDefinition(Long id) {
        this.id = id;
    }

    public ServiceDefinition(Long id, Integer definitionId) {
        this.id = id;
        this.definitionId = definitionId;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getDefinitionId() {
        return definitionId;
    }

    public void setDefinitionId(Integer definitionId) {
        this.definitionId = definitionId;
    }

}
