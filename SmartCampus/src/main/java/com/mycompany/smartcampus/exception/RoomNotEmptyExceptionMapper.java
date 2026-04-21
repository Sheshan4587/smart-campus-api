/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.smartcampus.exception;

import java.util.HashMap;
import java.util.Map;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;


/**
 *
 * @author sheshan
 */
@Provider
public class RoomNotEmptyExceptionMapper implements ExceptionMapper<RoomNotEmptyException>{
    
    @Override
    public Response toResponse(RoomNotEmptyException ex) {
        // Build a simple JSON error body
        Map<String, String> err = new HashMap<>();
        err.put("error", ex.getMessage());

        return Response
                .status(Response.Status.CONFLICT)   // 409
                .type(MediaType.APPLICATION_JSON)
                .entity(err)
                .build();
    }
    
}
