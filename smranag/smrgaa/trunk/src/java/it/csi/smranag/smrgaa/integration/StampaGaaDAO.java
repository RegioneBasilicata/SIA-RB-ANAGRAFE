package it.csi.smranag.smrgaa.integration;


import it.csi.smranag.smrgaa.dto.anagrafe.TipoInfoAggiuntivaVO;
import it.csi.smranag.smrgaa.dto.log.Parametro;
import it.csi.smranag.smrgaa.dto.log.Variabile;
import it.csi.smranag.smrgaa.dto.modol.RiepilogoStampaTerrVO;
import it.csi.smranag.smrgaa.dto.stampe.DichiarazioneConsistenzaGaaVO;
import it.csi.smranag.smrgaa.dto.stampe.InfoFascicoloVO;
import it.csi.smranag.smrgaa.dto.stampe.RichiestaTipoReportVO;
import it.csi.smranag.smrgaa.dto.stampe.TipoAllegatoVO;
import it.csi.smranag.smrgaa.dto.stampe.TipoFirmatarioVO;
import it.csi.smranag.smrgaa.dto.stampe.TipoReportVO;
import it.csi.smranag.smrgaa.dto.terreni.RiepiloghiUnitaArboreaVO;
import it.csi.smranag.smrgaa.util.LoggerUtils;
import it.csi.solmr.dto.CodeDescription;
import it.csi.solmr.dto.ComuneVO;
import it.csi.solmr.dto.anag.AnagAziendaVO;
import it.csi.solmr.dto.anag.ContoCorrenteVO;
import it.csi.solmr.dto.anag.DocumentoVO;
import it.csi.solmr.dto.anag.FabbricatoVO;
import it.csi.solmr.dto.anag.ParticellaVO;
import it.csi.solmr.dto.anag.TipoDocumentoVO;
import it.csi.solmr.dto.anag.TipoFormaConduzioneVO;
import it.csi.solmr.dto.anag.attestazioni.ParametriAttAziendaVO;
import it.csi.solmr.dto.anag.attestazioni.ParametriAttDichiarataVO;
import it.csi.solmr.dto.anag.attestazioni.TipoAttestazioneVO;
import it.csi.solmr.dto.anag.attestazioni.TipoParametriAttestazioneVO;
import it.csi.solmr.dto.anag.terreni.ConduzioneParticellaVO;
import it.csi.solmr.dto.anag.terreni.StoricoParticellaVO;
import it.csi.solmr.etc.SolmrConstants;
import it.csi.solmr.exception.DataAccessException;
import it.csi.solmr.exception.ResourceAccessException;
import it.csi.solmr.util.SolmrLogger;
import it.csi.solmr.util.Validator;

import java.math.BigDecimal;
import java.sql.Clob;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Date;
import java.util.Vector;


public class StampaGaaDAO extends it.csi.solmr.integration.BaseDAO {
  
  
  public StampaGaaDAO() throws ResourceAccessException{
    super(SolmrConstants.JNDI_ANAG_RESOURCE_REFERENCE);
  }

  public StampaGaaDAO(String refName) throws ResourceAccessException 
  {
    super(refName);
  }
  
  
  /**
   * 
   * Mi restituisce i subReport relativi alla stampa da visualizzare
   * 
   * 
   * @param codiceReport
   * @return
   * @throws DataAccessException
   */
  public RichiestaTipoReportVO[] getElencoSubReportRichiesta(String codiceReport, java.util.Date dataRiferimento) throws DataAccessException
  {
    Connection conn = null;
    String query = null;
    PreparedStatement stmt = null;
    ResultSet rs = null;
    StringBuffer queryBuf = null;
    Vector<RichiestaTipoReportVO> results = null;
    int idx = 0;
    try
    {
      SolmrLogger
      .debug(
          this,
          "[StampaGaaDAO::getElencoSubReportRichiesta] BEGIN.");

      queryBuf = new StringBuffer();
    
      /* CONCATENAZIONE/CREAZIONE QUERY BEGIN. */
      queryBuf.append("" +
      		"SELECT " + 
      		"        REP.ID_REPORT_SUB_REPORT, " +
          "        REP.ID_TIPO_REPORT, " +
          "        REP.ID_TIPO_SUB_REPORT, " +
          "        REP.QUADRO, " +
          "        REP.SEZIONE, " +
          "        REP.SALTO_PAGINA_INIZIALE, " +
          "        REP.VISIBILE, " +
          "        REP.SELEZIONABILE, " +
          "        REP.DESCRIZIONE_SELEZIONE, " +
          "        REP.ID_REPORT_SUB_REPORT_PADRE, " +
          "        REP.OBBLIGATORIO, " +
          "        TSR.CODICE, " +
          "        REP.ORDINAMENTO, " +
          "        TT.TESTO, " +
          "        REP.NOTE, " +
          "        REP.ID_TIPO_FIRMATARIO, " +
          "        TR.ID_TIPO_ALLEGATO " +
          "FROM " +
          "        DB_R_REPORT_SUB_REPORT REP, " + 
          "        DB_TIPO_REPORT TR, " +
          "        DB_TIPO_SUB_REPORT TSR, " +
          "        DB_TESTO_SUB_REPORT TT " +
          "WHERE " +
          "        TR.ID_TIPO_REPORT = REP.ID_TIPO_REPORT " +
          "AND     TR.CODICE_REPORT = ? " +
          "AND     REP.VISIBILE = 'S' " +
          "AND     TSR.ID_TIPO_SUB_REPORT = REP.ID_TIPO_SUB_REPORT " +
          "AND     REP.ID_REPORT_SUB_REPORT = TT.ID_REPORT_SUB_REPORT (+) ");
      if(Validator.isNotEmpty(dataRiferimento))
      {
        queryBuf.append("" +
          "AND     REP.DATA_INIZIO_VALIDITA <= ? " +
          "AND     NVL(REP.DATA_FINE_VALIDITA, TO_DATE('31/12/9999', 'dd/mm/yyyy')) >= ? " +
          "AND     TT.DATA_INIZIO_VALIDITA (+) <= ? " +
          "AND     NVL(TT.DATA_FINE_VALIDITA (+), TO_DATE('31/12/9999', 'dd/mm/yyyy')) >= ? ");
      }
      else
      {
        queryBuf.append("" +
          "AND     REP.DATA_FINE_VALIDITA IS NULL " +
          "AND     TT.DATA_FINE_VALIDITA IS NULL ");
      }
      
      queryBuf.append("" +
          "ORDER BY " +
          "        REP.ORDINAMENTO ASC");
      /* CONCATENAZIONE/CREAZIONE QUERY END. */
    
      conn = getDatasource().getConnection();
      query = queryBuf.toString();
      if (SolmrLogger.isDebugEnabled(this))
      {
        // Dato che la query costruita dinamicamente è un dato importante la
        // registro sul file di log se il
        // debug è abilitato
    
        SolmrLogger
            .debug(
                this,
                "[StampaGaaDAO::getElencoSubReportRichiesta] Query="
                    + query);
      }
      stmt = conn.prepareStatement(query);
    
      // Setto i parametri della query
      stmt.setString(++idx, codiceReport);
      if(Validator.isNotEmpty(dataRiferimento))
      {
        stmt.setTimestamp(++idx, convertDateToTimestamp(dataRiferimento));
        stmt.setTimestamp(++idx, convertDateToTimestamp(dataRiferimento));
        stmt.setTimestamp(++idx, convertDateToTimestamp(dataRiferimento));
        stmt.setTimestamp(++idx, convertDateToTimestamp(dataRiferimento));
      }
      rs = stmt.executeQuery();
      
      
      results = new Vector<RichiestaTipoReportVO>();
      while (rs.next())
      {
        RichiestaTipoReportVO richiestaTipoReportVO = new RichiestaTipoReportVO();
        richiestaTipoReportVO.setIdReportSubReport(new Long(rs.getLong("ID_REPORT_SUB_REPORT")));
        richiestaTipoReportVO.setIdTipoReport(rs.getInt("ID_TIPO_REPORT"));
        richiestaTipoReportVO.setIdTipoSubReport(checkLongNull(rs.getString("ID_TIPO_SUB_REPORT")));
        richiestaTipoReportVO.setQuadro(rs.getString("QUADRO"));
        richiestaTipoReportVO.setSezione(rs.getString("SEZIONE"));
        richiestaTipoReportVO.setSaltoPaginaIniziale(SolmrConstants.FLAG_S
            .equals(rs.getString("SALTO_PAGINA_INIZIALE")));
        richiestaTipoReportVO.setVisibile(SolmrConstants.FLAG_S
            .equals(rs.getString("VISIBILE")));
        richiestaTipoReportVO.setSelezionabile(SolmrConstants.FLAG_S
            .equals(rs.getString("SELEZIONABILE")));
        richiestaTipoReportVO.setDescrizioneSelezione(rs.getString("DESCRIZIONE_SELEZIONE"));
        richiestaTipoReportVO.setIdReportSubReportPadre(checkLongNull(rs.getString("ID_REPORT_SUB_REPORT_PADRE")));
        richiestaTipoReportVO.setCodice(rs.getString("CODICE"));
        richiestaTipoReportVO.setObbligatorio(rs.getString("OBBLIGATORIO"));
        richiestaTipoReportVO.setOrdinamento(rs.getInt("ORDINAMENTO"));
        richiestaTipoReportVO.setNote(rs.getString("NOTE"));
        richiestaTipoReportVO.setIdTipoFirmatario(checkIntegerNull(rs.getString("ID_TIPO_FIRMATARIO")));
        richiestaTipoReportVO.setIdTipoAllegato(checkIntegerNull(rs.getString("ID_TIPO_ALLEGATO")));
        
        Clob clob = rs.getClob("TESTO");
        if(clob != null)
        {
          String testo = clob.getSubString(1, (int) clob.length());
          richiestaTipoReportVO.setTesto(testo);
        }
        
        results.add(richiestaTipoReportVO);
      }
      if (results.size() > 0)
      {
        return results.toArray(new RichiestaTipoReportVO[results.size()]);
      }
      else
      {
        return null;
      }
    }
    catch (Throwable t)
    {
      Parametro parametri[] = new Parametro[]
      { new Parametro("codiceReport", codiceReport),
        new Parametro("dataRiferimento", dataRiferimento) };
      Variabile variabili[] = new Variabile[]
      { new Variabile("results", results), new Variabile("idx", idx)

      };
      LoggerUtils.logDAOError(this,
          "[StampaGaaDAO::getElencoSubReportRichiesta] ", t, query,
          variabili, parametri);
      throw new DataAccessException(t.getMessage());
    }
    finally
    {
      close(rs, stmt, conn);
      SolmrLogger
          .debug(this, "[StampaGaaDAO::getElencoSubReportRichiesta] END.");
    }
  }
  
  
  //===================================================================
  // Quadro A - Sez I
  //
  // Recupero azienda attiva con idAzienda e idDichiarazioneConsistenza
  // ===================================================================  
  public AnagAziendaVO getAnagraficaAzienda(Long idAzienda,
      java.util.Date dataRiferimento) throws DataAccessException
  {
    Connection conn = null;
    String query = null;
    PreparedStatement stmt = null;
    ResultSet rs = null;
    StringBuffer queryBuf = null;
    AnagAziendaVO result = null;
    int idx = 0;
    try
    {
      SolmrLogger
      .debug(
          this,
          "[StampaGaaDAO::getAnagraficaAzienda] BEGIN.");

      queryBuf = new StringBuffer();
    
      /* CONCATENAZIONE/CREAZIONE QUERY BEGIN. */
      queryBuf.append("" +
          "SELECT " + 
          "       AA.ID_ANAGRAFICA_AZIENDA, AA.ID_AZIENDA, " +
          "       AA.DATA_INIZIO_VALIDITA, AA.DATA_FINE_VALIDITA, " +
          "       AA.CUAA, PARTITA_IVA, AA.DENOMINAZIONE,  " +
          "       AA.ID_FORMA_GIURIDICA, " +
          "       TFG.DESCRIZIONE DESC_FORMA_GIURIDICA, " +
          "       AA.ID_ATTIVITA_ATECO, " +
          "       TAT.DESCRIZIONE DESC_ATTIVITA_ATECO, " +
          "       TAT.CODICE AS CODICE_ATECO, " +
          "       AA.ID_ATTIVITA_OTE, " +
          "       TOT.DESCRIZIONE DESC_ATTIVITA_OTE, " +
          "       AA.CCIAA_PROVINCIA_REA, " +
          "       AA.CCIAA_NUMERO_REA, " +
          "       AA.CCIAA_NUMERO_REGISTRO_IMPRESE, " +
          "       AA.CCIAA_ANNO_ISCRIZIONE, " +
          "       AA.PROVINCIA_COMPETENZA, " +
          "       P1.SIGLA_PROVINCIA SEDELEG_PROV,  " +
          "       COMUNE.DESCOM, " +
          "       AA.SEDELEG_INDIRIZZO, " +
          "       AA.SEDELEG_CAP, " +
          "       AA.SEDELEG_CITTA_ESTERO, " +
          "       AA.MAIL, " +
          "       AA.TELEFONO, " +
          "       AA.PEC, " +
          "       P2.DESCRIZIONE AS DESPROV, " +
          "       AZ.FASCICOLO_DEMATERIALIZZATO " +
          "FROM   DB_ANAGRAFICA_AZIENDA AA, " +
          "       DB_TIPO_FORMA_GIURIDICA TFG, " +
          "       DB_TIPO_ATTIVITA_ATECO TAT, " +
          "       DB_TIPO_ATTIVITA_OTE TOT, " +
          "       COMUNE, PROVINCIA P1, PROVINCIA P2, " +
          "       DB_AZIENDA AZ " +
          "WHERE  AA.SEDELEG_COMUNE = COMUNE.ISTAT_COMUNE(+) " +
          "AND    COMUNE.ISTAT_PROVINCIA = P1.ISTAT_PROVINCIA(+) " +
          "AND    AA.ID_FORMA_GIURIDICA = TFG.ID_FORMA_GIURIDICA(+) " +
          "AND    AA.ID_ATTIVITA_ATECO = TAT.ID_ATTIVITA_ATECO(+) " +
          "AND    AA.ID_ATTIVITA_OTE = TOT.ID_ATTIVITA_OTE(+) " +
          "AND    AA.PROVINCIA_COMPETENZA = P2.ISTAT_PROVINCIA(+) " +
          "AND    AA.ID_AZIENDA = ? " +
          "AND    AA.ID_AZIENDA = AZ.ID_AZIENDA " +
          "AND (  ( AA.DATA_FINE_VALIDITA IS NOT NULL AND ( TRUNC(?) BETWEEN AA.DATA_INIZIO_VALIDITA AND AA.DATA_FINE_VALIDITA ) ) " +
          "     OR ( AA.DATA_FINE_VALIDITA IS NULL     AND AA.DATA_INIZIO_VALIDITA <= TRUNC(?) ) ) ");
      /* CONCATENAZIONE/CREAZIONE QUERY END. */
    
      conn = getDatasource().getConnection();
      query = queryBuf.toString();
      if (SolmrLogger.isDebugEnabled(this))
      {
        // Dato che la query costruita dinamicamente è un dato importante la
        // registro sul file di log se il
        // debug è abilitato
    
        SolmrLogger
            .debug(
                this,
                "[StampaGaaDAO::getAnagraficaAzienda] Query="
                    + query);
      }
      stmt = conn.prepareStatement(query);
      
      
      SolmrLogger
      .debug(
          this,
          "[StampaGaaDAO::getAnagraficaAzienda] dataRiferimento="
              + dataRiferimento);
    
      // Setto i parametri della query
      stmt.setLong(++idx, idAzienda.longValue());
      stmt.setTimestamp(++idx, convertDateToTimestamp(dataRiferimento));
      stmt.setTimestamp(++idx, convertDateToTimestamp(dataRiferimento));
      rs = stmt.executeQuery();
      
      if (rs.next())
      {
        result = new AnagAziendaVO();
        result.setIdAnagAzienda(new Long(rs.getLong("ID_ANAGRAFICA_AZIENDA")));
        result.setIdAzienda(new Long(rs.getLong("ID_AZIENDA")));
        result.setDataInizioVal(rs.getDate("DATA_INIZIO_VALIDITA"));
        result.setDataFineVal(rs.getDate("DATA_FINE_VALIDITA"));
        result.setCUAA(rs.getString("CUAA"));
        result.setPartitaIVA(rs.getString("PARTITA_IVA"));
        result.setDenominazione(rs.getString("DENOMINAZIONE"));
        result.setTipoFormaGiuridica(new CodeDescription(new Integer(rs
            .getInt("ID_FORMA_GIURIDICA")), rs
            .getString("DESC_FORMA_GIURIDICA")));
        result
            .setTipoAttivitaATECO(new CodeDescription(new Integer(rs
                .getInt("ID_ATTIVITA_ATECO")), rs
                .getString("DESC_ATTIVITA_ATECO")));
        result.getTipoAttivitaATECO().setSecondaryCode(rs.getString("CODICE_ATECO"));
        result.setTipoAttivitaOTE(new CodeDescription(new Integer(rs
            .getInt("ID_ATTIVITA_OTE")), rs.getString("DESC_ATTIVITA_OTE")));
        result.setCCIAAprovREA(rs.getString("CCIAA_PROVINCIA_REA"));

        String numTemp = rs.getString("CCIAA_NUMERO_REA");
        if (numTemp != null)
          result.setCCIAAnumeroREA(new Long(rs.getLong("CCIAA_NUMERO_REA")));

        result.setCCIAAnumRegImprese(rs
            .getString("CCIAA_NUMERO_REGISTRO_IMPRESE"));
        result.setCCIAAannoIscrizione(rs.getString("CCIAA_ANNO_ISCRIZIONE"));
        result.setProvCompetenza(rs.getString("PROVINCIA_COMPETENZA"));
        result.setSedelegProv(rs.getString("SEDELEG_PROV"));
        result.setDescComune(rs.getString("DESCOM"));
        result.setSedelegIndirizzo(rs.getString("SEDELEG_INDIRIZZO"));
        result.setSedelegCAP(rs.getString("SEDELEG_CAP"));
        result.setSedelegCittaEstero(rs.getString("SEDELEG_CITTA_ESTERO"));
        result.setMail(rs.getString("MAIL"));
        result.setTelefono(rs.getString("TELEFONO"));
        result.setPec(rs.getString("PEC"));
        result.setDescrizioneProvCompetenza(rs.getString("DESPROV"));
        result.setFascicoloDematerializzato(rs.getString("FASCICOLO_DEMATERIALIZZATO"));
      }
      
      return result;
      
    }
    catch (Throwable t)
    {
      Parametro parametri[] = new Parametro[]
      { new Parametro("idAzienda", idAzienda),
        new Parametro("dataRiferimento", dataRiferimento)  };
      Variabile variabili[] = new Variabile[]
      { new Variabile("result", result), new Variabile("idx", idx)

      };
      LoggerUtils.logDAOError(this,
          "[StampaGaaDAO::getAnagraficaAzienda] ", t, query,
          variabili, parametri);
      throw new DataAccessException(t.getMessage());
    }
    finally
    {
      close(rs, stmt, conn);
      SolmrLogger
          .debug(this, "[StampaGaaDAO::getAnagraficaAzienda] END.");
    }
  }
  
  /**
   * 
   * Usato nel quadro istanza riesame dei tereni
   * 
   * 
   * @param idAzienda
   * @param dataRiferimento
   * @return
   * @throws DataAccessException
   */
  public Vector<ParticellaVO> getParticelleUtilizzoIstanzaRiesame(long idDocumento) 
    throws DataAccessException
  {
    Connection conn = null;
    String query = null;
    PreparedStatement stmt = null;
    ResultSet rs = null;
    StringBuffer queryBuf = null;
    Vector<ParticellaVO> vParticelle = null;
    int idx = 0;
    try
    {
      SolmrLogger
      .debug(
          this,
          "[StampaGaaDAO::getParticelleUtilizzoIstanzaRiesame] BEGIN.");

      queryBuf = new StringBuffer();
    
      /* CONCATENAZIONE/CREAZIONE QUERY BEGIN. */
      queryBuf.append("" +
          "SELECT " +
          "       C.DESCOM || ' (' || P.SIGLA_PROVINCIA || ')' AS DESC_COMUNE," +
          "       SP.SEZIONE," +
          "       SP.FOGLIO," +
          "       SP.PARTICELLA," +
          "       SP.SUBALTERNO, " +
          "       TU.CODICE AS COD_USO, " +
          "       TU.DESCRIZIONE AS DESC_USO, " +
          "       TV.CODICE_VARIETA," +
          "       TV.DESCRIZIONE AS DESC_VARIETA, " +
          "       SP.SUP_CATASTALE, " +
          "       CP.ID_TITOLO_POSSESSO, " +
          "       CP.PERCENTUALE_POSSESSO, " +
          "       CP.SUPERFICIE_CONDOTTA, " +
          "       UP.SUPERFICIE_UTILIZZATA, " +
          "       CP.SUPERFICIE_AGRONOMICA, " +
          "       SP.ID_CASO_PARTICOLARE, " +
          "       SP.ID_AREA_A," +
          "       SP.ID_AREA_B," +
          "       SP.ID_AREA_C," +
          "       SP.ID_AREA_D, "+
          "       DECODE(F.ID_AREA_E,2,'X') AS ZVN," +
          "       SP.ID_ZONA_ALTIMETRICA," +
          "       SP.FLAG_IRRIGABILE," +
          "       SP.FLAG_CAPTAZIONE_POZZI, "+
          "       SP.ID_POTENZIALITA_IRRIGUA, " +
          "       SP.ID_ROTAZIONE_COLTURALE, " +
          "       SP.ID_TERRAZZAMENTO, "+
          "       DC.DATA_FINE_VALIDITA AS DATA_VALIDITA_DOC_COND, " +
          "       DC.NOTE, " +
          "       DC.LAVORAZIONE_PRIORITARIA " +
          "FROM   DB_DOCUMENTO D, " +     
          "       DB_DOCUMENTO_CONDUZIONE DC, " +
          "       DB_CONDUZIONE_PARTICELLA CP, " +
          "       DB_STORICO_PARTICELLA SP, " +
          "       DB_UTILIZZO_PARTICELLA UP, " +
          "       DB_TIPO_UTILIZZO TU," +
          "       DB_TIPO_VARIETA TV, " +
          "       DB_FOGLIO F, " +
          "       COMUNE C, " +
          "       PROVINCIA P " +
          "WHERE  D.ID_DOCUMENTO = ? " +
          "AND    D.ID_DOCUMENTO = DC.ID_DOCUMENTO " +
          "AND    DC.ID_CONDUZIONE_PARTICELLA = CP.ID_CONDUZIONE_PARTICELLA " +
          "AND    CP.ID_PARTICELLA = SP.ID_PARTICELLA " +
          "AND    SP.DATA_INIZIO_VALIDITA <= D.DATA_ULTIMO_AGGIORNAMENTO " +
          "AND    NVL(SP.DATA_FINE_VALIDITA, TO_DATE('31/12/9999', 'dd/mm/yyyy')) >= D.DATA_ULTIMO_AGGIORNAMENTO " +
          "AND    CP.ID_CONDUZIONE_PARTICELLA = UP.ID_CONDUZIONE_PARTICELLA(+) "+
          "AND    UP.ID_UTILIZZO=TU.ID_UTILIZZO(+) "+
          "AND    UP.ID_VARIETA=TV.ID_VARIETA(+) "+
          "AND    SP.COMUNE=F.COMUNE(+) "+
          "AND    NVL(SP.SEZIONE,-1)=NVL(F.SEZIONE(+),-1) "+
          "AND    SP.FOGLIO=F.FOGLIO(+) "+
          "AND    SP.COMUNE = C.ISTAT_COMUNE " +
          "AND    C.ISTAT_PROVINCIA = P.ISTAT_PROVINCIA " +
          "ORDER BY DESC_COMUNE,SP.SEZIONE,SP.FOGLIO,SP.PARTICELLA,SP.SUBALTERNO ");
      /* CONCATENAZIONE/CREAZIONE QUERY END. */
    
      conn = getDatasource().getConnection();
      query = queryBuf.toString();
      if (SolmrLogger.isDebugEnabled(this))
      {
        // Dato che la query costruita dinamicamente è un dato importante la
        // registro sul file di log se il
        // debug è abilitato
    
        SolmrLogger
            .debug(
                this,
                "[StampaGaaDAO::getParticelleUtilizzoIstanzaRiesame] Query="
                    + query);
      }
      stmt = conn.prepareStatement(query);
    
      // Setto i parametri della query
      stmt.setLong(++idx, idDocumento);
      rs = stmt.executeQuery();
      
      while (rs.next())
      {
        if(vParticelle == null)
        {
          vParticelle = new Vector<ParticellaVO>();
        }
        ParticellaVO particella=new ParticellaVO();
        
        particella.setDescComuneParticella(rs.getString("DESC_COMUNE"));
        particella.setSezione(rs.getString("SEZIONE"));
        particella.setFoglio(checkLongNull(rs.getString("FOGLIO")));
        particella.setParticella(checkLongNull(rs.getString("PARTICELLA")));
        particella.setSubalterno(rs.getString("SUBALTERNO"));
        particella.setSupCatastale(rs.getString("SUP_CATASTALE"));
        particella.setIdTitoloPossesso(new Long(rs.getString("ID_TITOLO_POSSESSO")));
        particella.setPercentualePossesso(rs.getBigDecimal("PERCENTUALE_POSSESSO"));
        particella.setSuperficieCondotta(rs.getString("SUPERFICIE_CONDOTTA"));
        particella.setSupAgronomico(rs.getString("SUPERFICIE_AGRONOMICA"));
        particella.setSuperficieUtilizzata(rs.getString("SUPERFICIE_UTILIZZATA"));
        
        String uso=rs.getString("COD_USO");
        if (uso!=null) uso="["+uso+"] "+rs.getString("DESC_USO");
        particella.setDescUtilizzoParticella(uso);        
        String var=rs.getString("CODICE_VARIETA");
        if (var!=null) var="["+var+"] "+rs.getString("DESC_VARIETA");
        particella.setDescVarieta(var);        
        particella.setIdAreaA(checkLongNull(rs.getString("ID_AREA_A")));
        particella.setIdAreaB(checkLongNull(rs.getString("ID_AREA_B")));
        particella.setIdAreaC(checkLongNull(rs.getString("ID_AREA_C")));
        particella.setIdAreaD(checkLongNull(rs.getString("ID_AREA_D")));
        particella.setIdCasoParticolare(checkLongNull(rs.getString("ID_CASO_PARTICOLARE")));
        particella.setZonaVulnerabileNitrati(rs.getString("ZVN"));
        particella.setIdZonaAltimetrica(checkLongNull(rs.getString("ID_ZONA_ALTIMETRICA")));
        
        if (SolmrConstants.FLAG_S.equalsIgnoreCase(rs.getString("FLAG_IRRIGABILE")))
          particella.setFlagIrrigabile(true);
        
        if (SolmrConstants.FLAG_S.equalsIgnoreCase(rs.getString("FLAG_CAPTAZIONE_POZZI")))
          particella.setFlagCaptazionePozzi(true);
        
        
        particella.setIdPotenzaIrrigua(checkLongNull(rs.getString("ID_POTENZIALITA_IRRIGUA")));
        particella.setIdRotazioneColturale(checkLongNull(rs.getString("ID_ROTAZIONE_COLTURALE")));
        particella.setIdTerrazzamento(checkLongNull(rs.getString("ID_TERRAZZAMENTO")));
        
        
        //Data fine valdita di DB_DOCUMENTO_CONDUZIONE
        particella.setDataFineConduzione(rs.getTimestamp("DATA_VALIDITA_DOC_COND"));
        particella.setNote(rs.getString("NOTE"));
        particella.setPrioritaLavorazione(rs.getString("LAVORAZIONE_PRIORITARIA"));
        
        vParticelle.add(particella);        
      }
      
      return vParticelle;
    }
    catch (Throwable t)
    {
      Parametro parametri[] = new Parametro[]
      { new Parametro("idDocumento", idDocumento) };
      Variabile variabili[] = new Variabile[]
      { new Variabile("vParticelle", vParticelle), new Variabile("idx", idx)

      };
      LoggerUtils.logDAOError(this,
          "[StampaGaaDAO::getParticelleUtilizzoIstanzaRiesame] ", t, query,
          variabili, parametri);
      throw new DataAccessException(t.getMessage());
    }
    finally
    {
      close(rs, stmt, conn);
      SolmrLogger
          .debug(this, "[StampaGaaDAO::getParticelleUtilizzoIstanzaRiesame] END.");
    }
  }
  
  
  
