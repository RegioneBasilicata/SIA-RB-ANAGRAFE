var totOffset = 0;
var index = 0;
var idTable = null;

/**
 * @param id - l'id della tabella
 * @param shift - il numero di colonne mobili (le colonne sono contate in base alla prima riga di intestazione)
 * @param offset - spostamento: <0 sinistra; >0 destra; =0 nessuno spostamento 
 * @param fix - il numero di colonne fisse
**/
function shiftColumn(id, shift, offset, fix)
{
	var shiftCols = parseInt(shift);
	var relOffset = parseInt(offset);
	
	// recupero la tabella con id indicato
	var table = document.getElementById(id);
	
	// recupero il numero di righe della tabella
	var rows = table.getElementsByTagName('tr').length;

	// recupero i valori di colspan delle colonne della prima riga
	var colspans = new Array(table.rows[0].cells.length);

	for(i = 0; i < colspans.length; i++)
		colspans[i] = table.rows[0].cells[i].colSpan;

	// recupero i valori di rowspan delle colonne della prima riga e selezione il valore massimo
	var rowspans = new Array(table.rows[0].cells.length);

	for(i = 0; i < rowspans.length; i++)
		rowspans[i] = table.rows[0].cells[i].rowSpan;

	// recupero il numero di celle fisse in base all'ultima riga
	var fixCols = parseInt(fix);
	var flag = false;
	
	if(isNaN(fixCols) == true)
	{
		fixCols = 0;
		flag = true;
		var count = 0;
		
		while(count < table.rows[rows - 1].cells[0].colSpan)
		{	
			count += colspans[fixCols];
			fixCols++;
	  }
	}
	
	// imposto l'indice di colonna
	if(index == 0 || id != idTable) index = fixCols;

	if(id != idTable) idTable = id;
	
	if(relOffset > 0 && index < colspans.length) index++;
	else if(relOffset < 0 && index > fixCols) index--;
	
	// VALUTO LA PRIMA RIGA

	// imposto i valori di range
	var rangeStartAbs1 = fixCols;
	var rangeEndAbs1 = table.rows[0].cells.length;
	var rangeStart1 = rangeStartAbs1;
	
	for(j = rangeStartAbs1; j < index; j++)
		rangeStart1++;
	
	var rangeEnd1 = rangeStart1 + shiftCols;

	if(rangeEnd1 > rangeEndAbs1) rangeEnd1 = rangeEndAbs1;
	
	// ciclo sulle colonne
	for(j = 0; j < rangeEndAbs1; j++)
	{
		if((j < rangeStartAbs1) || (j >= rangeStart1 && j < rangeEnd1))
			table.rows[0].cells[j].style.display="";
		else
			table.rows[0].cells[j].style.display="none";
	}

	// VALUTO LA SECONDA RIGA
	var rangeEndAbs = table.rows[1].cells.length;
	var spans = 0;

	for(j = 0; j < rangeStartAbs1; j++)
		if(rowspans[j] == 1) 
			spans += colspans[j];
	
	var rangeStartAbs = 0;
	
	while(spans > 0)
	{
		spans -= table.rows[1].cells[rangeStartAbs].colSpan;
		rangeStartAbs++;
	}

	var rangeStart = rangeStartAbs;
	var count = 0;
	var spans = 0;

	for(j = rangeStartAbs1; j < index; j++)
		if(rowspans[j] > 1) count++;
		else spans += colspans[j];

	while(spans > 0)
	{
		spans -= table.rows[1].cells[rangeStart].colSpan;
		rangeStart++;
	}

	var rangeEnd = rangeStart;
	var count = 0;
	var spans = 0;

	for(j = index; j < rangeEnd1; j++)
		if(rowspans[j] > 1) count++;
		else spans += colspans[j];

	while(spans > 0)
	{
		spans -= table.rows[1].cells[rangeEnd].colSpan;
		rangeEnd++;
	}

	// ciclo sulle colonne
	for(j = 0; j < rangeEndAbs; j++)
	{
		if((j < rangeStartAbs) || (j >= rangeStart && j < rangeEnd))
			table.rows[1].cells[j].style.display="";
		else
			table.rows[1].cells[j].style.display="none";
	}

	// VALUTO LE RIGHE SUCCESSIVE
	var tmp = rows;

	if(flag) tmp--;
	
	for(k = 2; k < tmp; k++)
	{
		rangeEndAbs = table.rows[k].cells.length;
		rangeStartAbs = 0;

		for(j = 0; j < rangeStartAbs1; j++)
			rangeStartAbs += colspans[j];

		rangeStart = rangeStartAbs;
	
		for(j = rangeStartAbs1; j < index; j++)
			rangeStart += colspans[j];
	
		rangeEnd = rangeStart;

		for(j = index; j < rangeEnd1; j++)
			rangeEnd += colspans[j];

		// ciclo sulle colonne
		for(j = 0; j < rangeEndAbs; j++)
		{
			if((j < rangeStartAbs) || (j >= rangeStart && j < rangeEnd))
				table.rows[k].cells[j].style.display="";
			else
				table.rows[k].cells[j].style.display="none";
		}
	}

	// VALUTO L'ULTIMA RIGA
	if(flag)
	{
		rangeEndAbs = table.rows[rows - 1].cells.length;
		rangeStartAbs = 1;
		rangeStart = rangeStartAbs;

		for(j = rangeStartAbs1; j < index; j++)
			rangeStart += colspans[j];

		rangeEnd = rangeStart;

		for(j = index; j < rangeEnd1; j++)
			rangeEnd += colspans[j];

		// ciclo sulle colonne
		for(j = 0; j < rangeEndAbs; j++)
		{
			if((j < rangeStartAbs) || (j >= rangeStart && j < rangeEnd))
				table.rows[rows - 1].cells[j].style.display="";
			else
				table.rows[rows - 1].cells[j].style.display="none";
		}
	}
}
