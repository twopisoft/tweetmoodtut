package com.twopi.tutorial.helpers.servlet;

import java.sql.Connection;
import java.util.logging.Logger;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import com.twopi.tutorial.helpers.db.DBHelper;
import com.twopi.tutorial.utils.Constants;

/**
 * ServletContext Listener. Performs init/de-init of various resources.
 * 
 * @author arshad01
 *
 */
public class ContextListener implements ServletContextListener {

    private final static Logger logger = Logger.getLogger(ContextListener.class.getName());

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        logger.info("contextInitialized");

        ServletContext ctx = sce.getServletContext();

        String dbClass = ParamHelper.getParamValue(ctx,
                Constants.DB_DRV_CLASS_PARAM);
        String dbUrl = ParamHelper.getParamValue(ctx, Constants.DB_URL_PARAM);
        String dbUser = ParamHelper.getParamValue(ctx, Constants.DB_USER_PARAM);
        String dbPwd = ParamHelper.getParamValue(ctx, Constants.DB_PWD_PARAM);

        logger.info("Opening DB Connection");
        Connection dbConnection = DBHelper.openConnection(dbClass, dbUrl,dbUser, dbPwd);
        ctx.setAttribute(Constants.DB_CONNECTION_PARAM, dbConnection);

    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        logger.info("contextDestroyed");

        logger.info("Closing DB Connection");
        DBHelper.closeConnection();
    }

}