  /**
   * Restituisce le somme delle superficie catastale nel quadro terreni
   * dell'istanza di riesame
   * 
   * 
   * @param idDocumento
   * @return
   * @throws DataAccessException
   */
  public BigDecimal getSumSupCatastaleTerreniIstanzaRiesame(long idDocumento) 
    throws DataAccessException
  {
    Connection conn = null;
    String query = null;
    PreparedStatement stmt = null;
    ResultSet rs = null;
    StringBuffer queryBuf = null;
    BigDecimal result = null;
    
    int idx = 0;
    try
    {
      SolmrLogger
      .debug(
          this,
          "[StampaGaaDAO::getSumSupCatastaleTerreniIstanzaRiesame] BEGIN.");
  
      queryBuf = new StringBuffer();
    
      /* CONCATENAZIONE/CREAZIONE QUERY BEGIN. */
      queryBuf.append("" +
          "SELECT SUM(SUP_CATASTALE) AS SUP_CATASTALE " +
          "FROM  ( " +
          "       SELECT SP.SUP_CATASTALE, SP.ID_PARTICELLA " +
          "       FROM   DB_DOCUMENTO D, " +
          "              DB_DOCUMENTO_CONDUZIONE DC, " +
          "              DB_CONDUZIONE_PARTICELLA CP, " +
          "              DB_STORICO_PARTICELLA SP " +
          "       WHERE  D.ID_DOCUMENTO = ? " +
          "       AND    D.ID_DOCUMENTO = DC.ID_DOCUMENTO " +
          "       AND    DC.ID_CONDUZIONE_PARTICELLA = CP.ID_CONDUZIONE_PARTICELLA " +
          "       AND    CP.ID_PARTICELLA = SP.ID_PARTICELLA " +
          "       AND    SP.DATA_INIZIO_VALIDITA <= D.DATA_ULTIMO_AGGIORNAMENTO " +
          "       AND    NVL(SP.DATA_FINE_VALIDITA, TO_DATE('31/12/9999', 'dd/mm/yyyy')) >= D.DATA_ULTIMO_AGGIORNAMENTO " +
          "GROUP BY SP.SUP_CATASTALE, SP.ID_PARTICELLA)");
      /* CONCATENAZIONE/CREAZIONE QUERY END. */
    
      conn = getDatasource().getConnection();
      query = queryBuf.toString();
      if (SolmrLogger.isDebugEnabled(this))
      {
        // Dato che la query costruita dinamicamente è un dato importante la
        // registro sul file di log se il
        // debug è abilitato
    
        SolmrLogger
            .debug(
                this,
                "[StampaGaaDAO::getSumSupCatastaleTerreniIstanzaRiesame] Query="
                    + query);
      }
      stmt = conn.prepareStatement(query);
    
      // Setto i parametri della query
      stmt.setLong(++idx, idDocumento);
      rs = stmt.executeQuery();
      
      if (rs.next())
      {
        result = rs.getBigDecimal("SUP_CATASTALE");
      }
      else
      {
        result = new BigDecimal(0);
      }
      
      return result;
    }
    catch (Throwable t)
    {
      Parametro parametri[] = new Parametro[]
      { new Parametro("idDocumento", idDocumento) };
      Variabile variabili[] = new Variabile[]
      { new Variabile("result", result), new Variabile("idx", idx)
  
      };
      LoggerUtils.logDAOError(this,
          "[StampaGaaDAO::getSumSupCatastaleTerreniIstanzaRiesame] ", t, query,
          variabili, parametri);
      throw new DataAccessException(t.getMessage());
    }
    finally
    {
      close(rs, stmt, conn);
      SolmrLogger
          .debug(this, "[StampaGaaDAO::getSumSupCatastaleTerreniIstanzaRiesame] END.");
    }
  }
  
  
  /**
   * Restituisce le somme delle superficie agronomica e condotta nel quadro terreni
   * dell'istanza di riesame
   * 
   * 
   * @param idDocumento
   * @return
   * @throws DataAccessException
   */
  public BigDecimal[] getSumSupAgronomicaAndCondottaTerreniIstanzaRiesame(long idDocumento) 
    throws DataAccessException
  {
    Connection conn = null;
    String query = null;
    PreparedStatement stmt = null;
    ResultSet rs = null;
    StringBuffer queryBuf = null;
    BigDecimal result[] = new BigDecimal[2];
    
    int idx = 0;
    try
    {
      SolmrLogger
      .debug(
          this,
          "[StampaGaaDAO::getSumSupAgronomicaAndCondottaTerreniIstanzaRiesame] BEGIN.");
  
      queryBuf = new StringBuffer();
    
      /* CONCATENAZIONE/CREAZIONE QUERY BEGIN. */
      queryBuf.append("" +
          "SELECT SUM (CP.SUPERFICIE_CONDOTTA) AS SUPERFICIE_CONDOTTA, " +
          "       SUM (CP.SUPERFICIE_AGRONOMICA) AS SUPERFICIE_AGRONOMICA " +
          "FROM   DB_DOCUMENTO D, " +     
          "       DB_DOCUMENTO_CONDUZIONE DC, " +
          "       DB_CONDUZIONE_PARTICELLA CP " +
          "WHERE  D.ID_DOCUMENTO = ? " +
          "AND    D.ID_DOCUMENTO = DC.ID_DOCUMENTO " +
          "AND    DC.ID_CONDUZIONE_PARTICELLA = CP.ID_CONDUZIONE_PARTICELLA ");
      /* CONCATENAZIONE/CREAZIONE QUERY END. */
    
      conn = getDatasource().getConnection();
      query = queryBuf.toString();
      if (SolmrLogger.isDebugEnabled(this))
      {
        // Dato che la query costruita dinamicamente è un dato importante la
        // registro sul file di log se il
        // debug è abilitato
    
        SolmrLogger
            .debug(
                this,
                "[StampaGaaDAO::getSumSupAgronomicaAndCondottaTerreniIstanzaRiesame] Query="
                    + query);
      }
      stmt = conn.prepareStatement(query);
    
      // Setto i parametri della query
      stmt.setLong(++idx, idDocumento);
      rs = stmt.executeQuery();
      
      while (rs.next())
      {        
        result[0]=rs.getBigDecimal("SUPERFICIE_CONDOTTA");
        if (result[0]==null) result[0]=new BigDecimal(0);
        
        result[1]=rs.getBigDecimal("SUPERFICIE_AGRONOMICA");
        if (result[1]==null) result[1]=new BigDecimal(0);
      }
      
      return result;
    }
    catch (Throwable t)
    {
      Parametro parametri[] = new Parametro[]
      { new Parametro("idDocumento", idDocumento) };
      Variabile variabili[] = new Variabile[]
      { new Variabile("result", result), new Variabile("idx", idx)
  
      };
      LoggerUtils.logDAOError(this,
          "[StampaGaaDAO::getSumSupAgronomicaAndCondottaTerreniIstanzaRiesame] ", t, query,
          variabili, parametri);
      throw new DataAccessException(t.getMessage());
    }
    finally
    {
      close(rs, stmt, conn);
      SolmrLogger
          .debug(this, "[StampaGaaDAO::getSumSupAgronomicaAndCondottaTerreniIstanzaRiesame] END.");
    }
  }
  
  
  /**
   * 
   * Restiusce tutti i record relativi al menu del tipo di stampa
   * 
   * 
   * @param codiceTipoReport
   * @param dataRiferimento
   * @return
   * @throws DataAccessException
   */
  public Vector<TipoReportVO> getElencoTipoReport(String codiceTipoReport, java.util.Date dataRiferimento) throws DataAccessException
  {
    Connection conn = null;
    String query = null;
    PreparedStatement stmt = null;
    ResultSet rs = null;
    StringBuffer queryBuf = null;
    Vector<TipoReportVO> results = null;
    
    
    int idx = 0;
    try
    {
      SolmrLogger
      .debug(
          this,
          "[StampaGaaDAO::getElencoTipoReport] BEGIN.");

      queryBuf = new StringBuffer();
    
      /* CONCATENAZIONE/CREAZIONE QUERY BEGIN. */
      queryBuf.append("" +
          "SELECT TR.ID_TIPO_REPORT, " +
          "       TR.CODICE_REPORT, " +
          "       TR.DESCRIZIONE, " +
          "       TA.FLAG_INSERIBILE " +
          "FROM   DB_TIPO_MENU TM, " +
          "       DB_R_MENU_REPORT RMR, " +
          "       DB_TIPO_REPORT TR, " +
          "       DB_TIPO_ALLEGATO TA " +
          "WHERE  TM.CODICE_MENU = ? " +
          "AND    TM.ID_TIPO_MENU = RMR.ID_TIPO_MENU " +
          "AND    RMR.ID_TIPO_REPORT = TR.ID_TIPO_REPORT " +
          "AND    TR.ID_TIPO_ALLEGATO = TA.ID_TIPO_ALLEGATO (+) ");
      if(Validator.isNotEmpty(dataRiferimento))
      {
        queryBuf.append("" +
          "AND    RMR.DATA_INIZIO_VALIDITA <= ? " +
          "AND    NVL(RMR.DATA_FINE_VALIDITA, TO_DATE('31/12/9999', 'dd/mm/yyyy')) >= ? ");
      }
      else
      {
        queryBuf.append("" +
          "AND    RMR.DATA_FINE_VALIDITA IS NULL ");
      }
      
      queryBuf.append("" +
          "ORDER BY " +
          "        RMR.ORDINAMENTO ");
      /* CONCATENAZIONE/CREAZIONE QUERY END. */
    
      conn = getDatasource().getConnection();
      query = queryBuf.toString();
      if (SolmrLogger.isDebugEnabled(this))
      {
        // Dato che la query costruita dinamicamente è un dato importante la
        // registro sul file di log se il
        // debug è abilitato
    
        SolmrLogger
            .debug(
                this,
                "[StampaGaaDAO::getElencoTipoReport] Query="
                    + query);
      }
      stmt = conn.prepareStatement(query);
    
      // Setto i parametri della query
      stmt.setString(++idx, codiceTipoReport);
      
      if(Validator.isNotEmpty(dataRiferimento))
      {
        stmt.setTimestamp(++idx, convertDateToTimestamp(dataRiferimento));
        stmt.setTimestamp(++idx, convertDateToTimestamp(dataRiferimento));
      }
      rs = stmt.executeQuery();
      
      
      results = new Vector<TipoReportVO>();
      while (rs.next())
      {
        TipoReportVO tipoReportVO = new TipoReportVO();
        tipoReportVO.setIdTipoReport(rs.getInt("ID_TIPO_REPORT"));
        tipoReportVO.setCodiceReport(rs.getString("CODICE_REPORT"));
        tipoReportVO.setDescrizione(rs.getString("DESCRIZIONE"));
        tipoReportVO.setFlagInseribile(rs.getString("FLAG_INSERIBILE"));
        
        results.add(tipoReportVO);
      }
      
      return results;
      
    }
    catch (Throwable t)
    {
      Parametro parametri[] = new Parametro[]
      { new Parametro("codiceTipoReport", codiceTipoReport),
        new Parametro("dataRiferimento", dataRiferimento) };
      Variabile variabili[] = new Variabile[]
      { new Variabile("results", results), new Variabile("idx", idx)

      };
      LoggerUtils.logDAOError(this,
          "[StampaGaaDAO::getElencoTipoReport] ", t, query,
          variabili, parametri);
      throw new DataAccessException(t.getMessage());
    }
    finally
    {
      close(rs, stmt, conn);
      SolmrLogger
          .debug(this, "[StampaGaaDAO::getElencoTipoReport] END.");
    }
  }
  
  
  public Vector<TipoReportVO> getElencoTipoReportByValidazione(
    String codiceTipoReport, Long idDichiarazioneConsistenza) 
      throws DataAccessException
  {
    Connection conn = null;
    String query = null;
    PreparedStatement stmt = null;
    ResultSet rs = null;
    StringBuffer queryBuf = null;
    Vector<TipoReportVO> results = null;
    
    
    int idx = 0;
    try
    {
      SolmrLogger
      .debug(
          this,
          "[StampaGaaDAO::getElencoTipoReportByValidazione] BEGIN.");

      queryBuf = new StringBuffer();
    
      /* CONCATENAZIONE/CREAZIONE QUERY BEGIN. */
      queryBuf.append("" +
          "SELECT TR.ID_TIPO_REPORT, " +
          "       TR.CODICE_REPORT, " +
          "       TR.DESCRIZIONE " +
          "FROM   DB_TIPO_MENU TM, " +
          "       DB_R_MENU_REPORT RMR, " +
          "       DB_TIPO_REPORT TR, " +
          "       DB_DICHIARAZIONE_CONSISTENZA DC, " +
          "       DB_REPORT_MOTIVO_DICHIARAZIONE RMV " +
          "WHERE  TM.CODICE_MENU = ? " +
          "AND    TM.ID_TIPO_MENU = RMR.ID_TIPO_MENU " +
          "AND    RMR.ID_TIPO_REPORT = TR.ID_TIPO_REPORT " +
          "AND    DC.ID_DICHIARAZIONE_CONSISTENZA = ? " +
          "AND    DC.ID_MOTIVO_DICHIARAZIONE = RMV.ID_MOTIVO_DICHIARAZIONE " +
          "AND    RMV.ID_TIPO_REPORT = TR.ID_TIPO_REPORT " +
          "AND    RMR.DATA_INIZIO_VALIDITA <= DC.DATA_INSERIMENTO_DICHIARAZIONE " +
          "AND    NVL(RMR.DATA_FINE_VALIDITA, TO_DATE('31/12/9999', 'dd/mm/yyyy')) >= DC.DATA_INSERIMENTO_DICHIARAZIONE " +
          "ORDER BY " +
          "        RMR.ORDINAMENTO ");
      /* CONCATENAZIONE/CREAZIONE QUERY END. */
    
      conn = getDatasource().getConnection();
      query = queryBuf.toString();
      if (SolmrLogger.isDebugEnabled(this))
      {
        // Dato che la query costruita dinamicamente è un dato importante la
        // registro sul file di log se il
        // debug è abilitato
    
        SolmrLogger
            .debug(
                this,
                "[StampaGaaDAO::getElencoTipoReportByValidazione] Query="
                    + query);
      }
      stmt = conn.prepareStatement(query);
    
      // Setto i parametri della query
      stmt.setString(++idx, codiceTipoReport);
      stmt.setLong(++idx, idDichiarazioneConsistenza.longValue());
      
      
      rs = stmt.executeQuery();
      
      
      results = new Vector<TipoReportVO>();
      while (rs.next())
      {
        TipoReportVO tipoReportVO = new TipoReportVO();
        tipoReportVO.setIdTipoReport(rs.getInt("ID_TIPO_REPORT"));
        tipoReportVO.setCodiceReport(rs.getString("CODICE_REPORT"));
        tipoReportVO.setDescrizione(rs.getString("DESCRIZIONE"));
        
        results.add(tipoReportVO);
      }
      
      return results;
      
    }
    catch (Throwable t)
    {
      Parametro parametri[] = new Parametro[]
      { new Parametro("codiceTipoReport", codiceTipoReport),
        new Parametro("idDichiarazioneConsistenza", idDichiarazioneConsistenza) };
      Variabile variabili[] = new Variabile[]
      { new Variabile("results", results), new Variabile("idx", idx)

      };
      LoggerUtils.logDAOError(this,
          "[StampaGaaDAO::getElencoTipoReportByValidazione] ", t, query,
          variabili, parametri);
      throw new DataAccessException(t.getMessage());
    }
    finally
    {
      close(rs, stmt, conn);
      SolmrLogger
          .debug(this, "[StampaGaaDAO::getElencoTipoReportByValidazione] END.");
    }
  }
  
  /**
   * 
   * Restituisce i dati di DB_DICHIARAZIONE_CONSISTENZA
   * relativi alla stampa
   * 
   * 
   * @param idDichiarazioneConsistenza
   * @return
   * @throws DataAccessException
   */
  public DichiarazioneConsistenzaGaaVO getDatiDichiarazioneConsistenza(
      long idDichiarazioneConsistenza) throws DataAccessException
  {
    Connection conn = null;
    String query = null;
    PreparedStatement stmt = null;
    ResultSet rs = null;
    StringBuffer queryBuf = null;
    DichiarazioneConsistenzaGaaVO result = null;
    
    
    int idx = 0;
    try
    {
      SolmrLogger
      .debug(
          this,
          "[StampaGaaDAO::getDatiDichiarazioneConsistenza] BEGIN.");

      queryBuf = new StringBuffer();
    
      /* CONCATENAZIONE/CREAZIONE QUERY BEGIN. */
      queryBuf.append("" +
          "SELECT " +
          "       DC.DATA_INSERIMENTO_DICHIARAZIONE, " +
          "       DC.DATA_PROTOCOLLO, " +
          "       DC.NUMERO_PROTOCOLLO, " +
          "       DC.DATA, " +
          "       DC.CODICE_FOTOGRAFIA_TERRENI, " +
          "       DC.RESPONSABILE, " +
          "       DC.ANNO, " +
          "       DC.ANNO_CAMPAGNA, " +
          "       MD.DESCRIZIONE AS MOTIVO, " +
          "       MD.TIPO_DICHIARAZIONE, " +
          "       DC.ID_UTENTE, " +
          "       DC.FLAG_INVIO_MAIL " +
          "FROM   DB_DICHIARAZIONE_CONSISTENZA DC," +
          "       DB_TIPO_MOTIVO_DICHIARAZIONE MD " +
          "WHERE  ID_DICHIARAZIONE_CONSISTENZA = ? " +
          "AND    DC.ID_MOTIVO_DICHIARAZIONE = MD.ID_MOTIVO_DICHIARAZIONE ");
      /* CONCATENAZIONE/CREAZIONE QUERY END. */
    
      conn = getDatasource().getConnection();
      query = queryBuf.toString();
      if (SolmrLogger.isDebugEnabled(this))
      {
        // Dato che la query costruita dinamicamente è un dato importante la
        // registro sul file di log se il
        // debug è abilitato
    
        SolmrLogger
            .debug(
                this,
                "[StampaGaaDAO::getDatiDichiarazioneConsistenza] Query="
                    + query);
      }
      stmt = conn.prepareStatement(query);
    
      // Setto i parametri della query
      stmt.setLong(++idx, idDichiarazioneConsistenza);
      
      rs = stmt.executeQuery();
      
      
      if (rs.next())
      {
        result = new DichiarazioneConsistenzaGaaVO();
        result.setDataInserimentoDichiarazione(rs.getTimestamp("DATA_INSERIMENTO_DICHIARAZIONE"));
        result.setDataProtocollo(rs.getTimestamp("DATA_PROTOCOLLO"));
        result.setNumeroProtocollo(rs.getString("NUMERO_PROTOCOLLO"));
        result.setData(rs.getTimestamp("DATA"));
        result.setAnno(checkLong(rs.getString("ANNO")));
        result.setAnnoCampagna(checkLongNull(rs.getString("ANNO_CAMPAGNA")));
        result.setMotivo(rs.getString("MOTIVO"));
        result.setTipoMotivo(rs.getString("TIPO_DICHIARAZIONE"));
        result.setCodiceFotografia(checkLongNull(rs.getString("CODICE_FOTOGRAFIA_TERRENI")));
        result.setResponsabile(rs.getString("RESPONSABILE"));
        result.setIdUtente(checkLongNull(rs.getString("ID_UTENTE")));
        result.setFlagInvioMail(rs.getString("FLAG_INVIO_MAIL"));
      }
      
      return result;
      
    }
    catch (Throwable t)
    {
      Parametro parametri[] = new Parametro[]
      { new Parametro("idDichiarazioneConsistenza", idDichiarazioneConsistenza) };
      Variabile variabili[] = new Variabile[]
      { new Variabile("result", result), new Variabile("idx", idx)

      };
      LoggerUtils.logDAOError(this,
          "[StampaGaaDAO::getDatiDichiarazioneConsistenza] ", t, query,
          variabili, parametri);
      throw new DataAccessException(t.getMessage());
    }
    finally
    {
      close(rs, stmt, conn);
      SolmrLogger
          .debug(this, "[StampaGaaDAO::getDatiDichiarazioneConsistenza] END.");
    }
  }
  
  /**
   * 
   * Recupera le informazioni sull'ente detentore del fascicolo
   * 
   * @param idAzienda
   * @param dataRiferimento
   * @param codFotografia
   * @return
   * @throws DataAccessException
   */
  public InfoFascicoloVO getInfoFascicolo(
      long idAzienda, java.util.Date dataRiferimento, Long codFotografia) throws DataAccessException
  {
    Connection conn = null;
    String query = null;
    PreparedStatement stmt = null;
    ResultSet rs = null;
    StringBuffer queryBuf = null;
    InfoFascicoloVO result = null;
    
    
    int idx = 0;
    try
    {
      SolmrLogger
      .debug(
          this,
          "[StampaGaaDAO::getInfoFascicolo] BEGIN.");

      queryBuf = new StringBuffer();
    
      /* CONCATENAZIONE/CREAZIONE QUERY BEGIN. */
      queryBuf.append("" +
          "SELECT " +
          "       DI.DENOMINAZIONE, " +
          "       DAC.DESCRIZIONE, " +
          "       D.UFFICIO_FASCICOLO, " +
          "       D.INDIRIZZO, " +
          "       D.CAP, " +
          "       C.DESCOM, " +
          "       P.SIGLA_PROVINCIA, " +
          "       DI.CODICE_FISCALE, " +
          "       COM_DI.DESCOM AS DI_DESCOM, " +
          "       PROV_DI.SIGLA_PROVINCIA AS DI_SIGLA_PROVINCIA, " +
          "       DRI.RESPONSABILE " +
          "FROM   DB_AMM_COMPETENZA DAC, " +
          "       DB_INTERMEDIARIO DI, " +
          "       PROVINCIA P, " +
          "       PROVINCIA PROV_DI, " +
          "       COMUNE C, " +
          "       COMUNE COM_DI, " +
          "       DB_DELEGA D, " +
          "       DB_RESPONSABILE_INTERMEDIARIO DRI " +
          "WHERE  D.ID_AZIENDA = ? " +
          "AND    D.ID_PROCEDIMENTO = 7 " +
          "AND    D.COMUNE  = C.ISTAT_COMUNE(+) " +
          "AND    C.ISTAT_PROVINCIA = P.ISTAT_PROVINCIA(+) " +
          "AND    D.ID_INTERMEDIARIO = DI.ID_INTERMEDIARIO(+) " +
          "AND    D.ID_INTERMEDIARIO = DRI.ID_INTERMEDIARIO (+) " +
          "AND    D.CODICE_AMMINISTRAZIONE = DAC.CODICE_AMMINISTRAZIONE(+) " +
          "AND    DI.COMUNE = COM_DI.ISTAT_COMUNE(+) " +
          "AND    COM_DI.ISTAT_PROVINCIA = PROV_DI.ISTAT_PROVINCIA(+) ");
      
      if(codFotografia == null)
      {
        queryBuf.append("" +
          "AND D.DATA_FINE IS NULL ");
        queryBuf.append("" +
          "AND DRI.DATA_FINE (+) IS NULL ");
      }
      else
      {
        queryBuf.append("" +
          "AND D.DATA_INIZIO <= ? " +
          "AND NVL(D.DATA_FINE, TO_DATE('31/12/9999', 'dd/mm/yyyy')) >= ? ");
        queryBuf.append("" +
            "AND DRI.DATA_INIZIO (+) <= ? " +
            "AND NVL(DRI.DATA_FINE (+), TO_DATE('31/12/9999', 'dd/mm/yyyy')) >= ? ");
      }
      
      
      
      /* CONCATENAZIONE/CREAZIONE QUERY END. */
    
      conn = getDatasource().getConnection();
      query = queryBuf.toString();
      if (SolmrLogger.isDebugEnabled(this))
      {
        // Dato che la query costruita dinamicamente è un dato importante la
        // registro sul file di log se il
        // debug è abilitato
    
        SolmrLogger
            .debug(
                this,
                "[StampaGaaDAO::getInfoFascicolo] Query="
                    + query);
      }
      stmt = conn.prepareStatement(query);
    
      // Setto i parametri della query
      stmt.setLong(++idx, idAzienda);
      if(codFotografia !=null)
      { //sono alla dichiarazione di consistenza
        stmt.setTimestamp(++idx, convertDateToTimestamp(dataRiferimento));
        stmt.setTimestamp(++idx, convertDateToTimestamp(dataRiferimento));
        stmt.setTimestamp(++idx, convertDateToTimestamp(dataRiferimento));
        stmt.setTimestamp(++idx, convertDateToTimestamp(dataRiferimento));
      }
      
      rs = stmt.executeQuery();
      
      
      result = new InfoFascicoloVO();
      if (rs.next())
      {
        result.setDenominazione(rs.getString("DENOMINAZIONE")); //0
        result.setDescrizione(rs.getString("DESCRIZIONE")); //1
        result.setIndirizzo(rs.getString("INDIRIZZO")); //2
        result.setCap(rs.getString("CAP")); //3
        result.setDescComune(rs.getString("DESCOM")); //4
        result.setSiglaProv(rs.getString("SIGLA_PROVINCIA")); //5
        result.setCodiceFiscale(rs.getString("CODICE_FISCALE")); //6
        result.setDescComuneIntermediario(rs.getString("DI_DESCOM")); //7
        result.setSiglaProvIntermediario(rs.getString("DI_SIGLA_PROVINCIA")); //8
        result.setResponsabile(rs.getString("RESPONSABILE"));
      }
      
      return result;
      
    }
    catch (Throwable t)
    {
      Parametro parametri[] = new Parametro[]
      { new Parametro("idAzienda", idAzienda),
        new Parametro("dataRiferimento", dataRiferimento),
        new Parametro("codFotografia", codFotografia)};
      Variabile variabili[] = new Variabile[]
      { new Variabile("result", result), new Variabile("idx", idx)

      };
      LoggerUtils.logDAOError(this,
          "[StampaGaaDAO::getInfoFascicolo] ", t, query,
          variabili, parametri);
      throw new DataAccessException(t.getMessage());
    }
    finally
    {
      close(rs, stmt, conn);
      SolmrLogger
          .debug(this, "[StampaGaaDAO::getInfoFascicolo] END.");
    }
  }
  
  
  /**
   * Estrae tuttele attestazioni al piano attuale per gli allegati
   * in baso al tipo di allegato
   * 
   * 
   * @param idAzienda
   * @param idAllegato
   * @return
   * @throws DataAccessException
   */
  public TipoAttestazioneVO[] getListAttestazioniAlPianoAttuale(
      Long idAzienda, Long idAllegato) throws DataAccessException 
  {
    
    Connection conn = null;
    String query = null;
    PreparedStatement stmt = null;
    ResultSet rs = null;
    StringBuffer queryBuf = null;
    TipoAttestazioneVO[] result = null;
    Vector<TipoAttestazioneVO> elencoTipoAttestazione = new Vector<TipoAttestazioneVO>();

    
    int idx = 0;
    try
    {
      SolmrLogger
      .debug(
          this,
          "[StampaGaaDAO::getListAttestazioniAlPianoAttuale] BEGIN.");

      queryBuf = new StringBuffer();
    
      /* CONCATENAZIONE/CREAZIONE QUERY BEGIN. */
      queryBuf.append("" + 
          "SELECT TA.ID_ATTESTAZIONE, " +
          "       TA.CODICE_ATTESTAZIONE, " +
          "       TA.DESCRIZIONE, " +
          "       TA.DATA_INIZIO_VALIDITA, " +
          "       TA.DATA_FINE_VALIDITA, " +
          "       TA.TIPO_RIGA, " +
          "       TA.NUMERO_COLONNE_RIGA, " +
          "       TA.ID_ATTESTAZIONE_PADRE, " +
          "       TA.TIPO_CARATTERE, " +
          "       TA.GRUPPO, " +
          "       TA.ORDINAMENTO, " +
          "       TA.OBBLIGATORIO, " +
          "       TA.DISABILITATO, " +
          "       TA.SELEZIONA_VIDEO, " +
          "       TA.ATTESTAZIONI_CORRELATE, " +
          "       AA.ID_ATTESTAZIONE_AZIENDA, " +
          "       PAA.ID_PARAMETRI_ATT_AZIENDA, " +
          "       PAA.PARAMETRO_1, " +
          "       PAA.PARAMETRO_2, " +
          "       PAA.PARAMETRO_3, " +
          "       PAA.PARAMETRO_4, " +
          "       PAA.PARAMETRO_5, " +
          "       TPA.ID_PARAMETRI_ATTESTAZIONE, " +
          "       TPA.PARAMETRO_1 AS TIPO_PAR_1, " +
          "       TPA.PARAMETRO_2 AS TIPO_PAR_2, " +
          "       TPA.PARAMETRO_3 AS TIPO_PAR_3, " +
          "       TPA.PARAMETRO_4 AS TIPO_PAR_4, " +
          "       TPA.PARAMETRO_5 AS TIPO_PAR_5, " +
          "       TPA.OBBLIGATORIO_1, " +
          "       TPA.OBBLIGATORIO_2, " +
          "       TPA.OBBLIGATORIO_3, " +
          "       TPA.OBBLIGATORIO_4, " +
          "       TPA.OBBLIGATORIO_5 " +
          "FROM   DB_TIPO_ATTESTAZIONE TA, " +
          "       DB_ATTESTAZIONE_AZIENDA AA, " +
          "       DB_PARAMETRI_ATT_AZIENDA PAA, " +
          "       DB_TIPO_PARAMETRI_ATTESTAZIONE TPA, " +
          "       DB_R_ATTEST_REPORT_SUB_REPORT ARS " +
          "WHERE  TA.DATA_INIZIO_VALIDITA < SYSDATE " +
          "AND    NVL(TA.DATA_FINE_VALIDITA, TO_DATE('31/12/9999','dd/mm/yyyy')) > SYSDATE " +
          "AND    TA.ID_ATTESTAZIONE = AA.ID_ATTESTAZIONE(+) " +
          "AND    AA.ID_AZIENDA (+) = ? " +
          "AND    AA.ID_ATTESTAZIONE_AZIENDA = PAA.ID_ATTESTAZIONE_AZIENDA(+) " +
          "AND    TA.ID_ATTESTAZIONE = TPA.ID_ATTESTAZIONE(+) " +
          "AND    TA.FLAG_STAMPA = 'S' " +
          "AND    TA.ID_ATTESTAZIONE = ARS.ID_ATTESTAZIONE " +
          "AND    ARS.DATA_INIZIO_VALIDITA < SYSDATE " +
          "AND    NVL(ARS.DATA_FINE_VALIDITA, TO_DATE('31/12/9999','dd/mm/yyyy')) > SYSDATE " +
          "AND    ARS.ID_REPORT_SUB_REPORT = ?  " +
          "ORDER BY TA.GRUPPO ASC, TA.ORDINAMENTO ASC ");

      /* CONCATENAZIONE/CREAZIONE QUERY END. */
      
      conn = getDatasource().getConnection();
      query = queryBuf.toString();
      if (SolmrLogger.isDebugEnabled(this))
      {
        // Dato che la query costruita dinamicamente è un dato importante la
        // registro sul file di log se il
        // debug è abilitato
    
        SolmrLogger
            .debug(
                this,
                "[StampaGaaDAO::getListAttestazioniAlPianoAttuale] Query="
                    + query);
      }
      stmt = conn.prepareStatement(query);
    
      // Setto i parametri della query
      stmt.setLong(++idx, idAzienda.longValue());
      stmt.setLong(++idx, idAllegato.longValue());
      
      rs = stmt.executeQuery();
  
      Long idAttestazioneConfronto = null;
      while(rs.next()) {
        TipoAttestazioneVO tipoAttestazioneVO = new TipoAttestazioneVO();
        tipoAttestazioneVO.setIdAttestazione(new Long(rs.getLong("ID_ATTESTAZIONE")));
        tipoAttestazioneVO.setCodiceAttestazione(rs.getString("CODICE_ATTESTAZIONE"));
        tipoAttestazioneVO.setDescrizione(rs.getString("DESCRIZIONE"));
        tipoAttestazioneVO.setDataInizioValidita(rs.getDate("DATA_INIZIO_VALIDITA"));
        tipoAttestazioneVO.setDataFineValidita(rs.getDate("DATA_FINE_VALIDITA"));
        tipoAttestazioneVO.setTipoRiga(rs.getString("TIPO_RIGA"));
        tipoAttestazioneVO.setNumeroColonneRiga(rs.getString("NUMERO_COLONNE_RIGA"));
        tipoAttestazioneVO.setIdAttestazionePadre(new Long(rs.getLong("ID_ATTESTAZIONE_PADRE")));
        tipoAttestazioneVO.setTipoCarattere(rs.getString("TIPO_CARATTERE"));
        tipoAttestazioneVO.setGruppo(rs.getString("GRUPPO"));
        tipoAttestazioneVO.setOrdinamento(rs.getString("ORDINAMENTO"));
        tipoAttestazioneVO.setObbligatorio(rs.getString("OBBLIGATORIO"));
        tipoAttestazioneVO.setDisabilitato(rs.getString("DISABILITATO"));
        tipoAttestazioneVO.setSelezionaVideo(rs.getString("SELEZIONA_VIDEO"));
        tipoAttestazioneVO.setAttestazioniCorrelate(rs.getString("ATTESTAZIONI_CORRELATE"));
        if(Validator.isNotEmpty(rs.getString("ID_ATTESTAZIONE_AZIENDA"))) 
        {
          if(tipoAttestazioneVO.getAttestazioniCorrelate().equalsIgnoreCase(SolmrConstants.FLAG_S)) 
          {
            idAttestazioneConfronto = tipoAttestazioneVO.getIdAttestazione();
          }
          tipoAttestazioneVO.setAttestazioneAzienda(true);
        }
        else 
        {
          if(idAttestazioneConfronto != null 
              && idAttestazioneConfronto.compareTo(tipoAttestazioneVO.getIdAttestazionePadre()) == 0) 
          {
            tipoAttestazioneVO.setAttestazioneAzienda(true);
          }
        }
        if(Validator.isNotEmpty(rs.getString("ID_PARAMETRI_ATT_AZIENDA"))) 
        {
          ParametriAttAziendaVO parametriAttAziendaVO = new ParametriAttAziendaVO();
          parametriAttAziendaVO.setIdParametriAttAzienda(new Long(rs.getLong("ID_PARAMETRI_ATT_AZIENDA")));
          parametriAttAziendaVO.setParametro1(rs.getString("PARAMETRO_1"));
          parametriAttAziendaVO.setParametro2(rs.getString("PARAMETRO_2"));
          parametriAttAziendaVO.setParametro3(rs.getString("PARAMETRO_3"));
          parametriAttAziendaVO.setParametro4(rs.getString("PARAMETRO_4"));
          parametriAttAziendaVO.setParametro5(rs.getString("PARAMETRO_5"));
          tipoAttestazioneVO.setParametriAttAziendaVO(parametriAttAziendaVO);
        }
        if(Validator.isNotEmpty(rs.getString("ID_PARAMETRI_ATTESTAZIONE"))) 
        {
          TipoParametriAttestazioneVO tipoParametriAttestazioneVO = new TipoParametriAttestazioneVO();
          tipoParametriAttestazioneVO.setIdParametriAttestazione(new Long(rs.getLong("ID_PARAMETRI_ATTESTAZIONE")));
          tipoParametriAttestazioneVO.setIdAttestazione(new Long(rs.getLong("ID_ATTESTAZIONE")));
          tipoParametriAttestazioneVO.setParametro1(rs.getString("TIPO_PAR_1"));
          tipoParametriAttestazioneVO.setParametro2(rs.getString("TIPO_PAR_2"));
          tipoParametriAttestazioneVO.setParametro3(rs.getString("TIPO_PAR_3"));
          tipoParametriAttestazioneVO.setParametro4(rs.getString("TIPO_PAR_4"));
          tipoParametriAttestazioneVO.setParametro5(rs.getString("TIPO_PAR_5"));
          tipoParametriAttestazioneVO.setObbligatorio1(rs.getString("OBBLIGATORIO_1"));
          tipoParametriAttestazioneVO.setObbligatorio2(rs.getString("OBBLIGATORIO_2"));
          tipoParametriAttestazioneVO.setObbligatorio3(rs.getString("OBBLIGATORIO_3"));
          tipoParametriAttestazioneVO.setObbligatorio4(rs.getString("OBBLIGATORIO_4"));
          tipoParametriAttestazioneVO.setObbligatorio5(rs.getString("OBBLIGATORIO_5"));
          tipoAttestazioneVO.setTipoParametriAttestazioneVO(tipoParametriAttestazioneVO);
        }
        elencoTipoAttestazione.add(tipoAttestazioneVO);
      }

      if(elencoTipoAttestazione.size() == 0) {
        return (TipoAttestazioneVO[])elencoTipoAttestazione.toArray(new TipoAttestazioneVO[0]);
      }
      else {
        return (TipoAttestazioneVO[])elencoTipoAttestazione.toArray(new TipoAttestazioneVO[elencoTipoAttestazione.size()]);
      }

    }
    catch (Throwable t)
    {
      Parametro parametri[] = new Parametro[]
      { new Parametro("idAzienda", idAzienda),
        new Parametro("idAllegato", idAllegato)};
      Variabile variabili[] = new Variabile[]
      { new Variabile("result", result), new Variabile("idx", idx)

      };
      LoggerUtils.logDAOError(this,
          "[StampaGaaDAO::getListAttestazioniAlPianoAttuale] ", t, query,
          variabili, parametri);
      throw new DataAccessException(t.getMessage());
    }
    finally
    {
      close(rs, stmt, conn);
      SolmrLogger
          .debug(this, "[StampaGaaDAO::getListAttestazioniAlPianoAttuale] END.");
    }
  }
  
  
  /**
   * 
   * Estrae tuttele attestazioni alla dichiarazione di consistenza per gli allegati
   * in baso al tipo di allegato
   * 
   * 
   * @param codiceFotografiaTerreni
   * @param dataInserimentoDichiarazione
   * @param idAllegato
   * @return
   * @throws DataAccessException
   */
  public TipoAttestazioneVO[] getListAttestazioneAllaDichiarazione(
    Long codiceFotografiaTerreni, java.util.Date dataAnnoCampagna, Long idAllegato) 
  throws DataAccessException 
  {
    Connection conn = null;
    String query = null;
    PreparedStatement stmt = null;
    ResultSet rs = null;
    StringBuffer queryBuf = null;
    TipoAttestazioneVO[] result = null;
    Vector<TipoAttestazioneVO> elencoTipoAttestazione = new Vector<TipoAttestazioneVO>();

    int idx = 0;
    try
    {
      SolmrLogger
      .debug(
          this,
          "[StampaGaaDAO::getListAttestazioneAllaDichiarazione] BEGIN.");

      queryBuf = new StringBuffer();
    
      /* CONCATENAZIONE/CREAZIONE QUERY BEGIN. */
      queryBuf.append("" +
          "   WITH DATI_DICHIARATI AS " +
          "  (SELECT AD.*,  " +
          "          PAD.ID_PARAMETRI_ATT_DICHIARATA,   " +
          "          PAD.PARAMETRO_1,   " +
          "          PAD.PARAMETRO_2,   " +
          "          PAD.PARAMETRO_3,   " +
          "          PAD.PARAMETRO_4,   " +
          "          PAD.PARAMETRO_5  " +
          "     FROM DB_ATTESTAZIONE_DICHIARATA AD,   " +
          "          DB_PARAMETRI_ATT_DICHIARATA PAD    " +
          "    WHERE AD.CODICE_FOTOGRAFIA_TERRENI(+) = ?    " +
          "      AND AD.DATA_FINE_VALIDITA IS NULL    " +
          "      AND AD.ID_ATTESTAZIONE_DICHIARATA = PAD.ID_ATTESTAZIONE_DICHIARATA(+) ) " + 
          "SELECT TA.ID_ATTESTAZIONE, " +
          "       TA.CODICE_ATTESTAZIONE, " +
          "       TA.DESCRIZIONE, " +
          "       TA.DATA_INIZIO_VALIDITA, " +
          "       TA.DATA_FINE_VALIDITA, " +
          "       TA.TIPO_RIGA, " +
          "       TA.NUMERO_COLONNE_RIGA, " +
          "       TA.ID_ATTESTAZIONE_PADRE, " +
          "       TA.TIPO_CARATTERE, " +
          "       TA.GRUPPO, " +
          "       TA.ORDINAMENTO, " +
          "       TA.OBBLIGATORIO, " +
          "       TA.DISABILITATO, " +
          "       TA.SELEZIONA_VIDEO, " +
          "       TA.ATTESTAZIONI_CORRELATE, " +
          "       DATI_DICHIARATI.ID_ATTESTAZIONE_DICHIARATA, " +
          "       DATI_DICHIARATI.ID_PARAMETRI_ATT_DICHIARATA, " +
          "       DATI_DICHIARATI.PARAMETRO_1, " +
          "       DATI_DICHIARATI.PARAMETRO_2, " +
          "       DATI_DICHIARATI.PARAMETRO_3, " +
          "       DATI_DICHIARATI.PARAMETRO_4, " +
          "       DATI_DICHIARATI.PARAMETRO_5, " +
          "       TPA.ID_PARAMETRI_ATTESTAZIONE, " +
          "       TPA.PARAMETRO_1 AS TIPO_PAR_1, " +
          "       TPA.PARAMETRO_2 AS TIPO_PAR_2, " +
          "       TPA.PARAMETRO_3 AS TIPO_PAR_3, " +
          "       TPA.PARAMETRO_4 AS TIPO_PAR_4, " +
          "       TPA.PARAMETRO_5 AS TIPO_PAR_5, " +
          "       TPA.OBBLIGATORIO_1, " +
          "       TPA.OBBLIGATORIO_2, " +
          "       TPA.OBBLIGATORIO_3, " +
          "       TPA.OBBLIGATORIO_4, " +
          "       TPA.OBBLIGATORIO_5 " +
          "FROM   DB_TIPO_ATTESTAZIONE TA, " +
          "       DATI_DICHIARATI, " +
          "       DB_TIPO_PARAMETRI_ATTESTAZIONE TPA, " +
          "       DB_R_ATTEST_REPORT_SUB_REPORT ARS " +
          "WHERE  TA.ID_ATTESTAZIONE = DATI_DICHIARATI.ID_ATTESTAZIONE(+)  " +
          "AND    TA.DATA_INIZIO_VALIDITA <= ? " +
          "AND    NVL(TA.DATA_FINE_VALIDITA, TO_DATE('31/12/9999', 'DD/MM/YYYY')) > ? " +
          "AND    TA.ID_ATTESTAZIONE = TPA.ID_ATTESTAZIONE(+) " +
          "AND    TA.FLAG_STAMPA = 'S' " +
          "AND    TA.ID_ATTESTAZIONE = ARS.ID_ATTESTAZIONE " +
          "AND    ARS.DATA_INIZIO_VALIDITA <= ? " +
          "AND    NVL(ARS.DATA_FINE_VALIDITA, TO_DATE('31/12/9999', 'DD/MM/YYYY')) > ? " + 
          "AND    ARS.ID_REPORT_SUB_REPORT = ? " +
          "ORDER BY TA.GRUPPO ASC, TA.ORDINAMENTO ASC ");
      /* CONCATENAZIONE/CREAZIONE QUERY END. */
      
      conn = getDatasource().getConnection();
      query = queryBuf.toString();
      if (SolmrLogger.isDebugEnabled(this))
      {
        // Dato che la query costruita dinamicamente è un dato importante la
        // registro sul file di log se il
        // debug è abilitato
    
        SolmrLogger
            .debug(
                this,
                "[StampaGaaDAO::getListAttestazioneAllaDichiarazione] Query="
                    + query);
      }
      stmt = conn.prepareStatement(query);
    
      // Setto i parametri della query
      stmt.setLong(++idx, codiceFotografiaTerreni.longValue());
      stmt.setTimestamp(++idx, convertDateToTimestamp(dataAnnoCampagna));
      stmt.setTimestamp(++idx, convertDateToTimestamp(dataAnnoCampagna));
      stmt.setTimestamp(++idx, convertDateToTimestamp(dataAnnoCampagna));
      stmt.setTimestamp(++idx, convertDateToTimestamp(dataAnnoCampagna));
      stmt.setLong(++idx, idAllegato.longValue());
      
      rs = stmt.executeQuery();

      Long idAttestazioneConfronto = null;
      while(rs.next()) 
      {
        TipoAttestazioneVO tipoAttestazioneVO = new TipoAttestazioneVO();
        tipoAttestazioneVO.setIdAttestazione(new Long(rs.getLong("ID_ATTESTAZIONE")));
        tipoAttestazioneVO.setCodiceAttestazione(rs.getString("CODICE_ATTESTAZIONE"));
        tipoAttestazioneVO.setDescrizione(rs.getString("DESCRIZIONE"));
        tipoAttestazioneVO.setDataInizioValidita(rs.getDate("DATA_INIZIO_VALIDITA"));
        tipoAttestazioneVO.setDataFineValidita(rs.getDate("DATA_FINE_VALIDITA"));
        tipoAttestazioneVO.setTipoRiga(rs.getString("TIPO_RIGA"));
        tipoAttestazioneVO.setNumeroColonneRiga(rs.getString("NUMERO_COLONNE_RIGA"));
        tipoAttestazioneVO.setIdAttestazionePadre(new Long(rs.getLong("ID_ATTESTAZIONE_PADRE")));
        tipoAttestazioneVO.setTipoCarattere(rs.getString("TIPO_CARATTERE"));
        tipoAttestazioneVO.setGruppo(rs.getString("GRUPPO"));
        tipoAttestazioneVO.setOrdinamento(rs.getString("ORDINAMENTO"));
        tipoAttestazioneVO.setObbligatorio(rs.getString("OBBLIGATORIO"));
        tipoAttestazioneVO.setDisabilitato(rs.getString("DISABILITATO"));
        tipoAttestazioneVO.setSelezionaVideo(rs.getString("SELEZIONA_VIDEO"));
        tipoAttestazioneVO.setAttestazioniCorrelate(rs.getString("ATTESTAZIONI_CORRELATE"));
        if(Validator.isNotEmpty(rs.getString("ID_ATTESTAZIONE_DICHIARATA"))) 
        {
          if(tipoAttestazioneVO.getAttestazioniCorrelate().equalsIgnoreCase(SolmrConstants.FLAG_S)) 
          {
            idAttestazioneConfronto = tipoAttestazioneVO.getIdAttestazione();
          }
          tipoAttestazioneVO.setAttestazioneAzienda(true);
        }
        else 
        {
          if(idAttestazioneConfronto != null 
               && idAttestazioneConfronto.compareTo(tipoAttestazioneVO.getIdAttestazionePadre()) == 0) 
          {
            tipoAttestazioneVO.setAttestazioneAzienda(true);
          }
        }
        if(Validator.isNotEmpty(rs.getString("ID_PARAMETRI_ATT_DICHIARATA"))) 
        {
          ParametriAttDichiarataVO parametriAttDichiarataVO = new ParametriAttDichiarataVO();
          parametriAttDichiarataVO.setIdParametriAttDichiarata(new Long(rs.getLong("ID_PARAMETRI_ATT_DICHIARATA")));
          parametriAttDichiarataVO.setParametro1(rs.getString("PARAMETRO_1"));
          parametriAttDichiarataVO.setParametro2(rs.getString("PARAMETRO_2"));
          parametriAttDichiarataVO.setParametro3(rs.getString("PARAMETRO_3"));
          parametriAttDichiarataVO.setParametro4(rs.getString("PARAMETRO_4"));
          parametriAttDichiarataVO.setParametro5(rs.getString("PARAMETRO_5"));
          tipoAttestazioneVO.setParametriAttDichiarataVO(parametriAttDichiarataVO);
        }
        if(Validator.isNotEmpty(rs.getString("ID_PARAMETRI_ATTESTAZIONE"))) 
        {
          TipoParametriAttestazioneVO tipoParametriAttestazioneVO = new TipoParametriAttestazioneVO();
          tipoParametriAttestazioneVO.setIdParametriAttestazione(new Long(rs.getLong("ID_PARAMETRI_ATTESTAZIONE")));
          tipoParametriAttestazioneVO.setIdAttestazione(new Long(rs.getLong("ID_ATTESTAZIONE")));
          tipoParametriAttestazioneVO.setParametro1(rs.getString("TIPO_PAR_1"));
          tipoParametriAttestazioneVO.setParametro2(rs.getString("TIPO_PAR_2"));
          tipoParametriAttestazioneVO.setParametro3(rs.getString("TIPO_PAR_3"));
          tipoParametriAttestazioneVO.setParametro4(rs.getString("TIPO_PAR_4"));
          tipoParametriAttestazioneVO.setParametro5(rs.getString("TIPO_PAR_5"));
          tipoParametriAttestazioneVO.setObbligatorio1(rs.getString("OBBLIGATORIO_1"));
          tipoParametriAttestazioneVO.setObbligatorio2(rs.getString("OBBLIGATORIO_2"));
          tipoParametriAttestazioneVO.setObbligatorio3(rs.getString("OBBLIGATORIO_3"));
          tipoParametriAttestazioneVO.setObbligatorio4(rs.getString("OBBLIGATORIO_4"));
          tipoParametriAttestazioneVO.setObbligatorio5(rs.getString("OBBLIGATORIO_5"));
          tipoAttestazioneVO.setTipoParametriAttestazioneVO(tipoParametriAttestazioneVO);
        }
        elencoTipoAttestazione.add(tipoAttestazioneVO);
      }

      if(elencoTipoAttestazione.size() == 0) {
        return (TipoAttestazioneVO[])elencoTipoAttestazione.toArray(new TipoAttestazioneVO[0]);
      }
      else {
        return (TipoAttestazioneVO[])elencoTipoAttestazione.toArray(new TipoAttestazioneVO[elencoTipoAttestazione.size()]);
      }

    }
    catch (Throwable t)
    {
      Parametro parametri[] = new Parametro[]
      { new Parametro("codiceFotografiaTerreni", codiceFotografiaTerreni),
        new Parametro("dataAnnoCampagna", dataAnnoCampagna),
        new Parametro("idAllegato", idAllegato)};
      Variabile variabili[] = new Variabile[]
      { new Variabile("result", result), new Variabile("idx", idx)

      };
      LoggerUtils.logDAOError(this,
          "[StampaGaaDAO::getListAttestazioneAllaDichiarazione] ", t, query,
          variabili, parametri);
      throw new DataAccessException(t.getMessage());
    }
    finally
    {
      close(rs, stmt, conn);
      SolmrLogger
          .debug(this, "[StampaGaaDAO::getListAttestazioneAllaDichiarazione] END.");
    }
  }
  
  
  
