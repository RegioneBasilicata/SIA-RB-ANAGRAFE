package it.csi.solmr.integration.anag;
	
import it.csi.smranag.smrgaa.dto.terreni.TipoDestinazioneVO;
import it.csi.smranag.smrgaa.dto.terreni.TipoDettaglioUsoVO;
import it.csi.smranag.smrgaa.dto.terreni.TipoFaseAllevamentoVO;
import it.csi.smranag.smrgaa.dto.terreni.TipoPeriodoSeminaVO;
import it.csi.smranag.smrgaa.dto.terreni.TipoPraticaMantenimentoVO;
import it.csi.smranag.smrgaa.dto.terreni.TipoQualitaUsoVO;
import it.csi.smranag.smrgaa.dto.terreni.TipoSeminaVO;
import it.csi.smrcomms.reportdin.util.DateUtils;
import it.csi.solmr.dto.anag.terreni.TipoImpiantoVO;
import it.csi.solmr.dto.anag.terreni.TipoMacroUsoVO;
import it.csi.solmr.dto.anag.terreni.TipoUtilizzoVO;
import it.csi.solmr.dto.anag.terreni.TipoVarietaVO;
import it.csi.solmr.dto.anag.terreni.UtilizzoParticellaVO;
import it.csi.solmr.etc.SolmrConstants;
import it.csi.solmr.exception.DataAccessException;
import it.csi.solmr.exception.ResourceAccessException;
import it.csi.solmr.util.SolmrLogger;
import it.csi.solmr.util.StringUtils;
import it.csi.solmr.util.Validator;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Vector;

public class UtilizzoParticellaDAO extends it.csi.solmr.integration.BaseDAO {


	public UtilizzoParticellaDAO() throws ResourceAccessException{
		super(SolmrConstants.JNDI_ANAG_RESOURCE_REFERENCE);
	}

	public UtilizzoParticellaDAO(String refName) throws ResourceAccessException {
		super(refName);
	}
	
