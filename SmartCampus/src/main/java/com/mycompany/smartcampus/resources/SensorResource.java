/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.smartcampus.resources;

import com.mycompany.smartcampus.DataStore;
import com.mycompany.smartcampus.model.Room;
import com.mycompany.smartcampus.model.Sensor;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 *
 * @author sheshan
 */
@Path("/sensors")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class SensorResource {

    private final DataStore store = DataStore.getInstance();

    // GET /api/v1/sensors
    // GET /api/v1/sensors?type=CO2  (optional filter by type)
    @GET
    public Response getallSensors(@QueryParam("type") String type) {
        List<Sensor> list = new ArrayList<>(store.getSensors().values());

        // If type query param is provided, filter the list by type
        if (type != null && !type.isEmpty()) {
            list = list.stream().filter(s -> s.getType().equalsIgnoreCase(type)).collect(Collectors.toList());
        }
        return Response.ok(list).build();
    }
    
    // POST /api/v1/sensors
    // Registers a new sensor
    // Validates that the roomId provided actually exists
    
    @POST
    public Response createSensor(Sensor sensor){
        
        // Check id was provided
        if (sensor == null || sensor.getId() == null || sensor.getId().isEmpty()){
            Map<String, String> err = new HashMap<>();
            err.put("error", "Field 'id' is required.");
            return Response.status(Response.Status.BAD_REQUEST).entity(err).build(); //400
        }
        // Check sensor doesn't already exist
        if (store.getSensors().containsKey(sensor.getId())) {
            Map<String, String> err = new HashMap<>();
            err.put("error", "Sensor '" + sensor.getId() + "' already exists.");
            return Response.status(Response.Status.CONFLICT).entity(err).build(); // 409
        }
        
        // Check roomId was provided
        if (sensor.getRoomId() == null || sensor.getRoomId().isEmpty()){
            Map<String, String> err = new HashMap<>();
            err.put("error", "Field 'roomId' is required.");
            return Response.status(Response.Status.BAD_REQUEST).entity(err).build();
        }
        
        // Validate that the referenced room actually exists
        Room room = store.getRooms().get(sensor.getRoomId());
        if (room == null) {
            Map<String, String> err = new HashMap<>();
            err.put("error", "roomId '" + sensor.getRoomId() + "' does not exist. Create the room first.");
            return Response.status(422).entity(err).build(); // 422 Unprocessable Entity
        }

        // Default status to ACTIVE if not provided
        if (sensor.getStatus() == null || sensor.getStatus().isEmpty()) {
            sensor.setStatus("ACTIVE");
        }

        // Save sensor to store
        store.getSensors().put(sensor.getId(), sensor);

        // Link sensor ID into the room's sensorIds list
        room.getSensorIds().add(sensor.getId());

        return Response.status(Response.Status.CREATED).entity(sensor).build(); // 201
    }

    // GET /api/v1/sensors/{sensorId}
    // Returns a specific sensor by its ID
    
    @GET
    @Path("/{sensorId}")
    public Response getSensor(@PathParam("sensorId") String sensorId) {
        Sensor sensor = store.getSensors().get(sensorId);

        if (sensor == null) {
            Map<String, String> err = new HashMap<>();
            err.put("error", "Sensor '" + sensorId + "' not found.");
            return Response.status(Response.Status.NOT_FOUND).entity(err).build(); // 404
        }

        return Response.ok(sensor).build(); // 200 OK
    }
}

