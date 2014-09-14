/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package de.gwdg.portlet.hadoopcluster.model;

import com.fasterxml.jackson.databind.JsonNode;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;

/**
 *
 * @author dikshith
 */
public class Image implements Serializable{
    String status;
    String username;
    String name;
    ArrayList<String> tags;
    String id;
    
    public void load(JsonNode json)
    {
        setUsername(json.get("username").asText());
        setName(json.get("name").asText());
        setStatus(json.get("status").asText());
        setId(json.get("id").asText());
        ArrayList<String> t= new ArrayList<>();
        JsonNode node_groups=json.get("tags");
        Iterator<JsonNode> elements =node_groups.elements();
        while(elements.hasNext())
        {
            JsonNode node=elements.next();
            t.add(node.asText());
            
        }
        setTags(t);
        
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ArrayList<String> getTags() {
        return tags;
    }

    public void setTags(ArrayList<String> tags) {
        this.tags = tags;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
    
    
}
