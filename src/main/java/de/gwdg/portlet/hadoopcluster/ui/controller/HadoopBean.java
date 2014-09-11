/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package de.gwdg.portlet.hadoopcluster.ui.controller;

import com.fasterxml.jackson.databind.JsonNode;
import de.gwdg.portlet.computecloud.ui.util.HTTPClient;
import de.gwdg.portlet.computecloud.ui.util.json.JacksonReader;
import de.gwdg.portlet.hadoopcluster.model.ClusterTemplate;
import de.gwdg.portlet.hadoopcluster.model.NodeTemplate;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import javax.annotation.PostConstruct;
//import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Named;
import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonWriter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;



import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;




/**
 *
 * @author dikshith
 */
@ApplicationScoped
@Named
public class HadoopBean {

    private static final Logger logger=
            LoggerFactory.getLogger(HadoopBean.class);
    private String token;

    
    
    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
    
    // image name and image id list
    
    //manage cluster DS
    
    public void loadUserCredentials()
    {
        logger.info("Inside loadUserCredentials");
        String OS_AUTH_URL="http://10.108.16.13:5000/v2.0";
        String AUTH_TOKEN_URL=OS_AUTH_URL+"/tokens";
        
        logger.info("Token url"+AUTH_TOKEN_URL);
        String OS_TENANT_ID="e8db1678ff104c3397e30293ebdba61a";
        String OS_TENANT_NAME="demo";
        String OS_USERNAME="admin";
        String OS_PASSWORD="password";
        
        JsonObject  password_cred=Json.createObjectBuilder()
                .add("username",OS_USERNAME)
                .add("password",OS_PASSWORD).
                build();
        
        JsonObject auth=Json.createObjectBuilder()
                .add("passwordCredentials",password_cred)
                .add("tenantName",OS_TENANT_NAME)
                .build();
        JsonObject body=Json.createObjectBuilder()
                .add("auth",auth)
                .build();
        
        StringWriter stWriter = new StringWriter();
        try (JsonWriter jsonWriter = Json.createWriter(stWriter)) {
             jsonWriter.writeObject(body);
        }
        
        String jsonData = stWriter.toString();
        System.out.println(jsonData);
        HTTPClient.setEndpoint(AUTH_TOKEN_URL);
        HTTPClient.setToken("  ");
        try
        {
            
             Client client = Client.create();
            String ans= client.resource(AUTH_TOKEN_URL)
                .header("Content-Type", "application/json;charset=UTF-8")
                .post(ClientResponse.class, jsonData)
                .getEntity(String.class);
            //logger.info("ans is "+ans);
            //String response = HTTPClient.post(jsonData);
            
            JsonNode jsonResponse = (new JacksonReader(ans)).getJsonNode();
            
            logger.info("json response" + jsonResponse);
            
            if(jsonResponse.has("access"))
            {
                jsonResponse=jsonResponse.get("access");
                setToken(jsonResponse.get("token").get("id").asText());
                
                logger.info("token is "+getToken());
            }
            
        }catch(Exception e)
        {
            logger.error("Something went kapput in loadUserCredentials"+e);
        }
        
        
        
    }

    
     ArrayList<NodeTemplate> nodeTemplateList
            =new ArrayList<NodeTemplate>();
     HashMap<String,NodeTemplate> maplist=new HashMap<String,NodeTemplate>();
     
     ArrayList<ClusterTemplate> clusterTemplateList=
             new ArrayList<ClusterTemplate>();

    public ArrayList<ClusterTemplate> getClusterTemplateList() {
        return clusterTemplateList;
    }

    public void setClusterTemplateList(ArrayList<ClusterTemplate> clusterTemplateList) {
        this.clusterTemplateList = clusterTemplateList;
    }
     HashMap<String,ClusterTemplate> clusterlist= 
             new HashMap<String,ClusterTemplate>();

    public HashMap<String, ClusterTemplate> getClusterlist() {
        return clusterlist;
    }

    public void setClusterlist(HashMap<String, ClusterTemplate> clusterlist) {
        this.clusterlist = clusterlist;
    }

    public HashMap<String, NodeTemplate> getMaplist() {
        return maplist;
    }

    public void setMaplist(HashMap<String, NodeTemplate> maplist) {
        this.maplist = maplist;
    }

    public ArrayList<NodeTemplate> getNodeTemplateList() {
        return nodeTemplateList;
    }

