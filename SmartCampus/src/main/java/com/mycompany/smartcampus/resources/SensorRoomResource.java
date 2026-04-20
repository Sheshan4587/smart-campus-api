/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.smartcampus.resources;

import com.mycompany.smartcampus.DataStore;
import com.mycompany.smartcampus.exception.RoomNotEmptyException;
import com.mycompany.smartcampus.model.Room;
import java.util.ArrayList;
import java.util.HashMap;

import java.util.List;
import java.util.Map;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
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
    public Response getAllrooms() {

        // GET /api/v1/rooms
        // Returns a list of all rooms
        List<Room> roomList = new ArrayList<>(store.getRooms().values());
        return Response.ok(roomList).build(); //200 OK
    }

    @POST
    public Response createRoom(Room room) {

        // Check id was provided
        if (room == null || room.getId() == null || room.getId().isBlank()) {
            Map<String, String> err = new HashMap<>();
            err.put("error", "Field 'id' is required.");
            return Response.status(Response.Status.BAD_REQUEST).entity(err).build(); // 400
        }

        // Check room doesn't already exist
        if (store.getRooms().containsKey(room.getId())) {
            Map<String, String> err = new HashMap<>();
            err.put("error", "Room '" + room.getId() + "' already exists.");
            return Response.status(Response.Status.CONFLICT).entity(err).build(); // 409
        }

        // Make sure sensorIds list is not null
        if (room.getSensorIds() == null) {
            room.setSensorIds(new ArrayList<>());
        }

        // Save and return the created room
        store.getRooms().put(room.getId(), room);
        return Response.status(Response.Status.CREATED).entity(room).build(); // 201
    }
    
    // GET /api/v1/rooms/{roomId}
    // Returns a specific room by its ID
    // --------------------------------------------------------- //

    @GET
    @Path("/{roomId}")
    public Response getRoom(@PathParam("roomId") String roomId) {
        Room room = store.getRooms().get(roomId);

        // Return 404 if room not found
        if (room == null) {
            Map<String, String> err = new HashMap<>();
            err.put("error", "Room '" + roomId + "' not found.");
            return Response.status(Response.Status.NOT_FOUND).entity(err).build(); // 404
        }

        return Response.ok(room).build(); // 200 OK
    }

    // --------------------------------------------------------- //
    // DELETE /api/v1/rooms/{roomId}
    // Deletes a room — blocked if sensors are still assigned to it
    @DELETE
    @Path("/{roomId}")
    public Response deleteRoom(@PathParam("roomId") String roomId) {
        Room room = store.getRooms().get(roomId);

        // Return 404 if room doesn't exist
        if (room == null) {
            Map<String, String> err = new HashMap<>();
            err.put("error", "Room '" + roomId + "' not found.");
            return Response.status(Response.Status.NOT_FOUND).entity(err).build(); // 404
        }

        // Block deletion if room still has sensors assigned
        if (room.getSensorIds() != null && !room.getSensorIds().isEmpty()) {
            throw new RoomNotEmptyException(
                    "Room '" + roomId + "' cannot be deleted: it still has "
                    + room.getSensorIds().size() + " sensor(s) assigned. "
                    + "Please remove all sensors first."
            );
        }

        store.getRooms().remove(roomId);
        return Response.noContent().build(); // 204 No Content
    }
}
