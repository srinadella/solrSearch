package net.search.solr.impl;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.HttpSolrClient;
import org.apache.solr.client.solrj.impl.XMLResponseParser;
import org.apache.solr.client.solrj.request.UpdateRequest;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.client.solrj.response.UpdateResponse;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;
import org.apache.solr.common.SolrInputDocument;

import net.search.solr.model.Item;

public class SolrSearchImpl {

    public static void main(String[] args) {
        List<Item> beans = new ArrayList<Item>();
        try {
            addDocumentToSolr();
        } catch (SolrServerException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            addObjectToSolr();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (SolrServerException e) {
            e.printStackTrace();
        }

        try {
            beans = querySimpleSolr();
        } catch (SolrServerException e) {
            e.printStackTrace();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            beans = queryFacetSolr();
        } catch (SolrServerException e) {
            e.printStackTrace();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            List<String> results = queryHighlightingSolr();
            System.out.println("results : " + results.size());
        } catch (SolrServerException e) {
            e.printStackTrace();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private static List<String> queryHighlightingSolr() throws SolrServerException, IOException {
        SolrQuery query = new SolrQuery();
        query.setQuery("foo");

        query.setHighlight(true).setHighlightSnippets(1); // set other params as needed
        query.setParam("hl.fl", "content");

        QueryResponse queryResponse = getSolrServer().query(query);

        Iterator<SolrDocument> iter = queryResponse.getResults().iterator();
        List<String> highlightSnippets = new ArrayList<String>();
        while (iter.hasNext()) {
            SolrDocument resultDoc = iter.next();
            String content = (String)resultDoc.getFieldValue("content");
            System.out.println("Content : " + content);
            String id = (String)resultDoc.getFieldValue("id"); // id is the uniqueKey field
            if (queryResponse.getHighlighting().get(id) != null) {
                highlightSnippets = queryResponse.getHighlighting().get(id).get("content");
            }
        }
        return highlightSnippets;

    }

    private static List<Item> queryFacetSolr() throws SolrServerException, IOException {
        HttpSolrClient server = getSolrServer();
        SolrQuery solrQuery = new SolrQuery().setQuery("ipod").setFacet(true).setFacetMinCount(1)
                .setFacetLimit(8).addFacetField("category").addFacetField("inStock");
        QueryResponse rsp = server.query(solrQuery);

        SolrDocumentList docs = rsp.getResults();
        System.out.println("Docs : " + docs);
        List<Item> beans = rsp.getBeans(Item.class);

        return beans;

    }

    private static List<Item> querySimpleSolr() throws SolrServerException, IOException {
        // TODO Auto-generated method stub
        HttpSolrClient server = getSolrServer();

        SolrQuery query = new SolrQuery();
        query.setQuery("*:*");
        query.addSort("price", SolrQuery.ORDER.asc);

        QueryResponse rsp = server.query(query);

        SolrDocumentList docs = rsp.getResults();
        System.out.println("Docs : " + docs);

        List<Item> beans = rsp.getBeans(Item.class);

        return beans;
    }

    private static void addObjectToSolr() throws IOException, SolrServerException {
        HttpSolrClient server = getSolrServer();
        Item item = new Item();
        item.setId("one");
        item.setCategories(new String[] { "aaa", "bbb", "ccc" });
        server.addBean(item);
        List<Item> beans = new ArrayList<Item>();
        server.addBeans(beans);
    }

    private static void addDocumentToSolr() throws SolrServerException, IOException {
        HttpSolrClient server = getSolrServer();
        SolrInputDocument doc1 = new SolrInputDocument();
        doc1.addField("id", "id1", 1.0f);
        doc1.addField("name", "doc1", 1.0f);
        doc1.addField("price", 10);

        SolrInputDocument doc2 = new SolrInputDocument();
        doc2.addField("id", "id2", 1.0f);
        doc2.addField("name", "doc2", 1.0f);
        doc2.addField("price", 20);

        Collection<SolrInputDocument> docs = new ArrayList<SolrInputDocument>();
        docs.add(doc1);
        docs.add(doc2);

        server.add(docs);

        server.commit();

        UpdateRequest req = new UpdateRequest();
        req.setAction(UpdateRequest.ACTION.COMMIT, false, false);
        req.add(docs);
        UpdateResponse rsp = req.process(server);
        System.out.println(rsp);

    }

    /**
     * @return
     * @throws MalformedURLException
     */
    public static HttpSolrClient getSolrServer() throws MalformedURLException {
        String url = "http://nadellas.sandbox.gspt.net:7070/solr/products";
        
        /*
         * HttpSolrClient is thread-safe and if you are using the following constructor,
         * you *MUST* re-use the same instance for all requests. If instances are created on
         * the fly, it can cause a connection leak. The recommended practice is to keep a
         * static instance of HttpSolrClient per solr server url and share it for all requests.
         * See https://issues.apache.org/jira/browse/SOLR-861 for more details
         */

        HttpSolrClient server = new HttpSolrClient(url);
        server.setConnectionTimeout(5000); // 5 seconds to establish TCP
        // Setting the XML response parser is only required for cross
        // version compatibility and only when one side is 1.4.1 or
        // earlier and the other side is 3.1 or later.
        server.setParser(new XMLResponseParser()); // binary parser is used by default
        // The following settings are provided here for completeness.
        // They will not normally be required, and should only be used
        // after consulting javadocs to know whether they are truly required.
        server.setSoTimeout(1000);  // socket read timeout
        server.setDefaultMaxConnectionsPerHost(100);
        server.setMaxTotalConnections(100);
        server.setFollowRedirects(false);  // defaults to false
        // allowCompression defaults to false.
        // Server side must support gzip or deflate for this to have any effect.
        server.setAllowCompression(true);
        return server;
    }
}
