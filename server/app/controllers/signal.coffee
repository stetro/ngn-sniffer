express  = require 'express'
router = express.Router()
mongoose = require 'mongoose'
Measurement  = mongoose.model 'Measurement'

module.exports = (app) ->
  app.use '/signal/', router


router.get '/', (req, res, next) ->
  Measurement.find (err, data)->
    points = []
    for point in data
      points.push([point.location[0], point.location[1], 0.1])
    res.json(points)
    
