/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.smartcampus;

// Import the Room class from your model package.
import com.mycompany.smartcampus.dao.GenericDAO;
import com.mycompany.smartcampus.model.Room;
import com.mycompany.smartcampus.model.Sensor;
import com.mycompany.smartcampus.model.SensorReading;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
    private final Map<String, Room> rooms = Collections.synchronizedMap(new HashMap<>());
    
    // In-memory storage for sensors (key = sensorId, value = Sensor)
    private final Map<String, Sensor> sensors = Collections.synchronizedMap(new HashMap<>());    
    
    // In-memory storage for readings (key = sensorId, value = list of readings)
    private final Map<String, List<SensorReading>> readings = Collections.synchronizedMap(new HashMap<>());
    
    
    //public dao
    public final GenericDAO<Room> roomDAO = new GenericDAO<>(rooms);
    public final GenericDAO<Sensor> sensorDAO = new GenericDAO<>(sensors);

    private DataStore() {}

    // Public method to access the single DataStore instance.
    public static DataStore getInstance() {
        return INSTANCE;
    }
    
    public Map<String, List<SensorReading>> getReadings(){
        return readings;
    }
}