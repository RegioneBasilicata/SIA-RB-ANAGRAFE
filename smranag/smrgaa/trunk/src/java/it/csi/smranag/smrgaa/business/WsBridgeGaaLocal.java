package it.csi.smranag.smrgaa.business;

import it.csi.smranag.smrgaa.dto.ws.ResponseWsBridgeVO;
import it.csi.solmr.dto.anag.sian.SianAllevamentiVO;
import it.csi.solmr.exception.SolmrException;

import java.math.BigDecimal;
import java.util.Hashtable;

import javax.ejb.Local;

@Local
public interface WsBridgeGaaLocal
{
	
  public Hashtable<BigDecimal,SianAllevamentiVO> serviceAnagraficaAllevamenti(
      String cuaa, String dataRichiesta)
    throws SolmrException;
  
  public ResponseWsBridgeVO serviceConsistenzaStatisticaMediaAllevamento(
      String cuaa)  throws SolmrException;
  
  public ResponseWsBridgeVO serviceConsistenzaUbaCensimOvini(
      String cuaa)  throws SolmrException;
  
  public void serviceWsBridgeAggiornaDatiBDN(String cuaa) throws SolmrException;
	
}
