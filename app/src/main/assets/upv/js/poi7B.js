// implementacion de AR-Experience (alias "World" de Wikitude)
// javivas
var World = 
{
	// solo se obtienen datos una vez por llamada a la vista
	isRequestingData: false,

	// flag de datos cargados
	initiallyLoadedData: false,

	markerDrawable_idle: null,
	markerDrawable_selected: null,
	markerDrawable_directionIndicator: null,

	// lista de AR.GeoObjects que se muestran actualmente en la escena/World
	markerList: [],

	// ultimo marker seleccionado (solo hay uno)
	currentMarker: null,

	locationUpdateCounter: 0,
	updatePlacemarkDistancesEveryXLocationUpdates: 10,

	// cargar nuevos datos de POI
	loadPoisFromJsonData: function loadPoisFromJsonDataFn(poiData) 
	{

		// mostrar radar y establecer evento de click
		PoiRadar.show();
		$('#radarContainer').unbind('click');
		$("#radarContainer").click(PoiRadar.clickedRadar);

		// markers visibles
		World.markerList = [];

		// cargar assets de los markers
		World.markerDrawable_idle = new AR.ImageResource("assets/marker_idle.png");
		World.markerDrawable_selected = new AR.ImageResource("assets/marker_selected.png");
		World.markerDrawable_directionIndicator = new AR.ImageResource("assets/indi.png");

		World.markerList.push(new Marker(poiData));
		
		// actualizar distancia
		World.updateDistanceToUserValues();

		World.updateStatusMessage('Click target to show more info');
	},

	updateDistanceToUserValues: function updateDistanceToUserValuesFn() {
		for (var i = 0; i < World.markerList.length; i++) {
			World.markerList[i].distanceToUser = World.markerList[i].markerObject.locations[0].distanceToUser();
		}
	},

	// actualizar mensaje de status en el boton "i"
	updateStatusMessage: function updateStatusMessageFn(message, isWarning) {

		var themeToUse = isWarning ? "e" : "c";
		var iconToUse = isWarning ? "alert" : "info";

		$("#status-message").html(message);
		$("#popupInfoButton").buttonMarkup({
			theme: themeToUse
		});
		$("#popupInfoButton").buttonMarkup({
			icon: iconToUse
		});
	},

	// actualizar localizacion, se ejecuta cada vez que se invoca a architectView.setLocation() 
	locationChanged: function locationChangedFn(lat, lon, alt, acc) {

		// obtener datos si no se han cargado ya
		if (!World.initiallyLoadedData) 
		{
			//Julio
			var singlePoi = {
					"id": "1",
					"latitude": parseFloat(39.4804805),
					"longitude": parseFloat(-0.3383423),
					"altitude": parseFloat(100.0),
					"title": "7B",
					"description": "ETSID"
				};
			World.loadPoisFromJsonData(singlePoi);
			World.initiallyLoadedData = true;
		} else if (World.locationUpdateCounter === 0) {
			World.updateDistanceToUserValues();
		}

		// helper usado para actualizar info del marker de forma automatica (e.j. cada 10 location updates)
		World.locationUpdateCounter = (++World.locationUpdateCounter % World.updatePlacemarkDistancesEveryXLocationUpdates);
	},

	// evento click en el marker
	onMarkerSelected: function onMarkerSelectedFn(marker) {
		World.currentMarker = marker;

		// actualizar valor de los campos de detalle
		$("#poi-detail-title").html(marker.poiData.title);
		$("#poi-detail-description").html(marker.poiData.description);

		var distanceToUserValue = (marker.distanceToUser > 999) ? ((marker.distanceToUser / 1000).toFixed(2) + " km") : (Math.round(marker.distanceToUser) + " m");

		$("#poi-detail-distance").html(distanceToUserValue);

		// mostrar panel
		$("#panel-poidetail").panel("open", 123);
		
		$( ".ui-panel-dismiss" ).unbind("mousedown");

		$("#panel-poidetail").on("panelbeforeclose", function(event, ui) {
			World.currentMarker.setDeselected(World.currentMarker);
		});
	},

	// click en la pantalla fuera de cualquier marker
	onScreenClick: function onScreenClickFn() {
	},

	// Devuelve la distancia en metros
	getMaxDistance: function getMaxDistanceFn() {

		// Ordena los puntos en distancia descendente (si queremos mostrar mas de un marker)
		World.markerList.sort(World.sortByDistanceSortingDescending);

		// usar distanceToUser par obtener la maxima distancia
		var maxDistanceMeters = World.markerList[0].distanceToUser;

		return maxDistanceMeters * 1.1;
	},

	// helper para ordenar lugares por su distancia
	sortByDistanceSorting: function(a, b) {
		return a.distanceToUser - b.distanceToUser;
	},

	// idem, distancia descendente
	sortByDistanceSortingDescending: function(a, b) {
		return b.distanceToUser - a.distanceToUser;
	}

};


/* delegar cambios en localizacion de AR a nuestra funcion */
AR.context.onLocationChanged = World.locationChanged;

/* idem con eventos click */
AR.context.onScreenClick = World.onScreenClick;