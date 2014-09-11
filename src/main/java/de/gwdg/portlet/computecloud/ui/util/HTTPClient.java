/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package de.gwdg.portlet.computecloud.ui.util;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.core.util.MultivaluedMapImpl;
import javax.ws.rs.core.MultivaluedMap;

/**
 *
 * @author msrba
 */
public class HTTPClient {

    private static Client client = Client.create();
    private static String token;
    private static String endpoint;

    public static String get(MultivaluedMap queryParams) {
        prepare();

        if (queryParams == null) {
            queryParams = new MultivaluedMapImpl();
        }

        return client.resource(endpoint).queryParams(queryParams)
                .header("Content-Type", "application/json;charset=UTF-8")
                .header("X-Auth-Token", token)
                .get(ClientResponse.class)
                .getEntity(String.class);
    }

    public static String post(String content) {
        prepare();

        return client.resource(endpoint)
                .header("Content-Type", "application/json;charset=UTF-8")
                .header("X-Auth-Token", token)
                .post(ClientResponse.class, content)
                .getEntity(String.class);
    }

    public static String delete() {
        prepare();

        ClientResponse response = client.resource(endpoint)
                .header("Content-Type", "application/json;charset=UTF-8")
                .header("X-Auth-Token", token)
                .delete(ClientResponse.class);

        if (response.hasEntity()) {
            return response.getEntity(String.class);
        }else{
            return "";
        }
    }

    public static String put(String content) {
        prepare();

        return client.resource(endpoint)
                .header("Content-Type", "application/json;charset=UTF-8")
                .header("X-Auth-Token", token)
                .put(ClientResponse.class, content)
                .getEntity(String.class);
    }
    
//    public static String patch(String content) {
//        prepare();
//
//        return client.resource(endpoint)
//                .header("Content-Type", "application/json;charset=UTF-8")
//                .header("X-Auth-Token", token)
//                .patch(ClientResponse.class, content)
//                .getEntity(String.class);
//    }

    public static void setToken(String value) {
        token = value;
    }

    public static void setEndpoint(String value) {
        endpoint = value;
    }

    private static void prepare() throws RuntimeException {
        if (token == null) {
            throw new RuntimeException("Token is null");
        }

        if (endpoint == null) {
            throw new RuntimeException("Endpoint is null");
        }

    }
}