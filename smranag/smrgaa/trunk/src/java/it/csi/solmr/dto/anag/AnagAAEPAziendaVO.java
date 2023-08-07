package it.csi.solmr.dto.anag;

/**
 * <p>Title: S.O.L.M.R.</p>
 * <p>Description: Servizi On-Line per il Mondo Rurale</p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: TOBECONFIG</p>
 * @author
 * @version 1.0
 */

import it.csi.smranag.smrgaa.dto.anagrafe.AziendaAtecoSecVO;
import it.csi.smranag.smrgaa.dto.anagrafe.AziendaSezioniVO;
import it.csi.smrcomms.reportdin.util.Validator;
import it.csi.solmr.dto.CodeDescription;
import it.csi.solmr.util.DateUtils;
import it.csi.solmr.ws.infoc.Azienda;
import it.csi.solmr.ws.infoc.ListaSedi;
import it.csi.solmr.ws.infoc.RappresentanteLegale;
import it.csi.solmr.ws.infoc.Sede;

import java.io.Serializable;
import java.util.List;
import java.util.Vector;

public class AnagAAEPAziendaVO implements Serializable
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 6592312959733544089L;
	private String colorUguali;
	private String colorDiversi;
	private String codFisAnagrafe;
	private String codFisAAEP;
	private String denominazioneAAEP;
	private String denominazioneAnagrafe;
	private boolean denominazioneUguale;
	private String partitaIVAAAEP;
	private String partitaIVAAnagrafe;
	private boolean partitaIVAUguale;
	private String formaGiuridicaAAEP;
	private String formaGiuridicaAnagrafe;
	private String idAtecoAAEP;
	private CodeDescription atecoAAEP;
	private String idAtecoAnagrafe;
	private boolean idAtecoUguale;
	private String descrizioneAtecoAAEP;
	private String descrizioneAtecoAnagrafe;
	private String provinciaREAAAEP;
	private String provinciaREAAnagrafe;
	private boolean provinciaREAUguale;
	private String numeroREAAAEP;
	private String numeroREAAnagrafe;
	private boolean numeroREAUguale;
	private String annoIscrizioneAAEP;
	private String annoIscrizioneAnagrafe;
	private boolean annoIscrizioneUguale;
	private String numeroRegistroImpreseAAEP;
	private String numeroRegistroImpreseAnagrafe;
	private boolean numeroRegistroImpreseUguale;
	private String dataFondazioneAAEP;
	private String dataCessazioneAAEP;
	private String dataCessazioneAnagrafe;
	private boolean dataCessazioneUguale;
	private String causaleCessazioneAAEP;
	private String causaleCessazioneAnagrafe;
	private String sedeLegaleIndirizzoAAEP;
	private String sedeLegaleIndirizzoAnagrafe;
	private String sedeLegaleComuneAAEP;
	private String sedeLegaleComuneAnagrafe;
	private String sedeLegaleCAPAAEP;
	private String sedeLegaleCAPAnagrafe;
	private String sedeLegaleEmailAAEP;
	private String sedeLegaleEmailAnagrafe;
	private boolean sedeLegaleIndirizzoUguale;
	private boolean sedeLegaleComuneUguale;
	private boolean sedeLegaleCAPUguale;
	private boolean sedeLegaleEmailUguale;
	private Sede sedeAAEP;
	private String sedeLegaleIstatAAEP;
	private String sedeLegaleIstatAnagrafe;
	private String rapLegCodFisAAEP;
	private String rapLegCognomeAAEP;
	private String rapLegNomeAAEP;
	private String rapLegSessoAAEP;
	private String rapLegCodFisAnagrafe;
	private String rapLegCognomeAnagrafe;
	private String rapLegNomeAnagrafe;
	private String rapLegSessoAnagrafe;
	private boolean rapLegCodFisUguale;
	private boolean rapLegCognomeUguale;
	private boolean rapLegNomeUguale;
	private boolean rapLegSessoUguale;
	private String rapLegDataNascitaAAEP;
	private String rapLegLuogoNascitaAAEP;
	private String rapLegResidenzaAAEP;
	private String rapLegCapResidenzaAAEP;
	private String rapLegDataNascitaAnagrafe;
	private String rapLegLuogoNascitaAnagrafe;
	private String rapLegResidenzaAnagrafe;
	private boolean rapLegDataNascitaUguale;
	private boolean rapLegLuogoNascitaUguale;
	private boolean rapLegResidenzaUguale;
	//private RappresentanteLegaleAAEP rappresentanteLegaleAAEP;
	private RappresentanteLegale rappresentanteLegaleAAEP;
	private String rapLegResidenzaComuneDesc;
	private String rapLegNascitaComuneDesc;
	private String dataInizioValiditaAAEP;
	private boolean formaGiuridicaUguale;
	private boolean pecUguale;
	private String pecAAEP;
	private String pecAnagrafe;
	private String sezioneAAEP;
	private String sezioneAnagrafe;
	private String coltivatoreDirettoAAEP;
	private String coltivatoreDirettoAnagrafe;
	private String descrizioneAtecoSecAAEP;
	private String descrizioneAtecoSecAnagrafe;
	private String dataIscrizioneREAAAEP;
	private String dataIscrizioneREAAnagrafe;
	private boolean dataIscrizioneREAUguale;
	private String dataCancellazioneREAAAEP;
	private String dataCancellazioneREAAnagrafe;
	private boolean dataCancellazioneREAUguale;
	private String dataIscrizioneRIAAEP;
	private String dataIscrizioneRIAnagrafe;
	private boolean dataIscrizioneRIUguale;
	private String statoImpresaAAEP;
	private String vigenzaAAEP;
	private Vector<CodeDescription> vAtecoSecAAEP;
	private boolean atecoSecUguale;
	private Vector<TipoSezioniAaepVO> vTipoSezAAEP;
  private boolean tipoSezioniUguale;
  private String descrizioneTipoSezioneAAEP;
  private String descrizioneTipoSezioneAnagrafe;
	

	public AnagAAEPAziendaVO()
	{
		colorUguali="nero";
		colorDiversi="rosso";
	}

	public static ListaSedi estraiSedeLegaleAAEP(List<ListaSedi> vetSedi)
	{
		if (vetSedi!=null)
			for (int i=0;i<vetSedi.size();i++)
				if (vetSedi.get(i)!= null)
					// **** la sede legale ha il progrSede sempre settato a 0
					if ("0".equalsIgnoreCase(vetSedi.get(i).getProgrSede().getValue()))
						return vetSedi.get(i);
		return null;
	}

	@SuppressWarnings("unchecked")
  public void loadAnagAAEPAziendaVO(Azienda aziendaAAEP,
			AnagAziendaVO anagAziendaVO,
			PersonaFisicaVO personaVO)
	{
		/*try
		{
			this.setDataInizioValiditaAAEP(DateUtils.formatDate(aziendaAAEP.getDataInizioValidita()));
		}
		catch(Exception e) {}*/

		//     Codice fiscale azienda
		//  AziendaAAEP.codicefiscale
		this.setCodFisAAEP(aziendaAAEP.getCodiceFiscale().getValue());
		//  Db_anagrafica_azienda.cuaa
		this.setCodFisAnagrafe(anagAziendaVO.getCUAA());

		//      Denominazione azienda
		//  AziendaAAEP.denominazione
		// Prelevo solo i 1000 caratteri da AAEP e faccio la trim del campo
		if(aziendaAAEP.getRagioneSociale().getValue().length() > 1000) {
			this.setDenominazioneAAEP(eliminaSpazi(aziendaAAEP.getRagioneSociale().getValue().substring(0, 1000).trim()));
		}
		else {
			this.setDenominazioneAAEP(eliminaSpazi(aziendaAAEP.getRagioneSociale().getValue()));
		}
		//  Db_anagrafica_azienda.denominazione
		this.setDenominazioneAnagrafe(anagAziendaVO.getDenominazione());
		this.denominazioneUguale=confrontaStr(denominazioneAAEP,denominazioneAnagrafe);

		//      Partita iva azienda
		//  AziendaAAEP.partitiva
		this.setPartitaIVAAAEP(aziendaAAEP.getPartitaIva().getValue());
		//  Db_anagrafica_azienda. PARTITA_IVA
		this.setPartitaIVAAnagrafe(anagAziendaVO.getPartitaIVA());
		this.partitaIVAUguale=confrontaStr(partitaIVAAAEP,partitaIVAAnagrafe);

		//      Forma giuridica
		//  AziendaAAEP.descrizioneNaturaGiuridca
		//La descrizione di AAEP viene setta nel controller che chiama la pagina
		//  Db_tipo_forma_giuridica.descrizione a fronte di db_anagrafica_azienda.id_forma_giuridica
		if (anagAziendaVO.getTipoFormaGiuridica()!=null)
			this.setFormaGiuridicaAnagrafe(anagAziendaVO.getTipoFormaGiuridica().getDescription());
		else
			this.setFormaGiuridicaAnagrafe("");
		this.formaGiuridicaUguale=confrontaStr(formaGiuridicaAAEP,formaGiuridicaAnagrafe);



		//  Attività prevalente (Codice Ateco)
		//  AziendaAAEP.idAteco2007 + AziendaAAEP.descrizioneAteco2007
		/*String codAteco = aziendaAAEP.getCodATECO2007();
		if(Validator.isNotEmpty(codAteco))
		{
		  codAteco = codAteco.replaceAll("\\.", "");
		}
		this.setIdAtecoAAEP(codAteco);
		this.setDescrizioneAtecoAAEP(convertNull(codAteco) +" - " +convertNull(aziendaAAEP.getDescrATECO2007()));*/
		//  db_tipo_attivita_ateco.codice a fronte di db_anagrafica_azienda.id_attività_ateco
		if (anagAziendaVO.getTipoAttivitaATECO()!=null)
		{
			String tempId="",tempDesc="";
			if (anagAziendaVO.getTipoAttivitaATECO().getSecondaryCode()!=null)
				tempId=anagAziendaVO.getTipoAttivitaATECO().getSecondaryCode().toString();
			if (anagAziendaVO.getTipoAttivitaATECO().getDescription()!=null)
				tempDesc=anagAziendaVO.getTipoAttivitaATECO().getDescription().toString();
			this.setIdAtecoAnagrafe(tempId);
			
			if (Validator.isNotEmpty(tempId))
				this.setDescrizioneAtecoAnagrafe("("+tempId+") "+tempDesc);
			
		}
		else
		{
			this.setIdAtecoAnagrafe("");
			this.setDescrizioneAtecoAnagrafe("");
		}
		//  Confrontare solo AziendaAAEP.idAteco9 con db_tipo_attivita_ateco.codice a
		//  fronte di db_anagrafica_azienda.id_attività_ateco
		this.idAtecoUguale = confrontaStr(idAtecoAAEP, idAtecoAnagrafe);
		
		
		if(anagAziendaVO.getVAziendaATECOSec() != null)
		{
		  String atecoSecAnag = "";
		  for(int i=0;i<anagAziendaVO.getVAziendaATECOSec().size();i++)
      {
		    if(i!=0)
		      atecoSecAnag += " - ";
		    AziendaAtecoSecVO aziendaAtecoSecVO = (AziendaAtecoSecVO)anagAziendaVO.getVAziendaATECOSec().get(i);
        atecoSecAnag += "("+aziendaAtecoSecVO.getCodAttivitaAteco()+") "+aziendaAtecoSecVO.getDescAttivitaAteco();
      }
		  this.setDescrizioneAtecoSecAnagrafe(atecoSecAnag);
		}
		this.atecoSecUguale = confrontaAtecoSec(vAtecoSecAAEP, anagAziendaVO.getVAziendaATECOSec());
		
		if(anagAziendaVO.getvAziendaSezioni() != null)
    {
      String sezioneAnag = "";
      for(int i=0;i<anagAziendaVO.getvAziendaSezioni().size();i++)
      {
        if(i!=0)
          sezioneAnag += " - ";
        AziendaSezioniVO aziendaSezioniVO = (AziendaSezioniVO)anagAziendaVO.getvAziendaSezioni().get(i);
        sezioneAnag += aziendaSezioniVO.getDescrizione();
      }
      this.setDescrizioneTipoSezioneAnagrafe(sezioneAnag);
    }
    this.tipoSezioniUguale = confrontaSezioni(vTipoSezAAEP, anagAziendaVO.getvAziendaSezioni());
		
		
		
		
		


		//     Provincia rea
		//  AziendaAAEP.provinciacciaa
		//La provincia REA viene caricata nel controller perchè il servizio AAEP
		//restituisce solo l'istat, mentre a noi serve la sigla
		//  Db_anagrafica_azienda. CCIAA_PROVINCIA_REA
		this.setProvinciaREAAnagrafe(anagAziendaVO.getCCIAAprovREA());
		this.provinciaREAUguale=confrontaStr(provinciaREAAAEP,provinciaREAAnagrafe);

		//     Numero rea
		//  AziendaAAEP.numerocciaa
		this.setNumeroREAAAEP(aziendaAAEP.getNumeroCCIAA().getValue());
		//  Db_anagrafica_azienda.
		if (anagAziendaVO.getCCIAAnumeroREA()!=null)
			this.setNumeroREAAnagrafe(anagAziendaVO.getCCIAAnumeroREA().toString());
		else this.setNumeroREAAnagrafe("");
		this.numeroREAUguale=confrontaStr(numeroREAAAEP,numeroREAAnagrafe);
		
		
		String dataIscrizioneAnagrafeREAStr = DateUtils.formatDateNotNull(anagAziendaVO.getDataIscrizioneRea());
    this.setDataIscrizioneREAAnagrafe(dataIscrizioneAnagrafeREAStr);
    this.dataIscrizioneREAUguale=confrontaStr(dataIscrizioneREAAAEP, dataIscrizioneREAAnagrafe);
    
    String dataCancellazioneAnagrafeREAStr = DateUtils.formatDateNotNull(anagAziendaVO.getDataCessazioneRea());
    this.setDataCancellazioneREAAnagrafe(dataCancellazioneAnagrafeREAStr);
    this.dataCancellazioneREAUguale=confrontaStr(dataCancellazioneREAAAEP, dataCancellazioneREAAnagrafe);

    String dataIscrizioneAnagrafeRIStr = DateUtils.formatDateNotNull(anagAziendaVO.getDataIscrizioneRi());
    this.setDataIscrizioneRIAnagrafe(dataIscrizioneAnagrafeRIStr);
    this.dataIscrizioneRIUguale=confrontaStr(dataIscrizioneRIAAEP, dataIscrizioneRIAnagrafe);
    
    
		//     Anno iscrizione registro imprese
		//  AziendaAAEP.annocciaa
		//this.setAnnoIscrizioneAAEP(aziendaAAEP.getAnnoCCIAA());
		//  Db_anagrafica_azienda. CCIAA_ANNO_ISCRIZIONE
		this.setAnnoIscrizioneAnagrafe(anagAziendaVO.getCCIAAannoIscrizione());
		this.annoIscrizioneUguale=confrontaStr(annoIscrizioneAAEP,annoIscrizioneAnagrafe);

		//     Numero registro imprese
		//  AziendaAAEP.nRegistroImprese
		this.setNumeroRegistroImpreseAAEP(aziendaAAEP.getNRegistroImpreseCCIAA().getValue());
		//  Db_anagrafica_azienda. CCIAA_NUMERO_REGISTRO_IMPRESE
		this.setNumeroRegistroImpreseAnagrafe(anagAziendaVO.getCCIAAnumRegImprese());
		this.numeroRegistroImpreseUguale=confrontaStr(numeroRegistroImpreseAAEP,numeroRegistroImpreseAnagrafe);
	
    //  Db_anagrafica_azienda.pec
    this.setPecAnagrafe(anagAziendaVO.getPec());
    this.pecUguale=confrontaStr(pecAAEP, pecAnagrafe);

		//     Data fondazione
		//  AziendaAAEP.datacostituzioneazienda
		if (aziendaAAEP.getDataCostituzione()!=null)
		{
			try
			{
				this.setDataFondazioneAAEP(aziendaAAEP.getDataCostituzione().getValue());
			}
			catch(Exception e) {}
		}

		//     Data cessazione
		//  AziendaAAEP.data_cessazione
		if (aziendaAAEP.getDataCessazione()!=null)
		{
			try
			{
				this.setDataCessazioneAAEP(aziendaAAEP.getDataCessazione().getValue());
			}
			catch(Exception e) {}
		}
		else this.setDataCessazioneAAEP("");
		//  Db_anagrafica_azienda. DATA_CESSAZIONE
		if (anagAziendaVO.getDataCessazione()!=null)
		{
			try
			{
				this.setDataCessazioneAnagrafe(DateUtils.formatDate(anagAziendaVO.getDataCessazione()));
			}
			catch(Exception e) {}
		}
		else this.setDataCessazioneAnagrafe("");
		this.dataCessazioneUguale=confrontaStr(dataCessazioneAAEP,dataCessazioneAnagrafe);

		if (aziendaAAEP.getDataCessazione()!=null)
		{
			//     Causale cessazione
			//  AziendaAAEP.descrizionecausalecessazione
			this.setCausaleCessazioneAAEP(aziendaAAEP.getDescrizioneCausaleCessazione().getValue());
		}
		//  Db_anagrafica_azienda. causale_CESSAZIONE
		this.setCausaleCessazioneAnagrafe(anagAziendaVO.getCausaleCessazione());


		//     Indirizzo
		//  SedeAAEP.indirizzosede.
		if (sedeAAEP != null)
		{
			// Faccio la trim dell'indirizzo e prendo in considerazione solo i primi 100 caratteri
			if(sedeAAEP.getIndirizzoSede().getValue().length() > 100) {
				this.setSedeLegaleIndirizzoAAEP(sedeAAEP.getIndirizzoSede().getValue().substring(0, 100).trim());
			}
			else {
				this.setSedeLegaleIndirizzoAAEP(sedeAAEP.getIndirizzoSede().getValue());
			}
		}
		//  Db_anagrafica_azienda. SEDELEG_INDIRIZZO
		this.setSedeLegaleIndirizzoAnagrafe(anagAziendaVO.getSedelegIndirizzo());
		this.sedeLegaleIndirizzoUguale=confrontaStr(sedeLegaleIndirizzoAAEP,sedeLegaleIndirizzoAnagrafe);

		//     Comune
		//  Decodifica di SedeAAEP.codcomune
		if (sedeAAEP != null)
		{
			this.setSedeLegaleComuneAAEP(sedeAAEP.getNomeComune().getValue());
			this.setSedeLegaleIstatAAEP(sedeAAEP.getCodComune().getValue());
			this.setSedeLegaleCAPAAEP(sedeAAEP.getCap().getValue());
		}
		//  Decodifica di Db_anagrafica_azienda.sedeleg_comune
		if(anagAziendaVO.getSedelegEstero() == null || anagAziendaVO.getSedelegEstero().equals(""))
		{
			this.setSedeLegaleComuneAnagrafe(anagAziendaVO.getDescComune());
			this.setSedeLegaleIstatAnagrafe(anagAziendaVO.getSedelegComune());
		}
		else  this.setSedeLegaleComuneAnagrafe(anagAziendaVO.getStatoEstero());
		this.sedeLegaleComuneUguale=confrontaStr(sedeLegaleIstatAAEP,sedeLegaleIstatAnagrafe);

		//     Cap
		//  SedeAAEP.cap
		//spostao nella jsp per risolvere il MULTI
		//if (sedeAAEP != null)
			//this.setSedeLegaleCAPAAEP(sedeAAEP.getCap());
		
		//  Db_anagrafica_azienda. SEDELEG_CAP
		this.setSedeLegaleCAPAnagrafe(anagAziendaVO.getSedelegCAP());
		this.sedeLegaleCAPUguale=confrontaStr(sedeLegaleCAPAAEP,sedeLegaleCAPAnagrafe);

		//     E-mail
		//  SedeAAEP.email
		if (sedeAAEP != null)
			this.setSedeLegaleEmailAAEP(sedeAAEP.getEmail().getValue());
		//  Db_anagrafica_azienda. MAIL
		this.setSedeLegaleEmailAnagrafe(anagAziendaVO.getMail());
		this.sedeLegaleEmailUguale=confrontaStr(sedeLegaleEmailAAEP,sedeLegaleEmailAnagrafe);



		//     Codice fiscale
		//  RappresentanteLegaleAAEP.codicefiscale
		if (rappresentanteLegaleAAEP!=null)
			this.setRapLegCodFisAAEP(rappresentanteLegaleAAEP.getCodiceFiscale().getValue());
		//  Db_persona_fisica.codice_fiscale
		this.setRapLegCodFisAnagrafe(personaVO.getCodiceFiscale());
		this.rapLegCodFisUguale=confrontaStr(rapLegCodFisAAEP,rapLegCodFisAnagrafe);


		//     Cognome
		//  RappresentanteLegaleAAEP.cognome
		if (rappresentanteLegaleAAEP!=null)
			this.setRapLegCognomeAAEP(rappresentanteLegaleAAEP.getCognome().getValue());
		//  Db_persona_fisica.cognome
		this.setRapLegCognomeAnagrafe(personaVO.getCognome());
		this.rapLegCognomeUguale=confrontaStr(rapLegCognomeAAEP,rapLegCognomeAnagrafe);

		//     Nome
		//  RappresentanteLegaleAAEP.nome
		if (rappresentanteLegaleAAEP!=null)
			this.setRapLegNomeAAEP(rappresentanteLegaleAAEP.getNome().getValue());
		//  Db_persona_fisica.nome
		this.setRapLegNomeAnagrafe(personaVO.getNome());
		this.rapLegNomeUguale=confrontaStr(rapLegNomeAAEP,rapLegNomeAnagrafe);

		//     Sesso
		//  RappresentanteLegaleAAEP.sesso
		if (rappresentanteLegaleAAEP!=null)
			this.setRapLegSessoAAEP(rappresentanteLegaleAAEP.getSesso().getValue());
		//  Db_persona_fisica.sesso
		this.setRapLegSessoAnagrafe(personaVO.getSesso());
		this.rapLegSessoUguale=confrontaStr(rapLegSessoAAEP,rapLegSessoAnagrafe);

		//     Data nascita
		//  Db_persona_fisica.nascita_data
		if (rappresentanteLegaleAAEP!=null)
		{
			try
			{
				this.setRapLegDataNascitaAAEP(rappresentanteLegaleAAEP.getDataNascita().getValue());
			}
			catch(Exception e) {}
		}

		//  RappresentanteLegaleAAEP.datanascita
		try
		{
			this.setRapLegDataNascitaAnagrafe(DateUtils.formatDate(personaVO.getNascitaData()));
		}
		catch(Exception e) {}

		this.rapLegDataNascitaUguale=confrontaStr(rapLegDataNascitaAAEP,rapLegDataNascitaAnagrafe);

		//     Luogo nascita
		//  Decodifica di Db_persona_fisica.nascita_comune
		if (rappresentanteLegaleAAEP!=null)
			this.setRapLegLuogoNascitaAAEP(rapLegNascitaComuneDesc);
		//  Decodifica di RappresentanteLegaleAAEP.codicecomunenascita
		this.setRapLegLuogoNascitaAnagrafe(personaVO.getCittaNascita());
		this.rapLegLuogoNascitaUguale=confrontaStr(rappresentanteLegaleAAEP.getCodComuneNascita().getValue(),personaVO.getNascitaComune());

		//     Residenza	res_comune
		//  db_persona_fisica.res_indirizzo + db_persona_fisica.res_cap +Decodifica di db_persona_fisica.
		if (rappresentanteLegaleAAEP!=null)
			this.setRapLegResidenzaAAEP(
					convertNull(rappresentanteLegaleAAEP.getTipoVia().getValue())+" "+
					convertNull(rappresentanteLegaleAAEP.getIndirizzo().getValue())+" "+
					convertNull(rappresentanteLegaleAAEP.getNumeroCivico().getValue())+" "+
					convertNull(this.getRapLegCapResidenzaAAEP())+" "+
					convertNull(rapLegResidenzaComuneDesc)
			);
		//  RappresentanteLegaleAAEP.toponimo + indirizzo + numero civico + cap + decodifica codicecomuneresidenza

		if(personaVO.getStatoEsteroRes()==null||personaVO.getStatoEsteroRes().equals("")){
			this.setRapLegResidenzaAnagrafe(convertNull(personaVO.getResIndirizzo())+" "+
					convertNull(personaVO.getResCAP())+" "+
					convertNull(personaVO.getDescResComune()));
		}
		else{
			this.setRapLegResidenzaAnagrafe(convertNull(personaVO.getResIndirizzo())+" "+
					convertNull(personaVO.getResCAP())+" "+
					convertNull(personaVO.getStatoEsteroRes())+" "+
					convertNull(personaVO.getResCittaEstero()));
		}
		if (rappresentanteLegaleAAEP!=null)
			if (confrontaStr(rappresentanteLegaleAAEP.getCap().getValue(),personaVO.getResCAP())
					&&
					confrontaStr(rappresentanteLegaleAAEP.getCodComuneResidenza().getValue(),personaVO.getResComune())
			)
				this.rapLegResidenzaUguale=true;
			else this.rapLegResidenzaUguale=false;
	}

	public String convertNull(String str)
	{
		if (str==null)
			return "";
		else return str;
	}

	private boolean confrontaStr(String str1,String str2)
	{
		if (str1==null)
		{
			if (str2==null || "".equals(str2.trim())) return true;
			else return false;
		}
		else
		{
			if ("".equals(str1.trim()))
			{
				if (str2==null || "".equals(str2.trim())) return true;
				else return false;
			}
			else
			{
				if (str2==null) return false;
				if (eliminaSpazi(str1.trim()).equalsIgnoreCase(eliminaSpazi(str2.trim()))) return true;
			}
			return false;
		}
	}
	
	private boolean confrontaAtecoSec(Vector<CodeDescription> vAtecoSecAAEP, 
	    Vector<AziendaAtecoSecVO> vAziendaAtecoSec)
	{
	  boolean uguali = false;
  	if((vAtecoSecAAEP != null)
      && (vAziendaAtecoSec != null))
    {
      if(vAtecoSecAAEP.size() == vAziendaAtecoSec.size())
      {
        Vector<Long> vIdAtecoTrib = new Vector<Long>();
        for(int i=0;i<vAtecoSecAAEP.size();i++)
        {
          vIdAtecoTrib.add(new Long(vAtecoSecAAEP.get(i).getCode().intValue()));
        }
        
        boolean trovato = true;
        for(int i=0;i<vAziendaAtecoSec.size();i++)
        {
          if(!vIdAtecoTrib.contains(new Long(vAziendaAtecoSec.get(i).getIdAttivitaAteco())))
          {
            trovato = false;
            break;
          }
        }
        
        if(trovato)         
          uguali = true;
      }
    }
  	
  	return uguali;
	}
	
	private boolean confrontaSezioni(Vector<TipoSezioniAaepVO> vSezioniAAEP, 
      Vector<AziendaSezioniVO> vAziendaSezioni)
  {
    boolean uguali = false;
    if((vSezioniAAEP != null)
      && (vAziendaSezioni != null))
    {
      if(vSezioniAAEP.size() == vAziendaSezioni.size())
      {
        Vector<Long> vIdTipoSezioniAaep = new Vector<Long>();
        for(int i=0;i<vSezioniAAEP.size();i++)
        {
          vIdTipoSezioniAaep.add(new Long(vSezioniAAEP.get(i).getIdTipoSezioniAaep()));
        }
        
        boolean trovato = true;
        for(int i=0;i<vAziendaSezioni.size();i++)
        {
          if(!vIdTipoSezioniAaep.contains(new Long(vAziendaSezioni.get(i).getIdTipoSezioniAaep())))
          {
            trovato = false;
            break;
          }
        }
        
        if(trovato)         
          uguali = true;
      }
    }
    
    return uguali;
  }

	public static String eliminaSpazi(String str)
	{
		StringBuffer result=new StringBuffer();
		if (str==null || str.equals("")) return "";
		int size=str.length();
		char old=str.charAt(0);
		boolean spazio=false;
		if (old==' ') spazio=true;
		if (!spazio) result.append(old);
		for (int i=1;i<size;i++)
		{
			if (!(spazio && str.charAt(i)==' ')) result.append(str.charAt(i));
			old=str.charAt(i);
			if (old==' ') spazio=true;
			else spazio=false;
		}
		//SolmrLogger.debug(this, "\n\n\n\nresult.toString() "+result.toString());
		return result.toString();
	}


	public void setCodFisAnagrafe(String codFisAnagrafe) {
		this.codFisAnagrafe = codFisAnagrafe;
	}
	public String getCodFisAnagrafe() {
		return codFisAnagrafe;
	}
	public void setCodFisAAEP(String codFisAAEP) {
		this.codFisAAEP = codFisAAEP;
	}
	public String getCodFisAAEP() {
		return codFisAAEP;
	}
	public void setDenominazioneAAEP(String denominazioneAAEP) {
		this.denominazioneAAEP = denominazioneAAEP;
	}
	public String getDenominazioneAAEP() {
		return denominazioneAAEP;
	}
	public void setDenominazioneAnagrafe(String denominazioneAnagrafe) {
		this.denominazioneAnagrafe = denominazioneAnagrafe;
	}
	public String getDenominazioneAnagrafe() {
		return denominazioneAnagrafe;
	}
	public boolean isDenominazioneUguale() {
		return denominazioneUguale;
	}
	public String getDenominazioneColor() {
		if (denominazioneUguale)
			return colorUguali;
		else
			return colorDiversi;
	}
	public void setPartitaIVAAAEP(String partitaIVAAAEP) {
		this.partitaIVAAAEP = partitaIVAAAEP;
	}
	public String getPartitaIVAAAEP() {
		return partitaIVAAAEP;
	}
	public void setPartitaIVAAnagrafe(String partitaIVAAnagrafe) {
		this.partitaIVAAnagrafe = partitaIVAAnagrafe;
	}
	public String getPartitaIVAAnagrafe() {
		return partitaIVAAnagrafe;
	}
	public boolean isPartitaIVAUguale() {
		return partitaIVAUguale;
	}
	public String getPartitaIVAColor() {
		if (partitaIVAUguale)
			return colorUguali;
		else
			return colorDiversi;
	}
	public void setFormaGiuridicaAAEP(String formaGiuridicaAAEP) {
		this.formaGiuridicaAAEP = formaGiuridicaAAEP;
	}
	public String getFormaGiuridicaAAEP() {
		return formaGiuridicaAAEP;
	}
	public void setFormaGiuridicaAnagrafe(String formaGiuridicaAnagrafe) {
		this.formaGiuridicaAnagrafe = formaGiuridicaAnagrafe;
	}
	public String getFormaGiuridicaAnagrafe() {
		return formaGiuridicaAnagrafe;
	}
	public void setIdAtecoAAEP(String idAtecoAAEP) {
		this.idAtecoAAEP = idAtecoAAEP;
	}
	public String getIdAtecoAAEP() {
		return idAtecoAAEP;
	}
	public void setIdAtecoAnagrafe(String idAtecoAnagrafe) {
		this.idAtecoAnagrafe = idAtecoAnagrafe;
	}
	public String getIdAtecoAnagrafe() {
		return idAtecoAnagrafe;
	}
	public boolean isIdAtecoUguale() {
		return idAtecoUguale;
	}
	public String getDescrizioneAtecoAAEP() {
		return descrizioneAtecoAAEP;
	}
	public String getDescrizioneAtecoAnagrafe() {
		return descrizioneAtecoAnagrafe;
	}
	public void setDescrizioneAtecoAAEP(String descrizioneAtecoAAEP) {
		this.descrizioneAtecoAAEP = descrizioneAtecoAAEP;
	}
	public void setDescrizioneAtecoAnagrafe(String descrizioneAtecoAnagrafe) {
		this.descrizioneAtecoAnagrafe = descrizioneAtecoAnagrafe;
	}
	public String getIdAtecoColor() {
		if (idAtecoUguale)
			return colorUguali;
		else
			return colorDiversi;
	}
	
	public String getAtecoSecColor() {
    if (atecoSecUguale)
      return colorUguali;
    else
      return colorDiversi;
  }
	
	public String getTipoSezioneAAEPColor() {
    if (tipoSezioniUguale)
      return colorUguali;
    else
      return colorDiversi;
  }
	
	public void setProvinciaREAAAEP(String provinciaREAAAEP) {
		this.provinciaREAAAEP = provinciaREAAAEP;
	}
	public String getProvinciaREAAAEP() {
		return provinciaREAAAEP;
	}
	public void setProvinciaREAAnagrafe(String provinciaREAAnagrafe) {
		this.provinciaREAAnagrafe = provinciaREAAnagrafe;
	}
	public String getProvinciaREAAnagrafe() {
		return provinciaREAAnagrafe;
	}
	public boolean isProvinciaREAUguale() {
		return provinciaREAUguale;
	}
	public String getProvinciaREAColor() {
		if (provinciaREAUguale)
			return colorUguali;
		else
			return colorDiversi;
	}
	public void setNumeroREAAAEP(String numeroREAAAEP) {
		this.numeroREAAAEP = numeroREAAAEP;
	}
	public String getNumeroREAAAEP() {
		return numeroREAAAEP;
	}
	public void setNumeroREAAnagrafe(String numeroREAAnagrafe) {
		this.numeroREAAnagrafe = numeroREAAnagrafe;
	}
	public String getNumeroREAAnagrafe() {
		return numeroREAAnagrafe;
	}
	public boolean isNumeroREAUguale() {
		return numeroREAUguale;
	}
	public String getNumeroREAColor() {
		if (numeroREAUguale)
			return colorUguali;
		else
			return colorDiversi;
	}
	public void setAnnoIscrizioneAAEP(String annoIscrizioneAAEP) {
		this.annoIscrizioneAAEP = annoIscrizioneAAEP;
	}
	public String getAnnoIscrizioneAAEP() {
		return annoIscrizioneAAEP;
	}
	public void setAnnoIscrizioneAnagrafe(String annoIscrizioneAnagrafe) {
		this.annoIscrizioneAnagrafe = annoIscrizioneAnagrafe;
	}
	public String getAnnoIscrizioneAnagrafe() {
		return annoIscrizioneAnagrafe;
	}
	public boolean isAnnoIscrizioneUguale() {
		return annoIscrizioneUguale;
	}
	public String getAnnoIscrizioneColor() {
		if (annoIscrizioneUguale)
			return colorUguali;
		else
			return colorDiversi;
	}
	public void setNumeroRegistroImpreseAAEP(String numeroRegistroImpreseAAEP) {
		this.numeroRegistroImpreseAAEP = numeroRegistroImpreseAAEP;
	}
	public String getNumeroRegistroImpreseAAEP() {
		return numeroRegistroImpreseAAEP;
	}
	public void setNumeroRegistroImpreseAnagrafe(String numeroRegistroImpreseAnagrafe) {
		this.numeroRegistroImpreseAnagrafe = numeroRegistroImpreseAnagrafe;
	}
	public String getNumeroRegistroImpreseAnagrafe() {
		return numeroRegistroImpreseAnagrafe;
	}
	public boolean isNumeroRegistroImpreseUguale() {
		return numeroRegistroImpreseUguale;
	}
	public String getNumeroRegistroImpreseColor() {
		if (numeroRegistroImpreseUguale)
			return colorUguali;
		else
			return colorDiversi;
	}
	
	public String getDataIscrizioneREAColor() {
    if (dataIscrizioneREAUguale)
      return colorUguali;
    else
      return colorDiversi;
  }
	
	public String getDataCancellazioneREAColor() {
    if (dataCancellazioneREAUguale)
      return colorUguali;
    else
      return colorDiversi;
  }
	
	public String getDataIscrizioneRIColor() {
    if (dataIscrizioneRIUguale)
      return colorUguali;
    else
      return colorDiversi;
  }
	
	
	public String getPecColor() {
    if (pecUguale)
      return colorUguali;
    else
      return colorDiversi;
  }
	public void setDataFondazioneAAEP(String dataFondazioneAAEP) {
		this.dataFondazioneAAEP = dataFondazioneAAEP;
	}
	public String getDataFondazioneAAEP() {
		return dataFondazioneAAEP;
	}
	public void setDataCessazioneAAEP(String dataCessazioneAAEP) {
		this.dataCessazioneAAEP = dataCessazioneAAEP;
	}
	public String getDataCessazioneAAEP() {
		return dataCessazioneAAEP;
	}
	public void setDataCessazioneAnagrafe(String dataCessazioneAnagrafe) {
		this.dataCessazioneAnagrafe = dataCessazioneAnagrafe;
	}
	public String getDataCessazioneAnagrafe() {
		return dataCessazioneAnagrafe;
	}
	public boolean isDataCessazioneUguale() {
		return dataCessazioneUguale;
	}
	public String getDataCessazioneColor() {
		if (dataCessazioneUguale)
			return colorUguali;
		else
			return colorDiversi;
	}
	public void setCausaleCessazioneAAEP(String causaleCessazioneAAEP) {
		this.causaleCessazioneAAEP = causaleCessazioneAAEP;
	}
	public String getCausaleCessazioneAAEP() {
		return causaleCessazioneAAEP;
	}
	public void setCausaleCessazioneAnagrafe(String causaleCessazioneAnagrafe) {
		this.causaleCessazioneAnagrafe = causaleCessazioneAnagrafe;
	}
	public String getCausaleCessazioneAnagrafe() {
		return causaleCessazioneAnagrafe;
	}
	public void setSedeLegaleIndirizzoAAEP(String sedeLegaleIndirizzoAAEP) {
		this.sedeLegaleIndirizzoAAEP = sedeLegaleIndirizzoAAEP;
	}
	public String getSedeLegaleIndirizzoAAEP() {
		return sedeLegaleIndirizzoAAEP;
	}
	public void setSedeLegaleIndirizzoAnagrafe(String sedeLegaleIndirizzoAnagrafe) {
		this.sedeLegaleIndirizzoAnagrafe = sedeLegaleIndirizzoAnagrafe;
	}
	public String getSedeLegaleIndirizzoAnagrafe() {
		return sedeLegaleIndirizzoAnagrafe;
	}
	public void setSedeLegaleComuneAAEP(String sedeLegaleComuneAAEP) {
		this.sedeLegaleComuneAAEP = sedeLegaleComuneAAEP;
	}
	public String getSedeLegaleComuneAAEP() {
		return sedeLegaleComuneAAEP;
	}
	public void setSedeLegaleComuneAnagrafe(String sedeLegaleComuneAnagrafe) {
		this.sedeLegaleComuneAnagrafe = sedeLegaleComuneAnagrafe;
	}
	public String getSedeLegaleComuneAnagrafe() {
		return sedeLegaleComuneAnagrafe;
	}
	public void setSedeLegaleCAPAAEP(String sedeLegaleCAPAAEP) {
		this.sedeLegaleCAPAAEP = sedeLegaleCAPAAEP;
	}
	public String getSedeLegaleCAPAAEP() {
		return sedeLegaleCAPAAEP;
	}
	public void setSedeLegaleCAPAnagrafe(String sedeLegaleCAPAnagrafe) {
		this.sedeLegaleCAPAnagrafe = sedeLegaleCAPAnagrafe;
	}
	public String getSedeLegaleCAPAnagrafe() {
		return sedeLegaleCAPAnagrafe;
	}
	public void setSedeLegaleEmailAAEP(String sedeLegaleEmailAAEP) {
		this.sedeLegaleEmailAAEP = sedeLegaleEmailAAEP;
	}
	public String getSedeLegaleEmailAAEP() {
		return sedeLegaleEmailAAEP;
	}
	public void setSedeLegaleEmailAnagrafe(String sedeLegaleEmailAnagrafe) {
		this.sedeLegaleEmailAnagrafe = sedeLegaleEmailAnagrafe;
	}
	public String getSedeLegaleEmailAnagrafe() {
		return sedeLegaleEmailAnagrafe;
	}
	public boolean isSedeLegaleIndirizzoUguale() {
		return sedeLegaleIndirizzoUguale;
	}
	public String getSedeLegaleIndirizzoColor() {
		if (sedeLegaleIndirizzoUguale)
			return colorUguali;
		else
			return colorDiversi;
	}
	public boolean isSedeLegaleComuneUguale() {
		return sedeLegaleComuneUguale;
	}
	public String getSedeLegaleComuneColor() {
		if (sedeLegaleComuneUguale)
			return colorUguali;
		else
			return colorDiversi;
	}
	public boolean isSedeLegaleCAPUguale() {
		return sedeLegaleCAPUguale;
	}
	public String getSedeLegaleCAPColor() {
		if (sedeLegaleCAPUguale)
			return colorUguali;
		else
			return colorDiversi;
	}
	public boolean isSedeLegaleEmailUguale() {
		return sedeLegaleEmailUguale;
	}
	public String getSedeLegaleEmailColor() {
		if (sedeLegaleEmailUguale)
			return colorUguali;
		else
			return colorDiversi;
	}
	public void setSedeAAEP(Sede sedeAAEP) {
		this.sedeAAEP = sedeAAEP;
	}
	public Sede getSedeAAEP() {
		return sedeAAEP;
	}
	public void setSedeLegaleIstatAAEP(String sedeLegaleIstatAAEP) {
		this.sedeLegaleIstatAAEP = sedeLegaleIstatAAEP;
	}
	public String getSedeLegaleIstatAAEP() {
		return sedeLegaleIstatAAEP;
	}
	public void setSedeLegaleIstatAnagrafe(String sedeLegaleIstatAnagrafe) {
		this.sedeLegaleIstatAnagrafe = sedeLegaleIstatAnagrafe;
	}
	public String getSedeLegaleIstatAnagrafe() {
		return sedeLegaleIstatAnagrafe;
	}
	public void setRapLegCodFisAAEP(String rapLegCodFisAAEP) {
		this.rapLegCodFisAAEP = rapLegCodFisAAEP;
	}
	public String getRapLegCodFisAAEP() {
		return rapLegCodFisAAEP;
	}
	public void setRapLegCognomeAAEP(String rapLegCognomeAAEP) {
		this.rapLegCognomeAAEP = rapLegCognomeAAEP;
	}
	public String getRapLegCognomeAAEP() {
		return rapLegCognomeAAEP;
	}
	public void setRapLegNomeAAEP(String rapLegNomeAAEP) {
		this.rapLegNomeAAEP = rapLegNomeAAEP;
	}
	public String getRapLegNomeAAEP() {
		return rapLegNomeAAEP;
	}
	public void setRapLegSessoAAEP(String rapLegSessoAAEP) {
		this.rapLegSessoAAEP = rapLegSessoAAEP;
	}
	public String getRapLegSessoAAEP() {
		return rapLegSessoAAEP;
	}
	public void setRapLegCodFisAnagrafe(String rapLegCodFisAnagrafe) {
		this.rapLegCodFisAnagrafe = rapLegCodFisAnagrafe;
	}
	public String getRapLegCodFisAnagrafe() {
		return rapLegCodFisAnagrafe;
	}
	public void setRapLegCognomeAnagrafe(String rapLegCognomeAnagrafe) {
		this.rapLegCognomeAnagrafe = rapLegCognomeAnagrafe;
	}
	public String getRapLegCognomeAnagrafe() {
		return rapLegCognomeAnagrafe;
	}
	public void setRapLegNomeAnagrafe(String rapLegNomeAnagrafe) {
		this.rapLegNomeAnagrafe = rapLegNomeAnagrafe;
	}
	public String getRapLegNomeAnagrafe() {
		return rapLegNomeAnagrafe;
	}
	public void setRapLegSessoAnagrafe(String rapLegSessoAnagrafe) {
		this.rapLegSessoAnagrafe = rapLegSessoAnagrafe;
	}
	public String getRapLegSessoAnagrafe() {
		return rapLegSessoAnagrafe;
	}
	public boolean isRapLegCodFisUguale() {
		return rapLegCodFisUguale;
	}
	public boolean isRapLegCognomeUguale() {
		return rapLegCognomeUguale;
	}
	public boolean isRapLegNomeUguale() {
		return rapLegNomeUguale;
	}
	public boolean isRapLegSessoUguale() {
		return rapLegSessoUguale;
	}
	public String getRapLegCodFisColor() {
		if (rapLegCodFisUguale)
			return colorUguali;
		else
			return colorDiversi;
	}
	public String getRapLegCognomeColor() {
		if (rapLegCognomeUguale)
			return colorUguali;
		else
			return colorDiversi;
	}
	public String getRapLegNomeColor() {
		if (rapLegNomeUguale)
			return colorUguali;
		else
			return colorDiversi;
	}
	public String getRapLegSessoColor() {
		if (rapLegSessoUguale)
			return colorUguali;
		else
			return colorDiversi;
	}
	public void setRapLegDataNascitaAAEP(String rapLegDataNascitaAAEP) {
		this.rapLegDataNascitaAAEP = rapLegDataNascitaAAEP;
	}
	public String getRapLegDataNascitaAAEP() {
		return rapLegDataNascitaAAEP;
	}
	public void setRapLegLuogoNascitaAAEP(String rapLegLuogoNascitaAAEP) {
		this.rapLegLuogoNascitaAAEP = rapLegLuogoNascitaAAEP;
	}
	public String getRapLegLuogoNascitaAAEP() {
		return rapLegLuogoNascitaAAEP;
	}
	public void setRapLegResidenzaAAEP(String rapLegResidenzaAAEP) {
		this.rapLegResidenzaAAEP = rapLegResidenzaAAEP;
	}
	public String getRapLegResidenzaAAEP() {
		return rapLegResidenzaAAEP;
	}
	public void setRapLegDataNascitaAnagrafe(String rapLegDataNascitaAnagrafe) {
		this.rapLegDataNascitaAnagrafe = rapLegDataNascitaAnagrafe;
	}
	public String getRapLegDataNascitaAnagrafe() {
		return rapLegDataNascitaAnagrafe;
	}
	public void setRapLegLuogoNascitaAnagrafe(String rapLegLuogoNascitaAnagrafe) {
		this.rapLegLuogoNascitaAnagrafe = rapLegLuogoNascitaAnagrafe;
	}
	public String getRapLegLuogoNascitaAnagrafe() {
		return rapLegLuogoNascitaAnagrafe;
	}
	public void setRapLegResidenzaAnagrafe(String rapLegResidenzaAnagrafe) {
		this.rapLegResidenzaAnagrafe = rapLegResidenzaAnagrafe;
	}
	public String getRapLegResidenzaAnagrafe() {
		return rapLegResidenzaAnagrafe;
	}
	public boolean isRapLegDataNascitaUguale() {
		return rapLegDataNascitaUguale;
	}
	public boolean isRapLegLuogoNascitaUguale() {
		return rapLegLuogoNascitaUguale;
	}
	public boolean isRapLegResidenzaUguale() {
		return rapLegResidenzaUguale;
	}
	public String getRapLegDataNascitaColor() {
		if (rapLegDataNascitaUguale)
			return colorUguali;
		else
			return colorDiversi;
	}
	public String getRapLegLuogoNascitaColor() {
		if (rapLegLuogoNascitaUguale)
			return colorUguali;
		else
			return colorDiversi;
	}
	public String getRapLegResidenzaColor() {
		if (rapLegResidenzaUguale)
			return colorUguali;
		else
			return colorDiversi;
	}
	public void setRappresentanteLegaleAAEP(RappresentanteLegale rappresentanteLegaleAAEP) {
		this.rappresentanteLegaleAAEP = rappresentanteLegaleAAEP;
	}
	public RappresentanteLegale getRappresentanteLegale() {
		return rappresentanteLegaleAAEP;
	}
	public void setRapLegResidenzaComuneDesc(String rapLegResidenzaComuneDesc) {
		this.rapLegResidenzaComuneDesc = rapLegResidenzaComuneDesc;
	}
	public String getRapLegResidenzaComuneDesc() {
		return rapLegResidenzaComuneDesc;
	}
	public void setRapLegNascitaComuneDesc(String rapLegNascitaComuneDesc) {
		this.rapLegNascitaComuneDesc = rapLegNascitaComuneDesc;
	}
	public String getRapLegNascitaComuneDesc() {
		return rapLegNascitaComuneDesc;
	}
	public void setDataInizioValiditaAAEP(String dataInizioValiditaAAEP) {
		this.dataInizioValiditaAAEP = dataInizioValiditaAAEP;
	}
	public String getDataInizioValiditaAAEP() {
		return dataInizioValiditaAAEP;
	}
	public boolean getFormaGiuridicaUguale() {
		return formaGiuridicaUguale;
	}
	public String getFormaGiuridicaColor() {
		if (formaGiuridicaUguale)
			return colorUguali;
		else
			return colorDiversi;
	}

	public CodeDescription getAtecoAAEP() {
		return atecoAAEP;
	}

	public void setAtecoAAEP(CodeDescription atecoAAEP) {
		this.atecoAAEP = atecoAAEP;
	}

  public String getPecAAEP()
  {
    return pecAAEP;
  }

  public void setPecAAEP(String pecAAEP)
  {
    this.pecAAEP = pecAAEP;
  }

  public String getPecAnagrafe()
  {
    return pecAnagrafe;
  }

  public void setPecAnagrafe(String pecAnagrafe)
  {
    this.pecAnagrafe = pecAnagrafe;
  }

  public String getRapLegCapResidenzaAAEP()
  {
    return rapLegCapResidenzaAAEP;
  }

  public void setRapLegCapResidenzaAAEP(String rapLegCapResidenzaAAEP)
  {
    this.rapLegCapResidenzaAAEP = rapLegCapResidenzaAAEP;
  }

  public String getSezioneAAEP()
  {
    return sezioneAAEP;
  }

  public void setSezioneAAEP(String sezioneAAEP)
  {
    this.sezioneAAEP = sezioneAAEP;
  }

  public String getSezioneAnagrafe()
  {
    return sezioneAnagrafe;
  }

  public void setSezioneAnagrafe(String sezioneAnagrafe)
  {
    this.sezioneAnagrafe = sezioneAnagrafe;
  }

  public String getColtivatoreDirettoAAEP()
  {
    return coltivatoreDirettoAAEP;
  }

  public void setColtivatoreDirettoAAEP(String coltivatoreDirettoAAEP)
  {
    this.coltivatoreDirettoAAEP = coltivatoreDirettoAAEP;
  }

  public String getDescrizioneAtecoSecAAEP()
  {
    return descrizioneAtecoSecAAEP;
  }

  public void setDescrizioneAtecoSecAAEP(String descrizioneAtecoSecAAEP)
  {
    this.descrizioneAtecoSecAAEP = descrizioneAtecoSecAAEP;
  }

  public String getDescrizioneAtecoSecAnagrafe()
  {
    return descrizioneAtecoSecAnagrafe;
  }

  public void setDescrizioneAtecoSecAnagrafe(String descrizioneAtecoSecAnagrafe)
  {
    this.descrizioneAtecoSecAnagrafe = descrizioneAtecoSecAnagrafe;
  }

  public String getDataIscrizioneREAAAEP()
  {
    return dataIscrizioneREAAAEP;
  }

  public void setDataIscrizioneREAAAEP(String dataIscrizioneREAAAEP)
  {
    this.dataIscrizioneREAAAEP = dataIscrizioneREAAAEP;
  }

  public String getDataIscrizioneREAAnagrafe()
  {
    return dataIscrizioneREAAnagrafe;
  }

  public void setDataIscrizioneREAAnagrafe(String dataIscrizioneREAAnagrafe)
  {
    this.dataIscrizioneREAAnagrafe = dataIscrizioneREAAnagrafe;
  }

  public String getDataCancellazioneREAAAEP()
  {
    return dataCancellazioneREAAAEP;
  }

  public void setDataCancellazioneREAAAEP(String dataCancellazioneREAAAEP)
  {
    this.dataCancellazioneREAAAEP = dataCancellazioneREAAAEP;
  }

  public String getDataCancellazioneREAAnagrafe()
  {
    return dataCancellazioneREAAnagrafe;
  }

  public void setDataCancellazioneREAAnagrafe(String dataCancellazioneREAAnagrafe)
  {
    this.dataCancellazioneREAAnagrafe = dataCancellazioneREAAnagrafe;
  }

  public String getDataIscrizioneRIAAEP()
  {
    return dataIscrizioneRIAAEP;
  }

  public void setDataIscrizioneRIAAEP(String dataIscrizioneRIAAEP)
  {
    this.dataIscrizioneRIAAEP = dataIscrizioneRIAAEP;
  }

  public String getDataIscrizioneRIAnagrafe()
  {
    return dataIscrizioneRIAnagrafe;
  }

  public void setDataIscrizioneRIAnagrafe(String dataIscrizioneRIAnagrafe)
  {
    this.dataIscrizioneRIAnagrafe = dataIscrizioneRIAnagrafe;
  }

  public String getStatoImpresaAAEP()
  {
    return statoImpresaAAEP;
  }

  public void setStatoImpresaAAEP(String statoImpresaAAEP)
  {
    this.statoImpresaAAEP = statoImpresaAAEP;
  }

  public String getVigenzaAAEP()
  {
    return vigenzaAAEP;
  }

  public void setVigenzaAAEP(String vigenzaAAEP)
  {
    this.vigenzaAAEP = vigenzaAAEP;
  }

  public String getColtivatoreDirettoAnagrafe()
  {
    return coltivatoreDirettoAnagrafe;
  }

  public void setColtivatoreDirettoAnagrafe(String coltivatoreDirettoAnagrafe)
  {
    this.coltivatoreDirettoAnagrafe = coltivatoreDirettoAnagrafe;
  }

  public boolean isDataIscrizioneREAUguale()
  {
    return dataIscrizioneREAUguale;
  }

  public void setDataIscrizioneREAUguale(boolean dataIscrizioneREAUguale)
  {
    this.dataIscrizioneREAUguale = dataIscrizioneREAUguale;
  }

  public boolean isDataCancellazioneREAUguale()
  {
    return dataCancellazioneREAUguale;
  }

  public void setDataCancellazioneREAUguale(boolean dataCancellazioneREAUguale)
  {
    this.dataCancellazioneREAUguale = dataCancellazioneREAUguale;
  }

  public boolean isDataIscrizioneRIUguale()
  {
    return dataIscrizioneRIUguale;
  }

  public void setDataIscrizioneRIUguale(boolean dataIscrizioneRIUguale)
  {
    this.dataIscrizioneRIUguale = dataIscrizioneRIUguale;
  }

  public Vector<CodeDescription> getvAtecoSecAAEP()
  {
    return vAtecoSecAAEP;
  }

  public void setvAtecoSecAAEP(Vector<CodeDescription> vAtecoSecAAEP)
  {
    this.vAtecoSecAAEP = vAtecoSecAAEP;
  }

  public Vector<TipoSezioniAaepVO> getvTipoSezAAEP()
  {
    return vTipoSezAAEP;
  }

  public void setvTipoSezAAEP(Vector<TipoSezioniAaepVO> vTipoSezAAEP)
  {
    this.vTipoSezAAEP = vTipoSezAAEP;
  }

  public String getDescrizioneTipoSezioneAAEP()
  {
    return descrizioneTipoSezioneAAEP;
  }

  public void setDescrizioneTipoSezioneAAEP(String descrizioneTipoSezioneAAEP)
  {
    this.descrizioneTipoSezioneAAEP = descrizioneTipoSezioneAAEP;
  }

  public String getDescrizioneTipoSezioneAnagrafe()
  {
    return descrizioneTipoSezioneAnagrafe;
  }

  public void setDescrizioneTipoSezioneAnagrafe(
      String descrizioneTipoSezioneAnagrafe)
  {
    this.descrizioneTipoSezioneAnagrafe = descrizioneTipoSezioneAnagrafe;
  }
	
	
}
