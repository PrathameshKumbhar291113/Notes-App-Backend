package com.routes

import io.ktor.server.routing.*



const val NOTES = "$API_VERSION/notes"

const val ADD_NOTE = "$NOTES/add-note"
const val DELETE_NOTE = "$NOTES/delete-note"
const val UPDATE_NOTE = "$NOTES/update-note"
const val GET_NOTE = "$NOTES/get-note"
fun Route.notesRoutes(){

    get (GET_NOTE){

    }

}