	/**
	 * Metodo che mi restituisce l'elenco degli utilizzi a partire dall'id_conduzione_particella
	 * 
	 * @param idConduzioneParticella
	 * @param orderBy
	 * @return it.csi.solmr.dto.anag.terreni.UtilizzoParticellaVO[]
	 * @throws DataAccessException
	 */
	public UtilizzoParticellaVO[] getListUtilizzoParticellaVOByIdConduzioneParticella(Long idConduzioneParticella, String[] orderBy, boolean onlyActive) 
	  throws DataAccessException 
	{
		SolmrLogger.debug(this, "Invocating getListUtilizzoParticellaVOByIdConduzioneParticella method in UtilizzoParticellaDAO\n");
		return getListUtilPartByIdConduzionePartAndIdPartCertificata(idConduzioneParticella,null,orderBy,onlyActive);
	}
	
	
	
	
	public UtilizzoParticellaVO[] getListUtilPartByIdConduzionePartAndIdPartCertificata(
	    Long idConduzioneParticella, Long idParticellaCertificata, String[] orderBy, boolean onlyActive) throws DataAccessException 
	{
    SolmrLogger.debug(this, "Invocating getListUtilizzoParticellaVOByIdConduzioneParticella method in UtilizzoParticellaDAO\n");
    Connection conn = null;
    PreparedStatement stmt = null;
    Vector<UtilizzoParticellaVO> elencoUtilizzoParticellaVO = new Vector<UtilizzoParticellaVO>();

    try 
    {
      SolmrLogger.debug(this, "Creating db-connection in getListUtilPartByIdConduzionePartAndIdPartCertificata method in UtilizzoParticellaDAO\n");
      conn = getDatasource().getConnection();
      SolmrLogger.debug(this, "Created db-connection in getListUtilPartByIdConduzionePartAndIdPartCertificata method in UtilizzoParticellaDAO and it values: "+conn+"\n");

      String query = "" +
        "SELECT UP.ID_UTILIZZO_PARTICELLA, " +
        "       UP.ID_CATALOGO_MATRICE, " +
        "       RCM.DATA_FINE_VALIDITA AS DATA_SCAD_MATRICE, " +
        "       UP.ID_CATALOGO_MATRICE_SECONDARIO, " +
        "       RCM2.DATA_FINE_VALIDITA AS DATA_SCAD_MATRICE_SEC, " +
        "       RCM.ID_UTILIZZO, " +
        "       TU.CODICE, " +
        "       TU.DESCRIZIONE AS DESC_USO_PRIMARIO, " +
        "       UP.ID_CONDUZIONE_PARTICELLA, " +
        "       UP.SUPERFICIE_UTILIZZATA, " +
        "       UP.DATA_AGGIORNAMENTO, " +
        "       UP.ID_UTENTE_AGGIORNAMENTO, " +
        "       UP.ANNO, " +
        "       UP.NOTE, " +
        "       RCM2.ID_UTILIZZO AS ID_UTILIZZO_SECONDARIO, " +
        "       TU2.CODICE AS CODICE_SEC, " +
        "       TU2.DESCRIZIONE AS DESC_USO_SECONDARIO, " +
        "       UP.SUP_UTILIZZATA_SECONDARIA, " +
        "       RCM.ID_VARIETA, " +
        "       TV.DESCRIZIONE AS VAR_PRIMARIA, " +
        "       TV.CODICE_VARIETA AS COD_VAR_PRIMARIA, " +
        "       RCM2.ID_VARIETA AS ID_VARIETA_SECONDARIA, " +
        "       TV2.DESCRIZIONE AS VAR_SECONDARIA, " +
        "       TV2.CODICE_VARIETA AS COD_VAR_SECONDARIA, " +
        "       UP.ANNO_IMPIANTO, " +
        "       UP.ID_IMPIANTO, " +
        "       TI.DESCRIZIONE AS DESC_IMPIANTO, " +
        "       TI.DATA_INIZIO_VALIDITA, " +
        "       TI.DATA_FINE_VALIDITA, " +
        "       UP.SESTO_SU_FILE, " +
        "       UP.SESTO_TRA_FILE, " +
        "       UP.NUMERO_PIANTE_CEPPI, " +
        "       RCM.ID_TIPO_DETTAGLIO_USO, " +
        "       TDU.CODICE_DETTAGLIO_USO AS COD_DETT_USO_PRIM, " +
        "       TDU.DESCRIZIONE_DETTAGLIO_USO AS DESC_DETT_USO_PRIM, " +
        "       RCM2.ID_TIPO_DETTAGLIO_USO AS ID_TIPO_DETT_USO_SECONDARIO, " +
        "       TDU2.CODICE_DETTAGLIO_USO AS COD_DETT_USO_SEC, " +
        "       TDU2.DESCRIZIONE_DETTAGLIO_USO AS DESC_DETT_USO_SEC, " +
        "       RCM.ID_TIPO_DESTINAZIONE AS ID_TIPO_DEST_PRIM, " +
        "       TTD.CODICE_DESTINAZIONE AS COD_DEST_USO_PRIM, " +
        "       TTD.DESCRIZIONE_DESTINAZIONE AS DESC_DEST_USO_PRIM, " +
        "       RCM2.ID_TIPO_DESTINAZIONE AS ID_TIPO_DEST_SEC, " +
        "       TTD2.CODICE_DESTINAZIONE AS COD_DEST_USO_SEC, " +
        "       TTD2.DESCRIZIONE_DESTINAZIONE AS DESC_DEST_USO_SEC, " +
        "       RCM.ID_TIPO_QUALITA_USO AS ID_TIPO_QUALITA_USO_PRIM, " +
        "       TQU.CODICE_QUALITA_USO AS COD_QUALITA_USO_PRIM, " +
        "       TQU.DESCRIZIONE_QUALITA_USO AS DESC_QUALITA_USO_PRIM, " +
        "       RCM2.ID_TIPO_QUALITA_USO AS ID_TIPO_QUALITA_USO_SEC, " +
        "       TQU2.CODICE_QUALITA_USO AS COD_QUALITA_USO_SEC, " +
        "       TQU2.DESCRIZIONE_QUALITA_USO AS DESC_QUALITA_USO_SEC, " +
        "       UP.ID_TIPO_EFA, " +
        "       UP.VALORE_ORIGINALE, " +
        "       UP.VALORE_DOPO_CONVERSIONE, " +
        "       UP.VALORE_DOPO_PONDERAZIONE, " +
        "       UP.ID_TIPO_PERIODO_SEMINA, " +
        "       TPS.CODICE AS COD_PER_SEM_PRIM, " +
        "       TPS.DESCRIZIONE AS DESC_PER_SEM_PRIM, " +
        "       UP.ID_TIPO_PERIODO_SEMINA_SECOND, " +
        "       TPS2.CODICE AS COD_PER_SEM_SEC, " +
        "       TPS2.DESCRIZIONE AS DESC_PER_SEM_SEC, " +
        "       TEF.DICHIARABILE, " +
        "       TEF.DESCRIZIONE_TIPO_EFA, " +
        "       UM.DESCRIZIONE AS DESC_UNITA_MISURA, " +
        "       UP.ID_SEMINA, " +
        "       TSM.CODICE_SEMINA AS COD_SEM_PRIM, " +
        "       TSM.DESCRIZIONE_SEMINA AS DESC_SEM_PRIM, " +
        "       UP.ID_SEMINA_SECONDARIA, " +
        "       TSM2.CODICE_SEMINA AS COD_SEM_SEC, " +
        "       TSM2.DESCRIZIONE_SEMINA AS DESC_SEM_SEC, " +
        "       UP.DATA_INIZIO_DESTINAZIONE, " +
        "       UP.DATA_FINE_DESTINAZIONE, " +
        "       UP.ID_FASE_ALLEVAMENTO, " +
        "       TFA.CODICE_FASE_ALLEVAMENTO, " +
        "       TFA.DESCRIZIONE_FASE_ALLEVAMENTO, " +
        "       UP.ID_PRATICA_MANTENIMENTO, " +
        "       TPM.CODICE_PRATICA_MANTENIMENTO, " +
        "       TPM.DESCRIZIONE_PRATICA_MANTENIMEN, " +
        "       UP.DATA_INIZIO_DESTINAZIONE_SEC, " +
        "       UP.DATA_FINE_DESTINAZIONE_SEC, " +
        "       RCM.COEFFICIENTE_RIDUZIONE, " +
        "       (SELECT TETV.ABBATTIMENTO_PONDERAZIONE " +
        "        FROM   DB_TIPO_EFA_TIPO_VARIETA TETV " +
        "        WHERE  TETV.ID_TIPO_EFA = UP.ID_TIPO_EFA " +
        "        AND    TETV.ID_CATALOGO_MATRICE = UP.ID_CATALOGO_MATRICE " +
        "        AND    TETV.DATA_FINE_VALIDITA IS NULL) AS ABB_PONDERAZIONE ";
     if(Validator.isNotEmpty(idParticellaCertificata))
     {
       query +=     
        "       ,NVL(PCK_SMRGAA_LIBRERIA.SelTotSupElegByPartEVetCat(?,RCM.ID_CATALOGO_MATRICE),0) AS SUP_ELEGGIBILE, " +
        "       NVL(PCK_SMRGAA_LIBRERIA.SelTotSupElegByPartEVetVarNTar(?,RCM.ID_CATALOGO_MATRICE),0) AS SUP_ELEGGIBILE_NETTA ";
     }
     query +=
        "FROM   DB_UTILIZZO_PARTICELLA UP, " +
        "       DB_R_CATALOGO_MATRICE RCM, " +
        "       DB_R_CATALOGO_MATRICE RCM2, " +
        "       DB_TIPO_UTILIZZO TU, " +
        "       DB_TIPO_UTILIZZO TU2, " +
        "       DB_TIPO_VARIETA TV, " +
        "       DB_TIPO_VARIETA TV2, " +
        "       DB_TIPO_IMPIANTO TI, " +
        "       DB_CONDUZIONE_PARTICELLA CP, " +
        "       DB_TIPO_DESTINAZIONE TTD, " +
        "       DB_TIPO_DESTINAZIONE TTD2, " +
        "       DB_TIPO_DETTAGLIO_USO TDU, " +
        "       DB_TIPO_DETTAGLIO_USO TDU2, " +
        "       DB_TIPO_QUALITA_USO TQU, " +
        "       DB_TIPO_QUALITA_USO TQU2, " +
        "       DB_TIPO_PERIODO_SEMINA TPS, " +
        "       DB_TIPO_PERIODO_SEMINA TPS2, " +
        "       DB_TIPO_EFA TEF, " +
        "       DB_UNITA_MISURA UM, " +
        "       DB_TIPO_PRATICA_MANTENIMENTO TPM, " +
        "       DB_TIPO_FASE_ALLEVAMENTO TFA, " +
        "       DB_TIPO_SEMINA TSM, " +
        "       DB_TIPO_SEMINA TSM2 " +
        "WHERE  UP.ID_CONDUZIONE_PARTICELLA = ? " +
        "AND    UP.ID_CONDUZIONE_PARTICELLA = CP.ID_CONDUZIONE_PARTICELLA " +
        "AND    UP.ID_CATALOGO_MATRICE = RCM.ID_CATALOGO_MATRICE (+) " +
        "AND    UP.ID_CATALOGO_MATRICE_SECONDARIO = RCM2.ID_CATALOGO_MATRICE (+) " +
        "AND    RCM.ID_UTILIZZO = TU.ID_UTILIZZO " +    
        "AND    RCM2.ID_UTILIZZO = TU2.ID_UTILIZZO(+) " +
        "AND    RCM.ID_VARIETA = TV.ID_VARIETA(+) " +
        "AND    RCM.ID_VARIETA = TV2.ID_VARIETA(+) " +
        "AND    RCM.ID_TIPO_DESTINAZIONE = TTD.ID_TIPO_DESTINAZIONE(+) " +
        "AND    RCM2.ID_TIPO_DESTINAZIONE = TTD2.ID_TIPO_DESTINAZIONE(+) " +
        "AND    RCM.ID_TIPO_DETTAGLIO_USO = TDU.ID_TIPO_DETTAGLIO_USO(+) " +
        "AND    RCM2.ID_TIPO_DETTAGLIO_USO = TDU2.ID_TIPO_DETTAGLIO_USO(+) " +
        "AND    RCM.ID_TIPO_QUALITA_USO = TQU.ID_TIPO_QUALITA_USO(+) " +
        "AND    RCM2.ID_TIPO_QUALITA_USO = TQU2.ID_TIPO_QUALITA_USO(+) " +
        "AND    UP.ID_TIPO_PERIODO_SEMINA = TPS.ID_TIPO_PERIODO_SEMINA(+) " +
        "AND    UP.ID_TIPO_PERIODO_SEMINA_SECOND = TPS2.ID_TIPO_PERIODO_SEMINA(+) " +
        "AND    UP.ID_TIPO_EFA = TEF.ID_TIPO_EFA(+) " +
        "AND    UP.ID_IMPIANTO = TI.ID_IMPIANTO(+) " +
        "AND    TEF.ID_UNITA_MISURA = UM.ID_UNITA_MISURA (+) " +
        "AND    UP.ID_PRATICA_MANTENIMENTO = TPM.ID_PRATICA_MANTENIMENTO (+) " +
        "AND    UP.ID_FASE_ALLEVAMENTO = TFA.ID_FASE_ALLEVAMENTO (+) " +
        "AND    UP.ID_SEMINA = TSM.ID_SEMINA (+) " +
        "AND    UP.ID_SEMINA_SECONDARIA = TSM2.ID_SEMINA (+) ";
     
      if(onlyActive) 
      {
        query += " AND CP.DATA_FINE_CONDUZIONE IS NULL ";
      }
    
      query += " ORDER BY TU.CODICE, " +
                 "          TU.DESCRIZIONE ";

      SolmrLogger.debug(this, "Value of parameter 1 [ID_CONDUZIONE_PARTICELLA] in getListUtilPartByIdConduzionePartAndIdPartCertificata method in UtilizzoParticellaDAO: "+idConduzioneParticella+"\n");
      SolmrLogger.debug(this, "Value of parameter 2 [ORDER_BY] in getListUtilPartByIdConduzionePartAndIdPartCertificata method in UtilizzoParticellaDAO: "+orderBy+"\n");

      stmt = conn.prepareStatement(query);
      
      
      int indice = 0;
      if(Validator.isNotEmpty(idParticellaCertificata))
      {
        stmt.setLong(++indice, idParticellaCertificata.longValue());
        stmt.setLong(++indice, idParticellaCertificata.longValue());
      }
      
      stmt.setLong(++indice, idConduzioneParticella.longValue());
      

      SolmrLogger.debug(this, "Executing getListUtilPartByIdConduzionePartAndIdPartCertificata: "+query+"\n");

      ResultSet rs = stmt.executeQuery();

      while(rs.next()) 
      {
        UtilizzoParticellaVO utilizzoParticellaVO = new UtilizzoParticellaVO();
        utilizzoParticellaVO.setIdUtilizzoParticella(new Long(rs.getLong("ID_UTILIZZO_PARTICELLA")));
        
        utilizzoParticellaVO.setIdCatalogoMatrice(checkLongNull(rs.getString("ID_CATALOGO_MATRICE")));
        if(rs.getTimestamp("DATA_SCAD_MATRICE") != null)
          utilizzoParticellaVO.setFlagMatriceAttiva("N");
        else
          utilizzoParticellaVO.setFlagMatriceAttiva("S");
        
        utilizzoParticellaVO.setIdCatalogoMatriceSecondario(checkLongNull(rs.getString("ID_CATALOGO_MATRICE_SECONDARIO")));
        if(rs.getTimestamp("DATA_SCAD_MATRICE_SEC") != null)
          utilizzoParticellaVO.setFlagMatriceAttivaSecondario("N");
        else
          utilizzoParticellaVO.setFlagMatriceAttivaSecondario("S");
        
        utilizzoParticellaVO.setIdUtilizzo(new Long(rs.getLong("ID_UTILIZZO")));
        TipoUtilizzoVO tipoUtilizzoVO = new TipoUtilizzoVO();
        tipoUtilizzoVO.setIdUtilizzo(new Long(rs.getLong("ID_UTILIZZO")));
        tipoUtilizzoVO.setCodice(rs.getString("CODICE"));
        tipoUtilizzoVO.setDescrizione(rs.getString("DESC_USO_PRIMARIO"));
        utilizzoParticellaVO.setTipoUtilizzoVO(tipoUtilizzoVO);
        utilizzoParticellaVO.setIdConduzioneParticella(new Long(rs.getLong("ID_CONDUZIONE_PARTICELLA")));
        utilizzoParticellaVO.setSuperficieUtilizzata(rs.getString("SUPERFICIE_UTILIZZATA"));
        utilizzoParticellaVO.setDataAggiornamento(rs.getDate("DATA_AGGIORNAMENTO"));
        utilizzoParticellaVO.setIdUtenteAggiornamento(new Long(rs.getLong("ID_UTENTE_AGGIORNAMENTO")));
        utilizzoParticellaVO.setAnno(rs.getString("ANNO"));
        utilizzoParticellaVO.setNote(rs.getString("NOTE"));
        if(Validator.isNotEmpty(rs.getString("ID_UTILIZZO_SECONDARIO"))) {
          utilizzoParticellaVO.setIdUtilizzoSecondario(new Long(rs.getLong("ID_UTILIZZO_SECONDARIO")));
          TipoUtilizzoVO tipoUtilizzoSecondarioVO = new TipoUtilizzoVO();
          tipoUtilizzoSecondarioVO.setIdUtilizzo(new Long(rs.getLong("ID_UTILIZZO_SECONDARIO")));
          tipoUtilizzoSecondarioVO.setCodice(rs.getString("CODICE_SEC"));
          tipoUtilizzoSecondarioVO.setDescrizione(rs.getString("DESC_USO_SECONDARIO"));
          utilizzoParticellaVO.setTipoUtilizzoSecondarioVO(tipoUtilizzoSecondarioVO);
        }
        utilizzoParticellaVO.setSupUtilizzataSecondaria(rs.getString("SUP_UTILIZZATA_SECONDARIA"));
        if(Validator.isNotEmpty(rs.getString("ID_VARIETA"))) {
          utilizzoParticellaVO.setIdVarieta(new Long(rs.getLong("ID_VARIETA")));
          TipoVarietaVO tipoVarietaVO = new TipoVarietaVO();
          tipoVarietaVO.setIdVarieta(new Long(rs.getLong("ID_VARIETA")));
          tipoVarietaVO.setDescrizione(rs.getString("VAR_PRIMARIA"));
          tipoVarietaVO.setCodiceVarieta(rs.getString("COD_VAR_PRIMARIA"));
          utilizzoParticellaVO.setTipoVarietaVO(tipoVarietaVO);
        }
        if(Validator.isNotEmpty(rs.getString("ID_VARIETA_SECONDARIA"))) {
          utilizzoParticellaVO.setIdVarietaSecondaria(new Long(rs.getLong("ID_VARIETA_SECONDARIA")));
          TipoVarietaVO tipoVarietaSecondariaVO = new TipoVarietaVO();
          tipoVarietaSecondariaVO.setIdVarieta(new Long(rs.getLong("ID_VARIETA_SECONDARIA")));
          tipoVarietaSecondariaVO.setDescrizione(rs.getString("VAR_SECONDARIA"));
          tipoVarietaSecondariaVO.setCodiceVarieta(rs.getString("COD_VAR_SECONDARIA"));
          utilizzoParticellaVO.setTipoVarietaSecondariaVO(tipoVarietaSecondariaVO);
        }
        if(Validator.isNotEmpty(rs.getString("ID_TIPO_DEST_PRIM"))) {
          utilizzoParticellaVO.setIdTipoDestinazione(new Long(rs.getLong("ID_TIPO_DEST_PRIM")));
          TipoDestinazioneVO tipoDestinazioneVO = new TipoDestinazioneVO();
          tipoDestinazioneVO.setIdTipoDestinazione(new Long(rs.getLong("ID_TIPO_DEST_PRIM")));
          tipoDestinazioneVO.setDescrizioneDestinazione(rs.getString("DESC_DEST_USO_PRIM"));
          tipoDestinazioneVO.setCodiceDestinazione(rs.getString("COD_DEST_USO_PRIM"));
          utilizzoParticellaVO.setTipoDestinazione(tipoDestinazioneVO);
        }
        if(Validator.isNotEmpty(rs.getString("ID_TIPO_DEST_SEC"))) {
          utilizzoParticellaVO.setIdTipoDestinazioneSecondario(new Long(rs.getLong("ID_TIPO_DEST_SEC")));
          TipoDestinazioneVO tipoDestinazioneVO = new TipoDestinazioneVO();
          tipoDestinazioneVO.setIdTipoDestinazione(new Long(rs.getLong("ID_TIPO_DEST_SEC")));
          tipoDestinazioneVO.setDescrizioneDestinazione(rs.getString("DESC_DEST_USO_SEC"));
          tipoDestinazioneVO.setCodiceDestinazione(rs.getString("COD_DEST_USO_SEC"));
          utilizzoParticellaVO.setTipoDestinazioneSecondario(tipoDestinazioneVO);
        }
        if(Validator.isNotEmpty(rs.getString("ID_TIPO_DETTAGLIO_USO"))) {
          utilizzoParticellaVO.setIdTipoDettaglioUso(new Long(rs.getLong("ID_TIPO_DETTAGLIO_USO")));
          TipoDettaglioUsoVO tipoDettaglioUsoVO = new TipoDettaglioUsoVO();
          tipoDettaglioUsoVO.setIdTipoDettaglioUso(new Long(rs.getLong("ID_TIPO_DETTAGLIO_USO")));
          tipoDettaglioUsoVO.setDescrizione(rs.getString("DESC_DETT_USO_PRIM"));
          tipoDettaglioUsoVO.setCodiceDettaglioUso(rs.getString("COD_DETT_USO_PRIM"));
          utilizzoParticellaVO.setTipoDettaglioUso(tipoDettaglioUsoVO);
        }
        if(Validator.isNotEmpty(rs.getString("ID_TIPO_DETT_USO_SECONDARIO"))) {
          utilizzoParticellaVO.setIdTipoDettaglioUsoSecondario(new Long(rs.getLong("ID_TIPO_DETT_USO_SECONDARIO")));
          TipoDettaglioUsoVO tipoDettaglioUsoVO = new TipoDettaglioUsoVO();
          tipoDettaglioUsoVO.setIdTipoDettaglioUso(new Long(rs.getLong("ID_TIPO_DETT_USO_SECONDARIO")));
          tipoDettaglioUsoVO.setDescrizione(rs.getString("DESC_DETT_USO_SEC"));
          tipoDettaglioUsoVO.setCodiceDettaglioUso(rs.getString("COD_DETT_USO_SEC"));
          utilizzoParticellaVO.setTipoDettaglioUsoSecondario(tipoDettaglioUsoVO);
        }
        if(Validator.isNotEmpty(rs.getString("ID_TIPO_QUALITA_USO_PRIM"))) {
          utilizzoParticellaVO.setIdTipoQualitaUso(new Long(rs.getLong("ID_TIPO_QUALITA_USO_PRIM")));
          TipoQualitaUsoVO tipoQualitaUsoVO = new TipoQualitaUsoVO();
          tipoQualitaUsoVO.setIdTipoQualitaUso(new Long(rs.getLong("ID_TIPO_QUALITA_USO_PRIM")));
          tipoQualitaUsoVO.setDescrizioneQualitaUso(rs.getString("DESC_QUALITA_USO_PRIM"));
          tipoQualitaUsoVO.setCodiceQualitaUso(rs.getString("COD_QUALITA_USO_PRIM"));
          utilizzoParticellaVO.setTipoQualitaUso(tipoQualitaUsoVO);
        }
        if(Validator.isNotEmpty(rs.getString("ID_TIPO_QUALITA_USO_SEC"))) {
          utilizzoParticellaVO.setIdTipoQualitaUsoSecondario(new Long(rs.getLong("ID_TIPO_QUALITA_USO_SEC")));
          TipoQualitaUsoVO tipoQualitaUsoVO = new TipoQualitaUsoVO();
          tipoQualitaUsoVO.setIdTipoQualitaUso(new Long(rs.getLong("ID_TIPO_QUALITA_USO_SEC")));
          tipoQualitaUsoVO.setDescrizioneQualitaUso(rs.getString("DESC_QUALITA_USO_SEC"));
          tipoQualitaUsoVO.setCodiceQualitaUso(rs.getString("COD_QUALITA_USO_SEC"));
          utilizzoParticellaVO.setTipoQualitaUsoSecondario(tipoQualitaUsoVO);
        }
        if(Validator.isNotEmpty(rs.getString("ID_TIPO_PERIODO_SEMINA"))) {
          utilizzoParticellaVO.setIdTipoPeriodoSemina(new Long(rs.getLong("ID_TIPO_PERIODO_SEMINA")));
          TipoPeriodoSeminaVO tipoPeriodoSeminaVO = new TipoPeriodoSeminaVO();
          tipoPeriodoSeminaVO.setIdTipoPeriodoSemina(new Long(rs.getLong("ID_TIPO_PERIODO_SEMINA")));
          tipoPeriodoSeminaVO.setDescrizione(rs.getString("DESC_PER_SEM_PRIM"));
          tipoPeriodoSeminaVO.setCodice(rs.getString("COD_PER_SEM_PRIM"));
          utilizzoParticellaVO.setTipoPeriodoSemina(tipoPeriodoSeminaVO);
        }
        if(Validator.isNotEmpty(rs.getString("ID_TIPO_PERIODO_SEMINA_SECOND"))) {
          utilizzoParticellaVO.setIdTipoPeriodoSeminaSecondario(new Long(rs.getLong("ID_TIPO_PERIODO_SEMINA_SECOND")));
          TipoPeriodoSeminaVO tipoPeriodoSeminaVO = new TipoPeriodoSeminaVO();
          tipoPeriodoSeminaVO.setIdTipoPeriodoSemina(new Long(rs.getLong("ID_TIPO_PERIODO_SEMINA_SECOND")));
          tipoPeriodoSeminaVO.setDescrizione(rs.getString("DESC_PER_SEM_SEC"));
          tipoPeriodoSeminaVO.setCodice(rs.getString("COD_PER_SEM_SEC"));
          utilizzoParticellaVO.setTipoPeriodoSeminaSecondario(tipoPeriodoSeminaVO);
        }
        utilizzoParticellaVO.setIdTipoEfa(checkLongNull(rs.getString("ID_TIPO_EFA")));
        utilizzoParticellaVO.setValoreOriginale(rs.getBigDecimal("VALORE_ORIGINALE"));
        utilizzoParticellaVO.setValoreDopoConversione(rs.getBigDecimal("VALORE_DOPO_CONVERSIONE"));
        utilizzoParticellaVO.setValoreDopoPonderazione(rs.getBigDecimal("VALORE_DOPO_PONDERAZIONE"));
        utilizzoParticellaVO.setDichiarabileEfa(rs.getString("DICHIARABILE"));
        utilizzoParticellaVO.setDescTipoEfaEfa(rs.getString("DESCRIZIONE_TIPO_EFA"));
        utilizzoParticellaVO.setDescUnitaMisuraEfa(rs.getString("DESC_UNITA_MISURA"));
        
        utilizzoParticellaVO.setAnnoImpianto(rs.getString("ANNO_IMPIANTO"));
        if(Validator.isNotEmpty(rs.getString("ID_IMPIANTO"))) {
          utilizzoParticellaVO.setIdImpianto(new Long(rs.getLong("ID_IMPIANTO")));
          TipoImpiantoVO tipoImpiantoVO = new TipoImpiantoVO();
          tipoImpiantoVO.setIdImpianto(new Long(rs.getLong("ID_IMPIANTO")));
          tipoImpiantoVO.setDescrizione(rs.getString("DESC_IMPIANTO"));
          tipoImpiantoVO.setDataInizioValidita(rs.getDate("DATA_INIZIO_VALIDITA"));
          tipoImpiantoVO.setDataFineValidita(rs.getDate("DATA_FINE_VALIDITA"));
          utilizzoParticellaVO.setTipoImpiantoVO(tipoImpiantoVO);
        }
        utilizzoParticellaVO.setSestoSuFile(rs.getString("SESTO_SU_FILE"));
        utilizzoParticellaVO.setSestoTraFile(rs.getString("SESTO_TRA_FILE"));
        utilizzoParticellaVO.setNumeroPianteCeppi(rs.getString("NUMERO_PIANTE_CEPPI"));
        
        utilizzoParticellaVO.setDataInizioDestinazione(rs.getTimestamp("DATA_INIZIO_DESTINAZIONE"));
        utilizzoParticellaVO.setDataFineDestinazione(rs.getTimestamp("DATA_FINE_DESTINAZIONE"));
        
        if(Validator.isNotEmpty(utilizzoParticellaVO.getDataInizioDestinazione()))
          utilizzoParticellaVO.setDataInizioDestinazioneStr(DateUtils.formatDate(utilizzoParticellaVO.getDataInizioDestinazione()));
        if(Validator.isNotEmpty(utilizzoParticellaVO.getDataFineDestinazione()))
          utilizzoParticellaVO.setDataFineDestinazioneStr(DateUtils.formatDate(utilizzoParticellaVO.getDataFineDestinazione()));
        
        
        if(Validator.isNotEmpty(rs.getString("ID_FASE_ALLEVAMENTO"))) {
          utilizzoParticellaVO.setIdFaseAllevamento(new Long(rs.getLong("ID_FASE_ALLEVAMENTO")));
          TipoFaseAllevamentoVO tipoFaseAllevamentoVO = new TipoFaseAllevamentoVO();
          tipoFaseAllevamentoVO.setIdFaseAllevamento(new Long(rs.getLong("ID_FASE_ALLEVAMENTO")));
          tipoFaseAllevamentoVO.setDescrizioneFaseAllevamento(rs.getString("DESCRIZIONE_FASE_ALLEVAMENTO"));
          tipoFaseAllevamentoVO.setCodiceFaseAllevamento(rs.getString("CODICE_FASE_ALLEVAMENTO"));
          utilizzoParticellaVO.setTipoFaseAllevamento(tipoFaseAllevamentoVO);
        }
        
        if(Validator.isNotEmpty(rs.getString("ID_PRATICA_MANTENIMENTO"))) {
          utilizzoParticellaVO.setIdPraticaMantenimento(new Long(rs.getLong("ID_PRATICA_MANTENIMENTO")));
          TipoPraticaMantenimentoVO tipoPraticaMantenimentoVO = new TipoPraticaMantenimentoVO();
          tipoPraticaMantenimentoVO.setIdPraticaMantenimento(new Long(rs.getLong("ID_PRATICA_MANTENIMENTO")));
          tipoPraticaMantenimentoVO.setDescrizionePraticaMantenim(rs.getString("DESCRIZIONE_PRATICA_MANTENIMEN"));
          tipoPraticaMantenimentoVO.setCodicePraticaMantenimento(rs.getString("CODICE_PRATICA_MANTENIMENTO"));
          utilizzoParticellaVO.setTipoPraticaMantenimento(tipoPraticaMantenimentoVO);
        }
        
        if(Validator.isNotEmpty(rs.getString("ID_SEMINA"))) {
          utilizzoParticellaVO.setIdSemina(new Long(rs.getLong("ID_SEMINA")));
          TipoSeminaVO tipoSeminaVO = new TipoSeminaVO();
          tipoSeminaVO.setIdTipoSemina(new Long(rs.getLong("ID_SEMINA")));
          tipoSeminaVO.setDescrizioneSemina(rs.getString("DESC_SEM_PRIM"));
          tipoSeminaVO.setCodiceSemina(rs.getString("COD_SEM_PRIM"));
          utilizzoParticellaVO.setTipoSemina(tipoSeminaVO);
        }
        
        if(Validator.isNotEmpty(rs.getString("ID_SEMINA_SECONDARIA"))) {
          utilizzoParticellaVO.setIdSeminaSecondario(new Long(rs.getLong("ID_SEMINA_SECONDARIA")));
          TipoSeminaVO tipoSeminaVO = new TipoSeminaVO();
          tipoSeminaVO.setIdTipoSemina(new Long(rs.getLong("ID_SEMINA_SECONDARIA")));
          tipoSeminaVO.setDescrizioneSemina(rs.getString("DESC_SEM_SEC"));
          tipoSeminaVO.setCodiceSemina(rs.getString("COD_SEM_SEC"));
          utilizzoParticellaVO.setTipoSeminaSecondario(tipoSeminaVO);
        }
        
        utilizzoParticellaVO.setDataInizioDestinazioneSec(rs.getTimestamp("DATA_INIZIO_DESTINAZIONE_SEC"));
        utilizzoParticellaVO.setDataFineDestinazioneSec(rs.getTimestamp("DATA_FINE_DESTINAZIONE_SEC"));
        
        if(Validator.isNotEmpty(utilizzoParticellaVO.getDataInizioDestinazioneSec()))
          utilizzoParticellaVO.setDataInizioDestinazioneSecStr(DateUtils.formatDate(utilizzoParticellaVO.getDataInizioDestinazioneSec()));
        if(Validator.isNotEmpty(utilizzoParticellaVO.getDataFineDestinazioneSec()))
          utilizzoParticellaVO.setDataFineDestinazioneSecStr(DateUtils.formatDate(utilizzoParticellaVO.getDataFineDestinazioneSec()));
        
        
        if(Validator.isNotEmpty(idParticellaCertificata))
        {
          utilizzoParticellaVO.setSupEleggibile(rs.getBigDecimal("SUP_ELEGGIBILE"));
          utilizzoParticellaVO.setSupEleggibileNetta(rs.getBigDecimal("SUP_ELEGGIBILE_NETTA"));
        }
        
        utilizzoParticellaVO.setCoefficienteRiduzione(rs.getBigDecimal("COEFFICIENTE_RIDUZIONE"));
        utilizzoParticellaVO.setAbbaPonderazione(checkIntegerNull(rs.getString("ABB_PONDERAZIONE")));
        
        
        elencoUtilizzoParticellaVO.add(utilizzoParticellaVO);
      }
      
      rs.close();
      stmt.close();

    }
    catch(SQLException exc) {
      SolmrLogger.error(this, "getListUtilPartByIdConduzionePartAndIdPartCertificata in UtilizzoParticellaDAO - SQLException: "+exc.getMessage()+"\n");
      throw new DataAccessException(exc.getMessage());
    }
    catch(Exception ex) {
      SolmrLogger.error(this, "getListUtilPartByIdConduzionePartAndIdPartCertificata in UtilizzoParticellaDAO - Generic Exception: "+ex+"\n");
      throw new DataAccessException(ex.getMessage());
    }
    finally {
      try {
        if(stmt != null) stmt.close();
        if(conn != null) conn.close();
      }
      catch(SQLException exc) {
        SolmrLogger.error(this, "getListUtilPartByIdConduzionePartAndIdPartCertificata in UtilizzoParticellaDAO - SQLException while closing Statement and Connection: "+exc.getMessage()+"\n");
        throw new DataAccessException(exc.getMessage());
      }
      catch(Exception ex) {
        SolmrLogger.error(this, "getListUtilPartByIdConduzionePartAndIdPartCertificata in UtilizzoParticellaDAO - Generic Exception while closing Statement and Connection: "+ex.getMessage()+"\n");
        throw new DataAccessException(ex.getMessage());
      }
    }
    SolmrLogger.debug(this, "Invocated getListUtilPartByIdConduzionePartAndIdPartCertificata method in UtilizzoParticellaDAO\n");
    return elencoUtilizzoParticellaVO.size() == 0 ? null :(UtilizzoParticellaVO[])elencoUtilizzoParticellaVO.toArray(new UtilizzoParticellaVO[0]);
  }
	
