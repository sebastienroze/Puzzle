var puzzleWidth;
var puzzleHeight;
var nbPieces;
var nbPiecesPuzzle;
var infoPieces = [];
var pieces = [];
var ctxPieces = [];
var piecesImages = [];
var puzzleInitialized = false;
var pieceInfoModifiee = -1;
var pieceFusionA = -1;
var pieceFusionB = -1;
var piecesFusionnees;
var requestPuzzleInfo = 0;
var antiCache;
var winX = 0;
var winY = 0;
var scroolMode = false;

RegisterRADFillExtra(function(json) {
	requestPuzzleInfo = 0;
	if (json.color != null) {
		document.body.style.backgroundColor = json.color;
	}
	if (json.fullScreen != null) {
		if (json.fullScreen) {
			GoInFullscreen();
		}
	}
	if (json.nbPieces != null) {
		nbPieces = json.nbPieces;
		nbPiecesPuzzle = json.nbPieces
		displayNbPieces();
	}
	if (json.antiCache != null) {
		antiCache = json.antiCache;
	}
	if (json.imageWidth != null) {
		puzzleWidth = json.imageWidth;
	}
	if (json.imageHeight != null) {
		puzzleHeight = json.imageHeight;
	}
	if (json.piecesFusionnees != null) {
		piecesFusionnees = json.piecesFusionnees;
	}
	if (json.infoPieces != null) {
		setinfoPieces(json.infoPieces);
	}
});

RegisterRADEventExtra(function() {
	if (pieceInfoModifiee > 0) {
		const data = "&requestPuzzleInfo=" + requestPuzzleInfo
			+ "&pimNumero=" + pieceInfoModifiee
			+ "&pimNeighbors=" + encodeURIComponent(infoPieces[pieceInfoModifiee][2])
			+ "&pimRotation=" + encodeURIComponent(Math.trunc(infoPieces[pieceInfoModifiee][3]))
			+ "&pimX=" + encodeURIComponent(Math.trunc(infoPieces[pieceInfoModifiee][4]))
			+ "&pimY=" + encodeURIComponent(Math.trunc(infoPieces[pieceInfoModifiee][5]))
			+ "&pieceFusionA=" + pieceFusionA
			+ "&pieceFusionB=" + pieceFusionB;
		pieceInfoModifiee = -1;
		pieceFusionA = -1;
		pieceFusionB = -1;
		return data;
	}
	return "&requestPuzzleInfo=" + requestPuzzleInfo;
})

function initialisation() {
	puzzleInitialized = true;
	requestPuzzleInfo = 1;
	document.addEventListener('touchstart', touchDown);
	document.addEventListener('touchend', touchUp);
	document.addEventListener('touchcancel', touchUp);
	document.addEventListener('touchmove', touchMove, true);
	document.addEventListener('mouseup', mouseUp, true);
	document.addEventListener('mousedown', mouseDown, true);
	document.addEventListener('mousemove', mouseMove, true);
	eltRotation = document.getElementById("idrotation");
	texteNbPiece = document.getElementById("idtexteNbPiece");
	document.body.style.overflow = 'hidden';

	RegisterRADDestroy(function() {
		if (puzzleInitialized) {
			document.removeEventListener('touchstart', touchDown);
			document.removeEventListener('touchend', touchUp);
			document.removeEventListener('touchcancel', touchUp);
			document.removeEventListener('touchmove', touchMove, true);
			document.removeEventListener('mouseup', mouseUp, true);
			document.removeEventListener('mousedown', mouseDown, true);
			document.removeEventListener('mousemove', mouseMove, true);
			document.removeEventListener('dblclick', mouseDblclick, true);
			infoPieces = [];
			pieces = [];
			ctxPieces = [];
			piecesImages = [];
			puzzleInitialized = false;
			winX = 0;
			winY = 0;
			scroolMode = false;
			document.body.style.overflow = 'visible';
			document.body.style.backgroundColor = "";
			GoOutFullscreen();
		}
	})
	document.getElementById("idPuzzle_load").click();
}

function mouseDblclick(evt) {
	evt.preventDefault();
	evt.stopPropagation();
	return false;
}

