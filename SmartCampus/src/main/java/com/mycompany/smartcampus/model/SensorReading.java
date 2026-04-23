/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.smartcampus.model;

import java.util.UUID;

/**
 *
 * @author sheshan
 */
public class SensorReading implements BaseModel{

    private String id; // Unique reading event ID (UUID recommended)
    private long timestamp; // Epoch time (ms) when the reading was captured
    private double value; // The actual metric value recorded by the hardware

    
    public SensorReading() {
        this.id = UUID.randomUUID().toString();
        this.timestamp = System.currentTimeMillis();
    }

    // Auto-generate id and timestamp - used when saving a new reading
    public SensorReading(double value) {
        this();
        this.value = value;
    }
    
    @Override
    public String getId() {
        return id;
    }
    
    @Override
    public void setId(String id) {
        this.id = id;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public double getValue() {
        return value;
    }

    public void setValue(double value) {
        this.value = value;
    }

}