	/**
	 * Metodo che mi restituisce l'utilizzo_particella a partire dalla sua chiave primaria
	 * 
	 * @param idUtilizzoParticella
	 * @return it.csi.solmr.dto.anag.terreni.UtilizzoParticellaVO
	 * @throws DataAccessException
	 */
	public UtilizzoParticellaVO findUtilizzoParticellaByPrimaryKey(Long idUtilizzoParticella) throws DataAccessException {
		SolmrLogger.debug(this, "Invocating findUtilizzoParticellaByPrimaryKey method in UtilizzoParticellaDAO\n");
		Connection conn = null;
		PreparedStatement stmt = null;
		UtilizzoParticellaVO utilizzoParticellaVO = null;

		try {
			SolmrLogger.debug(this, "Creating db-connection in findUtilizzoParticellaByPrimaryKey method in UtilizzoParticellaDAO\n");
			conn = getDatasource().getConnection();
			SolmrLogger.debug(this, "Created db-connection in findUtilizzoParticellaByPrimaryKey method in UtilizzoParticellaDAO and it values: "+conn+"\n");

			String query = "" +
				"SELECT UP.ID_UTILIZZO_PARTICELLA, " +
				"       UP.ID_CATALOGO_MATRICE, " +
				"       UP.ID_CATALOGO_MATRICE_SECONDARIO, " +
				"       RCM.ID_UTILIZZO, " +
				"       TU.CODICE, " +
		    "       TU.DESCRIZIONE AS DESC_USO_PRIMARIO, " +
		    "       TU.COEFFICIENTE_RIDUZIONE, " +
		    "       UP.ID_CONDUZIONE_PARTICELLA, " +
		    "       UP.SUPERFICIE_UTILIZZATA, " +
		    "       UP.DATA_AGGIORNAMENTO, " +
		    "       UP.ID_UTENTE_AGGIORNAMENTO, " +
		    "		    UP.ANNO, " +
		    "		    UP.NOTE, " +
		    "       RCM2.ID_UTILIZZO AS ID_UTILIZZO_SECONDARIO, " +
		    "       TU2.CODICE AS CODICE_SEC, " +
		    "       TU2.DESCRIZIONE AS DESC_USO_SECONDARIO, " +
		    "       UP.SUP_UTILIZZATA_SECONDARIA, " +
		    "		    RCM.ID_VARIETA, " +
		    "		    TV.DESCRIZIONE AS VAR_PRIMARIA, " +
		    "       TV.CODICE_VARIETA AS COD_VAR_PRIMARIA, " +
		    "		    RCM2.ID_VARIETA AS ID_VARIETA_SECONDARIA, " +
		    "		    TV2.DESCRIZIONE AS VAR_SECONDARIA, " +
		    "       TV2.CODICE_VARIETA AS COD_VAR_SECONDARIA, " +
		    "		    UP.ANNO_IMPIANTO, " +
		    "       UP.ID_IMPIANTO, " +
		    "       TI.DESCRIZIONE AS DESC_IMPIANTO, " +
		    "       TI.DATA_INIZIO_VALIDITA, " +
		    "       TI.DATA_FINE_VALIDITA, " +
		    "		    UP.SESTO_SU_FILE, " +
		    "		    UP.SESTO_TRA_FILE, " +
		    "		    UP.NUMERO_PIANTE_CEPPI, " +
		    "       RCM.ID_TIPO_DETTAGLIO_USO, " +
        "       TDU.CODICE_DETTAGLIO_USO AS COD_DETT_USO_PRIM, " +
        "       TDU.DESCRIZIONE_DETTAGLIO_USO AS DESC_DETT_USO_PRIM, " +
        "       RCM2.ID_TIPO_DETTAGLIO_USO AS ID_TIPO_DETT_USO_SECONDARIO, " +
        "       TDU2.CODICE_DETTAGLIO_USO AS COD_DETT_USO_SEC, " +
        "       TDU2.DESCRIZIONE_DETTAGLIO_USO AS DESC_DETT_USO_SEC, " +
		    "       RCM.ID_TIPO_DESTINAZIONE AS ID_TIPO_DEST_PRIM, " +
		    "       TTD.CODICE_DESTINAZIONE AS COD_DEST_USO_PRIM, " +
		    "       TTD.DESCRIZIONE_DESTINAZIONE AS DESC_DEST_USO_PRIM, " +
		    "       RCM2.ID_TIPO_DESTINAZIONE AS ID_TIPO_DEST_SEC, " +
		    "       TTD2.CODICE_DESTINAZIONE AS COD_DEST_USO_SEC, " +
        "       TTD2.DESCRIZIONE_DESTINAZIONE AS DESC_DEST_USO_SEC, " +
        "       RCM.ID_TIPO_QUALITA_USO AS ID_TIPO_QUALITA_USO_PRIM, " +
        "       TQU.CODICE_QUALITA_USO AS COD_QUALITA_USO_PRIM, " +
        "       TQU.DESCRIZIONE_QUALITA_USO AS DESC_QUALITA_USO_PRIM, " +
        "       RCM2.ID_TIPO_QUALITA_USO AS ID_TIPO_QUALITA_USO_SEC, " +
        "       TQU2.CODICE_QUALITA_USO AS COD_QUALITA_USO_SEC, " +
        "       TQU2.DESCRIZIONE_QUALITA_USO AS DESC_QUALITA_USO_SEC, " +
        "       UP.ID_TIPO_EFA, " +
        "       UP.VALORE_ORIGINALE, " +
        "       UP.VALORE_DOPO_CONVERSIONE, " +
        "       UP.VALORE_DOPO_PONDERAZIONE, " +
        "       UP.ID_TIPO_PERIODO_SEMINA, " +
        "       TPS.CODICE AS COD_PER_SEM_PRIM, " +
        "       TPS.DESCRIZIONE AS DESC_PER_SEM_PRIM, " +
        "       UP.ID_TIPO_PERIODO_SEMINA_SECOND, " +
        "       TPS2.CODICE AS COD_PER_SEM_SEC, " +
        "       TPS2.DESCRIZIONE AS DESC_PER_SEM_SEC, " +
        "       TEF.DICHIARABILE, " +
        "       UP.ID_SEMINA," +
        "       UP.ID_SEMINA_SECONDARIA," +
        "       UP.DATA_INIZIO_DESTINAZIONE, " +
        "       UP.DATA_FINE_DESTINAZIONE, " +
        "       UP.ID_FASE_ALLEVAMENTO, " +
        "       UP.ID_PRATICA_MANTENIMENTO, " +
        "       UP.DATA_INIZIO_DESTINAZIONE_SEC, " +
        "       UP.DATA_FINE_DESTINAZIONE_SEC " +
		    "FROM   DB_UTILIZZO_PARTICELLA UP, " +
		    "       DB_R_CATALOGO_MATRICE RCM, " +
		    "       DB_R_CATALOGO_MATRICE RCM2, " +
		    " 		  DB_TIPO_UTILIZZO TU, " +
		    "		    DB_TIPO_UTILIZZO TU2, " +
		    "		    DB_TIPO_VARIETA TV, " +
		    "		    DB_TIPO_VARIETA TV2, " +
		    "       DB_TIPO_IMPIANTO TI, " +
		    "       DB_TIPO_DESTINAZIONE TTD, " +
        "       DB_TIPO_DESTINAZIONE TTD2, " +
		    "       DB_TIPO_DETTAGLIO_USO TDU, " +
		    "       DB_TIPO_DETTAGLIO_USO TDU2, " +
		    "       DB_TIPO_QUALITA_USO TQU, " +
        "       DB_TIPO_QUALITA_USO TQU2, " +
		    "       DB_TIPO_PERIODO_SEMINA TPS, " +
        "       DB_TIPO_PERIODO_SEMINA TPS2, " +
        "       DB_TIPO_EFA TEF " +
		    "WHERE  UP.ID_UTILIZZO_PARTICELLA = ? " +
		    "AND    UP.ID_CATALOGO_MATRICE = RCM.ID_CATALOGO_MATRICE (+) " +
		    "AND    UP.ID_CATALOGO_MATRICE_SECONDARIO = RCM2.ID_CATALOGO_MATRICE (+) " +
		    "AND    RCM.ID_UTILIZZO = TU.ID_UTILIZZO (+) " +    
		    "AND    RCM2.ID_UTILIZZO = TU2.ID_UTILIZZO(+) " +
		    "AND    RCM.ID_VARIETA = TV.ID_VARIETA(+) " +
		    "AND    RCM2.ID_VARIETA = TV2.ID_VARIETA(+) " +
		    "AND    RCM.ID_TIPO_DESTINAZIONE = TTD.ID_TIPO_DESTINAZIONE(+) " +
        "AND    RCM2.ID_TIPO_DESTINAZIONE = TTD2.ID_TIPO_DESTINAZIONE(+) " +
        "AND    RCM.ID_TIPO_DETTAGLIO_USO = TDU.ID_TIPO_DETTAGLIO_USO(+) " +
        "AND    RCM2.ID_TIPO_DETTAGLIO_USO = TDU2.ID_TIPO_DETTAGLIO_USO(+) " +
        "AND    RCM.ID_TIPO_QUALITA_USO = TQU.ID_TIPO_QUALITA_USO(+) " +
        "AND    RCM2.ID_TIPO_QUALITA_USO = TQU2.ID_TIPO_QUALITA_USO(+) " +
		    "AND    UP.ID_IMPIANTO = TI.ID_IMPIANTO(+) " +
  			"AND    UP.ID_TIPO_PERIODO_SEMINA = TPS.ID_TIPO_PERIODO_SEMINA(+) " +
        "AND    UP.ID_TIPO_PERIODO_SEMINA_SECOND = TPS2.ID_TIPO_PERIODO_SEMINA(+) " +
        "AND    UP.ID_TIPO_EFA = TEF.ID_TIPO_EFA(+) ";

			SolmrLogger.debug(this, "Value of parameter 1 [ID_UTILIZZO_PARTICELLA] in findUtilizzoParticellaByPrimaryKey method in UtilizzoParticellaDAO: "+idUtilizzoParticella+"\n");

			stmt = conn.prepareStatement(query);
			
			stmt.setLong(1, idUtilizzoParticella.longValue());

			SolmrLogger.debug(this, "Executing findUtilizzoParticellaByPrimaryKey: "+query+"\n");

			ResultSet rs = stmt.executeQuery();

			if(rs.next()) {
				utilizzoParticellaVO = new UtilizzoParticellaVO();
				utilizzoParticellaVO.setIdUtilizzoParticella(new Long(rs.getLong("ID_UTILIZZO_PARTICELLA")));
				utilizzoParticellaVO.setIdCatalogoMatrice(checkLongNull(rs.getString("ID_CATALOGO_MATRICE")));
				utilizzoParticellaVO.setIdCatalogoMatriceSecondario(checkLongNull(rs.getString("ID_CATALOGO_MATRICE_SECONDARIO")));
				utilizzoParticellaVO.setIdUtilizzo(new Long(rs.getLong("ID_UTILIZZO")));
				TipoUtilizzoVO tipoUtilizzoVO = new TipoUtilizzoVO();
				tipoUtilizzoVO.setIdUtilizzo(new Long(rs.getLong("ID_UTILIZZO")));
				tipoUtilizzoVO.setCodice(rs.getString("CODICE"));
				tipoUtilizzoVO.setDescrizione(rs.getString("DESC_USO_PRIMARIO"));
				tipoUtilizzoVO.setCoefficienteRiduzione(rs.getBigDecimal("COEFFICIENTE_RIDUZIONE"));
				utilizzoParticellaVO.setTipoUtilizzoVO(tipoUtilizzoVO);
				utilizzoParticellaVO.setIdConduzioneParticella(new Long(rs.getLong("ID_CONDUZIONE_PARTICELLA")));
				utilizzoParticellaVO.setSuperficieUtilizzata(rs.getString("SUPERFICIE_UTILIZZATA"));
				utilizzoParticellaVO.setDataAggiornamento(rs.getDate("DATA_AGGIORNAMENTO"));
				utilizzoParticellaVO.setIdUtenteAggiornamento(new Long(rs.getLong("ID_UTENTE_AGGIORNAMENTO")));
				utilizzoParticellaVO.setAnno(rs.getString("ANNO"));
				utilizzoParticellaVO.setNote(rs.getString("NOTE"));
				if(Validator.isNotEmpty(rs.getString("ID_UTILIZZO_SECONDARIO"))) {
					utilizzoParticellaVO.setIdUtilizzoSecondario(new Long(rs.getLong("ID_UTILIZZO_SECONDARIO")));
					TipoUtilizzoVO tipoUtilizzoSecondarioVO = new TipoUtilizzoVO();
					tipoUtilizzoSecondarioVO.setIdUtilizzo(new Long(rs.getLong("ID_UTILIZZO_SECONDARIO")));
					tipoUtilizzoSecondarioVO.setCodice(rs.getString("CODICE_SEC"));
					tipoUtilizzoSecondarioVO.setDescrizione(rs.getString("DESC_USO_SECONDARIO"));
					utilizzoParticellaVO.setTipoUtilizzoSecondarioVO(tipoUtilizzoSecondarioVO);
				}
				utilizzoParticellaVO.setSupUtilizzataSecondaria(rs.getString("SUP_UTILIZZATA_SECONDARIA"));
				if(Validator.isNotEmpty(rs.getString("ID_VARIETA"))) {
					utilizzoParticellaVO.setIdVarieta(new Long(rs.getLong("ID_VARIETA")));
					TipoVarietaVO tipoVarietaVO = new TipoVarietaVO();
					tipoVarietaVO.setIdVarieta(new Long(rs.getLong("ID_VARIETA")));
					tipoVarietaVO.setDescrizione(rs.getString("VAR_PRIMARIA"));
					tipoVarietaVO.setCodiceVarieta(rs.getString("COD_VAR_PRIMARIA"));
					utilizzoParticellaVO.setTipoVarietaVO(tipoVarietaVO);
				}
				if(Validator.isNotEmpty(rs.getString("ID_VARIETA_SECONDARIA"))) {
					utilizzoParticellaVO.setIdVarietaSecondaria(new Long(rs.getLong("ID_VARIETA_SECONDARIA")));
					TipoVarietaVO tipoVarietaSecondariaVO = new TipoVarietaVO();
					tipoVarietaSecondariaVO.setIdVarieta(new Long(rs.getLong("ID_VARIETA_SECONDARIA")));
					tipoVarietaSecondariaVO.setDescrizione(rs.getString("VAR_SECONDARIA"));
					tipoVarietaSecondariaVO.setCodiceVarieta(rs.getString("COD_VAR_SECONDARIA"));
					utilizzoParticellaVO.setTipoVarietaSecondariaVO(tipoVarietaSecondariaVO);
				}
				if(Validator.isNotEmpty(rs.getString("ID_TIPO_DEST_PRIM"))) {
          utilizzoParticellaVO.setIdTipoDestinazione(new Long(rs.getLong("ID_TIPO_DEST_PRIM")));
          TipoDestinazioneVO tipoDestinazioneVO = new TipoDestinazioneVO();
          tipoDestinazioneVO.setIdTipoDestinazione(new Long(rs.getLong("ID_TIPO_DEST_PRIM")));
          tipoDestinazioneVO.setDescrizioneDestinazione(rs.getString("DESC_DEST_USO_PRIM"));
          tipoDestinazioneVO.setCodiceDestinazione(rs.getString("COD_DEST_USO_PRIM"));
          utilizzoParticellaVO.setTipoDestinazione(tipoDestinazioneVO);
        }
        if(Validator.isNotEmpty(rs.getString("ID_TIPO_DEST_SEC"))) {
          utilizzoParticellaVO.setIdTipoDestinazioneSecondario(new Long(rs.getLong("ID_TIPO_DEST_SEC")));
          TipoDestinazioneVO tipoDestinazioneVO = new TipoDestinazioneVO();
          tipoDestinazioneVO.setIdTipoDestinazione(new Long(rs.getLong("ID_TIPO_DEST_SEC")));
          tipoDestinazioneVO.setDescrizioneDestinazione(rs.getString("DESC_DEST_USO_SEC"));
          tipoDestinazioneVO.setCodiceDestinazione(rs.getString("COD_DEST_USO_SEC"));
          utilizzoParticellaVO.setTipoDestinazioneSecondario(tipoDestinazioneVO);
        }
				if(Validator.isNotEmpty(rs.getString("ID_TIPO_DETTAGLIO_USO"))) {
          utilizzoParticellaVO.setIdTipoDettaglioUso(new Long(rs.getLong("ID_TIPO_DETTAGLIO_USO")));
          TipoDettaglioUsoVO tipoDettaglioUsoVO = new TipoDettaglioUsoVO();
          tipoDettaglioUsoVO.setIdTipoDettaglioUso(new Long(rs.getLong("ID_TIPO_DETTAGLIO_USO")));
          tipoDettaglioUsoVO.setDescrizione(rs.getString("DESC_DETT_USO_PRIM"));
          tipoDettaglioUsoVO.setCodiceDettaglioUso(rs.getString("COD_DETT_USO_PRIM"));
          utilizzoParticellaVO.setTipoDettaglioUso(tipoDettaglioUsoVO);
        }
				if(Validator.isNotEmpty(rs.getString("ID_TIPO_DETT_USO_SECONDARIO"))) {
          utilizzoParticellaVO.setIdTipoDettaglioUsoSecondario(new Long(rs.getLong("ID_TIPO_DETT_USO_SECONDARIO")));
          TipoDettaglioUsoVO tipoDettaglioUsoVO = new TipoDettaglioUsoVO();
          tipoDettaglioUsoVO.setIdTipoDettaglioUso(new Long(rs.getLong("ID_TIPO_DETT_USO_SECONDARIO")));
          tipoDettaglioUsoVO.setDescrizione(rs.getString("DESC_DETT_USO_SEC"));
          tipoDettaglioUsoVO.setCodiceDettaglioUso(rs.getString("COD_DETT_USO_SEC"));
          utilizzoParticellaVO.setTipoDettaglioUsoSecondario(tipoDettaglioUsoVO);
        }
				if(Validator.isNotEmpty(rs.getString("ID_TIPO_QUALITA_USO_PRIM"))) {
          utilizzoParticellaVO.setIdTipoQualitaUso(new Long(rs.getLong("ID_TIPO_QUALITA_USO_PRIM")));
          TipoQualitaUsoVO tipoQualitaUsoVO = new TipoQualitaUsoVO();
          tipoQualitaUsoVO.setIdTipoQualitaUso(new Long(rs.getLong("ID_TIPO_QUALITA_USO_PRIM")));
          tipoQualitaUsoVO.setDescrizioneQualitaUso(rs.getString("DESC_QUALITA_USO_PRIM"));
          tipoQualitaUsoVO.setCodiceQualitaUso(rs.getString("COD_QUALITA_USO_PRIM"));
          utilizzoParticellaVO.setTipoQualitaUso(tipoQualitaUsoVO);
        }
        if(Validator.isNotEmpty(rs.getString("ID_TIPO_QUALITA_USO_SEC"))) {
          utilizzoParticellaVO.setIdTipoQualitaUsoSecondario(new Long(rs.getLong("ID_TIPO_QUALITA_USO_SEC")));
          TipoQualitaUsoVO tipoQualitaUsoVO = new TipoQualitaUsoVO();
          tipoQualitaUsoVO.setIdTipoQualitaUso(new Long(rs.getLong("ID_TIPO_QUALITA_USO_SEC")));
          tipoQualitaUsoVO.setDescrizioneQualitaUso(rs.getString("DESC_QUALITA_USO_SEC"));
          tipoQualitaUsoVO.setCodiceQualitaUso(rs.getString("COD_QUALITA_USO_SEC"));
          utilizzoParticellaVO.setTipoQualitaUsoSecondario(tipoQualitaUsoVO);
        }
				if(Validator.isNotEmpty(rs.getString("ID_TIPO_PERIODO_SEMINA"))) {
          utilizzoParticellaVO.setIdTipoPeriodoSemina(new Long(rs.getLong("ID_TIPO_PERIODO_SEMINA")));
          TipoPeriodoSeminaVO tipoPeriodoSeminaVO = new TipoPeriodoSeminaVO();
          tipoPeriodoSeminaVO.setIdTipoPeriodoSemina(new Long(rs.getLong("ID_TIPO_PERIODO_SEMINA")));
          tipoPeriodoSeminaVO.setDescrizione(rs.getString("DESC_PER_SEM_PRIM"));
          tipoPeriodoSeminaVO.setCodice(rs.getString("COD_PER_SEM_PRIM"));
          utilizzoParticellaVO.setTipoPeriodoSemina(tipoPeriodoSeminaVO);
        }
        if(Validator.isNotEmpty(rs.getString("ID_TIPO_PERIODO_SEMINA_SECOND"))) {
          utilizzoParticellaVO.setIdTipoPeriodoSeminaSecondario(new Long(rs.getLong("ID_TIPO_PERIODO_SEMINA_SECOND")));
          TipoPeriodoSeminaVO tipoPeriodoSeminaVO = new TipoPeriodoSeminaVO();
          tipoPeriodoSeminaVO.setIdTipoPeriodoSemina(new Long(rs.getLong("ID_TIPO_PERIODO_SEMINA_SECOND")));
          tipoPeriodoSeminaVO.setDescrizione(rs.getString("DESC_PER_SEM_SEC"));
          tipoPeriodoSeminaVO.setCodice(rs.getString("COD_PER_SEM_SEC"));
          utilizzoParticellaVO.setTipoPeriodoSeminaSecondario(tipoPeriodoSeminaVO);
        }
				utilizzoParticellaVO.setIdTipoEfa(checkLongNull(rs.getString("ID_TIPO_EFA")));
        utilizzoParticellaVO.setValoreOriginale(rs.getBigDecimal("VALORE_ORIGINALE"));
        utilizzoParticellaVO.setValoreDopoConversione(rs.getBigDecimal("VALORE_DOPO_CONVERSIONE"));
        utilizzoParticellaVO.setValoreDopoPonderazione(rs.getBigDecimal("VALORE_DOPO_PONDERAZIONE"));
        utilizzoParticellaVO.setDichiarabileEfa(rs.getString("DICHIARABILE"));
				
				utilizzoParticellaVO.setAnnoImpianto(rs.getString("ANNO_IMPIANTO"));
				if(Validator.isNotEmpty(rs.getString("ID_IMPIANTO"))) {
					utilizzoParticellaVO.setIdImpianto(new Long(rs.getLong("ID_IMPIANTO")));
					TipoImpiantoVO tipoImpiantoVO = new TipoImpiantoVO();
					tipoImpiantoVO.setIdImpianto(new Long(rs.getLong("ID_IMPIANTO")));
					tipoImpiantoVO.setDescrizione(rs.getString("DESC_IMPIANTO"));
					tipoImpiantoVO.setDataInizioValidita(rs.getDate("DATA_INIZIO_VALIDITA"));
					tipoImpiantoVO.setDataFineValidita(rs.getDate("DATA_FINE_VALIDITA"));
					utilizzoParticellaVO.setTipoImpiantoVO(tipoImpiantoVO);
				}
				utilizzoParticellaVO.setSestoSuFile(rs.getString("SESTO_SU_FILE"));
				utilizzoParticellaVO.setSestoTraFile(rs.getString("SESTO_TRA_FILE"));
				utilizzoParticellaVO.setNumeroPianteCeppi(rs.getString("NUMERO_PIANTE_CEPPI"));
				
				
				
				utilizzoParticellaVO.setIdSemina(checkLongNull(rs.getString("ID_SEMINA")));
        utilizzoParticellaVO.setIdSeminaSecondario(checkLongNull(rs.getString("ID_SEMINA_SECONDARIA")));
        utilizzoParticellaVO.setDataInizioDestinazione(rs.getTimestamp("DATA_INIZIO_DESTINAZIONE"));
        utilizzoParticellaVO.setDataFineDestinazione(rs.getTimestamp("DATA_FINE_DESTINAZIONE"));
        utilizzoParticellaVO.setIdFaseAllevamento(checkLongNull(rs.getString("ID_FASE_ALLEVAMENTO")));
        utilizzoParticellaVO.setIdPraticaMantenimento(checkLongNull(rs.getString("ID_PRATICA_MANTENIMENTO")));
        utilizzoParticellaVO.setDataInizioDestinazioneSec(rs.getTimestamp("DATA_INIZIO_DESTINAZIONE_SEC"));
        utilizzoParticellaVO.setDataFineDestinazioneSec(rs.getTimestamp("DATA_FINE_DESTINAZIONE_SEC"));
			}

			rs.close();
			stmt.close();

		}
		catch(SQLException exc) {
			SolmrLogger.error(this, "findUtilizzoParticellaByPrimaryKey in UtilizzoParticellaDAO - SQLException: "+exc.getMessage()+"\n");
			throw new DataAccessException(exc.getMessage());
		}
		catch(Exception ex) {
			SolmrLogger.error(this, "findUtilizzoParticellaByPrimaryKey in UtilizzoParticellaDAO - Generic Exception: "+ex+"\n");
			throw new DataAccessException(ex.getMessage());
		}
		finally {
			try {
				if(stmt != null) stmt.close();
				if(conn != null) conn.close();
			}
			catch(SQLException exc) {
				SolmrLogger.error(this, "findUtilizzoParticellaByPrimaryKey in UtilizzoParticellaDAO - SQLException while closing Statement and Connection: "+exc.getMessage()+"\n");
				throw new DataAccessException(exc.getMessage());
			}
			catch(Exception ex) {
				SolmrLogger.error(this, "findUtilizzoParticellaByPrimaryKey in UtilizzoParticellaDAO - Generic Exception while closing Statement and Connection: "+ex.getMessage()+"\n");
				throw new DataAccessException(ex.getMessage());
			}
		}
		SolmrLogger.debug(this, "Invocated findUtilizzoParticellaByPrimaryKey method in UtilizzoParticellaDAO\n");
		return utilizzoParticellaVO;
	}
	
