express  = require 'express'
router = express.Router()
mongoose = require 'mongoose'
Measurement  = mongoose.model 'Measurement'

module.exports = (app) ->
  app.use '/measurement/', router

# create a new measurement instance on POST /measurement/
router.post '/', (req,res,next) ->
  # find a corresponding close measurement
  Measurement.find
    location:
      $geoNear: 
        spherical: true
        limit: 1
        $geometry: 
          type: 'Point' 
          coordinates: [req.body.lat, req.body.lng]
        $maxDistance: 100
  , (err, measurements)->
    if err
      console.log(err)
      res.status(500).end()
      return
    measurement = {}
    if measurements.length == 0
      # add a new measurement
      measurement = new Measurement(
        type: req.body.type
        signalDBm: req.body.signalDBm
        wifiAPs: req.body.wifiAPs
        location: 
          type: 'Point'
          coordinates: [req.body.lat, req.body.lng]
      )
    else
      # replace the measurement
      console.log('Replacing available measurement entry.')
      measurement = measurements[0]
      measurement.signalDBm = req.body.signalDBm
      measurement.wifiAPs = req.body.wifiAPs
      measurement.type = req.body.type

    measurement.save (err) ->
      if err
        res.status(500).end()
      else
        res.end()
    
# get measurements for wifi access points with GET /measurement/wifi
router.get '/wifi', (req, res, next) ->
  points = []
  if(distanceKm(req.query.nelat, req.query.nelng, req.query.swlat, req.query.swlng) > 10.0)
    res.json(points)
    return
  if req.query.nelat is undefined or req.query.swlat is undefined or req.query.nelng is undefined or req.query.swlng is undefined
    res.json(points)
    return
  Measurement.find(
    location:
      $geoWithin:
        $box: [
          [ parseFloat(req.query.swlat), parseFloat(req.query.swlng) ]
          [ parseFloat(req.query.nelat), parseFloat(req.query.nelng) ]
        ]
  , (err, data)->
    if err
      res.status(500)
      return
    for point in data
      points.push([point.location.coordinates[0], point.location.coordinates[1], 0.5])
    res.json(points)
  )

# get measurements for signal strength with GET /measurement/signal
router.get '/signal', (req, res, next) ->
  points = []
  if(distanceKm(req.query.nelat, req.query.nelng, req.query.swlat, req.query.swlng) > 10.0)
    res.json(points)
    return
  if req.query.nelat is undefined or req.query.swlat is undefined or req.query.nelng is undefined or req.query.swlng is undefined
    res.json(points)
    return
  Measurement.find
    location:
      $geoWithin:
        $box: [
          [ parseFloat(req.query.swlat), parseFloat(req.query.swlng) ]
          [ parseFloat(req.query.nelat), parseFloat(req.query.nelng) ]
        ]
  , (err, data)->
    if err
      res.status(500)
      return
    for point in data
      points.push([point.location.coordinates[0], point.location.coordinates[1],  parseFloat(point.signalDBm) / 31.0])
    res.json(points)


distanceKm = (lat1, lon1, lat2, lon2) -> 
  R = 6371
  dLat = (lat2 - lat1) * Math.PI / 180
  dLon = (lon2 - lon1) * Math.PI / 180
  a = Math.sin(dLat / 2) * Math.sin(dLat / 2) + Math.cos(lat1 * Math.PI / 180) * Math.cos(lat2 * Math.PI / 180) * Math.sin(dLon / 2) * Math.sin(dLon / 2)
  c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a))
  d = R * c
  return d
