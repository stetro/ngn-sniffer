express  = require 'express'
router = express.Router()
mongoose = require 'mongoose'
Measurement  = mongoose.model 'Measurement'

module.exports = (app) ->
  app.use '/signal/', router


router.post '/', (req, res, next) ->
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
    
