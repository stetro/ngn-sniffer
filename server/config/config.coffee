path     = require 'path'
rootPath = path.normalize __dirname + '/..'
env      = process.env.NODE_ENV || 'development'
port     = process.env.PORT || 5000
db       = process.env.MONGODB || 'mongodb://localhost/server-development'

config =
  development:
    root: rootPath
    app:
      name: 'server'
    port: port
    db: db

  production:
    root: rootPath
    app:
      name: 'server'
    port: port
    db: db

module.exports = config[env]
