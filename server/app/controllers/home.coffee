express  = require 'express'
router = express.Router()
mongoose = require 'mongoose'

module.exports = (app) ->
  app.use '/', router

# rendering default route
router.get '/', (req, res, next) ->
	res.render 'index', title: 'NGN-Sniffer Map'
  