  //===================================================================
  // Quadro A - Sez III
  //
  // Recupero conti correnti con idAzienda e idDichiarazioneConsistenza
  // ===================================================================
  public Vector<ContoCorrenteVO> getStampaContiCorrenti(
    Long idAzienda, java.util.Date dataInserimentoDichiarazione)
      throws DataAccessException
  {
    
    Connection conn = null;
    String query = null;
    PreparedStatement stmt = null;
    ResultSet rs = null;
    StringBuffer queryBuf = null;
    Vector<ContoCorrenteVO> result = new Vector<ContoCorrenteVO>();
    
    int idx = 0;
    try
    {
      SolmrLogger
      .debug(
          this,
          "[StampaGaaDAO::getStampaContiCorrenti] BEGIN.");

      queryBuf = new StringBuffer();
    
      /* CONCATENAZIONE/CREAZIONE QUERY BEGIN. */
      queryBuf.append("" + 
          "SELECT TB.ABI, " +
          "       TS.CAB, " +
          "       CC.NUMERO_CONTO_CORRENTE, " +
          "       CC.CIN, " +
          "       TB.DENOMINAZIONE ISTITUTO, " +
          "       TS.DENOMINAZIONE AGENZIA, " +
          "       TS.INDIRIZZO INDIRIZZO_SPORTELLO, " +
          "       C.DESCOM, " +
          "       P.SIGLA_PROVINCIA, " +
          "       TS.CAP, " +
          "       CC.IBAN, " +
          "       CC.CIFRA_CONTROLLO," +
          "       TS.CODICE_PAESE " +
          "FROM   PROVINCIA P, " +
          "       COMUNE C, " +
          "       DB_TIPO_BANCA TB, " +
          "       DB_TIPO_SPORTELLO TS, " +
          "       DB_CONTO_CORRENTE CC " +
          "WHERE  CC.ID_AZIENDA = ? " +
          "AND   (  " +
          "         (CC.DATA_FINE_VALIDITA IS NOT NULL AND " +
          "             ( ? BETWEEN TRUNC(CC.DATA_INIZIO_VALIDITA) " +
          "               AND TRUNC(CC.DATA_FINE_VALIDITA ) " +
          "             ) " +
          "         ) " +
          "         OR " +
          "         ( CC.DATA_FINE_VALIDITA IS NULL     " +
          "           AND TRUNC(CC.DATA_INIZIO_VALIDITA) <= ? ) " +
          "       ) " +
          "AND    ( CC.DATA_ESTINZIONE IS NULL OR TRUNC(CC.DATA_ESTINZIONE) > ? ) " +
          "AND    TS.ID_SPORTELLO = CC.ID_SPORTELLO " +
          "AND    TB.ID_BANCA = TS.ID_BANCA " +
          "AND    C.ISTAT_COMUNE(+) = TS.COMUNE " +
          "AND    P.ISTAT_PROVINCIA(+) = C.ISTAT_PROVINCIA " +
          "ORDER BY ISTITUTO, AGENZIA");

      /* CONCATENAZIONE/CREAZIONE QUERY END. */
      
      conn = getDatasource().getConnection();
      query = queryBuf.toString();
      if (SolmrLogger.isDebugEnabled(this))
      {
        // Dato che la query costruita dinamicamente è un dato importante la
        // registro sul file di log se il
        // debug è abilitato
    
        SolmrLogger
            .debug(
                this,
                "[StampaGaaDAO::getStampaContiCorrenti] Query="
                    + query);
      }
      stmt = conn.prepareStatement(query);
    
      // Setto i parametri della query
      stmt.setLong(++idx, idAzienda.longValue());
      stmt.setTimestamp(++idx, this.convertDateToTimestamp(dataInserimentoDichiarazione));
      stmt.setTimestamp(++idx, this.convertDateToTimestamp(dataInserimentoDichiarazione));
      stmt.setTimestamp(++idx, this.convertDateToTimestamp(dataInserimentoDichiarazione)); // DATA_ESTINZIONE

      rs = stmt.executeQuery();

      while (rs.next())
      {
        ContoCorrenteVO ccVO = new ContoCorrenteVO();
        ccVO.setAbi(rs.getString("ABI"));
        ccVO.setCab(rs.getString("CAB"));
        ccVO.setNumeroContoCorrente(rs.getString("NUMERO_CONTO_CORRENTE"));
        ccVO.setCin(rs.getString("CIN"));
        ccVO.setDenominazioneBanca(rs.getString("ISTITUTO"));
        ccVO.setDenominazioneSportello(rs.getString("AGENZIA"));
        ccVO.setIndirizzoSportello(rs.getString("INDIRIZZO_SPORTELLO"));
        ccVO.setDescrizioneComuneSportello(rs.getString("DESCOM"));
        ccVO.setSiglaProvincia(rs.getString("SIGLA_PROVINCIA"));
        ccVO.setCapSportello(rs.getString("CAP"));
        ccVO.setCifraCtrl(rs.getString("CIFRA_CONTROLLO"));
        ccVO.setCodPaese(rs.getString("CODICE_PAESE"));
        ccVO.setIban(rs.getString("IBAN"));

        result.add(ccVO);
      }
      
      return result;

    }
    catch (Throwable t)
    {
      Parametro parametri[] = new Parametro[]
      { new Parametro("idAzienda", idAzienda),
        new Parametro("dataInserimentoDichiarazione", dataInserimentoDichiarazione)};
      Variabile variabili[] = new Variabile[]
      { new Variabile("result", result), new Variabile("idx", idx)

      };
      LoggerUtils.logDAOError(this,
          "[StampaGaaDAO::getStampaContiCorrenti] ", t, query,
          variabili, parametri);
      throw new DataAccessException(t.getMessage());
    }
    finally
    {
      close(rs, stmt, conn);
      SolmrLogger
          .debug(this, "[StampaGaaDAO::getStampaContiCorrenti] END.");
    }
  }
  
  
  
  /**
   * 
   * Restituisce il codice report per la stampa relativo al tipo documento
   * dell'id_documento
   * 
   * 
   * 
   * @param idDocumento
   * @return
   * @throws DataAccessException
   */
  public Vector<TipoReportVO> getListTipiDocumentoStampaProtocollo(long idDocumento[]) 
    throws DataAccessException
  {
    String query = null;
    Connection conn = null;
    PreparedStatement stmt = null;
    StringBuffer queryBuf = null;
    Vector<TipoReportVO> vTipoReport = null;
    TipoReportVO tipoReportVO = null;
    
    try
    {
      SolmrLogger
          .debug(this,
              "[StampaGaaDAO::getListTipiDocumentoStampaProtocollo] BEGIN.");
  
      /* CONCATENAZIONE/CREAZIONE QUERY BEGIN. */
      
      String parametri=" (";
      
      if (idDocumento!=null && idDocumento.length!=0)
      {
        for(int i=0;i<idDocumento.length;i++)
        {
          parametri+="?";
          if (i<idDocumento.length-1)
            parametri+=",";
        }
      }
      parametri+=") ";
  
      queryBuf = new StringBuffer();
      queryBuf.append("" +
        "SELECT TR.ID_TIPO_REPORT," +
        "       TR.CODICE_REPORT," +
        "       TR.FLAG_SCELTA_PROPRIETARI " +
        "FROM   DB_DOCUMENTO DOC, " +
        "       DB_TIPO_DOCUMENTO TD," +
        "       DB_R_TIPO_DOCUMENTO_REPORT TDR," +
        "       DB_TIPO_REPORT TR " +
        "WHERE  DOC.ID_DOCUMENTO IN " +parametri +
        "AND    DOC.EXT_ID_DOCUMENTO = TD.ID_DOCUMENTO  " +
        "AND    TD.ID_DOCUMENTO = TDR.ID_DOCUMENTO " +
        "AND    TDR.ID_TIPO_REPORT = TR.ID_TIPO_REPORT " +
        "AND    TDR.DATA_FINE_VALIDITA IS NULL " +
        "GROUP BY TR.ID_TIPO_REPORT, TR.CODICE_REPORT, TR.FLAG_SCELTA_PROPRIETARI " +
        "ORDER BY TR.ID_TIPO_REPORT ");
      
      query = queryBuf.toString();
      /* CONCATENAZIONE/CREAZIONE QUERY END. */
  
      conn = getDatasource().getConnection();
      if (SolmrLogger.isDebugEnabled(this))
      {
        // Dato che la query costruita dinamicamente è un dato importante la
        // registro sul file di log se il
        // debug è abilitato
  
        SolmrLogger.debug(this,
            "[StampaGaaDAO::getListTipiDocumentoStampaProtocollo] Query="
                + query);
      }
      stmt = conn.prepareStatement(query);
      
      int indice = 0;
      
      if (idDocumento!=null && idDocumento.length!=0)
      {
        for(int i=0;i<idDocumento.length;i++)
          stmt.setLong(++indice,idDocumento[i]);
      }
      
      
      
      
  
      // Setto i parametri della query
      ResultSet rs = stmt.executeQuery();
      
      while (rs.next())
      {
        if(vTipoReport == null)
        {
          vTipoReport = new Vector<TipoReportVO>();
        }
        
        tipoReportVO = new TipoReportVO();
        tipoReportVO.setCodiceReport(rs.getString("CODICE_REPORT"));
        tipoReportVO.setIdTipoReport(rs.getInt("ID_TIPO_REPORT"));       
        tipoReportVO.setFlagSceltaPropietari(rs.getString("FLAG_SCELTA_PROPRIETARI"));
        
        vTipoReport.add(tipoReportVO);        
      }
      
      
      return vTipoReport;
    }
    catch (Throwable t)
    {
      // Vettore di variabili interne del metodo
      Variabile variabili[] = new Variabile[]
      { new Variabile("query", query), new Variabile("queryBuf", queryBuf),
          new Variabile("vTipoReport", vTipoReport),
          new Variabile("tipoReportVO", tipoReportVO) };
  
      // Vettore di parametri passati al metodo
      Parametro parametri[] = new Parametro[]
      { new Parametro("idDocumento", idDocumento)};
      // Logging dell'eccezione, query, variabili e parametri del metodo
      LoggerUtils
          .logDAOError(
              this,
              "[StampaGaaDAO::getListTipiDocumentoStampaProtocollo] ",
              t, query, variabili, parametri);
      /*
       * Rimappo e rilancio l'eccezione come DataAccessException.
       */
      throw new DataAccessException(t.getMessage());
    }
    finally
    {
      /*
       * Chiudo Connection e PreparedStatement (il metodo è a prova di null ed
       * ignora ogni eventuale eccezione)
       */
      close(null, stmt, conn);
  
      // Fine metodo
      SolmrLogger
          .debug(this,
              "[StampaGaaDAO::getListTipiDocumentoStampaProtocollo] END.");
    }
  }
  
  
  /**
   * Restituisce il tipo documento per la stampa protocollo
   * dall'idDocumento
   * 
   * 
   * 
   * @param idDocumento
   * @return
   * @throws DataAccessException
   */
  public String getTipoDocumentoStampaProtocollo(long idTipoReport) 
    throws DataAccessException
  {
    String query = null;
    Connection conn = null;
    PreparedStatement stmt = null;
    StringBuffer queryBuf = null;
    String tipoDocumento = null;
    
    try
    {
      SolmrLogger
          .debug(this,
              "[StampaGaaDAO::getTipoDocumentoStampaProtocollo] BEGIN.");
  
      /* CONCATENAZIONE/CREAZIONE QUERY BEGIN. */
  
      queryBuf = new StringBuffer();
      queryBuf.append("" +
         "SELECT CODICE_REPORT " +
         "FROM  DB_TIPO_REPORT TR " +
         "WHERE TR.ID_TIPO_REPORT = ? ");
      
      query = queryBuf.toString();
      /* CONCATENAZIONE/CREAZIONE QUERY END. */
  
      conn = getDatasource().getConnection();
      if (SolmrLogger.isDebugEnabled(this))
      {
        // Dato che la query costruita dinamicamente è un dato importante la
        // registro sul file di log se il
        // debug è abilitato
  
        SolmrLogger.debug(this,
            "[StampaGaaDAO::getTipoDocumentoStampaProtocollo] Query="
                + query);
      }
      stmt = conn.prepareStatement(query);
      
      int indice = 0;
      
      stmt.setLong(++indice, idTipoReport);
      
      
      
      
      
  
      // Setto i parametri della query
      ResultSet rs = stmt.executeQuery();
      
      if(rs.next())
      {
        tipoDocumento = rs.getString("CODICE_REPORT");        
      }
      
      
      return tipoDocumento;
    }
    catch (Throwable t)
    {
      // Vettore di variabili interne del metodo
      Variabile variabili[] = new Variabile[]
      { new Variabile("query", query), new Variabile("queryBuf", queryBuf),
          new Variabile("tipoDocumento", tipoDocumento) };
  
      // Vettore di parametri passati al metodo
      Parametro parametri[] = new Parametro[]
      { new Parametro("idTipoReport", idTipoReport)};
      // Logging dell'eccezione, query, variabili e parametri del metodo
      LoggerUtils
          .logDAOError(
              this,
              "[StampaGaaDAO::getTipoDocumentoStampaProtocollo] ",
              t, query, variabili, parametri);
      /*
       * Rimappo e rilancio l'eccezione come DataAccessException.
       */
      throw new DataAccessException(t.getMessage());
    }
    finally
    {
      /*
       * Chiudo Connection e PreparedStatement (il metodo è a prova di null ed
       * ignora ogni eventuale eccezione)
       */
      close(null, stmt, conn);
  
      // Fine metodo
      SolmrLogger
          .debug(this,
              "[StampaGaaDAO::getTipoDocumentoStampaProtocollo] END.");
    }
  }
  
  
  
  //===================================================================
  // Quadro I3.1 - Unità vitate - Riepilogo per Provincia e Idoneità
  // al piano in lavorazione
  // ===================================================================
  public Vector<RiepiloghiUnitaArboreaVO> getStampaUVRiepilogoIdoneita(
    Long idAzienda)
      throws DataAccessException
  {
    
    Connection conn = null;
    String query = null;
    PreparedStatement stmt = null;
    ResultSet rs = null;
    StringBuffer queryBuf = null;
    Vector<RiepiloghiUnitaArboreaVO> vRiepiloghi = null;
    
    int idx = 0;
    try
    {
      SolmrLogger
      .debug(
          this,
          "[StampaGaaDAO::getStampaUVRiepilogoIdoneita] BEGIN.");

      queryBuf = new StringBuffer();
    
      /* CONCATENAZIONE/CREAZIONE QUERY BEGIN. */
      queryBuf.append("" + 
          "WITH UNITA_VITATE AS " +
          "      (SELECT SUA.ID_STORICO_UNITA_ARBOREA AS ID_STORICO_UNITA_ARBOREA, " + 
          "              SUA.ID_PARTICELLA AS ID_PARTICELLA, " +
          "              SUA.AREA AS AREA, " +
          "              TTV.DESCRIZIONE AS DESCRIZIONE, " +  
          "              TTV.ID_TIPOLOGIA_VINO AS ID_TIPOLOGIA_VINO " +
          "       FROM   DB_STORICO_UNITA_ARBOREA SUA, " +  
          "              DB_TIPO_TIPOLOGIA_VINO TTV, " +
          "              DB_UTE U, " +
          "              DB_CONDUZIONE_PARTICELLA CP " + 
          "       WHERE  SUA.ID_AZIENDA = ? " +
          "       AND    SUA.ID_AZIENDA = U.ID_AZIENDA " +  
          "       AND    U.DATA_FINE_ATTIVITA IS NULL " +
          "       AND    U.ID_UTE = CP.ID_UTE " +
          "       AND    CP.ID_PARTICELLA = SUA.ID_PARTICELLA " + 
          "       AND    CP.DATA_FINE_CONDUZIONE IS NULL " +
          "       AND    SUA.DATA_FINE_VALIDITA IS NULL " +
          "       AND    SUA.ID_TIPOLOGIA_UNAR = 2 " +
          "       AND    SUA.ID_UTILIZZO = "+SolmrConstants.UVA_DA_VINO+" "+
          "       AND    SUA.ID_TIPOLOGIA_VINO = TTV.ID_TIPOLOGIA_VINO " +
          "       GROUP BY SUA.ID_STORICO_UNITA_ARBOREA, " +
          "                SUA.ID_PARTICELLA, " +
          "                SUA.AREA, " +
          "                TTV.DESCRIZIONE, " + 
          "                TTV.ID_TIPOLOGIA_VINO) " +       
          "SELECT UA.DESCRIZIONE, " +
          "       UA.ID_TIPOLOGIA_VINO, " +
          "       PROV.ISTAT_PROVINCIA, " +
          "       PROV.DESCRIZIONE AS DESC_PROV, " + 
          "       SUM(UA.AREA) AS SUP_VITATA " +
          "FROM   UNITA_VITATE UA, " +
          "       DB_STORICO_PARTICELLA SP, " +  
          "       COMUNE COM, " +
          "       PROVINCIA PROV " +
          "WHERE  UA.ID_PARTICELLA = SP.ID_PARTICELLA " + 
          "AND    SP.DATA_FINE_VALIDITA IS NULL " +
          "AND    SP.COMUNE = COM.ISTAT_COMUNE " +
          "AND    COM.ISTAT_PROVINCIA = PROV.ISTAT_PROVINCIA " + 
          "GROUP BY UA.DESCRIZIONE, " +
          "         UA.ID_TIPOLOGIA_VINO, " +
          "         PROV.ISTAT_PROVINCIA, " +
          "         PROV.DESCRIZIONE " +
          "ORDER BY " +
          "       UA.DESCRIZIONE, " + 
          "       PROV.DESCRIZIONE");

      /* CONCATENAZIONE/CREAZIONE QUERY END. */
      
      conn = getDatasource().getConnection();
      query = queryBuf.toString();
      if (SolmrLogger.isDebugEnabled(this))
      {
        // Dato che la query costruita dinamicamente è un dato importante la
        // registro sul file di log se il
        // debug è abilitato
    
        SolmrLogger
            .debug(
                this,
                "[StampaGaaDAO::getStampaUVRiepilogoIdoneita] Query="
                    + query);
      }
      stmt = conn.prepareStatement(query);
    
      // Setto i parametri della query
      stmt.setLong(++idx, idAzienda.longValue());

      rs = stmt.executeQuery();

      while (rs.next())
      {
        if(vRiepiloghi == null)
        {
          vRiepiloghi = new Vector<RiepiloghiUnitaArboreaVO>();
        }
        
        RiepiloghiUnitaArboreaVO riepiloghiUnitaArboreaVO = new RiepiloghiUnitaArboreaVO();
        riepiloghiUnitaArboreaVO.setTipoTipolgiaVino(rs.getString("DESCRIZIONE"));
        riepiloghiUnitaArboreaVO.setIdTipolgiaVino(checkLongNull(rs.getString("ID_TIPOLOGIA_VINO")));
        riepiloghiUnitaArboreaVO.setIstatProv(rs.getString("ISTAT_PROVINCIA"));
        riepiloghiUnitaArboreaVO.setDescProv(rs.getString("DESC_PROV"));
        riepiloghiUnitaArboreaVO.setSupVitata(rs.getBigDecimal("SUP_VITATA"));
        
        vRiepiloghi.add(riepiloghiUnitaArboreaVO);
      }
      
      return vRiepiloghi;

    }
    catch (Throwable t)
    {
      Parametro parametri[] = new Parametro[]
      { new Parametro("idAzienda", idAzienda)};
      Variabile variabili[] = new Variabile[]
      { new Variabile("vRiepiloghi", vRiepiloghi), new Variabile("idx", idx)

      };
      LoggerUtils.logDAOError(this,
          "[StampaGaaDAO::getStampaUVRiepilogoIdoneita] ", t, query,
          variabili, parametri);
      throw new DataAccessException(t.getMessage());
    }
    finally
    {
      close(rs, stmt, conn);
      SolmrLogger
          .debug(this, "[StampaGaaDAO::getStampaUVRiepilogoIdoneita] END.");
    }
  }
  
  
  
//===================================================================
  // Quadro I3.1 - Unità vitate - Riepilogo per Provincia e Idoneità
  // alla dichiarazione
  // ===================================================================
  public Vector<RiepiloghiUnitaArboreaVO> getStampaUVRiepilogoIdoneitaAllaDichiarazione(
    Long idDichiarazioneConsistenza)
      throws DataAccessException
  {
    
    Connection conn = null;
    String query = null;
    PreparedStatement stmt = null;
    ResultSet rs = null;
    StringBuffer queryBuf = null;
    Vector<RiepiloghiUnitaArboreaVO> vRiepiloghi = null;
    
    int idx = 0;
    try
    {
      SolmrLogger
      .debug(
          this,
          "[StampaGaaDAO::getStampaUVRiepilogoIdoneitaAllaDichiarazione] BEGIN.");

      queryBuf = new StringBuffer();
    
      /* CONCATENAZIONE/CREAZIONE QUERY BEGIN. */
      queryBuf.append("" + 
          "SELECT TTV.DESCRIZIONE, " + 
          "       TTV.ID_TIPOLOGIA_VINO, " +
          "       PROV.ISTAT_PROVINCIA, " +
          "       PROV.DESCRIZIONE AS DESC_PROV, " +
          "       SUM(UAD.AREA) AS SUP_VITATA " +
          "FROM   DB_UNITA_ARBOREA_DICHIARATA UAD, " +  
          "       DB_STORICO_PARTICELLA SP, " +
          "       DB_TIPO_TIPOLOGIA_VINO TTV, " +
          "       DB_DICHIARAZIONE_CONSISTENZA DC, " +  
          "       COMUNE COM, " +
          "       PROVINCIA PROV " +
          "WHERE  DC.ID_DICHIARAZIONE_CONSISTENZA = ? " +  
          "AND    DC.CODICE_FOTOGRAFIA_TERRENI = UAD.CODICE_FOTOGRAFIA_TERRENI " +  
          "AND    UAD.ID_TIPOLOGIA_UNAR = 2 " +
          "AND    UAD.ID_STORICO_PARTICELLA = SP.ID_STORICO_PARTICELLA " +  
          "AND    SP.COMUNE = COM.ISTAT_COMUNE " +
          "AND    COM.ISTAT_PROVINCIA = PROV.ISTAT_PROVINCIA " +  
          "AND    UAD.ID_UTILIZZO = "+SolmrConstants.UVA_DA_VINO+" "+
          "AND    UAD.ID_TIPOLOGIA_VINO = TTV.ID_TIPOLOGIA_VINO " +  
          "GROUP BY TTV.DESCRIZIONE, " +   
          "         TTV.ID_TIPOLOGIA_VINO, " +  
          "         PROV.ISTAT_PROVINCIA, " +  
          "         PROV.DESCRIZIONE " +  
          "ORDER BY PROV.DESCRIZIONE, " +
          "         TTV.DESCRIZIONE");

      /* CONCATENAZIONE/CREAZIONE QUERY END. */
      
      conn = getDatasource().getConnection();
      query = queryBuf.toString();
      if (SolmrLogger.isDebugEnabled(this))
      {
        // Dato che la query costruita dinamicamente è un dato importante la
        // registro sul file di log se il
        // debug è abilitato
    
        SolmrLogger
            .debug(
                this,
                "[StampaGaaDAO::getStampaUVRiepilogoIdoneitaAllaDichiarazione] Query="
                    + query);
      }
      stmt = conn.prepareStatement(query);
    
      // Setto i parametri della query
      stmt.setLong(++idx, idDichiarazioneConsistenza.longValue());

      rs = stmt.executeQuery();

      while (rs.next())
      {
        if(vRiepiloghi == null)
        {
          vRiepiloghi = new Vector<RiepiloghiUnitaArboreaVO>();
        }
        
        RiepiloghiUnitaArboreaVO riepiloghiUnitaArboreaVO = new RiepiloghiUnitaArboreaVO();
        riepiloghiUnitaArboreaVO.setTipoTipolgiaVino(rs.getString("DESCRIZIONE"));
        riepiloghiUnitaArboreaVO.setIdTipolgiaVino(checkLongNull(rs.getString("ID_TIPOLOGIA_VINO")));
        riepiloghiUnitaArboreaVO.setIstatProv(rs.getString("ISTAT_PROVINCIA"));
        riepiloghiUnitaArboreaVO.setDescProv(rs.getString("DESC_PROV"));
        riepiloghiUnitaArboreaVO.setSupVitata(rs.getBigDecimal("SUP_VITATA"));
        
        vRiepiloghi.add(riepiloghiUnitaArboreaVO);
      }
      
      return vRiepiloghi;

    }
    catch (Throwable t)
    {
      Parametro parametri[] = new Parametro[]
      { new Parametro("idDichiarazioneConsistenza", idDichiarazioneConsistenza)};
      Variabile variabili[] = new Variabile[]
      { new Variabile("vRiepiloghi", vRiepiloghi), new Variabile("idx", idx)

      };
      LoggerUtils.logDAOError(this,
          "[StampaGaaDAO::getStampaUVRiepilogoIdoneitaAllaDichiarazione] ", t, query,
          variabili, parametri);
      throw new DataAccessException(t.getMessage());
    }
    finally
    {
      close(rs, stmt, conn);
      SolmrLogger
          .debug(this, "[StampaGaaDAO::getStampaUVRiepilogoIdoneitaAllaDichiarazione] END.");
    }
  }
  
  
  /**
   * ritorna quanti documenti passati come id sono compatibili
   * con il report.
   * 
   * 
   * @param idTipoReport
   * @param idDocumento
   * @return
   * @throws DataAccessException
   */
  public int getCountDocumentoCompatibile(long idTipoReport, long[] idDocumento) 
      throws DataAccessException
  {
    String query = null;
    Connection conn = null;
    PreparedStatement stmt = null;
    StringBuffer queryBuf = null;
    int numeroCompatibili = 0;
    
    try
    {
      SolmrLogger
          .debug(this,
              "[StampaGaaDAO::getCountDocumentoCompatibile] BEGIN.");
  
      /* CONCATENAZIONE/CREAZIONE QUERY BEGIN. */
      
      String parametri=" (";
      
      if (idDocumento!=null && idDocumento.length!=0)
      {
        for(int i=0;i<idDocumento.length;i++)
        {
          parametri+="?";
          if (i<idDocumento.length-1)
            parametri+=",";
        }
      }
      parametri+=") ";
  
      queryBuf = new StringBuffer();
      queryBuf.append("" +
        "SELECT COUNT(*) AS COMPATIBILI " +
        "FROM   DB_DOCUMENTO DOC, " + 
        "       DB_TIPO_DOCUMENTO TD, " +
        "       DB_R_TIPO_DOCUMENTO_REPORT TDR " +
        "WHERE  DOC.ID_DOCUMENTO IN  "  +parametri +
        "AND    DOC.EXT_ID_DOCUMENTO = TD.ID_DOCUMENTO " +
        "AND    TD.ID_DOCUMENTO = TDR.ID_DOCUMENTO " +
        "AND    TDR.ID_TIPO_REPORT = ? " +
        "AND    TDR.DATA_FINE_VALIDITA IS NULL ");
      
      query = queryBuf.toString();
      /* CONCATENAZIONE/CREAZIONE QUERY END. */
  
      conn = getDatasource().getConnection();
      if (SolmrLogger.isDebugEnabled(this))
      {
        // Dato che la query costruita dinamicamente è un dato importante la
        // registro sul file di log se il
        // debug è abilitato
  
        SolmrLogger.debug(this,
            "[StampaGaaDAO::getCountDocumentoCompatibile] Query="
                + query);
      }
      stmt = conn.prepareStatement(query);
      
      int indice = 0;
      
      if (idDocumento!=null && idDocumento.length!=0)
      {
        for(int i=0;i<idDocumento.length;i++)
          stmt.setLong(++indice,idDocumento[i]);
      }
      
      stmt.setLong(++indice, idTipoReport);
      
      
      
      
  
      // Setto i parametri della query
      ResultSet rs = stmt.executeQuery();
      
      if(rs.next())
      {
        numeroCompatibili = rs.getInt("COMPATIBILI");        
      }
      
      
      return numeroCompatibili;
    }
    catch (Throwable t)
    {
      // Vettore di variabili interne del metodo
      Variabile variabili[] = new Variabile[]
      { new Variabile("query", query), new Variabile("queryBuf", queryBuf),
          new Variabile("numeroCompatibili", numeroCompatibili) };
  
      // Vettore di parametri passati al metodo
      Parametro parametri[] = new Parametro[]
      { new Parametro("idTipoReport", idTipoReport),
        new Parametro("idDocumento", idDocumento) };
      // Logging dell'eccezione, query, variabili e parametri del metodo
      LoggerUtils
          .logDAOError(
              this,
              "[StampaGaaDAO::getCountDocumentoCompatibile] ",
              t, query, variabili, parametri);
      /*
       * Rimappo e rilancio l'eccezione come DataAccessException.
       */
      throw new DataAccessException(t.getMessage());
    }
    finally
    {
      /*
       * Chiudo Connection e PreparedStatement (il metodo è a prova di null ed
       * ignora ogni eventuale eccezione)
       */
      close(null, stmt, conn);
  
      // Fine metodo
      SolmrLogger
          .debug(this,
              "[StampaGaaDAO::getCountDocumentoCompatibile] END.");
    }
  }
  
  
  /**
   * ritorna i propietari legati ai vari documenti
   * selezionati in stampa
   * 
   * 
   * 
   * @param idDocumento
   * @return
   * @throws DataAccessException
   */
  public Vector<CodeDescription> getElencoPropietariDocumento(long[] idDocumento) 
      throws DataAccessException
  {
    String query = null;
    Connection conn = null;
    PreparedStatement stmt = null;
    StringBuffer queryBuf = null;
    Vector<CodeDescription> vCode = null;
    CodeDescription code = null;
    
    try
    {
      SolmrLogger
          .debug(this,
              "[StampaGaaDAO::getElencoPropietariDocumento] BEGIN.");
  
      /* CONCATENAZIONE/CREAZIONE QUERY BEGIN. */
      
      String parametri=" (";
      
      if (idDocumento!=null && idDocumento.length!=0)
      {
        for(int i=0;i<idDocumento.length;i++)
        {
          parametri+="?";
          if (i<idDocumento.length-1)
            parametri+=",";
        }
      }
      parametri+=") ";
  
      queryBuf = new StringBuffer();
      queryBuf.append("" +
        "SELECT DP.CUAA," +
        "       DP.DENOMINAZIONE," +
        "       COUNT(*) AS NUMERO_DOC " +
        "FROM   DB_DOCUMENTO_PROPRIETARIO DP " + 
        "WHERE  DP.ID_DOCUMENTO IN  " +parametri+
        "GROUP BY DP.CUAA, DP.DENOMINAZIONE " +
        "ORDER BY DP.DENOMINAZIONE ");
      
      query = queryBuf.toString();
      /* CONCATENAZIONE/CREAZIONE QUERY END. */
  
      conn = getDatasource().getConnection();
      if (SolmrLogger.isDebugEnabled(this))
      {
        // Dato che la query costruita dinamicamente è un dato importante la
        // registro sul file di log se il
        // debug è abilitato
  
        SolmrLogger.debug(this,
            "[StampaGaaDAO::getElencoPropietariDocumento] Query="
                + query);
      }
      stmt = conn.prepareStatement(query);
      
      int indice = 0;
      
      if (idDocumento!=null && idDocumento.length!=0)
      {
        for(int i=0;i<idDocumento.length;i++)
          stmt.setLong(++indice,idDocumento[i]);
      }    
  
      // Setto i parametri della query
      ResultSet rs = stmt.executeQuery();
      
      while(rs.next())
      {
        if(vCode == null)
        {
          vCode = new Vector<CodeDescription>();         
        }
        
        code = new CodeDescription();
        code.setDescription(rs.getString("CUAA"));
        code.setSecondaryCode(rs.getString("DENOMINAZIONE"));
        code.setCode(new Integer(rs.getInt("NUMERO_DOC")));
        
        vCode.add(code);        
      }
      
      
      return vCode;
    }
    catch (Throwable t)
    {
      // Vettore di variabili interne del metodo
      Variabile variabili[] = new Variabile[]
      { new Variabile("query", query), new Variabile("queryBuf", queryBuf),
          new Variabile("vCode", vCode),
          new Variabile("code", code) };
  
      // Vettore di parametri passati al metodo
      Parametro parametri[] = new Parametro[]
      { new Parametro("idDocumento", idDocumento) };
      // Logging dell'eccezione, query, variabili e parametri del metodo
      LoggerUtils
          .logDAOError(
              this,
              "[StampaGaaDAO::getElencoPropietariDocumento] ",
              t, query, variabili, parametri);
      /*
       * Rimappo e rilancio l'eccezione come DataAccessException.
       */
      throw new DataAccessException(t.getMessage());
    }
    finally
    {
      /*
       * Chiudo Connection e PreparedStatement (il metodo è a prova di null ed
       * ignora ogni eventuale eccezione)
       */
      close(null, stmt, conn);
  
      // Fine metodo
      SolmrLogger
          .debug(this,
              "[StampaGaaDAO::getElencoPropietariDocumento] END.");
    }
  }
  
  
  
