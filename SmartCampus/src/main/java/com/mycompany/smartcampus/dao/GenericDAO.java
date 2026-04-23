/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.smartcampus.dao;

import com.mycompany.smartcampus.model.BaseModel;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 *
 * @author sheshan
 */
public class GenericDAO<T extends BaseModel>{
    
    // Internal "table" represented as a Map (key = id, value = entity)
    private final Map<String, T> table;

    // Constructor: injects the Map that acts as the storage backend
    public GenericDAO(Map<String, T> table) {
        this.table = table;
    }

    // Save or update an entity in the table
    public void save(T entity) {
        table.put(entity.getId(), entity); // Uses entity's id as the key
    }

    // Retrieve an entity by its id
    public T getById(String id) {
        return table.get(id); // Returns null if not found
    }

    // Retrieve all entities as a List
    public List<T> getAll() {
        return new ArrayList<>(table.values()); // Converts Map values into a List
    }

    // Delete an entity by its id
    public void delete(String id) {
        table.remove(id); // Removes entry if it exists
    }
    
    // Check if an entity exists by id
    public boolean exists(String id) {
        return table.containsKey(id); // Returns true if id is present
    }
    
}
