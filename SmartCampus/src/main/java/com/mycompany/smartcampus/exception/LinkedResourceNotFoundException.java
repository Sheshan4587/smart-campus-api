/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.smartcampus.exception;

/**
 *
 * @author sheshan
 */

// Thrown when a sensor references a roomId that doesn't exist
public class LinkedResourceNotFoundException extends RuntimeException{
    
    public LinkedResourceNotFoundException(String message){
        super(message);
    }
    
}
