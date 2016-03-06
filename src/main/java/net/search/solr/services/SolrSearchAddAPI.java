package net.search.solr.services;

import java.io.IOException;
import java.net.MalformedURLException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import net.search.solr.db.DBAccessor;

import org.apache.solr.client.solrj.SolrServerException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

@Path("/solrAddApi")
public class SolrSearchAddAPI {
	@GET
	@Produces("application/xml")
	public String solrApi() {
//		writeJavaBinToSolr();

		writeXMLToSolr();
		return "<solrAddApi> <fahrenheit></fahrenheit><ftocoutput> </ftocoutput></solrAddApi>";
	}

	private void writeXMLToSolr() {

		ResultSet rs =  getResultSets();
		try {
			Document doc = toDocument(rs);
			System.out.println(doc);
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	private void writeJavaBinToSolr() {
		
		ResultSet rs =  getResultSets();
		try {
			DBAccessor accessor = new DBAccessor();
			//accessor.addResultSet(rs);
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Path("{f}")
	@GET
	@Produces("application/xml")
	public String solrApi(@PathParam("f") Double f) {
		return "<solrAddApi> <fahrenheit></fahrenheit><ftocoutput> </ftocoutput></solrAddApi>";
	}

	private ResultSet getResultSets() {
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
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		
		return rs;
	}

	public static Document toDocument(ResultSet rs)
			throws ParserConfigurationException, SQLException {
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		DocumentBuilder builder = factory.newDocumentBuilder();
		Document doc = builder.newDocument();

		Element results = doc.createElement("Results");
		doc.appendChild(results);

		ResultSetMetaData rsmd = rs.getMetaData();
		int colCount = rsmd.getColumnCount();

		while (rs.next()) {
			Element row = doc.createElement("Row");
			results.appendChild(row);

			for (int i = 1; i <= colCount; i++) {
				String columnName = rsmd.getColumnName(i);
				Object value = rs.getObject(i);
				
				if(value != null ) {
					System.out.println(" ColumnName : " + columnName + " Value : " + value );
					Element node = doc.createElement(columnName);
					node.appendChild(doc.createTextNode(value.toString()));
					row.appendChild(node);
				}
			}
		}
		return doc;
	}
}
