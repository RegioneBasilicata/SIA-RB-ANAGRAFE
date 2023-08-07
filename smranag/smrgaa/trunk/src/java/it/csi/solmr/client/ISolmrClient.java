package it.csi.solmr.client;

/**
 * <p>Title: S.O.L.M.R.</p>
 * <p>Description: Servizi On-Line per il Mondo Rurale</p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: TOBECONFIG</p>
 * @author Luca Romanello
 * @version 1.0
 */


import it.csi.solmr.exception.SolmrException;

import java.util.Collection;

public interface ISolmrClient {
  public String getService() throws SolmrException;
  public Collection<?> getCodeDescriptions() throws SolmrException;
}