package it.csi.smranag.smrgaa.business;

import javax.ejb.EJBLocalObject;
import javax.ejb.Local;

import it.csi.smranag.smrgaa.dto.BaseCodeDescription;
import it.csi.solmr.exception.SolmrException;

/**
 * Interfaccia Local dell'EJB delle decodifica
 * @author TOBECONFIG
 */

@Local
public interface DecodificheLocal
{
  public BaseCodeDescription[] baseDecodeUtilizzoByIdRange(long ids[])
      throws SolmrException;
}
