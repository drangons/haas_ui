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
import de.gwdg.portlet.hadoopcluster.model.Cluster;
import de.gwdg.portlet.hadoopcluster.model.Image;
import de.gwdg.portlet.hadoopcluster.model.Plugin;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;




/**
 *@ApplicationScoped. 
 * Per tenant data container
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
    
    
    
    
    /**
     * Get keystone token
     * Should be replaced when integrated with cloud portlet
     */
    @Deprecated
    public void loadUserCredentials()
    {
        logger.info("Inside loadUserCredentials");
        String OS_AUTH_URL="http://141.5.101.121:5000/v2.0";
        String AUTH_TOKEN_URL=OS_AUTH_URL+"/tokens";
        
        logger.debug("Token url"+AUTH_TOKEN_URL);
        String OS_TENANT_ID="bf809f0f55954a80b73d69a6b369774b";
        String OS_TENANT_NAME="admin";
        String OS_USERNAME="admin";
        String OS_PASSWORD="nova";
        
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
                logger.info("inside access");
                jsonResponse=jsonResponse.get("access");
                String temp=jsonResponse.get("token").get("id").asText();
                logger.info("token is" + temp);
                setToken(jsonResponse.get("token").get("id").asText());
                
                logger.debug("token is "+getToken());
            }
            logger.info("outside if");
            
        }catch(Exception e)
        {
            logger.error("Something went kapput in loadUserCredentials"+e.getMessage());
            FacesContext.getCurrentInstance().addMessage(null,
                new FacesMessage("Error loading user credentials " + e.getMessage()));
        }
        
        
        
    }

    /**
     * Display the node templates
     */
     ArrayList<NodeTemplate> nodeTemplateList
            =new ArrayList<NodeTemplate>();
     /**
      * Store node template with node name as key for lookup
      */
     HashMap<String,NodeTemplate> maplist=new HashMap<String,NodeTemplate>();
     
     /**
      * Display cluster templates
      */
     ArrayList<ClusterTemplate> clusterTemplateList=
             new ArrayList<ClusterTemplate>();
    
    public ArrayList<ClusterTemplate> getClusterTemplateList() {
        return clusterTemplateList;
    }

    public void setClusterTemplateList(ArrayList<ClusterTemplate> clusterTemplateList) {
        this.clusterTemplateList = clusterTemplateList;
    }
    /**
     * Store cluster template with template name as key for lookup
     */
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
    /**
     * Initializes the application
     */
    @PostConstruct
    public void init()
    {
        logger.info("Hadoop Bean init");
        loadUserCredentials();
        loadImages();
        loadNodeTemplate();
        loadClusterTemplate();
        loadClusters();
        loadPlugins();
        loadOpenStackFlavors();
    }
    /**
     * Invoked when cluster template is added or deleted. Updates cluster template 
     * display table
     */
    public void updateClusterTemplateDisplay()
    {
        clusterTemplateList.clear();
        clusterTemplateList.addAll(clusterlist.values());
    }
    /**
     * Invoked when node template is added or deleted. Updates node template table 
     * display in NodeTEmplate.xhtml
     */
    public void updateNodeTemplateDisplay()
    {
        
        
        nodeTemplateList.clear();
        nodeTemplateList.addAll(maplist.values());
    }
    /**
     * Adds a new node template
     * @param temp Node template 
     */
    public void add(NodeTemplate temp)
    {
        //nodeTemplateList.add(temp);
        maplist.put(temp.getName(),temp);
        updateNodeTemplateDisplay();
    }
    /**
     * Adds new cluster template
     * @param temp cluster template 
     */
    public void add(ClusterTemplate temp)
    {
        clusterlist.put(temp.getName(),temp);
        updateClusterTemplateDisplay();
        //clusterTemplateList.add(temp);
    }
    /**
     * Returns id of given cluster template name
     * @param name name of cluster template
     * @return id id of cluster template
     */
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
    /**
     * Delete cluster template 
     * @param name Name of the template
     */
    public void deleteClusterTemplate(String name) {
        if (clusterlist.containsKey(name)) {
            String clusterTemplateId = clusterlist.get(name).getId();
            String SAHARA_ENDPOINT = "http://141.5.101.121:8386/v1.0/";
            String TENANT_ID = "bf809f0f55954a80b73d69a6b369774b";
            String CLUSTER_TEMPLATE_PATH
                    = SAHARA_ENDPOINT + TENANT_ID + "/cluster-templates/" + clusterTemplateId;

            HTTPClient.setEndpoint(CLUSTER_TEMPLATE_PATH);

            HTTPClient.setToken(getToken());

            try {
                String response = HTTPClient.delete();
                clusterlist.remove(name);
                updateClusterTemplateDisplay();
            } catch (Exception e) {
                logger.error("Error while deleting cluster template" + name + " " + e);
            }
        } else {
            logger.error("deleteClusterTemplate(): ClusterTemplate" + name + "not found");
        }
    }
    /**
     * Delete Node template
     * @param name Name of node template
     */
    public void deleteNodeTemplate(String name)
    {
        if(maplist.containsKey(name))
        {
            String nodeTemplateId=maplist.get(name).getId();
            String SAHARA_ENDPOINT="http://141.5.101.121:8386/v1.0/";
        String TENANT_ID="bf809f0f55954a80b73d69a6b369774b";
        String NODE_GROUP_TEMP_PATH=SAHARA_ENDPOINT + 
                TENANT_ID + "/node-group-templates/"+nodeTemplateId;
         HTTPClient.setEndpoint(NODE_GROUP_TEMP_PATH);
        //logger.info("endpoint"+NODE_GROUP_TEMP_PATH);
        HTTPClient.setToken(getToken());
        try{
                String response = HTTPClient.delete();
                maplist.remove(name);
            updateNodeTemplateDisplay();
        }
        catch(Exception e)
                {
                    logger.error("Error while deleting node template"+name+" "+e);
                }
            
        }
        else
        {
            logger.error("deleteNodeTemplate(): Nodetemplate"+name+"not found");
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

    

    /**
     * Reads node templates from sahara. Invoked during application init()
     */
    public void loadNodeTemplate()
    {
        logger.info(" Inside loadNodeTemplate");
        String SAHARA_ENDPOINT="http://141.5.101.121:8386/v1.0/";
        String TENANT_ID="bf809f0f55954a80b73d69a6b369774b";
        String NODE_GROUP_TEMP_PATH=SAHARA_ENDPOINT + TENANT_ID + "/node-group-templates";
        HTTPClient.setEndpoint(NODE_GROUP_TEMP_PATH);
        //logger.info("endpoint"+NODE_GROUP_TEMP_PATH);
        HTTPClient.setToken(getToken());
        try{
                String response = HTTPClient.get(null);
                JsonNode jsonResponse = (new JacksonReader(response)).getJsonNode();
            
            logger.debug("json response" + jsonResponse);
            if(jsonResponse.has("node_group_templates"))
            {
                jsonResponse=jsonResponse.get("node_group_templates");
                Iterator<JsonNode> jsonIterator = jsonResponse.iterator();
                while (jsonIterator.hasNext()) 
                {
                    NodeTemplate nt=new NodeTemplate();
                    JsonNode temp=jsonIterator.next();
                    
                    nt.load(temp);
                    
                    maplist.put(nt.getName(), nt);
                    updateNodeTemplateDisplay();
                    
                }
            }
        }
        catch(Exception e)
        {
            logger.error("Error while loading the NodeTemplate list"+e.getMessage());
            FacesContext.getCurrentInstance().addMessage(null,
                new FacesMessage("Error loading user credentials " + e.getMessage()));
        }
    }
    /**
     * Loads cluster template from sahara .
     * Invoked at application init()
     */
    public void loadClusterTemplate()
    {
        logger.info(" Inside loadClusterTemplate");
        String SAHARA_ENDPOINT="http://141.5.101.121:8386/v1.0/";
        String TENANT_ID="bf809f0f55954a80b73d69a6b369774b";
        String CLUSTER_TEMPLATE_PATH=
                SAHARA_ENDPOINT + TENANT_ID + "/cluster-templates";

        HTTPClient.setEndpoint(CLUSTER_TEMPLATE_PATH);

        HTTPClient.setToken(getToken());


        try{
             String response = HTTPClient.get(null);
            JsonNode jsonResponse = (new JacksonReader(response)).getJsonNode();
            
            logger.debug("json response" + jsonResponse);
            if(jsonResponse.has("cluster_templates"))
            {
                jsonResponse=jsonResponse.get("cluster_templates");
                Iterator<JsonNode> jsonIterator = jsonResponse.iterator();
                while (jsonIterator.hasNext()) {
                    ClusterTemplate ct=new ClusterTemplate();
                    ct.load(jsonIterator.next());
                    clusterlist.put(ct.getName(),ct);
                    updateClusterTemplateDisplay();
                }
            }
        }catch(Exception e)
        {
            logger.error("Error while loading the loadClusterTemplate list"+e.getMessage());
            FacesContext.getCurrentInstance().addMessage(null,
                new FacesMessage("Error loading user credentials " + e.getMessage()));
        }
    }
    /**
     * Stores the images 
     */
    ArrayList<Image> imageList=
            new ArrayList<Image>();

    public ArrayList<Image> getImageList() {
        return imageList;
    }

    public void setImageList(ArrayList<Image> imageList) {
        this.imageList = imageList;
    }
    /**
     * Loads images from sahara. Invoked during application init()
     */
    public void loadImages()
    {
        logger.info("Loading sahara images");
        String SAHARA_ENDPOINT="http://141.5.101.121:8386/v1.0/";
        String TENANT_ID="bf809f0f55954a80b73d69a6b369774b";
        String IMAGE_PATH=
                SAHARA_ENDPOINT + TENANT_ID + "/images";
        HTTPClient.setEndpoint(IMAGE_PATH);

        HTTPClient.setToken(getToken());
        
        try{
            String response = HTTPClient.get(null);
            JsonNode jsonResponse = (new JacksonReader(response)).getJsonNode();
            logger.debug(" loadImages(): json response" + jsonResponse);
            if(jsonResponse.has("images"))
            {
                jsonResponse=jsonResponse.get("images");
                Iterator<JsonNode> jsonIterator = jsonResponse.iterator();
                while (jsonIterator.hasNext()) {
                    Image img=new Image();
                    img.load(jsonIterator.next());
                    imageList.add(img);
                    
                }
            }
        }catch(Exception e)
        {
            logger.error("Error while loading the images"+ e.getMessage());
            FacesContext.getCurrentInstance().addMessage(null,
                new FacesMessage("Error loading user credentials " + e.getMessage()));
        }
        
                
    }
    /**
     *  cluster list 
     */
    ArrayList<Cluster> clusters =new ArrayList<Cluster>();
    
        public ArrayList<Cluster> getClusters() {
        return clusters;
    }

    public void setClusters(ArrayList<Cluster> clusters) {
        this.clusters = clusters;
    }
    /**
     * Loads clusters from sahara. Invoked at application init()
     */
    public void loadClusters()
    {
        logger.info("loading clusters");
        String SAHARA_ENDPOINT="http://141.5.101.121:8386/v1.0/";
        String TENANT_ID="bf809f0f55954a80b73d69a6b369774b";
        String CLUSTER_PATH=
                SAHARA_ENDPOINT + TENANT_ID + "/clusters";
        
        HTTPClient.setEndpoint(CLUSTER_PATH);

        HTTPClient.setToken(getToken());
        try{
            String response = HTTPClient.get(null);
            JsonNode jsonResponse = (new JacksonReader(response)).getJsonNode();
            logger.debug(" loadClusters(): json response" + jsonResponse);
            if(jsonResponse.has("clusters"))
            {
                jsonResponse=jsonResponse.get("clusters");
                Iterator<JsonNode> jsonIterator = jsonResponse.iterator();
                while (jsonIterator.hasNext()) {
                    Cluster c=new Cluster();
                    JsonNode Node=jsonIterator.next();
                    logger.debug("cluster node "+Node);
                    c.load(Node);
                    clusters.add(c);
                    
                }
                
            }
        }catch(Exception e)
        {
            logger.error("Error while loading the clusters"+e.getMessage());
            FacesContext.getCurrentInstance().addMessage(null,
                new FacesMessage("Error loading user credentials " + e.getMessage()));
        }
            
    }
    /**
     * stores Sahara plugins
     */
    ArrayList<Plugin> plugins=
            new ArrayList<Plugin>();

    public ArrayList<Plugin> getPlugins() {
        return plugins;
    }

    public void setPlugins(ArrayList<Plugin> plugins) {
        this.plugins = plugins;
    }
    /**
     * Loads plugins from sahara. Invoked during application init()
     */
    public void loadPlugins()
    {
        logger.info("Inside loadPlugins()");
        String SAHARA_ENDPOINT="http://141.5.101.121:8386/v1.0/";
        String TENANT_ID="bf809f0f55954a80b73d69a6b369774b";
        String PLUGIN_PATH=
                SAHARA_ENDPOINT + TENANT_ID + "/plugins";
         HTTPClient.setEndpoint(PLUGIN_PATH);

        HTTPClient.setToken(getToken());
        try{
             String response = HTTPClient.get(null);
            JsonNode jsonResponse = (new JacksonReader(response)).getJsonNode();
            logger.debug(" loadPlugins(): json response" + jsonResponse);
            if(jsonResponse.has("plugins"))
            {
                jsonResponse=jsonResponse.get("plugins");
                Iterator<JsonNode> jsonIterator = jsonResponse.iterator();
                while (jsonIterator.hasNext()) {
                    Plugin p=new Plugin();
                    JsonNode Node=jsonIterator.next();
                    p.load(Node);
                    plugins.add(p);
                    
                    
                }
            }
        }catch(Exception e)
        {
            logger.error("Error loading loadPlugins"+e.getMessage());
            FacesContext.getCurrentInstance().addMessage(null,
                new FacesMessage("Error loading user credentials " + e.getMessage()));
        }
        
    }
    /**
     * Stores openstack nova flavors
     */
    ArrayList<String> flavorList=
            new ArrayList<String>();

    public ArrayList<String> getFlavorList() {
        return flavorList;
    }

    public void setFlavorList(ArrayList<String> flavorList) {
        this.flavorList = flavorList;
    }
    /**
     * Loads nova instance flavors. Invoked during application init()
     */
    public void loadOpenStackFlavors()
    {
        logger.info("Inside loadOpenStackFlavors()");
        String NOVA_ENDPOINT="http://141.5.101.121:8774/v2/";
        String TENANT_ID="bf809f0f55954a80b73d69a6b369774b";
        String FLAVOR_PATH=
                NOVA_ENDPOINT + TENANT_ID + "/flavors";
         HTTPClient.setEndpoint(FLAVOR_PATH);

        HTTPClient.setToken(getToken());
        try{
            String response = HTTPClient.get(null);
            JsonNode jsonResponse = (new JacksonReader(response)).getJsonNode();
            if(jsonResponse.has("flavors"))
            {
                jsonResponse=jsonResponse.get("flavors");
                Iterator<JsonNode> jsonIterator = jsonResponse.iterator();
                while (jsonIterator.hasNext()) {
                    flavorList.add(jsonIterator.next().get("name").asText());
                }
                
            }
            
        }catch(Exception e)
        {
            logger.error("Error while loading OpenStackFlavors"+e.getMessage());
            FacesContext.getCurrentInstance().addMessage(null,
                new FacesMessage("Error loading user credentials " + e.getMessage()));
        }
        
    }
    
}
