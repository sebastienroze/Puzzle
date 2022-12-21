function GetAjax(apiURL, returnFnc) {
	var xhr = new XMLHttpRequest();
	xhr.open("GET", apiURL);
	xhr.onreadystatechange = function() {
		if (xhr.readyState == 4 && xhr.status < 300) {
			// readyState 4 means request has finished + we only want to parse it if the request was successful (status code lower than 300)       
			//			returnFnc(JSON.parse(xhr.responseText))
			returnFnc(xhr.responseText)
		}
		else if (xhr.readyState == 4) {
			erreurAjax("Erreur du serveur API.\n\nCode Status: " + xhr.status + "\nResponse serveur: " + xhr.responseText);
		}
	};
	xhr.send();
}

function erreurAjax(contenuErreur) {
	var fiche = document.createElement("DIV");
	fiche.innerHTML = contenuErreur;
	document.body.appendChild(fiche);
}


function PostAjax(apiURL, returnFnc) {
	var xhr = new XMLHttpRequest();
	//	console.log("POST:" + apiURL);
	xhr.open("POST", apiURL, true);
	xhr.setRequestHeader("Content-type", "application/json; charset=utf-8");
	xhr.onreadystatechange = function() {
		if (xhr.readyState == 4 && xhr.status < 300) {
			// readyState 4 means request has finished + we only want to parse it if the request was successful (status code lower than 300)       
			returnFnc(xhr.responseText)
		}
		else if (xhr.readyState == 4) {
			erreurAjax("Erreur du serveur API.\n\nCode Status: " + xhr.status + "\nResponse serveur: " + xhr.responseText);
		}
	};
	xhr.send();
}

var radEvenExtaValues = "";

var RADEventExtras = [];
function RegisterRADEventExtra(aRADEventExtra) {
	RADEventExtras.push(aRADEventExtra);
}

function RADEvent(idCaller, apiURL, WebServiceName, evName) {
	apiURL += "?RADEvent=" + encodeURIComponent(idCaller);
	apiURL += "&RADEventName=" + evName
	const requestFieldvalues = document.querySelectorAll("[RADWebServiceValue" + WebServiceName + "]");
	for (let element of requestFieldvalues) {
		apiURL += "&" + element.getAttribute("name") + "=" + encodeURIComponent(element.value);
	}

	const requestFieldDatavalues = document.querySelectorAll("[RADWebServiceDataValue" + WebServiceName + "]");
	for (let element of requestFieldDatavalues) {
		apiURL += "&" + element.getAttribute("name") + "=" + encodeURIComponent(element.getAttribute("data-value"));
	}

	RADEventExtras.forEach(RADEventExtra => {
		apiURL += RADEventExtra();
	})
//	console.log(apiURL);
	
	PostAjax(apiURL, function(resultat) {
//		console.log(resultat);
		RADFill(resultat)
	})
}

function setDataValueById(id, value) {
	setDataValue(document.getElementById(id), value)
}

function setDataValue(cpn, value) {
	cpn.setAttribute("data-value", value)
}

/*
var setInnerHTML = function(elm, html) {
	elm.innerHTML = html;
	Array.from(elm.querySelectorAll("script")).forEach(oldScript => {
		const newScript = document.createElement("script");
		Array.from(oldScript.attributes)
			.forEach(attr => newScript.setAttribute(attr.name, attr.value));
		newScript.appendChild(document.createTextNode(oldScript.innerHTML));
		oldScript.parentNode.replaceChild(newScript, oldScript);
	});
}
	*/

var RADDestroys = [];

function RegisterRADDestroy(aRADDestroy) {
	RADDestroys.push(aRADDestroy);
}

function doRADInits() {
	const RADinits = document.querySelectorAll("[RADinit]");
	for (let element of RADinits) {
		window[element.getAttribute("RADinit")](element);
	}
}

function getJSONResultat(resultat) {
	try {
		return JSON.parse(resultat)
	} catch (e) {
		var fiche = document.getElementById("idRadApplication");
		RADDestroys.forEach(RADDestroy => {
			RADDestroy();
		})
		RADDestroys = [];
		fiche.innerHTML = resultat;
		doRADInits();
		return null;
	}
}

var RADFillExtras = [];

function RegisterRADFillExtra(aRADFillExtra) {
	RADFillExtras.push(aRADFillExtra);
}

function RADFill(resultat) {
	const json = getJSONResultat(resultat)
	if (json != null) {
//		console.log(json);
		json.properties.forEach(element => {
			document.getElementById(element[0])[element[1]] = element[2];
		});
		json.datavalues.forEach(element => {
			setDataValueById(element[0], element[1]);
		});
		json.functions.forEach(element => {
			window[element[0]](element[1]);
		});

		json.alerts.forEach(element => {
			alert(element);
		});
		RADFillExtras.forEach(RADFillExtra => {
			RADFillExtra(json);
		})
	}
}

function scroolRADGridByID(cpnID) {
	scroolRADGrid(document.getElementById(cpnID));
}

function selectGridLine(cpn) {
	var lineSelected = cpn.parentElement.querySelector(".line_selected");
	if (lineSelected != null) {
		lineSelected.classList.remove("line_selected")
	}
	setDataValue(cpn.parentElement.parentElement, cpn.rowIndex)
	cpn.classList.add("line_selected")
}

function scroolRADGrid(cpn) {
	const lineSelected = cpn.querySelector(".line_selected");
	if (lineSelected != null) {
		lineSelected.scrollIntoView();
	}
}

function loadApplication() {
	apiURL = "./ApplicationRAD";
	GetAjax(apiURL, function(resultat) {
		var fiche = document.getElementById("idRadApplication");
//		console.log(resultat);
		fiche.innerHTML = resultat;
		doRADInits();
	})
}

loadApplication();
