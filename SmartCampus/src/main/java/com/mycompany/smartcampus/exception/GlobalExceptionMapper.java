/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.smartcampus.exception;

import com.mycompany.smartcampus.model.ErrorMessage;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;

/**
 *
 * @author sheshan
 */
// Catches ANY unhandled exception and returns 500
// Logs the real error on server but never exposes it to the client
public class GlobalExceptionMapper implements ExceptionMapper<Throwable>{
    
    @Override
    public Response toResponse(Throwable exception) {
        
        // Print the real error to the GlassFish/Tomcat console so you can still debug
        exception.printStackTrace(); 
        
        // Hide the real Java error from external users
        ErrorMessage errorMessage = new ErrorMessage(
            "An unexpected internal server error occurred.",
            500,
            "Please check your request payload or contact the server administrator if this persists."
        );
        
        // Build the response entity
        return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                .entity(errorMessage)
                .build();
    }
    
}
