package com.twopi.tutorial.utils;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;

public class AssertUtil {

    public static boolean assertParam(String param) {
        if (param == null || param.equals("")) {
            throw new WebApplicationException(Response.Status.BAD_REQUEST);
        }
        return true;
    }
    
    public static boolean assertField(Object obj) {
        if (obj == null) {
            throw new WebApplicationException(Response.Status.SERVICE_UNAVAILABLE);
        }
        
        return true;
    }
}
