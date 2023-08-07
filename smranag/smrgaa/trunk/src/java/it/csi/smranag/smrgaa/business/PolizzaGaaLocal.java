package it.csi.smranag.smrgaa.business;

import it.csi.smranag.smrgaa.dto.BaseCodeDescription;
import it.csi.smranag.smrgaa.dto.polizze.PolizzaDettaglioVO;
import it.csi.smranag.smrgaa.dto.polizze.PolizzaVO;
import it.csi.solmr.exception.SolmrException;

import java.util.TreeMap;
import java.util.Vector;

import javax.ejb.Local;



/**
 * <p>
 * Title: S.O.L.M.R.
 * </p>
 * <p>
 * Description: Servizi On-Line per il Mondo Rurale
 * </p>
 * <p>
 * Copyright: Copyright (c) 2008
 * </p>
 * <p>
 * Company: TOBECONFIG
 * </p>
 * 
 * @author TOBECONFIG
 * @version 1.0
 */

@Local
public interface PolizzaGaaLocal 
{
  public Vector<Integer> getElencoAnnoCampagnaByIdAzienda(long idAzienda) throws SolmrException;
  
  public Vector<BaseCodeDescription> getElencoInterventoByIdAzienda(long idAzienda)
    throws SolmrException;
  
  public Vector<PolizzaVO> getElencoPolizze(long idAzienda, Integer annoCampagna, Long idIntervento)
    throws SolmrException;
  
  public PolizzaVO getDettaglioPolizza(long idPolizzaAssicurativa)
    throws SolmrException;
  
  public TreeMap<Long,Vector<PolizzaDettaglioVO>> getDettaglioPolizzaColtura(long idPolizzaAssicurativa)
    throws SolmrException;
  
  public TreeMap<Long,Vector<PolizzaDettaglioVO>> getDettaglioPolizzaStruttura(long idPolizzaAssicurativa)
    throws SolmrException;
  
  public TreeMap<Long,Vector<PolizzaDettaglioVO>> getDettaglioPolizzaZootecnia(long idPolizzaAssicurativa)
    throws SolmrException;
  
  
}