	/**
	 * Metodo che mi restituisce la somma delle superfici utilizzate in relazione ad
	 * un id_conduzione_particella
	 * 
	 * @param idConduzioneParticella
	 * @return double
	 * @throws DataAccessException
	 */
	public double getTotSupUtilizzataByIdConduzioneParticella(Long idConduzioneParticella) throws DataAccessException {
		SolmrLogger.debug(this, "Invocating getTotSupUtilizzataByIdConduzioneParticella method in UtilizzoParticellaDAO\n");
		Connection conn = null;
		PreparedStatement stmt = null;
		double totSupUtilizzata = 0;

		try {
			SolmrLogger.debug(this, "Creating db-connection in getTotSupUtilizzataByIdConduzioneParticella method in UtilizzoParticellaDAO\n");
			conn = getDatasource().getConnection();
			SolmrLogger.debug(this, "Created db-connection in getTotSupUtilizzataByIdConduzioneParticella method in UtilizzoParticellaDAO and it values: "+conn+"\n");

			String query = " SELECT SUM(SUPERFICIE_UTILIZZATA) AS TOT_SUP" +
                     	   " FROM   DB_UTILIZZO_PARTICELLA " +
                     	   " WHERE  ID_CONDUZIONE_PARTICELLA = ? ";

			SolmrLogger.debug(this, "Value of parameter 1 [ID_CONDUZIONE_PARTICELLA] in getTotSupUtilizzataByIdConduzioneParticella method in UtilizzoParticellaDAO: "+idConduzioneParticella+"\n");

			stmt = conn.prepareStatement(query);
			
			stmt.setLong(1, idConduzioneParticella.longValue());

			SolmrLogger.debug(this, "Executing getTotSupUtilizzataByIdConduzioneParticella: "+query+"\n");

			ResultSet rs = stmt.executeQuery();

			if(rs.next()) {
				totSupUtilizzata = rs.getDouble("TOT_SUP");
			}
			
			rs.close();
			stmt.close();

		}
		catch(SQLException exc) {
			SolmrLogger.error(this, "getTotSupUtilizzataByIdConduzioneParticella in UtilizzoParticellaDAO - SQLException: "+exc.getMessage()+"\n");
			throw new DataAccessException(exc.getMessage());
		}
		catch(Exception ex) {
			SolmrLogger.error(this, "getTotSupUtilizzataByIdConduzioneParticella in UtilizzoParticellaDAO - Generic Exception: "+ex+"\n");
			throw new DataAccessException(ex.getMessage());
		}
		finally {
			try {
				if(stmt != null) stmt.close();
				if(conn != null) conn.close();
			}
			catch(SQLException exc) {
				SolmrLogger.error(this, "getTotSupUtilizzataByIdConduzioneParticella in UtilizzoParticellaDAO - SQLException while closing Statement and Connection: "+exc.getMessage()+"\n");
				throw new DataAccessException(exc.getMessage());
			}
			catch(Exception ex) {
				SolmrLogger.error(this, "getTotSupUtilizzataByIdConduzioneParticella in UtilizzoParticellaDAO - Generic Exception while closing Statement and Connection: "+ex.getMessage()+"\n");
				throw new DataAccessException(ex.getMessage());
			}
		}
		SolmrLogger.debug(this, "Invocated getTotSupUtilizzataByIdConduzioneParticella method in UtilizzoParticellaDAO\n");
		return totSupUtilizzata;
	}
	
