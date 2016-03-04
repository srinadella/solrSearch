package net.search.solr.services;


import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;

@Path("/solrAddApi")
public class SolrSearchAddAPI {
    @GET
    @Produces("application/xml")
    public String solrApi() {
        Class.forName("oracle.jdbc.driver.OracleDriver");
        Statement stmt = null;
        String query = "select COF_NAME, SUP_ID, PRICE, " +
                       "SALES, TOTAL " +
                       "from " + "dbName" + ".COFFEES";
        Connection connection = null;
        
        connection = DriverManager.getConnection(
                "jdbc:oracle:thin:@localhost:1521:mkyong", "username",
                "password");
        stmt = connection.createStatement();
        ResultSet rs = stmt.executeQuery(query);
        
        return "<solrAddApi> <fahrenheit></fahrenheit><ftocoutput> </ftocoutput></solrAddApi>";
    }

    @Path("{f}")
    @GET
    @Produces("application/xml")
    public String solrApi(@PathParam("f") Double f) {
        return "<solrAddApi> <fahrenheit></fahrenheit><ftocoutput> </ftocoutput></solrAddApi>";
    }
}