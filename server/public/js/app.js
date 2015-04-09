'use strict';

var application = angular.module('ngn', ['ngResource', 'leaflet-directive']);

application.controller('MapController', function($scope, $http) {
  var dataPoints = [];
  angular.extend($scope, {
    center: {
      lat: 51.505,
      lng: -0.09,
      zoom: 15
    },
    events: {
      map: {
        enable: ['moveend'],
        logic: 'emit'
      }
    },
    layers: {
      baselayers: {
        osm: {
          name: 'OpenStreetMap',
          url: 'http://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png',
          type: 'xyz'
        }
      },
      overlays: {
        heatmap: {
          name: 'Heat Map',
          type: 'heatmap',
          data: dataPoints,
          visible: true,
          layerOptions: {
            size: 200
          }
        }
      }
    }
  });

  $scope.reloadData = function() {
    $http.get('/wifi/').success(function(data) {
      $scope.layers.overlays.heatmap.data = data;
    });
  };



  $scope.$on('leafletDirectiveMap.moveend', function(event) {
    $scope.reloadData();
  });


  $scope.reloadData();
});