var tempNewPiece;
var ctxNewPiece;
var movingPiece = -1;
var moveOffsetLeft;
var moveOffsetTop;
var inRotation = 0;

var eltRotation;
var rotationAngle;
var imagesLoaded = 0;
var texteNbPiece;


function displayNbPieces() {
	texteNbPiece.innerText = (nbPiecesPuzzle * 2 - nbPieces);
}

function setinfoPieces(valueinfoPieces) {
	infoPieces = valueinfoPieces;
	createTempPiece();
	createImages();
}

function piecesLoaded() {
	for (let imageNo = 0; imageNo < nbPieces; imageNo++) {
		const piece = document.createElement("canvas");
		const imgpiece = piecesImages.shift();
		piece.width = imgpiece.width;
		piece.height = imgpiece.height;
		const ctx = piece.getContext("2d", { willReadFrequently: true });
		ctx.drawImage(imgpiece, 0, 0);
		ctxPieces.push(ctx);
		pieces.push(piece);
		if (infoPieces[imageNo][3] == -1) {
			infoPieces[imageNo][3] = Math.random() * 360;
			infoPieces[imageNo][4] = Math.floor((Math.random() * puzzleWidth / 2) + (puzzleWidth / 4) - (piece.width / 2));
			infoPieces[imageNo][5] = Math.floor((Math.random() * puzzleHeight / 2) + (puzzleHeight / 4) - (piece.height / 2));
		}
		imgpiece.height = puzzleWidth;
		piece.style.position = "absolute";
		piece.style.left = infoPieces[imageNo][4] + "px";
		piece.style.top = infoPieces[imageNo][5] + "px";
		piece.style.transform = "rotate(" + infoPieces[imageNo][3] + "deg)";
		piece.style.zIndex = imageNo;
		document.getElementById("idRadApplication").appendChild(piece);
	}
}

function createImages() {
	imagesLoaded = 0;
	for (let imageNo = 0; imageNo < nbPieces; imageNo++) {
		const piece = document.createElement("IMG");
		piece.onload = function() {
			if (++imagesLoaded == nbPieces) {
				piecesLoaded();
				setPiecesFusionnees();
			}
		};
		piece.src = "Pieces/" + antiCache + imageNo;
		piecesImages.push(piece);
	}
}

function createTempPiece() {
	tempNewPiece = document.createElement("canvas");
	tempNewPiece.width = puzzleWidth;
	tempNewPiece.height = puzzleHeight;
	tempNewPiece.classList.add("hidden");
	ctxNewPiece = tempNewPiece.getContext("2d");
	document.getElementById("idRadApplication").appendChild(tempNewPiece);
}

function setPiecesFusionnees() {
	pieceInfoModifiee = -1;
	var pieceFusionA;
	var pieceFusionB;
	piecesFusionnees.forEach(piece => {
		if (pieceInfoModifiee == -1) {
			pieceFusionA = piece;
			pieceInfoModifiee = nbPieces;
		} else {
			pieceFusionB = piece;
			fusionnePieces(pieceFusionA, pieceFusionB);
			pieceInfoModifiee = -1;
		}
	});
	displayNbPieces();
	pieceInfoModifiee = -1;
	pieceFusionA = -1;
	pieceFusionB = -1;
}


