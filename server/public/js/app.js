'use strict';

var application = angular.module('ngn', ['leaflet-directive', 'ngResource']);

application.factory('Measurement', function($resource) {
  return $resource('/measurement', {}, {
    getWifiPoints: {
      url: '/measurement/wifi',
      method: 'GET',
      isArray: true
    },
    getSignalPoints: {
      url: '/measurement/signal',
      method: 'GET',
      isArray: true
    }
  });
});

application.controller('MapController', function($scope, $http, Measurement) {
  var dataPoints = [];
  angular.extend($scope, {
    measurement: {
      type: 'EDGE',
      signalDBm: 15,
      wifiAPs: 1
    },
    types: ['CDMA', 'EDGE', 'GSM', 'LTE'],
    bounds: {
      northEast: {
        lat: 50.9412313855822,
        lng: 7.005071640014648
      },
      southWest: {
        lat: 50.92500353699139,
        lng: 6.970396041870117
      }
    },
    center: {
      lat: 50.93371318638334,
      lng: 6.986961364746094,
      zoom: 15
    },
    events: {
      map: {
        enable: ['moveend', 'click'],
        logic: 'emit'
      }
    },
    markers: {
      measurementMarker: {
        lat: 51,
        lng: 0,
        focus: true,
        draggable: true
      }
    },
    layers: {
      baselayers: {
        osm: {
          //name: 'OpenStreetMap',
          name: 'Stamen Toner Lite',
          //url: 'http://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png',
          url: 'http://a.tile.stamen.com/toner-labels/{z}/{x}/{y}.png',
          type: 'xyz'
        },
        googleRoadmap: {
          name: 'Google Maps',
          layerType: 'ROADMAP',
          type: 'google'
        }
      }
    }
  });
  $scope.layers.overlays = {
    wifiAPs: {
      name: 'Wifi APs',
      type: 'heatmap',
      data: dataPoints,
      visible: false,
      layerOptions: {
        size: 250
      }
    },
    signalDBm: {
      name: 'Mobile Connectivity',
      type: 'heatmap',
      data: dataPoints,
      visible: true,
      layerOptions: {
        size: 250
      }
    }
  };

  var reloadData = function() {
    Measurement.getWifiPoints({
      'nelat': $scope.bounds.northEast.lat,
      'nelng': $scope.bounds.northEast.lng,
      'swlat': $scope.bounds.southWest.lat,
      'swlng': $scope.bounds.southWest.lng
    }, function(data) {
      $scope.layers.overlays.wifiAPs.data = data;
    });
    Measurement.getSignalPoints({
      'nelat': $scope.bounds.northEast.lat,
      'nelng': $scope.bounds.northEast.lng,
      'swlat': $scope.bounds.southWest.lat,
      'swlng': $scope.bounds.southWest.lng
    }, function(data) {
      $scope.layers.overlays.signalDBm.data = data;
    });
  };

  var getLocation = function() {
    navigator.geolocation.getCurrentPosition(function(position) {
      $scope.$apply(function() {
        $scope.center.lat = position.coords.latitude;
        $scope.center.lng = position.coords.longitude;
      });
    });
  };

  $scope.updateMarkerPosition = function() {
    $scope.markers.measurementMarker.lat = $scope.center.lat;
    $scope.markers.measurementMarker.lng = $scope.center.lng;
  };

  $scope.saveMeasurement = function() {
    var measurement = {
      lat: $scope.markers.measurementMarker.lat,
      lng: $scope.markers.measurementMarker.lng
    };
    angular.extend(measurement, $scope.measurement);
    var newMeasurement = new Measurement(measurement);
    newMeasurement.$save(function() {
      $scope.failure = false;
      reloadData();
    }, function() {
      $scope.failure = true;
    });
  };

  $scope.$on('leafletDirectiveMap.moveend', function(event, data) {
    reloadData();
  });

  $scope.$on('leafletDirectiveMap.click', function(event, e) {
    $scope.markers.measurementMarker.lat = e.leafletEvent.latlng.lat;
    $scope.markers.measurementMarker.lng = e.leafletEvent.latlng.lng;
  });

  $scope.lookUpAddress = function() {
    delete $http.defaults.headers.common['X-Requested-With'];
    $http
      .get('http://maps.google.com/maps/api/geocode/json?sensor=true&address=' + $scope.search)
      .success(function(data) {
        if (data.results.length > 0) {
          $scope.center = {
            lat: data.results[0].geometry.location.lat,
            lng: data.results[0].geometry.location.lng,
            zoom: 15
          };
          $scope.search = '';
        }
      });
  };

  reloadData();
  getLocation();

});
