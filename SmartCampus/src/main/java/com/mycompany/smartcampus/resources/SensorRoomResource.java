/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.smartcampus.resources;

import com.mycompany.smartcampus.DataStore;
import com.mycompany.smartcampus.exception.RoomNotEmptyException;
import com.mycompany.smartcampus.model.ErrorMessage;
import com.mycompany.smartcampus.model.Room;
import com.mycompany.smartcampus.model.Sensor;
import java.util.ArrayList;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 *
 * @author sheshan
 */
@Path("/rooms")
public class SensorRoomResource {

    public static final DataStore store = DataStore.getInstance();

    //get all rooms
    @GET
    public Response getAllRooms() {
        return Response.ok(store.roomDAO.getAll()).build();
    }

    @POST
    public Response createRoom(Room room) {

        // Check id was provided
        if (room == null || room.getId() == null || room.getId().isBlank()) {
            ErrorMessage errorObj = new ErrorMessage(
                    "Field 'id' is required.",
                    400,
                    "Please provide a valid Room JSON payload containing at least an 'id'."
            );

            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(errorObj)
                    .type(MediaType.APPLICATION_JSON)
                    .build(); // 400
        }

        // Check room doesn't already exist
        if (store.roomDAO.exists(room.getId())) {
            ErrorMessage errorObj = new ErrorMessage(
                    "Room '" + room.getId() + "' already exists.",
                    409,
                    "Room IDs must be unique. Please use a different ID or update the existing room."
            );

            return Response.status(Response.Status.CONFLICT)
                    .entity(errorObj)
                    .type(MediaType.APPLICATION_JSON)
                    .build(); // 409
        }

        // Make sure sensorIds list is not null
        if (room.getSensorIds() == null) {
            room.setSensorIds(new ArrayList<>());
        }

        // Save and return the created room using the DAO
        store.roomDAO.save(room);

        return Response.status(Response.Status.CREATED)
                .entity(room)
                .type(MediaType.APPLICATION_JSON)
                .build(); // 201
    }

    // GET /api/v1/rooms/{roomId}
    // Returns a specific room by its ID
    // --------------------------------------------------------- //
    @GET
    @Path("/{roomId}")
    public Response getRoomById(@PathParam("roomId") String roomId) {
        Room room = store.roomDAO.getById(roomId);
        // Return a clean 404 error if room is not found
        if (room == null) {
            ErrorMessage errorObj = new ErrorMessage(
                    "Room not found.",
                    404,
                    "No room exists with the ID: " + roomId
            );
            return Response.status(Response.Status.NOT_FOUND)
                    .entity(errorObj)
                    .type(MediaType.APPLICATION_JSON)
                    .build();
        }

        return Response.ok(room).build();
    }

    // --------------------------------------------------------- //
    // DELETE /api/v1/rooms/{roomId}
    // Deletes a room — blocked if sensors are still assigned to it
    @DELETE
    @Path("/{roomId}")
    public Response deleteRoom(@PathParam("roomId") String roomId) {
        Room room = store.roomDAO.getById(roomId);
        // Return a clean 404 error if room doesn't exist
        if (room == null) {
            ErrorMessage errorObj = new ErrorMessage(
                    "Room not found.",
                    404,
                    "Cannot delete a room that does not exist."
            );
            return Response.status(Response.Status.NOT_FOUND)
                    .entity(errorObj)
                    .type(MediaType.APPLICATION_JSON)
                    .build();
        }

        // 409 Conflict check for active sensors
        for (String sensorId : room.getSensorIds()) {
            Sensor sensor = store.sensorDAO.getById(sensorId);
            if (sensor != null && "ACTIVE".equalsIgnoreCase(sensor.getStatus())) {
                // Throws Exception -> Exception Mapper catches it and formats to JSON!
                throw new RoomNotEmptyException("Cannot delete: Room is currently occupied by active hardware.");
            }
        }

        store.roomDAO.delete(roomId);
        return Response.noContent().build(); // 204 No Content
    }
}