  public TipoReportVO getTipoReport(long idTipoReport) throws DataAccessException
  {
    Connection conn = null;
    String query = null;
    PreparedStatement stmt = null;
    ResultSet rs = null;
    StringBuffer queryBuf = null;
    TipoReportVO tipoReportVO = null;
    
    
    int idx = 0;
    try
    {
      SolmrLogger
      .debug(
          this,
          "[StampaGaaDAO::getTipoReport] BEGIN.");

      queryBuf = new StringBuffer();
    
      /* CONCATENAZIONE/CREAZIONE QUERY BEGIN. */
      queryBuf.append("" +
          "SELECT TR.ID_TIPO_REPORT, " +
          "       TR.CODICE_REPORT, " +
          "       TR.DESCRIZIONE," +
          "       TR.FLAG_SCELTA_PROPRIETARI " +
          "FROM   DB_TIPO_REPORT TR " +
          "WHERE  TR.ID_TIPO_REPORT = ? ");
      
      /* CONCATENAZIONE/CREAZIONE QUERY END. */
    
      conn = getDatasource().getConnection();
      query = queryBuf.toString();
      if (SolmrLogger.isDebugEnabled(this))
      {
        // Dato che la query costruita dinamicamente è un dato importante la
        // registro sul file di log se il
        // debug è abilitato
    
        SolmrLogger
            .debug(
                this,
                "[StampaGaaDAO::getTipoReport] Query="
                    + query);
      }
      stmt = conn.prepareStatement(query);
    
      // Setto i parametri della query
      stmt.setLong(++idx, idTipoReport);
     
      rs = stmt.executeQuery();
      
      if (rs.next())
      {
        tipoReportVO = new TipoReportVO();
        tipoReportVO.setIdTipoReport(rs.getInt("ID_TIPO_REPORT"));
        tipoReportVO.setCodiceReport(rs.getString("CODICE_REPORT"));
        tipoReportVO.setDescrizione(rs.getString("DESCRIZIONE"));
        tipoReportVO.setFlagSceltaPropietari(rs.getString("FLAG_SCELTA_PROPRIETARI"));
      }
      
      return tipoReportVO;
      
    }
    catch (Throwable t)
    {
      Parametro parametri[] = new Parametro[]
      { new Parametro("idTipoReport", idTipoReport) };
      Variabile variabili[] = new Variabile[]
      { new Variabile("tipoReportVO", tipoReportVO) };
      LoggerUtils.logDAOError(this,
          "[StampaGaaDAO::getElencoTipoReport] ", t, query,
          variabili, parametri);
      throw new DataAccessException(t.getMessage());
    }
    finally
    {
      close(rs, stmt, conn);
      SolmrLogger
          .debug(this, "[StampaGaaDAO::getTipoReport] END.");
    }
  }
  
  public TipoReportVO getTipoReportByCodice(String codiceReport) 
      throws DataAccessException
  {
    Connection conn = null;
    String query = null;
    PreparedStatement stmt = null;
    ResultSet rs = null;
    StringBuffer queryBuf = null;
    TipoReportVO tipoReportVO = null;
    
    
    int idx = 0;
    try
    {
      SolmrLogger
      .debug(
          this,
          "[StampaGaaDAO::getTipoReportByCodice] BEGIN.");

      queryBuf = new StringBuffer();
    
      /* CONCATENAZIONE/CREAZIONE QUERY BEGIN. */
      queryBuf.append("" +
          "SELECT TR.ID_TIPO_REPORT, " +
          "       TR.CODICE_REPORT, " +
          "       TR.DESCRIZIONE," +
          "       TR.FLAG_SCELTA_PROPRIETARI, " +
          "       TR.NOME_FILE, " +
          "       TR.ID_TIPO_ALLEGATO " +
          "FROM   DB_TIPO_REPORT TR " +
          "WHERE  TR.CODICE_REPORT = ? ");
      
      /* CONCATENAZIONE/CREAZIONE QUERY END. */
    
      conn = getDatasource().getConnection();
      query = queryBuf.toString();
      if (SolmrLogger.isDebugEnabled(this))
      {
        // Dato che la query costruita dinamicamente è un dato importante la
        // registro sul file di log se il
        // debug è abilitato
    
        SolmrLogger
            .debug(
                this,
                "[StampaGaaDAO::getTipoReportByCodice] Query="
                    + query);
      }
      stmt = conn.prepareStatement(query);
    
      // Setto i parametri della query
      stmt.setString(++idx, codiceReport);
     
      rs = stmt.executeQuery();
      
      if (rs.next())
      {
        tipoReportVO = new TipoReportVO();
        tipoReportVO.setIdTipoReport(rs.getInt("ID_TIPO_REPORT"));
        tipoReportVO.setCodiceReport(rs.getString("CODICE_REPORT"));
        tipoReportVO.setDescrizione(rs.getString("DESCRIZIONE"));
        tipoReportVO.setFlagSceltaPropietari(rs.getString("FLAG_SCELTA_PROPRIETARI"));
        tipoReportVO.setNomeFileXDP(rs.getString("NOME_FILE"));
        tipoReportVO.setIdTipoAllegato(checkLongNull(rs.getString("ID_TIPO_ALLEGATO")));
      }
      
      return tipoReportVO;
      
    }
    catch (Throwable t)
    {
      Parametro parametri[] = new Parametro[]
      { new Parametro("codiceReport", codiceReport) };
      Variabile variabili[] = new Variabile[]
      { new Variabile("tipoReportVO", tipoReportVO) };
      LoggerUtils.logDAOError(this,
          "[StampaGaaDAO::getTipoReportByCodice] ", t, query,
          variabili, parametri);
      throw new DataAccessException(t.getMessage());
    }
    finally
    {
      close(rs, stmt, conn);
      SolmrLogger
          .debug(this, "[StampaGaaDAO::getTipoReportByCodice] END.");
    }
  }
  
  
  /**
   * 
   * ritorna tutte le attestazioni relative ad un subreport
   * (Stampa protocollo)
   * 
   * 
   * @param idReportSubReport
   * @return
   * @throws DataAccessException
   */
  public Vector<TipoAttestazioneVO> getAttestStampaProtoc(long idReportSubReport) throws DataAccessException
  {
    Connection conn = null;
    String query = null;
    PreparedStatement stmt = null;
    ResultSet rs = null;
    StringBuffer queryBuf = null;
    Vector<TipoAttestazioneVO> vTipoAttestazioneVO = null;
    TipoAttestazioneVO tipoAttestazioneVO = null;
    
    int idx = 0;
    try
    {
      SolmrLogger
      .debug(
          this,
          "[StampaGaaDAO::getAttestStampaProtoc] BEGIN.");

      queryBuf = new StringBuffer();
    
      /* CONCATENAZIONE/CREAZIONE QUERY BEGIN. */
      queryBuf.append("" +
        "SELECT TA.DESCRIZIONE " + 
        "FROM DB_R_ATTEST_REPORT_SUB_REPORT RSR, " +
        "         DB_TIPO_ATTESTAZIONE TA " +
        "WHERE RSR.ID_REPORT_SUB_REPORT = ? " +
        "AND     RSR.DATA_FINE_VALIDITA IS NULL " +
        "AND     RSR.ID_ATTESTAZIONE = TA.ID_ATTESTAZIONE " +
        "AND     TA.DATA_FINE_VALIDITA IS NULL " +
        "ORDER BY TA.GRUPPO, TA.ORDINAMENTO");
      
      /* CONCATENAZIONE/CREAZIONE QUERY END. */
    
      conn = getDatasource().getConnection();
      query = queryBuf.toString();
      if (SolmrLogger.isDebugEnabled(this))
      {
        // Dato che la query costruita dinamicamente è un dato importante la
        // registro sul file di log se il
        // debug è abilitato
    
        SolmrLogger
            .debug(
                this,
                "[StampaGaaDAO::getAttestStampaProtoc] Query="
                    + query);
      }
      stmt = conn.prepareStatement(query);
    
      // Setto i parametri della query
      stmt.setLong(++idx, idReportSubReport);
     
      rs = stmt.executeQuery();
      
      while (rs.next())
      {
        if(vTipoAttestazioneVO == null)
        {
          vTipoAttestazioneVO = new Vector<TipoAttestazioneVO>();
        }
        
        tipoAttestazioneVO = new TipoAttestazioneVO();        
        tipoAttestazioneVO.setDescrizione(rs.getString("DESCRIZIONE"));
        
        vTipoAttestazioneVO.add(tipoAttestazioneVO);
      }
      
      return vTipoAttestazioneVO;
      
    }
    catch (Throwable t)
    {
      Parametro parametri[] = new Parametro[]
      { new Parametro("idReportSubReport", idReportSubReport) };
      Variabile variabili[] = new Variabile[]
      { new Variabile("tipoAttestazioneVO", tipoAttestazioneVO),
        new Variabile("vTipoAttestazioneVO", vTipoAttestazioneVO) };
      LoggerUtils.logDAOError(this,
          "[StampaGaaDAO::getAttestStampaProtoc] ", t, query,
          variabili, parametri);
      throw new DataAccessException(t.getMessage());
    }
    finally
    {
      close(rs, stmt, conn);
      SolmrLogger
          .debug(this, "[StampaGaaDAO::getAttestStampaProtoc] END.");
    }
  }  
  
  /**
   * 
   * mi dice s eil numero di protocollo passato appartiene ad un documento.
   * 
   * 
   * @param numeroProtocollo
   * @return
   * @throws DataAccessException
   */
  public boolean isNumeroProtocolloValido(String numeroProtocollo) throws DataAccessException
  {
    Connection conn = null;
    String query = null;
    PreparedStatement stmt = null;
    ResultSet rs = null;
    StringBuffer queryBuf = null;
    boolean result = false;
    
    int idx = 0;
    try
    {
      SolmrLogger
      .debug(
          this,
          "[StampaGaaDAO::isNumeroProtocolloValido] BEGIN.");

      queryBuf = new StringBuffer();
    
      /* CONCATENAZIONE/CREAZIONE QUERY BEGIN. */
      queryBuf.append("" +
        "SELECT DC.ID_DOCUMENTO " + 
        "FROM   DB_DOCUMENTO DC " +
        "WHERE  DC.NUMERO_PROTOCOLLO = ? ");
      
      /* CONCATENAZIONE/CREAZIONE QUERY END. */
    
      conn = getDatasource().getConnection();
      query = queryBuf.toString();
      if (SolmrLogger.isDebugEnabled(this))
      {
        // Dato che la query costruita dinamicamente è un dato importante la
        // registro sul file di log se il
        // debug è abilitato
    
        SolmrLogger
            .debug(
                this,
                "[StampaGaaDAO::isNumeroProtocolloValido] Query="
                    + query);
      }
      stmt = conn.prepareStatement(query);
    
      // Setto i parametri della query
      stmt.setString(++idx, numeroProtocollo);
     
      rs = stmt.executeQuery();
      
      if (rs.next())
      {
        result = true;
      }
      
      return result;
      
    }
    catch (Throwable t)
    {
      Parametro parametri[] = new Parametro[]
      { new Parametro("numeroProtocollo", numeroProtocollo) };
      Variabile variabili[] = new Variabile[]
      { new Variabile("result", result) };
      LoggerUtils.logDAOError(this,
          "[StampaGaaDAO::isNumeroProtocolloValido] ", t, query,
          variabili, parametri);
      throw new DataAccessException(t.getMessage());
    }
    finally
    {
      close(rs, stmt, conn);
      SolmrLogger
          .debug(this, "[StampaGaaDAO::isNumeroProtocolloValido] END.");
    }
  }
  
  
  public Vector<ParticellaVO> getElencoParticelleVarCat(long idAzienda, long idDichiarazioneConsistenza) throws DataAccessException
  {
    Connection conn = null;
    String query = null;
    PreparedStatement stmt = null;
    ResultSet rs = null;
    StringBuffer queryBuf = null;
    Vector<ParticellaVO> vParticella = null;
    ParticellaVO particellaVO = null;
    
    int idx = 0;
    try
    {
      SolmrLogger
      .debug(
          this,
          "[StampaGaaDAO::getElencoParticelleVarCat] BEGIN.");

      queryBuf = new StringBuffer();
    
      /* CONCATENAZIONE/CREAZIONE QUERY BEGIN. */
      queryBuf.append("" +
        "SELECT CD.ID_CONDUZIONE_DICHIARATA, " +
        "       DC.DATA_INSERIMENTO_DICHIARAZIONE, " +
        "       C.DESCOM || ' (' || P.SIGLA_PROVINCIA || ')' AS DESC_COMUNE," +
        "       SP.SEZIONE, " +
        "       SP.FOGLIO, " +
        "       SP.PARTICELLA, " +
        "       SP.SUBALTERNO, "+
        "       TU.CODICE AS COD_USO, " +
        "       TU.DESCRIZIONE AS DESC_USO, " +
        "       TV.CODICE_VARIETA, " +
        "       TV.DESCRIZIONE AS DESC_VARIETA, " +
        "       SP.SUP_CATASTALE, SP.SUPERFICIE_GRAFICA, "+
        "       CD.ID_TITOLO_POSSESSO, " +
        "       CD.SUPERFICIE_CONDOTTA, " +
        "       CD.PERCENTUALE_POSSESSO, " +
        "       UD.SUPERFICIE_UTILIZZATA, " +
        "       CD.SUPERFICIE_AGRONOMICA, " +
        "       SP.ID_CASO_PARTICOLARE, "+
        "       SP.ID_AREA_C, " +
        "       DECODE(F.ID_AREA_E,2,'X') AS ZVN, " +
        "       SP.ID_POTENZIALITA_IRRIGUA, " +
        "       SP.ID_ROTAZIONE_COLTURALE, " +
        "       PC.ID_PARTICELLA_CERTIFICATA, " +
        "       TQ.CODICE AS COD_QUALITA " +
        "FROM   DB_CONDUZIONE_DICHIARATA CD, " +
        "       DB_UTILIZZO_DICHIARATO UD, " +
        "       DB_STORICO_PARTICELLA SP, " +
        "       COMUNE C, " +
        "       PROVINCIA P," +
        "       DB_TIPO_UTILIZZO TU, " +
        "       DB_TIPO_VARIETA TV, " +
        "       DB_FOGLIO F, " +
        "       DB_DICHIARAZIONE_CONSISTENZA DC, " +
        "       DB_PARTICELLA_CERTIFICATA PC, " +
        "       DB_TIPO_QUALITA TQ " +
        "WHERE  DC.ID_DICHIARAZIONE_CONSISTENZA = ? " +
        "AND    CD.CODICE_FOTOGRAFIA_TERRENI = DC.CODICE_FOTOGRAFIA_TERRENI " +
        "AND    CD.ID_CONDUZIONE_DICHIARATA = UD.ID_CONDUZIONE_DICHIARATA(+) " +
        "AND    SP.ID_STORICO_PARTICELLA = CD.ID_STORICO_PARTICELLA " +
        "AND    SP.COMUNE = C.ISTAT_COMUNE " +
        "AND    P.ISTAT_PROVINCIA = C.ISTAT_PROVINCIA " +
        "AND    UD.ID_UTILIZZO = TU.ID_UTILIZZO(+) " +
        "AND    UD.ID_VARIETA = TV.ID_VARIETA(+) " +
        "AND    SP.COMUNE = F.COMUNE(+) " +
        "AND    NVL(SP.SEZIONE,-1) = NVL(F.SEZIONE(+),-1) " +
        "AND    SP.FOGLIO = F.FOGLIO(+) " +
        "AND    SP.ID_PARTICELLA = PC.ID_PARTICELLA(+) " +
        "AND    PC.DATA_FINE_VALIDITA (+) IS NULL " +
        "AND    PC.ID_QUALITA = TQ.ID_QUALITA (+) " +
        "AND    EXISTS  (SELECT NE.ID_NOTIFICA_ENTITA " + 
        "              FROM   DB_NOTIFICA_ENTITA NE, " +
        "                     DB_NOTIFICA NO, " +
        "                     DB_TIPO_ENTITA TE " +
        "              WHERE  TE.CODICE_TIPO_ENTITA = 'P' " +
        "              AND    TE.ID_TIPO_ENTITA = NE.ID_TIPO_ENTITA " +
        "              AND    NE.IDENTIFICATIVO = SP.ID_PARTICELLA " +
        "              AND    NE.ID_NOTIFICA = NO.ID_NOTIFICA " +
        "              AND    NE.ID_DICHIARAZIONE_CONSISTENZA = DC.ID_DICHIARAZIONE_CONSISTENZA " +
        "              AND    NO.ID_TIPOLOGIA_NOTIFICA = " +SolmrConstants.ID_TIPO_TIPOLOGIA_VARIAZIONECATASTALE+") "+
        "ORDER BY DESC_COMUNE, " +
        "         SP.SEZIONE, " +
        "         SP.FOGLIO, " +
        "         SP.PARTICELLA, " +
        "         SP.SUBALTERNO ");
      
      /* CONCATENAZIONE/CREAZIONE QUERY END. */
    
      conn = getDatasource().getConnection();
      query = queryBuf.toString();
      if (SolmrLogger.isDebugEnabled(this))
      {
        // Dato che la query costruita dinamicamente è un dato importante la
        // registro sul file di log se il
        // debug è abilitato
    
        SolmrLogger
            .debug(
                this,
                "[StampaGaaDAO::getAttestStampaProtoc] Query="
                    + query);
      }
      stmt = conn.prepareStatement(query);
    
      // Setto i parametri della query
      stmt.setLong(++idx, idDichiarazioneConsistenza);
     
      rs = stmt.executeQuery();
      
      while (rs.next())
      {
        if(vParticella == null)
        {
          vParticella = new Vector<ParticellaVO>();
        }
        
        ParticellaVO particella=new ParticellaVO();
        particella.setDescComuneParticella(rs.getString("DESC_COMUNE"));
        particella.setSezione(rs.getString("SEZIONE"));
        try
        {
          particella.setFoglio(new Long(rs.getString("FOGLIO")));
        }
        catch(Exception e){}
        try
        {
          particella.setParticella(new Long(rs.getString("PARTICELLA")));
        }
        catch(Exception e){}
        particella.setSubalterno(rs.getString("SUBALTERNO"));
        particella.setSupCatastale(rs.getString("SUP_CATASTALE"));
        particella.setSuperficieGrafica(rs.getString("SUPERFICIE_GRAFICA"));
        particella.setIdTitoloPossesso(new Long(rs.getString("ID_TITOLO_POSSESSO")));
        particella.setPercentualePossesso(rs.getBigDecimal("PERCENTUALE_POSSESSO"));
        particella.setSuperficieCondotta(rs.getString("SUPERFICIE_CONDOTTA"));
        particella.setSupAgronomico(rs.getString("SUPERFICIE_AGRONOMICA"));
        particella.setSuperficieUtilizzata(rs.getString("SUPERFICIE_UTILIZZATA"));
        
        String uso=rs.getString("COD_USO");
        if (uso!=null) uso="["+uso+"] "+rs.getString("DESC_USO");
        particella.setDescUtilizzoParticella(uso);
        
        String var=rs.getString("CODICE_VARIETA");
        if (var!=null) var="["+var+"] "+rs.getString("DESC_VARIETA");
        particella.setDescVarieta(var);
     
        particella.setIdAreaC(checkLongNull(rs.getString("ID_AREA_C")));
        particella.setIdCasoParticolare(checkLongNull(rs.getString("ID_CASO_PARTICOLARE")));
        particella.setZonaVulnerabileNitrati(rs.getString("ZVN"));
        particella.setIdPotenzaIrrigua(checkLongNull(rs.getString("ID_POTENZIALITA_IRRIGUA")));
        particella.setIdRotazioneColturale(checkLongNull(rs.getString("ID_ROTAZIONE_COLTURALE")));
        
        String documento = null;
        long idConduzioneDichiarata = rs.getLong("ID_CONDUZIONE_DICHIARATA");
        java.util.Date dataInserimentoDichiarazione = rs.getDate("DATA_INSERIMENTO_DICHIARAZIONE");   
        try
        {
          documento = getDocumentiFromIdConduzioneDichiarata(
             idConduzioneDichiarata, idAzienda, dataInserimentoDichiarazione);
        }
        catch(Exception e){}
        particella.setDocumento(documento);
        
        String proprietari = null;
        Long idParticellaCertificata = checkLongNull(rs.getString("ID_PARTICELLA_CERTIFICATA"));
        if(Validator.isNotEmpty(idParticellaCertificata))
        {
          try
          {
            proprietari = getProprietariCertificati(idParticellaCertificata, dataInserimentoDichiarazione);
          }
          catch(Exception e){}
          particella.setProprietari(proprietari);
        }
        
        
        particella.setCodQualita(rs.getString("COD_QUALITA"));
        
        
        vParticella.add(particella);        
        
      }
      
      return vParticella;
      
    }
    catch (Throwable t)
    {
      Parametro parametri[] = new Parametro[]
      { new Parametro("idAzienda", idAzienda),
        new Parametro("idDichiarazioneConsistenza", idDichiarazioneConsistenza) };
      Variabile variabili[] = new Variabile[]
      { new Variabile("particellaVO", particellaVO),
        new Variabile("vParticella", vParticella) };
      LoggerUtils.logDAOError(this,
          "[StampaGaaDAO::getAttestStampaProtoc] ", t, query,
          variabili, parametri);
      throw new DataAccessException(t.getMessage());
    }
    finally
    {
      close(rs, stmt, conn);
      SolmrLogger
          .debug(this, "[StampaGaaDAO::getAttestStampaProtoc] END.");
    }
  }
  
  
  private String getDocumentiFromIdConduzioneDichiarata(long idConduzioneDichiarata,
      long idAzienda, Date dataInserimentoDichiarazione) throws DataAccessException
  {
    String query = null;
    Connection conn = null;
    PreparedStatement stmt = null;
    StringBuffer queryBuf = null;
    String documento = null;
    try
    {
      SolmrLogger.debug(this,
          "[StampeGaaDAO::getDocumentiFromIdConduzioneParticella] BEGIN.");      
       
      queryBuf = new StringBuffer();

      /* CONCATENAZIONE/CREAZIONE QUERY BEGIN. */
      queryBuf.append(
        "SELECT  TD.DESCRIZIONE, " +       
        "        DOC.NUMERO_PROTOCOLLO, " +
        "        DOC.DATA_PROTOCOLLO " + 
        "FROM    DB_DOCUMENTO_CONDUZIONE DOCC," +
        "        DB_DOCUMENTO DOC, " +
        "        DB_TIPO_DOCUMENTO TD, " +
        "        DB_DOCUMENTO_CATEGORIA DCAT, " +
        "        DB_TIPO_CATEGORIA_DOCUMENTO TCD," +
        "        DB_CONDUZIONE_DICHIARATA CD "+
        "WHERE   CD.ID_CONDUZIONE_DICHIARATA = ? " +
        "AND     CD.ID_CONDUZIONE_PARTICELLA = DOCC.ID_CONDUZIONE_PARTICELLA " +
        "AND     DOCC.ID_DOCUMENTO = DOC.ID_DOCUMENTO " +
        "AND     DOC.EXT_ID_DOCUMENTO = TD.ID_DOCUMENTO " +
        "AND     TD.ID_DOCUMENTO = DCAT.ID_DOCUMENTO " +
        "AND     DCAT.ID_CATEGORIA_DOCUMENTO = TCD.ID_CATEGORIA_DOCUMENTO " +
        "AND     DOC.ID_AZIENDA = ? " +    
        "AND     TD.ID_TIPOLOGIA_DOCUMENTO = 2 " +
        "AND     TCD.TIPO_IDENTIFICATIVO = 'TP' " +
        "AND     DOCC.ID_CONDUZIONE_PARTICELLA = CD.ID_CONDUZIONE_PARTICELLA " +
        "AND     TCD.IDENTIFICATIVO = CD.ID_TITOLO_POSSESSO " +
        "AND     DOCC.DATA_INIZIO_VALIDITA < ? " +
        "AND     NVL(DOCC.DATA_FINE_VALIDITA, TO_DATE('31/12/9999', 'DD/MM/YYYY')) > ? ");
      
      
      /* CONCATENAZIONE/CREAZIONE QUERY END. */

      conn = getDatasource().getConnection();
      query = queryBuf.toString();
      if (SolmrLogger.isDebugEnabled(this))
      {
        // Dato che la query costruita dinamicamente è un dato importante la
        // registro sul file di log se il
        // debug è abilitato

        SolmrLogger.debug(this,
            "[StampeGaaDAO::getDocumentiFromIdConduzioneParticella] Query="
                + query);
      }
      stmt = conn.prepareStatement(query);
      
      stmt.setLong(1,idConduzioneDichiarata);
      stmt.setLong(2,idAzienda);
      stmt.setTimestamp(3,convertDateToTimestamp(dataInserimentoDichiarazione));
      stmt.setTimestamp(4,convertDateToTimestamp(dataInserimentoDichiarazione));
      
      ResultSet rs = stmt.executeQuery();
      
      while (rs.next())
      {
        if(Validator.isNotEmpty(documento))
        {
          documento += " - ";
        }
        else
        {
          documento = "";
        }
        
        String documentoDesc = rs.getString("DESCRIZIONE")+ " ("+rs.getString("NUMERO_PROTOCOLLO")+")";
        
        if(Validator.isNotEmpty(documentoDesc))
        {
          documento +=  documentoDesc;
        }
        
       
        
      }
      
      return documento;
      
    }
    catch (Throwable t)
    {
      // Vettore di variabili interne del metodo
      Variabile variabili[] = new Variabile[]
      { new Variabile("queryBuf", queryBuf), new Variabile("query", query),
          new Variabile("documento", documento) };

      // Vettore di parametri passati al metodo
      Parametro parametri[] = new Parametro[]
      { new Parametro("idConduzioneDichiarata", idConduzioneDichiarata),
        new Parametro("idAzienda", idAzienda),
        new Parametro("dataInserimentoDichiarazione", dataInserimentoDichiarazione) };

      // Logging dell'eccezione, query, variabili e parametri del metodo
      LoggerUtils.logDAOError(this,
          "[StampeGaaDAO::getDocumentiFromIdConduzioneParticella] ", t,
          query, variabili, parametri);
      /*
       * Rimappo e rilancio l'eccezione come GaaservInternalException. Per
       * informazione sui codici di errore utilizzati vedere il javadoc di
       * BaseDAO.convertIntoGaaservInternalException()
       */
      throw new DataAccessException(t.getMessage());
    }
    finally
    {
      /*
       * Chiudo Connection e PreparedStatement (il metodo è a prova di null ed
       * ignora ogni eventuale eccezione)
       */
      close(null, stmt, conn);

      // Fine metodo
      SolmrLogger.debug(this,
          "[StampeGaaDAO::getDocumentiFromIdConduzioneParticella] END.");
    }
  }
  
  
  
