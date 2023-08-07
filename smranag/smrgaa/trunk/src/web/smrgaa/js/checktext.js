//Attivazione refresh di sessione
if(!(window.opener)) {
  /* alert("Non è una popup"); */
  initTimeout();
}
/*else {
  alert("È una popup");
}*/

//Gestione per singolo click su pulsante di submit - Begin
setTimeout(event_onload, 100);

function noSubmit(){
  return false;
}

function singleClick(){
  for (i=0; i<document.forms.length; i++)
  {
    var itemForm = document.forms[i];
    itemForm.onsubmit=noSubmit;
  }
}

function event_onload()
{
  if(document.forms){
    var i;
    for (i=0; i<document.forms.length; i++)
    {
            var itemForm = document.forms[i];
            itemForm.onsubmit=singleClick;
    }
  }
  else{
     setTimeout(event_onload, 100);
  }
}
//Gestione per singolo click su pulsante di submit - End

// Questa funzione apre un nuovo pop-up solo a condizione che il parametro "richiesta" sia valorizzato
function NewWindow(mypage, myname, w, h, scroll,richiestaModifica) {
  if(richiestaModifica != '') {
    var winl = (screen.width - w) / 2;
    var wint = (screen.height - h) / 2;
    winprops =    'height='+h+',width='+w+',top='+wint+',left='+winl+',scrollbars='+scroll+',resizable';
    win = window.open(mypage, myname, winprops);
    if (myname=='abaco')
    {
      win.close();
      win = window.open(mypage, myname, winprops);
    }

    if (parseInt(navigator.appVersion) >= 4) {
      win.window.focus();
    }
  }
}


function NewWindowComune(mypage, myname, w, h, scroll,provincia,comune) {
  NewWindowComune(mypage, myname, w, h, scroll,provincia,comune,'');
}

function NewWindowComune(mypage, myname, w, h, scroll,provincia,comune,obiettivo) {
  var winl = (screen.width - w) / 2;
  var wint = (screen.height - h) / 2;
  if(provincia ==  '' && comune == '') {
    alert('Inserire una provincia o un comune!');
  }
  else if(!mappaOggetti(provincia) || !mappaOggetti(comune) || !mappaOggetti(obiettivo)) {
  }
  else {
    winprops =    'height='+h+',width='+w+',top='+wint+',left='+winl+',scrollbars='+scroll+',resizable';
    win = window.open(mypage+'?obiettivo='+obiettivo+'&provincia='+provincia+'&comune='+comune, myname, winprops);
    if (parseInt(navigator.appVersion) >= 4) {
      win.window.focus();
    }
  }
}

function popupComune(mypage, myname, w, h, scroll, provincia, comune, obiettivo, provenienza) {
	var winl = (screen.width - w) / 2;
  	var wint = (screen.height - h) / 2;
  	if(provincia ==  '' && comune == '') {
    	alert('Inserire una provincia o un comune!');
  	}
 	else if(!mappaOggetti(provincia) || !mappaOggetti(comune)) {
  	}
  	else {
    	winprops =    'height='+h+',width='+w+',top='+wint+',left='+winl+',scrollbars='+scroll+',resizable';
    	win = window.open(mypage+'?obiettivo='+obiettivo+'&provincia='+provincia+'&comune='+comune+'&provenienza='+provenienza, myname, winprops);
    	if(parseInt(navigator.appVersion) >= 4) {
      		win.focus();
    	}
  	}
}

/**
 * Funzione javascript che apre una pop-up relativa alla scelta dei comuni(usata nell'inserimento del documento)
 */
function popupComuneDocumenti(mypage, myname, w, h, scroll, comune, provincia, obiettivo, provenienza) {
  var winl = (screen.width - w) / 2;
  var wint = (screen.height - h) / 2;
  if(comune == '' && provincia == '') {
    alert('Inserire una parte del comune o della provincia');
  }
  else if(!mappaOggetti(comune)) {
  }
  else {
    winprops =    'height='+h+',width='+w+',top='+wint+',left='+winl+',scrollbars='+scroll+',resizable';
    win = window.open(mypage+'?obiettivo='+obiettivo+'&comune='+comune+'&provincia='+provincia+'&provenienza='+provenienza, myname, winprops);
    if (parseInt(navigator.appVersion) >= 4) {
      win.window.focus();
    }
  }
}

/**
 * Funzione che apre una pop-up utilizzata per l'associazione delle particelle ad un documento
 */
function popupParticelleDocumenti(mypage, myname, w, h, scroll, siglaProvincia, comune, sezione, foglio, particella, idTitoloPossesso, dataInizio, dataFine, protocolla, urlChiamante) {
  var winl = (screen.width - w) / 2;
  var wint = (screen.height - h) / 2;
  if(siglaProvincia != '' && comune == '') {
    alert('Indicare il comune!');
  }
  else if(comune == '' && sezione == '' && foglio == '' && particella == '' && idTitoloPossesso == '') {
    alert('Indicare almeno un criterio di ricerca');
  }
  else if(!mappaOggetti(comune)) {
  }
  else {
    winprops =    'height='+h+',width='+w+',top='+wint+',left='+winl+',scrollbars='+scroll+',resizable';
    window.name = 'PADRE';
    win = window.open(mypage+'?provincia='+siglaProvincia+'&comune='+comune+'&sezione='+sezione+'&foglio='+foglio+'&particella='+particella+'&idTitoloPossesso='+idTitoloPossesso+'&dataInizio='+dataInizio+'&dataFine='+dataFine+'&protocolla='+protocolla+'&urlChiamante='+urlChiamante, myname, winprops);
    if (parseInt(navigator.appVersion) >= 4) {
      win.window.focus();
    }
  }
}

function NewWindowStato(mypage, myname, w, h, scroll,stato) {
  var winl = (screen.width - w) / 2;
  var wint = (screen.height - h) / 2;

  if(!mappaOggetti(stato)) {
  }
  else {
    winprops =    'height='+h+',width='+w+',top='+wint+',left='+winl+',scrollbars='+scroll+',resizable';
    win = window.open(mypage+'?stato='+stato, myname, winprops);
    if (parseInt(navigator.appVersion) >= 4) {
      win.window.focus();
    }
  }
}

function NewWindowStatoObj(mypage, myname, w, h, scroll,stato,obiettivo) {
  var winl = (screen.width - w) / 2;
  var wint = (screen.height - h) / 2;

  if(!mappaOggetti(stato)) {
  }
  else {
    winprops =    'height='+h+',width='+w+',top='+wint+',left='+winl+',scrollbars='+scroll+',resizable';
    win = window.open(mypage+'?stato='+stato+'&obiettivo='+obiettivo, myname, winprops);
    if (parseInt(navigator.appVersion) >= 4) {
      win.window.focus();
    }
  }
}

// Funzione javascript che apre una pop-up per l'help relativo alla ricerca stato
// estero
function popupStato(mypage, myname, w, h, scroll, stato, obiettivo, provenienza) {
	var winl = (screen.width - w) / 2;
  	var wint = (screen.height - h) / 2;
  	if(!mappaOggetti(stato)) {
  	}
  	else {
    	winprops =    'height='+h+',width='+w+',top='+wint+',left='+winl+',scrollbars='+scroll+',resizable';
    	win = window.open(mypage+'?stato='+stato+'&obiettivo='+obiettivo+'&provenienza='+provenienza, myname, winprops);
    	if(parseInt(navigator.appVersion) >= 4) {
      		win.window.focus();
    	}
  	}
}

