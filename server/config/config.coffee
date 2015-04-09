path     = require 'path'
rootPath = path.normalize __dirname + '/..'
env      = process.env.NODE_ENV || 'development'

config =
  development:
    root: rootPath
    app:
      name: 'server'
    port: 3000
    db: 'mongodb://localhost/server-development'

  test:
    root: rootPath
    app:
      name: 'server'
    port: 3000
    db: 'mongodb://localhost/server-test'

  production:
    root: rootPath
    app:
      name: 'server'
    port: 3000
    db: 'mongodb://localhost/server-production'

module.exports = config[env]