  private String getProprietariCertificati(long idParticellaCertificata,
      Date dataInserimentoDichiarazione) throws DataAccessException
  {
    String query = null;
    Connection conn = null;
    PreparedStatement stmt = null;
    StringBuffer queryBuf = null;
    String proprietari = "";
    try
    {
      SolmrLogger.debug(this,
          "[StampeGaaDAO::getProprietariCertificati] BEGIN.");      
       
      queryBuf = new StringBuffer();

      /* CONCATENAZIONE/CREAZIONE QUERY BEGIN. */
      queryBuf.append(
        "SELECT  PC.DENOMINAZIONE_PROPRIETARIO, " +
        "        PC.CODICE_FISCALE_PROPRIETARIO " +       
        "FROM    DB_PROPRIETA_CERTIFICATA PC " +
        "WHERE   PC.ID_PARTICELLA_CERTIFICATA = ? " +
        "AND     PC.DATA_INIZIO_VALIDITA < ? " +
        "AND     NVL(PC.DATA_FINE_VALIDITA, TO_DATE('31/12/9999', 'DD/MM/YYYY')) > ? ");
      
      
      /* CONCATENAZIONE/CREAZIONE QUERY END. */

      conn = getDatasource().getConnection();
      query = queryBuf.toString();
      if (SolmrLogger.isDebugEnabled(this))
      {
        // Dato che la query costruita dinamicamente è un dato importante la
        // registro sul file di log se il
        // debug è abilitato

        SolmrLogger.debug(this,
            "[StampeGaaDAO::getProprietariCertificati] Query="
                + query);
      }
      stmt = conn.prepareStatement(query);
      
      stmt.setLong(1,idParticellaCertificata);
      stmt.setTimestamp(2,convertDateToTimestamp(dataInserimentoDichiarazione));
      stmt.setTimestamp(3,convertDateToTimestamp(dataInserimentoDichiarazione));
      
      ResultSet rs = stmt.executeQuery();
      
      while (rs.next())
      {
        if(Validator.isNotEmpty(proprietari))
        {
          proprietari += "\n";
        }
        
        String proprietariDesc = rs.getString("DENOMINAZIONE_PROPRIETARIO")+" - "+rs.getString("CODICE_FISCALE_PROPRIETARIO");
        
        if(Validator.isNotEmpty(proprietariDesc))
        {
          proprietari +=  proprietariDesc;
        }
        
       
        
      }
      
      return proprietari;
      
    }
    catch (Throwable t)
    {
      // Vettore di variabili interne del metodo
      Variabile variabili[] = new Variabile[]
      { new Variabile("queryBuf", queryBuf), new Variabile("query", query),
          new Variabile("proprietari", proprietari) };

      // Vettore di parametri passati al metodo
      Parametro parametri[] = new Parametro[]
      { new Parametro("idParticellaCertificata", idParticellaCertificata),
        new Parametro("dataInserimentoDichiarazione", dataInserimentoDichiarazione) };

      // Logging dell'eccezione, query, variabili e parametri del metodo
      LoggerUtils.logDAOError(this,
          "[StampeGaaDAO::getProprietariCertificati] ", t,
          query, variabili, parametri);
      /*
       * Rimappo e rilancio l'eccezione come GaaservInternalException. Per
       * informazione sui codici di errore utilizzati vedere il javadoc di
       * BaseDAO.convertIntoGaaservInternalException()
       */
      throw new DataAccessException(t.getMessage());
    }
    finally
    {
      /*
       * Chiudo Connection e PreparedStatement (il metodo è a prova di null ed
       * ignora ogni eventuale eccezione)
       */
      close(null, stmt, conn);

      // Fine metodo
      SolmrLogger.debug(this,
          "[StampeGaaDAO::getProprietariCertificati] END.");
    }
  }
  
  /**
   * 
   * Sezione anagrafica nuova stampa ***
   * 
   * 
   * @param idAzienda
   * @param dataRiferimento
   * @return
   * @throws DataAccessException
   */
  public Vector<TipoFormaConduzioneVO> getFormaConduzioneSezioneAnagrafica(Long idAzienda,
      Date dataRiferimento) throws DataAccessException
  {
    String query = null;
    Connection conn = null;
    PreparedStatement stmt = null;
    StringBuffer queryBuf = null;
    Vector<TipoFormaConduzioneVO> vTipoFormaConduzione = null;
    TipoFormaConduzioneVO tipoFormaConduzioneVO = null;
    Vector<String> vDescAttivitaCompl = null;
    try
    {
      SolmrLogger.debug(this,
          "[StampeGaaDAO::getFormaConduzioneSezioneAnagrafica] BEGIN.");      
       
      queryBuf = new StringBuffer();

      /* CONCATENAZIONE/CREAZIONE QUERY BEGIN. */
      queryBuf.append(
        "SELECT TFC.FORMA || ' - ' || TFC.DESCRIZIONE AS FORMA, " +
        "       TAC.DESCRIZIONE AS DESC_ATTIVITA_COMPL " + 
        "FROM   DB_MANODOPERA MAN, " +
        "       DB_DETTAGLIO_ATTIVITA DA, " +
        "       DB_TIPO_FORMA_CONDUZIONE TFC, " +
        "       DB_TIPO_ATTIVITA_COMPLEMENTARI TAC " +
        "WHERE  MAN.ID_AZIENDA = ? " +
        "AND    TRUNC(MAN.DATA_INIZIO_VALIDITA) <= ? " + 
        "AND    NVL(TRUNC(MAN.DATA_FINE_VALIDITA), TO_DATE('31/12/9999','dd/mm/yyyy')) > ? " +
        "AND    MAN.ID_MANODOPERA = DA.ID_MANODOPERA (+) " +
        "AND    DA.ID_ATTIVITA_COMPLEMENTARI = TAC.ID_ATTIVITA_COMPLEMENTARI (+) " +
        "AND    MAN.ID_FORMA_CONDUZIONE = TFC.ID_FORMA_CONDUZIONE " +
        "ORDER BY TFC.FORMA ");
      
      
      /* CONCATENAZIONE/CREAZIONE QUERY END. */

      conn = getDatasource().getConnection();
      query = queryBuf.toString();
      if (SolmrLogger.isDebugEnabled(this))
      {
        // Dato che la query costruita dinamicamente è un dato importante la
        // registro sul file di log se il
        // debug è abilitato

        SolmrLogger.debug(this,
            "[StampeGaaDAO::getFormaConduzioneSezioneAnagrafica] Query="
                + query);
      }
      stmt = conn.prepareStatement(query);
      
      stmt.setLong(1, idAzienda.longValue());
      stmt.setTimestamp(2,convertDateToTimestamp(dataRiferimento));
      stmt.setTimestamp(3,convertDateToTimestamp(dataRiferimento));
      
      ResultSet rs = stmt.executeQuery();
      
      String formaTmp = "";
      while (rs.next())
      {
        if(vTipoFormaConduzione == null)
        {
          vTipoFormaConduzione = new Vector<TipoFormaConduzioneVO>();
        }
        String forma = rs.getString("FORMA");
        if(!formaTmp.equalsIgnoreCase(forma))
        {
          if(!formaTmp.equalsIgnoreCase(""))
          {
            tipoFormaConduzioneVO.setvDescAttivitaComplementari(vDescAttivitaCompl);
            vTipoFormaConduzione.add(tipoFormaConduzioneVO);
          }
          tipoFormaConduzioneVO = new TipoFormaConduzioneVO();
          tipoFormaConduzioneVO.setForma(forma);
          vDescAttivitaCompl = new Vector<String>();
        }
        
        String attivitaComplementari = rs.getString("DESC_ATTIVITA_COMPL");
        if(Validator.isNotEmpty(attivitaComplementari))
          vDescAttivitaCompl.add(attivitaComplementari);   
        
        formaTmp = forma;
      }
      
      if(vTipoFormaConduzione != null)
      {
        tipoFormaConduzioneVO.setvDescAttivitaComplementari(vDescAttivitaCompl);
        vTipoFormaConduzione.add(tipoFormaConduzioneVO);
      }
      
      return vTipoFormaConduzione;
      
    }
    catch (Throwable t)
    {
      // Vettore di variabili interne del metodo
      Variabile variabili[] = new Variabile[]
      { new Variabile("queryBuf", queryBuf), new Variabile("query", query),
          new Variabile("vTipoFormaConduzione", vTipoFormaConduzione), 
          new Variabile("tipoFormaConduzioneVO", tipoFormaConduzioneVO) };

      // Vettore di parametri passati al metodo
      Parametro parametri[] = new Parametro[]
      { new Parametro("idAzienda", idAzienda), 
          new Parametro("dataRiferimento", dataRiferimento) };

      // Logging dell'eccezione, query, variabili e parametri del metodo
      LoggerUtils.logDAOError(this,
          "[StampeGaaDAO::getFormaConduzioneSezioneAnagrafica] ", t,
          query, variabili, parametri);
      /*
       * Rimappo e rilancio l'eccezione come GaaservInternalException. Per
       * informazione sui codici di errore utilizzati vedere il javadoc di
       * BaseDAO.convertIntoGaaservInternalException()
       */
      throw new DataAccessException(t.getMessage());
    }
    finally
    {
      /*
       * Chiudo Connection e PreparedStatement (il metodo è a prova di null ed
       * ignora ogni eventuale eccezione)
       */
      close(null, stmt, conn);

      // Fine metodo
      SolmrLogger.debug(this,
          "[StampeGaaDAO::getFormaConduzioneSezioneAnagrafica] END.");
    }

  }
  
  public Vector<TipoInfoAggiuntivaVO> getTipoInfoAggiuntiveStampa(long idAzienda, 
      Date dataInserimentoDichiarazione) 
    throws DataAccessException
  {
    String query = null;
    Connection conn = null;
    PreparedStatement stmt = null;
    StringBuffer queryBuf = null;
    Vector<TipoInfoAggiuntivaVO> vTipoInfoAggiuntiva = null;
    TipoInfoAggiuntivaVO tipoInfoAggiuntivaVO = null;
  
    try
    {
      SolmrLogger
        .debug(this,
            "[StampaGaaDAO::getTipoInfoAggiuntiveStampa] BEGIN.");
  
      /* CONCATENAZIONE/CREAZIONE QUERY BEGIN. */
  
      queryBuf = new StringBuffer();
      queryBuf.append("" +
        "SELECT TIA.ID_INFO_AGGIUNTIVA, " + 
        "       TIA.CODICE_INFO_AGGIUNTIVA, " + 
        "       TIA.DESCRIZIONE " +
        "FROM   DB_TIPO_INFO_AGGIUNTIVA TIA, " +
        "       DB_R_AZIENDA_INFO_AGGIUNTIVA AIA " +
        "WHERE  AIA.ID_AZIENDA = ? " +
        "AND    AIA.DATA_INIZIO_VALIDITA <= ? " + 
        "AND    NVL(AIA.DATA_FINE_VALIDITA, TO_DATE('31/12/9999','dd/mm/yyyy')) > ? " +
        "AND    AIA.ID_INFO_AGGIUNTIVA = TIA.ID_INFO_AGGIUNTIVA " +
        "ORDER BY TIA.DESCRIZIONE ASC ");
      
      
      
      
      query = queryBuf.toString();
      /* CONCATENAZIONE/CREAZIONE QUERY END. */
  
      conn = getDatasource().getConnection();
      if (SolmrLogger.isDebugEnabled(this))
      {
        // Dato che la query costruita dinamicamente è un dato importante la
        // registro sul file di log se il
        // debug è abilitato
  
        SolmrLogger.debug(this,
            "[StampaGaaDAO::getTipoInfoAggiuntiveStampa] Query="
                + query);
      }
      stmt = conn.prepareStatement(query);
  
      // Setto i parametri della query
      int indice = 0;
      stmt.setLong(++indice, idAzienda);
      stmt.setTimestamp(++indice,convertDateToTimestamp(dataInserimentoDichiarazione));
      stmt.setTimestamp(++indice,convertDateToTimestamp(dataInserimentoDichiarazione));
      
      
      ResultSet rs = stmt.executeQuery();
      
      while(rs.next())
      {
        if(vTipoInfoAggiuntiva == null)
        {
          vTipoInfoAggiuntiva = new Vector<TipoInfoAggiuntivaVO>();
        }
        tipoInfoAggiuntivaVO = new TipoInfoAggiuntivaVO();
        tipoInfoAggiuntivaVO.setIdInfoAggiuntiva(rs.getLong("ID_INFO_AGGIUNTIVA"));
        tipoInfoAggiuntivaVO.setCodiceInfoAggiuntiva(rs.getString("CODICE_INFO_AGGIUNTIVA"));
        tipoInfoAggiuntivaVO.setDescrizione(rs.getString("DESCRIZIONE"));
        
        vTipoInfoAggiuntiva.add(tipoInfoAggiuntivaVO);
      }
      
      return vTipoInfoAggiuntiva;
    }
    catch (Throwable t)
    {
      // Vettore di variabili interne del metodo
      Variabile variabili[] = new Variabile[]
      { new Variabile("query", query), new Variabile("queryBuf", queryBuf),
          new Variabile("vTipoInfoAggiuntiva", vTipoInfoAggiuntiva),
          new Variabile("tipoInfoAggiuntivaVO", tipoInfoAggiuntivaVO)};
      
      // Vettore di parametri passati al metodo
      Parametro parametri[] = new Parametro[]
      { new Parametro("idAzienda", idAzienda),
        new Parametro("dataInserimentoDichiarazione", dataInserimentoDichiarazione)};
  
      
      // Logging dell'eccezione, query, variabili e parametri del metodo
      LoggerUtils
          .logDAOError(
              this,
              "[StampaGaaDAO::getTipoInfoAggiuntiveStampa] ",
              t, query, variabili, parametri);
      /*
       * Rimappo e rilancio l'eccezione come DataAccessException.
       */
      throw new DataAccessException(t.getMessage());
    }
    finally
    {
      /*
       * Chiudo Connection e PreparedStatement (il metodo è a prova di null ed
       * ignora ogni eventuale eccezione)
       */
      close(null, stmt, conn);
  
      // Fine metodo
      SolmrLogger
          .debug(this,
              "[StampaGaaDAO::getTipoInfoAggiuntiveStampa] END.");
    }
  }
  
  /**
   * elenco delle aziende in cui si è socio...
   * 
   * 
   * @param idAzienda
   * @param dataInserimentoDichiarazione
   * @return
   * @throws DataAccessException
   */
  public Vector<AnagAziendaVO> getAziendeAssociateStampa(long idAzienda, 
      Date dataInserimentoDichiarazione) 
    throws DataAccessException
  {
    String query = null;
    Connection conn = null;
    PreparedStatement stmt = null;
    StringBuffer queryBuf = null;
    Vector<AnagAziendaVO> vAziendeAssociate = null;
    AnagAziendaVO anagAziendaVO = null;
  
    try
    {
      SolmrLogger
        .debug(this,
            "[StampaGaaDAO::getAziendeAssociateStampa] BEGIN.");
  
      /* CONCATENAZIONE/CREAZIONE QUERY BEGIN. */
  
      queryBuf = new StringBuffer();
      queryBuf.append("" +
        "SELECT AA.DENOMINAZIONE, " + 
        "       AA.INTESTAZIONE_PARTITA_IVA, " +
        "       AA.CUAA  " +
        "FROM   DB_AZIENDA_COLLEGATA AC, " +
        "       DB_ANAGRAFICA_AZIENDA AA " +
        "WHERE  AC.ID_AZIENDA_ASSOCIATA = ? " +
        "AND    AC.DATA_INIZIO_VALIDITA <= ? " + 
        "AND    NVL(AC.DATA_FINE_VALIDITA, TO_DATE('31/12/9999','dd/mm/yyyy')) > ? " +
        "AND    AC.ID_AZIENDA = AA.ID_AZIENDA " +
        "AND    AA.DATA_INIZIO_VALIDITA <= ? " + 
        "AND    NVL(AA.DATA_FINE_VALIDITA, TO_DATE('31/12/9999','dd/mm/yyyy')) > ? " +
        "ORDER BY AA.DENOMINAZIONE ASC ");
      
      
      
      
      query = queryBuf.toString();
      /* CONCATENAZIONE/CREAZIONE QUERY END. */
  
      conn = getDatasource().getConnection();
      if (SolmrLogger.isDebugEnabled(this))
      {
        // Dato che la query costruita dinamicamente è un dato importante la
        // registro sul file di log se il
        // debug è abilitato
  
        SolmrLogger.debug(this,
            "[StampaGaaDAO::getAziendeAssociateStampa] Query="
                + query);
      }
      stmt = conn.prepareStatement(query);
  
      // Setto i parametri della query
      int indice = 0;
      stmt.setLong(++indice, idAzienda);
      stmt.setTimestamp(++indice,convertDateToTimestamp(dataInserimentoDichiarazione));
      stmt.setTimestamp(++indice,convertDateToTimestamp(dataInserimentoDichiarazione));
      stmt.setTimestamp(++indice,convertDateToTimestamp(dataInserimentoDichiarazione));
      stmt.setTimestamp(++indice,convertDateToTimestamp(dataInserimentoDichiarazione));
      
      
      ResultSet rs = stmt.executeQuery();
      
      while(rs.next())
      {
        if(vAziendeAssociate == null)
        {
          vAziendeAssociate = new Vector<AnagAziendaVO>();
        }
        anagAziendaVO = new AnagAziendaVO();
        anagAziendaVO.setDenominazione(rs.getString("DENOMINAZIONE"));
        anagAziendaVO.setIntestazionePartitaIva(rs.getString("INTESTAZIONE_PARTITA_IVA"));
        anagAziendaVO.setCUAA(rs.getString("CUAA"));
        
        vAziendeAssociate.add(anagAziendaVO);
      }
      
      return vAziendeAssociate;
    }
    catch (Throwable t)
    {
      // Vettore di variabili interne del metodo
      Variabile variabili[] = new Variabile[]
      { new Variabile("query", query), new Variabile("queryBuf", queryBuf),
          new Variabile("vAziendeAssociate", vAziendeAssociate),
          new Variabile("anagAziendaVO", anagAziendaVO)};
      
      // Vettore di parametri passati al metodo
      Parametro parametri[] = new Parametro[]
      { new Parametro("idAzienda", idAzienda),
          new Parametro("dataInserimentoDichiarazione", dataInserimentoDichiarazione) };
  
      
      // Logging dell'eccezione, query, variabili e parametri del metodo
      LoggerUtils
          .logDAOError(
              this,
              "[StampaGaaDAO::getAziendeAssociateStampa] ",
              t, query, variabili, parametri);
      /*
       * Rimappo e rilancio l'eccezione come DataAccessException.
       */
      throw new DataAccessException(t.getMessage());
    }
    finally
    {
      /*
       * Chiudo Connection e PreparedStatement (il metodo è a prova di null ed
       * ignora ogni eventuale eccezione)
       */
      close(null, stmt, conn);
  
      // Fine metodo
      SolmrLogger
          .debug(this,
              "[StampaGaaDAO::getAziendeAssociateStampa] END.");
    }
  }
  
  
  /**
   * 
   * Usato nella stampa fscicolo pe ril riepilogo per conduzione....
   * 
   * 
   * @param idAzienda
   * @param codFotografia
   * @return
   * @throws DataAccessException
   */
  public Vector<ConduzioneParticellaVO> getRiepilogoConduzioneStampa(long idAzienda, 
      Long codFotografia) 
    throws DataAccessException
  {
    String query = null;
    Connection conn = null;
    PreparedStatement stmt = null;
    StringBuffer queryBuf = null;
    Vector<ConduzioneParticellaVO> vConduzioneParticella = null;
    ConduzioneParticellaVO conduzioneParticellaVO = null;
  
    try
    {
      SolmrLogger
        .debug(this,
            "[StampaGaaDAO::getRiepilogoConduzioneStampa] BEGIN.");
  
      /* CONCATENAZIONE/CREAZIONE QUERY BEGIN. */
  
      queryBuf = new StringBuffer();
      queryBuf.append("" +
        "SELECT TTP.DESCRIZIONE AS DESC_CONDUZIONE, " +
        "       TTP.ID_TITOLO_POSSESSO, " +
        "       SUM (CP.SUPERFICIE_CONDOTTA) AS SUPERFICIE_CONDOTTA, " +
        "       NVL(SUM(DECODE(TTP.ID_TITOLO_POSSESSO,5,CP.SUPERFICIE_CONDOTTA,CP.SUPERFICIE_AGRONOMICA)),0) " +
        "         AS SUP_AGRONOMICA " +
        "FROM   DB_UTE U, " +
        "       DB_STORICO_PARTICELLA SP, " + 
        "       DB_TIPO_TITOLO_POSSESSO TTP, " +
        "       DB_CONDUZIONE_PARTICELLA CP " +
        "WHERE  U.ID_AZIENDA = ? " +
        "AND    U.DATA_FINE_ATTIVITA IS NULL " +  
        "AND    CP.ID_UTE = U.ID_UTE " +
        "AND    CP.DATA_FINE_CONDUZIONE IS NULL " +  
        "AND    CP.ID_PARTICELLA = SP.ID_PARTICELLA " + 
        "AND    SP.DATA_FINE_VALIDITA IS NULL " +
        "AND    CP.ID_TITOLO_POSSESSO = TTP.ID_TITOLO_POSSESSO " +
        "GROUP BY TTP.DESCRIZIONE,TTP.ID_TITOLO_POSSESSO " +
        "ORDER BY TTP.DESCRIZIONE ");
      if(Validator.isNotEmpty(codFotografia))
      {
        queryBuf = new StringBuffer();
        queryBuf.append("" +
          "SELECT TTP.DESCRIZIONE AS DESC_CONDUZIONE, " +
          "       TTP.ID_TITOLO_POSSESSO, " +
          "       SUM(CD.SUPERFICIE_CONDOTTA) AS SUPERFICIE_CONDOTTA, " +
          "       NVL(SUM(DECODE(TTP.ID_TITOLO_POSSESSO,5,CD.SUPERFICIE_CONDOTTA,CD.SUPERFICIE_AGRONOMICA)),0) " +
          "        AS SUP_AGRONOMICA " + 
          "FROM   DB_CONDUZIONE_DICHIARATA CD, " + 
          "       DB_TIPO_TITOLO_POSSESSO TTP " +
          "WHERE  CD.CODICE_FOTOGRAFIA_TERRENI = ? " + 
          "AND    CD.ID_TITOLO_POSSESSO = TTP.ID_TITOLO_POSSESSO " + 
          "GROUP BY TTP.DESCRIZIONE, " +
          "         TTP.ID_TITOLO_POSSESSO " +
          "ORDER BY TTP.DESCRIZIONE ");
      }
      
      
      
      query = queryBuf.toString();
      /* CONCATENAZIONE/CREAZIONE QUERY END. */
  
      conn = getDatasource().getConnection();
      if (SolmrLogger.isDebugEnabled(this))
      {
        // Dato che la query costruita dinamicamente è un dato importante la
        // registro sul file di log se il
        // debug è abilitato
  
        SolmrLogger.debug(this,
            "[StampaGaaDAO::getRiepilogoConduzioneStampa] Query="
                + query);
      }
      stmt = conn.prepareStatement(query);
  
      // Setto i parametri della query
      int indice = 0;
      if(Validator.isNotEmpty(codFotografia))
      {
        stmt.setLong(++indice, codFotografia.longValue());
      }
      else
      {
        stmt.setLong(++indice, idAzienda);
      }
      
      
      
      ResultSet rs = stmt.executeQuery();
      
      while(rs.next())
      {
        if(vConduzioneParticella == null)
        {
          vConduzioneParticella = new Vector<ConduzioneParticellaVO>();
        }
        conduzioneParticellaVO = new ConduzioneParticellaVO();
        CodeDescription cd = new CodeDescription();
        cd.setCode(new Integer(rs.getInt("ID_TITOLO_POSSESSO")));
        cd.setDescription(rs.getString("DESC_CONDUZIONE"));
        conduzioneParticellaVO.setTitoloPossesso(cd);
        conduzioneParticellaVO.setSuperficieCondotta(rs.getString("SUPERFICIE_CONDOTTA"));
        conduzioneParticellaVO.setSuperficieAgronomica(rs.getString("SUP_AGRONOMICA"));
        
        
        vConduzioneParticella.add(conduzioneParticellaVO);
      }
      
      return vConduzioneParticella;
    }
    catch (Throwable t)
    {
      // Vettore di variabili interne del metodo
      Variabile variabili[] = new Variabile[]
      { new Variabile("query", query), new Variabile("queryBuf", queryBuf),
          new Variabile("vConduzioneParticella", vConduzioneParticella),
          new Variabile("conduzioneParticellaVO", conduzioneParticellaVO)};
      
      // Vettore di parametri passati al metodo
      Parametro parametri[] = new Parametro[]
      { new Parametro("idAzienda", idAzienda),
          new Parametro("codFotografia", codFotografia) };
  
      
      // Logging dell'eccezione, query, variabili e parametri del metodo
      LoggerUtils
          .logDAOError(
              this,
              "[StampaGaaDAO::getRiepilogoConduzioneStampa] ",
              t, query, variabili, parametri);
      /*
       * Rimappo e rilancio l'eccezione come DataAccessException.
       */
      throw new DataAccessException(t.getMessage());
    }
    finally
    {
      /*
       * Chiudo Connection e PreparedStatement (il metodo è a prova di null ed
       * ignora ogni eventuale eccezione)
       */
      close(null, stmt, conn);
  
      // Fine metodo
      SolmrLogger
          .debug(this,
              "[StampaGaaDAO::getRiepilogoConduzioneStampa] END.");
    }
  }
  
  
  public Vector<StoricoParticellaVO> getRiepilogoComuneStampa(long idAzienda, 
      Long codFotografia) 
    throws DataAccessException
  {
    String query = null;
    Connection conn = null;
    PreparedStatement stmt = null;
    StringBuffer queryBuf = null;
    Vector<StoricoParticellaVO> vStoricoParticella = null;
    StoricoParticellaVO storicoParticellaVO = null;
  
    try
    {
      SolmrLogger
        .debug(this,
            "[StampaGaaDAO::getRiepilogoComuneStampa] BEGIN.");
  
      /* CONCATENAZIONE/CREAZIONE QUERY BEGIN. */
  
      queryBuf = new StringBuffer();
      queryBuf.append("" +
        "SELECT CO.DESCOM, " +
        "       PO.SIGLA_PROVINCIA, " +
        "       SUM (CP.SUPERFICIE_CONDOTTA) AS SUPERFICIE_CONDOTTA, " +
        "       NVL(SUM(DECODE(CP.ID_TITOLO_POSSESSO,5,CP.SUPERFICIE_CONDOTTA,CP.SUPERFICIE_AGRONOMICA)),0) " + 
        "         AS SUP_AGRONOMICA " +
        "FROM   DB_UTE U, " +
        "       DB_STORICO_PARTICELLA SP, " +  
        "       COMUNE CO, " +
        "       DB_CONDUZIONE_PARTICELLA CP, " +
        "       PROVINCIA PO " +
        "WHERE  U.ID_AZIENDA = ? " + 
        "AND    U.DATA_FINE_ATTIVITA IS NULL " +   
        "AND    CP.ID_UTE = U.ID_UTE " +
        "AND    CP.DATA_FINE_CONDUZIONE IS NULL " +   
        "AND    CP.ID_PARTICELLA = SP.ID_PARTICELLA " +  
        "AND    SP.DATA_FINE_VALIDITA IS NULL " +
        "AND    SP.COMUNE = CO.ISTAT_COMUNE " +
        "AND    CO.ISTAT_PROVINCIA = PO.ISTAT_PROVINCIA " +
        "GROUP BY CO.DESCOM, " +
        "         PO.SIGLA_PROVINCIA " +
        "ORDER BY CO.DESCOM");
      if(Validator.isNotEmpty(codFotografia))
      {
        queryBuf = new StringBuffer();
        queryBuf.append("" +
          "SELECT CO.DESCOM, " +
          "       PO.SIGLA_PROVINCIA, " + 
          "       SUM(CD.SUPERFICIE_CONDOTTA) AS SUPERFICIE_CONDOTTA, " + 
          "       NVL(SUM(DECODE(CD.ID_TITOLO_POSSESSO,5,CD.SUPERFICIE_CONDOTTA,CD.SUPERFICIE_AGRONOMICA)),0) " + 
          "        AS SUP_AGRONOMICA  " +
          "FROM   DB_CONDUZIONE_DICHIARATA CD, " +  
          "       COMUNE CO, " +
          "       PROVINCIA PO, " +
          "       DB_STORICO_PARTICELLA SP " +
          "WHERE  CD.CODICE_FOTOGRAFIA_TERRENI = ? " +  
          "AND     CD.ID_STORICO_PARTICELLA = SP.ID_STORICO_PARTICELLA " +
          "AND    SP.COMUNE = CO.ISTAT_COMUNE " +
          "AND    CO.ISTAT_PROVINCIA = PO.ISTAT_PROVINCIA " +  
          "GROUP BY CO.DESCOM, " +
          "         PO.SIGLA_PROVINCIA " + 
          "ORDER BY CO.DESCOM ");        
      }
      
      
      
      query = queryBuf.toString();
      /* CONCATENAZIONE/CREAZIONE QUERY END. */
  
      conn = getDatasource().getConnection();
      if (SolmrLogger.isDebugEnabled(this))
      {
        // Dato che la query costruita dinamicamente è un dato importante la
        // registro sul file di log se il
        // debug è abilitato
  
        SolmrLogger.debug(this,
            "[StampaGaaDAO::getRiepilogoComuneStampa] Query="
                + query);
      }
      stmt = conn.prepareStatement(query);
  
      // Setto i parametri della query
      int indice = 0;
      if(Validator.isNotEmpty(codFotografia))
      {
        stmt.setLong(++indice, codFotografia.longValue());
      }
      else
      {
        stmt.setLong(++indice, idAzienda);
      }
      
      
      
      ResultSet rs = stmt.executeQuery();
      
      while(rs.next())
      {
        if(vStoricoParticella == null)
        {
          vStoricoParticella = new Vector<StoricoParticellaVO>();
        }
        storicoParticellaVO = new StoricoParticellaVO();
        ComuneVO comuneVO = new ComuneVO();
        comuneVO.setDescom(rs.getString("DESCOM")+" ("+rs.getString("SIGLA_PROVINCIA")+")");
        storicoParticellaVO.setComuneParticellaVO(comuneVO);
        
        ConduzioneParticellaVO conduzioneParticellaVO = new ConduzioneParticellaVO();        
        conduzioneParticellaVO.setSuperficieCondotta(rs.getString("SUPERFICIE_CONDOTTA"));
        conduzioneParticellaVO.setSuperficieAgronomica(rs.getString("SUP_AGRONOMICA"));
        
        ConduzioneParticellaVO[] elencoConduzioni = new ConduzioneParticellaVO[1];
        elencoConduzioni[0] = conduzioneParticellaVO;
        
        storicoParticellaVO.setElencoConduzioni(elencoConduzioni);
        
        
        vStoricoParticella.add(storicoParticellaVO);
      }
      
      return vStoricoParticella;
    }
    catch (Throwable t)
    {
      // Vettore di variabili interne del metodo
      Variabile variabili[] = new Variabile[]
      { new Variabile("query", query), new Variabile("queryBuf", queryBuf),
          new Variabile("storicoParticellaVO", storicoParticellaVO),
          new Variabile("vStoricoParticella", vStoricoParticella)};
      
      // Vettore di parametri passati al metodo
      Parametro parametri[] = new Parametro[]
      { new Parametro("idAzienda", idAzienda),
          new Parametro("codFotografia", codFotografia) };
  
      
      // Logging dell'eccezione, query, variabili e parametri del metodo
      LoggerUtils
          .logDAOError(
              this,
              "[StampaGaaDAO::getRiepilogoComuneStampa] ",
              t, query, variabili, parametri);
      /*
       * Rimappo e rilancio l'eccezione come DataAccessException.
       */
      throw new DataAccessException(t.getMessage());
    }
    finally
    {
      /*
       * Chiudo Connection e PreparedStatement (il metodo è a prova di null ed
       * ignora ogni eventuale eccezione)
       */
      close(null, stmt, conn);
  
      // Fine metodo
      SolmrLogger
          .debug(this,
              "[StampaGaaDAO::getRiepilogoComuneStampa] END.");
    }
  }
  
  
  public Vector<RiepiloghiUnitaArboreaVO> getStampaUVRiepilogoVitignoIdoneita(
      Long idAzienda)  throws DataAccessException
  {
    
    Connection conn = null;
    String query = null;
    PreparedStatement stmt = null;
    ResultSet rs = null;
    StringBuffer queryBuf = null;
    Vector<RiepiloghiUnitaArboreaVO> vRiepiloghi = null;
    
    int idx = 0;
    try
    {
      SolmrLogger
      .debug(
          this,
          "[StampaGaaDAO::getStampaUVRiepilogoVitignoIdoneita] BEGIN.");

      queryBuf = new StringBuffer();
    
      /* CONCATENAZIONE/CREAZIONE QUERY BEGIN. */
      queryBuf.append("" + 
          "WITH UNITA_VITATE AS " +
          "    (SELECT SUA.ID_STORICO_UNITA_ARBOREA AS ID_STORICO_UNITA_ARBOREA, " +
          "            SUA.ID_PARTICELLA AS ID_PARTICELLA, " +
          "            SUA.AREA AS AREA, " +
          "            TV.DESCRIZIONE AS DESC_VARIETA, " +
          "            TV.CODICE_VARIETA, " +  
          "            TTV.DESCRIZIONE AS DESCRIZIONE, " + 
          "            TTV.ID_TIPOLOGIA_VINO AS ID_TIPOLOGIA_VINO " +
          "     FROM   DB_STORICO_UNITA_ARBOREA SUA, " +
          "            DB_TIPO_TIPOLOGIA_VINO TTV, " +
          "            DB_UTE U, " +
          "            DB_CONDUZIONE_PARTICELLA CP, " +
          "            DB_TIPO_VARIETA TV, " +
          "            DB_R_CATALOGO_MATRICE RCM, " +
          "            DB_TIPO_DESTINAZIONE TDU, " +
          "            DB_TIPO_UTILIZZO TU " +
          "     WHERE  SUA.ID_AZIENDA = ? " +  
          "     AND    SUA.ID_AZIENDA = U.ID_AZIENDA " + 
          "     AND    U.DATA_FINE_ATTIVITA IS NULL " +
          "     AND    U.ID_UTE = CP.ID_UTE " +
          "     AND    CP.ID_PARTICELLA = SUA.ID_PARTICELLA " + 
          "     AND    CP.DATA_FINE_CONDUZIONE IS NULL " +
          "     AND    SUA.DATA_FINE_VALIDITA IS NULL " +
          "     AND    SUA.ID_CATALOGO_MATRICE = RCM.ID_CATALOGO_MATRICE " +
          "     AND    RCM.ID_UTILIZZO = TU.ID_UTILIZZO " +
          "     AND    TU.CODICE = '"+SolmrConstants.COD_UTILIZZO_UVA_DA_VINO+"' "+
          "     AND    RCM.ID_TIPO_DESTINAZIONE = TDU.ID_TIPO_DESTINAZIONE " +
          "     AND    TDU.CODICE_DESTINAZIONE = '"+SolmrConstants.COD_DESTINAZIONE_UVA_DA_VINO+"' "+
          "     AND    SUA.ID_TIPOLOGIA_UNAR = 2 " + 
          "     AND    RCM.ID_VARIETA = TV.ID_VARIETA " +
          "     AND    SUA.ID_TIPOLOGIA_VINO = TTV.ID_TIPOLOGIA_VINO " + 
          "     GROUP BY SUA.ID_STORICO_UNITA_ARBOREA, " +
          "              SUA.ID_PARTICELLA, " +
          "              SUA.AREA, " +
          "              TTV.DESCRIZIONE, " + 
          "              TTV.ID_TIPOLOGIA_VINO, " +
          "              TV.DESCRIZIONE, " +
          "              TV.CODICE_VARIETA) " +
          "SELECT UA.DESC_VARIETA, " +
          "       UA.CODICE_VARIETA, " +
          "       COUNT(*) AS NUM_UNAR, " +
          "       SUM(UA.AREA) AS AREA, " +
          "       UA.DESCRIZIONE, " +   
          "       UA.ID_TIPOLOGIA_VINO " +  
          "FROM   UNITA_VITATE UA " +
          "GROUP BY UA.DESC_VARIETA, " + 
          "         UA.CODICE_VARIETA, " +
          "         UA.DESCRIZIONE, " +
          "         UA.ID_TIPOLOGIA_VINO " + 
          "ORDER BY UA.DESC_VARIETA, " +
          "         UA.DESCRIZIONE");

      /* CONCATENAZIONE/CREAZIONE QUERY END. */
      
      conn = getDatasource().getConnection();
      query = queryBuf.toString();
      if (SolmrLogger.isDebugEnabled(this))
      {
        // Dato che la query costruita dinamicamente è un dato importante la
        // registro sul file di log se il
        // debug è abilitato
    
        SolmrLogger
            .debug(
                this,
                "[StampaGaaDAO::getStampaUVRiepilogoVitignoIdoneita] Query="
                    + query);
      }
      stmt = conn.prepareStatement(query);
    
      // Setto i parametri della query
      stmt.setLong(++idx, idAzienda.longValue());

      rs = stmt.executeQuery();

      while (rs.next())
      {
        if(vRiepiloghi == null)
        {
          vRiepiloghi = new Vector<RiepiloghiUnitaArboreaVO>();
        }
        
        RiepiloghiUnitaArboreaVO riepiloghiUnitaArboreaVO = new RiepiloghiUnitaArboreaVO();
        riepiloghiUnitaArboreaVO.setDescVarieta("["+rs.getString("CODICE_VARIETA")+"] "+rs.getString("DESC_VARIETA"));
        riepiloghiUnitaArboreaVO.setTipoTipolgiaVino(rs.getString("DESCRIZIONE"));
        riepiloghiUnitaArboreaVO.setIdTipolgiaVino(checkLongNull(rs.getString("ID_TIPOLOGIA_VINO")));
        riepiloghiUnitaArboreaVO.setSupVitata(rs.getBigDecimal("AREA"));
        riepiloghiUnitaArboreaVO.setNumElementi(new Integer(rs.getInt("NUM_UNAR")));
        
        vRiepiloghi.add(riepiloghiUnitaArboreaVO);
      }
      
      return vRiepiloghi;

    }
    catch (Throwable t)
    {
      Parametro parametri[] = new Parametro[]
      { new Parametro("idAzienda", idAzienda)};
      Variabile variabili[] = new Variabile[]
      { new Variabile("vRiepiloghi", vRiepiloghi), new Variabile("idx", idx)

      };
      LoggerUtils.logDAOError(this,
          "[StampaGaaDAO::getStampaUVRiepilogoIdoneita] ", t, query,
          variabili, parametri);
      throw new DataAccessException(t.getMessage());
    }
    finally
    {
      close(rs, stmt, conn);
      SolmrLogger
          .debug(this, "[StampaGaaDAO::getStampaUVRiepilogoIdoneita] END.");
    }
  }
  
  
  public Vector<RiepiloghiUnitaArboreaVO> getStampaUVRiepilogoVitignoIdoneitaAllaDich(
      Long idDichiarazioneConsistenza)  throws DataAccessException
  {
    
    Connection conn = null;
    String query = null;
    PreparedStatement stmt = null;
    ResultSet rs = null;
    StringBuffer queryBuf = null;
    Vector<RiepiloghiUnitaArboreaVO> vRiepiloghi = null;
    
    int idx = 0;
    try
    {
      SolmrLogger
      .debug(
          this,
          "[StampaGaaDAO::getStampaUVRiepilogoVitignoIdoneitaAllaDich] BEGIN.");

      queryBuf = new StringBuffer();
    
      /* CONCATENAZIONE/CREAZIONE QUERY BEGIN. */
      queryBuf.append("" + 
          "SELECT TV.DESCRIZIONE AS DESC_VARIETA, " +
          "       TV.CODICE_VARIETA, " +
          "       TTV.DESCRIZIONE, " +
          "       COUNT(*) AS NUM_UNAR, " +
          "       SUM(UAD.AREA) AS AREA " + 
          "FROM   DB_UNITA_ARBOREA_DICHIARATA UAD, " +
          "       DB_TIPO_TIPOLOGIA_VINO TTV, " +
          "       DB_DICHIARAZIONE_CONSISTENZA DC, " +   
          "       DB_TIPO_VARIETA TV, " +
          "       DB_R_CATALOGO_MATRICE RCM, " +
          "       DB_TIPO_UTILIZZO TU, " +
          "       DB_TIPO_DESTINAZIONE TDU " +
          "WHERE  DC.ID_DICHIARAZIONE_CONSISTENZA = ? " +
          "AND    DC.CODICE_FOTOGRAFIA_TERRENI = UAD.CODICE_FOTOGRAFIA_TERRENI " +  
          "AND    UAD.ID_TIPOLOGIA_UNAR = 2 " +
          "AND    RCM.ID_VARIETA = TV.ID_VARIETA " + 
          "AND    UAD.ID_CATALOGO_MATRICE = RCM.ID_CATALOGO_MATRICE " +
          "AND    RCM.ID_UTILIZZO = TU.ID_UTILIZZO " +
          "AND    TU.CODICE = '"+SolmrConstants.COD_UTILIZZO_UVA_DA_VINO+"' "+
          "AND    RCM.ID_TIPO_DESTINAZIONE = TDU.ID_TIPO_DESTINAZIONE " +
          "AND    TDU.CODICE_DESTINAZIONE = '"+SolmrConstants.COD_DESTINAZIONE_UVA_DA_VINO+"' "+
          "AND    UAD.ID_TIPOLOGIA_VINO = TTV.ID_TIPOLOGIA_VINO " +   
          "GROUP BY TV.DESCRIZIONE, " +
          "         TV.CODICE_VARIETA, " +
          "         TTV.DESCRIZIONE " +
          "ORDER BY TV.DESCRIZIONE, " +
          "         TTV.DESCRIZIONE");

      /* CONCATENAZIONE/CREAZIONE QUERY END. */
      
      conn = getDatasource().getConnection();
      query = queryBuf.toString();
      if (SolmrLogger.isDebugEnabled(this))
      {
        // Dato che la query costruita dinamicamente è un dato importante la
        // registro sul file di log se il
        // debug è abilitato
    
        SolmrLogger
            .debug(
                this,
                "[StampaGaaDAO::getStampaUVRiepilogoVitignoIdoneitaAllaDich] Query="
                    + query);
      }
      stmt = conn.prepareStatement(query);
    
      // Setto i parametri della query
      stmt.setLong(++idx, idDichiarazioneConsistenza.longValue());

      rs = stmt.executeQuery();

      while (rs.next())
      {
        if(vRiepiloghi == null)
        {
          vRiepiloghi = new Vector<RiepiloghiUnitaArboreaVO>();
        }
        
        RiepiloghiUnitaArboreaVO riepiloghiUnitaArboreaVO = new RiepiloghiUnitaArboreaVO();
        riepiloghiUnitaArboreaVO.setDescVarieta("["+rs.getString("CODICE_VARIETA")+"] "+rs.getString("DESC_VARIETA"));
        riepiloghiUnitaArboreaVO.setTipoTipolgiaVino(rs.getString("DESCRIZIONE"));
        riepiloghiUnitaArboreaVO.setSupVitata(rs.getBigDecimal("AREA"));
        riepiloghiUnitaArboreaVO.setNumElementi(new Integer(rs.getInt("NUM_UNAR")));
        
        vRiepiloghi.add(riepiloghiUnitaArboreaVO);
      }
      
      return vRiepiloghi;

    }
    catch (Throwable t)
    {
      Parametro parametri[] = new Parametro[]
      { new Parametro("idDichiarazioneConsistenza", idDichiarazioneConsistenza)};
      Variabile variabili[] = new Variabile[]
      { new Variabile("vRiepiloghi", vRiepiloghi), new Variabile("idx", idx)

      };
      LoggerUtils.logDAOError(this,
          "[StampaGaaDAO::getStampaUVRiepilogoIdoneita] ", t, query,
          variabili, parametri);
      throw new DataAccessException(t.getMessage());
    }
    finally
    {
      close(rs, stmt, conn);
      SolmrLogger
          .debug(this, "[StampaGaaDAO::getStampaUVRiepilogoVitignoIdoneitaAllaDich] END.");
    }
  }
  
  
  public Vector<FabbricatoVO> getStampaFabbricati(
      Long idAzienda, Date dataRiferimento)  throws DataAccessException
  {
    
    Connection conn = null;
    String query = null;
    PreparedStatement stmt = null;
    ResultSet rs = null;
    StringBuffer queryBuf = null;
    Vector<FabbricatoVO> vFabbricati = null;
    FabbricatoVO fabbricatoVO = null;
    
    
    try
    {
      SolmrLogger
      .debug(
          this,
          "[StampaGaaDAO::getStampaFabbricati] BEGIN.");

      queryBuf = new StringBuffer();
    
      /* CONCATENAZIONE/CREAZIONE QUERY BEGIN. */
      queryBuf.append("" + 
          "SELECT FA.ID_FABBRICATO, " +  
          "       TTF.DESCRIZIONE as DESC_TIPO_FAB, " +  
          "       FA.SUPERFICIE, " +
          "       TTF.UNITA_MISURA, " + 
          "       FA.DIMENSIONE, " +
          "       FA.ANNO_COSTRUZIONE " +
          "FROM   DB_FABBRICATO FA, " +
          "       DB_UTE UT, " +
          "       DB_TIPO_TIPOLOGIA_FABBRICATO TTF " +
          "WHERE  UT.ID_AZIENDA= ? " + 
          "AND    FA.ID_UTE = UT.ID_UTE " +  
          "AND    FA.ID_TIPOLOGIA_FABBRICATO = TTF.ID_TIPOLOGIA_FABBRICATO " +
          "AND    FA.DATA_INIZIO_VALIDITA <= ? " +
          "AND    NVL(FA.DATA_FINE_VALIDITA, TO_DATE('31/12/9999','dd/mm/yyyy')) > ? " +
          "ORDER BY TTF.DESCRIZIONE ");

      /* CONCATENAZIONE/CREAZIONE QUERY END. */
      
      conn = getDatasource().getConnection();
      query = queryBuf.toString();
      if (SolmrLogger.isDebugEnabled(this))
      {
        // Dato che la query costruita dinamicamente è un dato importante la
        // registro sul file di log se il
        // debug è abilitato
    
        SolmrLogger
            .debug(
                this,
                "[StampaGaaDAO::getStampaFabbricati] Query="
                    + query);
      }
      stmt = conn.prepareStatement(query);
    
      // Setto i parametri della query
      int idx = 0;
      stmt.setLong(++idx, idAzienda.longValue());
      stmt.setTimestamp(++idx, convertDateToTimestamp(dataRiferimento));
      stmt.setTimestamp(++idx, convertDateToTimestamp(dataRiferimento));

      rs = stmt.executeQuery();

      while (rs.next())
      {
        if(vFabbricati == null)
        {
          vFabbricati = new Vector<FabbricatoVO>();
        }
        
        fabbricatoVO = new FabbricatoVO();
        fabbricatoVO.setIdFabbricato(new Long(rs.getLong("ID_FABBRICATO")));
        fabbricatoVO.setDescrizioneTipoFabbricato(rs.getString("DESC_TIPO_FAB"));
        fabbricatoVO.setSuperficieFabbricato(rs.getString("SUPERFICIE"));
        fabbricatoVO.setUnitaMisura(rs.getString("UNITA_MISURA"));
        fabbricatoVO.setDimensioneFabbricato(rs.getString("DIMENSIONE"));
        fabbricatoVO.setAnnoCostruzioneFabbricato(rs.getString("ANNO_COSTRUZIONE"));
        
        vFabbricati.add(fabbricatoVO);
      }
      
      return vFabbricati;

    }
    catch (Throwable t)
    {
      Parametro parametri[] = new Parametro[]
      { new Parametro("idAzienda", idAzienda),
        new Parametro("dataRiferimento", dataRiferimento) };
      Variabile variabili[] = new Variabile[]
      { new Variabile("vFabbricati", vFabbricati), 
          new Variabile("fabbricatoVO", fabbricatoVO)
      };
      LoggerUtils.logDAOError(this,
          "[StampaGaaDAO::getStampaFabbricati] ", t, query,
          variabili, parametri);
      throw new DataAccessException(t.getMessage());
    }
    finally
    {
      close(rs, stmt, conn);
      SolmrLogger
          .debug(this, "[StampaGaaDAO::getStampaFabbricati] END.");
    }
  }
  
