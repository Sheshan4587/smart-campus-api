/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.smartcampus.filter;

import java.io.IOException;
import java.util.logging.Logger;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.container.ContainerResponseContext;
import javax.ws.rs.container.ContainerResponseFilter;
import javax.ws.rs.ext.Provider;

/**
 *
 * @author sheshan
 */
@Provider // This tells the server to register this filter
public class LoggingFilter implements ContainerRequestFilter, ContainerResponseFilter {

    // 1. Initialize the Logger
    private static final Logger LOG = Logger.getLogger(LoggingFilter.class.getName());

    // Runs before every request hits a resource method
    @Override
    public void filter(ContainerRequestContext requestContext) throws IOException {
        
        // 2. Log the incoming request
        LOG.info(String.format("[REQUEST]  %s %s",
                requestContext.getMethod(),
                requestContext.getUriInfo().getRequestUri()));
    }

    // Runs after every response leaves a resource method
    @Override
    public void filter(ContainerRequestContext requestContext,
            ContainerResponseContext responseContext) throws IOException {
        
        // 3. Log the outgoing response
        LOG.info(String.format("[RESPONSE] %s %s -> HTTP %d",
                requestContext.getMethod(),
                requestContext.getUriInfo().getRequestUri(),
                responseContext.getStatus()));
    }
}
