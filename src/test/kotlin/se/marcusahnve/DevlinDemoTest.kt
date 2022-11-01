package se.marcusahnve

import io.kotest.core.spec.style.ShouldSpec
import io.kotest.matchers.shouldBe
import org.http4k.client.OkHttp
import org.http4k.core.Method.GET
import org.http4k.core.Request
import org.http4k.core.Response
import org.http4k.core.Status.Companion.OK

class DevlinDemoTest : ShouldSpec({



    context("Calling static App") {
        val response = staticApp(Request(GET, "/"))
        should("return Hello World") {
            response shouldBe Response(OK).body("Hello World")
        }
    }






































    context("Calling parameterized app with name query set to DevLin") {
        val response = parameterizedApp(Request(GET, "/").query("name", "DevLin"))
        should("return Hello Marcus") {
            response shouldBe Response(OK).body("Hello DevLin")
        }
    }






































    context("routed app") {

        context("calling /helloworld") {
            val response = routedApp(Request(GET, "/helloworld"))
            should("return Hello World") {
                response shouldBe Response(OK).body("Hello World!")
            }
        }

        context("calling /queryparams?name=DevLin") {
            val response = routedApp(Request(GET, "/queryparams").query("name", "Devlin"))
            should("return Hello World") {
                response shouldBe Response(OK).body("Hello Devlin")
            }
        }

    }

    context("running for real") {
        beforeTest {
            server.stop()
            server.start()
        }
        afterTest { server.stop() }

        val client = OkHttp()

        context("calling /helloworld") {
            val response = client(Request(GET, "http://localhost:9000/helloworld"))
            should("return Hello World") {
                response shouldBe Response(OK).body("Hello World!")
            }
        }

        context("calling /queryparams?name=DevLin") {
            val response = client(Request(GET, "http://localhost:9000/queryparams").query("name", "Devlin"))
            should("return Hello World") {
                response shouldBe Response(OK).body("Hello Devlin")
            }
        }

    }
})
