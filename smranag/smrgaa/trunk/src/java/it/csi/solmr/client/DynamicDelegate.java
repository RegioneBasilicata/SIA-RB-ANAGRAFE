package it.csi.solmr.client;

import java.lang.reflect.*;

import it.csi.solmr.etc.*;
import it.csi.solmr.business.anag.*;
import it.csi.solmr.interfaceCSI.uma.*;
import it.csi.solmr.util.*;

import java.util.*;

import javax.naming.InitialContext;

import it.csi.csi.porte.*;
import it.csi.csi.porte.proxy.*;
import it.csi.csi.util.xml.*;
import it.csi.solmr.exception.ExcManager;

/**
 * <p>Title: S.O.L.M.R.</p>
 * <p>Description: Servizi On-Line per il Mondo Rurale</p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: TOBECONFIG</p>
 * @author Daniele Marzico e Luca Romanello
 * @version 1.0
 */

public class DynamicDelegate implements InvocationHandler
{
   String nomeServer;
   static HashMap<String,String> csMapping = new HashMap<String,String>();
   Object theServer = null;
   
   
   AnagFacadeLocal afr;
   UmaCSIInterface ucsi;

   static
   {
     csMapping.put(SolmrConstants.I_ANAG_FACADE,  SolmrConstants.I_ANAG_CLIENT);
     csMapping.put(SolmrConstants.I_PROF_FACADE,  SolmrConstants.I_PROF_CLIENT);
     csMapping.put(SolmrConstants.I_UMA_CSI,      SolmrConstants.I_UMA_CLIENT);
     if (SolmrLogger.isDebugEnabled(csMapping))
       SolmrLogger.debug(csMapping, "Initialized csMapping");
   }

   // Interfacce client
   public static Class<?>[] getElencoInterfacceImplementate(String nomeServer)
   {
     Class<?>[] returnValue = new Class[1];
     try
     {
       returnValue[0] = Class.forName((String)csMapping.get(nomeServer));
     }
     catch (ClassNotFoundException ex)
     {
       SolmrLogger.fatal(ex, ex);
     }
     return returnValue;
   }

   public static Object newInstance(String nomeServer)
   {
     return Proxy.newProxyInstance(DynamicDelegate.class.getClassLoader(),
				   getElencoInterfacceImplementate(nomeServer),
				   new DynamicDelegate(nomeServer));
   }

   private DynamicDelegate(String nomeServer)
   {
     this.nomeServer = nomeServer;
     //String clientBound = (String)csMapping.get(nomeServer);
     try
     {
       if (nomeServer.equals(SolmrConstants.I_ANAG_FACADE))
       {          	   
    	 // Con Jboss non funziona getDynamicProxy()
    	   if(afr == null) {
    		   InitialContext ctx = new InitialContext();
    		   afr = (AnagFacadeLocal) ctx.lookup("java:app/smrgaa/comp/env/solmr/anag/AnagFacade");
    	   }
    	 
         theServer = afr;
       }
       else
         if (nomeServer.equals(SolmrConstants.I_UMA_CSI))
         {
           InfoPortaDelegata info = PDConfigReader.read(getClass().getResourceAsStream(SolmrConstants.FILE_PD_UMA));
           ucsi = (UmaCSIInterface)PDProxy.newInstance(info);
           theServer = ucsi;
         }
     } catch (Exception exc) {
       exc.printStackTrace();
       SolmrLogger.fatal(this, exc);
     }
   }

   public Object invoke(Object proxy, Method m, Object[] args) throws Exception {
     Object result = null;

     SolmrLogger.debug(this, "Proxy = "+proxy.getClass().getName());
     // Costruisco il vettore di classi per i parametri del metodo
     // selezionato
     Class<?>[] parameters = m.getParameterTypes();
     // Seleziono il server ed invoco il metodo con lo stesso nome
     // e con gli stessi parametri
     try {
       Method mServer = theServer.getClass().getMethod(m.getName(),parameters);
       result = mServer.invoke(theServer, args);
     } catch (InvocationTargetException e) {
       // Genero una eccezione per il client
       // partendo da quella ricevuta dal server.
       throw ExcManager.remap(e);
     } catch (Exception e) {
       // Caso di eccezione di basso livello (Es interfaccia non implementata o metodo mancante
       e.printStackTrace();
       throw new RuntimeException("Unexpected invocation exception: "+e.getMessage());
     } finally {
     }
     return result;
   }
}
