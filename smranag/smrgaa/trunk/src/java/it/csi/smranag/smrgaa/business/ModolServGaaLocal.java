package it.csi.smranag.smrgaa.business;

import it.csi.smranag.smrgaa.dto.modol.AttributiModuloVO;
import it.csi.solmr.exception.SolmrException;

import javax.ejb.EJBLocalObject;
import javax.ejb.Local;

@Local
public interface ModolServGaaLocal
{
	
  public byte[] callModol(byte[] xmlInput, AttributiModuloVO attributiModuloVO)  
      throws SolmrException;
  
  public byte[] trasformStaticPDF(byte[] xmlInput, AttributiModuloVO attributiModuloVO)
      throws SolmrException;
}
