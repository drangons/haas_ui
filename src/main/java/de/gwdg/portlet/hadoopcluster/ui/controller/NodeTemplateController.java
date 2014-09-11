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
import javax.annotation.PostConstruct;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;


@Named
//@SessionScoped//Is this what u want??
@RequestScoped
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
    
    @Inject
    HadoopBean  bean; 
    
    private  String token;
    //@TODO write init definition
    @PostConstruct
    public void init()
    {
        logger.info("inside init");
        //nodeTemplateList =new ArrayList<NodeTemplate>();
    
    }
//    public class NodeTemplateDataList
//    {
//
//        public String getName() {
//            return name;
//        }
//
//        public void setName(String name) {
//            this.name = name;
//        }
//
//        public String getPlugin() {
//            return plugin;
//        }
//
//        public void setPlugin(String plugin) {
//            this.plugin = plugin;
//        }
//
//        public String getHadoopVersion() {
//            return hadoopVersion;
//        }
//
//        public void setHadoopVersion(String hadoopVersion) {
//            this.hadoopVersion = hadoopVersion;
//        }
//
//        public String getNodeProcesses() {
//            return nodeProcesses;
//        }
//
//        public void setNodeProcesses(String nodeProcesses) {
//            this.nodeProcesses = nodeProcesses;
//        }
//        String name;
//        String plugin;
//        String hadoopVersion;
//        String nodeProcesses;
//    }
    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public NodeTemplate getTempNodeTemplate() {
        return tempNodeTemplate;
    }

    public void setTempNodeTemplate(NodeTemplate tempNodeTemplate) {
        this.tempNodeTemplate = tempNodeTemplate;
    }
    private int i; 
    private static final Logger logger=LoggerFactory.getLogger(NodeTemplateController.class);

    public int getI() {
        return i;
    }

    public void setI(int i) {
        this.i = i;
    }

//    public HashMap<String, NodeTemplate> getNodeTemplateList() {
//        return nodeTemplateList;
//    }
//
//
//
//    public void setNodeTemplateList(HashMap<String, NodeTemplate> nodeTemplateList) {
//        this.nodeTemplateList = nodeTemplateList;
//    }

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
        String TENANT_ID="e8db1678ff104c3397e30293ebdba61a";
        String NODE_GROUP_TEMP_PATH=SAHARA_ENDPOINT + TENANT_ID + "/node-group-templates";
      
        
        
        //logger.info("token"+ token);
        HTTPClient.setEndpoint(NODE_GROUP_TEMP_PATH);
        //logger.info("endpoint"+NODE_GROUP_TEMP_PATH);
        HTTPClient.setToken(bean.getToken());
        try{
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
            logger.error("Something fucked up during node template processing\n"+ e);
        }
        return "";
    }
    
    
}