// Funzione di controllo per i caratteri non validi
function mappaOggetti(stringa) {

    var nonvalidi = '"<>$£|()^*:;0123456789';
    var trovatocarattere = false;

      if (stringa!=" " && stringa!="") {
        if(eValida(stringa, nonvalidi)) {
        }
        else {
          trovatocarattere = true;
        }
      }
      else {
      }
      if (trovatocarattere) {
        alert('I campi non possono contenere caratteri speciali');
        return false;
      }
      else {
        return true;
      }
}


//queste funzioni controllano lo stato di determinati campi della form
function eValida(stringa,nonammessi) {
  for (var i=0; i< stringa.length; i++) {
    if (nonammessi.indexOf(stringa.substring(i, i+1)) != -1) {
      return false;
    }
  }
  return true;
}

// Funzione per chiudere la finestra di pop-up e valorizzare i campi della finestra padre con i valori selezionati
// precedentemente dall'utente
function confermaComune() {
	if(document.sceltaComune.istat != null) {
    	var radio;
    	var obiettivo = document.sceltaComune.obiettivo;
    	for(var i = 0; i<document.sceltaComune.istat.length || i < 1;i++) {
      		radio = document.sceltaComune.istat[i];
      		if(radio == null) {
        		radio = document.sceltaComune.istat;
      		}
      		if(radio.checked == true) {
        		var provincia = document.sceltaComune.siglaProvincia[i];
        		var comune = document.sceltaComune.comune[i];
        		var istatComune = document.sceltaComune.istatComune[i];
        		var cap = document.sceltaComune.cap[i];
        		var istatProvincia = document.sceltaComune.istatProvincia[i];
        		var codiceFiscaleComune = document.sceltaComune.codiceFiscaleComune[i];
        		var zonaAlt = document.sceltaComune.zonaAltimetrica[i];
        		if(provincia == null) {
          			provincia = document.sceltaComune.siglaProvincia;
        		}
        		if(comune == null) {
          			comune = document.sceltaComune.comune;
        		}
        		if(istatComune == null) {
          			istatComune = document.sceltaComune.istatComune;
        		}
        		if(cap == null) {
          			cap = document.sceltaComune.cap;
        		}
        		if(istatProvincia == null) {
          			istatProvincia = document.sceltaComune.istatProvincia;
        		}
        		if(zonaAlt == null) {
          			zonaAlt = document.sceltaComune.zonaAltimetrica;
        		}
        		if(codiceFiscaleComune == null) {
          			codiceFiscaleComune = document.sceltaComune.codiceFiscaleComune;
        		}
    			if(obiettivo == null || obiettivo.value == '') {
      				opener.document.form1.provincia.value = provincia.value;
      				opener.document.form1.comune.value = comune.value;
      				opener.document.form1.istatComune.value = istatComune.value;
      				opener.document.form1.cap.value = cap.value;
          			if(opener.document.form1.tipiZonaAltimetrica != null) {
        				for(k = 0; k <opener.document.form1.tipiZonaAltimetrica.options.length; k++) {
          					if(opener.document.form1.tipiZonaAltimetrica.options[k].value==zonaAlt.value){
            					opener.document.form1.tipiZonaAltimetrica.selectedIndex = k;
          					}
        				}
      				}
    			}
        		else if(obiettivo.value=='nascita') {
      				opener.document.form1.nascitaComune.value = comune.value;
      				opener.document.form1.descNascitaComune.value = istatComune.value;
    			}
        		else if(obiettivo.value=='nascitaCap') 
        		{
      				opener.document.form1.descNascitaComune.value = comune.value;
      				opener.document.form1.nascitaComune.value = istatComune.value;
      				opener.document.form1.nascitaProv.value = provincia.value;
    			}
        		else if(obiettivo.value=='res') {
      				opener.document.form1.resCAP.value = cap.value;
      				opener.document.form1.descResComune.value = comune.value;
      				opener.document.form1.resComune.value = istatComune.value;
      				opener.document.form1.resProvincia.value = provincia.value;
    			}
        		else if(obiettivo.value == 'sedeleg') {
      				opener.document.form1.sedelegCAP.value = cap.value;
      				opener.document.form1.descComune.value = comune.value;
      				opener.document.form1.sedelegComune.value = istatComune.value;
      				opener.document.form1.sedelegProv.value = provincia.value;
    			}
        		else if (obiettivo.value=='nascitaProvCom'){
          			opener.document.form1.nascitaProv.value=provincia.value;
          			opener.document.form1.descNascitaComune.value=comune.value;
        		}
        		else if(obiettivo.value == 'insAzienda') {
          			opener.document.form1.descNascitaComune.value = comune.value;
          			opener.document.form1.nascitaComune.value = istatComune.value;
          			opener.document.form1.codiceFiscaleComune.value = codiceFiscaleComune.value;
          			opener.document.form1.nascitaProv.value = provincia.value;
          			opener.document.form1.provinciaN.value = istatProvincia.value;
        		}
        		else if(obiettivo.value == 'insAziendaProvAndCom') {
          			opener.document.form1.resProvincia.value = provincia.value;
         	 		opener.document.form1.descResComune.value = comune.value;
          			opener.document.form1.resComune.value = istatComune.value;
          			opener.document.form1.resCAP.value = cap.value;
        		}
        		else if(obiettivo.value == 'insSede') {
          			opener.document.form1.sedelegProv.value = provincia.value;
          			opener.document.form1.sedelegComune.value = comune.value;
          			opener.document.form1.sedelegCAP.value = cap.value;
        		}
        		else if(obiettivo.value == 'modSede') {
          			opener.document.form1.sedelegProv.value = provincia.value;
          			opener.document.form1.descComune.value = comune.value;
          			opener.document.form1.sedelegCAP.value = cap.value;
        		}
        		else if(obiettivo.value == 'domicilio') {
          			opener.document.form1.domProvincia.value = provincia.value;
          			opener.document.form1.domComune.value = comune.value;
          			opener.document.form1.domCAP.value = cap.value;
        		}
        		else if(obiettivo.value == 'ricercaPersona') {
          			opener.document.form1.resProvincia.value = provincia.value;
          			opener.document.form1.descResComune.value = comune.value;
          			opener.document.form1.resComune.value = istatComune.value;
        		}
        		else if(obiettivo.value == 'insParticella') {
          			opener.document.form1.siglaProvincia.value = provincia.value;
          			opener.document.form1.descComune.value = comune.value;
        		}
        		else if(obiettivo.value == 'modificaSoggetto') {
          			opener.document.form1.descResProvincia.value = provincia.value;
          			opener.document.form1.descResComune.value = comune.value;
          			opener.document.form1.resComune.value = istatComune.value;
          			opener.document.form1.resCAP.value = cap.value;
        		}
        		else if(obiettivo.value == 'funzionalitaTerreni') {
          			opener.document.form1.siglaProvinciaParticella.value = provincia.value;
          			opener.document.form1.descComuneParticella.value = comune.value;
        		}
        		else if(obiettivo.value == 'ricercaBiologico') {
          			opener.document.form1.siglaProvincia.value = provincia.value;
          			opener.document.form1.descComuneSedeLegaleAzienda.value = comune.value;
        		}
        		else if(obiettivo.value == 'allevamenti') {
          			opener.document.form1.resProvincia.value = provincia.value;
          			opener.document.form1.descResComune.value = comune.value;
          			opener.document.form1.resComune.value = istatComune.value;
          			opener.document.form1.cap.value = cap.value;
        		}
        		else if(obiettivo.value == 'insUvFrazionate') {
        			opener.document.form1.siglaProvinciaAssocia.value = provincia.value;
          			opener.document.form1.descComuneAssocia.value = comune.value;
        		}
        		else if(obiettivo.value == 'documentale') {
          			opener.document.form1.comune.value = comune.value;
          			opener.document.form1.siglaProvinciaParticella.value = provincia.value;
        		}
            else if(obiettivo.value == 'Comunicazione10RCessAcqu') {
                opener.document.form1.comuneCessAcqu.value = comune.value;
                opener.document.form1.provinciaCessAcqu.value = provincia.value;
                opener.document.form1.istatComuneCessAcqu.value = istatComune.value;
            }
            else if(obiettivo.value == 'Comunicazione10RCessioni') {
                opener.document.form1.comuneCessioni.value = comune.value;
                opener.document.form1.provinciaCessioni.value = provincia.value;
                opener.document.form1.istatComuneCessioni.value = istatComune.value;
            }
            else if(obiettivo.value == 'Comunicazione10RStocc') {
                opener.document.form1.comuneStocc.value = comune.value;
                opener.document.form1.provinciaStocc.value = provincia.value;
                opener.document.form1.istatComuneStocc.value = istatComune.value;
            }
            else if(obiettivo.value == 'AziendeCollegateInserisci') {
                opener.document.form1.comuneIns.value = comune.value;
                opener.document.form1.provinciaIns.value = provincia.value;
                opener.document.form1.istatComuneIns.value = istatComune.value;
                opener.document.form1.capIns.value = cap.value;
            }
            else if(obiettivo.value == 'AziendeCollegateElenco') {
                opener.document.elenco.comuneRicerca.value = comune.value;
                opener.document.elenco.provinciaRicerca.value = provincia.value;
                opener.document.elenco.istatComuneRicerca.value = istatComune.value;
            }
            else if(obiettivo.value == 'RicercaVariazioni') {
                opener.document.form1.comuneRicerca.value = comune.value;
                opener.document.form1.provinciaRicerca.value = provincia.value;
                opener.document.form1.istatComuneRicerca.value = istatComune.value;
            }
            else if(obiettivo.value == 'newInserimentoAzAss') {
            	opener.document.form1.comuneIns.value = comune.value;
                opener.document.form1.provinciaIns.value = provincia.value;
                opener.document.form1.istatComuneIns.value = istatComune.value;
                opener.document.form1.capIns.value = cap.value;
            }
        		window.close();
        		return;
      		}
    	}
    	alert('Selezionare un comune!');
  	}
}

