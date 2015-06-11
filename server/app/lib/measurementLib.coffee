mongoose = require 'mongoose'
Measurement  = mongoose.model 'Measurement'



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
	  # avg the measurement
	  measurement = measurements[0]
	  measurement.signalDBm = Math.floor((parseInt(req.body.signalDBm) + parseInt(measurement.signalDBm)) / 2)
	  measurement.wifiAPs = Math.floor((parseInt(req.body.wifiAPs) + parseInt(measurement.wifiAPs)) / 2)
	  measurement.type = req.body.type
	  return measurement

exports.validateParameters = (req, res, next) ->
  if(distanceKm(req.query.nelat, req.query.nelng, req.query.swlat, req.query.swlng) > 10.0)
    res.json([])
    return
  if req.query.nelat is undefined or req.query.swlat is undefined or req.query.nelng is undefined or req.query.swlng is undefined
    res.json([])
    return
  next()

distanceKm = (lat1, lon1, lat2, lon2) -> 
  R = 6371
  dLat = (lat2 - lat1) * Math.PI / 180
  dLon = (lon2 - lon1) * Math.PI / 180
  a = Math.sin(dLat / 2) * Math.sin(dLat / 2) + Math.cos(lat1 * Math.PI / 180) * Math.cos(lat2 * Math.PI / 180) * Math.sin(dLon / 2) * Math.sin(dLon / 2)
  c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a))
  d = R * c
  return d