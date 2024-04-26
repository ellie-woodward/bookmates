package com.example.myapplication

import androidx.room.PrimaryKey
import java.util.UUID

data class Template(
    @PrimaryKey val templateId: UUID = UUID.randomUUID(),
    val gameName: String,
    val maxPlayers: Int,
    val scoreType: String,
    val rows: Int,
    val rowTitles: List<String>?
)


object TemplatesList {
    private val templates: MutableList<Template> = mutableListOf()

    init{
        templates.add(Template(
            templateId = UUID.randomUUID(),
            gameName = "Chess",
            maxPlayers = 2,
            scoreType = "Rounds-Wins",
            rows = 0,
            rowTitles = null
        ))

        templates.add(Template(
            templateId = UUID.randomUUID(),
            gameName = "Uno",
            maxPlayers = 4,
            scoreType = "Rounds-Score",
            rows = 1,
            rowTitles = listOf("Score:")
        ))

        templates.add(Template(
            templateId = UUID.randomUUID(),
            gameName = "Spades",
            maxPlayers = 4,
            scoreType = "Rounds-Wins",
            rows = 1,
            rowTitles = listOf("Score:")
        ))
    }


    fun getTemplates(): List<Template>{
        return templates.toList()
    }

    fun getTemplateNames(): List<String>{
        return templates.map{template -> template.gameName}
    }


    fun addTemplates(template: Template){
        templates.add(template)
    }



}
