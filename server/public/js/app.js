'use strict';

var application = angular.module('ngn', ['ngResource', 'leaflet-directive']);

application.controller('MapController', function($scope) {
  var dataPoints = [
    [51.505, -0.09, 0.5],
    [51.505, -0.091, 0.5],
    [51.505, -0.092, 0.5],
    [51.515, -0.092, 0.5],
    [51.525, -0.092, 0.5],
    [51.535, -0.092, 0.5]
  ];
  angular.extend($scope, {
    center: {
      lat: 51.505,
      lng: -0.09,
      zoom: 15
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
});