  /**
   * 
   * query per documenti nuova stampa modol...
   * 
   * 
   * @param idAzienda
   * @param cuaa
   * @return
   * @throws DataAccessException
   */
  public Vector<DocumentoVO> getStampaDocumentiMd(
      Long idAzienda, String cuaa)  throws DataAccessException
  {
    
    Connection conn = null;
    String query = null;
    PreparedStatement stmt = null;
    ResultSet rs = null;
    StringBuffer queryBuf = null;
    Vector<DocumentoVO> vDocumenti = null;
    DocumentoVO documentoVO = null;   
    
    try
    {
      SolmrLogger
      .debug(
          this,
          "[StampaGaaDAO::getStampaDocumentiMd] BEGIN.");

      queryBuf = new StringBuffer();
    
      /* CONCATENAZIONE/CREAZIONE QUERY BEGIN. */
      queryBuf.append("" + 
        "SELECT DISTINCT " +
        "       D.ID_DOCUMENTO, " +
        "       TTD.DESCRIZIONE AS TIPO_DOCUMENTO, " +
        "       TD.DESCRIZIONE AS DESC_DOCUMENTO, " +
        "       D.DATA_INIZIO_VALIDITA, " +
        "       D.DATA_FINE_VALIDITA, " +
        "       D.NUMERO_PROTOCOLLO, " +
        "       D.DATA_PROTOCOLLO, " +
        "       D.ID_STATO_DOCUMENTO, " +
        "       TTD.ID_TIPOLOGIA_DOCUMENTO, " +
        "       TD.ID_DOCUMENTO, " +
        "       TD.CODICE_DOCUMENTO " +
        "FROM   DB_DOCUMENTO D, " +
        "       DB_TIPO_TIPOLOGIA_DOCUMENTO TTD, " +
        "       DB_TIPO_DOCUMENTO TD, " +
        "       DB_TIPO_CATEGORIA_DOCUMENTO TCD, " +
        "       DB_DOCUMENTO_CATEGORIA DC " +
        "WHERE  D.CUAA = ? " +
        "AND    D.ID_AZIENDA = ? " +
        "AND    (" +
        "        D.ID_STATO_DOCUMENTO IS NULL " +
        "        OR " +
        "        (D.ID_STATO_DOCUMENTO <> 1 AND      D.ID_STATO_DOCUMENTO <> 2) " +
        "       ) " +
        "AND    (D.DATA_FINE_VALIDITA IS NULL OR D.DATA_FINE_VALIDITA > SYSDATE) " +
        "AND    TTD.ID_TIPOLOGIA_DOCUMENTO = TD.ID_TIPOLOGIA_DOCUMENTO " +
        "AND    TD.ID_DOCUMENTO = D.EXT_ID_DOCUMENTO " +
        "AND    TD.ID_DOCUMENTO = DC.ID_DOCUMENTO " +
        "AND    DC.ID_CATEGORIA_DOCUMENTO = TCD.ID_CATEGORIA_DOCUMENTO " +
        "ORDER BY " +
        "       D.ID_STATO_DOCUMENTO DESC, " +
        "       TTD.ID_TIPOLOGIA_DOCUMENTO, " +
        "       TD.ID_DOCUMENTO");

      /* CONCATENAZIONE/CREAZIONE QUERY END. */
      
      conn = getDatasource().getConnection();
      query = queryBuf.toString();
      if (SolmrLogger.isDebugEnabled(this))
      {
        // Dato che la query costruita dinamicamente è un dato importante la
        // registro sul file di log se il
        // debug è abilitato
    
        SolmrLogger.debug(this, "[StampaGaaDAO::getStampaDocumentiMd] Query="
                    + query);
      }
      stmt = conn.prepareStatement(query);
    
      // Setto i parametri della query
      int idx = 0;
      stmt.setString(++idx, cuaa);
      stmt.setLong(++idx, idAzienda.longValue());

      rs = stmt.executeQuery();

      while (rs.next())
      {
        if(vDocumenti == null)
        {
          vDocumenti = new Vector<DocumentoVO>();
        }
        
        documentoVO = new DocumentoVO();
        TipoDocumentoVO tipoDocumentoVO = new TipoDocumentoVO();
        documentoVO.setIdDocumento(new Long(rs.getLong("ID_DOCUMENTO")));
        tipoDocumentoVO.setDescrizione(rs.getString("DESC_DOCUMENTO"));
        CodeDescription tipoTipologiaDocumento = new CodeDescription(null, rs
            .getString("TIPO_DOCUMENTO"));
        tipoDocumentoVO.setCodiceDocumento(rs.getString("CODICE_DOCUMENTO"));
        tipoDocumentoVO.setTipoTipologiaDocumento(tipoTipologiaDocumento);
        documentoVO.setDataInizioValidita(rs.getTimestamp("DATA_INIZIO_VALIDITA"));
        documentoVO.setDataFineValidita(rs.getTimestamp("DATA_FINE_VALIDITA"));
        documentoVO.setNumeroProtocollo(rs.getString("NUMERO_PROTOCOLLO"));
        documentoVO.setDataProtocollo(rs.getTimestamp("DATA_PROTOCOLLO"));        
        documentoVO.setTipoDocumentoVO(tipoDocumentoVO);
        
        vDocumenti.add(documentoVO);
      }
      
      return vDocumenti;

    }
    catch (Throwable t)
    {
      Parametro parametri[] = new Parametro[]
      { new Parametro("idAzienda", idAzienda),
        new Parametro("cuaa", cuaa) };
      Variabile variabili[] = new Variabile[]
      { new Variabile("vDocumenti", vDocumenti), 
          new Variabile("documentoVO", documentoVO)
      };
      LoggerUtils.logDAOError(this,
          "[StampaGaaDAO::getStampaDocumentiMd] ", t, query,
          variabili, parametri);
      throw new DataAccessException(t.getMessage());
    }
    finally
    {
      close(rs, stmt, conn);
      SolmrLogger
          .debug(this, "[StampaGaaDAO::getStampaDocumentiMd] END.");
    }
  }
  
