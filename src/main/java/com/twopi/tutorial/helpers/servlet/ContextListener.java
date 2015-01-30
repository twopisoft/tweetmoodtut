package com.twopi.tutorial.helpers.servlet;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import com.twopi.tutorial.utils.Constants;

import java.util.logging.Logger;

/**
 * ServletContext Listener. Performs init/de-init of various resources.
 * @author arshad01
 *
 */
public class ContextListener implements ServletContextListener {

	private final static Logger logger = Logger.getLogger(ContextListener.class.getName());
	
	@Override
	public void contextInitialized(ServletContextEvent sce) {
		ServletContext ctx = sce.getServletContext();
		logger.info(Constants.IDOLAPI_KEY_PARAM+"="+ParamHelper.getParamValue(ctx, Constants.IDOLAPI_KEY_PARAM));
		logger.info(Constants.DB_USER_PARAM+"="+ParamHelper.getParamValue(ctx, Constants.DB_USER_PARAM));
		logger.info(Constants.TWAPI_KEY_PARAM+"="+ParamHelper.getParamValue(ctx, Constants.TWAPI_KEY_PARAM));
		
	}

	@Override
	public void contextDestroyed(ServletContextEvent sce) {
		logger.info("contextDestroyed");
	}

}
