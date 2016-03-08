package net.search.solr.model;

import org.apache.solr.client.solrj.beans.Field;

public class Product {
    @Field
    String catalog_entry_id;
    @Field
    String entry_type;
    @Field
    String short_description;
    @Field
    String long_description;
    @Field
    String title;
    public String getCatalog_entry_id() {
        return catalog_entry_id;
    }
    public void setCatalog_entry_id(String catalog_entry_id) {
        this.catalog_entry_id = catalog_entry_id;
    }
    public String getEntry_type() {
        return entry_type;
    }
    public void setEntry_type(String entry_type) {
        this.entry_type = entry_type;
    }
    public String getShort_description() {
        return short_description;
    }
    public void setShort_description(String short_description) {
        this.short_description = short_description;
    }
    public String getLong_description() {
        return long_description;
    }
    public void setLong_description(String long_description) {
        this.long_description = long_description;
    }
    public String getTitle() {
        return title;
    }
    public void setTitle(String title) {
        this.title = title;
    }
}