  public Vector<DocumentoVO> getStampaDocumentiAllaDichMd(
      Long idAzienda, Date dataConsistenza)  throws DataAccessException
  {
    
    Connection conn = null;
    String query = null;
    PreparedStatement stmt = null;
    ResultSet rs = null;
    StringBuffer queryBuf = null;
    Vector<DocumentoVO> vDocumenti = null;
    DocumentoVO documentoVO = null;   
    
    try
    {
      SolmrLogger
      .debug(
          this,
          "[StampaGaaDAO::getStampaDocumentiAllaDichMd] BEGIN.");

      queryBuf = new StringBuffer();
    
      /* CONCATENAZIONE/CREAZIONE QUERY BEGIN. */
      queryBuf.append("" + 
        "SELECT DISTINCT " +
        "       D.ID_DOCUMENTO, " +
        "       TTD.DESCRIZIONE AS TIPO_DOCUMENTO, " +
        "       TD.DESCRIZIONE AS DESC_DOCUMENTO, " +
        "       D.DATA_INIZIO_VALIDITA, " +
        "       D.DATA_FINE_VALIDITA, " +
        "       D.NUMERO_PROTOCOLLO, " +
        "       D.DATA_PROTOCOLLO, " +
        "       D.ID_STATO_DOCUMENTO, " +
        "       TTD.ID_TIPOLOGIA_DOCUMENTO, " +
        "       TD.ID_DOCUMENTO, " +
        "       TD.CODICE_DOCUMENTO " +
        "FROM   DB_DOCUMENTO D, " +
        "       DB_TIPO_TIPOLOGIA_DOCUMENTO TTD, " +
        "       DB_TIPO_DOCUMENTO TD, " +
        "       DB_TIPO_CATEGORIA_DOCUMENTO TCD, " +
        "       DB_DOCUMENTO_CATEGORIA DC " +
        "WHERE  D.ID_AZIENDA = ? " +
        "AND    D.DATA_INSERIMENTO <= ? " +
        "AND    ( D.DATA_VARIAZIONE_STATO IS NULL OR " +
        "         D.DATA_VARIAZIONE_STATO > ? ) " +
        "AND    TTD.ID_TIPOLOGIA_DOCUMENTO = TD.ID_TIPOLOGIA_DOCUMENTO " +
        "AND    TD.ID_DOCUMENTO = D.EXT_ID_DOCUMENTO " +
        "AND    TD.ID_DOCUMENTO = DC.ID_DOCUMENTO " +
        "AND    DC.ID_CATEGORIA_DOCUMENTO = TCD.ID_CATEGORIA_DOCUMENTO " +
        "AND    D.CUAA = " +
        "                ( " +
        "                  SELECT CUAA FROM DB_ANAGRAFICA_AZIENDA " +
        "                  WHERE ID_AZIENDA = ? " +
        "                  AND " +
        "                      ( " +
        "                       (DATA_INIZIO_VALIDITA <= ? AND DATA_FINE_VALIDITA IS NULL) " +
        "                        OR  " +
        "                       (DATA_INIZIO_VALIDITA <= ? AND DATA_FINE_VALIDITA >= ?) " +
        "                      ) " +
        "                ) " +
        "ORDER BY " +
        "      D.ID_STATO_DOCUMENTO DESC, " +
        "      TTD.ID_TIPOLOGIA_DOCUMENTO, " +
        "      TD.ID_DOCUMENTO");

      /* CONCATENAZIONE/CREAZIONE QUERY END. */
      
      conn = getDatasource().getConnection();
      query = queryBuf.toString();
      if (SolmrLogger.isDebugEnabled(this))
      {
        // Dato che la query costruita dinamicamente è un dato importante la
        // registro sul file di log se il
        // debug è abilitato
    
        SolmrLogger.debug(this, "[StampaGaaDAO::getStampaDocumentiAllaDichMd] Query="
                    + query);
      }
      stmt = conn.prepareStatement(query);
    
      // Setto i parametri della query
      int idx = 0;
      stmt.setLong(++idx, idAzienda.longValue());
      stmt.setTimestamp(++idx, convertDateToTimestamp(dataConsistenza));
      stmt.setTimestamp(++idx, convertDateToTimestamp(dataConsistenza));
      stmt.setLong(++idx, idAzienda.longValue());
      stmt.setTimestamp(++idx, convertDateToTimestamp(dataConsistenza));
      stmt.setTimestamp(++idx, convertDateToTimestamp(dataConsistenza));
      stmt.setTimestamp(++idx, convertDateToTimestamp(dataConsistenza));

      rs = stmt.executeQuery();

      while (rs.next())
      {
        if(vDocumenti == null)
        {
          vDocumenti = new Vector<DocumentoVO>();
        }
        
        documentoVO = new DocumentoVO();
        TipoDocumentoVO tipoDocumentoVO = new TipoDocumentoVO();
        documentoVO.setIdDocumento(new Long(rs.getLong("ID_DOCUMENTO")));
        tipoDocumentoVO.setDescrizione(rs.getString("DESC_DOCUMENTO"));
        CodeDescription tipoTipologiaDocumento = new CodeDescription(null, rs
            .getString("TIPO_DOCUMENTO"));
        tipoDocumentoVO.setCodiceDocumento(rs.getString("CODICE_DOCUMENTO"));
        tipoDocumentoVO.setTipoTipologiaDocumento(tipoTipologiaDocumento);
        documentoVO.setDataInizioValidita(rs.getTimestamp("DATA_INIZIO_VALIDITA"));
        documentoVO.setDataFineValidita(rs.getTimestamp("DATA_FINE_VALIDITA"));
        documentoVO.setNumeroProtocollo(rs.getString("NUMERO_PROTOCOLLO"));
        documentoVO.setDataProtocollo(rs.getTimestamp("DATA_PROTOCOLLO"));        
        documentoVO.setTipoDocumentoVO(tipoDocumentoVO);
        
        vDocumenti.add(documentoVO);
      }
      
      return vDocumenti;

    }
    catch (Throwable t)
    {
      Parametro parametri[] = new Parametro[]
      { new Parametro("idAzienda", idAzienda),
        new Parametro("dataConsistenza", dataConsistenza) };
      Variabile variabili[] = new Variabile[]
      { new Variabile("vDocumenti", vDocumenti), 
          new Variabile("documentoVO", documentoVO)
      };
      LoggerUtils.logDAOError(this,
          "[StampaGaaDAO::getStampaDocumentiAllaDichMd] ", t, query,
          variabili, parametri);
      throw new DataAccessException(t.getMessage());
    }
    finally
    {
      close(rs, stmt, conn);
      SolmrLogger
          .debug(this, "[StampaGaaDAO::getStampaDocumentiAllaDichMd] END.");
    }
  }
  
  
  public Vector<RiepilogoStampaTerrVO> getStampaRiepiloghiStoricoPartTipoArea(
      String tipoArea, Long idAzienda, Long codFotografia)  throws DataAccessException
  {
    
    Connection conn = null;
    String query = null;
    PreparedStatement stmt = null;
    ResultSet rs = null;
    StringBuffer queryBuf = null;
    Vector<RiepilogoStampaTerrVO> vRiga = null;
    RiepilogoStampaTerrVO rigaVO = null;
    
    try
    {
      SolmrLogger
      .debug(
          this,
          "[StampaGaaDAO::getStampaRiepiloghiStoricoPartTipoArea] BEGIN.");

      queryBuf = new StringBuffer();
    
      /* CONCATENAZIONE/CREAZIONE QUERY BEGIN. */
      queryBuf.append("" + 
        "SELECT    'Tipo Area "+tipoArea+" - ' || TIPO.DESCRIZIONE AS DESC_TIPO, " + 
        "       SUM (CP.SUPERFICIE_CONDOTTA) AS SUPERFICIE_CONDOTTA, " +
        "       NVL(SUM(DECODE(CP.ID_TITOLO_POSSESSO,5,CP.SUPERFICIE_CONDOTTA,CP.SUPERFICIE_AGRONOMICA)),0) " + 
        "         AS SUP_AGRONOMICA " +
        "FROM   DB_UTE U, " +
        "       DB_STORICO_PARTICELLA SP, " +   
        "       DB_TIPO_AREA_"+tipoArea+" TIPO, " +
        "       DB_CONDUZIONE_PARTICELLA CP " +               
        "WHERE  U.ID_AZIENDA = ? " +
        "AND    U.DATA_FINE_ATTIVITA IS NULL " +    
        "AND    CP.ID_UTE = U.ID_UTE " +
        "AND    CP.DATA_FINE_CONDUZIONE IS NULL " +    
        "AND    CP.ID_PARTICELLA = SP.ID_PARTICELLA " +   
        "AND    SP.DATA_FINE_VALIDITA IS NULL " +
        "AND    SP.ID_AREA_"+tipoArea+" = TIPO.ID_AREA_"+tipoArea+" "+
        "GROUP BY TIPO.DESCRIZIONE " +
        "ORDER BY TIPO.DESCRIZIONE");
      if(Validator.isNotEmpty(codFotografia))
      {
        queryBuf = new StringBuffer();
        queryBuf.append("" +
          "SELECT 'Tipo Area "+tipoArea+" - ' || TIPO.DESCRIZIONE AS DESC_TIPO, " + 
          "     SUM(CD.SUPERFICIE_CONDOTTA) AS SUPERFICIE_CONDOTTA, " +  
          "     NVL(SUM(DECODE(CD.ID_TITOLO_POSSESSO,5,CD.SUPERFICIE_CONDOTTA,CD.SUPERFICIE_AGRONOMICA)),0) " +
          "      AS SUP_AGRONOMICA " +
          "FROM   DB_CONDUZIONE_DICHIARATA CD, " +
          "       DB_STORICO_PARTICELLA SP, " +
          "       DB_TIPO_AREA_"+tipoArea+" TIPO " +
          "WHERE  CD.CODICE_FOTOGRAFIA_TERRENI = ? " +   
          "AND     CD.ID_STORICO_PARTICELLA = SP.ID_STORICO_PARTICELLA " + 
          "AND    SP.ID_AREA_"+tipoArea+" = TIPO.ID_AREA_"+tipoArea+" "+
          "GROUP BY TIPO.DESCRIZIONE " +
          "ORDER BY TIPO.DESCRIZIONE");        
      }

      /* CONCATENAZIONE/CREAZIONE QUERY END. */
      
      conn = getDatasource().getConnection();
      query = queryBuf.toString();
      if (SolmrLogger.isDebugEnabled(this))
      {
        // Dato che la query costruita dinamicamente è un dato importante la
        // registro sul file di log se il
        // debug è abilitato
    
        SolmrLogger.debug(this, "[StampaGaaDAO::getStampaRiepiloghiStoricoPartTipoArea] Query="
                    + query);
      }
      stmt = conn.prepareStatement(query);
    
      // Setto i parametri della query
      // Setto i parametri della query
      int indice = 0;
      if(Validator.isNotEmpty(codFotografia))
      {
        stmt.setLong(++indice, codFotografia.longValue());
      }
      else
      {
        stmt.setLong(++indice, idAzienda);
      }

      rs = stmt.executeQuery();

      while (rs.next())
      {
        if(vRiga == null)
        {
          vRiga = new Vector<RiepilogoStampaTerrVO>();
        }
        rigaVO = new RiepilogoStampaTerrVO(); 
        
        rigaVO.setDescrizione(rs.getString("DESC_TIPO"));
        rigaVO.setValore1(rs.getBigDecimal("SUP_AGRONOMICA"));
        rigaVO.setValore2(rs.getBigDecimal("SUPERFICIE_CONDOTTA"));
        
        vRiga.add(rigaVO);
      }
      
      return vRiga;

    }
    catch (Throwable t)
    {
      Parametro parametri[] = new Parametro[]
      { new Parametro("idAzienda", idAzienda),
        new Parametro("tipoArea", tipoArea),
        new Parametro("codFotografia", codFotografia)
      };
      Variabile variabili[] = new Variabile[]
      { new Variabile("vRiga", vRiga), 
          new Variabile("rigaVO", rigaVO)
      };
      LoggerUtils.logDAOError(this,
          "[StampaGaaDAO::getStampaRiepiloghiStoricoPartTipoArea] ", t, query,
          variabili, parametri);
      throw new DataAccessException(t.getMessage());
    }
    finally
    {
      close(rs, stmt, conn);
      SolmrLogger
          .debug(this, "[StampaGaaDAO::getStampaRiepiloghiStoricoPartTipoArea] END.");
    }
  }
  
  
  public Vector<RiepilogoStampaTerrVO> getStampaRiepiloghiFoglioTipoArea(
      String tipoArea, Long idAzienda, Long codFotografia)  throws DataAccessException
  {
    
    Connection conn = null;
    String query = null;
    PreparedStatement stmt = null;
    ResultSet rs = null;
    StringBuffer queryBuf = null;
    Vector<RiepilogoStampaTerrVO> vRiga = null;
    RiepilogoStampaTerrVO rigaVO = null;
    
    try
    {
      SolmrLogger
      .debug(
          this,
          "[StampaGaaDAO::getStampaRiepiloghiFoglioTipoArea] BEGIN.");

      queryBuf = new StringBuffer();
    
      /* CONCATENAZIONE/CREAZIONE QUERY BEGIN. */
      queryBuf.append("" + 
        "SELECT    'Tipo Area "+tipoArea+" - ' || TIPO.DESCRIZIONE AS DESC_TIPO, " +  
        "       SUM (CP.SUPERFICIE_CONDOTTA) AS SUPERFICIE_CONDOTTA, " +
        "       NVL(SUM(DECODE(CP.ID_TITOLO_POSSESSO,5,CP.SUPERFICIE_CONDOTTA,CP.SUPERFICIE_AGRONOMICA)),0) " + 
        "         AS SUP_AGRONOMICA " +
        "FROM   DB_UTE U, " +
        "       DB_STORICO_PARTICELLA SP, " +   
        "       DB_TIPO_AREA_"+tipoArea+" TIPO, " +
        "       DB_CONDUZIONE_PARTICELLA CP, " +
        "       DB_FOGLIO F " +              
        "WHERE  U.ID_AZIENDA = ? " + 
        "AND    U.DATA_FINE_ATTIVITA IS NULL " +   
        "AND    CP.ID_UTE = U.ID_UTE " +
        "AND    CP.DATA_FINE_CONDUZIONE IS NULL " +    
        "AND    CP.ID_PARTICELLA = SP.ID_PARTICELLA " +   
        "AND    SP.DATA_FINE_VALIDITA IS NULL " +
        "AND    SP.COMUNE = F.COMUNE " +
        "AND    NVL(SP.SEZIONE,'-') = NVL(F.SEZIONE,'-') " + 
        "AND    SP.FOGLIO = F.FOGLIO " +
        "AND    F.ID_AREA_"+tipoArea+" = TIPO.ID_AREA_"+tipoArea+" " + 
        "GROUP BY TIPO.DESCRIZIONE " +
        "ORDER BY TIPO.DESCRIZIONE");
      if(Validator.isNotEmpty(codFotografia))
      {
        queryBuf = new StringBuffer();
        queryBuf.append("" +
          "SELECT 'Tipo Area "+tipoArea+" - ' || TIPO.DESCRIZIONE AS DESC_TIPO, " + 
          "     SUM(CD.SUPERFICIE_CONDOTTA) AS SUPERFICIE_CONDOTTA, " +
          "     NVL(SUM(DECODE(CD.ID_TITOLO_POSSESSO,5,CD.SUPERFICIE_CONDOTTA,CD.SUPERFICIE_AGRONOMICA)),0) " + 
          "      AS SUP_AGRONOMICA " +
          "FROM   DB_CONDUZIONE_DICHIARATA CD, " +
          "       DB_STORICO_PARTICELLA SP, " +
          "       DB_FOGLIO F, " +
          "       DB_TIPO_AREA_"+tipoArea+" TIPO " + 
          "WHERE  CD.CODICE_FOTOGRAFIA_TERRENI = ? " +   
          "AND     CD.ID_STORICO_PARTICELLA = SP.ID_STORICO_PARTICELLA " + 
          "AND    SP.COMUNE = F.COMUNE " +
          "AND    NVL(SP.SEZIONE,'-') = NVL(F.SEZIONE,'-') " + 
          "AND    SP.FOGLIO = F.FOGLIO " +
          "AND    F.ID_AREA_"+tipoArea+" = TIPO.ID_AREA_"+tipoArea+" " + 
          "GROUP BY TIPO.DESCRIZIONE  " +
          "ORDER BY TIPO.DESCRIZIONE");        
      }

      /* CONCATENAZIONE/CREAZIONE QUERY END. */
      
      conn = getDatasource().getConnection();
      query = queryBuf.toString();
      if (SolmrLogger.isDebugEnabled(this))
      {
        // Dato che la query costruita dinamicamente è un dato importante la
        // registro sul file di log se il
        // debug è abilitato
    
        SolmrLogger.debug(this, "[StampaGaaDAO::getStampaRiepiloghiStoricoPartTipoArea] Query="
                    + query);
      }
      stmt = conn.prepareStatement(query);
    
      // Setto i parametri della query
      // Setto i parametri della query
      int indice = 0;
      if(Validator.isNotEmpty(codFotografia))
      {
        stmt.setLong(++indice, codFotografia.longValue());
      }
      else
      {
        stmt.setLong(++indice, idAzienda);
      }

      rs = stmt.executeQuery();

      while (rs.next())
      {
        if(vRiga == null)
        {
          vRiga = new Vector<RiepilogoStampaTerrVO>();
        }
        rigaVO = new RiepilogoStampaTerrVO(); 
        
        rigaVO.setDescrizione(rs.getString("DESC_TIPO"));
        rigaVO.setValore1(rs.getBigDecimal("SUP_AGRONOMICA"));
        rigaVO.setValore2(rs.getBigDecimal("SUPERFICIE_CONDOTTA"));
        
        vRiga.add(rigaVO);
      }
      
      return vRiga;

    }
    catch (Throwable t)
    {
      Parametro parametri[] = new Parametro[]
      { new Parametro("idAzienda", idAzienda),
        new Parametro("tipoArea", tipoArea),
        new Parametro("codFotografia", codFotografia)
      };
      Variabile variabili[] = new Variabile[]
      { new Variabile("vRiga", vRiga), 
          new Variabile("rigaVO", rigaVO)
      };
      LoggerUtils.logDAOError(this,
          "[StampaGaaDAO::getStampaRiepiloghiStoricoPartTipoArea] ", t, query,
          variabili, parametri);
      throw new DataAccessException(t.getMessage());
    }
    finally
    {
      close(rs, stmt, conn);
      SolmrLogger
          .debug(this, "[StampaGaaDAO::getStampaRiepiloghiStoricoPartTipoArea] END.");
    }
  }
  
  
  public Vector<TipoAttestazioneVO> getListAttestazioniDichiarazioniAlPianoAttuale(
      Long idAzienda, String voceMenu, boolean flagCondizionalita) throws DataAccessException 
  {
    
    Connection conn = null;
    String query = null;
    PreparedStatement stmt = null;
    ResultSet rs = null;
    StringBuffer queryBuf = null;
    TipoAttestazioneVO[] result = null;
    Vector<TipoAttestazioneVO> vTipoAttestazione = null;

    
    int idx = 0;
    try
    {
      SolmrLogger
      .debug(
          this,
          "[StampaGaaDAO::getListAttestazioniDichiarazioniAlPianoAttuale] BEGIN.");

      queryBuf = new StringBuffer();
    
      /* CONCATENAZIONE/CREAZIONE QUERY BEGIN. */
      queryBuf.append("" + 
          "SELECT TA.ID_ATTESTAZIONE, " +
          "       TA.CODICE_ATTESTAZIONE, " + 
          "       TA.DESCRIZIONE, " +
          "       TA.DATA_INIZIO_VALIDITA, " +
          "       TA.DATA_FINE_VALIDITA, " +
          "       TA.TIPO_RIGA, " +
          "       TA.NUMERO_COLONNE_RIGA, " + 
          "       TA.TIPO_CARATTERE, " +
          "       TA.GRUPPO, " +
          "       TA.ORDINAMENTO, " + 
          "       TA.ATTESTAZIONI_CORRELATE, " +
          "       TA.ID_ATTESTAZIONE_PADRE, " +
          "       AA.ID_ATTESTAZIONE_AZIENDA, " +
          "       PAA.ID_PARAMETRI_ATT_AZIENDA, " +
          "       PAA.PARAMETRO_1, " +
          "       PAA.PARAMETRO_2, " +
          "       PAA.PARAMETRO_3, " +
          "       PAA.PARAMETRO_4, " +
          "       PAA.PARAMETRO_5 " +
          "FROM   DB_TIPO_ATTESTAZIONE TA, " + 
          "       DB_ATTESTAZIONE_AZIENDA AA, " +
          "       DB_PARAMETRI_ATT_AZIENDA PAA " +
          "WHERE  TA.DATA_INIZIO_VALIDITA <= SYSDATE " + 
          "AND    NVL(TA.DATA_FINE_VALIDITA, TO_DATE('31/12/9999','dd/mm/yyyy')) >= SYSDATE " +
          "AND    TA.FLAG_STAMPA = 'S' " +
          "AND    TA.VOCE_MENU = ? ");
      if(flagCondizionalita)
      {
        queryBuf.append("" +
          "AND    TA.CODICE_ATTESTAZIONE = 'COND' ");
      }
      else
      {
        queryBuf.append("" +
            "AND    TA.CODICE_ATTESTAZIONE <> 'COND' ");
      }
      queryBuf.append("" +
          "AND    TA.ID_ATTESTAZIONE = AA.ID_ATTESTAZIONE(+) " + 
          "AND    AA.ID_AZIENDA (+) = ? " +
          "AND    AA.ID_ATTESTAZIONE_AZIENDA = PAA.ID_ATTESTAZIONE_AZIENDA(+) " +
          "ORDER BY TA.GRUPPO ASC, TA.ORDINAMENTO ASC");

      /* CONCATENAZIONE/CREAZIONE QUERY END. */
      
      conn = getDatasource().getConnection();
      query = queryBuf.toString();
      if (SolmrLogger.isDebugEnabled(this))
      {
        // Dato che la query costruita dinamicamente è un dato importante la
        // registro sul file di log se il
        // debug è abilitato
    
        SolmrLogger
            .debug(
                this,
                "[StampaGaaDAO::getListAttestazioniDichiarazioniAlPianoAttuale] Query="
                    + query);
      }
      stmt = conn.prepareStatement(query);
    
      // Setto i parametri della query
      stmt.setString(++idx, voceMenu);
      stmt.setLong(++idx, idAzienda.longValue());
      
      rs = stmt.executeQuery();
  
      Long idAttestazioneConfronto = null;
      while(rs.next()) 
      {
        if(vTipoAttestazione == null)
          vTipoAttestazione = new Vector<TipoAttestazioneVO>();
        
        TipoAttestazioneVO tipoAttestazioneVO = new TipoAttestazioneVO();
        tipoAttestazioneVO.setIdAttestazione(new Long(rs.getLong("ID_ATTESTAZIONE")));
        tipoAttestazioneVO.setCodiceAttestazione(rs.getString("CODICE_ATTESTAZIONE"));
        tipoAttestazioneVO.setDescrizione(rs.getString("DESCRIZIONE"));
        tipoAttestazioneVO.setDataInizioValidita(rs.getDate("DATA_INIZIO_VALIDITA"));
        tipoAttestazioneVO.setDataFineValidita(rs.getDate("DATA_FINE_VALIDITA"));
        tipoAttestazioneVO.setTipoRiga(rs.getString("TIPO_RIGA"));
        tipoAttestazioneVO.setNumeroColonneRiga(rs.getString("NUMERO_COLONNE_RIGA"));
        tipoAttestazioneVO.setTipoCarattere(rs.getString("TIPO_CARATTERE"));
        tipoAttestazioneVO.setGruppo(rs.getString("GRUPPO"));
        tipoAttestazioneVO.setOrdinamento(rs.getString("ORDINAMENTO"));
        tipoAttestazioneVO.setIdAttestazionePadre(new Long(rs.getLong("ID_ATTESTAZIONE_PADRE")));
        tipoAttestazioneVO.setAttestazioniCorrelate(rs.getString("ATTESTAZIONI_CORRELATE"));
        if(Validator.isNotEmpty(rs.getString("ID_ATTESTAZIONE_AZIENDA"))) 
        {
          if(tipoAttestazioneVO.getAttestazioniCorrelate().equalsIgnoreCase(SolmrConstants.FLAG_S)) 
          {
            idAttestazioneConfronto = tipoAttestazioneVO.getIdAttestazione();
          }
          tipoAttestazioneVO.setAttestazioneAzienda(true);
        }
        else 
        {
          if(idAttestazioneConfronto != null 
              && idAttestazioneConfronto.compareTo(tipoAttestazioneVO.getIdAttestazionePadre()) == 0) 
          {
            tipoAttestazioneVO.setAttestazioneAzienda(true);
          }
        }
        if(Validator.isNotEmpty(rs.getString("ID_PARAMETRI_ATT_AZIENDA"))) 
        {
          ParametriAttAziendaVO parametriAttAziendaVO = new ParametriAttAziendaVO();
          parametriAttAziendaVO.setIdParametriAttAzienda(new Long(rs.getLong("ID_PARAMETRI_ATT_AZIENDA")));
          parametriAttAziendaVO.setParametro1(rs.getString("PARAMETRO_1"));
          parametriAttAziendaVO.setParametro2(rs.getString("PARAMETRO_2"));
          parametriAttAziendaVO.setParametro3(rs.getString("PARAMETRO_3"));
          parametriAttAziendaVO.setParametro4(rs.getString("PARAMETRO_4"));
          parametriAttAziendaVO.setParametro5(rs.getString("PARAMETRO_5"));
          tipoAttestazioneVO.setParametriAttAziendaVO(parametriAttAziendaVO);
        }
        
        vTipoAttestazione.add(tipoAttestazioneVO);
      }

      return vTipoAttestazione;

    }
    catch (Throwable t)
    {
      Parametro parametri[] = new Parametro[]
      { new Parametro("idAzienda", idAzienda),
        new Parametro("voceMenu", voceMenu),
        new Parametro("flagCondizionalita", flagCondizionalita)};
      Variabile variabili[] = new Variabile[]
      { new Variabile("result", result), new Variabile("idx", idx)

      };
      LoggerUtils.logDAOError(this,
          "[StampaGaaDAO::getListAttestazioniDichiarazioniAlPianoAttuale] ", t, query,
          variabili, parametri);
      throw new DataAccessException(t.getMessage());
    }
    finally
    {
      close(rs, stmt, conn);
      SolmrLogger
          .debug(this, "[StampaGaaDAO::getListAttestazioniDichiarazioniAlPianoAttuale] END.");
    }
  }
  
  
  
  public Vector<TipoAttestazioneVO> getListAttestazioniDichiarazioniAllaValidazione(
      Long codiceFotografiaTerreni, Date dataInserimentoDichiarazione, String voceMenu, 
      boolean flagCondizionalita) 
    throws DataAccessException 
  {
    
    Connection conn = null;
    String query = null;
    PreparedStatement stmt = null;
    ResultSet rs = null;
    StringBuffer queryBuf = null;
    TipoAttestazioneVO[] result = null;
    Vector<TipoAttestazioneVO> vTipoAttestazione = null;

    
    int idx = 0;
    try
    {
      SolmrLogger
      .debug(
          this,
          "[StampaGaaDAO::getListAttestazioniDichiarazioniAllaValidazione] BEGIN.");

      queryBuf = new StringBuffer();
    
      /* CONCATENAZIONE/CREAZIONE QUERY BEGIN. */
      queryBuf.append("" + 
          "SELECT TA.ID_ATTESTAZIONE, " +
          "       TA.CODICE_ATTESTAZIONE, " + 
          "       TA.DESCRIZIONE, " +
          "       TA.DATA_INIZIO_VALIDITA, " + 
          "       TA.DATA_FINE_VALIDITA, " +
          "       TA.TIPO_RIGA, " +
          "       TA.NUMERO_COLONNE_RIGA, " +
          "       TA.ID_ATTESTAZIONE_PADRE, " +
          "       TA.TIPO_CARATTERE, " +
          "       TA.GRUPPO, " +
          "       TA.ORDINAMENTO, " + 
          "       TA.ATTESTAZIONI_CORRELATE, " + 
          "       AD.ID_ATTESTAZIONE_DICHIARATA, " + 
          "       PAD.ID_PARAMETRI_ATT_DICHIARATA, " +
          "       PAD.PARAMETRO_1, " +
          "       PAD.PARAMETRO_2, " +
          "       PAD.PARAMETRO_3, " +
          "       PAD.PARAMETRO_4, " +
          "       PAD.PARAMETRO_5 " +
          "FROM   DB_TIPO_ATTESTAZIONE TA, " +
          "       DB_ATTESTAZIONE_DICHIARATA AD, " + 
          "       DB_PARAMETRI_ATT_DICHIARATA PAD " +
          "WHERE  TA.DATA_INIZIO_VALIDITA <= ? " +
          "AND    NVL(TA.DATA_FINE_VALIDITA, TO_DATE('31/12/9999','dd/mm/yyyy')) >= ? " + 
          "AND    TA.FLAG_STAMPA = 'S' " +
          "AND    TA.VOCE_MENU = ? ");
      if(flagCondizionalita)
      {
        queryBuf.append("" +
          "AND    TA.CODICE_ATTESTAZIONE = 'COND' ");
      }
      else
      {
        queryBuf.append("" +
            "AND    TA.CODICE_ATTESTAZIONE <> 'COND' ");
      }
      queryBuf.append("" +
          "AND    TA.ID_ATTESTAZIONE = AD.ID_ATTESTAZIONE(+) " + 
          "AND    AD.CODICE_FOTOGRAFIA_TERRENI  (+) = ? " + 
          "AND    AD.ID_ATTESTAZIONE_DICHIARATA = PAD.ID_ATTESTAZIONE_DICHIARATA (+) " + 
          "ORDER BY TA.GRUPPO ASC, TA.ORDINAMENTO ASC");

      /* CONCATENAZIONE/CREAZIONE QUERY END. */
      
      conn = getDatasource().getConnection();
      query = queryBuf.toString();
      if (SolmrLogger.isDebugEnabled(this))
      {
        // Dato che la query costruita dinamicamente è un dato importante la
        // registro sul file di log se il
        // debug è abilitato
    
        SolmrLogger
            .debug(
                this,
                "[StampaGaaDAO::getListAttestazioniDichiarazioniAllaValidazione] Query="
                    + query);
      }
      stmt = conn.prepareStatement(query);
    
      // Setto i parametri della query
      stmt.setTimestamp(++idx, convertDateToTimestamp(dataInserimentoDichiarazione));
      stmt.setTimestamp(++idx, convertDateToTimestamp(dataInserimentoDichiarazione));
      stmt.setString(++idx, voceMenu);
      stmt.setLong(++idx, codiceFotografiaTerreni.longValue());
      
      rs = stmt.executeQuery();
  
      Long idAttestazioneConfronto = null;
      while(rs.next()) 
      {
        if(vTipoAttestazione == null)
          vTipoAttestazione = new Vector<TipoAttestazioneVO>();
        
        TipoAttestazioneVO tipoAttestazioneVO = new TipoAttestazioneVO();
        tipoAttestazioneVO.setIdAttestazione(new Long(rs.getLong("ID_ATTESTAZIONE")));
        tipoAttestazioneVO.setCodiceAttestazione(rs.getString("CODICE_ATTESTAZIONE"));
        tipoAttestazioneVO.setDescrizione(rs.getString("DESCRIZIONE"));
        tipoAttestazioneVO.setDataInizioValidita(rs.getDate("DATA_INIZIO_VALIDITA"));
        tipoAttestazioneVO.setDataFineValidita(rs.getDate("DATA_FINE_VALIDITA"));
        tipoAttestazioneVO.setTipoRiga(rs.getString("TIPO_RIGA"));
        tipoAttestazioneVO.setNumeroColonneRiga(rs.getString("NUMERO_COLONNE_RIGA"));
        tipoAttestazioneVO.setIdAttestazionePadre(new Long(rs.getLong("ID_ATTESTAZIONE_PADRE")));
        tipoAttestazioneVO.setTipoCarattere(rs.getString("TIPO_CARATTERE"));
        tipoAttestazioneVO.setGruppo(rs.getString("GRUPPO"));
        tipoAttestazioneVO.setOrdinamento(rs.getString("ORDINAMENTO"));
        
        tipoAttestazioneVO.setAttestazioniCorrelate(rs.getString("ATTESTAZIONI_CORRELATE"));
        if(Validator.isNotEmpty(rs.getString("ID_ATTESTAZIONE_DICHIARATA"))) 
        {
          if(tipoAttestazioneVO.getAttestazioniCorrelate().equalsIgnoreCase(SolmrConstants.FLAG_S)) 
          {
            idAttestazioneConfronto = tipoAttestazioneVO.getIdAttestazione();
          }
          tipoAttestazioneVO.setAttestazioneAzienda(true);
        }
        else 
        {
          if(idAttestazioneConfronto != null 
               && idAttestazioneConfronto.compareTo(tipoAttestazioneVO.getIdAttestazionePadre()) == 0) 
          {
            tipoAttestazioneVO.setAttestazioneAzienda(true);
          }
        }
        if(Validator.isNotEmpty(rs.getString("ID_PARAMETRI_ATT_DICHIARATA"))) 
        {
          ParametriAttDichiarataVO parametriAttDichiarataVO = new ParametriAttDichiarataVO();
          parametriAttDichiarataVO.setIdParametriAttDichiarata(new Long(rs.getLong("ID_PARAMETRI_ATT_DICHIARATA")));
          parametriAttDichiarataVO.setParametro1(rs.getString("PARAMETRO_1"));
          parametriAttDichiarataVO.setParametro2(rs.getString("PARAMETRO_2"));
          parametriAttDichiarataVO.setParametro3(rs.getString("PARAMETRO_3"));
          parametriAttDichiarataVO.setParametro4(rs.getString("PARAMETRO_4"));
          parametriAttDichiarataVO.setParametro5(rs.getString("PARAMETRO_5"));
          tipoAttestazioneVO.setParametriAttDichiarataVO(parametriAttDichiarataVO);
        }
        
        vTipoAttestazione.add(tipoAttestazioneVO);
      }

      return vTipoAttestazione;

    }
    catch (Throwable t)
    {
      Parametro parametri[] = new Parametro[]
      { new Parametro("codiceFotografiaTerreni", codiceFotografiaTerreni), 
        new Parametro("dataInserimentoDichiarazione", dataInserimentoDichiarazione),
        new Parametro("voceMenu", voceMenu),
        new Parametro("flagCondizionalita", flagCondizionalita)};
      Variabile variabili[] = new Variabile[]
      { new Variabile("result", result), new Variabile("idx", idx)

      };
      LoggerUtils.logDAOError(this,
          "[StampaGaaDAO::getListAttestazioniDichiarazioniAllaValidazione] ", t, query,
          variabili, parametri);
      throw new DataAccessException(t.getMessage());
    }
    finally
    {
      close(rs, stmt, conn);
      SolmrLogger
          .debug(this, "[StampaGaaDAO::getListAttestazioniDichiarazioniAllaValidazione] END.");
    }
  }
  
  
  public Vector<TipoAttestazioneVO> getElencoAllegatiAllaValidazionePerStampa(
      Long codiceFotografiaTerreni, Date dataInserimentoDichiarazione, String codiceAttestazione) 
    throws DataAccessException 
  {
    
    Connection conn = null;
    String query = null;
    PreparedStatement stmt = null;
    ResultSet rs = null;
    StringBuffer queryBuf = null;
    TipoAttestazioneVO[] result = null;
    Vector<TipoAttestazioneVO> vTipoAttestazione = null;

    
    int idx = 0;
    try
    {
      SolmrLogger
      .debug(
          this,
          "[StampaGaaDAO::getElencoAllegatiAllaValidazionePerStampa] BEGIN.");

      queryBuf = new StringBuffer();
    
      /* CONCATENAZIONE/CREAZIONE QUERY BEGIN. */
      queryBuf.append("" + 
          "SELECT TA.ID_ATTESTAZIONE, " +
          "       TA.CODICE_ATTESTAZIONE, " + 
          "       TA.DESCRIZIONE, " +
          "       TA.DATA_INIZIO_VALIDITA, " + 
          "       TA.DATA_FINE_VALIDITA, " +
          "       TA.TIPO_RIGA, " +
          "       TA.NUMERO_COLONNE_RIGA, " +
          "       TA.ID_ATTESTAZIONE_PADRE, " +
          "       TA.TIPO_CARATTERE, " +
          "       TA.GRUPPO, " +
          "       TA.ORDINAMENTO, " + 
          "       TA.ATTESTAZIONI_CORRELATE, " + 
          "       AD.ID_ATTESTAZIONE_DICHIARATA, " + 
          "       PAD.ID_PARAMETRI_ATT_DICHIARATA, " +
          "       PAD.PARAMETRO_1, " +
          "       PAD.PARAMETRO_2, " +
          "       PAD.PARAMETRO_3, " +
          "       PAD.PARAMETRO_4, " +
          "       PAD.PARAMETRO_5 " +
          "FROM   DB_TIPO_ATTESTAZIONE TA, " +
          "       DB_ATTESTAZIONE_DICHIARATA AD, " + 
          "       DB_PARAMETRI_ATT_DICHIARATA PAD " +
          "WHERE  TA.DATA_INIZIO_VALIDITA <= ? " +
          "AND    NVL(TA.DATA_FINE_VALIDITA, TO_DATE('31/12/9999','dd/mm/yyyy')) >= ? " + 
          "AND    TA.FLAG_STAMPA = 'S' " +
          "AND    TA.CODICE_ATTESTAZIONE = ? " +
          "AND    TA.ID_ATTESTAZIONE = AD.ID_ATTESTAZIONE(+) " + 
          "AND    AD.CODICE_FOTOGRAFIA_TERRENI  (+) = ? " + 
          "AND    AD.ID_ATTESTAZIONE_DICHIARATA = PAD.ID_ATTESTAZIONE_DICHIARATA (+) " + 
          "ORDER BY TA.GRUPPO ASC, TA.ORDINAMENTO ASC");

      /* CONCATENAZIONE/CREAZIONE QUERY END. */
      
      conn = getDatasource().getConnection();
      query = queryBuf.toString();
      if (SolmrLogger.isDebugEnabled(this))
      {
        // Dato che la query costruita dinamicamente è un dato importante la
        // registro sul file di log se il
        // debug è abilitato
    
        SolmrLogger
            .debug(
                this,
                "[StampaGaaDAO::getElencoAllegatiAllaValidazionePerStampa] Query="
                    + query);
      }
      stmt = conn.prepareStatement(query);
    
      // Setto i parametri della query
      stmt.setTimestamp(++idx, convertDateToTimestamp(dataInserimentoDichiarazione));
      stmt.setTimestamp(++idx, convertDateToTimestamp(dataInserimentoDichiarazione));
      stmt.setString(++idx, codiceAttestazione);
      stmt.setLong(++idx, codiceFotografiaTerreni.longValue());
      
      rs = stmt.executeQuery();
  
      Long idAttestazioneConfronto = null;
      while(rs.next()) 
      {
        if(vTipoAttestazione == null)
          vTipoAttestazione = new Vector<TipoAttestazioneVO>();
        
        TipoAttestazioneVO tipoAttestazioneVO = new TipoAttestazioneVO();
        tipoAttestazioneVO.setIdAttestazione(new Long(rs.getLong("ID_ATTESTAZIONE")));
        tipoAttestazioneVO.setCodiceAttestazione(rs.getString("CODICE_ATTESTAZIONE"));
        tipoAttestazioneVO.setDescrizione(rs.getString("DESCRIZIONE"));
        tipoAttestazioneVO.setDataInizioValidita(rs.getDate("DATA_INIZIO_VALIDITA"));
        tipoAttestazioneVO.setDataFineValidita(rs.getDate("DATA_FINE_VALIDITA"));
        tipoAttestazioneVO.setTipoRiga(rs.getString("TIPO_RIGA"));
        tipoAttestazioneVO.setNumeroColonneRiga(rs.getString("NUMERO_COLONNE_RIGA"));
        tipoAttestazioneVO.setIdAttestazionePadre(new Long(rs.getLong("ID_ATTESTAZIONE_PADRE")));
        tipoAttestazioneVO.setTipoCarattere(rs.getString("TIPO_CARATTERE"));
        tipoAttestazioneVO.setGruppo(rs.getString("GRUPPO"));
        tipoAttestazioneVO.setOrdinamento(rs.getString("ORDINAMENTO"));
        
        tipoAttestazioneVO.setAttestazioniCorrelate(rs.getString("ATTESTAZIONI_CORRELATE"));
        if(Validator.isNotEmpty(rs.getString("ID_ATTESTAZIONE_DICHIARATA"))) 
        {
          if(tipoAttestazioneVO.getAttestazioniCorrelate().equalsIgnoreCase(SolmrConstants.FLAG_S)) 
          {
            idAttestazioneConfronto = tipoAttestazioneVO.getIdAttestazione();
          }
          tipoAttestazioneVO.setAttestazioneAzienda(true);
        }
        else 
        {
          if(idAttestazioneConfronto != null 
               && idAttestazioneConfronto.compareTo(tipoAttestazioneVO.getIdAttestazionePadre()) == 0) 
          {
            tipoAttestazioneVO.setAttestazioneAzienda(true);
          }
        }
        if(Validator.isNotEmpty(rs.getString("ID_PARAMETRI_ATT_DICHIARATA"))) 
        {
          ParametriAttDichiarataVO parametriAttDichiarataVO = new ParametriAttDichiarataVO();
          parametriAttDichiarataVO.setIdParametriAttDichiarata(new Long(rs.getLong("ID_PARAMETRI_ATT_DICHIARATA")));
          parametriAttDichiarataVO.setParametro1(rs.getString("PARAMETRO_1"));
          parametriAttDichiarataVO.setParametro2(rs.getString("PARAMETRO_2"));
          parametriAttDichiarataVO.setParametro3(rs.getString("PARAMETRO_3"));
          parametriAttDichiarataVO.setParametro4(rs.getString("PARAMETRO_4"));
          parametriAttDichiarataVO.setParametro5(rs.getString("PARAMETRO_5"));
          tipoAttestazioneVO.setParametriAttDichiarataVO(parametriAttDichiarataVO);
        }
        
        vTipoAttestazione.add(tipoAttestazioneVO);
      }

      return vTipoAttestazione;

    }
    catch (Throwable t)
    {
      Parametro parametri[] = new Parametro[]
      { new Parametro("codiceFotografiaTerreni", codiceFotografiaTerreni), 
        new Parametro("dataInserimentoDichiarazione", dataInserimentoDichiarazione),
        new Parametro("codiceAttestazione", codiceAttestazione)};
      Variabile variabili[] = new Variabile[]
      { new Variabile("result", result), new Variabile("idx", idx)

      };
      LoggerUtils.logDAOError(this,
          "[StampaGaaDAO::getElencoAllegatiAllaValidazionePerStampa] ", t, query,
          variabili, parametri);
      throw new DataAccessException(t.getMessage());
    }
    finally
    {
      close(rs, stmt, conn);
      SolmrLogger
          .debug(this, "[StampaGaaDAO::getElencoAllegatiAllaValidazionePerStampa] END.");
    }
  }
  
  
  public Vector<TipoAttestazioneVO> getAttestazioniFromSubReport(long idReportSubReport, 
      Date dataInserimentoDichiarazione) throws DataAccessException
  {
    Connection conn = null;
    String query = null;
    PreparedStatement stmt = null;
    ResultSet rs = null;
    StringBuffer queryBuf = null;
    Vector<TipoAttestazioneVO> vTipoAttestazioneVO = null;
    TipoAttestazioneVO tipoAttestazioneVO = null;
    
    int idx = 0;
    try
    {
      SolmrLogger
      .debug(
          this,
          "[StampaGaaDAO::getAttestazioniFromSubReport] BEGIN.");

      queryBuf = new StringBuffer();
    
      /* CONCATENAZIONE/CREAZIONE QUERY BEGIN. */
      queryBuf.append("" +
        "SELECT TA.DESCRIZIONE " + 
        "FROM   DB_R_ATTEST_REPORT_SUB_REPORT RSR, " +
        "       DB_TIPO_ATTESTAZIONE TA " +
        "WHERE  RSR.ID_REPORT_SUB_REPORT = ? " +
        "AND    RSR.ID_ATTESTAZIONE = TA.ID_ATTESTAZIONE ");
      if(Validator.isNotEmpty(dataInserimentoDichiarazione))
      {
        queryBuf.append("" +
        "AND    RSR.DATA_INIZIO_VALIDITA <= ? " +
        "AND    NVL(RSR.DATA_FINE_VALIDITA, TO_DATE('31/12/9999','dd/mm/yyyy')) >= ? ");
      }
      else
      {
        queryBuf.append("" +
        "AND    RSR.DATA_FINE_VALIDITA IS NULL ");
      }
      if(Validator.isNotEmpty(dataInserimentoDichiarazione))
      { 
        queryBuf.append("" +
        "AND    TA.DATA_INIZIO_VALIDITA <= ? " +
        "AND    NVL(TA.DATA_FINE_VALIDITA, TO_DATE('31/12/9999','dd/mm/yyyy')) >= ? ");
      }
      else
      {
        queryBuf.append("" +
        "AND     TA.DATA_FINE_VALIDITA IS NULL ");
      }
      queryBuf.append("" +
        "ORDER BY TA.GRUPPO, TA.ORDINAMENTO");
      
      /* CONCATENAZIONE/CREAZIONE QUERY END. */
    
      conn = getDatasource().getConnection();
      query = queryBuf.toString();
      if (SolmrLogger.isDebugEnabled(this))
      {
        // Dato che la query costruita dinamicamente è un dato importante la
        // registro sul file di log se il
        // debug è abilitato
    
        SolmrLogger
            .debug(
                this,
                "[StampaGaaDAO::getAttestazioniFromSubReport] Query="
                    + query);
      }
      stmt = conn.prepareStatement(query);
    
      // Setto i parametri della query
      stmt.setLong(++idx, idReportSubReport);
      if(Validator.isNotEmpty(dataInserimentoDichiarazione))
      { 
        stmt.setTimestamp(++idx, convertDateToTimestamp(dataInserimentoDichiarazione));
        stmt.setTimestamp(++idx, convertDateToTimestamp(dataInserimentoDichiarazione));
        stmt.setTimestamp(++idx, convertDateToTimestamp(dataInserimentoDichiarazione));
        stmt.setTimestamp(++idx, convertDateToTimestamp(dataInserimentoDichiarazione));
      }
     
      rs = stmt.executeQuery();
      
      while (rs.next())
      {
        if(vTipoAttestazioneVO == null)
        {
          vTipoAttestazioneVO = new Vector<TipoAttestazioneVO>();
        }
        
        tipoAttestazioneVO = new TipoAttestazioneVO();        
        tipoAttestazioneVO.setDescrizione(rs.getString("DESCRIZIONE"));
        
        vTipoAttestazioneVO.add(tipoAttestazioneVO);
      }
      
      return vTipoAttestazioneVO;
      
    }
    catch (Throwable t)
    {
      Parametro parametri[] = new Parametro[]
      { new Parametro("idReportSubReport", idReportSubReport),
        new Parametro("dataInserimentoDichiarazione", dataInserimentoDichiarazione) };
      Variabile variabili[] = new Variabile[]
      { new Variabile("tipoAttestazioneVO", tipoAttestazioneVO),
        new Variabile("vTipoAttestazioneVO", vTipoAttestazioneVO) };
      LoggerUtils.logDAOError(this,
          "[StampaGaaDAO::getAttestazioniFromSubReport] ", t, query,
          variabili, parametri);
      throw new DataAccessException(t.getMessage());
    }
    finally
    {
      close(rs, stmt, conn);
      SolmrLogger
          .debug(this, "[StampaGaaDAO::getAttestazioniFromSubReport] END.");
    }
  }
  
  
  public Vector<TipoAttestazioneVO> getAttestazioniFromSubReportValidDettagli(long idReportSubReport, 
      Date dataInserimentoDichiarazione, Long codiceFotografiaTerreni) throws DataAccessException
  {
    Connection conn = null;
    String query = null;
    PreparedStatement stmt = null;
    ResultSet rs = null;
    StringBuffer queryBuf = null;
    Vector<TipoAttestazioneVO> vTipoAttestazione = null;
    TipoAttestazioneVO tipoAttestazioneVO = null;
    
    int idx = 0;
    try
    {
      SolmrLogger
      .debug(
          this,
          "[StampaGaaDAO::getAttestazioniFromSubReportValidDettagli] BEGIN.");

      queryBuf = new StringBuffer();
    
      /* CONCATENAZIONE/CREAZIONE QUERY BEGIN. */
      queryBuf.append("" +
        "SELECT TA.ID_ATTESTAZIONE, " +
        "       TA.CODICE_ATTESTAZIONE, " + 
        "       TA.DESCRIZIONE, " +
        "       TA.DATA_INIZIO_VALIDITA, " + 
        "       TA.DATA_FINE_VALIDITA, " +
        "       TA.TIPO_RIGA, " +
        "       TA.NUMERO_COLONNE_RIGA, " +
        "       TA.ID_ATTESTAZIONE_PADRE, " +
        "       TA.TIPO_CARATTERE, " +
        "       TA.GRUPPO, " +
        "       TA.ORDINAMENTO, " + 
        "       TA.ATTESTAZIONI_CORRELATE, " + 
        "       AD.ID_ATTESTAZIONE_DICHIARATA, " + 
        "       PAD.ID_PARAMETRI_ATT_DICHIARATA, " +
        "       PAD.PARAMETRO_1, " +
        "       PAD.PARAMETRO_2, " +
        "       PAD.PARAMETRO_3, " +
        "       PAD.PARAMETRO_4, " +
        "       PAD.PARAMETRO_5 " +
        "FROM   DB_R_ATTEST_REPORT_SUB_REPORT RSR, " +
        "       DB_TIPO_ATTESTAZIONE TA, " +
        "       DB_ATTESTAZIONE_DICHIARATA AD, " + 
        "       DB_PARAMETRI_ATT_DICHIARATA PAD " +
        "WHERE  RSR.ID_REPORT_SUB_REPORT = ? " +
        "AND    RSR.ID_ATTESTAZIONE = TA.ID_ATTESTAZIONE " +
        "AND    RSR.DATA_INIZIO_VALIDITA <= ? " +
        "AND    NVL(RSR.DATA_FINE_VALIDITA, TO_DATE('31/12/9999','dd/mm/yyyy')) >= ? " +
        "AND    TA.DATA_INIZIO_VALIDITA <= ? " +
        "AND    NVL(TA.DATA_FINE_VALIDITA, TO_DATE('31/12/9999','dd/mm/yyyy')) >= ? " +
        "AND    TA.ID_ATTESTAZIONE = AD.ID_ATTESTAZIONE(+) " + 
        "AND    AD.CODICE_FOTOGRAFIA_TERRENI  (+) = ? " + 
        "AND    AD.DATA_FINE_VALIDITA (+) IS NULL " + 
        "AND    AD.ID_ATTESTAZIONE_DICHIARATA = PAD.ID_ATTESTAZIONE_DICHIARATA (+) " + 
        "ORDER BY TA.GRUPPO ASC, TA.ORDINAMENTO ASC");
      
      /* CONCATENAZIONE/CREAZIONE QUERY END. */
    
      conn = getDatasource().getConnection();
      query = queryBuf.toString();
      if (SolmrLogger.isDebugEnabled(this))
      {
        // Dato che la query costruita dinamicamente è un dato importante la
        // registro sul file di log se il
        // debug è abilitato
    
        SolmrLogger
            .debug(
                this,
                "[StampaGaaDAO::getAttestazioniFromSubReportValidDettagli] Query="
                    + query);
      }
      stmt = conn.prepareStatement(query);
    
      // Setto i parametri della query
      stmt.setLong(++idx, idReportSubReport);
      stmt.setTimestamp(++idx, convertDateToTimestamp(dataInserimentoDichiarazione));
      stmt.setTimestamp(++idx, convertDateToTimestamp(dataInserimentoDichiarazione));
      stmt.setTimestamp(++idx, convertDateToTimestamp(dataInserimentoDichiarazione));
      stmt.setTimestamp(++idx, convertDateToTimestamp(dataInserimentoDichiarazione));
      stmt.setLong(++idx, codiceFotografiaTerreni.longValue());
      
     
      rs = stmt.executeQuery();
      
      Long idAttestazioneConfronto = null;
      while(rs.next()) 
      {
        if(vTipoAttestazione == null)
          vTipoAttestazione = new Vector<TipoAttestazioneVO>();
        
        tipoAttestazioneVO = new TipoAttestazioneVO();
        tipoAttestazioneVO.setIdAttestazione(new Long(rs.getLong("ID_ATTESTAZIONE")));
        tipoAttestazioneVO.setCodiceAttestazione(rs.getString("CODICE_ATTESTAZIONE"));
        tipoAttestazioneVO.setDescrizione(rs.getString("DESCRIZIONE"));
        tipoAttestazioneVO.setDataInizioValidita(rs.getDate("DATA_INIZIO_VALIDITA"));
        tipoAttestazioneVO.setDataFineValidita(rs.getDate("DATA_FINE_VALIDITA"));
        tipoAttestazioneVO.setTipoRiga(rs.getString("TIPO_RIGA"));
        tipoAttestazioneVO.setNumeroColonneRiga(rs.getString("NUMERO_COLONNE_RIGA"));
        tipoAttestazioneVO.setIdAttestazionePadre(new Long(rs.getLong("ID_ATTESTAZIONE_PADRE")));
        tipoAttestazioneVO.setTipoCarattere(rs.getString("TIPO_CARATTERE"));
        tipoAttestazioneVO.setGruppo(rs.getString("GRUPPO"));
        tipoAttestazioneVO.setOrdinamento(rs.getString("ORDINAMENTO"));
        
        tipoAttestazioneVO.setAttestazioniCorrelate(rs.getString("ATTESTAZIONI_CORRELATE"));
        if(Validator.isNotEmpty(rs.getString("ID_ATTESTAZIONE_DICHIARATA"))) 
        {
          if(tipoAttestazioneVO.getAttestazioniCorrelate().equalsIgnoreCase(SolmrConstants.FLAG_S)) 
          {
            idAttestazioneConfronto = tipoAttestazioneVO.getIdAttestazione();
          }
          tipoAttestazioneVO.setAttestazioneAzienda(true);
        }
        else 
        {
          if(idAttestazioneConfronto != null 
               && idAttestazioneConfronto.compareTo(tipoAttestazioneVO.getIdAttestazionePadre()) == 0) 
          {
            tipoAttestazioneVO.setAttestazioneAzienda(true);
          }
        }
        if(Validator.isNotEmpty(rs.getString("ID_PARAMETRI_ATT_DICHIARATA"))) 
        {
          ParametriAttDichiarataVO parametriAttDichiarataVO = new ParametriAttDichiarataVO();
          parametriAttDichiarataVO.setIdParametriAttDichiarata(new Long(rs.getLong("ID_PARAMETRI_ATT_DICHIARATA")));
          parametriAttDichiarataVO.setParametro1(rs.getString("PARAMETRO_1"));
          parametriAttDichiarataVO.setParametro2(rs.getString("PARAMETRO_2"));
          parametriAttDichiarataVO.setParametro3(rs.getString("PARAMETRO_3"));
          parametriAttDichiarataVO.setParametro4(rs.getString("PARAMETRO_4"));
          parametriAttDichiarataVO.setParametro5(rs.getString("PARAMETRO_5"));
          tipoAttestazioneVO.setParametriAttDichiarataVO(parametriAttDichiarataVO);
        }
        
        vTipoAttestazione.add(tipoAttestazioneVO);
      }
      
      return vTipoAttestazione;
      
    }
    catch (Throwable t)
    {
      Parametro parametri[] = new Parametro[]
      { new Parametro("idReportSubReport", idReportSubReport),
        new Parametro("dataInserimentoDichiarazione", dataInserimentoDichiarazione) };
      Variabile variabili[] = new Variabile[]
      { new Variabile("tipoAttestazioneVO", tipoAttestazioneVO),
        new Variabile("vTipoAttestazione", vTipoAttestazione) };
      LoggerUtils.logDAOError(this,
          "[StampaGaaDAO::getAttestazioniFromSubReportValidDettagli] ", t, query,
          variabili, parametri);
      throw new DataAccessException(t.getMessage());
    }
    finally
    {
      close(rs, stmt, conn);
      SolmrLogger
          .debug(this, "[StampaGaaDAO::getAttestazioniFromSubReportValidDettagli] END.");
    }
  }
  
  
  public String getTipoReportByValidazioneEAllegato(
    long idDichiarazioneConsistenza, int idTipoAllegato) 
      throws DataAccessException
  {
    Connection conn = null;
    String query = null;
    PreparedStatement stmt = null;
    ResultSet rs = null;
    StringBuffer queryBuf = null;
    String tipoReport = null;
    
    
    int idx = 0;
    try
    {
      SolmrLogger
      .debug(
          this,
          "[StampaGaaDAO::getTipoReportByValidazioneEAllegato] BEGIN.");

      queryBuf = new StringBuffer();
    
      /* CONCATENAZIONE/CREAZIONE QUERY BEGIN. */
      queryBuf.append("" +
          "SELECT TR.CODICE_REPORT " + 
          "FROM   DB_TIPO_REPORT TR, " +
          "       DB_DICHIARAZIONE_CONSISTENZA DC, " + 
          "       DB_REPORT_MOTIVO_DICHIARAZIONE RMV " +
          "WHERE  DC.ID_DICHIARAZIONE_CONSISTENZA = ? " +
          "AND    DC.ID_MOTIVO_DICHIARAZIONE = RMV.ID_MOTIVO_DICHIARAZIONE " + 
          "AND    RMV.ID_TIPO_REPORT = TR.ID_TIPO_REPORT " +
          "AND    TR.ID_TIPO_ALLEGATO = ? ");
      /* CONCATENAZIONE/CREAZIONE QUERY END. */
    
      conn = getDatasource().getConnection();
      query = queryBuf.toString();
      if (SolmrLogger.isDebugEnabled(this))
      {
        // Dato che la query costruita dinamicamente è un dato importante la
        // registro sul file di log se il
        // debug è abilitato
    
        SolmrLogger
            .debug(
                this,
                "[StampaGaaDAO::getTipoReportByValidazioneEAllegato] Query="
                    + query);
      }
      stmt = conn.prepareStatement(query);
    
      // Setto i parametri della query
      stmt.setLong(++idx, idDichiarazioneConsistenza);
      stmt.setInt(++idx, idTipoAllegato);
      
      
      rs = stmt.executeQuery();
      
      if(rs.next())
      {
        tipoReport = rs.getString("CODICE_REPORT");
      }
      
      return tipoReport;
      
    }
    catch (Throwable t)
    {
      Parametro parametri[] = new Parametro[]
      { new Parametro("idDichiarazioneConsistenza", idDichiarazioneConsistenza),
          new Parametro("idTipoAllegato", idTipoAllegato),};
      Variabile variabili[] = new Variabile[]
      { new Variabile("tipoReport", tipoReport), new Variabile("idx", idx)

      };
      LoggerUtils.logDAOError(this,
          "[StampaGaaDAO::getTipoReportByValidazioneEAllegato] ", t, query,
          variabili, parametri);
      throw new DataAccessException(t.getMessage());
    }
    finally
    {
      close(rs, stmt, conn);
      SolmrLogger
          .debug(this, "[StampaGaaDAO::getTipoReportByValidazioneEAllegato] END.");
    }
  }
  