// Funzione per chiudere la finestra di pop-up e valorizzare i campi della finestra padre con i valori selezionati
// precedentemente dall'utente
function confermaStato() {
	var obiettivo = document.sceltaStato.obiettivo.value;
  	if(document.sceltaStato.istat != null) {
    	var radio;
    	for(var i = 0; i < document.sceltaStato.istat.length || i < 1 ;i++) {
      		radio = document.sceltaStato.istat[i];
      		if(radio == null) {
        		radio = document.sceltaStato.istat;
      		}
      		if(radio.checked == true) {
        		var sigla = document.sceltaStato.siglaStato[i];
        		if(sigla == null) {
          			sigla = document.sceltaStato.siglaStato;
        		}
        		if(obiettivo == 'nascita') {
          			opener.document.form1.nascitaStatoEstero.value = sigla.value;
        		}
        		else if(obiettivo == 'res') {
          			opener.document.form1.statoEsteroRes.value = sigla.value;
        		}
        		else if(obiettivo == 'insAziendaStato') {
          			opener.document.form1.statoEsteroRes.value = sigla.value;
          			opener.document.form1.stato.value = radio.value;
        		}
        		else if(obiettivo == 'insAziendaStatoTitolare') {
          			opener.document.form1.descStatoEsteroResidenza.value = sigla.value;
          			opener.document.form1.stato.value = radio.value;
        		}
        		else if(obiettivo == 'insStatoSoggetto') {
          			opener.document.form1.statoEsteroRes.value = sigla.value;
          			opener.document.form1.stato.value = radio.value;
        		}	
        		else if(obiettivo == 'statoNascita') {
          			opener.document.form1.nascitaStatoEstero.value = sigla.value;
          			opener.document.form1.istatStatoEsteroNascita.value = radio.value;
        		}
        		else if(obiettivo == 'insSedeLegale') {
          			opener.document.form1.statoEstero.value = sigla.value;
          			opener.document.form1.istatStatoEstero.value = radio.value;
       	 		}
        		else if(obiettivo == 'funzionalitaTerreni') {
          			opener.document.form1.descStatoEsteroParticella.value = sigla.value;
        		}
        		else if(obiettivo == 'insParticella') {
        			opener.document.forms[0].descStatoEstero.value = sigla.value;
        		}
        		else if(obiettivo == 'statoDomicilio') {
          			opener.document.form1.domicilioStatoEstero.value = sigla.value;
        		}
        		else {
          			opener.document.form1.sedelegEstero.value = sigla.value;
          			opener.document.form1.istatStatoEstero.value = radio.value;
        		}
        		window.close();
       	 		return;
      		}
    	}
    	alert('Selezionare uno stato!');
  	}
}

// Funzione per disabilitare il campo stato estero nel caso in cui siano valorizzati provincia,comune o cap
function disabilitaStatoEstero(provincia,comune,cap) {
  if(provincia != '' || comune!= '' || cap != '' ) {
    document.form1.statoEstero.disabled = true;
  }
  else {
    document.form1.statoEstero.disabled = false;
  }
}

// Funzione per disabilitare il campo stato estero oppure i campi provincia comune e cap in base al loro valore
function disabilitaCampo(provincia,comune,cap,statoEstero) {
  if(provincia.value != '' ||  comune.value != '' || cap.value != '') {
    statoEstero.disabled = true;
  }
  else if(statoEstero.value != '') {
    provincia.disabled = true;
    comune.disabled = true;
    cap.disabled = true;
  }
}


function confermaAttivitaOTE() {
  var obiettivo = '';
  if(opener.document.form1.obiettivo != null) {
    obiettivo = opener.document.form1.obiettivo.value;
  }
  if(document.sceltaOTE.idAttivitaOTE != null) {
    var radio;
    for(var i = 0; i<document.sceltaOTE.idAttivitaOTE.length || i < 1;i++) {
      radio = document.sceltaOTE.idAttivitaOTE[i];
      if(radio == null) {
        radio = document.sceltaOTE.idAttivitaOTE;
      }
      if(radio.checked == true) {
        var codiceOTE = document.sceltaOTE.codiceOTE[i];
        var descrizioneOTE = document.sceltaOTE.descrizioneOTE[i];
        if(codiceOTE == null) {
          codiceOTE = document.sceltaOTE.codiceOTE;
        }
        if(descrizioneOTE == null) {
          descrizioneOTE = document.sceltaOTE.descrizioneOTE;
        }
        if(obiettivo == "sedi_mod") {
          opener.document.form1.codeOte.value = codiceOTE.value;
          opener.document.form1.descOte.value = descrizioneOTE.value;
        }
        else if(obiettivo == "sedi_new") {
          opener.document.form1.codeOte.value = codiceOTE.value;
          opener.document.form1.descOte.value = descrizioneOTE.value;
        }
        else {
          opener.document.form1.idAttivitaOTE.value = radio.value;
          opener.document.form1.codiceOTE.value = codiceOTE.value;
          opener.document.form1.descrizioneOTE.value = descrizioneOTE.value;
        }

        window.close();
        return;
      }
    }
    alert('Selezionare una attività OTE!');
  }
}

