package it.csi.solmr.business.anag;

import it.csi.solmr.dto.anag.ParticellaVO;
import it.csi.solmr.exception.SolmrException;


import javax.ejb.Local;

@Local
public interface SitiLocal
{
  public Boolean serviceGetDataLavParticelle(ParticellaVO[] particelle)
      throws SolmrException, Exception;
  
  public String serviceGetParticellaUrl3D(String istatComune,
      String sezione, String foglio, String particella, String subalterno)
    throws SolmrException, Exception;
  
}
