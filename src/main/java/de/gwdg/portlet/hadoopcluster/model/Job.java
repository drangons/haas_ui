/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package de.gwdg.portlet.hadoopcluster.model;

import java.io.Serializable;
import java.util.ArrayList;

/**
 *
 * @author dikshith
 */
public class Job implements Serializable{

    public String getName() {
        return Name;
    }

    public void setName(String Name) {
        this.Name = Name;
    }

    public String getJobType() {
        return JobType;
    }

    public void setJobType(String JobType) {
        this.JobType = JobType;
    }

    public String getMainBinary() {
        return MainBinary;
    }

    public void setMainBinary(String MainBinary) {
        this.MainBinary = MainBinary;
    }

    public String getDescription() {
        return Description;
    }

    public void setDescription(String Description) {
        this.Description = Description;
    }

    public ArrayList<JobBinary> getLibsList() {
        return LibsList;
    }

    public void setLibsList(ArrayList<JobBinary> LibsList) {
        this.LibsList = LibsList;
    }
    
    private String Name;
    private String JobType;
    private String MainBinary;
    private String Description;
    private ArrayList<JobBinary> LibsList;
    
}
