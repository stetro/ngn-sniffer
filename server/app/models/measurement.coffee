mongoose = require 'mongoose'
Schema   = mongoose.Schema

MeasurementSchema = new Schema(
  signalDBm: Number
  wifiAPs: Number
  location:
    type: [Number],
    index: '2dsphere'
)

MeasurementSchema.virtual('date').get (-> this._id.getTimestamp())

mongoose.model 'Measurement', MeasurementSchema

