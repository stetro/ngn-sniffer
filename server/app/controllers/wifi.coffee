express  = require 'express'
router = express.Router()

module.exports = (app) ->
  app.use '/wifi/', router

points = [[51.505, -0.09, 0.5]];

counter = 0

router.get '/', (req, res, next) ->
  counter = counter + 1
  points.push([51.505, -0.09+(0.001*counter), 0.5]);
  res.json(points)
