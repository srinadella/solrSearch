package net.search.solr.model;

import java.util.List;

import org.apache.solr.client.solrj.beans.Field;

public class Item {
    @Field
    String id;

    @Field("cat")
    String[] categories;

    @Field
    List<String> features;

    /**
     * @return the id
     */
    
    public String getId() {
        return id;
    }

    /**
     * @param id the id to set
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * @return the categories
     */
    public String[] getCategories() {
        return categories;
    }

    /**
     * @param categories the categories to set
     */

    @Field("cat")
    public void setCategories(String[] categories) {
        this.categories = categories;
    }

    /**
     * @return the features
     */
    public List<String> getFeatures() {
        return features;
    }

    /**
     * @param features the features to set
     */
    public void setFeatures(List<String> features) {
        this.features = features;
    }

}
