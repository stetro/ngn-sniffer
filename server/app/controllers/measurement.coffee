express  = require 'express'
router = express.Router()
mongoose = require 'mongoose'
measurementLib = require '../lib/measurementLib'
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
    measurementLib.createOrReplaceMeasurement(measurements, req).save (err)->
      if err
        res.status(500).end()
      else
        res.end()

# get measurements for wifi access points with GET /measurement/wifi
router.get '/wifi', measurementLib.validateParameters, (req, res, next) ->
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
    res.json(data.map (item) ->
      if(parseFloat(item.wifiAPs) < 20.0)
        return [item.location.coordinates[0], item.location.coordinates[1], parseFloat(item.wifiAPs)/20.0]
      else
        return [item.location.coordinates[0], item.location.coordinates[1], 1.0]
    )
  )

# get measurements for signal strength with GET /measurement/signal
router.get '/signal', measurementLib.validateParameters, (req, res, next) ->
  query = 
    location:
      $geoWithin:
        $box: [
          [ parseFloat(req.query.swlat), parseFloat(req.query.swlng) ]
          [ parseFloat(req.query.nelat), parseFloat(req.query.nelng) ]
        ]
  if req.query.edgeOnly == 'true'
    console.log 'filtering ...'
    query.type = 'EDGE'
  Measurement.find query , (err, data)->
    if err
      res.status(500)
      return
    res.json(data.map (item) ->
      return [item.location.coordinates[0], item.location.coordinates[1],  parseFloat(item.signalDBm) / 31.0]
    )

# get plain measurements with GET /measurement/
router.get '/', measurementLib.validateParameters, (req, res, next) ->
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
    res.json data

