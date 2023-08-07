package it.csi.solmr.business.anag;

import it.csi.smrvit.vitiserv.dto.diritto.DirittoVO;

import javax.ejb.Local;

@Local
public interface VitiServLocal 
{
  public long[] vitiservSearchListIdDiritto(long idAzienda, boolean flagAttivi, int tipoOrdinamento)
    throws Exception, Exception;

  public DirittoVO[] vitiservGetListDirittoByIdRange(long[] ids, int tipoRisultato)
    throws Exception, Exception;
}
