package me.avrong.vacancies

import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import io.ktor.application.*
import io.ktor.features.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import me.avrong.vacancies.models.Vacancies
import me.avrong.vacancies.models.VacancyDAO
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.StdOutSqlLogger
import org.jetbrains.exposed.sql.addLogger
import org.jetbrains.exposed.sql.transactions.transaction
import java.time.LocalDateTime

fun main() {
    Database.connect(hikari())

    transaction {
        addLogger(StdOutSqlLogger)
        SchemaUtils.create(Vacancies)

        VacancyDAO.new {
            name = "An example"
            minSalary = 10000
            maxSalary = 30000
            schedule = "part-time"
            publishedDate = LocalDateTime.now()
        }
    }

    embeddedServer(Netty, port = 8080, host = "127.0.0.1") {
        install(CORS) {
            anyHost()
        }

        configureJsonApi()
    }.start(wait = true)
}

private fun hikari(): HikariDataSource = HikariDataSource(
    HikariConfig().apply {
        driverClassName = "org.h2.Driver"
        jdbcUrl = "jdbc:h2:mem:test"
        maximumPoolSize = 3
        isAutoCommit = false
        transactionIsolation = "TRANSACTION_REPEATABLE_READ"
        validate()
    }
)