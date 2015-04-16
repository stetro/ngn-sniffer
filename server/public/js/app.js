'use strict';

var application = angular.module('ngn', ['leaflet-directive']);

application.controller('MapController', function($scope, $http) {
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
          name: 'OpenStreetMap',
          url: 'http://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png',
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
      visible: true,
      layerOptions: {
        size: 200
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
    var measurement = {
      lat: $scope.markers.measurementMarker.lat,
      lng: $scope.markers.measurementMarker.lng
    };
    angular.extend(measurement, $scope.measurement);
    $http.post('/measurement/', measurement).success(function() {
      $scope.failure = false;
      reloadData();
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