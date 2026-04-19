/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.smartcampus;

import com.mycompany.smartcampus.resources.DiscoveryResource;
import java.util.HashSet;
import java.util.Set;
import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;


/**
 *
 * @author sheshan
 * Configures Jakarta RESTful Web Services for the application.
 */
@ApplicationPath("/api/v1")
public class SmartCampusApplication extends Application{
      // Overriding the getClasses method 
    @Override 
    public Set<Class<?>> getClasses() {
        
        // Declare anc initlize the HashSet
        Set<Class<?>> classes = new HashSet<>();
        classes.add(DiscoveryResource.class);
        return classes;
    }
    
    
}
