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
public class LinkedResourceNotFoundExceptionMapper implements ExceptionMapper<LinkedResourceNotFoundException>{
    
    @Override
    public Response toResponse(LinkedResourceNotFoundException exception) {
        
        // Create an instance of ErrorMessage class
        ErrorMessage errorMessage = new ErrorMessage(
            exception.getMessage(),
            422,
            "Error!!!. Ensure the referenced roomId exists before creating a sensor."
        );
        
        // Build the response entity
        return Response.status(422)
                .entity(errorMessage)
                .build();
    }
  
    
}
