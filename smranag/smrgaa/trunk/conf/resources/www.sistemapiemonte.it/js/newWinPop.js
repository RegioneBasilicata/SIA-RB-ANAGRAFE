// APERTURE NUOVE FINESTRE E POPUP //

/*apertura finestra con parametri nel codice xhtml*/
function MM_openBrWindow (theURL,winName,features) {
window.open(theURL,winName,features);
}

/*apertura finestra per link esterni (nome finestra variabile) */
function apri(file)
    {
var prop = "toolbar=yes,location=yes, directories=yes, status=yes, menuBar=yes, scrollbars=yes, resizable=yes, width=800, height=600, top=0, left=0";
var ran_unrounded=Math.random()*1000;
var ran_number=Math.round(ran_unrounded);
       newwin = window.open(file,'finestra'+ran_number,prop);
       newwin.focus();
 return false;
    }
	
/*apertura finestra per link esterni (nome finestra fisso)*/
 function aprifinestra(file)
    {
 var prop = "toolbar=yes,location=yes, directories=yes, status=yes, menuBar=yes, scrollbars=yes, resizable=yes, width=800, height=600, top=0, left=0";
       newwin = window.open(file,'finestra',prop);
       newwin.focus();
 return false;
    }
	
/*apertura finestra per contatti*/
 function apricontatti(file)
    {
 var prop = "toolbar=no,location=no, directories=no, status=no, menuBar=no, scrollbars=no, resizable=no, width=377, height=220";
       newwin = window.open(file,'contatti',prop);
       newwin.focus();
 return false;
    }
	
/*apertura finestra per help con scroll da css*/
 function aprihelp(file)
    {
 var prop = "toolbar=no,location=no, directories=no, status=no, menuBar=no, scrollbars=no, resizable=no, width=720, height=402";
       newwin = window.open(file,'help',prop);
       newwin.focus();
 return false;
    }

/*apertura finestra per help con scroll su finestra*/
 function aprihelpwin(file)
    {
 var prop = "screenX=0,screenY=0,toolbar=no,location=no, directories=no, status=no, menuBar=no, scrollbars=yes, resizable=yes, width=736, height=402";
       newwin = window.open(file,'helpwin',prop);
       newwin.focus();
 return false;
    }
	
/*apertura finestra per credits*/
 function apricredits(file)
    {
 var prop = "toolbar=no,location=no, directories=no, status=no, menuBar=no, scrollbars=no, resizable=yes, width=600, height=250";
       newwin = window.open(file,'credits',prop);
       newwin.focus();
 return false;
    }
	
/*apertura finestra per glossario con scroll su finestra*/
 function apriglossario(file)
    {
 var prop = "toolbar=no,location=no, directories=no, status=no, menuBar=no, scrollbars=yes, resizable=yes, width=736, height=402";
       newwin = window.open(file,'glossario',prop);
       newwin.focus();
 return false;
    }
	
		
/* FUNZIONE NEW 2 */

function apri_new() { 
var prop="toolbar=yes,location=yes, directories=yes, status=yes, menuBar=yes, scrollbars=yes, resizable=yes, width=800, height=600, top=0, left=0";
var dimensione = 'apri'; apri_url(prop, dimensione); }

function apricontatti_new() {
var prop = "toolbar=no,location=no, directories=no, status=no, menuBar=no, scrollbars=no, resizable=no, width=377, height=220";	
var dimensione = 'apricontatti'; apri_url(prop, dimensione); }	

function aprihelp_new() {
var prop = "toolbar=no,location=no, directories=no, status=no, menuBar=no, scrollbars=no, resizable=no, width=720, height=402";	
var dimensione = 'aprihelp'; apri_url(prop, dimensione); }	

function aprihelpwin_new() {
var prop = "screenX=0,screenY=0,toolbar=no,location=no, directories=no, status=no, menuBar=no, scrollbars=yes, resizable=yes, width=736, height=402";	
var dimensione = 'aprihelpwin'; apri_url(prop, dimensione); }

function apricredits_new() {
var prop = "screenX=0,screenY=0,toolbar=no,location=no, directories=no, status=no, menuBar=no, scrollbars=yes, resizable=yes, width=600, height=250";	
var dimensione = 'apricredits'; apri_url(prop, dimensione); }

function apriglossario_new() {
var prop = "screenX=0,screenY=0,toolbar=no,location=no, directories=no, status=no, menuBar=no, scrollbars=yes, resizable=yes, width=736, height=402";	
var dimensione = 'apriglossario'; apri_url(prop, dimensione); }

function aprifaq_new() {
var prop = "screenX=0,screenY=0,toolbar=no,location=no, directories=no, status=no, menuBar=no, scrollbars=yes, resizable=yes, width=736, height=402";	
var dimensione = 'aprifaq'; apri_url(prop, dimensione); }

function apri_url(prop, dimensione) { 
a = document.getElementsByTagName('a');
for(i=0;a[i];i++) if(a[i].className == dimensione) 
{
if(a[i].title) {a[i].title += ""; }
else { if (!a[i].textContent) {a[i].title = a[i].innerText + " - Attenzione: questo link si apre in una nuova finestra"} else {a[i].title = a[i].textContent + " - Attenzione: questo link si apre in una nuova finestra";}}
a[i].title = a[i].title + "";
a[i].onclick = function () {window.open(this.href, '_blank',prop);return false;};
a[i].onkeypress = function (e) {k = (e) ? e.keyCode : window.event.keyCode; if(k==13) 
{window.open(this.href,'_blank',prop); return false;}
}}}
window.onload = function() {apri_new();apricontatti_new();aprihelp_new();aprihelpwin_new();apricredits_new();apriglossario_new();aprifaq_new();}