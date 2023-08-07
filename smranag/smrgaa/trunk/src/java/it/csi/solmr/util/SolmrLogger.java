package it.csi.solmr.util;

/**
 * <p>Title: S.O.L.M.R.</p>
 * <p>Description: Servizi On-Line per il Mondo Rurale</p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: TOBECONFIG</p>
 * @author Luca Romanello
 * @version 1.0
 */

import it.csi.solmr.etc.SolmrConstants;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.HashMap;

import org.apache.log4j.Logger;
import org.apache.log4j.Priority;

public abstract class SolmrLogger {
  private static HashMap<Object,Object> logMapper = new HashMap<Object,Object>();

  static {

    logMapper.put("it.csi.solmr.business.anag", SolmrConstants.APP_NAME_BUSINESS_ANAG);
    logMapper.put("it.csi.solmr.business.anag.services", SolmrConstants.APP_NAME_BUSINESS_ANAG);
    logMapper.put("it.csi.solmr.business.anag.stampe", SolmrConstants.APP_NAME_BUSINESS_ANAG);
    logMapper.put("it.csi.solmr.business.anag.sian", SolmrConstants.APP_NAME_BUSINESS_ANAG);
    logMapper.put("it.csi.solmr.business.profile", SolmrConstants.APP_NAME_BUSINESS_PROF);
    logMapper.put("it.csi.solmr.client", SolmrConstants.APP_NAME_CLIENT_ANAG);
    logMapper.put("it.csi.solmr.client.aaep", SolmrConstants.APP_NAME_CLIENT_ANAG);
    logMapper.put("it.csi.solmr.client.anag", SolmrConstants.APP_NAME_CLIENT_ANAG);
    logMapper.put("it.csi.solmr.client.anag.services", SolmrConstants.APP_NAME_CLIENT_ANAG);
    logMapper.put("it.csi.solmr.client.profile", SolmrConstants.APP_NAME_CLIENT_ANAG);
    logMapper.put("it.csi.solmr.client.services", SolmrConstants.APP_NAME_CLIENT_ANAG);
    logMapper.put("it.csi.solmr.dto", SolmrConstants.APP_NAME_BUSINESS_ANAG);
    logMapper.put("it.csi.solmr.dto.anag", SolmrConstants.APP_NAME_BUSINESS_ANAG);
    logMapper.put("it.csi.solmr.dto.anag.stampe", SolmrConstants.APP_NAME_BUSINESS_ANAG);
    logMapper.put("it.csi.solmr.dto.services", SolmrConstants.APP_NAME_BUSINESS_ANAG);
    logMapper.put("it.csi.solmr.etc", SolmrConstants.APP_NAME_BUSINESS_ANAG);
    logMapper.put("it.csi.solmr.etc.anag", SolmrConstants.APP_NAME_BUSINESS_ANAG);
    logMapper.put("it.csi.solmr.exception", SolmrConstants.APP_NAME_BUSINESS_ANAG);
    logMapper.put("it.csi.solmr.exception.services", SolmrConstants.APP_NAME_BUSINESS_ANAG);
    logMapper.put("it.csi.solmr.integration", SolmrConstants.APP_NAME_INTEGRATION_ANAG);
    logMapper.put("it.csi.solmr.integration.anag", SolmrConstants.APP_NAME_INTEGRATION_ANAG);
    logMapper.put("it.csi.solmr.integration.anag.stampe", SolmrConstants.APP_NAME_INTEGRATION_ANAG);
    logMapper.put("it.csi.solmr.integration.profile", SolmrConstants.APP_NAME_INTEGRATION_PROF);
    logMapper.put("it.csi.solmr.interfaceCSI.anag", SolmrConstants.APP_NAME_CLIENT_ANAG);
    logMapper.put("it.csi.solmr.interfaceCSI.anag.services", SolmrConstants.APP_NAME_CLIENT_ANAG);
    logMapper.put("it.csi.solmr.interfaceCSI.profile", SolmrConstants.APP_NAME_CLIENT_ANAG);
    logMapper.put("it.csi.solmr.util", SolmrConstants.APP_NAME_BUSINESS_ANAG);
    logMapper.put("it.csi.solmr.client.sian", SolmrConstants.APP_NAME_INTEGRATION_ANAG);
    logMapper.put("it.csi.solmr.servlet.anag", SolmrConstants.APP_NAME_INTEGRATION_ANAG);
  }

  private static Logger getLogger(Object thrower) {
    Logger logger = null;
    try {
      String appName = (String)logMapper.get(thrower.getClass().getPackage().getName());
      if (appName == null)
	throw new Exception();
      logger = Logger.getLogger(appName);
      if (logger == null)
	throw new Exception();
    }
    catch (Exception ex) {
      logger = Logger.getLogger(SolmrConstants.APP_NAME_GENERIC);
      return logger;
    }
    return logger;
  }

  public static void info(Object thrower, Object msg) {
    getLogger(thrower).info(" <"+thrower.getClass().getName()+"> "+msg);
  }

  public static void debug(Object thrower, Object msg) {
    getLogger(thrower).debug(" <"+thrower.getClass().getName()+"> "+msg);
  }

  public static void warn(Object thrower, Object msg) {
    getLogger(thrower).warn(" <"+thrower.getClass().getName()+"> "+msg);
  }

  public static void error(Object thrower, Object msg) {
    getLogger(thrower).error(" <"+thrower.getClass().getName()+"> "+msg);
  }

  public static void fatal(Object thrower, Object msg) {
    getLogger(thrower).fatal(" <"+thrower.getClass().getName()+"> "+msg);
  }

  public static boolean isDebugEnabled(Object thrower) {
    return getLogger(thrower).isDebugEnabled();
  }

  public static boolean isInfoEnabled(Object thrower) {
    return getLogger(thrower).isInfoEnabled();
  }

  public static boolean isEnabledFor(Object thrower, Priority priority) {
    return getLogger(thrower).isEnabledFor(priority);
  }

  public static void dumpStackTrace(Object thrower, Object obj, Throwable t)
  {
    error(thrower,new StringBuffer(StringUtils.checkNull(obj)).append(getStackTrace(t)));
  }

  public static String getStackTrace(Throwable t)
  {
    StringWriter sw = new StringWriter();
    t.printStackTrace(new PrintWriter(sw));
    return sw.toString();
  }


}
