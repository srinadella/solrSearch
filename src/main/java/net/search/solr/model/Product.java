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

    public Product() {

    }

    public Product(String id, String Cat_id, String ent_type, String short_desc, String long_desc,
            String title) {
        this.id = id;
        this.CATALOG_ENTRY_ID = Cat_id;
        this.ENTRY_TYPE = ent_type;
        this.SHORT_DESCRIPTION = short_desc;
        this.LONG_DESCRIPTION = long_desc;
        this.TITLE = title;
    }

    public String getId() {
        return this.id;
    }

    @Field("id")
    public void setId(String id) {
        this.id = id;
    }

    public String getCATALOG_ENTRY_ID() {
        return this.CATALOG_ENTRY_ID;
    }

    @Field("CATALOG_ENTRY_ID")
    public void setCATALOG_ENTRY_ID(String CATALOG_ENTRY_ID) {
        this.CATALOG_ENTRY_ID = CATALOG_ENTRY_ID;
    }

    public String getENTRY_TYPE() {
        return this.ENTRY_TYPE;
    }

    @Field ("ENTRY_TYPE")
    public void setENTRY_TYPE(String ENTRY_TYPE) {
        this.ENTRY_TYPE = ENTRY_TYPE;
    }

    public String getSHORT_DESCRIPTION() {
        return this.SHORT_DESCRIPTION;
    }

    @Field ("SHORT_DESCRIPTION")
    public void setSHORT_DESCRIPTION(String SHORT_DESCRIPTION) {
        this.SHORT_DESCRIPTION = SHORT_DESCRIPTION;
    }

    public String getLONG_DESCRIPTION() {
        return this.LONG_DESCRIPTION;
    }

    @Field ("LONG_DESCRIPTION")
    public void setLONG_DESCRIPTION(String LONG_DESCRIPTION) {
        this.LONG_DESCRIPTION = LONG_DESCRIPTION;
    }

    public String getTITLE() {
        return this.TITLE;
    }

    @Field ("TITLE")
    public void setTITLE(String TITLE) {
        this.TITLE = TITLE;
    }
}
