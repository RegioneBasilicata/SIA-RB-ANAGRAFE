package it.csi.solmr.business.anag;

import it.csi.solmr.dto.CodeDescription;


import javax.ejb.Local;

/**
 * <p>
 * Title: SMRGAA
 * </p>
 * 
 * <p>
 * Description: Anagrafe delle Imprese Agricole e Agro-Alimentari
 * </p>
 * 
 * <p>
 * Copyright: Copyright (c) 2006
 * </p>
 * 
 * <p>
 * Company: CSI - PIEMONTE
 * </p>
 * 
 * @author Mauro Vocale
 * @version 1.0
 */
@Local
public interface CommonLocal
{
  public CodeDescription findRegioneByIstatProvincia(String istatProvincia)
      throws Exception;

  public CodeDescription findRegioneByCodiceFiscaleIntermediario(
      String codiceFiscaleIntermediario) throws Exception;

  public CodeDescription[] getListTipoGruppoControlloByIdDichiarazioneConsistenza(
      Long idDichiarazioneConsistenza, String[] orderBy) throws Exception;

  public CodeDescription[] getCodeDescriptionsNew(String tableName,
      String filtro, String valFiltro, String orderBy) throws Exception;

  public it.csi.solmr.dto.profile.CodeDescription getGruppoRuolo(String ruolo)
      throws Exception;

  public String testDB() throws Exception;
  
  public Object getValoreParametroAltriDati(String codiceParametro)
    throws Exception;

}
