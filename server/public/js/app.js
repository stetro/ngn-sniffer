'use strict';

var application = angular.module('ngn', ['leaflet-directive']);

application.controller('MapController', function($scope, $http) {
  var dataPoints = [];
  angular.extend($scope, {
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
      lat: 51.505,
      lng: -0.09,
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
        googleTerrain: {
          name: 'Google Terrain',
          layerType: 'TERRAIN',
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
      visible: true,
      layerOptions: {
        size: 140
      }
    },
    signalDBm: {
      name: 'Mobile Connectivity',
      type: 'heatmap',
      data: dataPoints,
      visible: true,
      layerOptions: {
        size: 200
      }
    }
  };

  var reloadData = function() {
    $http.post('/wifi/', $scope.bounds).success(function(data) {
      $scope.layers.overlays.wifiAPs.data = data;
    });
    $http.post('/signal/', $scope.bounds).success(function(data) {
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
    $http.post('/measurement/', {
      lat: $scope.markers.measurementMarker.lat,
      lng: $scope.markers.measurementMarker.lng,
      signalDBm: $scope.signalDBm,
      wifiAPs: $scope.wifiAPs
    }).success(function() {
      $scope.failure = false;
      reloadData();
      $scope.signalDBm = undefined;
      $scope.wifiAPs = undefined;
    }).error(function() {
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

  reloadData();
  getLocation();

});