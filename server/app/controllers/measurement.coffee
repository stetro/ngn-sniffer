express  = require 'express'
router = express.Router()
mongoose = require 'mongoose'
Measurement  = mongoose.model 'Measurement'

module.exports = (app) ->
  app.use '/measurement/', router

router.post '/', (req,res,next) ->
  measurement = new Measurement(
    signalDBm: req.body.signalDBm
    wifiAPs: req.body.wifiAPs
    location: [req.body.lat, req.body.lng]
  )
  measurement.save (err) ->
    res.end()
    
router.get '/', (req, res, next) ->
  Measurement.find (err, measurements) ->
    res.json(measurements)
