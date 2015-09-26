/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package de.gwdg.portlet.hadoopcluster.ui.controller;

/**
 *
 * @author dikshith
 */
import de.gwdg.portlet.computecloud.ui.util.HTTPClient;
import de.gwdg.portlet.hadoopcluster.model.NodeTemplate;

import java.io.Serializable;
;
import javax.inject.Named;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.databind.JsonNode;
import de.gwdg.portlet.computecloud.ui.util.json.JacksonReader;
import de.gwdg.portlet.hadoopcluster.model.Plugin;
import java.util.ArrayList;
import java.util.HashMap;
import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;

/**
 * Backing bean for page NodeTemplate.xhtml
 * 
 * @author dikshith
 */
@Named
//@SessionScoped//Is this what u want??
@ViewScoped
public class NodeTemplateController implements Serializable{
    //@TODO I think you only need String and the id of the node template 
    //private HashMap<String,NodeTemplate> nodeTemplateList;
    //@SessionScoped
    //private ArrayList<NodeTemplate> nodeTemplateList;

//    public ArrayList<NodeTemplate> getNodeTemplateList() {
//        return nodeTemplateList;
//    }
//
//    public void setNodeTemplateList(ArrayList<NodeTemplate> NodeTemplateList) {
//        //this.nodeTemplateList = NodeTemplateList;
//    }
    
    private NodeTemplate tempNodeTemplate= new NodeTemplate();
    public NodeTemplate getTempNodeTemplate() {
        return tempNodeTemplate;
    }

    public void setTempNodeTemplate(NodeTemplate tempNodeTemplate) {
        this.tempNodeTemplate = tempNodeTemplate;
    }
    
    @Inject
    HadoopBean  bean; 
    
    @PostConstruct
    public void init()
    {
        logger.info("inside init");
        
        
    
    }


    
    
    private static final Logger logger=LoggerFactory.getLogger(NodeTemplateController.class);


    /**
     * Hadoop version corresponding to plugin selected.
     */
    ArrayList<String> hadoopVersions=new ArrayList<String>();

    public ArrayList<String> getHadoopVersions() {
        return hadoopVersions;
    }

