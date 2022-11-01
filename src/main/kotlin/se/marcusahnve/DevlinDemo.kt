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

















val staticApp: HttpHandler = fun(_ :Request) : Response {
    return Response(OK).body("Hello World")
}



































val parameterizedApp: HttpHandler = fun(request: Request) :Response{
    val name = request.query("name")
    return Response(OK).body("Hello $name")
}






































val routedApp: HttpHandler = routes(
    "/helloworld" bind GET to staticApp,
    "/queryparams" bind GET to parameterizedApp
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
