package net.search.solr.model;

import org.apache.solr.client.solrj.beans.Field;

public class Product {

    @Field
    String id;
    @Field
    String CATALOG_ENTRY_ID;
    @Field
    String ENTRY_TYPE;
    @Field
    String SHORT_DESCRIPTION;
    @Field
    String LONG_DESCRIPTION;
    @Field
    String TITLE;
    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }
    public String getCATALOG_ENTRY_ID() {
        return CATALOG_ENTRY_ID;
    }
    public void setCATALOG_ENTRY_ID(String cATALOG_ENTRY_ID) {
        CATALOG_ENTRY_ID = cATALOG_ENTRY_ID;
    }
    public String getENTRY_TYPE() {
        return ENTRY_TYPE;
    }
    public void setENTRY_TYPE(String eNTRY_TYPE) {
        ENTRY_TYPE = eNTRY_TYPE;
    }
    public String getSHORT_DESCRIPTION() {
        return SHORT_DESCRIPTION;
    }
    public void setSHORT_DESCRIPTION(String sHORT_DESCRIPTION) {
        SHORT_DESCRIPTION = sHORT_DESCRIPTION;
    }
    public String getLONG_DESCRIPTION() {
        return LONG_DESCRIPTION;
    }
    public void setLONG_DESCRIPTION(String lONG_DESCRIPTION) {
        LONG_DESCRIPTION = lONG_DESCRIPTION;
    }
    public String getTITLE() {
        return TITLE;
    }
    public void setTITLE(String tITLE) {
        TITLE = tITLE;
    }
}
