// Funzione per selezionare tutti i checkbox dell'elenco più la chiave nascosta
// relativa all'id_utilizzo_particella
function selectAllKeys() 
{
  var array = document.forms[0].idUtilizzo;
    if(array != null) {
      if(array.length) {
          for(var i = 0; i < array.length; i++) {
            if(array[i].disabled == false) {
                array[i].checked = true;
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

// Funzione per deselezionare tutti i checkbox dell'elenco più la chiave nascosta
// relativa all'id_utilizzo_particella
function deselectAllKeys() 
{
  var array = document.forms[0].idUtilizzo;
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

function go_page(pagina, value, operazione) 
{
  pagina.value = value;
  goToOperazione(document.forms[0].action, document.forms[0], operazione);
}


// Funzione utilizzata per la gestione del num pagina della doppia paginazione
function duplicaNumPagina(argument) {
  if(document.forms[0].paginaCorrente != null) {
    if(argument == 'duplicato') {
      document.forms[0].paginaCorrente.value = document.forms[0].paginaCorrenteDup.value;
    }
    else {
      document.forms[0].paginaCorrenteDup.value = document.forms[0].paginaCorrente.value;
    }
  }
}


// Funzione che intercetta il pulsante invio e simula il click sul pulsante indicato come parametro
function simulateButtonViaByInvio(document, objectSubmit, event) {
  var mozilla = document.getElementById && !document.all;
  var ie = document.all;
  if(mozilla) {
    if(event.keyCode == 13) {
      //document.forms[0].operazioneDefault.value = 'operazioneDefault';
      document.forms[0].submit();
    }
  }
  else if(ie) {
    if(event.keyCode == 13) {
      objectSubmit.click();
    }
  }
}

function goToOperazioneBlocked(url, form, operazione, obj) {
  obj.onclick = '';
  form.operazione.value = operazione;
  form.action = url;
  form.submit();
}

function setHiddenOperazione(form, operazione) {
  form.operazione.value = operazione;
}