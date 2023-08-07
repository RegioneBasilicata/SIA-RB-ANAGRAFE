package it.csi.smranag.smrgaa.business;

import it.csi.smranag.smrgaa.dto.manuali.ManualeVO;
import it.csi.solmr.exception.SolmrException;

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
public interface ManualeGaaLocal 
{
  public Vector<ManualeVO> getElencoManualiFromRuoli(String descRuolo) 
      throws SolmrException;
  
  public ManualeVO getManuale(long idManuale) throws SolmrException;
  
}
