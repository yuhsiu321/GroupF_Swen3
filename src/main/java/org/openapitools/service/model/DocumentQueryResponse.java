package org.openapitools.service.model;

import com.fasterxml.jackson.annotation.JsonTypeName;
import org.openapitools.persistence.entities.DocumentEntity;

import javax.annotation.Generated;
import java.util.List;

@JsonTypeName("CreateUISettings_request_settings_document_details")
@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", date = "2023-10-10T06:36:40.060738Z[Etc/UTC]")
public class DocumentQueryResponse {

    private Integer next;
    private Object[] all;
    private Integer previous;
    private Integer count;

    private List<DocumentEntity> results;

    public List<DocumentEntity> getResults() {
        return results;
    }

    public void setResults(List<DocumentEntity> results) {
        this.results = results;
    }

    public Integer getNext() {
        return next;
    }

    public void setNext(Integer next) {
        this.next = next;
    }

    public Object[] getAll() {
        return all;
    }

    public void setAll(Object[] all) {
        this.all = all;
    }

    public Integer getPrevious() {
        return previous;
    }

    public void setPrevious(Integer previous) {
        this.previous = previous;
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }
}
