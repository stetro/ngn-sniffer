express  = require 'express'
router = express.Router()
mongoose = require 'mongoose'
Measurement  = mongoose.model 'Measurement'

module.exports = (app) ->
  app.use '/wifi/', router


router.post '/', (req, res, next) ->
  points = []
  if req.body.northEast is undefined or req.body.southWest is undefined
    return points
  Measurement.find(
    location:
      $geoWithin:
        $box: [
          [ req.body.northEast.lat, req.body.northEast.lng ]
          [ req.body.southWest.lat, req.body.southWest.lng ]
        ]
  , (err, data)->
    for point in data

      points.push([point.location.coordinates[0], point.location.coordinates[1], 0.5])
    res.json(points)
  )
    