function confermaAttivitaATECO() {
  var obiettivo = '';
  if(opener.document.form1.obiettivo != null) {
    obiettivo = opener.document.form1.obiettivo.value;
  }
  if(document.sceltaATECO.idAttivitaATECO != null) {
    var radio;
    for(var i = 0; i<document.sceltaATECO.idAttivitaATECO.length || i < 1;i++) {
      radio = document.sceltaATECO.idAttivitaATECO[i];
      if(radio == null) {
        radio = document.sceltaATECO.idAttivitaATECO;
      }
      if(radio.checked == true) {
        var codiceATECO = document.sceltaATECO.codiceATECO[i];
        var descrizioneATECO = document.sceltaATECO.descrizioneATECO[i];
        if(codiceATECO == null) {
          codiceATECO = document.sceltaATECO.codiceATECO;
        }
        if(descrizioneATECO == null) {
          descrizioneATECO = document.sceltaATECO.descrizioneATECO;
        }
        if(obiettivo == "sedi_mod") {
          opener.document.form1.codeAteco.value = codiceATECO.value;
          opener.document.form1.descAteco.value = descrizioneATECO.value;
        }
        else if(obiettivo == "sedi_new") {
          opener.document.form1.codeAteco.value = codiceATECO.value;
          opener.document.form1.descAteco.value = descrizioneATECO.value;
        }
        else if(obiettivo == "confirmElencoAtecoImport") {
          opener.document.form1.codeAteco.value = codiceATECO.value;
          opener.document.form1.descAteco.value = descrizioneATECO.value;
        }
        else if(obiettivo == "modificaAzienda") {
          opener.document.form1.idAttivitaATECOSec.value = radio.value;
          opener.document.form1.codiceATECOSec.value = codiceATECO.value;
          opener.document.form1.descrizioneATECOSec.value = descrizioneATECO.value;
          opener.document.form1.operazione.value = "inserisciATECOSec";
          opener.document.form1.submit();
        }
        else if(obiettivo == "modificaUte") {
	      opener.document.form1.idAttivitaATECOSec.value = radio.value;
	      opener.document.form1.codiceATECOSec.value = codiceATECO.value;
	      opener.document.form1.descrizioneATECOSec.value = descrizioneATECO.value;
	      opener.document.form1.operazione.value = "inserisciATECOSec";
	      opener.document.form1.submit();
	    }
        else{
          opener.document.form1.idAttivitaATECO.value = radio.value;
          opener.document.form1.codiceATECO.value = codiceATECO.value;
          opener.document.form1.descrizioneATECO.value = descrizioneATECO.value;
        }
        window.close();
        return;
      }
    }
    alert('Selezionare una attività ATECO!');
  }
}

function goToLine(prev)
{
  document.paginazione.startRow.value=prev;
  document.paginazione.submit();
}

function visualError()
{
    oggettoDiv = document.getElementById("messageError");
    if ( (oggettoDiv != null) && (oggettoDiv.innerHTML != '') ){
            window.alert(oggettoDiv.innerHTML);
    }
}

// Funzione per chiudere la finestra di pop-up e valorizzare i campi della finestra padre con i valori selezionati
// precedentemente dall'utente
function confermaFoglio() {
	if(document.sceltaFoglio.idFoglio != null) {
    	var radio;
    	for(var i = 0; i < document.sceltaFoglio.idFoglio.length || i < 1; i++) {
      		radio = document.sceltaFoglio.idFoglio[i];
      		if(radio == null) {
        		radio = document.sceltaFoglio.idFoglio;
      		}
      		if(radio.checked == true) {
        		var foglio = document.sceltaFoglio.foglio[i];
        		var sezione = document.sceltaFoglio.sezione[i];
        		if(foglio == null) {
          			foglio = document.sceltaFoglio.foglio;
        		}
        		if(sezione == null) {
          			sezione = document.sceltaFoglio.sezione;
        		}
        		if(opener.document.form1.foglio != null) {
          			opener.document.form1.foglio.value = foglio.value;
        		}
        		else if(opener.document.form1.strFoglio != null) {
          			opener.document.form1.strFoglio.value = foglio.value;
        		}
        		else if(opener.document.form1.foglioFiltro != null) {
          			opener.document.form1.foglioFiltro.value = foglio.value;
        		}
            
            /* Solo per la ricerca terreni BEGIN */
            if(opener.document.form1.strFoglioRicerca != null) {
                opener.document.form1.strFoglioRicerca.value = foglio.value;
            }
            /* Solo per la ricerca terreni END */
            
        		if(sezione != null) {
          			if(opener.document.form1.sezione != null) {
            			opener.document.form1.sezione.value = sezione.value;
          			}
          			if(opener.document.form1.sezioneFiltro != null) {
            			opener.document.form1.sezioneFiltro.value = sezione.value;
          			}
        		}
        		window.close();
        		return;
      		}
    	}
    	alert('Selezionare un foglio!');
  	}
}

function confermaParticella() {
	if(document.sceltaParticella.idParticella != null) {
    	var radio;
   		for(var i = 0; i < document.sceltaParticella.idParticella.length || i < 1; i++) {
     			radio = document.sceltaParticella.idParticella[i];
     			if(radio == null) {
       			radio = document.sceltaParticella.idParticella;
     			}
     			if(radio.checked == true) {
       			var particella = document.sceltaParticella.particella[i];
       			if(particella == null) {
         				particella = document.sceltaParticella.particella;
       			}
       			opener.document.form1.particella.value = particella.value;
       			if(opener.document.form1.strParticella != null) {
       				opener.document.form1.strParticella.value = particella.value;
       			}
       			window.close();
       			return;
     			}
   		}
    	alert('Selezionare una particella');
  	}
}

function confermaSezione() {
	if(document.sceltaSezione.sezione != null) {
    	var radio;
    	for(var i = 0; i < document.sceltaSezione.sezione.length || i < 1; i++) {
      		radio = document.sceltaSezione.sezione[i];
      		if(radio == null) {
        		radio = document.sceltaSezione.sezione;
      		}	
      		if(radio.checked == true) {
        		var sezione = document.sceltaSezione.sezione[i];
        		if(sezione == null) {
          			sezione = document.sceltaSezione.sezione;
        		}
        		if(opener.document.form1.sezione != null) {
          			opener.document.form1.sezione.value = sezione.value;
        		}
        		else if(opener.document.form1.sezioneFiltro != null) {
          			opener.document.form1.sezioneFiltro.value = sezione.value;
        		}
        		window.close();
        		return;
      		}
    	}
    	alert('Selezionare una sezione');
  	}
}

function logout() {
  if(confirm('Si desidera abbandonare il servizio?')) {
    if(window.opener) {
      if(!window.opener.closed) {
        window.opener.opener = self;
        window.opener.close();
      }
    }
    window.opener = self;
    window.close();
  }
}

function nonDisponibile() {
  alert('Funzionalità non disponibile');
}

function cessaAttivita(page, operazione) {
	var radio = '';
  	if(operazione == 'fabbricato') {
    	radio = document.form1.idFabbricato;
  	}
  	else if(operazione == 'ute') {
    	radio = document.form1.radioDettaglio;
  	}
    else if(operazione == 'manodopera') {
      radio = document.form1.idManodopera;
    }
  	else {
    	radio = document.form1.radiobutton;
  	}
  	if(!radio) {
    	alert('Non esistono elementi su cui effettuare operazioni');
      	return;
    }
  	if(!radio.length) {
    	if(radio.checked) {
      		document.form1.idElemento.value = radio.value;
      		document.form1.operazione.value = operazione;
      		document.form1.action = page;
      		document.form1.submit();
      		return;
    	}
  	}
  	else {
    	for(i = 0;i < radio.length; i++) {
      		if(radio[i].checked) {
        		document.form1.idElemento.value = radio[i].value;
        		document.form1.operazione.value = operazione;
        		document.form1.action = page;
        		document.form1.submit();
        		return;
      		}
    	}
  	}
  	alert("Selezionare una voce dall''elenco");
}

