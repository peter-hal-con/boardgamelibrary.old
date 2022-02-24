environments {
  production {
    dataSource {
      dbCreate = "update"
      driverClassName = "org.mariadb.jdbc.Driver"
      uri = new URI(System.env.JAWSDB_MARIA_URL)
      url = "jdbc:mariadb://" + uri.host + ":" + uri.port + uri.path
      username = uri.userInfo.split(":")[0]
      password = uri.userInfo.split(":")[1]
    }
  }
}