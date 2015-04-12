path     = require 'path'
rootPath = path.normalize __dirname + '/..'
env      = process.env.NODE_ENV || 'development'
port 	 = process.env.PORT || 5000

config =
  development:
    root: rootPath
    app:
      name: 'server'
    port: port
    db: 'mongodb://localhost/server-development'

  test:
    root: rootPath
    app:
      name: 'server'
    port: port
    db: 'mongodb://localhost/server-test'

  production:
    root: rootPath
    app:
      name: 'server'
    port: port
    db: 'mongodb://localhost/server-production'

module.exports = config[env]
