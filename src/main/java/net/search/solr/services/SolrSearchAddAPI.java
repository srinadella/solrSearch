package net.search.solr.services;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;

import org.apache.solr.client.solrj.SolrServerException;

import net.search.solr.db.DBAccessor;

@Path("/solrAddApi")
public class SolrSearchAddAPI {
	@GET
	@Produces("application/xml")
	public String solrApi() {
		Statement stmt = null;
		String query = "select ce.catalog_entry_id, ce.entry_type, ce.short_description, ce.long_description,ce.title "
				+ " from catalog_entry ce, store_product sp "
				+ " where sp.store_code='TRUS' and sp.sp_status like 'A%' and sp.product_id = ce.catalog_entry_id and ce.status like 'A%' and"
				+ " rownum < 10";
		Connection connection = null;
		ResultSet rs = null;
		try {
			Class.forName("oracle.jdbc.driver.OracleDriver");
			connection = DriverManager.getConnection(
					"jdbc:oracle:thin:@lvsdtumerdb01a.gspt.net:1521:mercaldv1",
					"NADELLAS", "cOmmerce1");

			stmt = connection.createStatement();
			rs = stmt.executeQuery(query);

	        if (rs != null) {
	            try {
	                DBAccessor accessor = new DBAccessor();
	                accessor.addResultSet(rs);
	            } catch (SQLException e) {
	                // TODO Auto-generated catch block
	                e.printStackTrace();
	            } catch (SolrServerException e) {
	                // TODO Auto-generated catch block
	                e.printStackTrace();
	            } catch (IOException e) {
	                // TODO Auto-generated catch block
	                e.printStackTrace();
	            }
	        }
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} finally {
			if (connection != null) {
				try {
					connection.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
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