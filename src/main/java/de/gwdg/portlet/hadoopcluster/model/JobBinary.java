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
public class JobBinary implements Serializable{

    public String getName() {
        return Name;
    }

    public void setName(String Name) {
        this.Name = Name;
    }

    public String getStorageType() {
        return StorageType;
    }

    public void setStorageType(String StorageType) {
        this.StorageType = StorageType;
    }

    public String getURL() {
        return URL;
    }

    public void setURL(String URL) {
        this.URL = URL;
    }

    public String getUsername() {
        return Username;
    }

    public void setUsername(String Username) {
        this.Username = Username;
    }

    public String getPassword() {
        return Password;
    }

    public void setPassword(String Password) {
        this.Password = Password;
    }

    public String getDescription() {
        return Description;
    }

    public void setDescription(String Description) {
        this.Description = Description;
    }
    
    private String Name;
    private String StorageType;
    private String URL;
    private String Username;
    private String Password;
    private String Description;
    
    
    
}
