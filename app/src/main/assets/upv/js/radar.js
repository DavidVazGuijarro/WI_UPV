	var PoiRadar = 
	{

		hide: function hideFn() {
			AR.radar.enabled = false;
		},

		show: function initFn() 
		{

			// div definida en index.htm
			AR.radar.container = document.getElementById("radarContainer");

			// imagen de fondo del radar
			AR.radar.background = new AR.ImageResource("assets/radar_bg.png");

			// indicador del Norte
			AR.radar.northIndicator.image = new AR.ImageResource("assets/radar_north.png");

			// centrado
			AR.radar.centerX = 0.5;
			AR.radar.centerY = 0.5;

			AR.radar.radius = 0.3;
			AR.radar.northIndicator.radius = 0.0;

			AR.radar.enabled = true;
		},

		updatePosition: function updatePositionFn() {
			if (AR.radar.enabled) {
				AR.radar.notifyUpdateRadarPosition();
			}
		},

		clickedRadar: function clickedRadarFn() {
			//alert("Radar Clicked");
		},

		setMaxDistance: function setMaxDistanceFn(maxDistanceMeters) {
			AR.radar.maxDistance = maxDistanceMeters;
		}
	};