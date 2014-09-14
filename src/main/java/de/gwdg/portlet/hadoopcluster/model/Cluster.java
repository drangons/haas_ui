/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package de.gwdg.portlet.hadoopcluster.model;

import com.fasterxml.jackson.databind.JsonNode;
import de.gwdg.portlet.hadoopcluster.ui.controller.NodeTemplateCount;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author dikshith
 */
public class Cluster implements Serializable{
    
    String name;
    String id;
    String HadoopVersion;
    String pluginName;
    String templateName;
    
    String image;
    String neutronNetwork;
    String keypair;
    
    
    int instanceCount;
    
    
    
    
    
    String nameNode;
    String hdfsUI;
    String jobFlowUI;
    String mapReduceUI;
    String jobTracker;
    
    ArrayList<NodeTemplateCount> nodeGroup;

    public ArrayList<NodeTemplateCount> getNodeGroup() {
        return nodeGroup;
    }

    public void setNodeGroup(ArrayList<NodeTemplateCount> nodeGroup) {
        this.nodeGroup = nodeGroup;
    }
    
    ArrayList<Instances> instances;
    
    private static final Logger logger=
            LoggerFactory.getLogger(Cluster.class);
    
    public void load(JsonNode json)
    {
        logger.info("Inside Cluster load");
        setImage(json.get("default_image_id").asText());
        setPluginName(json.get("plugin_name").asText());
        setId(json.get("id").asText());
        setHadoopVersion(json.get("hadoop_version").asText());
        
        //JsonNode cluster=json.get("cluster");
        setName(json.get("name").asText());
        JsonNode node_groups=json.get("node_groups");
        Iterator<JsonNode> elements =node_groups.elements();
        ArrayList<Instances> ti= new ArrayList<Instances>();
        ArrayList<NodeTemplateCount> tn= new ArrayList<NodeTemplateCount>();
        while(elements.hasNext())
        {
            JsonNode node=elements.next();
            logger.info("node group node"+node);
            NodeTemplateCount t=new NodeTemplateCount();
            t.setCount(node.get("count").asInt());
            t.setNode(node.get("name").asText());
            //nodeGroup.add(t);
            tn.add(t);
            JsonNode ins=node.get("instances");
            Iterator<JsonNode> itr =ins.elements();
            while(itr.hasNext())
            {
                Instances i=new Instances();
                JsonNode n=itr.next();
                i.setName(n.get("instance_name").asText());
                i.setInternalIP(n.get("internal_ip").asText());
                i.setManagementIP(n.get("management_ip").asText());
                //instances.add(i);
                ti.add(i);

            }
            
            
        }
        JsonNode info=json.get("info");
        
        setNodeGroup(tn);
        setInstances(ti);
        
        
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getInstanceCount() {
        return instanceCount;
    }

    public void setInstanceCount(int instanceCount) {
        this.instanceCount = instanceCount;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPluginName() {
        return pluginName;
    }

    public void setPluginName(String pluginName) {
        this.pluginName = pluginName;
    }

    public String getHadoopVersion() {
        return HadoopVersion;
    }

    public void setHadoopVersion(String HadoopVersion) {
        this.HadoopVersion = HadoopVersion;
    }

    public String getTemplateName() {
        return templateName;
    }

    public void setTemplateName(String templateName) {
        this.templateName = templateName;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getNeutronNetwork() {
        return neutronNetwork;
    }

    public void setNeutronNetwork(String neutronNetwork) {
        this.neutronNetwork = neutronNetwork;
    }

    public String getKeypair() {
        return keypair;
    }

    public void setKeypair(String keypair) {
        this.keypair = keypair;
    }

    public String getHdfsUI() {
        return hdfsUI;
    }

    public void setHdfsUI(String hdfsUI) {
        this.hdfsUI = hdfsUI;
    }

    public String getJobFlowUI() {
        return jobFlowUI;
    }

    public void setJobFlowUI(String jobFlowUI) {
        this.jobFlowUI = jobFlowUI;
    }

    public String getMapReduceUI() {
        return mapReduceUI;
    }

    public void setMapReduceUI(String mapReduceUI) {
        this.mapReduceUI = mapReduceUI;
    }



    public ArrayList<Instances> getInstances() {
        return instances;
    }

    public void setInstances(ArrayList<Instances> instances) {
        this.instances = instances;
    }
    
    
}