// Funzione per aprire una pop-up per la ricerca della sezione in relazione a provincia e comune
// o stato estero ed eventualmente sezione stessa
function newWindowSezione(mypage, myname, w, h, scroll,comune, provincia, statoEstero) {
  var winl = (screen.width - w) / 2;
  var wint = (screen.height - h) / 2;
  if(comune != '' && provincia != '' && statoEstero != '' ) {
    alert('Valorizzare provincia e comune o stato estero');
    return;
  }
  if(comune == '' && provincia == '' && statoEstero == '' ) {
    alert('Controllare che siano stati valorizzati il comune e la provincia o lo stato estero');
    return;
  }
  else {
    if(statoEstero == '') {
      if(comune == '' || provincia == '') {
        alert('Se lo stato estero non è valorizzato, specificare provincia e comune');
        return;
      }
    }
  }
  if(!mappaOggetti(comune) || !mappaOggetti(provincia)) {
  }
  else {
    winprops =    'height='+h+',width='+w+',top='+wint+',left='+winl+',scrollbars='+scroll+',resizable';
    win = window.open('', myname, winprops);
    if (parseInt(navigator.appVersion) >= 4) {
      win.window.focus();
    }
    var target1 = document.form1.target;
    var action1 = document.form1.action;
    document.form1.target = myname;
    document.form1.action = mypage;
    document.form1.submit();
    document.form1.target = target1;
    document.form1.action = action1;
  }
}

/**
 * Funzione javascript per aprire la pop-up della sezione(usato in inserimento documenti)
 */
function newWindowSezioneDocumenti(mypage, myname, w, h, scroll,comune) {
  var winl = (screen.width - w) / 2;
  var wint = (screen.height - h) / 2;
  if(comune == '') {
    alert('Inserire il comune');
    return;
  }
  if(!mappaOggetti(comune)) {
  }
  else {
    winprops =    'height='+h+',width='+w+',top='+wint+',left='+winl+',scrollbars='+scroll+',resizable';
    win = window.open('', myname, winprops);
    if (parseInt(navigator.appVersion) >= 4) {
      win.window.focus();
    }
    var target1 = document.form1.target;
    var action1 = document.form1.action;
    document.form1.target = myname;
    document.form1.action = mypage;
    document.form1.submit();
    document.form1.target = target1;
    document.form1.action = action1;
  }
}

// Funzione per aprire una pop-up per la ricerca del foglio in relazione a provincia e comune
// o stato estero, sezione ed eventualmente foglio stesso
function newWindowFoglio(mypage, myname, w, h, scroll,comune, provincia, statoEstero) {
  var winl = (screen.width - w) / 2;
  var wint = (screen.height - h) / 2;
  if(comune != '' && provincia != '' && statoEstero != '' ) {
    alert('Valorizzare provincia e comune o stato estero');
    return;
  }
  if(comune == '' && provincia == '' && statoEstero == '' ) {
    alert('Controllare che siano stati valorizzati il comune e  la provincia o lo stato estero');
    return;
  }
  else {
    if(statoEstero == '') {
      if(comune == '' || provincia == '') {
        alert('Se lo stato estero non è valorizzato, specificare provincia e comune');
        return;
      }
    }
  }
  if(!mappaOggetti(comune) || !mappaOggetti(provincia)) {
  }
  else {
    winprops =    'height='+h+',width='+w+',top='+wint+',left='+winl+',scrollbars='+scroll+',resizable';
    win = window.open('', myname, winprops);
    if (parseInt(navigator.appVersion) >= 4) {
      win.window.focus();
    }
    var target1 = document.form1.target;
    var action1 = document.form1.action;
    document.form1.target = myname;
    document.form1.action = mypage;
    document.form1.submit();
    document.form1.target = target1;
    document.form1.action = action1;
  }
}

/**
 * Funzione javascript che apre una pop-up relativa alla scelta del foglio(utilizzata in inserisci documento)
 */
function newWindowFoglioDocumenti(mypage, myname, w, h, scroll,comune,sezione) {
  var winl = (screen.width - w) / 2;
  var wint = (screen.height - h) / 2;
  if(comune == '') {
    alert('Inserire il comune');
    return;
  }
  if(!mappaOggetti(comune)) {
  }
  else {
    winprops =    'height='+h+',width='+w+',top='+wint+',left='+winl+',scrollbars='+scroll+',resizable';
    win = window.open('', myname, winprops);
    if (parseInt(navigator.appVersion) >= 4) {
      win.window.focus();
    }
    var target1 = document.form1.target;
    var action1 = document.form1.action;
    document.form1.target = myname;
    document.form1.action = mypage;
    document.form1.submit();
    document.form1.target = target1;
    document.form1.action = action1;
  }
}

// Funzione per aprire una pop-up per la ricerca della particella in relazione a provincia e comune
// o stato estero, foglio ed eventualmente particella stessa
function newWindowParticella(mypage, myname, w, h, scroll,comune, provincia, statoEstero, foglio) {
  var winl = (screen.width - w) / 2;
  var wint = (screen.height - h) / 2;
  if(comune != '' && provincia != '' && statoEstero != '' ) {
    alert('Valorizzare provincia e comune o stato estero');
    return;
  }
  if(comune == '' && provincia == '' && statoEstero == '' && foglio == '') {
    alert('Controllare che siano stati valorizzati il foglio, il comune e la provincia o lo stato estero');
    return;
  }
  else {
    if(statoEstero == '') {
      if(comune == '' || provincia == '') {
        alert('Se lo stato estero non è valorizzato, specificare provincia e comune');
        return;
      }
    }
  }
  if(foglio == '') {
    alert('Specificare il foglio');
    return;
  }
  if(!mappaOggetti(comune) || !mappaOggetti(provincia)) {
  }
  else {
    winprops =    'height='+h+',width='+w+',top='+wint+',left='+winl+',scrollbars='+scroll+',resizable';
    win = window.open('', myname, winprops);
    if (parseInt(navigator.appVersion) >= 4) {
      win.window.focus();
    }
    var target1 = document.form1.target;
    var action1 = document.form1.action;
    document.form1.target = myname;
    document.form1.action = mypage;
    document.form1.submit();
    document.form1.target = target1;
    document.form1.action = action1;
  }
}

// Funzione creata per aprire una pop-up per la ricerca della provincia e del comune.
// Viene utilizzata nella funzionalità dei terrei
function newWindowComuneAttivoTerreni(mypage, myname, w, h, scroll,comune, provincia) {
  var winl = (screen.width - w) / 2;
  var wint = (screen.height - h) / 2;
  if(comune == '' && provincia == '') {
    alert('Inserire almeno una parte del comune o la provincia');
  }
  else if(!mappaOggetti(comune) || !mappaOggetti(provincia)) {
  }
  else {
    winprops =    'height='+h+',width='+w+',top='+wint+',left='+winl+',scrollbars='+scroll+',resizable';
    win = window.open('', myname, winprops);
    if (parseInt(navigator.appVersion) >= 4) {
      win.window.focus();
    }
    document.form1.comune.value = comune;
    document.form1.provincia.value = provincia;
    document.form1.obiettivo.value = 'funzionalitaTerreni';
    document.form1.provenienza.value = 'funzionalitaTerreni';
    var target1 = document.form1.target;
    var action1 = document.form1.action;
    document.form1.target = myname;
    document.form1.action = mypage;
    document.form1.submit();
    document.form1.target = target1;
    document.form1.action = action1;
  }
}

// Funzione creata per aprire una pop-up per la ricerca dello stato estero.
// Viene utilizzata nella funzionalità dei terrei
function newWindowStatoTerreni(mypage, myname, w, h, scroll,stato) {
  var winl = (screen.width - w) / 2;
  var wint = (screen.height - h) / 2;

  if(!mappaOggetti(stato)) {
  }
  else {
    winprops =    'height='+h+',width='+w+',top='+wint+',left='+winl+',scrollbars='+scroll+',resizable';
    win = window.open('', myname, winprops);
    if (parseInt(navigator.appVersion) >= 4) {
      win.window.focus();
    }
    document.form1.stato.value = stato;
    document.form1.obiettivo.value = 'funzionalitaTerreni';
    var target1 = document.form1.target;
    var action1 = document.form1.action;
    document.form1.target = myname;
    document.form1.action = mypage;
    document.form1.submit();
    document.form1.target = target1;
    document.form1.action = action1;
  }
}

