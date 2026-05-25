package com.example.nobelserver.data.repository

import com.example.nobelserver.domain.model.Laureate
import com.example.nobelserver.domain.model.NobelPrize
import com.example.nobelserver.domain.model.User
import com.example.nobelserver.domain.repository.NobelRepository
import com.example.nobelserver.domain.repository.UserRepository

class InMemoryNobelRepository : NobelRepository {
    private val prizes = listOf(
        NobelPrize(
            id = "1",
            awardYear = "2023",
            category = "physics",
            laureates = listOf(
                Laureate("1", "Pierre Agostini", "1/3", "for experimental methods that generate attosecond pulses of light for the study of electron dynamics in matter", "United States of America"),
                Laureate("2", "Ferenc Krausz", "1/3", "for experimental methods that generate attosecond pulses of light for the study of electron dynamics in matter", "Hungary"),
                Laureate("3", "Anne L'Huillier", "1/3", "for experimental methods that generate attosecond pulses of light for the study of electron dynamics in matter", "France")
            )
        ),
        NobelPrize(
            id = "2",
            awardYear = "2023",
            category = "chemistry",
            laureates = listOf(
                Laureate("4", "Moungi G. Bawendi", "1/3", "for the discovery and synthesis of quantum dots", "United States of America"),
                Laureate("5", "Louis E. Brus", "1/3", "for the discovery and synthesis of quantum dots", "United States of America"),
                Laureate("6", "Alexei I. Ekimov", "1/3", "for the discovery and synthesis of quantum dots", "Russia")
            )
        ),
        NobelPrize(
            id = "3",
            awardYear = "2023",
            category = "literature",
            laureates = listOf(
                Laureate("7", "Jon Fosse", "1/1", "for his innovative plays and prose which give voice to the unsayable", "Norway")
            )
        ),
        NobelPrize(
            id = "4",
            awardYear = "2023",
            category = "peace",
            laureates = listOf(
                Laureate("8", "Narges Mohammadi", "1/1", "for her fight against the oppression of women in Iran and her efforts to promote human rights and freedom for all", "Iran")
            )
        ),
        NobelPrize(
            id = "5",
            awardYear = "2022",
            category = "physics",
            laureates = listOf(
                Laureate("9", "Alain Aspect", "1/3", "for experiments with entangled photons, establishing the violation of Bell inequalities and pioneering quantum information science", "France"),
                Laureate("10", "John F. Clauser", "1/3", "for experiments with entangled photons", "United States of America"),
                Laureate("11", "Anton Zeilinger", "1/3", "for experiments with entangled photons", "Austria")
            )
        ),
        NobelPrize(
            id = "6",
            awardYear = "2022",
            category = "medicine",
            laureates = listOf(
                Laureate("12", "Svante Pääbo", "1/1", "for his discoveries concerning the genomes of extinct hominins and human evolution", "Sweden")
            )
        ),
        NobelPrize(
            id = "7",
            awardYear = "2021",
            category = "physics",
            laureates = listOf(
                Laureate("13", "Syukuro Manabe", "1/4", "for the physical modelling of Earth's climate, quantifying variability and reliably predicting global warming", "United States of America"),
                Laureate("14", "Klaus Hasselmann", "1/4", "for the physical modelling of Earth's climate", "Germany"),
                Laureate("15", "Giorgio Parisi", "1/2", "for the discovery of the interplay of disorder and fluctuations in physical systems", "Italy")
            )
        )
    )

    override fun getAllPrizes(): List<NobelPrize> = prizes
    override fun getPrize(year: String, category: String): NobelPrize? =
        prizes.find { it.awardYear == year && it.category == category }
}

class InMemoryUserRepository : UserRepository {
    // Passwords are plain text for demo; in production use BCrypt
    private val users = mapOf(
        "admin" to User("admin", "admin123", "admin"),
        "user" to User("user", "user123", "user")
    )

    override fun findByUsername(username: String): User? = users[username]
}
