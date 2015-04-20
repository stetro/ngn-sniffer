express  = require 'express'
router = express.Router()
mongoose = require 'mongoose'
Measurement  = mongoose.model 'Measurement'

module.exports = (app) ->
  app.use '/measurement/', router

router.post '/', (req,res,next) ->
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
      console.log('Adding new measurement entry.')
      measurement = new Measurement(
        type: req.body.type
        signalDBm: req.body.signalDBm
        wifiAPs: req.body.wifiAPs
        location: 
          type: 'Point'
          coordinates: [req.body.lat, req.body.lng]
      )
    else
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
    

router.post '/wifi', (req, res, next) ->
  points = []
  if req.body.northEast is undefined or req.body.southWest is undefined
    return points
  Measurement.find(
    location:
      $geoWithin:
        $box: [
          [ req.body.southWest.lat, req.body.southWest.lng ]
          [ req.body.northEast.lat, req.body.northEast.lng ]
        ]
  , (err, data)->
    for point in data

      points.push([point.location.coordinates[0], point.location.coordinates[1], 0.5])
    res.json(points)
  )

router.post '/signal', (req, res, next) ->
  points = []
  if req.body.northEast is undefined or req.body.southWest is undefined
    return points
  Measurement.find
    location:
      $geoWithin:
        $box: [
          [ req.body.southWest.lat, req.body.southWest.lng ]
          [ req.body.northEast.lat, req.body.northEast.lng ]
        ]
  , (err, data)->
    for point in data
      points.push([point.location.coordinates[0], point.location.coordinates[1],  parseFloat(point.signalDBm) / 31.0])
    res.json(points)