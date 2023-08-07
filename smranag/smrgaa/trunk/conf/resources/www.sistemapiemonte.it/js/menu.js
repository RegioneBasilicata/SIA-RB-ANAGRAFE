var flag = false;
function chiudiTutto(menuID) {
	var liste = document.getElementsByTagName("ul");
	if (flag)
		for(var i=0;i<liste.length;i++)
			if ((liste[i].className == "menuTendinaOrizz" && liste[i].id != menuID) || (liste[i].className == "menuTendina" && liste[i].id != menuID))
				chiudiLivello(liste[i].firstChild.firstChild);
	flag = true;
}
function menu(e) {
	var menu = e.parentNode;
	var menuID = menu.parentNode.id;
	if(menuID) chiudiTutto(menuID)
	var sottoMenu = menu.childNodes[1];
		/*alert(sottoMenu);*/
		
		var prossimoStato = '';
	if (sottoMenu.className != "attivo") prossimoStato = "attivo";

	chiudiLivello(sottoMenu);
	sottoMenu.className = prossimoStato;
	menu.className = prossimoStato;
	if (e.className != "fir") e.className = prossimoStato;
	flag = false;
/* le seguenti tre linee di codice fissano un bug di IE5 */
		var skin = document.getElementById('skin');
		skin.disabled = true;
		skin.disabled = false;
}

function chiudiLivello(e) {
	var menu = e.parentNode;
	if (e.className != "fir") e.className = "";
	var i = menu.parentNode.firstChild;

	do {
		//alert(i.id +" -  n. figli " + i.childNodes.length + " - title: " + i.title)
		if(i.childNodes.length > 1) {
			//*alert(i.childNodes[0].title);
			i.className="";
			i.childNodes[1].className = "";
			chiudiLivello(i.childNodes[1].firstChild.childNodes[0]); // ricorsione per analizzare l'albero fino alle foglie
			}
		i=i.nextSibling;
		}
	while (i);
	flag = false;
}

function getKeyChiudiTutto(e) {
	if (window.event.keyCode != 9) {
		chiudiTutto();
		flag = true;
	}
}
function getKeyMenu(e) {
	if (window.event.keyCode == 13) {
		menu(e);
		flag = false;
	}
}