package net.search.solr.services;

import java.io.IOException;
import java.net.MalformedURLException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Random;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;

import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.HttpSolrClient;

import net.search.solr.db.DBAccessor;
import net.search.solr.model.Product;

@Path("/solrAddApi")
public class SolrSearchAddAPI {
    @GET
    @Produces("application/xml")
    public String solrApi() {
        writeJavaBinToSolr(10);
        return "<Results>OK</Results>";
    }

    @Path("{num}")
    @GET
    @Produces("application/xml")
    public String solrApi(@PathParam("num") int num) {
        writeJavaBinToSolr(num);
        return "<Results> <Updated></Updated></Results>";
    }

    private void writeJavaBinToSolr(int num) {
        try {
            ResultSet rs = getResultSets(num);
             DBAccessor accessor = new DBAccessor();
             accessor.addResultSet(rs);
//            addResultSetAsBean(rs);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (SolrServerException e) {
            e.printStackTrace();
        }
    }

    private void addResultSetAsBean(ResultSet rs)
        throws SolrServerException, IOException, SQLException {

        HttpSolrClient solrCore =
                new HttpSolrClient("http://nadellas.sandbox.gspt.net:7070/solr/products");

        solrCore.deleteByQuery("*:*");

        Product prod = new Product();

        prod.setId(randomId());
        ArrayList<Product> prodList = new ArrayList<Product>();
        while (rs.next()) {
            
            prod.setCATALOG_ENTRY_ID(rs.getString("catalog_entry_id"));
            prod.setENTRY_TYPE(rs.getString("entry_type"));
            prod.setLONG_DESCRIPTION(rs.getString("long_description"));
            prod.setSHORT_DESCRIPTION(rs.getString("short_description"));
            prod.setTITLE(rs.getString("title"));
            System.out.println("Commiting Catalog_Entry_Id : " + prod.getCATALOG_ENTRY_ID());
            solrCore.addBean(prod);
            solrCore.commit();
        }
        
//        solrCore.addBeans(prodList);
        
//        solrCore.commit();
        
        System.out.println("Done Commiting to Core : " + prodList.size() + " : Docs");
    }

    private String randomId() {
        Random randomGenerator = new Random();
        int randomInt = randomGenerator.nextInt(100);
        Integer id = new Integer(randomInt);
        return id.toString();
    }

    private ResultSet getResultSets(int num) {
        Statement stmt = null;
        String query =
                "select ce.catalog_entry_id, ce.entry_type, ce.title " +
                        " from catalog_entry ce, store_product sp " +
                        " where sp.store_code='TRUS' and sp.sp_status like 'A%' and sp.product_id = ce.catalog_entry_id " +
                        "and ce.status like 'A%' and" +
                        " ce.entry_type = 'P' and rownum < " + num;
        Connection connection = null;
        ResultSet rs = null;
        try {
            Class.forName("oracle.jdbc.driver.OracleDriver");
            connection = DriverManager.getConnection(
                    "jdbc:oracle:thin:@lvsdtumerdb01a.gspt.net:1521:mercaldv1",
                    "NADELLAS", "cOmmerce1");

            stmt = connection.createStatement();
            rs = stmt.executeQuery(query);
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        return rs;
    }

}
