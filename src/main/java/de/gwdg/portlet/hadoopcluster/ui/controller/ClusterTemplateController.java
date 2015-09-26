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
import de.gwdg.portlet.hadoopcluster.model.Plugin;
import java.io.Serializable;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import javax.inject.Named;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.json.Json;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;
import javax.json.JsonWriter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author dikshith
 */
@Named
@ViewScoped
public class ClusterTemplateController implements Serializable {
    
    String node;
    @Inject
    HadoopBean  bean; 
    
    int count;

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }


    
    private static final Logger logger=
            LoggerFactory.getLogger(ClusterTemplateController.class);
    
    public String getNode() {
        return node;
    }

    public void setNode(String node) {
        this.node = node;
    }

    private ClusterTemplate tempClusterTemplate= new ClusterTemplate();
    
    
    ArrayList<NodeTemplateCount> nodeTemplateCountList= new 
        ArrayList<NodeTemplateCount>();

    public ArrayList<NodeTemplateCount> getNodeTemplateCountList() {
        return nodeTemplateCountList;
    }

    public void setNodeTemplateCountList(ArrayList<NodeTemplateCount> nodeTemplateCountList) {
        this.nodeTemplateCountList = nodeTemplateCountList;
    }



    public ClusterTemplate getTempClusterTemplate() {
        return tempClusterTemplate;
    }

    public void setTempClusterTemplate(ClusterTemplate tempClusterTemplate) {
        this.tempClusterTemplate = tempClusterTemplate;
    }
    NodeTemplateCount nodeTemplateCount =
            new NodeTemplateCount();

    public NodeTemplateCount getNodeTemplateCount() {
        return nodeTemplateCount;
    }

    public void setNodeTemplateCount(NodeTemplateCount nodeTemplateCount) {
        this.nodeTemplateCount = nodeTemplateCount;
    }
    public void addNode()
    {
        logger.info("inside addNode with "+node+"node and "+count);
        NodeTemplateCount t=new NodeTemplateCount();
        t.setCount(count);
        t.setNode(node);
        nodeTemplateCountList.add(t);
        
        //tempNodeTemplate.add(new ClusterTemplate.NodeTemplate(node,count));
    }
//    public void onCellEdit(CellEditEvent event) {
//        Object oldValue = event.getOldValue();
//        Object newValue = event.getNewValue();
//         FacesContext f=FacesContext.getCurrentInstance();
//         Map<Object,Object> map=f.getAttributes();
//
//         
//         
//        if(newValue != null && !newValue.equals(oldValue)) {
//            FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO, "Cell Changed", "Old: " + oldValue + ", New:" + newValue);
//            FacesContext.getCurrentInstance().addMessage(null, msg);
//        }
//    }
//    
//    public void onNodeChange() {
//        FacesMessage msg;
//        msg = new FacesMessage("Selected node "+ node);
//        FacesContext.getCurrentInstance().addMessage(null, msg);
//        
//        FacesContext f=FacesContext.getCurrentInstance();
//         Map<Object,Object> map=f.getAttributes();
//
//         logger.info("faces context" + map.toString());
//         
//         
//         
//        if(node !=null && !node.equals(""))
//           // cities = data.get(country);
//            tempClusterTemplate.addNodeTemplate(node,0);
//    }
    

