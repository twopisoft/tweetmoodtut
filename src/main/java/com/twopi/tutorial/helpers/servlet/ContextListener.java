package com.twopi.tutorial.helpers.servlet;

import java.sql.Connection;
import java.util.logging.Logger;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import com.twopi.tutorial.helpers.db.DBHelper;
import com.twopi.tutorial.helpers.idol.IDOLServiceHelper;
import com.twopi.tutorial.helpers.twitter.TwitterHelper;
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

        // Open DB Connection and set in as context attribute
        String dbClass = ParamHelper.getParamValue(ctx,Constants.DB_DRV_CLASS_PARAM);
        String dbUrl = ParamHelper.getParamValue(ctx, Constants.DB_URL_PARAM);
        String dbUser = ParamHelper.getParamValue(ctx, Constants.DB_USER_PARAM);
        String dbPwd = ParamHelper.getParamValue(ctx, Constants.DB_PWD_PARAM);

        logger.info("Opening DB Connection");
        Connection dbConnection = DBHelper.openConnection(dbClass, dbUrl,dbUser, dbPwd);
        ctx.setAttribute(Constants.DB_CONNECTION_PARAM, dbConnection);
        
        // Initialize IDOLServiceHelper and set in as context attribute
        logger.info("Initializing IDOLServiceHelper");
        String idolApiKey = ParamHelper.getParamValue(ctx, Constants.IDOLAPI_KEY_PARAM);
        IDOLServiceHelper idolServiceHelper = new IDOLServiceHelper(ParamHelper.getParamValue(ctx, idolApiKey));
        ctx.setAttribute(Constants.IDOL_SVC_PARAM, idolServiceHelper);
        
        // Initialize TwitterHelper and set in as context attribute
        logger.info("Initializing TwitterHelper");
        String twOauthKey = ParamHelper.getParamValue(ctx, Constants.TWOAUTH_KEY_PARAM);
        String twOauthSecret = ParamHelper.getParamValue(ctx, Constants.TWOAUTH_SECRET_PARAM);
        String twOauthToken = ParamHelper.getParamValue(ctx, Constants.TWOAUTH_TOKEN_PARAM);
        String twOauthTokenSecret = ParamHelper.getParamValue(ctx, Constants.TWOAUTH_TOEKN_SECRET_PARAM);
        
        TwitterHelper twitterHelper = new TwitterHelper(twOauthKey, twOauthSecret, twOauthToken, twOauthTokenSecret);
        ctx.setAttribute(Constants.TWITTER_HELPER_PARAM, twitterHelper);

    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        logger.info("contextDestroyed");

        logger.info("Closing DB Connection");
        DBHelper.closeConnection();
    }

}