	/**
	 * Metodo utilizzato per effettuare la modifica del record relativa alla tabella
	 * DB_UTILIZZO_PARTICELLA
	 * 
	 * @param utilizzoParticellaVO
	 * @throws DataAccessException
	 */
	public void updateUtilizzoParticella(UtilizzoParticellaVO utilizzoParticellaVO) throws DataAccessException 
	{
		SolmrLogger.debug(this, "Invocating updateUtilizzoParticella method in UtilizzoParticellaDAO\n");
    Connection conn = null;
    PreparedStatement stmt = null;

    try 
    {
    	SolmrLogger.debug(this, "Creating db-connection in updateUtilizzoParticella method in UtilizzoParticellaDAO\n");
  		conn = getDatasource().getConnection();
  		SolmrLogger.debug(this, "Created db-connection in updateUtilizzoParticella method in UtilizzoParticellaDAO and it values: "+conn+"\n");
  
  		String query = " UPDATE DB_UTILIZZO_PARTICELLA "+
                         " SET    ID_UTILIZZO = ?, " +
                         "        ID_CONDUZIONE_PARTICELLA = ?, " +
                         "        SUPERFICIE_UTILIZZATA = ?, " +
                         "        DATA_AGGIORNAMENTO = SYSDATE, " +
                         "        ID_UTENTE_AGGIORNAMENTO = ?, " +
                         "        ANNO = ?, " +
                         "        NOTE = ?, " +
                         "        ID_UTILIZZO_SECONDARIO = ?, " +
                         "        SUP_UTILIZZATA_SECONDARIA = ?, " +
                         "        ID_VARIETA = ?, " +
                         "        ID_VARIETA_SECONDARIA = ?, " +
                         "        ANNO_IMPIANTO = ?, " +
                         "        ID_IMPIANTO = ?, " +
                         "        SESTO_SU_FILE = ?, " +
                         "        SESTO_TRA_FILE = ?, " +
                         "        NUMERO_PIANTE_CEPPI = ?, " +
                         "        ID_TIPO_DETTAGLIO_USO = ?, " +
                         "        ID_TIPO_DETT_USO_SECONDARIO = ?, " +
                         "        ID_TIPO_EFA = ?, " +
                         "        VALORE_ORIGINALE = ?, " +
                         "        VALORE_DOPO_CONVERSIONE = ?, " +
                         "        VALORE_DOPO_PONDERAZIONE = ?," +
                         "        ID_TIPO_PERIODO_SEMINA = ?, " +
                         "        ID_TIPO_PERIODO_SEMINA_SECOND = ?, " +
                         "        ID_CATALOGO_MATRICE = ?, " +
                         "        ID_CATALOGO_MATRICE_SECONDARIO = ?, " +
                         "        ID_SEMINA = ?, " +
                         "        ID_SEMINA_SECONDARIA = ?," +
                         "        DATA_INIZIO_DESTINAZIONE = ?, " +
                         "        DATA_FINE_DESTINAZIONE = ?, " +
                         "        ID_FASE_ALLEVAMENTO = ?, " +
                         "        ID_PRATICA_MANTENIMENTO = ?, " +
                         "        DATA_INIZIO_DESTINAZIONE_SEC = ?, " +
                         "        DATA_FINE_DESTINAZIONE_SEC = ?  " +
                         " WHERE  ID_UTILIZZO_PARTICELLA = ? ";
  
  		stmt = conn.prepareStatement(query);
  
  		SolmrLogger.debug(this, "Executing updateUtilizzoParticella: "+query);
  		
  		int idx = 0;
  
  		stmt.setLong(++idx, utilizzoParticellaVO.getIdUtilizzo().longValue());
  		SolmrLogger.debug(this, "Value of parameter 1 [ID_UTILIZZO] in method updateUtilizzoParticella in UtilizzoParticellaDAO: "+utilizzoParticellaVO.getIdUtilizzo().longValue()+"\n");
  		stmt.setLong(++idx, utilizzoParticellaVO.getIdConduzioneParticella().longValue());
  		SolmrLogger.debug(this, "Value of parameter 2 [ID_CONDUZIONE_PARTICELLA] in method updateUtilizzoParticella in UtilizzoParticellaDAO: "+utilizzoParticellaVO.getIdConduzioneParticella().longValue()+"\n");
  		stmt.setString(++idx, StringUtils.parseSuperficieField(utilizzoParticellaVO.getSuperficieUtilizzata()));
  		SolmrLogger.debug(this, "Value of parameter 3 [SUPERFICIE_UTILIZZATA] in method updateUtilizzoParticella in UtilizzoParticellaDAO: "+StringUtils.parseSuperficieField(utilizzoParticellaVO.getSuperficieUtilizzata())+"\n");
  		stmt.setLong(++idx, utilizzoParticellaVO.getIdUtenteAggiornamento().longValue());
  		SolmrLogger.debug(this, "Value of parameter 4 [ID_UTENTE] in method updateUtilizzoParticella in UtilizzoParticellaDAO: "+utilizzoParticellaVO.getIdUtenteAggiornamento().longValue()+"\n");
  		stmt.setString(++idx, utilizzoParticellaVO.getAnno());
  		SolmrLogger.debug(this, "Value of parameter 5 [ANNO] in method updateUtilizzoParticella in UtilizzoParticellaDAO: "+utilizzoParticellaVO.getAnno()+"\n");
  		stmt.setString(++idx, utilizzoParticellaVO.getNote());
  		SolmrLogger.debug(this, "Value of parameter 6 [NOTE] in method updateUtilizzoParticella in UtilizzoParticellaDAO: "+utilizzoParticellaVO.getNote()+"\n");
  		if(utilizzoParticellaVO.getIdUtilizzoSecondario() != null) {
  			stmt.setLong(++idx, utilizzoParticellaVO.getIdUtilizzoSecondario().longValue());
  			SolmrLogger.debug(this, "Value of parameter 7 [ID_UTILIZZO_SECONDARIO] in method updateUtilizzoParticella in UtilizzoParticellaDAO: "+utilizzoParticellaVO.getIdUtilizzoSecondario().longValue()+"\n");
  		}
  		else {
  			 SolmrLogger.debug(this, "Value of parameter 7 [ID_UTILIZZO_SECONDARIO] in method updateUtilizzoParticella in UtilizzoParticellaDAO: "+null+"\n");
  			 stmt.setString(++idx, null);
  		}
  		if(Validator.isNotEmpty(utilizzoParticellaVO.getSupUtilizzataSecondaria())) {
  			stmt.setString(++idx, StringUtils.parseSuperficieField(utilizzoParticellaVO.getSupUtilizzataSecondaria()));
  			SolmrLogger.debug(this, "Value of parameter 8 [SUP_UTILIZZATA_SECONDARIA] in method updateUtilizzoParticella in UtilizzoParticellaDAO: "+StringUtils.parseSuperficieField(utilizzoParticellaVO.getSupUtilizzataSecondaria())+"\n");
  		}
  		else {
  			stmt.setString(++idx, null);
  			SolmrLogger.debug(this, "Value of parameter 8 [SUP_UTILIZZATA_SECONDARIA] in method updateUtilizzoParticella in UtilizzoParticellaDAO: "+null+"\n");
  		}
  		if(utilizzoParticellaVO.getIdVarieta() != null) {
  			stmt.setLong(++idx, utilizzoParticellaVO.getIdVarieta().longValue());
  			SolmrLogger.debug(this, "Value of parameter 9 [ID_VARIETA] in method updateUtilizzoParticella in UtilizzoParticellaDAO: "+utilizzoParticellaVO.getIdVarieta().longValue()+"\n");
  		}
  		else {
  			stmt.setString(++idx, null);
  			SolmrLogger.debug(this, "Value of parameter 9 [ID_VARIETA] in method updateUtilizzoParticella in UtilizzoParticellaDAO: "+null+"\n");
  		}
  		if(utilizzoParticellaVO.getIdVarietaSecondaria() != null) {
  			stmt.setLong(++idx, utilizzoParticellaVO.getIdVarietaSecondaria().longValue());
  			SolmrLogger.debug(this, "Value of parameter 10 [ID_VARIETA_SECONDARIA] in method updateUtilizzoParticella in UtilizzoParticellaDAO: "+utilizzoParticellaVO.getIdVarietaSecondaria().longValue()+"\n");
  		}
  		else {
  			stmt.setString(++idx, null);
  			SolmrLogger.debug(this, "Value of parameter 10 [ID_VARIETA_SECONDARIA] in method updateUtilizzoParticella in UtilizzoParticellaDAO: "+null+"\n");
  		}
  		stmt.setString(++idx, utilizzoParticellaVO.getAnnoImpianto());
  		SolmrLogger.debug(this, "Value of parameter 11 [ANNO_IMPIANTO] in method updateUtilizzoParticella in UtilizzoParticellaDAO: "+utilizzoParticellaVO.getAnnoImpianto()+"\n");
  		if(utilizzoParticellaVO.getIdImpianto() != null) {
  			stmt.setLong(++idx, utilizzoParticellaVO.getIdImpianto().longValue());
  			SolmrLogger.debug(this, "Value of parameter 12 [ID_IMPIANTO] in method updateUtilizzoParticella in UtilizzoParticellaDAO: "+utilizzoParticellaVO.getIdImpianto().longValue()+"\n");
  		}
  		else {
  			stmt.setString(++idx, null);
  			SolmrLogger.debug(this, "Value of parameter 12 [ID_IMPIANTO] in method updateUtilizzoParticella in UtilizzoParticellaDAO: "+null+"\n");
  		}
  		stmt.setString(++idx, utilizzoParticellaVO.getSestoSuFile());
  		SolmrLogger.debug(this, "Value of parameter 13 [SESTO_SU_FILE] in method updateUtilizzoParticella in UtilizzoParticellaDAO: "+utilizzoParticellaVO.getSestoSuFile()+"\n");
  		stmt.setString(++idx, utilizzoParticellaVO.getSestoTraFile());
  		SolmrLogger.debug(this, "Value of parameter 14 [SESTO_TRA_FILE] in method updateUtilizzoParticella in UtilizzoParticellaDAO: "+utilizzoParticellaVO.getSestoTraFile()+"\n");
  		stmt.setString(++idx, utilizzoParticellaVO.getNumeroPianteCeppi());
  		SolmrLogger.debug(this, "Value of parameter 15 [NUMERO_PIANTE_CEPPI] in method updateUtilizzoParticella in UtilizzoParticellaDAO: "+utilizzoParticellaVO.getNumeroPianteCeppi()+"\n");
  		stmt.setBigDecimal(++idx, convertLongToBigDecimal(utilizzoParticellaVO.getIdTipoDettaglioUso()));
  		stmt.setBigDecimal(++idx, convertLongToBigDecimal(utilizzoParticellaVO.getIdTipoDettaglioUsoSecondario()));
  		stmt.setBigDecimal(++idx, convertLongToBigDecimal(utilizzoParticellaVO.getIdTipoEfa()));
  		stmt.setBigDecimal(++idx, utilizzoParticellaVO.getValoreOriginale());
  		stmt.setBigDecimal(++idx, utilizzoParticellaVO.getValoreDopoConversione());
  		stmt.setBigDecimal(++idx, utilizzoParticellaVO.getValoreDopoPonderazione());
      stmt.setBigDecimal(++idx, convertLongToBigDecimal(utilizzoParticellaVO.getIdTipoPeriodoSemina()));
      stmt.setBigDecimal(++idx, convertLongToBigDecimal(utilizzoParticellaVO.getIdTipoPeriodoSeminaSecondario()));
      stmt.setBigDecimal(++idx, convertLongToBigDecimal(utilizzoParticellaVO.getIdCatalogoMatrice()));
      stmt.setBigDecimal(++idx, convertLongToBigDecimal(utilizzoParticellaVO.getIdCatalogoMatriceSecondario()));
      stmt.setBigDecimal(++idx, convertLongToBigDecimal(utilizzoParticellaVO.getIdSemina()));
      stmt.setBigDecimal(++idx, convertLongToBigDecimal(utilizzoParticellaVO.getIdSeminaSecondario()));
      stmt.setTimestamp(++idx, convertDateToTimestamp(utilizzoParticellaVO.getDataInizioDestinazione()));
      stmt.setTimestamp(++idx, convertDateToTimestamp(utilizzoParticellaVO.getDataFineDestinazione()));
      stmt.setBigDecimal(++idx, convertLongToBigDecimal(utilizzoParticellaVO.getIdFaseAllevamento()));
      stmt.setBigDecimal(++idx, convertLongToBigDecimal(utilizzoParticellaVO.getIdPraticaMantenimento()));
      stmt.setTimestamp(++idx, convertDateToTimestamp(utilizzoParticellaVO.getDataInizioDestinazioneSec()));
      stmt.setTimestamp(++idx, convertDateToTimestamp(utilizzoParticellaVO.getDataFineDestinazioneSec()));
  		
  		stmt.setLong(++idx, utilizzoParticellaVO.getIdUtilizzoParticella().longValue());
  		SolmrLogger.debug(this, "Value of parameter 16 [ID_UTILIZZO_PARTICELLA] in method updateUtilizzoParticella in UtilizzoParticellaDAO: "+utilizzoParticellaVO.getIdUtilizzoParticella().longValue()+"\n");
  
  		stmt.executeUpdate();
  
  		stmt.close();
    }
    catch(SQLException exc) {
    	SolmrLogger.error(this, "updateUtilizzoParticella in UtilizzoParticellaDAO - SQLException: "+exc.getMessage());
    	throw new DataAccessException(exc.getMessage());
    }
    catch (Exception ex) {
    	SolmrLogger.error(this, "updateUtilizzoParticella in UtilizzoParticellaDAO - Generic Exception: "+ex);
    	throw new DataAccessException(ex.getMessage());
    }
    finally {
    	try {
    		if(stmt != null) {
    			stmt.close();
    		}
    		if(conn != null) {
    			conn.close();
    		}
    	}
    	catch(SQLException exc) {
    		SolmrLogger.error(this, "updateUtilizzoParticella in UtilizzoParticellaDAO - SQLException while closing Statement and Connection: "+exc.getMessage());
    		throw new DataAccessException(exc.getMessage());
    	}
    	catch(Exception ex) {
    		SolmrLogger.error(this, "updateUtilizzoParticella in UtilizzoParticellaDAO - Generic Exception while closing Statement and Connection: "+ex.getMessage());
    		throw new DataAccessException(ex.getMessage());
    	}
    }
    SolmrLogger.debug(this, "Invocated updateUtilizzoParticella method in UtilizzoParticellaDAO\n");
	}
	