//    public void incrementCount()
//    {
//        count++;
//    }
//    
    
    public void process()
    {
        logger.info("reached cluster processing");
        
        String SAHARA_ENDPOINT="http://10.108.16.13:8386/v1.0/";
        String TENANT_ID="e8db1678ff104c3397e30293ebdba61a";
        String CLUSTER_TEMPLATE_PATH=
                SAHARA_ENDPOINT + TENANT_ID + "/cluster-templates";
        //logger.info("token"+ bean.getToken());
        HTTPClient.setEndpoint(CLUSTER_TEMPLATE_PATH);
        logger.info("endpoint"+CLUSTER_TEMPLATE_PATH);
        HTTPClient.setToken(bean.getToken());
        //tempClusterTemplate.setNodeTemplatelist(nodeTemplateCountList);
        logger.info("json format"+ this.toJSON());
        try{
             String response = HTTPClient.post(this.toJSON());
            JsonNode jsonResponse = (new JacksonReader(response)).getJsonNode();
            
            logger.info("json response" + jsonResponse);
            if(jsonResponse.has("cluster_template"))
            {
                jsonResponse=jsonResponse.get("cluster_template");
                tempClusterTemplate.setId(jsonResponse.get("id").asText());
                logger.info("cluster template id is "+jsonResponse.get("id").asText());
                tempClusterTemplate.setNodeTemplatelist(nodeTemplateCountList);
                bean.add(tempClusterTemplate);
            }
        }
        catch(Exception e)
        {
            logger.info("something went wrong while saving cluster template"+e);
            
        }
    }
    
        public String toJSON()
    {
        JsonArrayBuilder nodearray =Json.createArrayBuilder();
        JsonObjectBuilder node=Json.createObjectBuilder();
        
        HashMap<String,NodeTemplate> temp=bean.getMaplist();
        
        logger.info("Bean map entries");
        
        for(Entry<String,NodeTemplate> entry: temp.entrySet())
        {
            logger.info("key"+entry.getKey());
            logger.info("Value"+entry.getValue().getId());
        }

        for(NodeTemplateCount process : nodeTemplateCountList)
        {
                    if(temp.containsKey(process.getNode()))
                    {
                        
                        node.add("name",process.getNode());
                        node.add("node_group_template_id",temp.get(process.getNode()).getId());
                        node.add("count",process.getCount());
                        nodearray.add(node);
                    }
                    
                    else
                        logger.error("Node id for node template "+process.getNode()+"not found");
                    
        }
        JsonObject model = Json.createObjectBuilder()
                .add("name",tempClusterTemplate.getName())
                .add("plugin_name",tempClusterTemplate.getPluginName())
                .add("hadoop_version",tempClusterTemplate.getHadoopVersion())
                .add("node_groups",nodearray)
                .build();
        
        StringWriter stWriter = new StringWriter();
        try (JsonWriter jsonWriter = Json.createWriter(stWriter)) {
             jsonWriter.writeObject(model);
        }
        
        String jsonData = stWriter.toString();
        System.out.println(jsonData);
        
        return jsonData;
        
        
    }
       
    public void actionLaunchCluster(String name)
    {
        logger.info("Launching cluster"+name);
        String SAHARA_ENDPOINT="http://10.108.16.13:8386/v1.0/";
        String TENANT_ID="e8db1678ff104c3397e30293ebdba61a";
        String CLUSTER_PATH=
                SAHARA_ENDPOINT + TENANT_ID + "/clusters";
        HTTPClient.setEndpoint(CLUSTER_PATH);
        HTTPClient.setToken(bean.getToken());
        try{
            ClusterTemplate t=bean.getClusterTemplate(name);
            JsonObject cluster_configs=Json.createObjectBuilder().build();
            JsonObject model = Json.createObjectBuilder()
                    .add("plugin_name",t.getPluginName())
                    .add("hadoop_version",t.getHadoopVersion())
                    .add("cluster_template_id",t.getId())
                    .add("default_image_id","aef6f97e-60b1-4814-9920-1ffa8e0d16de")
                    .add("neutron_management_network","bfa008c6-bc89-42dd-bd9e-0324d3dcc5cd")
                    .add("name","portal")
                    .add("cluster_configs",cluster_configs)
                    .build();
             StringWriter stWriter = new StringWriter();
         try (JsonWriter jsonWriter = Json.createWriter(stWriter)) {
             jsonWriter.writeObject(model);
        }
        
        
        String jsonData = stWriter.toString();
        
        logger.info("JSOn data is "+jsonData);
            String response = HTTPClient.post(jsonData);
            JsonNode jsonResponse = (new JacksonReader(response)).getJsonNode();
            logger.info("Cluster launch response"+response);
            
        }catch(Exception e)
        {
            logger.error("actionLaunchCluster failed"+e);
        }
            
            
        
    }
    public void deleteClusterTemplate(String name)
    {
        logger.info("Deleting cluster template with name"+name);
        String id= bean.getClusterTemplateId(name);
        try
        {
            String SAHARA_ENDPOINT="http://10.108.16.13:8386/v1.0/";
            String TENANT_ID="e8db1678ff104c3397e30293ebdba61a";
            String CLUSTER_TEMPLATE_PATH=
                    SAHARA_ENDPOINT + TENANT_ID + "/cluster-templates/"+id;

            logger.info("ClusterTemplate delete path"+CLUSTER_TEMPLATE_PATH);
            HTTPClient.setEndpoint(CLUSTER_TEMPLATE_PATH);
            HTTPClient.setToken(bean.getToken());
            HTTPClient.delete();
            bean.deleteClusterTemplate(name);
        }catch(Exception e)
        {
            logger.error("Something went wrong while delete cluster template"+e);
        }
        
        
    }
    public void validateClusterTemplate()
    {
        if(tempClusterTemplate.getName()=="")
        {
           logger.error("Cluster template name cannot be empty");
        }
        else if(tempClusterTemplate.getPluginName()=="")
        {
            logger.error("Cluster template plugin name cannot be empty");
        }else if(tempClusterTemplate.getHadoopVersion()=="")
        {
            logger.error("Cluster template hadoop version cannot be empty");
        }else if(tempClusterTemplate.getNodeTemplatelist().size()==0)
        {
            logger.error("Cluster template node list cannot be empty");
        }
        
    }
    ArrayList<String> tempHadoopVersionList=
            new ArrayList<String>();

    public ArrayList<String> getTempHadoopVersionList() {
        return tempHadoopVersionList;
    }

    public void setTempHadoopVersionList(ArrayList<String> tempHadoopVersionList) {
        this.tempHadoopVersionList = tempHadoopVersionList;
    }
    
    public void onPluginChange()
    {
        for(Plugin p : bean.getPlugins())
        {
            
            if(p.getName().equals(tempClusterTemplate.getPluginName()))
            {
                setTempHadoopVersionList(p.getSupportVersions());
            }
        }
    }
    ArrayList<String> tempNodeGroups=
            new ArrayList<String>();

    public ArrayList<String> getTempNodeGroups() {
        return tempNodeGroups;
    }

    public void setTempNodeGroups(ArrayList<String> tempNodeGroups) {
        this.tempNodeGroups = tempNodeGroups;
    }
    
    public void updateNodeGroups()
    {
        HashMap<String, NodeTemplate> temp=bean.getMaplist();
        Iterator it=temp.entrySet().iterator();
        ArrayList<String>tl = new 
                ArrayList<String>();
        while(it.hasNext())
        {
            Map.Entry pair=(Map.Entry) it.next();
            NodeTemplate t=(NodeTemplate) pair.getValue();
            if(tempClusterTemplate.getPluginName().equals(t.getPluginName())
                    && tempClusterTemplate.getHadoopVersion().equals(t.getHadoopVersion()))
            {
                logger.info("updateNodeGroups adding node "+ (String)pair.getKey());
                tl.add((String)pair.getKey());
            }
        }
        setTempNodeGroups(tl);
    }
    public void deleteNodeTemplate(String name)
    {
        logger.info("deleteNodeTemplate delete node template "+ name);
        int n=nodeTemplateCountList.size();
        for(int i=0;i<n;i++)
        {
            NodeTemplateCount t=nodeTemplateCountList.get(i);
            if(t.getNode().equals(name))
            {
                logger.info("deleteNodeTemplate remove "+name);
                nodeTemplateCountList.remove(i);
                n--;
            }
            
            
        }
        
    }
    public void clearNodeTemplate()
    {
        logger.info("clearNodeTemplate");
        nodeTemplateCountList.clear();
    }
}