function fusionnePieces(pieceA, pieceB) {
	ctxNewPiece.clearRect(0, 0, puzzleWidth, puzzleHeight);
	ctxNewPiece.drawImage(pieces[pieceA], infoPieces[pieceA][0], infoPieces[pieceA][1]);
	ctxNewPiece.drawImage(pieces[pieceB], infoPieces[pieceB][0], infoPieces[pieceB][1]);
	var xMin = infoPieces[pieceA][0];
	var yMin = infoPieces[pieceA][1];
	var xMax = xMin + pieces[pieceA].width;
	var yMax = yMin + pieces[pieceA].height;
	var xMinB = infoPieces[pieceB][0];
	var yMinB = infoPieces[pieceB][1];
	var xMaxB = xMinB + pieces[pieceB].width;
	var yMaxB = yMinB + pieces[pieceB].height;
	if (xMinB < xMin) { xMin = xMinB; }
	if (yMinB < yMin) { yMin = yMinB; }
	if (xMaxB > xMax) { xMax = xMaxB; }
	if (yMaxB > yMax) { yMax = yMaxB; }
	const newPiece = document.createElement("canvas");
	newPiece.width = xMax - xMin + 1;
	newPiece.height = yMax - yMin + 1;
	newPiece.getContext("2d", { willReadFrequently: true }).drawImage(tempNewPiece, xMin, yMin, newPiece.width, newPiece.height,
		0, 0, newPiece.width, newPiece.height);
	newPiece.style.position = "absolute";
	pieces.push(newPiece);
	if (pieceInfoModifiee == -1) {
		infoPieces.push([xMin, yMin, [], infoPieces[pieceB][3],
			0,
			0,
			newPiece.width, newPiece.height
		]);
		const neighborPosition = findNeighborPosition(pieceB, nbPieces);
		infoPieces[nbPieces][4] = neighborPosition[0];
		infoPieces[nbPieces][5] = neighborPosition[1];

	} else {
		infoPieces[pieceInfoModifiee][0] = xMin;
		infoPieces[pieceInfoModifiee][1] = yMin;
	}
	//	appel = fusionnePieces(movingPiece, neighbor);			
	newPiece.style.left = infoPieces[nbPieces][4] + "px";
	newPiece.style.top = infoPieces[nbPieces][5] + "px";
	newPiece.style.transform = "rotate(" + infoPieces[nbPieces][3] + "deg)";

	for (const voisine of infoPieces[pieceA][2]) {
		if (voisine != pieceB) {
			if (!infoPieces[voisine][2].includes(nbPieces)) {
				infoPieces[voisine][2].push(nbPieces);
			}
			if (!infoPieces[nbPieces][2].includes(voisine)) {
				infoPieces[nbPieces][2].push(voisine)
			}
			infoPieces[voisine][2].splice(infoPieces[voisine][2].indexOf(pieceA), 1);
		}
	}
	for (const voisine of infoPieces[pieceB][2]) {
		if (voisine != pieceA) {
			if (!infoPieces[voisine][2].includes(nbPieces)) {
				infoPieces[voisine][2].push(nbPieces);
			}
			if (!infoPieces[nbPieces][2].includes(voisine)) {
				infoPieces[nbPieces][2].push(voisine)
			}
			infoPieces[voisine][2].splice(infoPieces[voisine][2].indexOf(pieceB), 1);
		}
	}
	newPiece.style.zIndex = ++nbPieces;
	ctxPieces.push(null);
	document.getElementById("idRadApplication").removeChild(pieces[pieceA]);
	document.getElementById("idRadApplication").removeChild(pieces[pieceB]);
	document.getElementById("idRadApplication").appendChild(newPiece);
	pieces[pieceA] = null;
	pieces[pieceB] = null;
}

function touchUp(evt) {
	//	evt.preventDefault();
	screenUp();
}
function mouseUp(evt) {
	evt.preventDefault();
	screenUp();
}

function uploadPieceModifiee() {
	pieceInfoModifiee = movingPiece;
	document.getElementById("idPuzzle_load").click();
	movingPiece = -1;
}