  public TipoAllegatoVO getTipoAllegatoById(int idTipoAllegato) 
        throws DataAccessException
  {
    Connection conn = null;
    String query = null;
    PreparedStatement stmt = null;
    ResultSet rs = null;
    StringBuffer queryBuf = null;
    TipoAllegatoVO tipoAllegatoVO = null;
    
    
    int idx = 0;
    try
    {
      SolmrLogger
      .debug(
          this,
          "[StampaGaaDAO::getTipoAllegatoById] BEGIN.");

      queryBuf = new StringBuffer();
    
      /* CONCATENAZIONE/CREAZIONE QUERY BEGIN. */
      queryBuf.append("" +
          "SELECT TA.ID_TIPO_ALLEGATO, " +
          "       TA.CODICE_TIPO_ALLEGATO, " +
          "       TA.DESCRIZIONE_TIPO_ALLEGATO, " +
          "       TA.EXT_ID_TIPO_DOCUMENTO_INDEX, " +
          "       TA.FLAG_DA_FIRMARE, " +
          "       TR.CODICE_REPORT, " +
          "       TR.NOME_FILE, " +
          "       TA.CODICE_ATTESTAZIONE, " +
          "       TA.FLAG_INSERIBILE " + 
          "FROM   DB_TIPO_ALLEGATO TA, " +
          "       DB_TIPO_REPORT TR " +
          "WHERE  TA.ID_TIPO_ALLEGATO = ? " +
          "AND    TA.ID_TIPO_ALLEGATO = TR.ID_TIPO_ALLEGATO (+) ");
      /* CONCATENAZIONE/CREAZIONE QUERY END. */
    
      conn = getDatasource().getConnection();
      query = queryBuf.toString();
      if (SolmrLogger.isDebugEnabled(this))
      {
        // Dato che la query costruita dinamicamente è un dato importante la
        // registro sul file di log se il
        // debug è abilitato
    
        SolmrLogger.debug(this,
                "[StampaGaaDAO::getTipoAllegatoById] Query="
                    + query);
      }
      stmt = conn.prepareStatement(query);
    
      // Setto i parametri della query
      stmt.setInt(++idx, idTipoAllegato);
      
      
      rs = stmt.executeQuery();
      
      if(rs.next())
      {
        tipoAllegatoVO = new TipoAllegatoVO();
        tipoAllegatoVO.setIdTipoAllegato(rs.getInt("ID_TIPO_ALLEGATO"));
        tipoAllegatoVO.setCodiceTipoAllegato(rs.getString("CODICE_TIPO_ALLEGATO"));
        tipoAllegatoVO.setDescrizioneTipoAllegato(rs.getString("DESCRIZIONE_TIPO_ALLEGATO"));
        tipoAllegatoVO.setExtIdTipoDocumentoIndex(checkLongNull(rs.getString("EXT_ID_TIPO_DOCUMENTO_INDEX")));
        tipoAllegatoVO.setFlagDaFirmare(rs.getString("FLAG_DA_FIRMARE"));
        tipoAllegatoVO.setCodiceReportAllegato(rs.getString("CODICE_REPORT"));
        tipoAllegatoVO.setNomeFileXDP(rs.getString("NOME_FILE"));
        tipoAllegatoVO.setCodiceAttestazione(rs.getString("CODICE_ATTESTAZIONE"));
        tipoAllegatoVO.setFlagInseribile(rs.getString("FLAG_INSERIBILE"));
        
      }
      
      return tipoAllegatoVO;
      
    }
    catch (Throwable t)
    {
      Parametro parametri[] = new Parametro[]
      { new Parametro("idTipoAllegato", idTipoAllegato),};
      Variabile variabili[] = new Variabile[]
      { new Variabile("tipoAllegatoVO", tipoAllegatoVO), new Variabile("idx", idx)

      };
      LoggerUtils.logDAOError(this,
          "[StampaGaaDAO::getTipoAllegatoById] ", t, query,
          variabili, parametri);
      throw new DataAccessException(t.getMessage());
    }
    finally
    {
      close(rs, stmt, conn);
      SolmrLogger
          .debug(this, "[StampaGaaDAO::getTipoAllegatoById] END.");
    }
  }
  
  /**
   * 
   * Allegati alal validazione facoltativi o cmq generati successivamente ad essa
   * 
   * 
   * @param idMotivoDichiarazione
   * @return
   * @throws DataAccessException
   */
  public Vector<TipoAllegatoVO> getTipoAllegatoForValidazione(int idMotivoDichiarazione, 
      Date dataInserimentoDichiarazione) 
      throws DataAccessException
  {
    Connection conn = null;
    String query = null;
    PreparedStatement stmt = null;
    ResultSet rs = null;
    StringBuffer queryBuf = null;
    Vector<TipoAllegatoVO> vTipoAllegato = null;
    TipoAllegatoVO tipoAllegatoVO = null;
    
    
    try
    {
      SolmrLogger.debug(this, "[StampaGaaDAO::getTipoAllegatoForValidazione] BEGIN.");

      queryBuf = new StringBuffer();
    
      /* CONCATENAZIONE/CREAZIONE QUERY BEGIN. */
      queryBuf.append("" +
          "SELECT TA.ID_TIPO_ALLEGATO, " +
          "       TA.DESCRIZIONE_TIPO_ALLEGATO " +
          "FROM   DB_TIPO_ALLEGATO TA, " +
          "       DB_TP_DICHIARAZ_TP_ALLEGATO TDT " +
          "WHERE  TDT.ID_MOTIVO_DICHIARAZIONE = ? " +
          "AND    TDT.ID_TIPO_ALLEGATO = TA.ID_TIPO_ALLEGATO " +
          "AND    TA.FLAG_INSERIBILE = 'S' " +
          "AND    TDT.DATA_INIZIO_VALIDITA < ? " +
          "AND    NVL(TDT.DATA_FINE_VALIDITA, TO_DATE('31/12/9999','DD/MM/YYYY')) > ? ");
      /* CONCATENAZIONE/CREAZIONE QUERY END. */
    
      conn = getDatasource().getConnection();
      query = queryBuf.toString();
      if (SolmrLogger.isDebugEnabled(this))
      {
        // Dato che la query costruita dinamicamente è un dato importante la
        // registro sul file di log se il
        // debug è abilitato
    
        SolmrLogger.debug(this,
                "[StampaGaaDAO::getTipoAllegatoForValidazione] Query="
                    + query);
      }
      stmt = conn.prepareStatement(query);
    
      // Setto i parametri della query
      int idx = 0;
      stmt.setInt(++idx, idMotivoDichiarazione);
      stmt.setTimestamp(++idx, convertDateToTimestamp(dataInserimentoDichiarazione));
      stmt.setTimestamp(++idx, convertDateToTimestamp(dataInserimentoDichiarazione));
      
      
      rs = stmt.executeQuery();
      
      while(rs.next())
      {
        if(vTipoAllegato == null)
          vTipoAllegato = new Vector<TipoAllegatoVO>();
        
        tipoAllegatoVO = new TipoAllegatoVO();
        tipoAllegatoVO.setIdTipoAllegato(rs.getInt("ID_TIPO_ALLEGATO"));
        tipoAllegatoVO.setDescrizioneTipoAllegato(rs.getString("DESCRIZIONE_TIPO_ALLEGATO"));
        
        vTipoAllegato.add(tipoAllegatoVO);
        
      }
      
      return vTipoAllegato;
      
    }
    catch (Throwable t)
    {
      Parametro parametri[] = new Parametro[]
      { new Parametro("idMotivoDichiarazione", idMotivoDichiarazione)};
      Variabile variabili[] = new Variabile[]
      { new Variabile("tipoAllegatoVO", tipoAllegatoVO), new Variabile("vTipoAllegato", vTipoAllegato)

      };
      LoggerUtils.logDAOError(this,
          "[StampaGaaDAO::getTipoAllegatoForValidazione] ", t, query,
          variabili, parametri);
      throw new DataAccessException(t.getMessage());
    }
    finally
    {
      close(rs, stmt, conn);
      SolmrLogger
          .debug(this, "[StampaGaaDAO::getTipoAllegatoForValidazione] END.");
    }
  }
  
  
    
  
  /**
   * Estare gli allegati obbligatori nella genreazione della validazione
   * 
   * 
   * @param idMotivoDichiarazione
   * @return
   * @throws DataAccessException
   */
  public Vector<TipoAllegatoVO> getTipoAllegatoObbligatorioForValidazione(int idMotivoDichiarazione) 
      throws DataAccessException
  {
    Connection conn = null;
    String query = null;
    PreparedStatement stmt = null;
    ResultSet rs = null;
    StringBuffer queryBuf = null;
    Vector<TipoAllegatoVO> vTipoAllegato = null;
    TipoAllegatoVO tipoAllegatoVO = null;
    
    
    try
    {
      SolmrLogger.debug(this, "[StampaGaaDAO::getTipoAllegatoObbligatorioForValidazione] BEGIN.");

      queryBuf = new StringBuffer();
    
      /* CONCATENAZIONE/CREAZIONE QUERY BEGIN. */
      queryBuf.append("" +
          "SELECT TR.CODICE_REPORT, " +
          "       TA.ID_TIPO_ALLEGATO, " +
          "       TA.DESCRIZIONE_TIPO_ALLEGATO, " +
          "       TR.NOME_FILE " +
          "FROM   DB_REPORT_MOTIVO_DICHIARAZIONE RMD, " +
          "       DB_TIPO_REPORT TR, " +
          "       DB_TIPO_ALLEGATO TA " +
          "WHERE  RMD.ID_MOTIVO_DICHIARAZIONE = ? " +
          "AND    RMD.DATA_INIZIO_VALIDITA <= SYSDATE " +
          "AND    NVL(RMD.DATA_FINE_VALIDITA, TO_DATE('31/12/9999','dd/mm/yyyy')) >= SYSDATE " +
          "AND    RMD.ID_TIPO_REPORT = TR.ID_TIPO_REPORT " +
          "AND    TR.ID_TIPO_ALLEGATO = TA.ID_TIPO_ALLEGATO " +
          "AND    TA.FLAG_INSERIBILE = 'A' ");
      /* CONCATENAZIONE/CREAZIONE QUERY END. */
    
      conn = getDatasource().getConnection();
      query = queryBuf.toString();
      if (SolmrLogger.isDebugEnabled(this))
      {
        // Dato che la query costruita dinamicamente è un dato importante la
        // registro sul file di log se il
        // debug è abilitato
    
        SolmrLogger.debug(this,
                "[StampaGaaDAO::getTipoAllegatoObbligatorioForValidazione] Query="
                    + query);
      }
      stmt = conn.prepareStatement(query);
    
      // Setto i parametri della query
      int idx = 0;
      stmt.setInt(++idx, idMotivoDichiarazione);
      
      
      rs = stmt.executeQuery();
      
      while(rs.next())
      {
        if(vTipoAllegato == null)
          vTipoAllegato = new Vector<TipoAllegatoVO>();
        
        tipoAllegatoVO = new TipoAllegatoVO();
        tipoAllegatoVO.setIdTipoAllegato(rs.getInt("ID_TIPO_ALLEGATO"));
        tipoAllegatoVO.setDescrizioneTipoAllegato(rs.getString("DESCRIZIONE_TIPO_ALLEGATO"));
        tipoAllegatoVO.setCodiceReportAllegato(rs.getString("CODICE_REPORT"));
        tipoAllegatoVO.setNomeFileXDP(rs.getString("NOME_FILE"));
        
        vTipoAllegato.add(tipoAllegatoVO);
        
      }
      
      return vTipoAllegato;
      
    }
    catch (Throwable t)
    {
      Parametro parametri[] = new Parametro[]
      { new Parametro("idMotivoDichiarazione", idMotivoDichiarazione)};
      Variabile variabili[] = new Variabile[]
      { new Variabile("tipoAllegatoVO", tipoAllegatoVO), new Variabile("vTipoAllegato", vTipoAllegato)

      };
      LoggerUtils.logDAOError(this,
          "[StampaGaaDAO::getTipoAllegatoObbligatorioForValidazione] ", t, query,
          variabili, parametri);
      throw new DataAccessException(t.getMessage());
    }
    finally
    {
      close(rs, stmt, conn);
      SolmrLogger
          .debug(this, "[StampaGaaDAO::getTipoAllegatoObbligatorioForValidazione] END.");
    }
  }
  
  
  
  public TipoFirmatarioVO getTipoFirmatarioById(int idTipoFirmatario) 
      throws DataAccessException
  {
    Connection conn = null;
    String query = null;
    PreparedStatement stmt = null;
    ResultSet rs = null;
    StringBuffer queryBuf = null;
  
    TipoFirmatarioVO tipoFirmatarioVO = null;
    
    
    try
    {
      SolmrLogger.debug(this, "[StampaGaaDAO::getTipoFirmatarioById] BEGIN.");

      queryBuf = new StringBuffer();
    
      /* CONCATENAZIONE/CREAZIONE QUERY BEGIN. */
      queryBuf.append("" +
          "SELECT TF.ID_TIPO_FIRMATARIO, " +
          "       TF.CODICE, " +
          "       TF.DESCRIZIONE, " +
          "       TF.FLAG_CAA " +
          "FROM   DB_TIPO_FIRMATARIO TF " +
          "WHERE  TF.ID_TIPO_FIRMATARIO = ? ");
      /* CONCATENAZIONE/CREAZIONE QUERY END. */
    
      conn = getDatasource().getConnection();
      query = queryBuf.toString();
      if (SolmrLogger.isDebugEnabled(this))
      {
        // Dato che la query costruita dinamicamente è un dato importante la
        // registro sul file di log se il
        // debug è abilitato
    
        SolmrLogger.debug(this,
                "[StampaGaaDAO::getTipoFirmatarioById] Query="
                    + query);
      }
      stmt = conn.prepareStatement(query);
    
      // Setto i parametri della query
      int idx = 0;
      stmt.setInt(++idx, idTipoFirmatario);
      
      
      rs = stmt.executeQuery();
      
      if(rs.next())
      {
        tipoFirmatarioVO = new TipoFirmatarioVO();
        tipoFirmatarioVO.setIdTipoFirmatario(rs.getInt("ID_TIPO_FIRMATARIO"));
        tipoFirmatarioVO.setDescrizione(rs.getString("DESCRIZIONE"));
        tipoFirmatarioVO.setCodice(rs.getString("CODICE"));
        tipoFirmatarioVO.setFlagCaa(rs.getString("FLAG_CAA"));
        
      }
      
      return tipoFirmatarioVO;
      
    }
    catch (Throwable t)
    {
      Parametro parametri[] = new Parametro[]
      { new Parametro("idTipoFirmatario", idTipoFirmatario)};
      Variabile variabili[] = new Variabile[]
      { new Variabile("tipoFirmatarioVO", tipoFirmatarioVO)
      };
      LoggerUtils.logDAOError(this,
          "[StampaGaaDAO::getTipoFirmatarioById] ", t, query,
          variabili, parametri);
      throw new DataAccessException(t.getMessage());
    }
    finally
    {
      close(rs, stmt, conn);
      SolmrLogger
          .debug(this, "[StampaGaaDAO::getTipoFirmatarioById] END.");
    }
  }
  
  
  public Vector<RichiestaTipoReportVO> getElencoCoordinateFirma(int idTipoAllegato) 
      throws DataAccessException
  {
    Connection conn = null;
    String query = null;
    PreparedStatement stmt = null;
    ResultSet rs = null;
    StringBuffer queryBuf = null;
    Vector<RichiestaTipoReportVO> results = null;
    int idx = 0;
    try
    {
      SolmrLogger
      .debug(
          this,
          "[StampaGaaDAO::getElencoCoordinateFirma] BEGIN.");

      queryBuf = new StringBuffer();
    
      /* CONCATENAZIONE/CREAZIONE QUERY BEGIN. */
      queryBuf.append("" +
          "SELECT  REP.POS_FIRMA_GRAFO_X_DESTRA, " +
          "        REP.POS_FIRMA_GRAFO_X_SINISTRA, " +
          "        REP.POS_FIRMA_GRAFO_Y_ALTO, " +
          "        REP.POS_FIRMA_GRAFO_Y_BASSO, " +
          "        TF.CODICE AS CODICE_FIRMATARIO " +
          "FROM    DB_R_REPORT_SUB_REPORT REP, " + 
          "        DB_TIPO_REPORT TR, " +
          "        DB_TIPO_FIRMATARIO TF " +
          "WHERE   TR.ID_TIPO_ALLEGATO = ? " +
          "AND     TR.ID_TIPO_REPORT = REP.ID_TIPO_REPORT " +
          "AND     REP.DATA_FINE_VALIDITA IS NULL " +   
          "AND     REP.POS_FIRMA_GRAFO_X_SINISTRA IS NOT NULL " +
          "AND     REP.ID_TIPO_FIRMATARIO = TF.ID_TIPO_FIRMATARIO " +     
          "ORDER BY REP.ORDINAMENTO ASC");
      /* CONCATENAZIONE/CREAZIONE QUERY END. */
    
      conn = getDatasource().getConnection();
      query = queryBuf.toString();
      if (SolmrLogger.isDebugEnabled(this))
      {
        // Dato che la query costruita dinamicamente è un dato importante la
        // registro sul file di log se il
        // debug è abilitato
    
        SolmrLogger.debug(this,
                "[StampaGaaDAO::getElencoCoordinateFirma] Query="
                    + query);
      }
      stmt = conn.prepareStatement(query);
    
      // Setto i parametri della query
      stmt.setInt(++idx, idTipoAllegato);
      
      rs = stmt.executeQuery();
      
            
      while (rs.next())
      {
        if(results == null)
          results = new Vector<RichiestaTipoReportVO>();
        
        RichiestaTipoReportVO richiestaTipoReportVO = new RichiestaTipoReportVO();
        richiestaTipoReportVO.setPosFirmaGrafoXDestra(rs.getBigDecimal("POS_FIRMA_GRAFO_X_DESTRA"));        
        richiestaTipoReportVO.setPosFirmaGrafoXSinistra(rs.getBigDecimal("POS_FIRMA_GRAFO_X_SINISTRA"));
        richiestaTipoReportVO.setPosFirmaGrafoYAlto(rs.getBigDecimal("POS_FIRMA_GRAFO_Y_ALTO"));
        richiestaTipoReportVO.setPosFirmaGrafoYBasso(rs.getBigDecimal("POS_FIRMA_GRAFO_Y_BASSO"));
        richiestaTipoReportVO.setCodiceFirmatario(rs.getString("CODICE_FIRMATARIO"));
        
        results.add(richiestaTipoReportVO);
      }
      
    }
    catch (Throwable t)
    {
      Parametro parametri[] = new Parametro[]
      { new Parametro("idTipoAllegato", idTipoAllegato) };
      Variabile variabili[] = new Variabile[]
      { new Variabile("results", results), new Variabile("idx", idx)

      };
      LoggerUtils.logDAOError(this,
          "[StampaGaaDAO::getElencoCoordinateFirma] ", t, query,
          variabili, parametri);
      throw new DataAccessException(t.getMessage());
    }
    finally
    {
      close(rs, stmt, conn);
      SolmrLogger
          .debug(this, "[StampaGaaDAO::getElencoCoordinateFirma] END.");
    }
    
    return results;
  }
  
  public boolean isInseribileAllegatoAuto(String query, long valore) 
      throws DataAccessException
  {
    Connection conn = null;
    PreparedStatement stmt = null;
    ResultSet rs = null;
    boolean almenoUno = false;
    int idx = 0;
    try
    {
      SolmrLogger.debug(this, "[StampaGaaDAO::isInseribileAllegatoAuto] BEGIN.");
    
      /* CONCATENAZIONE/CREAZIONE QUERY BEGIN. */
      
      /* CONCATENAZIONE/CREAZIONE QUERY END. */
    
      conn = getDatasource().getConnection();
      if (SolmrLogger.isDebugEnabled(this))
      {
        // Dato che la query costruita dinamicamente è un dato importante la
        // registro sul file di log se il
        // debug è abilitato
    
        SolmrLogger.debug(this, "[StampaGaaDAO::isInseribileAllegatoAuto] Query="
                    + query);
      }
      stmt = conn.prepareStatement(query);
    
      // Setto i parametri della query
      stmt.setLong(++idx, valore);
      
      rs = stmt.executeQuery();
      
            
      if(rs.next())
      {
        int numOcc = rs.getInt("NUM_OCC");
        
        if(numOcc > 0)
          almenoUno = true;
      }
      
    }
    catch (Throwable t)
    {
      Parametro parametri[] = new Parametro[]
      { new Parametro("query", query), 
        new Parametro("valore", valore) };
      Variabile variabili[] = new Variabile[]
      { new Variabile("almenoUno", almenoUno), new Variabile("idx", idx)

      };
      LoggerUtils.logDAOError(this,
          "[StampaGaaDAO::isInseribileAllegatoAuto] ", t, query,
          variabili, parametri);
      throw new DataAccessException(t.getMessage());
    }
    finally
    {
      close(rs, stmt, conn);
      SolmrLogger
          .debug(this, "[StampaGaaDAO::isInseribileAllegatoAuto] END.");
    }
    
    return almenoUno;
  }
  
  
  
  
}
