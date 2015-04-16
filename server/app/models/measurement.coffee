mongoose = require 'mongoose'
Schema   = mongoose.Schema

MeasurementSchema = new Schema(
  signalDBm: Number
  wifiAPs: Number
  type: String
  location:
    type:
      type: String
    coordinates: []
)

MeasurementSchema.index
  location: '2dsphere'

MeasurementSchema.virtual('date').get (-> this._id.getTimestamp())

mongoose.model 'Measurement', MeasurementSchema