function screenUp() {
	scroolMode = false;
	if (movingPiece == -1) { return; }
	if (inRotation == 1) {
		eltRotation.style.display = "";
		const pieceRect = pieces[movingPiece].getBoundingClientRect();
		eltRotation.style.left = (window.scrollX + pieceRect.left + (pieceRect.width / 2) - 50) + "px";
		eltRotation.style.top = (window.scrollY + pieceRect.top + (pieceRect.height / 2) - 50) + "px";
		eltRotation.style.zIndex = nbPieces + 1;
		inRotation = 2;
		return;
	}
	if (inRotation >= 3) {
		eltRotation.style.display = "none";
		eltRotation.style.opacity = "1";
	}

	inRotation = 0;
	const neighbors = infoPieces[movingPiece][2];
	for (const neighbor of neighbors) {
		var deltaAngle = (360 + infoPieces[neighbor][3] - infoPieces[movingPiece][3]) % 360;
		if (deltaAngle > 180) {
			deltaAngle = 360 - deltaAngle;
		}
		if (deltaAngle > 20) {
			continue;
		}
		const piecePosition = findNeighborPosition(neighbor, movingPiece);
		const deltaX = infoPieces[movingPiece][4] - piecePosition[0];
		const deltaY = infoPieces[movingPiece][5] - piecePosition[1];
		if (Math.abs(deltaX) < 15 && Math.abs(deltaY) < 15) {
			pieceInfoModifiee = -1;
			fusionnePieces(movingPiece, neighbor);
			pieceFusionA = movingPiece;
			pieceFusionB = neighbor;
			movingPiece = nbPieces - 1;
			displayNbPieces();
			uploadPieceModifiee();
			return;
		}
	}
	uploadPieceModifiee();
}

function findNeighborPosition(piece, neighbor) {
	const originalDeltaX = infoPieces[piece][0] - infoPieces[neighbor][0];
	const originalDeltaY = infoPieces[neighbor][1] - infoPieces[piece][1];
	const angle = -infoPieces[piece][3] * Math.PI / 180;
	var deltaX = (originalDeltaX * Math.cos(angle)) - (originalDeltaY * Math.sin(angle));
	var deltaY = (originalDeltaY * Math.cos(angle)) + (originalDeltaX * Math.sin(angle));
	const cornerPieceX = - pieces[piece].width / 2;
	const cornerPieceY = pieces[piece].height / 2;
	const cornerPieceRotatedX = (cornerPieceX * Math.cos(angle)) - (cornerPieceY * Math.sin(angle));
	const cornerPieceRotatedY = (cornerPieceY * Math.cos(angle)) + (cornerPieceX * Math.sin(angle));
	const cornerNeighborX = -pieces[neighbor].width / 2;
	const cornerNeighborY = pieces[neighbor].height / 2;
	const cornerNeighborRotatedX = (cornerNeighborX * Math.cos(angle)) - (cornerNeighborY * Math.sin(angle));
	const cornerNeighborRotatedY = (cornerNeighborY * Math.cos(angle)) + (cornerNeighborX * Math.sin(angle));
	const deltaPieceX = cornerPieceRotatedX - cornerPieceX;
	const deltaPieceY = cornerPieceRotatedY - cornerPieceY;
	const deltaNeighborX = cornerNeighborRotatedX - cornerNeighborX;
	const deltaNeighborY = cornerNeighborRotatedY - cornerNeighborY;
	deltaX += deltaNeighborX - deltaPieceX;
	deltaY += deltaNeighborY - deltaPieceY;
	return [infoPieces[piece][4] - deltaX, infoPieces[piece][5] + deltaY];
}

function touchDown(evt) {
	evt.preventDefault();
	screenDown(evt.touches[0].clientX, evt.touches[0].clientY);
}
function mouseDown(evt) {
	evt.preventDefault();
	screenDown(evt.clientX, evt.clientY);
}

