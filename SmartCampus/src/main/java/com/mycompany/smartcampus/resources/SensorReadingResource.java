/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.smartcampus.resources;

import com.mycompany.smartcampus.DataStore;
import com.mycompany.smartcampus.model.Sensor;
import com.mycompany.smartcampus.model.SensorReading;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 *
 * @author sheshan
 */
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class SensorReadingResource {
    
    private final String sensorId;
    private final DataStore store;

    public SensorReadingResource(String sensorId, DataStore store) {
        this.sensorId = sensorId;
        this.store = store;
    }
    
    // GET /api/v1/sensors/{sensorId}/readings
    // Returns full reading history for this sensor
    @GET
    public Response getReadings(){
        List<SensorReading> history = store.getReadings().get(sensorId);
        
        // If no readings exist yet return empty list
        if (history == null) {
            return Response.ok(new ArrayList<>()).build(); // 200 OK
        }

        return Response.ok(history).build(); // 200 OK
        
    }
    
    // POST /api/v1/sensors/{sensorId}/readings
    // Appends a new reading for this sensor
    // Side effect: updates currentValue on the parent Sensor
    @POST
    public Response addReading(SensorReading reading) {

        // Validate reading body was provided
        if (reading == null) {
            Map<String, String> err = new HashMap<>();
            err.put("error", "Request body is required.");
            return Response.status(Response.Status.BAD_REQUEST).entity(err).build(); // 400
        }

        // Build a new reading with auto generated id and timestamp
        SensorReading saved = new SensorReading(reading.getValue());

        // Add reading to history list, create list if first reading
        store.getReadings()
             .computeIfAbsent(sensorId, k -> new ArrayList<>())
             .add(saved);

        // Side effect - update currentValue on the parent sensor
        Sensor sensor = store.getSensors().get(sensorId);
        sensor.setCurrentValue(saved.getValue());

        return Response.status(Response.Status.CREATED).entity(saved).build(); // 201
    }
    
}
