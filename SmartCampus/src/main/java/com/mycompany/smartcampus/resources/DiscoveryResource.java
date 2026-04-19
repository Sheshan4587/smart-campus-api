/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.smartcampus.resources;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author sheshan
 */

 
@Path("/")
public class DiscoveryResource {
 
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response discover() {
 
        // API metadata
        Map<String, Object> response = new HashMap<>();
        response.put("name",        "Smart Campus Sensor & Room Management API");
        response.put("version",     "1.0");
        response.put("description", "A RESTful API for managing campus rooms, sensors and sensor readings.");
        response.put("contact",     "admin@smartcampus.ac.uk");
 
        // HATEOAS links — clients can navigate the API from this single entry point
        Map<String, String> links = new HashMap<>();
        links.put("self",    "/api/v1");
        links.put("rooms",   "/api/v1/rooms");
        links.put("sensors", "/api/v1/sensors");
        response.put("_links", links);
 
        return Response.ok(response).build();  // 200 OK
    }
}
