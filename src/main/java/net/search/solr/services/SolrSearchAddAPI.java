package net.search.solr.services;

import java.io.IOException;
import java.net.MalformedURLException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Iterator;
import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;

import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.HttpSolrServer;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocumentList;

import net.search.solr.db.DBAccessor;
import net.search.solr.model.Product;

@Path("/solrAddApi")
public class SolrSearchAddAPI {
    @GET
    @Produces("application/xml")
    public String solrApi() {
        writeJavaBinToSolr(10);

        StringBuffer response = buildResponse();
        return response.toString();
    }
    @Path("{num}")
    @GET
    @Produces("application/xml")
    public String solrApi(@PathParam("num") int num) {
        writeJavaBinToSolr(num);

        StringBuffer response = buildResponse();
        return response.toString();
    }

    private void writeJavaBinToSolr(int num) {
        try {
            ResultSet rs = getResultSets(num);
            DBAccessor accessor = new DBAccessor();
            accessor.addResultSet(rs);
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


    private StringBuffer buildResponse() {
        List<Product> products;
        StringBuffer response = new StringBuffer();
        response.append("<solrAddApi>"); 
        try {
            products = querySimpleSolr();
            Iterator itr = products.iterator();
            while (itr.hasNext()) {
                Product product = (Product)itr.next();
                response.append("<cat_id>" + product.getCatalog_entry_id() + "</cat_id>" );
                response.append("<title>" + product.getTitle() + "</title>" );
                response.append("<entry_type>" + product.getEntry_type() + "</entry_type>" );
                response.append("<shortDescription>" + product.getShort_description() + "</shortDescription>" );
                response.append("<longDescription>" + product.getLong_description() + "</longDescription>" );
            }
            response.append("</solrAddApi>"); 
        } catch (SolrServerException | IOException e) {
            e.printStackTrace();
        }
        return response;
    }

    private static List<Product> querySimpleSolr() throws SolrServerException, IOException {
        // TODO Auto-generated method stub
        HttpSolrServer server =
                new HttpSolrServer("http://nadellas.sandbox.gspt.net:7070/solr/products");

        SolrQuery query = new SolrQuery();
        query.setQuery("*:*");
        query.addSort("CATALOG_ENTRY_ID", SolrQuery.ORDER.asc);

        QueryResponse rsp = server.query(query);

        SolrDocumentList docs = rsp.getResults();
        System.out.println("Docs : " + docs);

        List<Product> beans = rsp.getBeans(Product.class);
        
        System.out.println("Beans :  " + beans);

        return beans;
    }

    private ResultSet getResultSets(int num) {
        Statement stmt = null;
        String query =
                "select ce.catalog_entry_id, ce.entry_type, ce.short_description, ce.long_description,ce.title " +
                        " from catalog_entry ce, store_product sp " +
                        " where sp.store_code='TRUS' and sp.sp_status like 'A%' and sp.product_id = ce.catalog_entry_id and ce.status like 'A%' and" +
                        " rownum < " + num;
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