// Funzione utilizzata nei link con funzionalità di pulsante per effettuare il submit del form
// nelle pagine che contengono un elenco di dati
function submitForm(url, form, radio) {
  var coll = radio;
  var count = 0;
  if(coll != null) {
    if(coll.length) {
      for(i=0; i < coll.length; i++) {
        if(coll[i].checked) {
          count++;
        }
      }
      if(count > 1) {
        alert("Selezionare una sola voce dall'elenco");
      }
      else if(count == 0) {
        alert("Selezionare una voce dall'elenco");
      }
      else {
        form.action = url;
        form.submit();
      }
    }
    else {
      if(coll.checked) {
        count++;
      }
      if(count > 1) {
        alert("Selezionare una sola voce dall'elenco");
      }
      else if(count == 0) {
        alert("Selezionare una voce dall'elenco");
      }
      else {
        form.action = url;
        form.submit();
      }
    }
  }
  else {
    alert('Non esistono elementi su cui effettuare operazioni');
  }
}

// Funzione che effettua la submit del form: utilizzata nei link con funzione di pulsante
function goTo(url, form) {
  form.action = url;
  form.submit();
}


function goToWithDescDich(url, form) 
{
  if((form.labelPianoLavorazione != null)
    && (form.idDichiarazioneConsistenza != null))
  {
    form.labelPianoLavorazione.value = form.idDichiarazioneConsistenza.options[
      form.idDichiarazioneConsistenza.options.selectedIndex].text;
  }
  form.action = url;
  form.submit();
}

// Funzione che effettua la submit del form: utilizzata nei link con funzione di pulsante specificando
// l'operazione
function goToOperazione(url, form, operazione) {
  form.operazione.value = operazione;
  form.action = url;
  form.submit();
  form.operazione.value=null;
}

// Funzione per gestire la paginazione negli elenchi
function freccia(url, valore, form) {
  form.operazione.value = "freccia";
  form.valoreIndice.value = valore;
  form.submit();
}

function doForm(formName,hiddenName) {
  var divElement=document.getElementById("hiddenElement");
  divElement.innerHTML="<input type='hidden' name='"+hiddenName+"' value=''>";
  formName.submit();
}


// Funzione creata per aprire una pop-up per la ricerca della provincia e del comune.
// Viene utilizzata nella funzionalità del biologico
function newWindowComuneBiologico(mypage, myname, w, h, scroll,comune, provincia) {
  var winl = (screen.width - w) / 2;
  var wint = (screen.height - h) / 2;
  if(comune == '' && provincia == '') {
    alert('Inserire almeno una parte del comune o la provincia');
  }
  else if(!mappaOggetti(comune) || !mappaOggetti(provincia)) {
  }
  else {
    winprops =    'height='+h+',width='+w+',top='+wint+',left='+winl+',scrollbars='+scroll+',resizable';
    win = window.open('', myname, winprops);
    if (parseInt(navigator.appVersion) >= 4) {
      win.window.focus();
    }
    document.form1.comune.value = comune;
    document.form1.provincia.value = provincia;
    document.form1.obiettivo.value = 'ricercaBiologico';
    document.form1.provenienza.value = 'ricercaBiologico';
    var target1 = document.form1.target;
    var action1 = document.form1.action;
    document.form1.target = myname;
    document.form1.action = mypage;
    document.form1.submit();
    document.form1.target = target1;
    document.form1.action = action1;
  }
}

// Metodo per far comparire un confirm di richiesta operazione all'utente
function requestConfirm(operazione, form) {
  if(operazione != '') {
    if(confirm(document.form1.messaggioOperazione.value)) {
      form.submit();
    }
  }
}

// Metodo per aprire una pop-up ed effettuare su di essa il submit del form
function newWindowDoForm(mypage, myname, w, h, scroll) {
	var winl = (screen.width - w) / 2;
  	var wint = (screen.height - h) / 2;

  	winprops =    'height='+h+',width='+w+',top='+wint+',left='+winl+',scrollbars='+scroll+',resizable';
  	win = window.open('', myname, winprops);
  	if (myname=='abaco') {
   		win.close();
    	win = window.open('', myname, winprops);
  	}

  	if (parseInt(navigator.appVersion) >= 4) {
    	win.window.focus();
  	}

  	var target1 = document.form1.target;
  	var action1 = document.form1.action;
  	document.form1.target = myname;
  	document.form1.action = mypage;
	document.form1.submit();
  	document.form1.target = target1;
  	document.form1.action = action1;
}


function newWindowDoFormGeneric(mypage, myname, w, h, scroll) {
  var winl = (screen.width - w) / 2;
    var wint = (screen.height - h) / 2;

    winprops =    'height='+h+',width='+w+',top='+wint+',left='+winl+',scrollbars='+scroll+',resizable';
    win = window.open('', myname, winprops);
    if (myname=='abaco') {
      win.close();
      win = window.open('', myname, winprops);
    }

    if (parseInt(navigator.appVersion) >= 4) {
      win.window.focus();
    }

    var target1 = document.forms[0].target;
    var action1 = document.forms[0].action;
    document.forms[0].target = myname;
    document.forms[0].action = mypage;
    document.forms[0].submit();
    document.forms[0].target = target1;
    document.forms[0].action = action1;
}

/**
*  Funzione che intercetta il pulsante invio e simula il click sul pulsante indicato come parametro
*/
function simulateButtonEventByInvio(document, objectSubmit, event) {
	if(event.keyCode == 13) {
		objectSubmit.click();
	}
}

/**
 * Funzione per selezionare tutti i checkbox di un elenco
 */
function selectAll(array) {
  if(array != null) {
    if(array.length) {
      for(var i = 0; i < array.length; i++) {
        if(array[i].disabled == false) {
          array[i].checked =   true;
        }
      }
    }
    else {
      if(array.disabled == false) {
        array.checked = true;
      }
    }
  }
}

/**
 * Funzione per deselezionare tutti i checkbox di un elenco
 */
function deselectAll(array) {
  if(array != null) {
    if(array.length) {
      for(var i = 0; i < array.length; i++) {
        array[i].checked = false;
      }
    }
    else {
      array.checked = false;
    }
  }
}

	/**
 	* Funzione per chiudere una pop-up ed effettuare la submit del form
 	*/
	function closePopupWithSubmit(form, array) {
  		var coll = array;
  		var count = 0;
  		if(coll != null) {
    		if(coll.length) {
      			for(i = 0; i < coll.length; i++) {
        			if(coll[i].checked) {
          				count++;
        			}
      			}
      			if(count == 0) {
        			alert("Selezionare una voce dall'elenco");
      			}
      			else {
        			form.target = window.opener.name;
        			form.submit();
        			window.close();
        			return false;
      			}
    		}
    		else {
      			if(coll.checked) {
        			count++;
      			}
      			if(count == 0) {
        			alert("Selezionare una voce dall'elenco");
      			}
      			else {
        			form.target = window.opener.name;
        			form.submit();
        			window.close();
        			return false;
      			}
    		}
  		}
  		else {
  			form.conferma.value = 'conferma';
  			form.target = window.opener.name;
        	form.submit();
        	window.close();
        	return false;
  		}
	}

