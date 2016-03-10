package net.search.solr.services;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Iterator;
import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;

import org.apache.http.NameValuePair;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.HttpSolrClient;
import org.apache.solr.client.solrj.impl.HttpSolrServer;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocumentList;
import org.apache.solr.common.params.ModifiableSolrParams;
import org.apache.solr.common.util.NamedList;

import net.search.solr.model.Product;

@Path("/query")
public class SolrSearchAPI {
    @GET
    @Produces("application/xml")
    public String solrApi() {
        StringBuffer response = buildResponse("all", "TITLE");
        return response.toString();
    }

    @Path("{field}/{keyword}")
    @GET
    @Produces("application/xml")
    public String solrApi(@PathParam("field") String field, @PathParam("keyword") String keyword) {
        StringBuffer response = buildResponse(keyword, field);
        return response.toString();
    }

    private StringBuffer buildResponse(String keyword, String field) {
        List<Product> products;
        StringBuffer response = new StringBuffer();
        response.append("<solrAddApi>");
        try {
            if (keyword.equals("all")) {
                products = queryAllSolr();
            } else {
                products = querySimpleSolr(keyword, field);
            }
            Iterator<Product> itr = products.iterator();
            while (itr.hasNext()) {
                Product product = itr.next();
                    response.append("<id>" + product.getId() + "</id>");
                    response.append("<cat_id>" + product.getCATALOG_ENTRY_ID() + "</cat_id>");
                    response.append("<title>" + product.getTITLE() + "</title>");
                    response.append("<entry_type>" + product.getENTRY_TYPE() + "</entry_type>");
            }
            response.append("</solrAddApi>");
        } catch (SolrServerException | IOException e) {
            e.printStackTrace();
        }

        System.out.println("Response : " + response);
        return response;
    }

    private List<Product> queryAllSolr() throws SolrServerException, IOException {
        HttpSolrClient server =
                new HttpSolrClient("http://nadellas.sandbox.gspt.net:7070/solr/products");
        SolrQuery query = new SolrQuery("*:*");
        System.out.println("Query : " + query);
        QueryResponse response = server.query(query);
        // get the response as List of POJO type
        List<Product> foundDocuments = response.getBeans(Product.class);
        System.out.println("Found : " + foundDocuments.size() + " Docs !!!");
        return foundDocuments;
    }

    private static List<Product> querySimpleSolr(String keyword, String field)
        throws SolrServerException, IOException {
        HttpSolrClient server =
                new HttpSolrClient("http://nadellas.sandbox.gspt.net:7070/solr/products");

        if (keyword == null || keyword.length() == 0)
            keyword = "*:*";
        System.out.println("Keywords : " + keyword + " : Field " + field);
        SolrQuery query = toQuery(field + ":*" + keyword + "*");

        // query.setQuery(keyword);
        // query.addFilterQuery("cat:electronics","store:amazon.com");
        // query.setFields(field);
        // query.setStart(0);
        // query.set("defType", "edismax");

        // SolrQuery query = new SolrQuery();
        // query.setQuery(params);
        // query.addSort("CATALOG_ENTRY_ID", SolrQuery.ORDER.asc);
        query.setFields("CATALOG_ENTRY_ID", "ENTRY_TYPE", "SHORT_DESCRIPTION", "LONG_DESCRIPTION",
                "TITLE");

        System.out.println("Query  : " + query);
        QueryResponse rsp = server.query(query);
        SolrDocumentList docs = rsp.getResults();
        System.out.println("Docs : " + docs);
        List<Product> beans = rsp.getBeans(Product.class);
        System.out.println("Beans :  " + beans);
        return beans;
    }

    public static SolrQuery toQuery(String queryString) {

        if (queryString == null || queryString.length() == 0)
            queryString = "*:*";

        SolrQuery q = new SolrQuery();
        if (queryString.indexOf("=") == -1) {
            // no name-value pairs ... just assume this single clause is the q part
            q.setQuery(queryString);
        } else {
            NamedList<Object> params = new NamedList<Object>();
            for (NameValuePair nvp : URLEncodedUtils.parse(queryString, StandardCharsets.UTF_8)) {
                String value = nvp.getValue();
                if (value != null && value.length() > 0) {
                    String name = nvp.getName();
                    if ("sort".equals(name)) {
                        if (value.indexOf(" ") == -1) {
                            q.addSort(SolrQuery.SortClause.asc(value));
                        } else {
                            String[] split = value.split(" ");
                            q.addSort(SolrQuery.SortClause.create(split[0], split[1]));
                        }
                    } else {
                        params.add(name, value);
                    }
                }
            }
            q.add(ModifiableSolrParams.toSolrParams(params));
        }

        return q;
    }
}
