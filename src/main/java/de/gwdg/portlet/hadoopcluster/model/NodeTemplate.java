/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package de.gwdg.portlet.hadoopcluster.model;

import com.fasterxml.jackson.databind.JsonNode;
import java.io.Serializable;
import javax.faces.model.SelectItem;
import javax.json.Json;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObject;

import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import javax.json.JsonWriter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
/**
 *
 * @author dikshith
 */
public class NodeTemplate implements Serializable {
    
    private static final Logger logger=LoggerFactory.getLogger(
            NodeTemplate.class);
    
    public void load(JsonNode json)
    {
        setPluginName(json.get("plugin_name").asText());
        setName(json.get("name").asText());
        setId(json.get("id").asText()); 
        JsonNode node_processes=json.get("node_processes");
        Iterator<JsonNode> elements =node_processes.elements();
        List<String> p=new ArrayList<String>();
        
        while(elements.hasNext())
        {
            
            p.add(elements.next().asText());       
        }
        String[] temp =new String[p.size()];
        p.toArray(temp);
        setProcesses(temp);
        setHadoopVersion(json.get("hadoop_version").asText());
        
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
        logger.info("Set the hadoop version in Nodetemplate");
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

    public void setId(String id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getOpenstackFlavour() {
        return openstackFlavour;
    }

    public void setOpenstackFlavour(String openstackFlavour) {
        this.openstackFlavour = openstackFlavour;
    }

    public String getStorageLocation() {
        return storageLocation;
    }

    public void setStorageLocation(String storageLocation) {
        this.storageLocation = storageLocation;
    }

    public String getFloatingIpPool() {
        return floatingIpPool;
    }

    public void setFloatingIpPool(String floatingIpPool) {
        this.floatingIpPool = floatingIpPool;
    }
    public String[] getProcesses() {
        return processes;
    }

   public void setProcesses(String[] processes) {
        this.processes = processes;
    }

    public String toJSON()
    {
        /*
        {
    "name": "test-master-tmpl",
    "flavor_id": "2",
    "plugin_name": "vanilla",
    "hadoop_version": "1.2.1",
    "node_processes": ["jobtracker", "namenode"]
}
        */
        logger.info("Processes length" + processes.length);
        JsonArrayBuilder processarray =Json.createArrayBuilder();
        for(String process : processes)
        {
            
                    processarray.add(process);
        }
            
        JsonObject model = Json.createObjectBuilder()
                .add("name",this.name)
                .add("flavor_id","2")
                .add("plugin_name",this.pluginName)
                .add("hadoop_version",this.hadoopVersion)
                .add("floating_ip_pool",this.getFloatingIpPool())
                .add("node_processes",processarray)
                .build();
        
        StringWriter stWriter = new StringWriter();
        try (JsonWriter jsonWriter = Json.createWriter(stWriter)) {
             jsonWriter.writeObject(model);
        }
        
        String jsonData = stWriter.toString();
        System.out.println(jsonData);
        
        return jsonData;
                     
    }
    public void clear()
    {
      pluginName= hadoopVersion=name= id= description =openstackFlavour="";
     storageLocation= floatingIpPool="";
    }

    @Override
    public boolean equals(Object obj)
    {
        if(this.getName().equals((((NodeTemplate)obj).getName())))
            return true;
        else
            return false;
    }
    private String pluginName;
    private String hadoopVersion;
    private String name;
    private String id;
    private String description;
    private String openstackFlavour;
    private String storageLocation;
    private String floatingIpPool;
    private String[] processes;



            
    
}
