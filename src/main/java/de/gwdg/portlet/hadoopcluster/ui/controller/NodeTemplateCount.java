/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package de.gwdg.portlet.hadoopcluster.ui.controller;

import java.io.Serializable;

/**
 *
 * @author dikshith
 */
public class NodeTemplateCount implements  Serializable{
    
    int count;
    String node;

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public String getNode() {
        return node;
    }

    public void setNode(String node) {
        this.node = node;
    }
    
}
