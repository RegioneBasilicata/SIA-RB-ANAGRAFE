package it.csi.solmr.business.anag;

import it.csi.sigop.dto.services.PagamentiErogatiVO;
import it.csi.sigop.dto.services.RecuperiPregressiVO;
import it.csi.sigop.dto.services.SchedaCreditoVO;


import javax.ejb.Local;

@Local
public interface SigopServLocal 
{
    
  public SchedaCreditoVO[] sigopservVisualizzaDebiti(String cuaa)
      throws Exception, Exception;
  
  public PagamentiErogatiVO sigopservEstraiPagamentiErogati(String cuaa, String settore,
      Integer anno) throws Exception, Exception;
    
  public RecuperiPregressiVO sigopservEstraiRecuperiPregressi(String cuaa, String settore,
      Integer anno) throws Exception, Exception;

}
