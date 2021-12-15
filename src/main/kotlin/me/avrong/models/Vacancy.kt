package me.avrong.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import me.avrong.DateSerializer
import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.javatime.datetime
import java.time.LocalDateTime

@Serializable
data class VacancyOut(
    val id: Int,
    @SerialName("vacancy_name")
    val name: String,
    @SerialName("salary_max")
    val maxSalary: Int,
    @SerialName("salary_min")
    val minSalary: Int,
    @SerialName("schedule")
    val schedule: String,
    @Serializable(DateSerializer::class)
    @SerialName("date_published")
    val publishedDate: LocalDateTime
)

@Serializable
data class VacancyIn(
    @SerialName("vacancy_name")
    val name: String,
    @SerialName("salary_max")
    val maxSalary: Int,
    @SerialName("salary_min")
    val minSalary: Int,
    @SerialName("schedule")
    val schedule: String,
    @Serializable(DateSerializer::class)
    @SerialName("date_published")
    val publishedDate: LocalDateTime
)

class VacancyDAO(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<VacancyDAO>(Vacancies)

    var name by Vacancies.name
    var minSalary by Vacancies.minSalary
    var maxSalary by Vacancies.maxSalary
    var schedule by Vacancies.schedule
    var publishedDate by Vacancies.publishedDate

    fun toOutObject() : VacancyOut = VacancyOut(
        id.value,
        name,
        minSalary,
        maxSalary,
        schedule,
        publishedDate
    )
}

object Vacancies : IntIdTable() {
    val name = varchar("name", 50)
    val minSalary = integer("salary_min")
    val maxSalary = integer("salary_max")
    val schedule = varchar("schedule", 50)
    val publishedDate = datetime("date_published")
}