	/**
	 * Metodo utilizzato per inserire un record nella tabella DB_UTILIZZO_PARTICELLA
	 * 
	 * @param utilizzoParticellaVO
	 * @return java.lang.Long
	 * @throws DataAccessException
	 */
	public Long insertUtilizzoParticella(UtilizzoParticellaVO utilizzoParticellaVO) throws DataAccessException {
		SolmrLogger.debug(this, "Invocating insertUtilizzoParticella method in UtilizzoParticellaDAO\n");
		Long idUtilizzoParticella = null;
		Connection conn = null;
		PreparedStatement stmt = null;

		try {

			idUtilizzoParticella = getNextPrimaryKey((String)SolmrConstants.get("SEQ_UTILIZZO_PARTICELLA"));
			SolmrLogger.debug(this, "Creating db-connection in insertUtilizzoParticella method in UtilizzoParticellaDAO\n");
			conn = getDatasource().getConnection();
			SolmrLogger.debug(this, "Created db-connection in insertUtilizzoParticella method in UtilizzoParticellaDAO and it values: "+conn+"\n");

			String query  = " INSERT INTO DB_UTILIZZO_PARTICELLA "+
                    		"            (ID_UTILIZZO_PARTICELLA, " +
                    		"             ID_UTILIZZO, " +
                    		"             ID_CONDUZIONE_PARTICELLA, "+
                    		"             SUPERFICIE_UTILIZZATA, " +
                    		"             DATA_AGGIORNAMENTO, " +
                    		"             ID_UTENTE_AGGIORNAMENTO, " +
                    		"             ANNO, " +
                    		"             NOTE, " +
                    		"             ID_UTILIZZO_SECONDARIO, " +
                    		"             SUP_UTILIZZATA_SECONDARIA, " +
                    		"             ID_VARIETA, " +
                    		"             ID_VARIETA_SECONDARIA, " +
                    		"             ANNO_IMPIANTO, " +
                    		"             ID_IMPIANTO, " +
                    		"             SESTO_SU_FILE, " +
                    		"             SESTO_TRA_FILE, " +
                    		"             NUMERO_PIANTE_CEPPI," +
                    		"             ID_TIPO_DETTAGLIO_USO, " +
                        "             ID_TIPO_DETT_USO_SECONDARIO, " +
                        "             ID_TIPO_EFA, " +
                        "             VALORE_ORIGINALE, " +
                        "             VALORE_DOPO_CONVERSIONE, " +
                        "             VALORE_DOPO_PONDERAZIONE," +
                        "             ID_TIPO_PERIODO_SEMINA," +
                        "             ID_TIPO_PERIODO_SEMINA_SECOND," +
                        "             ID_CATALOGO_MATRICE, " +
                        "             ID_CATALOGO_MATRICE_SECONDARIO, " +
                        "             ID_SEMINA," +
                        "             ID_SEMINA_SECONDARIA," +
                        "             DATA_INIZIO_DESTINAZIONE, " +
                        "             DATA_FINE_DESTINAZIONE, " +
                        "             ID_FASE_ALLEVAMENTO, " +
                        "             ID_PRATICA_MANTENIMENTO, " +
                        "             DATA_INIZIO_DESTINAZIONE_SEC, " +
                        "             DATA_FINE_DESTINAZIONE_SEC) " +
                    		" VALUES     (?, ?, ?, ?, SYSDATE, ?, ?, ?, ?, ?, " +
                    		"             ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, " +
                    		"             ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, " +
                    		"             ?, ?, ?, ?, ?) ";


			stmt = conn.prepareStatement(query);
			
			SolmrLogger.debug(this, "Executing insertUtilizzoParticella: "+query);
			int idx = 0;
			
			stmt.setLong(++idx, idUtilizzoParticella.longValue());
			SolmrLogger.debug(this, "Value of parameter 1 [ID_UTILIZZO_PARTICELLA] in method insertUtilizzoParticella in UtilizzoParticellaDAO: "+idUtilizzoParticella.longValue()+"\n");
			stmt.setLong(++idx, utilizzoParticellaVO.getIdUtilizzo().longValue());
			SolmrLogger.debug(this, "Value of parameter 2 [ID_UTILIZZO] in method insertUtilizzoParticella in UtilizzoParticellaDAO: "+utilizzoParticellaVO.getIdUtilizzo().longValue()+"\n");
			stmt.setLong(++idx, utilizzoParticellaVO.getIdConduzioneParticella().longValue());
			SolmrLogger.debug(this, "Value of parameter 3 [ID_CONDUZIONE_PARTICELLA] in method insertUtilizzoParticella in UtilizzoParticellaDAO: "+utilizzoParticellaVO.getIdConduzioneParticella().longValue()+"\n");
			stmt.setString(++idx, StringUtils.parseSuperficieField(utilizzoParticellaVO.getSuperficieUtilizzata()));
			SolmrLogger.debug(this, "Value of parameter 4 [SUPERFICIE_UTILIZZATA] in method insertUtilizzoParticella in UtilizzoParticellaDAO: "+StringUtils.parseSuperficieField(utilizzoParticellaVO.getSuperficieUtilizzata())+"\n");
			stmt.setLong(++idx, utilizzoParticellaVO.getIdUtenteAggiornamento().longValue());
			SolmrLogger.debug(this, "Value of parameter 5 [ID_UTENTE_AGGIORNAMENTO] in method insertUtilizzoParticella in UtilizzoParticellaDAO: "+utilizzoParticellaVO.getIdUtenteAggiornamento().longValue()+"\n");
			stmt.setString(++idx, utilizzoParticellaVO.getAnno());
			SolmrLogger.debug(this, "Value of parameter 6 [ANNO] in method insertUtilizzoParticella in UtilizzoParticellaDAO: "+utilizzoParticellaVO.getAnno()+"\n");
			stmt.setString(++idx, utilizzoParticellaVO.getNote());
			SolmrLogger.debug(this, "Value of parameter 7 [NOTE] in method insertUtilizzoParticella in UtilizzoParticellaDAO: "+utilizzoParticellaVO.getNote()+"\n");
			if(utilizzoParticellaVO.getIdUtilizzoSecondario() != null) {
				stmt.setLong(++idx, utilizzoParticellaVO.getIdUtilizzoSecondario().longValue());
				SolmrLogger.debug(this, "Value of parameter 8 [ID_UTILIZZO_SECONDARIO] in method insertUtilizzoParticella in UtilizzoParticellaDAO: "+utilizzoParticellaVO.getIdUtilizzoSecondario().longValue()+"\n");
			}
			else {
				stmt.setString(++idx, null);
				SolmrLogger.debug(this, "Value of parameter 8 [ID_UTILIZZO_SECONDARIO] in method insertUtilizzoParticella in UtilizzoParticellaDAO: "+null+"\n");
			}
			if(Validator.isNotEmpty(utilizzoParticellaVO.getSupUtilizzataSecondaria())) {
				stmt.setString(++idx, StringUtils.parseSuperficieField(utilizzoParticellaVO.getSupUtilizzataSecondaria()));
				SolmrLogger.debug(this, "Value of parameter 9 [SUP_UTILIZZATA_SECONDARIA] in method insertUtilizzoParticella in UtilizzoParticellaDAO: "+StringUtils.parseSuperficieField(utilizzoParticellaVO.getSupUtilizzataSecondaria())+"\n");
			}
			else {
				stmt.setString(++idx, null);
				SolmrLogger.debug(this, "Value of parameter 9 [SUP_UTILIZZATA_SECONDARIA] in method insertUtilizzoParticella in UtilizzoParticellaDAO: "+null+"\n");
			}
			if(utilizzoParticellaVO.getIdVarieta() != null) {
				stmt.setLong(++idx, utilizzoParticellaVO.getIdVarieta().longValue());
				SolmrLogger.debug(this, "Value of parameter 10 [ID_VARIETA] in method insertUtilizzoParticella in UtilizzoParticellaDAO: "+utilizzoParticellaVO.getIdVarieta().longValue()+"\n");
			}
			else {
				stmt.setString(++idx, null);
				SolmrLogger.debug(this, "Value of parameter 10 [ID_VARIETA] in method insertUtilizzoParticella in UtilizzoParticellaDAO: "+null+"\n");
			}
			if(utilizzoParticellaVO.getIdVarietaSecondaria() != null) {
				stmt.setLong(++idx, utilizzoParticellaVO.getIdVarietaSecondaria().longValue());
				SolmrLogger.debug(this, "Value of parameter 11 [ID_VARIETA_SECONDARIA] in method insertUtilizzoParticella in UtilizzoParticellaDAO: "+utilizzoParticellaVO.getIdVarietaSecondaria().longValue()+"\n");
			}
			else {
				stmt.setString(++idx, null);
				SolmrLogger.debug(this, "Value of parameter 11 [ID_VARIETA_SECONDARIA] in method insertUtilizzoParticella in UtilizzoParticellaDAO: "+null+"\n");
			}
			stmt.setString(++idx, utilizzoParticellaVO.getAnnoImpianto());
			SolmrLogger.debug(this, "Value of parameter 12 [ANNO_IMPIANTO] in method insertUtilizzoParticella in UtilizzoParticellaDAO: "+utilizzoParticellaVO.getAnnoImpianto()+"\n");
			if(utilizzoParticellaVO.getIdImpianto() != null) {
				stmt.setLong(++idx, utilizzoParticellaVO.getIdImpianto().longValue());
				SolmrLogger.debug(this, "Value of parameter 13 [ID_IMPIANTO] in method insertUtilizzoParticella in UtilizzoParticellaDAO: "+utilizzoParticellaVO.getIdImpianto().longValue()+"\n");
			}
			else {
				stmt.setString(++idx, null);
				SolmrLogger.debug(this, "Value of parameter 13 [ID_IMPIANTO] in method insertUtilizzoParticella in UtilizzoParticellaDAO: "+null+"\n");
			}
			stmt.setString(++idx, utilizzoParticellaVO.getSestoSuFile());
			SolmrLogger.debug(this, "Value of parameter 14 [SESTO_SU_FILE] in method insertUtilizzoParticella in UtilizzoParticellaDAO: "+utilizzoParticellaVO.getSestoSuFile()+"\n");
			stmt.setString(++idx, utilizzoParticellaVO.getSestoTraFile());
			SolmrLogger.debug(this, "Value of parameter 15 [SESTO_TRA_FILE] in method insertUtilizzoParticella in UtilizzoParticellaDAO: "+utilizzoParticellaVO.getSestoTraFile()+"\n");
			stmt.setString(++idx, utilizzoParticellaVO.getNumeroPianteCeppi());
			SolmrLogger.debug(this, "Value of parameter 16 [NUMERO_PIANTE_CEPPI] in method insertUtilizzoParticella in UtilizzoParticellaDAO: "+utilizzoParticellaVO.getNumeroPianteCeppi()+"\n");

			stmt.setBigDecimal(++idx, convertLongToBigDecimal(utilizzoParticellaVO.getIdTipoDettaglioUso()));
      stmt.setBigDecimal(++idx, convertLongToBigDecimal(utilizzoParticellaVO.getIdTipoDettaglioUsoSecondario()));
      stmt.setBigDecimal(++idx, convertLongToBigDecimal(utilizzoParticellaVO.getIdTipoEfa()));
      stmt.setBigDecimal(++idx, utilizzoParticellaVO.getValoreOriginale());
      stmt.setBigDecimal(++idx, utilizzoParticellaVO.getValoreDopoConversione());
      stmt.setBigDecimal(++idx, utilizzoParticellaVO.getValoreDopoPonderazione());
      stmt.setBigDecimal(++idx, convertLongToBigDecimal(utilizzoParticellaVO.getIdTipoPeriodoSemina()));
      stmt.setBigDecimal(++idx, convertLongToBigDecimal(utilizzoParticellaVO.getIdTipoPeriodoSeminaSecondario()));
      stmt.setBigDecimal(++idx, convertLongToBigDecimal(utilizzoParticellaVO.getIdCatalogoMatrice()));
      stmt.setBigDecimal(++idx, convertLongToBigDecimal(utilizzoParticellaVO.getIdCatalogoMatriceSecondario()));
      stmt.setBigDecimal(++idx, convertLongToBigDecimal(utilizzoParticellaVO.getIdSemina()));
      stmt.setBigDecimal(++idx, convertLongToBigDecimal(utilizzoParticellaVO.getIdSeminaSecondario()));
      stmt.setTimestamp(++idx, convertDateToTimestamp(utilizzoParticellaVO.getDataInizioDestinazione()));
      stmt.setTimestamp(++idx, convertDateToTimestamp(utilizzoParticellaVO.getDataFineDestinazione()));
      stmt.setBigDecimal(++idx, convertLongToBigDecimal(utilizzoParticellaVO.getIdFaseAllevamento()));
      stmt.setBigDecimal(++idx, convertLongToBigDecimal(utilizzoParticellaVO.getIdPraticaMantenimento()));
      stmt.setTimestamp(++idx, convertDateToTimestamp(utilizzoParticellaVO.getDataInizioDestinazioneSec()));
      stmt.setTimestamp(++idx, convertDateToTimestamp(utilizzoParticellaVO.getDataFineDestinazioneSec()));
      
			stmt.executeUpdate();
    
			stmt.close();
		}
		catch(SQLException exc) {
			SolmrLogger.error(this, "SQLException in insertUtilizzoParticella: "+exc.getMessage());
			throw new DataAccessException(exc.getMessage());
		}
		catch(Exception ex) {
			SolmrLogger.error(this, "Generic Exception in insertUtilizzoParticella: "+ex.getMessage());
			throw new DataAccessException(ex.getMessage());
		}
		finally {
			try {
				if(stmt != null) {
					stmt.close();
				}
				if(conn != null) {
					conn.close();
				}
			}
			catch(SQLException exc) {
				SolmrLogger.error(this, "SQLException while closing Statement and Connection in insertUtilizzoParticella: "+exc.getMessage());
				throw new DataAccessException(exc.getMessage());
			}
			catch(Exception ex) {
				SolmrLogger.error(this, "Generic Exception while closing Statement and Connection in insertUtilizzoParticella: "+ex.getMessage());
				throw new DataAccessException(ex.getMessage());
			}
		}
		SolmrLogger.debug(this, "Invocated insertUtilizzoParticella method in UtilizzoParticellaDAO\n");
		return idUtilizzoParticella;
	}

	
	/**
	 * Metodo utilizzato per eliminare un record nella tabella DB_UTILIZZO_PARTICELLA
	 * a partire dall'id_conduzione_particella
	 * 
	 * @param java.lang.Long idConduzioneParticella
	 * @throws DataAccessException
	 */
	public void deleteUtilizzoParticellaByIdConduzioneParticella(Long idConduzioneParticella) throws DataAccessException {
		SolmrLogger.debug(this, "Invocating deleteUtilizzoParticellaByIdConduzioneParticella method in UtilizzoParticellaDAO\n");
		Connection conn = null;
		PreparedStatement stmt = null;

		try {
			SolmrLogger.debug(this, "Creating db-connection in deleteUtilizzoParticellaByIdConduzioneParticella method in UtilizzoParticellaDAO\n");
			conn = getDatasource().getConnection();
			SolmrLogger.debug(this, "Created db-connection in deleteUtilizzoParticellaByIdConduzioneParticella method in UtilizzoParticellaDAO and it values: "+conn+"\n");

			String query  = " DELETE FROM  DB_UTILIZZO_PARTICELLA "+
                    		"        WHERE ID_CONDUZIONE_PARTICELLA = ? ";

			stmt = conn.prepareStatement(query);
			
			SolmrLogger.debug(this, "Executing deleteUtilizzoParticellaByIdConduzioneParticella: "+query);
			
			stmt.setLong(1, idConduzioneParticella.longValue());
			SolmrLogger.debug(this, "Value of parameter 1 [ID_CONDUZIONE_PARTICELLA] in method deleteUtilizzoParticellaByIdConduzioneParticella in UtilizzoParticellaDAO: "+idConduzioneParticella.longValue()+"\n");

			stmt.executeUpdate();
    
			stmt.close();
		}
		catch(SQLException exc) {
			SolmrLogger.error(this, "SQLException in deleteUtilizzoParticellaByIdConduzioneParticella: "+exc.getMessage());
			throw new DataAccessException(exc.getMessage());
		}
		catch(Exception ex) {
			SolmrLogger.error(this, "Generic Exception in deleteUtilizzoParticellaByIdConduzioneParticella: "+ex.getMessage());
			throw new DataAccessException(ex.getMessage());
		}
		finally {
			try {
				if(stmt != null) {
					stmt.close();
				}
				if(conn != null) {
					conn.close();
				}
			}
			catch(SQLException exc) {
				SolmrLogger.error(this, "SQLException while closing Statement and Connection in deleteUtilizzoParticellaByIdConduzioneParticella: "+exc.getMessage());
				throw new DataAccessException(exc.getMessage());
			}
			catch(Exception ex) {
				SolmrLogger.error(this, "Generic Exception while closing Statement and Connection in deleteUtilizzoParticellaByIdConduzioneParticella: "+ex.getMessage());
				throw new DataAccessException(ex.getMessage());
			}
		}
		SolmrLogger.debug(this, "Invocated deleteUtilizzoParticellaByIdConduzioneParticella method in UtilizzoParticellaDAO\n");
	}
	
