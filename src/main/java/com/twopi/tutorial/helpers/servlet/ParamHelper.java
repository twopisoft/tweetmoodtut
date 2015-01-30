package com.twopi.tutorial.helpers.servlet;

import javax.servlet.ServletContext;

import com.twopi.tutorial.utils.CipherUtil;

/**
 * This class provides a convenient way to read servlet parameters. It
 * also decrypts values for "key" parameters i.e. all parameters whose
 * name ends with ".key".
 * 
 * @author arshad01
 */
public class ParamHelper {

    private final static String KEY_SUFFIX = ".key";
    private final static String ENC_PARAM  = "encrypt.keys";

    private ParamHelper() {

    }

    /**
     * This method is the only publicly accessible method of this class. The
     * value of the paramName is returned. If "encrypt.keys" property is "1"
     * then the value of the parameter whose name end in ".key" is first
     * decrypted.
     * 
     * @param ctx - ServletContext          
     * @param paramName - Parameter Name     
     * @return - Parameter value. Can return null if parameter is not defined.
     */
    public static String getParamValue(ServletContext ctx, String paramName) {
        if (isKey(paramName) && isEncrypted(ctx)) {
            return decryptKeyValue(ctx, paramName);
        }

        return getValue(ctx, paramName);
    }

    private static boolean isKey(String paramName) {
        return paramName.endsWith(KEY_SUFFIX);
    }

    private static boolean isEncrypted(ServletContext ctx) {
        return getValue(ctx, ENC_PARAM).equals("1");
    }

    private static String getValue(ServletContext ctx, String paramName) {
        if (ctx == null) {
            throw new IllegalArgumentException("ServletContext is null");
        }
        return ctx.getInitParameter(paramName);
    }

    private static String decryptKeyValue(ServletContext ctx, String paramName) {
        return CipherUtil.decrypt(getValue(ctx, paramName));
    }

}
