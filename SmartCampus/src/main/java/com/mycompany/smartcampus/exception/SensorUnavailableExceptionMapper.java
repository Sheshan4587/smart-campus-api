/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.smartcampus.exception;

import com.mycompany.smartcampus.model.ErrorMessage;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

/**
 *
 * @author sheshan
 */
// Catches SensorUnavailableException and returns 403
@Provider
public class SensorUnavailableExceptionMapper implements ExceptionMapper<SensorUnavailableException>{

    @Override
    public Response toResponse(SensorUnavailableException exception) {
        
        // Create an instance of ErrorMessage class
        ErrorMessage errorMessage = new ErrorMessage(
            exception.getMessage(),
            403,
            "State Constraint Violation. Sensors in MAINTENANCE mode cannot accept new readings."
        );
        
        // Build the response entity
        return Response.status(Response.Status.FORBIDDEN)
                .entity(errorMessage)
                .build();
    }

}
