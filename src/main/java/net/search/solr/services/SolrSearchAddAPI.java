package net.search.solr.services;


import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
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
        Statement stmt = null;
        String query = "select COF_NAME, SUP_ID, PRICE, " +
                       "SALES, TOTAL " +
                       "from " + "dbName" + ".COFFEES";
        Connection connection = null;
        
        try {
            Class.forName("oracle.jdbc.driver.OracleDriver");
            connection = DriverManager.getConnection(
                    "jdbc:oracle:thin:@localhost:1521:mkyong", "username",
                    "password");

            stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery(query);
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        
        return "<solrAddApi> <fahrenheit></fahrenheit><ftocoutput> </ftocoutput></solrAddApi>";
    }

    @Path("{f}")
    @GET
    @Produces("application/xml")
    public String solrApi(@PathParam("f") Double f) {
        return "<solrAddApi> <fahrenheit></fahrenheit><ftocoutput> </ftocoutput></solrAddApi>";
    }
}