    public void setHadoopVersions(ArrayList<String> hadoopVersions) {
        this.hadoopVersions = hadoopVersions;
    }
    /**
     * Callback function to populate hadoopVersion after plugin is selected
     */
    public void onPluginChange()
    {
        
        for(Plugin p : bean.getPlugins())
        {
            logger.info("p.getName().equals(tempNodeTemplate.getPluginName()" + p.getName()+ tempNodeTemplate.getPluginName());
            if(p.getName().equals(tempNodeTemplate.getPluginName()))
            {
                setHadoopVersions(p.getSupportVersions());
            }
        }
    }
    /**
     * Validator to check for unique node template name
     */
    public void checkUniqueNodeTemplateName()
    {
        HashMap<String, NodeTemplate>t=bean.getMaplist();
        FacesMessage msg;
        if(t.containsKey(tempNodeTemplate.getName()))
        {
            msg = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Invalid", "Name already exists");
            logger.error("Node template Name already exists");
            FacesContext.getCurrentInstance().addMessage(null, msg);
        }
        
        
    }
    /**
     * Action : creates a node template
     * @return 
     */
    public String process()
    {
        logger.info("reached process");
        /**
         * @TODO
         * 1. check if the template name is already present.This can be a 
         * validation on the template name field.
         * 2. Construct the JSON representation of the tempNodeTemplate by writing
         * a method in that class.
         * 3. Post the content to sahara.
         * 4. Get the id of the nodetemplate and store it in a list along with
         * its name
         */
        
        //logger.info("nodetemplate" + tempNodeTemplate.toJSON());
        String SAHARA_ENDPOINT="http://10.108.16.13:8386/v1.0/";
        String TENANT_ID="fc0f4b2273444df69638958aaadcc60f";
        String NODE_GROUP_TEMP_PATH=SAHARA_ENDPOINT + TENANT_ID + "/node-group-templates";
      
        
        
        //logger.info("token"+ token);
        HTTPClient.setEndpoint(NODE_GROUP_TEMP_PATH);
        //logger.info("endpoint"+NODE_GROUP_TEMP_PATH);
        HTTPClient.setToken(bean.getToken());
        try{
            logger.info("tempNodeTemplate.toJSON()"+tempNodeTemplate.toJSON());
                String response = HTTPClient.post(tempNodeTemplate.toJSON());
            JsonNode jsonResponse = (new JacksonReader(response)).getJsonNode();
            
            logger.info("json response" + jsonResponse);
            if(jsonResponse.has("node_group_template"))
            {
                //handle error
                //logger.error("handle error from sahara");
                jsonResponse=jsonResponse.get("node_group_template");
                 String id=jsonResponse.get("id").asText();
                 logger.info("1. id of the template " + id );
            tempNodeTemplate.setId(id);
            //NodeTemplate token=new NodeTemplate();
            //NodeTemplate.newInstace(tempNodeTemplate,token);
            
            //bean.nodeTemplateList.add(tempNodeTemplate); 
            bean.add(tempNodeTemplate);
            logger.info(" size of array " + bean.nodeTemplateList.size() );
            //tempNodeTemplate.clear();
            for(NodeTemplate x : bean.nodeTemplateList)  
            {
                logger.info(x.getName());
            }
                
            }
            // Now the bean should contain the fields name, plugin,Hadoop version
            //Node processes. Let me desigbn it as a class and check it out.
            //class is a bad idea, we can handle with tempnodetemplate varable
            
            //get the id 
           
            
            

            
            
        }
        catch(Exception e)
        {
            logger.error("Something went wrong up during node template processing\n"+ e);
        }
        return "";
    }
    /**
     * Deletes a node template
     * @param name Name of the node template
     */
    public void deleteNodeTemplate(String name)
    {
        bean.deleteNodeTemplate(name);
    }
    /**
     * Validator for node template
     */
    public void validateNodeTemplate()
    {
        if( tempNodeTemplate.getName()=="" || 
                tempNodeTemplate.getProcesses()== null ||
                tempNodeTemplate.getPluginName() == "" ||
                tempNodeTemplate.getHadoopVersion()=="" 
                )
        {
            logger.error("Node Template is missing a mandatory parameter");
        }
    }
    @Deprecated
    public void nodeProcessListener()
    {
        logger.info("Inside nodeProcessListener"+tempNodeTemplate.getProcesses().length);
        if(tempNodeTemplate.getProcesses().length == 0)
        {
            logger.error("processes list is empty");
        }
    }
    /**
     * Stores node process based on plugin and hadoop version selected.
     */
    ArrayList<String> p= 
            new ArrayList<String>();

    public ArrayList<String> getP() {
        return p;
    }

    public void setP(ArrayList<String> p) {
        this.p = p;
    }
    /**
     * Updates process list in NodeTemplate.xhtml based on plugin and hadoop version
     * selected.
     */
    public void updateNodeProcess()
    {
        p.clear();
        logger.info(" inside updateNodeProcess"+tempNodeTemplate.getHadoopVersion());
        logger.info("inside updateNodeProcess"+tempNodeTemplate.getPluginName());
        if (tempNodeTemplate.getPluginName().equals("vanilla")) {
            if (tempNodeTemplate.getHadoopVersion().equals("1.2.1")) {
                p.add("namenode");
                p.add("datanode");
                p.add("secondarynamenode");
                p.add("oozie");
                p.add("tasktracker");
                p.add("jobtracker");
                p.add("hiveserver");
                
            } else {
                p.add("namenode");
                p.add("datanode");
                p.add("resourcemanager");
                p.add("oozie");
                p.add("nodemanager");
                p.add("historyserver");
            }
        }
    }
}