function screenDown(x, y) {
	if (inRotation != 0) {
		if (inRotation == 2) {
			inRotation = 3;
		}
		return;
	}
	var laPiece = -1;
	var maxZindex = -1;
	for (let i = 0; i < pieces.length; i++) {
		if (pieces[i] == null) { continue; }
		var evx = x + window.scrollX - infoPieces[i][4] - Math.floor(pieces[i].width / 2);
		var evy = Math.floor(pieces[i].height / 2) + infoPieces[i][5] - y - window.scrollY;
		const angle = infoPieces[i][3] * Math.PI / 180;
		const revx = (evx * Math.cos(angle)) - (evy * Math.sin(angle));
		const revy = (evy * Math.cos(angle)) + (evx * Math.sin(angle));
		evx = Math.floor(revx + (pieces[i].width / 2));
		evy = Math.floor(pieces[i].height / 2 - revy);
		if (evx >= 0 && evx <= pieces[i].width &&
			evy >= 0 && evy <= pieces[i].height) {
			if (ctxPieces[i] == null) {
				ctxPieces[i] = pieces[i].getContext("2d", { willReadFrequently: true });
			}
			const pixelPiece = ctxPieces[i].getImageData(evx, evy, 1, 1);
			const alhpa = pixelPiece.data[3];
			if (alhpa == 255) {
				if (maxZindex < parseInt(pieces[i].style.zIndex, 10)) {
					laPiece = i;
					maxZindex = parseInt(pieces[i].style.zIndex, 10);
					moveOffsetLeft = infoPieces[i][4] - x;
					moveOffsetTop = infoPieces[i][5] - y;
				}
			}
		}
	}
	movingPiece = laPiece;
	if (movingPiece >= 0) {
		inRotation = 1;
		for (let i = 0; i < pieces.length; i++) {
			if (pieces[i] == null) { continue; }
			if (laPiece == i) {
				pieces[i].style.zIndex = pieces.length;
			} else if (maxZindex < parseInt(pieces[i].style.zIndex, 10)) {
				pieces[i].style.zIndex = (pieces[i].style.zIndex - 1);
			}

		}
	} else {
		scroolMode = true;
		moveOffsetLeft = x + winX;
		moveOffsetTop = y + winY;
	}

}

function touchMove(evt) {
	evt.preventDefault();
	screenMove(evt.touches[0].clientX, evt.touches[0].clientY);
}


function mouseMove(evt) {
	evt.preventDefault();
	if (scroolMode) {
		winX = moveOffsetLeft - evt.clientX;
		winY = moveOffsetTop - evt.clientY;
		window.scrollTo(winX, winY);
		winX = window.scrollX;
		winY = window.scrollY;
	}
	screenMove(evt.clientX, evt.clientY);
}

function screenMove(x, y) {
	if (movingPiece == -1) {
		return;
	}
	if (inRotation <= 1) {
		infoPieces[movingPiece][4] = x + moveOffsetLeft;
		infoPieces[movingPiece][5] = y + moveOffsetTop;
		pieces[movingPiece].style.left = (infoPieces[movingPiece][4]) + "px";
		pieces[movingPiece].style.top = (infoPieces[movingPiece][5]) + "px";
		inRotation = 0;
		return;
	}
	if (inRotation == 3) {
		eltRotation.style.opacity = "0.5";
		inRotation = 4;
		rotationAngle = calculeAngleRotation(x, y);
		return;
	}

	if (inRotation == 4) {
		const rotationEvt = calculeAngleRotation(x, y);
		const anglePiece = (rotationEvt - rotationAngle);
		rotationAngle = rotationEvt;
		infoPieces[movingPiece][3] = (360 + (infoPieces[movingPiece][3] - anglePiece)) % 360
		pieces[movingPiece].style.transform = "rotate(" + infoPieces[movingPiece][3] + "deg)";
	}

}

function calculeAngleRotation(x, y) {
	const rotationRect = eltRotation.getBoundingClientRect();
	const rayonX = x - rotationRect.left - 50;
	const rayonY = 50 - y + rotationRect.top;
	if (rayonX == 0) { return rotationAngle; }
	const angle = Math.atan(rayonY / rayonX) * 180 / Math.PI;
	if (rayonX > 0) {
		return angle
	} else {
		return angle + 180;
	}

}

/* Get into full screen */
function GoInFullscreen() {
	try {
		const element = document.documentElement;
		if (element.requestFullscreen)
			element.requestFullscreen();
		else if (element.mozRequestFullScreen)
			element.mozRequestFullScreen();
		else if (element.webkitRequestFullscreen)
			element.webkitRequestFullscreen();
		else if (element.msRequestFullscreen)
			element.msRequestFullscreen();
	}
	catch (err) {
	}
}

/* Get out of full screen */
function GoOutFullscreen() {
	try {
		if (document.exitFullscreen)
			document.exitFullscreen();
		else if (document.mozCancelFullScreen)
			document.mozCancelFullScreen();
		else if (document.webkitExitFullscreen)
			document.webkitExitFullscreen();
		else if (document.msExitFullscreen)
			document.msExitFullscreen();
	}
	catch (err) {
	}

}
