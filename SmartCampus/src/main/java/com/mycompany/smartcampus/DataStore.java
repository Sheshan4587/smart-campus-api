/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.smartcampus;

// Import the Room class from your model package.
import com.mycompany.smartcampus.model.Room;
import com.mycompany.smartcampus.model.Sensor;
import java.util.Map;
// Import ConcurrentHashMap (thread-safe Map implementation).
import java.util.concurrent.ConcurrentHashMap;

/**
 *
 * @author sheshan
 */

public class DataStore {

    // Create a single, static instance of DataStore.
    private static final DataStore INSTANCE = new DataStore();

    // Define a thread-safe Map to store rooms.
    // Keys are Strings (like room IDs or names).
    // Values are Room objects.
    private final Map<String, Room> rooms = new ConcurrentHashMap<>();
    
    // In-memory storage for sensors (key = sensorId, value = Sensor)
    private final Map<String, Sensor> sensors = new ConcurrentHashMap<>();

    private DataStore() {}

    // Public method to access the single DataStore instance.
    public static DataStore getInstance() {
        return INSTANCE;
    }

    // Getter method to access the rooms map to add, retrieve, or modify rooms.
    public Map<String, Room> getRooms() {
        return rooms;
    }
    
    public Map<String, Sensor> getSensors(){
        return sensors;
    }
}