/**
 * Metodo per effettuare il submit di più elementi di un elenco presenti nella form
 */
  function submitSomeElementsForm(url, form, array) {
    var coll = array;
    var count = 0;
    if(coll.length) {
      for(i=0; i < coll.length; i++) {
        if(coll[i].checked) {
          count++;
        }
      }
    }
    else {
      if(coll.checked) {
        count++;
      }
    }
    if(count == 0) {
      alert("Nessun elemento selezionato");
    }
    else {
      form.action = url;
      form.submit();
    }
  }

	/**
	 * Funzione javascript utilizzata per ottenere la data odierna formattata
	 * in dd/mm/yyyy
	 */
	 function getToday() {
	 	var mozilla = document.getElementById && !document.all;
		var ie = document.all;
	 	var now = new Date();
		var day = now.getDate();
		// Aggiungo 1 perchè in javascript il calcolo dei mesi parte da 0
		var month = now.getMonth()+1;
		var year;
		if(ie) {
			year = now.getYear();
		}
		else if(mozilla) {
			year = 1900 + now.getYear();
		}
		var today;
		if(day < 9) {
			day = '0'+day;
		}
		if(month < 9) {
			month = '0'+month;
		}
		today = day + '/' + month + '/'+year;
		return today;
	}



/**
 * RENEW SESSION
 */
//Gestione refresh sessione

//Funzioni per il refresh della sessione - Begin
var req;
var isIE;
var timeout = 1680000; // ms = 28m
//var timeout = 45000; // ms = 45s
var cntRefresh = 0;
var MAX_TIME_REFRESH = 8;

function initTimeout() {
	setTimeout("refreshSession()",timeout);
}

// (3) JavaScript function in which XMLHttpRequest JavaScript object is created.
// Please note that depending on a browser type, you create
// XMLHttpRequest JavaScript object differently.  Also the "url" parameter is not
// used in this code (just in case you are wondering why it is
// passed as a parameter).
//
function initRequest(url) {
    if (window.XMLHttpRequest) {
        req = new XMLHttpRequest();
    } else if (window.ActiveXObject) {
        isIE = true;
        req = new ActiveXObject("Microsoft.XMLHTTP");
    }
}

// (2) Event handler that gets invoked whenever a user types a character
// in the input form field whose id is set as "name".  This event
// handler invokes "initRequest(url)" function above to create XMLHttpRequest
// JavaScript object.
//
function refreshSession() {
    //window.alert('Get new Request');
    var url = "../servlet/RefreshServlet";
    //window.alert('Get new Request');

    // Invoke initRequest(url) to create XMLHttpRequest object
    initRequest(url);

    // The "processRequest" function is set as a callback function.
    // (Please note that, in JavaScript, functions are first-class objects: they
    // can be passed around as objects.  This is different from the way
    // methods are treated in Java programming language.)
    req.onreadystatechange = processRequest;
    req.open("GET", url, true);
    req.send(null);
}

// (4) Callback function that gets invoked asynchronously by the browser
// when the data has been successfully returned from the server.
// (Actually this callback function gets called every time the value
// of "readyState" field of the XMLHttpRequest object gets changed.)
// This callback function needs to be set to the "onreadystatechange"
// field of the XMLHttpRequest.
//
function processRequest() {
    if (req.readyState == 4) {
      if (req.status == 200) {
        //window.alert('cntRefresh: '+cntRefresh);
        if(cntRefresh<MAX_TIME_REFRESH){
          //window.alert('if('+cntRefresh+'<'+MAX_TIME_REFRESH+')');
          initTimeout();
        }
        else{
          //window.alert('else('+cntRefresh+'<'+MAX_TIME_REFRESH+')');
        }
        cntRefresh++;
      }
    }
}
//Funzioni per il refresh della sessione - End

// Funzione per aprire una pop-up per l'help della ricerca della sezione
function popupSezione(mypage, myname, w, h, scroll, provincia, comune, statoEstero, sezione) {
	var winl = (screen.width - w) / 2;
  	var wint = (screen.height - h) / 2;
  	if(comune != '' && provincia != '' && statoEstero != '' ) {
    	alert('Valorizzare provincia e comune o stato estero');
    	return;
  	}
  	if(comune == '' && provincia == '' && statoEstero == '' ) {
    	alert('Controllare che siano stati valorizzati il comune e la provincia o lo stato estero');
    	return;
  	}
  	else {
    	if(statoEstero == '') {
      		if(comune == '' || provincia == '') {
        		alert('Se lo stato estero non è valorizzato, specificare provincia e comune');
        		return;
      		}
    	}
  	}
  	if(!mappaOggetti(comune) || !mappaOggetti(provincia)) {
  	}
  	else {
    	winprops =    'height='+h+',width='+w+',top='+wint+',left='+winl+',scrollbars='+scroll+',resizable';
    	win = window.open(mypage+'?siglaProvinciaParticella='+provincia+'&descComuneParticella='+comune+'&descStatoEsteroParticella='+statoEstero+'&sezione='+sezione, myname, winprops);
    	if (parseInt(navigator.appVersion) >= 4) {
      		win.window.focus();
    	}
  	}
}

// Funzione per aprire una pop-up per l'help della ricerca del foglio
function popupFoglio(mypage, myname, w, h, scroll, provincia, comune, statoEstero, sezione, foglio) {
	var winl = (screen.width - w) / 2;
  	var wint = (screen.height - h) / 2;
  	if(comune != '' && provincia != '' && statoEstero != '' ) {
    	alert('Valorizzare provincia e comune o stato estero');
    	return;
  	}
  	if(comune == '' && provincia == '' && statoEstero == '' ) {
    	alert('Controllare che siano stati valorizzati il comune e la provincia o lo stato estero');
    	return;
  	}
  	else {
    	if(statoEstero == '') {
      		if(comune == '' || provincia == '') {
        		alert('Se lo stato estero non è valorizzato, specificare provincia e comune');
        		return;
      		}
    	}
  	}
  	if(!mappaOggetti(comune) || !mappaOggetti(provincia)) {
  	}
  	else {
    	winprops =    'height='+h+',width='+w+',top='+wint+',left='+winl+',scrollbars='+scroll+',resizable';
    	win = window.open(mypage+'?siglaProvinciaParticella='+provincia+'&descComuneParticella='+comune+'&descStatoEsteroParticella='+statoEstero+'&sezione='+sezione+'&foglio='+foglio, myname, winprops);
    	if (parseInt(navigator.appVersion) >= 4) {
      		win.window.focus();
    	}
  	}
}


