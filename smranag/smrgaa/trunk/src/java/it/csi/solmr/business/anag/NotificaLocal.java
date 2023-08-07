package it.csi.solmr.business.anag;

import it.csi.papua.papuaserv.presentation.ws.profilazione.axis.UtenteAbilitazioni;
import it.csi.solmr.dto.anag.ElencoNotificheVO;
import it.csi.solmr.dto.anag.NotificaEntitaVO;
import it.csi.solmr.dto.anag.NotificaVO;
import it.csi.solmr.dto.profile.RuoloUtenza;
import it.csi.solmr.exception.SolmrException;

import java.util.HashMap;
import java.util.Vector;

import javax.ejb.Local;

@Local
public interface NotificaLocal
{
  public ElencoNotificheVO ricercaNotificheByParametri(NotificaVO notificaVO,
      UtenteAbilitazioni utenteAbilitazioni, RuoloUtenza ruoloUtenza, Boolean storico, int maxRecord) throws Exception, SolmrException;

  public NotificaVO findNotificaByPrimaryKey(Long idNotifica, String provenienza)
      throws SolmrException, Exception;

  public Vector<NotificaVO> getElencoNotificheByIdAzienda(
      NotificaVO notificaVO, Boolean storico, String ordinamento)
      throws Exception, SolmrException;
  
  public Vector<NotificaVO> getElencoNotifichePopUp(NotificaVO notificaVO) 
      throws Exception;

  public Long insertNotifica(NotificaVO notificaVO, long idUtenteAggiornamento)
      throws Exception, SolmrException;
  
  public void updateNotifica(NotificaVO notificaVO, long idUtenteAggiornamento)
      throws Exception, SolmrException;

  public void closeNotifica(NotificaVO notificaVO, long idUtenteAggiornamento)
      throws Exception, SolmrException;
  
  public Long getIdTipologiaNotificaFromCategoria(Long idCategoriaNotifica)
      throws Exception;
  
  public boolean isChiusuraNotificaRuoloPossibile(RuoloUtenza ruoloUtenza, long idCategoriaNotifica)
      throws Exception;
  
  public Vector<NotificaVO> getElencoNotificheForIdentificato(long identificativo, 
      String codiceTipo, long idAzienda, Long idDichiarazioneConsistenza) 
    throws Exception;
  
  public boolean isModificaNotificaRuoloPossibile(RuoloUtenza ruoloUtenza, long idCategoriaNotifica)
      throws Exception;
  
  public HashMap<Long,Vector<NotificaEntitaVO>> getNotificheEntitaUvFromIdNotifica(
      long ids[]) throws Exception;
  
  public HashMap<Long,Vector<NotificaEntitaVO>> getNotificheEntitaParticellaFromIdNotifica(
      long ids[]) throws Exception;

}