	/**
	 * Metodo utilizzato per effettuare il riepilogo delle particelle relative
	 * ad un'azienda al piano in lavorazione per uso primario
	 * 
	 * @param idAzienda
	 * @param escludiAsservimento
	 * @param onlyActive
	 * @param orderBy
	 * @return it.csi.solmr.dto.anag.terreni.UtilizzoParticellaVO[]
	 * @throws DataAccessException
	 */
	public UtilizzoParticellaVO[] riepilogoUsoPrimario(Long idAzienda) throws DataAccessException 
	{
		SolmrLogger.debug(this, "Invocating riepilogoUsoPrimario method in UtilizzoParticellaDAO\n");
		Connection conn = null;
		PreparedStatement stmt = null;
		Vector<UtilizzoParticellaVO> elencoUtilizzoParticellaVO = new Vector<UtilizzoParticellaVO>();

		try 
		{
			SolmrLogger.debug(this, "Creating db-connection in riepilogoUsoPrimario method in UtilizzoParticellaDAO\n");
			conn = getDatasource().getConnection();
			SolmrLogger.debug(this, "Created db-connection in riepilogoUsoPrimario method in UtilizzoParticellaDAO and it values: "+conn+"\n");

			String query = "" +
				"SELECT  RCM.ID_UTILIZZO, " +
				"        TU.DESCRIZIONE, " +
				"        TU.ANNO_FINE_VALIDITA, " +
				"        RCM.FLAG_SAU, " +    
				"        NVL(SUM(UP.SUPERFICIE_UTILIZZATA),0) AS SUP_UTILIZZATA " +  
				"FROM    DB_CONDUZIONE_PARTICELLA CP, " +  
				"        DB_UTE U, " +  
				"        DB_UTILIZZO_PARTICELLA UP, " +  
				"        DB_TIPO_UTILIZZO TU, " +
				"        DB_R_CATALOGO_MATRICE RCM " +    
				"WHERE   CP.ID_UTE = U.ID_UTE " +  
				"AND     U.ID_AZIENDA = ? " +  
				"AND     UP.ID_CONDUZIONE_PARTICELLA = CP.ID_CONDUZIONE_PARTICELLA " + 
				"AND     UP.ID_CATALOGO_MATRICE = RCM.ID_CATALOGO_MATRICE " +
				"AND     RCM.ID_UTILIZZO = TU.ID_UTILIZZO " +
				"AND     U.DATA_FINE_ATTIVITA IS NULL " +
				"AND     CP.ID_TITOLO_POSSESSO <> 5 " +
			  "AND     CP.DATA_FINE_CONDUZIONE IS NULL " +
				"GROUP BY RCM.ID_UTILIZZO, " +  
				"         TU.DESCRIZIONE, " +  
				"         TU.ANNO_FINE_VALIDITA, " +
				"         RCM.FLAG_SAU " +
				"ORDER BY RCM.FLAG_SAU DESC, " +
				"         TU.DESCRIZIONE ASC ";

			SolmrLogger.debug(this, "Value of parameter 1 [ID_AZIENDA] in riepilogoUsoPrimario method in UtilizzoParticellaDAO: "+idAzienda+"\n");

			stmt = conn.prepareStatement(query);
			
			stmt.setLong(1, idAzienda.longValue());

			SolmrLogger.debug(this, "Executing riepilogoUsoPrimario: "+query+"\n");

			ResultSet rs = stmt.executeQuery();

			while(rs.next()) 
			{
				UtilizzoParticellaVO utilizzoParticellaVO = new UtilizzoParticellaVO();
				utilizzoParticellaVO.setIdUtilizzo(new Long(rs.getLong("ID_UTILIZZO")));
				TipoUtilizzoVO tipoUtilizzoVO = new TipoUtilizzoVO();
				tipoUtilizzoVO.setIdUtilizzo(new Long(rs.getLong("ID_UTILIZZO")));
				tipoUtilizzoVO.setDescrizione(rs.getString("DESCRIZIONE"));
				tipoUtilizzoVO.setAnnoFineValidita(rs.getString("ANNO_FINE_VALIDITA"));
				tipoUtilizzoVO.setFlagSau(rs.getString("FLAG_SAU"));
				utilizzoParticellaVO.setTipoUtilizzoVO(tipoUtilizzoVO);
				utilizzoParticellaVO.setSuperficieUtilizzata(rs.getString("SUP_UTILIZZATA"));
				elencoUtilizzoParticellaVO.add(utilizzoParticellaVO);
			}
			
			rs.close();
			stmt.close();

		}
		catch(SQLException exc) {
			SolmrLogger.error(this, "riepilogoUsoPrimario in UtilizzoParticellaDAO - SQLException: "+exc+"\n");
			throw new DataAccessException(exc.getMessage());
		}
		catch(Exception ex) {
			SolmrLogger.error(this, "riepilogoUsoPrimario in UtilizzoParticellaDAO - Generic Exception: "+ex+"\n");
			throw new DataAccessException(ex.getMessage());
		}
		finally {
			try {
				if(stmt != null) stmt.close();
				if(conn != null) conn.close();
			}
			catch(SQLException exc) {
				SolmrLogger.error(this, "riepilogoUsoPrimario in UtilizzoParticellaDAO - SQLException while closing Statement and Connection: "+exc+"\n");
				throw new DataAccessException(exc.getMessage());
			}
			catch(Exception ex) {
				SolmrLogger.error(this, "riepilogoUsoPrimario in UtilizzoParticellaDAO - Generic Exception while closing Statement and Connection: "+ex+"\n");
				throw new DataAccessException(ex.getMessage());
			}
		}
		SolmrLogger.debug(this, "Invocated riepilogoUsoPrimario method in UtilizzoParticellaDAO\n");
		if(elencoUtilizzoParticellaVO.size() == 0) {
			return (UtilizzoParticellaVO[])elencoUtilizzoParticellaVO.toArray(new UtilizzoParticellaVO[0]);
		}
		else {
			return (UtilizzoParticellaVO[])elencoUtilizzoParticellaVO.toArray(new UtilizzoParticellaVO[elencoUtilizzoParticellaVO.size()]);
		}
	}
	
	/**
	 * Metodo utilizzato per effettuare il riepilogo delle particelle relative
	 * ad un'azienda al piano in lavorazione per macro uso
	 * 
	 * @param idAzienda
	 * @param escludiAsservimento
	 * @param onlyActive
	 * @param orderBy
	 * @return it.csi.solmr.dto.anag.terreni.UtilizzoParticellaVO[]
	 * @throws DataAccessException
	 */
	public UtilizzoParticellaVO[] riepilogoMacroUso(Long idAzienda) throws DataAccessException 
	{
		SolmrLogger.debug(this, "Invocating riepilogoMacroUso method in UtilizzoParticellaDAO\n");
		Connection conn = null;
		PreparedStatement stmt = null;
		Vector<UtilizzoParticellaVO> elencoUtilizzoParticellaVO = new Vector<UtilizzoParticellaVO>();

		try 
		{
			SolmrLogger.debug(this, "Creating db-connection in riepilogoMacroUso method in UtilizzoParticellaDAO\n");
			conn = getDatasource().getConnection();
			SolmrLogger.debug(this, "Created db-connection in riepilogoMacroUso method in UtilizzoParticellaDAO and it values: "+conn+"\n");

			String query = "" +
				"SELECT   TMU.ID_MACRO_USO, " + 
				"         TMU.CODICE, " +
				"         TMU.DESCRIZIONE, " +
				"         SUM (UP.SUPERFICIE_UTILIZZATA) AS SUP_UTILIZZATA " +
				"FROM     DB_CONDUZIONE_PARTICELLA CP, " +
				"         DB_UTILIZZO_PARTICELLA UP, " +
				"         DB_UTE U, " +
				"         DB_STORICO_PARTICELLA SP, " +
        "         DB_TIPO_MACRO_USO TMU, " +
				"         DB_TIPO_MACRO_USO_VARIETA TMUV " +
				"WHERE    U.ID_AZIENDA = ? " +
				"AND      U.ID_UTE = CP.ID_UTE " +
				"AND      CP.ID_PARTICELLA = SP.ID_PARTICELLA " +
				"AND      CP.ID_CONDUZIONE_PARTICELLA = UP.ID_CONDUZIONE_PARTICELLA " +
				"AND      UP.ID_CATALOGO_MATRICE = TMUV.ID_CATALOGO_MATRICE " +
				"AND      TMUV.ID_MACRO_USO = TMU.ID_MACRO_USO " +
			  "AND      U.DATA_FINE_ATTIVITA IS NULL " +
				"AND      CP.DATA_FINE_CONDUZIONE IS NULL " +
				"AND      SP.DATA_FINE_VALIDITA IS NULL " + 
				"AND      TMUV.DATA_FINE_VALIDITA IS NULL " +
			  "GROUP BY TMU.ID_MACRO_USO, " +
				"         TMU.CODICE, " + 
				"         TMU.DESCRIZIONE " +
				"ORDER BY TMU.CODICE ASC, " +
        "         TMU.DESCRIZIONE ASC";

			SolmrLogger.debug(this, "Value of parameter 1 [ID_AZIENDA] in riepilogoMacroUso method in UtilizzoParticellaDAO: "+idAzienda+"\n");
			stmt = conn.prepareStatement(query);
			
			stmt.setLong(1, idAzienda.longValue());

			SolmrLogger.debug(this, "Executing riepilogoMacroUso: "+query+"\n");

			ResultSet rs = stmt.executeQuery();

			while(rs.next()) {
				UtilizzoParticellaVO utilizzoParticellaVO = new UtilizzoParticellaVO();
				TipoMacroUsoVO tipoMacroUsoVO = new TipoMacroUsoVO();
				tipoMacroUsoVO.setIdMacroUso(new Long(rs.getLong("ID_MACRO_USO")));
				tipoMacroUsoVO.setCodice(rs.getString("CODICE"));
				tipoMacroUsoVO.setDescrizione(rs.getString("DESCRIZIONE"));
				utilizzoParticellaVO.setTipoMacroUsoVO(tipoMacroUsoVO);
				utilizzoParticellaVO.setSuperficieUtilizzata(rs.getString("SUP_UTILIZZATA"));
				elencoUtilizzoParticellaVO.add(utilizzoParticellaVO);
			}
			
			rs.close();
			stmt.close();

		}
		catch(SQLException exc) {
			SolmrLogger.error(this, "riepilogoMacroUso in UtilizzoParticellaDAO - SQLException: "+exc+"\n");
			throw new DataAccessException(exc.getMessage());
		}
		catch(Exception ex) {
			SolmrLogger.error(this, "riepilogoMacroUso in UtilizzoParticellaDAO - Generic Exception: "+ex+"\n");
			throw new DataAccessException(ex.getMessage());
		}
		finally {
			try {
				if(stmt != null) stmt.close();
				if(conn != null) conn.close();
			}
			catch(SQLException exc) {
				SolmrLogger.error(this, "riepilogoMacroUso in UtilizzoParticellaDAO - SQLException while closing Statement and Connection: "+exc+"\n");
				throw new DataAccessException(exc.getMessage());
			}
			catch(Exception ex) {
				SolmrLogger.error(this, "riepilogoMacroUso in UtilizzoParticellaDAO - Generic Exception while closing Statement and Connection: "+ex+"\n");
				throw new DataAccessException(ex.getMessage());
			}
		}
		SolmrLogger.debug(this, "Invocated riepilogoMacroUso method in UtilizzoParticellaDAO\n");
		if(elencoUtilizzoParticellaVO.size() == 0) {
			return (UtilizzoParticellaVO[])elencoUtilizzoParticellaVO.toArray(new UtilizzoParticellaVO[0]);
		}
		else {
			return (UtilizzoParticellaVO[])elencoUtilizzoParticellaVO.toArray(new UtilizzoParticellaVO[elencoUtilizzoParticellaVO.size()]);
		}
	}
	
