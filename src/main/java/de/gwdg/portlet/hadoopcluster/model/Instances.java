/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package de.gwdg.portlet.hadoopcluster.model;

import java.io.Serializable;

/**
 *
 * @author dikshith
 */
public class Instances implements Serializable{
    String name;
    String internalIP;
    String managementIP;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getInternalIP() {
        return internalIP;
    }

    public void setInternalIP(String internalIP) {
        this.internalIP = internalIP;
    }

    public String getManagementIP() {
        return managementIP;
    }

    public void setManagementIP(String managementIP) {
        this.managementIP = managementIP;
    }
    
    
}
