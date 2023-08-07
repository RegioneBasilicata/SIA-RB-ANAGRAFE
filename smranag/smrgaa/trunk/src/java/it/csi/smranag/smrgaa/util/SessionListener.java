package it.csi.smranag.smrgaa.util;


import it.csi.solmr.util.SolmrLogger;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.http.HttpSessionAttributeListener;
import javax.servlet.http.HttpSessionBindingEvent;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;


/**
 * Example listener for context-related application events, which were
 * introduced in the 2.3 version of the Servlet API.  This listener
 * merely do*****ents the occurrence of such events in the application log
 * associated with our servlet context.
 *
 * @author Craig R. McClanahan
 * @version $Revision: 267129 $ $Date: 2004-03-18 11:40:35 -0500 (Thu, 18 Mar 2004) $
 */

public final class SessionListener
    implements ServletContextListener,
          HttpSessionAttributeListener, HttpSessionListener {


    // ----------------------------------------------------- Instance Variables


    /**
     * The servlet context with which we are associated.
     */
    @SuppressWarnings("unused")
    private ServletContext context = null;


    // --------------------------------------------------------- Public Methods


    /**
     * Record the fact that a servlet context attribute was added.
     *
     * @param event The session attribute event
     */
    public void attributeAdded(HttpSessionBindingEvent event) 
    {
      if("anagAziendaVO".equalsIgnoreCase(event.getName()))
      {
        SolmrLogger.debug(this,"attributeAdded('" + event.getSession().getId() + "', '" +
            event.getName() + "', '" + event.getValue() + "')");
      }
    }


    /**
     * Record the fact that a servlet context attribute was removed.
     *
     * @param event The session attribute event
     */
    public void attributeRemoved(HttpSessionBindingEvent event) 
    {
      if("anagAziendaVO".equalsIgnoreCase(event.getName()))
      {
        SolmrLogger.debug(this,"attributeRemoved('" + event.getSession().getId() + "', '" +
            event.getName() + "', '" + event.getValue() + "')");
      }

    }


    /**
     * Record the fact that a servlet context attribute was replaced.
     *
     * @param event The session attribute event
     */
    public void attributeReplaced(HttpSessionBindingEvent event) 
    {
      if("anagAziendaVO".equalsIgnoreCase(event.getName()))
      {
        SolmrLogger.debug(this,"attributeReplaced('" + event.getSession().getId() + "', '" +
            event.getName() + "', '" + event.getValue() + "')");
      }

    }


    /**
     * Record the fact that this web application has been destroyed.
     *
     * @param event The servlet context event
     */
    public void contextDestroyed(ServletContextEvent event) {

      SolmrLogger.debug(this,"contextDestroyed()");
      this.context = null;

    }


    /**
     * Record the fact that this web application has been initialized.
     *
     * @param event The servlet context event
     */
    public void contextInitialized(ServletContextEvent event) 
    {

      this.context = event.getServletContext();
      SolmrLogger.debug(this,"contextInitialized()");

    }


    /**
     * Record the fact that a session has been created.
     *
     * @param event The session event
     */
    public void sessionCreated(HttpSessionEvent event) 
    {

      SolmrLogger.debug(this,"sessionCreated('" + event.getSession().getId() + "')");

    }


    /**
     * Record the fact that a session has been destroyed.
     *
     * @param event The session event
     */
    public void sessionDestroyed(HttpSessionEvent event) 
    {

      SolmrLogger.debug(this,"sessionDestroyed('" + event.getSession().getId() + "')");

    }


    // -------------------------------------------------------- Private Methods


    /**
     * Log a message to the servlet context application log.
     *
     * @param message Message to be logged
     */
    /*private void log(String message) {

   if (context != null)
       context.log("SessionListener: " + message);
   else
       SolmrLogger.debug(this, "SessionListener: " + message);

    }*/


    /**
     * Log a message and associated exception to the servlet context
     * application log.
     *
     * @param message Message to be logged
     * @param throwable Exception to be logged
     */
    /*private void log(String message, Throwable throwable) {

   if (context != null)
       context.log("SessionListener: " + message, throwable);
   else {
       SolmrLogger.debug(this, "SessionListener: " + message);
       throwable.printStackTrace(System.out);
   }

    }*/


}