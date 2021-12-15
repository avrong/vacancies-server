package me.avrong.plugins

import io.ktor.serialization.*
import io.ktor.features.*
import io.ktor.application.*
import io.ktor.http.*
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.*
import me.avrong.models.VacancyIn
import me.avrong.models.VacancyDAO
import org.jetbrains.exposed.sql.transactions.transaction

fun Application.configureJsonApi() {
    install(ContentNegotiation) {
        json()
    }

    routing {
        get("/getVacancy") {
            val id = call.parameters["id"]?.toInt()
                ?: return@get call.respond(HttpStatusCode.BadRequest, "Id not specified")

            val vacancy = transaction { VacancyDAO.findById(id) }?.toOutObject()
                ?: return@get call.respond(HttpStatusCode.NotFound, "Vacancy not found")

            call.respond(vacancy)
        }

        post("/addVacancy") {
            val vacancy = try {
                call.receiveOrNull<VacancyIn>() ?: return@post call.respond(HttpStatusCode.BadRequest, "Bad request")
            } catch (e: kotlinx.serialization.SerializationException) {
                return@post call.respond(HttpStatusCode.BadRequest, "Bad request")
            }

            val newVacancy = transaction {
                VacancyDAO.new {
                    name = vacancy.name
                    minSalary = vacancy.minSalary
                    maxSalary = vacancy.maxSalary
                    schedule = vacancy.schedule
                    publishedDate = vacancy.publishedDate
                }
            }.toOutObject()

            call.respond(newVacancy)
        }
    }
}
