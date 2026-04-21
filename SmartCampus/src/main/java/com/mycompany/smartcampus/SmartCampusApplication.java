/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.smartcampus;

import com.mycompany.smartcampus.exception.RoomNotEmptyExceptionMapper;
import com.mycompany.smartcampus.resources.DiscoveryResource;
import com.mycompany.smartcampus.resources.SensorResource;
import com.mycompany.smartcampus.resources.SensorRoomResource;
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
        
        
        
        //part 1
        classes.add(DiscoveryResource.class);
        
        //part 2 - Room Management
        classes.add(SensorRoomResource.class);
        classes.add(RoomNotEmptyExceptionMapper.class);
        
        // Part 3 - Sensor Operations
        classes.add(SensorResource.class);
        
        return classes;
    }
    
    
}
