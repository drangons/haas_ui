/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package de.gwdg.portlet.hadoopcluster.convertors;

import de.gwdg.portlet.hadoopcluster.model.NodeTemplate;
import de.gwdg.portlet.hadoopcluster.ui.controller.HadoopBean;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author dikshith
 */
@FacesConverter(value="nodeTemplateConvertor")
public class NodeTemplateConvertor implements Converter{
    private static final Logger logger=LoggerFactory.getLogger(NodeTemplateConvertor.class);
    @Override
    public Object getAsObject(FacesContext context, UIComponent component,
            String value) {
        if (value == null) {
            return null;
        }
        HadoopBean controller = context.getApplication()
                .evaluateExpressionGet(context, "#{hadoopBean}",
                        HadoopBean.class);
        NodeTemplate country = controller.getNodeTemplate(value);
        return country;
    }
 
    @Override
    public String getAsString(FacesContext context, UIComponent component,
            Object value) {
        logger.info("inside getAsString");
        
       // logger.info("classtype"+ value.getClass());
        
        String string = null;
        if (value instanceof NodeTemplate) {
            string = ((NodeTemplate) value).getName();
        }
        return string;
    }
    
}
