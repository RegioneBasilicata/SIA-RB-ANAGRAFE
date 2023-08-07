/*
function showhide(y) {
   elem=document.getElementById(y);
   prefisso=document.getElementById("apri" + y);
   visibile=(elem.style.display!="none")
   if (visibile) {elem.style.display="none"; prefisso.className="espandi";} 
   else {elem.style.display="block"; prefisso.className="comprimi";}
} 
window.onload = function() {
	var divs = document.getElementsByTagName("div");
	var spans = document.getElementsByTagName("span");
	for (var i=0; i<divs.length; i++) {if(divs[i].className == "sottosez") 
	divs[i].style.display = "none";};
	for (var i=0; i<spans.length; i++) {if(spans[i].className == "default") 
	spans[i].className = "espandi";};
}
*/

function showhide(y) {
   elem=document.getElementById(y);
   prefisso=document.getElementById("apri" + y);
   visibile=(elem.style.display!="none");
   if (visibile) {elem.style.display="none"; prefisso.className="espandi";} 
   else {elem.style.display="block"; prefisso.className="comprimi";}
} 
window.onload = function() {
	var divs = document.getElementsByTagName("div");
	var hs = document.getElementsByTagName("h3");
	for (var i=0; i<divs.length; i++) {if(divs[i].className == "sottosez") 
	divs[i].style.display = "none";};
	for (var i=0; i<hs.length; i++) {if(hs[i].className == "titoloapri") 
	hs[i].className = "espandi";};
}	

/*link esterni (metterli in coda alla funzione soprastante)
apri_new();apricontatti_new();aprihelp_new();aprihelpwin_new();apricredits_new();apriglossario_new();*/
