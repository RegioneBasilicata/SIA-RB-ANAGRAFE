package it.csi.solmr.client;

import javax.servlet.http.HttpSessionListener;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSession;

import java.util.*;

import it.csi.solmr.util.SolmrLogger;

/**
 * <p>Title: PSJSP</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: </p>
 * @author Luca Romanello
 * @version 1.0
 */

public class SolmrSessionListener implements HttpSessionListener {
  private static HashMap<Object,Object> hmSessions = new HashMap<Object,Object>();

  public void sessionCreated(HttpSessionEvent se) {
    SolmrLogger.debug(this, "Adding session: "+se.getSession().getId());
    hmSessions.put(se.getSession().getId(), se.getSession());
    //PsjspGatewayServlet.addSession(se.getSession());
  }

  public void sessionDestroyed(HttpSessionEvent se) {
    SolmrLogger.debug(this, "Removing session: "+se.getSession().getId());
    String deadSessionId = se.getSession().getId();
    HttpSession session = (HttpSession)hmSessions.get(deadSessionId);
    Enumeration<?> enumeration = (Enumeration<?>)session.getAttributeNames();
    while (enumeration.hasMoreElements()) {
      String name = (String)enumeration.nextElement();
      session.removeAttribute(name);
    }
    hmSessions.remove(deadSessionId);
    //PsjspGatewayServlet.removeDeadSession(se.getSession().getId());
  }

  public static HashMap<Object,Object> getHmSessions() {
    return hmSessions;
  }
}
