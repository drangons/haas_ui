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
public class Plugin implements Serializable{
    String name;
    String title;
    String description;
    ArrayList<String> supportVersions;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public ArrayList<String> getSupportVersions() {
        return supportVersions;
    }

    public void setSupportVersions(ArrayList<String> supportVersions) {
        this.supportVersions = supportVersions;
    }
    
    public void load(JsonNode json)
    {
        setName(json.get("name").asText());
        setTitle(json.get("title").asText());
        setDescription(json.get("description").asText());
        JsonNode version=json.get("versions");
        Iterator<JsonNode> elements =version.elements();
        ArrayList<String> t=
                new ArrayList<String>();
        while (elements.hasNext()) {
            JsonNode Node=elements.next();
            t.add(Node.asText());
            
        }
        setSupportVersions(t);
        
    }
    
    
}