	/**
	 * Metodo utilizzato per effettuare il riepilogo delle particelle relative
	 * ad un'azienda al piano in lavorazione per uso secondario
	 * 
	 * @param idAzienda
	 * @param escludiAsservimento
	 * @param onlyActive
	 * @param orderBy
	 * @return it.csi.solmr.dto.anag.terreni.UtilizzoParticellaVO[]
	 * @throws DataAccessException
	 */
	public UtilizzoParticellaVO[] riepilogoUsoSecondario(Long idAzienda) throws DataAccessException 
	{
		SolmrLogger.debug(this, "Invocating riepilogoUsoSecondario method in UtilizzoParticellaDAO\n");
		Connection conn = null;
		PreparedStatement stmt = null;
		Vector<UtilizzoParticellaVO> elencoUtilizzoParticellaVO = new Vector<UtilizzoParticellaVO>();

		try {
			SolmrLogger.debug(this, "Creating db-connection in riepilogoUsoSecondario method in UtilizzoParticellaDAO\n");
			conn = getDatasource().getConnection();
			SolmrLogger.debug(this, "Created db-connection in riepilogoUsoSecondario method in UtilizzoParticellaDAO and it values: "+conn+"\n");

			String query = "" +
				"SELECT   RCM.ID_UTILIZZO, " +
				"         TU.DESCRIZIONE, " +  
				"         RCM.FLAG_SAU, " +    
				"         SUM(UP.SUP_UTILIZZATA_SECONDARIA) AS SUP_UTILIZZATA_SEC " +  
				"FROM     DB_CONDUZIONE_PARTICELLA CP, " +  
				"         DB_UTE U, " +  
				"         DB_UTILIZZO_PARTICELLA UP, " +  
				"         DB_TIPO_UTILIZZO TU," +
				"         DB_R_CATALOGO_MATRICE RCM " +    
				"WHERE    CP.ID_UTE = U.ID_UTE " +  
				"AND      U.ID_AZIENDA = ? " +  
				"AND      UP.ID_CONDUZIONE_PARTICELLA = CP.ID_CONDUZIONE_PARTICELLA " +
				"AND      UP.ID_CATALOGO_MATRICE_SECONDARIO = RCM.ID_CATALOGO_MATRICE " +
				"AND      RCM.ID_UTILIZZO = TU.ID_UTILIZZO " +
			  "AND      CP.DATA_FINE_CONDUZIONE IS NULL " +
			  "AND      U.DATA_FINE_ATTIVITA IS NULL " +
			  "AND      TU.ANNO_FINE_VALIDITA IS NULL " +
				"GROUP BY RCM.ID_UTILIZZO, " +  
				"         TU.DESCRIZIONE, " +  
				"         RCM.FLAG_SAU " +
				"ORDER BY RCM.FLAG_SAU DESC, " +
				"         TU.DESCRIZIONE ASC";

			SolmrLogger.debug(this, "Value of parameter 1 [ID_AZIENDA] in riepilogoUsoSecondario method in UtilizzoParticellaDAO: "+idAzienda+"\n");

			stmt = conn.prepareStatement(query);
			
			stmt.setLong(1, idAzienda.longValue());

			SolmrLogger.debug(this, "Executing riepilogoUsoSecondario: "+query+"\n");

			ResultSet rs = stmt.executeQuery();

			while(rs.next()) {
				UtilizzoParticellaVO utilizzoParticellaVO = new UtilizzoParticellaVO();
				utilizzoParticellaVO.setIdUtilizzoSecondario(new Long(rs.getLong("ID_UTILIZZO")));
				TipoUtilizzoVO tipoUtilizzoSecondarioVO = new TipoUtilizzoVO();
				tipoUtilizzoSecondarioVO.setIdUtilizzo(new Long(rs.getLong("ID_UTILIZZO")));
				tipoUtilizzoSecondarioVO.setDescrizione(rs.getString("DESCRIZIONE"));
				tipoUtilizzoSecondarioVO.setFlagSau(rs.getString("FLAG_SAU"));
				utilizzoParticellaVO.setTipoUtilizzoSecondarioVO(tipoUtilizzoSecondarioVO);
				utilizzoParticellaVO.setSupUtilizzataSecondaria(rs.getString("SUP_UTILIZZATA_SEC"));
				elencoUtilizzoParticellaVO.add(utilizzoParticellaVO);
			}
			
			rs.close();
			stmt.close();

		}
		catch(SQLException exc) {
			SolmrLogger.error(this, "riepilogoUsoSecondario in UtilizzoParticellaDAO - SQLException: "+exc+"\n");
			throw new DataAccessException(exc.getMessage());
		}
		catch(Exception ex) {
			SolmrLogger.error(this, "riepilogoUsoSecondario in UtilizzoParticellaDAO - Generic Exception: "+ex+"\n");
			throw new DataAccessException(ex.getMessage());
		}
		finally {
			try {
				if(stmt != null) stmt.close();
				if(conn != null) conn.close();
			}
			catch(SQLException exc) {
				SolmrLogger.error(this, "riepilogoUsoSecondario in UtilizzoParticellaDAO - SQLException while closing Statement and Connection: "+exc+"\n");
				throw new DataAccessException(exc.getMessage());
			}
			catch(Exception ex) {
				SolmrLogger.error(this, "riepilogoUsoSecondario in UtilizzoParticellaDAO - Generic Exception while closing Statement and Connection: "+ex+"\n");
				throw new DataAccessException(ex.getMessage());
			}
		}
		SolmrLogger.debug(this, "Invocated riepilogoUsoSecondario method in UtilizzoParticellaDAO\n");
		if(elencoUtilizzoParticellaVO.size() == 0) {
			return (UtilizzoParticellaVO[])elencoUtilizzoParticellaVO.toArray(new UtilizzoParticellaVO[0]);
		}
		else {
			return (UtilizzoParticellaVO[])elencoUtilizzoParticellaVO.toArray(new UtilizzoParticellaVO[elencoUtilizzoParticellaVO.size()]);
		}
	}
	
	
	/**
	 * 
	 * restituisce le superfici che non sono state
	 * associate a nessun uso del suolo e se selezioinato anche quelle
	 * relative all'asservimento
	 * 
	 * 
	 * @param idAzienda
	 * @param escludiAsservimento
	 * @return
	 * @throws DataAccessException
	 */
	public BigDecimal getTotSupSfriguAndAsservimento(Long idAzienda, String escludiAsservimento) 
	  throws DataAccessException 
  {
    SolmrLogger.debug(this, "Invocating getTotSupSfriguAndAsservimento method in UtilizzoParticellaDAO\n");
    Connection conn = null;
    PreparedStatement stmt = null;
    BigDecimal result = null;

    try 
    {
      SolmrLogger.debug(this, "Creating db-connection in getTotSupSfriguAndAsservimento method in UtilizzoParticellaDAO\n");
      conn = getDatasource().getConnection();
      SolmrLogger.debug(this, "Created db-connection in getTotSupSfriguAndAsservimento method in UtilizzoParticellaDAO and it values: "+conn+"\n");

      String query = "" +
      	"SELECT NVL(SUM(DECODE(COM.ID_TITOLO_POSSESSO,5,COM.SUP_COND,COM.SUP_UTILIZZATA)),0) AS SUP_UTILIZZATA " +
        "FROM " +
        "    (SELECT SP.ID_PARTICELLA, " +
        "           SP.SUPERFICIE_GRAFICA, " +
        "           SP.SUP_CATASTALE, " +
        "           CP.SUPERFICIE_CONDOTTA SUP_COND, " +
        "           CP.ID_CONDUZIONE_PARTICELLA, " +
        "           CP.ID_TITOLO_POSSESSO, " +
        "           CP.PERCENTUALE_POSSESSO, " +
        "           DECODE(CP.ID_TITOLO_POSSESSO, 5 , CP.SUPERFICIE_CONDOTTA, " +
        "             TRUNC(LEAST(DECODE(SP.SUPERFICIE_GRAFICA,0,SP.SUP_CATASTALE,SP.SUPERFICIE_GRAFICA), " + 
        "                       DECODE(SP.SUP_CATASTALE,0,SP.SUPERFICIE_GRAFICA,SP.SUP_CATASTALE)) * CP.PERCENTUALE_POSSESSO / 100,4) " +
        "           - TRUNC(SUM(NVL(UP.SUPERFICIE_UTILIZZATA,0)),4)) SUP_UTILIZZATA " +
        "    FROM   DB_CONDUZIONE_PARTICELLA CP, " +
        "           DB_UTE U, " +
        "           DB_STORICO_PARTICELLA SP, " +
        "           DB_UTILIZZO_PARTICELLA UP " +
        "    WHERE  CP.ID_UTE = U.ID_UTE " +
        "    AND    U.ID_AZIENDA = ? " +
        "    AND    CP.ID_PARTICELLA = SP.ID_PARTICELLA " +
        "    AND    CP.ID_CONDUZIONE_PARTICELLA = UP.ID_CONDUZIONE_PARTICELLA(+)";
      if(Validator.isNotEmpty(escludiAsservimento) 
          && escludiAsservimento.equalsIgnoreCase(SolmrConstants.FLAG_S)) 
      {
        query +=   "" +
          "  AND    CP.ID_TITOLO_POSSESSO <> "+SolmrConstants.ID_TITOLO_POSSESSO_ASSERVIMENTO;
      } 
      query += "" +
        "    AND    CP.DATA_FINE_CONDUZIONE IS NULL " +
        "    AND    SP.DATA_FINE_VALIDITA   IS NULL " +
        "    AND    U.DATA_FINE_ATTIVITA IS NULL " +
        "    GROUP BY SP.ID_PARTICELLA, " +
        "             SP.SUPERFICIE_GRAFICA, " +
        "             SP.SUP_CATASTALE, " +
        "             CP.SUPERFICIE_CONDOTTA, " + 
        "             CP.ID_CONDUZIONE_PARTICELLA, " + 
        "             CP.ID_TITOLO_POSSESSO, " +
        "             CP.PERCENTUALE_POSSESSO " +
        "    HAVING DECODE(CP.ID_TITOLO_POSSESSO, 5 , CP.SUPERFICIE_CONDOTTA, TRUNC(LEAST(DECODE(SP.SUPERFICIE_GRAFICA,0,SP.SUP_CATASTALE,SP.SUPERFICIE_GRAFICA), " +
        "                DECODE(SP.SUP_CATASTALE,0,SP.SUPERFICIE_GRAFICA,SP.SUP_CATASTALE)) * CP.PERCENTUALE_POSSESSO / 100,4) " +
        "          - TRUNC(SUM(NVL(UP.SUPERFICIE_UTILIZZATA,0)),4)) > 0) COM";

      SolmrLogger.debug(this, "Value of parameter 1 [ID_AZIENDA] in getTotSupSfriguAndAsservimento method in UtilizzoParticellaDAO: "+idAzienda+"\n");
      SolmrLogger.debug(this, "Value of parameter 2 [ESCLUDI_ASSERVIMENTO] in getTotSupSfriguAndAsservimento method in UtilizzoParticellaDAO: "+escludiAsservimento+"\n");
      

      stmt = conn.prepareStatement(query);
      
      stmt.setLong(1, idAzienda.longValue());

      SolmrLogger.debug(this, "Executing getTotSupSfriguAndAsservimento: "+query+"\n");

      ResultSet rs = stmt.executeQuery();

      if(rs.next()) 
      {
        result = rs.getBigDecimal("SUP_UTILIZZATA");
      }
      
      rs.close();
      stmt.close();

    }
    catch(SQLException exc) {
      SolmrLogger.error(this, "getTotSupSfriguAndAsservimento in UtilizzoParticellaDAO - SQLException: "+exc+"\n");
      throw new DataAccessException(exc.getMessage());
    }
    catch(Exception ex) {
      SolmrLogger.error(this, "getTotSupSfriguAndAsservimento in UtilizzoParticellaDAO - Generic Exception: "+ex+"\n");
      throw new DataAccessException(ex.getMessage());
    }
    finally {
      try {
        if(stmt != null) stmt.close();
        if(conn != null) conn.close();
      }
      catch(SQLException exc) {
        SolmrLogger.error(this, "getTotSupSfriguAndAsservimento in UtilizzoParticellaDAO - SQLException while closing Statement and Connection: "+exc+"\n");
        throw new DataAccessException(exc.getMessage());
      }
      catch(Exception ex) {
        SolmrLogger.error(this, "getTotSupSfriguAndAsservimento in UtilizzoParticellaDAO - Generic Exception while closing Statement and Connection: "+ex+"\n");
        throw new DataAccessException(ex.getMessage());
      }
    }
    SolmrLogger.debug(this, "Invocated getTotSupSfriguAndAsservimento method in UtilizzoParticellaDAO\n");
    
    return result;
  }
	
	
	
	/*public Vector<ParticellaEfaVO> getElencoParticelleEfaByUtilizzo(long idUtilizzoParticella) 
	    throws DataAccessException 
	{
    SolmrLogger.debug(this, "Invocating getElencoParticelleEfaByUtilizzo method in UtilizzoParticellaDAO\n");
    Connection conn = null;
    PreparedStatement stmt = null;
    Vector<ParticellaEfaVO> vParticellaEfa = null;
    ParticellaEfaVO particellaEfaVO = null;
    
    try 
    {
      SolmrLogger.debug(this, "Creating db-connection in getElencoParticelleEfaByUtilizzo method in UtilizzoParticellaDAO\n");
      conn = getDatasource().getConnection();
      SolmrLogger.debug(this, "Created db-connection in getElencoParticelleEfaByUtilizzo method in UtilizzoParticellaDAO and it values: "+conn+"\n");

      String query = "" +
        "SELECT PE.ID_PARTICELLA_EFA, " +
        "       PE.ID_AZIENDA_EFA, " +
        "       PE.ID_UTILIZZO_PARTICELLA, " +
        "       PE.VALORE_ORIGINALE, " +
        "       PE.VALORE_DOPO_CONVERSIONE, " +
        "       PE.VALORE_DOPO_PONDERAZIONE, " +
        "       AE.ID_TIPO_EFA, " +
        "       UM.DESCRIZIONE AS DESC_UNITA_MISURA " +
        "FROM   DB_PARTICELLA_EFA PE, " +
        "       DB_AZIENDA_EFA AE, " +
        "       DB_TIPO_EFA TE, " +
        "       DB_UNITA_MISURA UM " +
        "WHERE  PE.ID_UTILIZZO_PARTICELLA = ? " +
        "AND    PE.DATA_FINE_VALIDITA IS NULL " +    
        "AND    PE.ID_AZIENDA_EFA = AE.ID_AZIENDA_EFA " +
        "AND    AE.DATA_FINE_VALIDITA IS NULL " +
        "AND    AE.ID_TIPO_EFA = TE.ID_TIPO_EFA " +
        "AND    TE.ID_UNITA_MISURA = UM.ID_UNITA_MISURA " +
        "ORDER BY TE.DESCRIZIONE_TIPO_EFA ";

      SolmrLogger.debug(this, "Value of parameter 1 [ID_UTILIZZO_PARTICELLA] in getElencoParticelleEfaByUtilizzo method in UtilizzoParticellaDAO: "+idUtilizzoParticella+"\n");

      stmt = conn.prepareStatement(query);
      
      stmt.setLong(1, idUtilizzoParticella);

      SolmrLogger.debug(this, "Executing getElencoParticelleEfaByUtilizzo: "+query+"\n");

      ResultSet rs = stmt.executeQuery();

      while(rs.next()) 
      {
        if(vParticellaEfa == null)
          vParticellaEfa = new Vector<ParticellaEfaVO>();
        
        particellaEfaVO = new ParticellaEfaVO();
        particellaEfaVO.setIdUtilizzoParticella(rs.getLong("ID_UTILIZZO_PARTICELLA"));
        particellaEfaVO.setIdAziendaEfa(rs.getLong("ID_AZIENDA_EFA"));
        particellaEfaVO.setIdParticellaEfa(rs.getLong("ID_PARTICELLA_EFA"));
        particellaEfaVO.setValoreOriginale(rs.getBigDecimal("VALORE_ORIGINALE"));
        particellaEfaVO.setValoreOriginaleStr(Formatter.formatDouble(particellaEfaVO.getValoreOriginale()));
        particellaEfaVO.setValoreDopoConversione(rs.getBigDecimal("VALORE_DOPO_CONVERSIONE"));
        particellaEfaVO.setValoreDopoConversioneStr(Formatter.formatDouble(particellaEfaVO.getValoreDopoConversione()));
        particellaEfaVO.setValoreDopoPonderazione(rs.getBigDecimal("VALORE_DOPO_PONDERAZIONE"));
        particellaEfaVO.setValoreDopoPonderazioneStr(Formatter.formatDouble(particellaEfaVO.getValoreDopoPonderazione()));
        particellaEfaVO.setIdTipoEfa(rs.getLong("ID_TIPO_EFA"));
        particellaEfaVO.setDescUnitaMisura(rs.getString("DESC_UNITA_MISURA"));
        
        vParticellaEfa.add(particellaEfaVO);
      }

      rs.close();
      stmt.close();

    }
    catch(SQLException exc) {
      SolmrLogger.error(this, "getElencoParticelleEfaByUtilizzo in UtilizzoParticellaDAO - SQLException: "+exc.getMessage()+"\n");
      throw new DataAccessException(exc.getMessage());
    }
    catch(Exception ex) {
      SolmrLogger.error(this, "getElencoParticelleEfaByUtilizzo in UtilizzoParticellaDAO - Generic Exception: "+ex+"\n");
      throw new DataAccessException(ex.getMessage());
    }
    finally {
      try {
        if(stmt != null) stmt.close();
        if(conn != null) conn.close();
      }
      catch(SQLException exc) {
        SolmrLogger.error(this, "getElencoParticelleEfaByUtilizzo in UtilizzoParticellaDAO - SQLException while closing Statement and Connection: "+exc.getMessage()+"\n");
        throw new DataAccessException(exc.getMessage());
      }
      catch(Exception ex) {
        SolmrLogger.error(this, "getElencoParticelleEfaByUtilizzo in UtilizzoParticellaDAO - Generic Exception while closing Statement and Connection: "+ex.getMessage()+"\n");
        throw new DataAccessException(ex.getMessage());
      }
    }
    SolmrLogger.debug(this, "Invocated getElencoParticelleEfaByUtilizzo method in UtilizzoParticellaDAO\n");
    
    return vParticellaEfa;
  }*/
	
	
	
}