// Funzione per chiudere la finestra di pop-up e valorizzare i campi della finestra padre con i valori selezionati
// precedentemente dall'utente
function confermaAzienda(provenienza) {
  if(provenienza == 'AziendeCollegate')
  {
    //alert("AziendeCollegate");
    if(document.sceltaAzienda.idAzienda != null) 
    {
      var radio;
      for(var i = 0; i < document.sceltaAzienda.idAzienda.length || i < 1; i++) {
          radio = document.sceltaAzienda.idAzienda[i];
          if(radio == null) {
            radio = document.sceltaAzienda.idAzienda;
          }
          if(radio.checked == true) 
          {
            var idAzienda = radio.value;
            var cuaa = document.sceltaAzienda.cuaa[i];
            var partitaIva = document.sceltaAzienda.partitaIva[i];
            var denominazione = document.sceltaAzienda.denominazione[i];
            if(cuaa == null) {
                cuaa = document.sceltaAzienda.cuaa;
            }
            if(partitaIva == null) {
                partitaIva = document.sceltaAzienda.partitaIva;
            }
            if(denominazione == null) {
                denominazione = document.sceltaAzienda.denominazione;
            }
            
            opener.document.form1.cuaa.value = cuaa.value;
            opener.document.form1.partitaIva.value = partitaIva.value;
            opener.document.form1.denominazione.value = denominazione.value;
            opener.document.form1.idAzienda.value = idAzienda;
            opener.document.form1.operazione.value = "inserisciAzienda";
            opener.document.form1.submit();
            window.close();
            return;
          }
      }
      alert('Selezionare un\'azienda!');
    }
  }
  else if(provenienza == 'Comunicazione10RCessAcqu')
  {
    //alert("Comunicazione10RCessAcqu");
    if(document.sceltaAzienda.idAzienda != null) 
    {
      var radio;
      for(var i = 0; i < document.sceltaAzienda.idAzienda.length || i < 1; i++) {
          radio = document.sceltaAzienda.idAzienda[i];
          if(radio == null) {
            radio = document.sceltaAzienda.idAzienda;
          }
          if(radio.checked == true) 
          {
            var idAzienda = radio.value;
            var cuaa = document.sceltaAzienda.cuaa[i];
            var denominazione = document.sceltaAzienda.denominazione[i];
            if(cuaa == null) {
                cuaa = document.sceltaAzienda.cuaa;
            }
            if(denominazione == null) {
                denominazione = document.sceltaAzienda.denominazione;
            }
            
            opener.document.form1.cuaaCessAcqu.value = cuaa.value;
            opener.document.form1.denominazioneCessAcqu.value = denominazione.value;
            opener.document.form1.idAziendaCessAcqu.value = idAzienda;
            //opener.document.form1.operazione.value = "inserisciAzienda";
            //opener.document.form1.submit();
            window.close();
            return;
          }
      }
      alert('Selezionare un\'azienda!');
    }
  }
  else if(provenienza == 'Comunicazione10RCessioni')
  {
    //alert("Comunicazione10RCessAcqu");
    if(document.sceltaAzienda.idAzienda != null) 
    {
      var radio;
      for(var i = 0; i < document.sceltaAzienda.idAzienda.length || i < 1; i++) {
          radio = document.sceltaAzienda.idAzienda[i];
          if(radio == null) {
            radio = document.sceltaAzienda.idAzienda;
          }
          if(radio.checked == true) 
          {
            var idAzienda = radio.value;
            var cuaa = document.sceltaAzienda.cuaa[i];
            var denominazione = document.sceltaAzienda.denominazione[i];
            if(cuaa == null) {
                cuaa = document.sceltaAzienda.cuaa;
            }
            if(denominazione == null) {
                denominazione = document.sceltaAzienda.denominazione;
            }
            
            opener.document.form1.cuaaCessioni.value = cuaa.value;
            opener.document.form1.denominazioneCessioni.value = denominazione.value;
            opener.document.form1.idAziendaCessioni.value = idAzienda;
            //opener.document.form1.operazione.value = "inserisciAzienda";
            //opener.document.form1.submit();
            window.close();
            return;
          }
      }
      alert('Selezionare un\'azienda!');
    }
  }
  else if(provenienza == 'Comunicazione10RStocc')
  {
    //alert("Comunicazione10RStocc");
    if(document.sceltaAzienda.idAzienda != null) 
    {
      var radio;
      for(var i = 0; i < document.sceltaAzienda.idAzienda.length || i < 1; i++) {
          radio = document.sceltaAzienda.idAzienda[i];
          if(radio == null) {
            radio = document.sceltaAzienda.idAzienda;
          }
          if(radio.checked == true) 
          {
            var idAzienda = radio.value;
            var cuaa = document.sceltaAzienda.cuaa[i];
            var denominazione = document.sceltaAzienda.denominazione[i];
            if(cuaa == null) {
                cuaa = document.sceltaAzienda.cuaa;
            }
            if(denominazione == null) {
                denominazione = document.sceltaAzienda.denominazione;
            }
            
            opener.document.form1.cuaaStocc.value = cuaa.value;
            opener.document.form1.denominazioneStocc.value = denominazione.value;
            opener.document.form1.idAziendaStocc.value = idAzienda;
            //opener.document.form1.operazione.value = "inserisciAzienda";
            //opener.document.form1.submit();
            window.close();
            return;
          }
      }
      alert('Selezionare un\'azienda!');
    }
  }
  else if(provenienza == 'NuovaIscrizione')
  {
    //alert("Comunicazione10RStocc");
    if(document.sceltaAzienda.idAzienda != null) 
    {
      var radio;
      for(var i = 0; i < document.sceltaAzienda.idAzienda.length || i < 1; i++) {
          radio = document.sceltaAzienda.idAzienda[i];
          if(radio == null) {
            radio = document.sceltaAzienda.idAzienda;
          }
          if(radio.checked == true) 
          {
            var idAzienda = radio.value;
            var cuaa = document.sceltaAzienda.cuaa[i];
            var denominazione = document.sceltaAzienda.denominazione[i];
            if(cuaa == null) {
                cuaa = document.sceltaAzienda.cuaa;
            }
            if(denominazione == null) {
                denominazione = document.sceltaAzienda.denominazione;
            }
            
            opener.document.form1.cuaaSubentro.value = cuaa.value;
            opener.document.form1.denomSubentro.value = denominazione.value;
            opener.document.form1.idAziendaSubentro.value = idAzienda;
            //opener.document.form1.operazione.value = "inserisciAzienda";
            //opener.document.form1.submit();
            window.close();
            return;
          }
      }
      alert('Selezionare un\'azienda!');
    }
  }
}

function trimAll(sString)
{
  while (sString.substring(0,1) == ' ')
  {
    sString = sString.substring(1, sString.length);
  }
  while (sString.substring(sString.length-1, sString.length) == ' ')
  {
    sString = sString.substring(0,sString.length-1);
  }
  return sString;
}

function setHiddenOperazione(form, operazione) 
{
  form.operazione.value = operazione;
  form.submit();
}



function confermaTipoDocumento() 
{
  if(document.sceltaTipoDocumento.idDescDocumento != null) 
  {
   	var radio;
   	var provenienza = document.sceltaTipoDocumento.provenienza;
   	for(var i = 0; i<document.sceltaTipoDocumento.idDescDocumento.length || i < 1;i++) 
   	{
      radio = document.sceltaTipoDocumento.idDescDocumento[i];
	  if(radio == null) 
	  {
		radio = document.sceltaTipoDocumento.idDescDocumento;
	  }
	  if(radio.checked == true) 
	  {
		var idTipologiaDocumento = document.sceltaTipoDocumento.idTipologiaDocumento[i];
		var idCategoriaDocumento = document.sceltaTipoDocumento.idCategoriaDocumento[i];
		if(idTipologiaDocumento == null) 
		{
		  idTipologiaDocumento = document.sceltaTipoDocumento.idTipologiaDocumento;
		}
		if(idCategoriaDocumento == null) 
		{
		  idCategoriaDocumento = document.sceltaTipoDocumento.idCategoriaDocumento;
		}
		
		if(provenienza.value == 'inserisci') 
		{
		  opener.document.form1.idTipologiaDocumentoPopUp.value = idTipologiaDocumento.value;
		  opener.document.form1.idCategoriaDocumentoPopUp.value = idCategoriaDocumento.value;
		  opener.document.form1.idTipoDocumentoPopUp.value = radio.value;
		  opener.document.form1.submit();
		}
		else if(provenienza.value =='elenco') 
		{
		  opener.document.form1.idTipologiaDocumentoPopUp.value = idTipologiaDocumento.value;
		  opener.document.form1.idCategoriaDocumentoPopUp.value = idCategoriaDocumento.value;
		  opener.document.form1.idTipoDocumentoPopUp.value = radio.value;
		  opener.document.form1.operazione.value = 'arrivoPopUp';
		  opener.document.form1.submit();
		}
        		
		window.close();
		return;
      }
    }
   	alert('Selezionare un documento!');
  }
}
