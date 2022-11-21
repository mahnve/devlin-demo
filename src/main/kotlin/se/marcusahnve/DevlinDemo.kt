package se.marcusahnve

import org.http4k.core.HttpHandler
import org.http4k.core.Method.GET
import org.http4k.core.Request
import org.http4k.core.Response
import org.http4k.core.Status.Companion.OK
import org.http4k.routing.bind
import org.http4k.routing.routes
import org.http4k.server.Undertow
import org.http4k.server.asServer
import java.sql.Connection
import java.sql.DriverManager
import java.sql.PreparedStatement
import java.sql.ResultSet


val searchHandler: HttpHandler = fun(request: Request): Response {
    val searchString = request.query("q")
    val searchFunction = createSearchFunction(searchString)
    val resultSet = queryDatabase(searchFunction)
    val searchResult = readResultSet(resultSet)
    return Response(OK).body("Search result: $searchResult")
}

fun queryDatabase(func: (Connection) -> ResultSet): ResultSet {
    val connection: Connection = DriverManager.getConnection("jdbc:sqlite:devlindemo.db")
    return func(connection)
}

fun readResultSet(resultSet: ResultSet): String {
    while (resultSet.next()) {
        return resultSet.getString("hex")
    }
    return ""
}


fun createSearchFunction(searchString: String?): (Connection) -> ResultSet {
    return { connection: Connection ->
        val statement: PreparedStatement = connection.prepareStatement("SELECT * FROM colors WHERE color = ?")
        statement.setString(1, searchString)
        statement.executeQuery()
    }

}

val staticHandler: HttpHandler = fun(_: Request): Response {
    return Response(OK).body("Hello World")
}


val parameterizedHandler: HttpHandler = fun(request: Request): Response {
    val name = request.query("name")
    return Response(OK).body("Hello $name")
}


val routedApp: HttpHandler = routes(
    "/helloworld" bind GET to staticHandler,
    "/queryparams" bind GET to parameterizedHandler,
    "/search" bind GET to searchHandler
)


val server = routedApp.asServer(Undertow(9000))

fun start() {
    server.start()
}

fun stop() {
    server.stop()
}

fun main() {
    start()
    println("Server started on " + server.port())
}