    public void setNodeTemplateList(ArrayList<NodeTemplate> nodeTemplateList) {
        this.nodeTemplateList = nodeTemplateList;
    }
    @PostConstruct
    public void init()
    {
        logger.info("Hadoop Bean init");
        loadUserCredentials();
        loadNodeTemplate();
        loadClusterTemplate();
        
    }
    public void updateClusterTemplateDisplay()
    {
        clusterTemplateList.clear();
        clusterTemplateList.addAll(clusterlist.values());
    }
    public void updateNodeTemplateDisplay()
    {
        
        
        nodeTemplateList.clear();
        nodeTemplateList.addAll(maplist.values());
    }
    public void add(NodeTemplate temp)
    {
        nodeTemplateList.add(temp);
        maplist.put(temp.getName(),temp);
    }
    public void add(ClusterTemplate temp)
    {
        clusterlist.put(temp.getName(),temp);
        clusterTemplateList.add(temp);
    }
    public String getClusterTemplateId(String name)
    {
        String id="";
        if(clusterlist.containsKey(name))
        {
            
            id= clusterlist.get(name).getId();
            logger.info("return id"+id +"for clustertemplate"+name);
        }
        else
        {
            logger.error("getClusterTemplateId(): ClusterTemplate"+ name+"not found");
        }
        return id;
    }
    public void deleteClusterTemplate(String name)
    {
        if(clusterlist.containsKey(name))
        {
            clusterlist.remove(name);
            updateClusterTemplateDisplay();
        }
        else
        {
            logger.error("deleteClusterTemplate(): ClusterTemplate"+ name+"not found");
        }
    }
    public ClusterTemplate getClusterTemplate(String name)
    {
        if(clusterlist.containsKey(name))
            return clusterlist.get(name);
        else
            logger.error("getClusterTemplate(): No cluster template with name"+name);
        return null;
    }
    public NodeTemplate getNodeTemplate(String Name)
    {
        return maplist.get(Name);
    }
    
    public void addClusterTemplate(ClusterTemplate temp)
    {
        clusterlist.put(temp.getName(),temp);
    }
    
    public void loadNodeTemplate()
    {
        logger.info(" Inside loadNodeTemplate");
        String SAHARA_ENDPOINT="http://10.108.16.13:8386/v1.0/";
        String TENANT_ID="e8db1678ff104c3397e30293ebdba61a";
        String NODE_GROUP_TEMP_PATH=SAHARA_ENDPOINT + TENANT_ID + "/node-group-templates";
        HTTPClient.setEndpoint(NODE_GROUP_TEMP_PATH);
        //logger.info("endpoint"+NODE_GROUP_TEMP_PATH);
        HTTPClient.setToken(getToken());
        try{
                String response = HTTPClient.get(null);
                JsonNode jsonResponse = (new JacksonReader(response)).getJsonNode();
            
            logger.info("json response" + jsonResponse);
            if(jsonResponse.has("node_group_templates"))
            {
                jsonResponse=jsonResponse.get("node_group_templates");
                Iterator<JsonNode> jsonIterator = jsonResponse.iterator();
                while (jsonIterator.hasNext()) 
                {
                    NodeTemplate nt=new NodeTemplate();
                    JsonNode temp=jsonIterator.next();
                    logger.info("before load"+temp.toString());
                    nt.load(temp);
                    logger.info("after load");
                    maplist.put(nt.getName(), nt);
                    nodeTemplateList.add(nt);
                    
                }
            }
        }
        catch(Exception e)
        {
            logger.error("Error while loading the NodeTemplate list"+e);
        }
    }
    
    public void loadClusterTemplate()
    {
        logger.info(" Inside loadClusterTemplate");
        String SAHARA_ENDPOINT="http://10.108.16.13:8386/v1.0/";
        String TENANT_ID="e8db1678ff104c3397e30293ebdba61a";
        String CLUSTER_TEMPLATE_PATH=
                SAHARA_ENDPOINT + TENANT_ID + "/cluster-templates";

        HTTPClient.setEndpoint(CLUSTER_TEMPLATE_PATH);

        HTTPClient.setToken(getToken());


        try{
             String response = HTTPClient.get(null);
            JsonNode jsonResponse = (new JacksonReader(response)).getJsonNode();
            
            logger.info("json response" + jsonResponse);
            if(jsonResponse.has("cluster_templates"))
            {
                jsonResponse=jsonResponse.get("cluster_templates");
                Iterator<JsonNode> jsonIterator = jsonResponse.iterator();
                while (jsonIterator.hasNext()) {
                    ClusterTemplate ct=new ClusterTemplate();
                    ct.load(jsonIterator.next());
                    clusterlist.put(ct.getName(),ct);
                    clusterTemplateList.add(ct);
                }
            }
        }catch(Exception e)
        {
            logger.error("Error while loading the loadClusterTemplate list"+e);
        }
    }
    
}
