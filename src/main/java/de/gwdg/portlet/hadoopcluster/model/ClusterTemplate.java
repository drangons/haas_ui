/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package de.gwdg.portlet.hadoopcluster.model;

import com.fasterxml.jackson.databind.JsonNode;
import de.gwdg.portlet.hadoopcluster.ui.controller.NodeTemplateCount;
import java.io.Serializable;
import java.io.StringWriter;
import java.util.ArrayList;

import java.util.HashMap;
import java.util.Iterator;
import javax.json.Json;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObject;
import javax.json.JsonWriter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author dikshith
 */
public class ClusterTemplate implements Serializable {

    
    private static final Logger logger=
            LoggerFactory.getLogger(ClusterTemplate.class);
    
    
     public void load(JsonNode json)
    {
        setPluginName(json.get("plugin_name").asText());
        setName(json.get("name").asText());
        setId(json.get("id").asText()); 
        setHadoopVersion(json.get("hadoop_version").asText());
        JsonNode node_groups=json.get("node_groups");
        Iterator<JsonNode> elements =node_groups.elements();
        
        NodeTemplateCount t=new NodeTemplateCount();
        ArrayList<NodeTemplateCount> temp=
                new ArrayList<NodeTemplateCount>();
         while(elements.hasNext())
        {
            JsonNode node=elements.next();
            logger.info("node template count"+node.toString());
            t.setCount(node.get("count").asInt());
            t.setNode(node.get("name").asText());
            temp.add(t);
        }
         logger.info("Before setNodeTemplatelist");
         setNodeTemplatelist(temp);
         logger.info("After setNodeTemplatelist");
        
    }
    public String getPluginName() {
        return pluginName;
    }

    public void setPluginName(String pluginName) {
        this.pluginName = pluginName;
    }

    public String getHadoopVersion() {
        return hadoopVersion;
    }

    public void setHadoopVersion(String hadoopVersion) {
        this.hadoopVersion = hadoopVersion;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String Id) {
        this.id = Id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }



    public HashMap getGeneralParameters() {
        return generalParameters;
    }

    public void setGeneralParameters(HashMap GeneralParameters) {
        this.generalParameters = GeneralParameters;
    }

    public HashMap getHDFSParameters() {
        return hDFSParameters;
    }

    public void setHDFSParameters(HashMap hDFSParameters) {
        this.hDFSParameters = hDFSParameters;
    }

    public HashMap getMapReduceParameters() {
        return mapReduceParameters;
    }

    public void setMapReduceParameters(HashMap mapReduceParameters) {
        this.mapReduceParameters = mapReduceParameters;
    }

    public String toJSON()
    {
        JsonArrayBuilder nodearray =Json.createArrayBuilder();
        if(nodeTemplatelist == null)
            logger.error("nodeTemplatelist is empty");
        for(NodeTemplateCount process : nodeTemplatelist)
        {
                    
                    nodearray.add(process.getCount());
        }
        JsonObject model = Json.createObjectBuilder()
                .add("name",this.name)
                .add("plugin_name",this.pluginName)
                .add("hadoop_version",this.hadoopVersion)
                .add("node_processes",nodearray)
                .build();
        
        StringWriter stWriter = new StringWriter();
        try (JsonWriter jsonWriter = Json.createWriter(stWriter)) {
             jsonWriter.writeObject(model);
        }
        
        String jsonData = stWriter.toString();
        System.out.println(jsonData);
        
        return jsonData;
        
        
    }

    


    private ArrayList<NodeTemplateCount> nodeTemplatelist;

    public ArrayList<NodeTemplateCount> getNodeTemplatelist() {
        return nodeTemplatelist;
    }

    public void setNodeTemplatelist(ArrayList<NodeTemplateCount> nodeTemplatelist) {
        this.nodeTemplatelist = nodeTemplatelist;
    }


    private String pluginName;
    private String hadoopVersion;
    private String name;
    private String id;
    private String description;

    private HashMap generalParameters;
    private HashMap hDFSParameters;
    private HashMap mapReduceParameters;
    
    
}
