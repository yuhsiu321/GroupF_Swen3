package org.openapitools.service.model;

import com.fasterxml.jackson.annotation.JsonTypeName;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import org.springframework.web.bind.annotation.RequestParam;

import javax.annotation.Generated;
import javax.validation.Valid;
import java.util.List;

@JsonTypeName("CreateUISettings_request_settings_document_details")
@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", date = "2023-10-10T06:36:40.060738Z[Etc/UTC]")
public class DocumentPageQuertReq {

    private Integer page;
    private Integer pageSize;
    private String query;
    private String ordering;
    private List<Integer> tagsIdAll;
    private Integer documentTypeId;
    private Integer storagePathIdIn;
    private Integer correspondentId;

    private Boolean truncateContent;


    public Integer getPage() {
        return page;
    }

    public void setPage(Integer page) {
        this.page = page;
    }

    public Integer getPageSize() {
        return pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }

    public String getQuery() {
        return query;
    }

    public void setQuery(String query) {
        this.query = query;
    }

    public String getOrdering() {
        return ordering;
    }

    public void setOrdering(String ordering) {
        this.ordering = ordering;
    }

    public List<Integer> getTagsIdAll() {
        return tagsIdAll;
    }

    public void setTagsIdAll(List<Integer> tagsIdAll) {
        this.tagsIdAll = tagsIdAll;
    }

    public Integer getDocumentTypeId() {
        return documentTypeId;
    }

    public void setDocumentTypeId(Integer documentTypeId) {
        this.documentTypeId = documentTypeId;
    }

    public Integer getStoragePathIdIn() {
        return storagePathIdIn;
    }

    public void setStoragePathIdIn(Integer storagePathIdIn) {
        this.storagePathIdIn = storagePathIdIn;
    }

    public Integer getCorrespondentId() {
        return correspondentId;
    }

    public void setCorrespondentId(Integer correspondentId) {
        this.correspondentId = correspondentId;
    }

    public Boolean getTruncateContent() {
        return truncateContent;
    }

    public void setTruncateContent(Boolean truncateContent) {
        this.truncateContent = truncateContent;
    }
}
