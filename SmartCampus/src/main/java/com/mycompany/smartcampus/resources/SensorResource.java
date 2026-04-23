/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.smartcampus.resources;

import com.mycompany.smartcampus.DataStore;
import com.mycompany.smartcampus.exception.LinkedResourceNotFoundException;
import com.mycompany.smartcampus.model.ErrorMessage;
import com.mycompany.smartcampus.model.Room;
import com.mycompany.smartcampus.model.Sensor;
import java.util.List;
import java.util.stream.Collectors;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.WebApplicationException;
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
        // Using DAO to get all sensors
        List<Sensor> list = store.sensorDAO.getAll();

        // If type query param is provided, filter the list by type
        if (type != null && !type.trim().isEmpty()) {
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
        if (sensor == null || sensor.getId() == null || sensor.getId().trim().isEmpty()){
            ErrorMessage errorObj = new ErrorMessage(
                    "Field 'id' is required.", 
                    400, 
                    "Please provide a valid Sensor JSON payload containing at least an 'id'."
            );
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(errorObj)
                    .type(MediaType.APPLICATION_JSON)
                    .build(); //400
        }
        
        // Check sensor doesn't already exist using DAO
        if (store.sensorDAO.exists(sensor.getId())) {
            ErrorMessage errorObj = new ErrorMessage(
                    "Sensor '" + sensor.getId() + "' already exists.", 
                    409, 
                    "Sensor IDs must be unique."
            );
            return Response.status(Response.Status.CONFLICT)
                    .entity(errorObj)
                    .type(MediaType.APPLICATION_JSON)
                    .build(); // 409
        }
        
        // Check roomId was provided
        if (sensor.getRoomId() == null || sensor.getRoomId().trim().isEmpty()){
            ErrorMessage errorObj = new ErrorMessage(
                    "Field 'roomId' is required.", 
                    400, 
                    "A sensor must be assigned to an existing room."
            );
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(errorObj)
                    .type(MediaType.APPLICATION_JSON)
                    .build(); // 400
        }
        
        // Validate that the referenced room actually exists using DAO and Custom Exception
        if (!store.roomDAO.exists(sensor.getRoomId())) {
            // This throw triggers the LinkedResourceNotFoundExceptionMapper to send a 422!
            throw new LinkedResourceNotFoundException("roomId '" + sensor.getRoomId() + "' does not exist. Create the room first.");
        }

        // Default status to ACTIVE if not provided
        if (sensor.getStatus() == null || sensor.getStatus().trim().isEmpty()) {
            sensor.setStatus("ACTIVE");
        }

        // Save sensor to store using DAO
        store.sensorDAO.save(sensor);

        // Link sensor ID into the room's sensorIds list using DAO
        Room room = store.roomDAO.getById(sensor.getRoomId());
        room.getSensorIds().add(sensor.getId());

        return Response.status(Response.Status.CREATED)
                .entity(sensor)
                .type(MediaType.APPLICATION_JSON)
                .build(); // 201
    }

    // GET /api/v1/sensors/{sensorId}
    // Returns a specific sensor by its ID
    
    @GET
    @Path("/{sensorId}")
    public Response getSensor(@PathParam("sensorId") String sensorId) {
        // Get sensor by ID using DAO
        Sensor sensor = store.sensorDAO.getById(sensorId);

        if (sensor == null) {
            ErrorMessage errorObj = new ErrorMessage(
                    "Sensor '" + sensorId + "' not found.", 
                    404, 
                    "Verify the sensor ID and try again."
            );
            return Response.status(Response.Status.NOT_FOUND)
                    .entity(errorObj)
                    .type(MediaType.APPLICATION_JSON)
                    .build(); // 404
        }

        return Response.ok(sensor).build(); // 200 OK
    }
    
    // Sub-resource locator
    // Delegates /sensors/{sensorId}/readings to SensorReadingResource
    
    @Path("/{sensorId}/readings")
    public SensorReadingResource getReadingsResource(@PathParam("sensorId") String sensorId) {
        // Check sensor exists first using DAO
        Sensor sensor = store.sensorDAO.getById(sensorId);
        
        if (sensor == null) {
            // Replaced the raw NotFoundException with a perfectly formatted JSON response!
            ErrorMessage errorObj = new ErrorMessage(
                    "Sensor '" + sensorId + "' not found.", 
                    404, 
                    "Cannot access readings for a non-existent sensor."
            );
            Response errorResponse = Response.status(Response.Status.NOT_FOUND)
                    .entity(errorObj)
                    .type(MediaType.APPLICATION_JSON)
                    .build();
            
            throw new WebApplicationException(errorResponse);
        }
        
        // Hand off to SensorReadingResource with the sensorId context
        return new SensorReadingResource(sensorId, store);
    }
}

