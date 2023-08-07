package it.csi.solmr.business.anag;

import javax.ejb.CreateException;
import javax.ejb.EJBLocalHome;

public interface AnagFacadeUmaLocalHome extends EJBLocalHome
{
  public AnagFacadeUmaLocal create() throws CreateException;
}