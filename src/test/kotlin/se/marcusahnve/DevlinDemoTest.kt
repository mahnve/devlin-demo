package se.marcusahnve

import io.kotest.core.spec.style.ExpectSpec
import io.kotest.core.spec.style.ShouldSpec
import io.kotest.matchers.shouldBe
import org.http4k.client.OkHttp
import org.http4k.core.Method.GET
import org.http4k.core.Request
import org.http4k.core.Response
import org.http4k.core.Status.Companion.OK

class DevlinDemoTest : ExpectSpec({


    context("Calling static App") {
        val response = staticApp(Request(GET, "/"))
        expect("body is 'Hello World'") {
            response.body.toString() shouldBe "Hello World"
        }

        expect("return code to be 200") {
            response.status shouldBe OK
        }
    }






































    context("Calling parameterized app with name query set to DevLin") {
        val response = parameterizedApp(Request(GET, "/").query("name", "DevLin"))
        expect("body to be Hello Devlin") {
            response.body.toString() shouldBe "Hello DevLin"
        }

        expect("response status to be 200") {
            response.status shouldBe OK
        }
    }






































    context("routed app") {

        context("calling /helloworld") {
            val response = routedApp(Request(GET, "/helloworld"))
            expect("body to be Hello World") {
                response.body.toString() shouldBe "Hello World"
            }

            expect("response status to be 200") {
                response.status shouldBe OK
            }

            context("calling /queryparams?name=DevLin") {
                val response = routedApp(Request(GET, "/queryparams").query("name", "DevLin"))
                expect("body to be Hello DevLin") {
                    response.body.toString() shouldBe "Hello DevLin"
                }
                expect("response status to be 200") {
                    response.status shouldBe OK
                }
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
            expect("body to be Hello World") {
                response.body.toString() shouldBe "Hello World"
            }
            expect("response status to be 200") {
                response.status shouldBe OK
            }
        }

        context("calling /queryparams?name=DevLin") {
            val response = client(Request(GET, "http://localhost:9000/queryparams").query("name", "DevLin"))
            expect("body to be Hello DevLin") {
                response.body.toString() shouldBe "Hello DevLin"
            }
            expect("response status to be 200") {
                response.status shouldBe OK
            }

        }

    }
})
