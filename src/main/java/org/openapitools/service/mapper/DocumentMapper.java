package org.openapitools.service.mapper;
import org.mapstruct.*;
import org.mapstruct.factory.Mappers;
import org.openapitools.jackson.nullable.JsonNullable;
import org.openapitools.persistence.entities.DocumentEntity;
import org.openapitools.service.model.Document;

import java.util.List;

@Mapper
public interface DocumentMapper{

    DocumentMapper INSTANCE = Mappers.getMapper(DocumentMapper.class);
    /*@Mapping(target = "correspondent", source = "correspondent", qualifiedByName = "wrapInteger")
    @Mapping(target = "documentType", source = "documentType", qualifiedByName = "wrapInteger")
    @Mapping(target = "storagePath", source = "storagePath", qualifiedByName = "wrapInteger")
    @Mapping(target = "title", source = "title", qualifiedByName = "wrapString")
    @Mapping(target = "content", source = "content", qualifiedByName = "wrapString")
    @Mapping(target = "created", source = "created")
    @Mapping(target = "createdDate", source = "createdDate")
    @Mapping(target = "modified", source = "modified")
    @Mapping(target = "added", source = "added")
    @Mapping(target = "archiveSerialNumber", source = "archiveSerialNumber", qualifiedByName = "wrapString")
    @Mapping(target = "originalFileName", source = "originalFileName", qualifiedByName = "wrapString")
    @Mapping(target = "archivedFileName", source = "archivedFileName", qualifiedByName = "wrapString")
    DocumentEntity dtoToEntity(Document dto);

    @Mapping(target = "correspondent", source = "correspondent", qualifiedByName = "unwrapInteger")
    @Mapping(target = "documentType", source = "documentType", qualifiedByName = "unwrapInteger")
    @Mapping(target = "storagePath", source = "storagePath", qualifiedByName = "unwrapInteger")
    @Mapping(target = "title", source = "title", qualifiedByName = "unwrapString")
    @Mapping(target = "content", source = "content", qualifiedByName = "unwrapString")
    @Mapping(target = "created", source = "created")
    @Mapping(target = "createdDate", source = "createdDate")
    @Mapping(target = "modified", source = "modified")
    @Mapping(target = "added", source = "added")
    @Mapping(target = "archiveSerialNumber", source = "archiveSerialNumber", qualifiedByName = "unwrapString")
    @Mapping(target = "originalFileName", source = "originalFileName", qualifiedByName = "unwrapString")
    @Mapping(target = "archivedFileName", source = "archivedFileName", qualifiedByName = "unwrapString")
    Document entityToDto(DocumentEntity entity);


    @Named("wrapInteger")
    default JsonNullable<Integer> integerToJsonNullable(Integer value) {return JsonNullable.of(value);}
    @Named("wrapString")
    default JsonNullable<String> stringToJsonNullable(String value) {return JsonNullable.of(value);}

    @Named("wrapList")
    default JsonNullable <List<Integer>> listToJsonNullable(List<Integer> value) {return JsonNullable.of(value);}

    @Named("unwrapInteger")
    default Integer jsonNullableToInteger(JsonNullable<Integer> value){return value.orElse(null);}
    @Named("unwrapString")
    default String jsonNullableToString(JsonNullable<String> value){return value.orElse(null);}

    @Named("unwrapList")
    default List<Integer> jsonNullableToList(JsonNullable<List<Integer>> value){return value.orElse(null);}
    */
    Document toDto(DocumentEntity entity);
    DocumentEntity toEntity(Document dto);

    default JsonNullable<Integer> integerToJsonNullable(Integer value){return JsonNullable.of(value);}
    default JsonNullable<String> stringToJsonNullable(String value){return JsonNullable.of(value);}

    default JsonNullable<List<Integer>> listToJsonNullable(List<Integer> value){return JsonNullable.of(value);}

    default Integer jsonNullableToInteger(JsonNullable<Integer> value){return value.orElse(null);}
    default String jsonNullableToString(JsonNullable<String> value){return value.orElse(null);}

    default List<Integer> jsonNullableToList(JsonNullable<List<Integer>> value){return value.orElse(null);}



}
