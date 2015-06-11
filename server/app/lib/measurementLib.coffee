mongoose = require 'mongoose'
Measurement  = mongoose.model 'Measurement'

exports.distanceKm = (lat1, lon1, lat2, lon2) -> 
  R = 6371
  dLat = (lat2 - lat1) * Math.PI / 180
  dLon = (lon2 - lon1) * Math.PI / 180
  a = Math.sin(dLat / 2) * Math.sin(dLat / 2) + Math.cos(lat1 * Math.PI / 180) * Math.cos(lat2 * Math.PI / 180) * Math.sin(dLon / 2) * Math.sin(dLon / 2)
  c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a))
  d = R * c
  return d

exports.createOrReplaceMeasurement = (measurements, req) ->
	# if there is a too close measurement
	if measurements.length == 0
	  # add a new measurement
	  return new Measurement(
	    type: req.body.type
	    signalDBm: req.body.signalDBm
	    wifiAPs: req.body.wifiAPs
	    location: 
	      type: 'Point'
	      coordinates: [req.body.lat, req.body.lng]
	  )
	else
	  # replace the measurement
	  measurement = measurements[0]
	  measurement.signalDBm = req.body.signalDBm
	  measurement.wifiAPs = req.body.wifiAPs
	  measurement.type = req.body.type
	  return measurement
