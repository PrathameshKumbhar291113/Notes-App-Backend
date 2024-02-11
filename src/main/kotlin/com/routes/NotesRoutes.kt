package com.routes

import com.data.model.Notes
import com.data.model.SimpleResponse
import com.data.model.User
import com.repository.NotesRepository
import com.utils.JWT_DECLARATION
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*


const val NOTES = "$API_VERSION/notes"

const val ADD_NOTE = "$NOTES/add-note"
const val DELETE_NOTE = "$NOTES/delete-note"
const val UPDATE_NOTE = "$NOTES/update-note"
const val GET_NOTE = "$NOTES/get-note"
fun Route.notesRoutes(
    database : NotesRepository,
){

    authenticate(JWT_DECLARATION) {

        post(ADD_NOTE){

            val note = try {
                call.receive<Notes>()
            } catch (e:Exception){
                call.respond(HttpStatusCode.BadRequest, SimpleResponse(false,"Missing Fields", response = null))
                return@post
            }
            try {

                val userId = call.principal<User>()?.userId
                userId?.let {
                    database.addNote(note,userId)
                    call.respond(HttpStatusCode.OK,SimpleResponse(true,"Note Added Successfully!", response = note))
                }
            } catch (e:Exception){
                call.respond(HttpStatusCode.Conflict,SimpleResponse(false,e.message ?: "Some Problem Occurred!", response = null))
            }
        }

        get (GET_NOTE){
            try {
                val userId = call.principal<User>()?.userId
                userId?.let{
                    val notes = database.getAllNotes(userId)
                    call.respond(HttpStatusCode.OK,notes)
                }
            } catch (e:Exception){
                call.respond(HttpStatusCode.Conflict, emptyList<Notes>())
            }
        }

        post(UPDATE_NOTE) {

            val note = try {
                call.receive<Notes>()
            } catch (e:Exception){
                call.respond(HttpStatusCode.BadRequest,SimpleResponse(false,"Missing Fields", response = null))
                return@post
            }

            try {
                val userId = call.principal<User>()?.userId
                userId?.let {
                    database.updateNotes(note,userId)
                    call.respond(HttpStatusCode.OK,SimpleResponse(true,"Note Updated Successfully!", response = note))
                }
            } catch (e:Exception){
                call.respond(HttpStatusCode.Conflict,SimpleResponse(false,e.message ?: "Some Problem Occurred!", response = null))
            }

        }


        delete(DELETE_NOTE) {

            val noteId = try{
                call.request.queryParameters["noteId"]
            }catch (e:Exception){
                call.respond(HttpStatusCode.BadRequest,SimpleResponse(false,"QueryParameter:id is not present", response = null))
                return@delete
            }

            try {
                val userId = call.principal<User>()?.userId
                userId?.let {
                    noteId?.let {
                        database.deleteNotes(noteId, userId)
                        call.respond(HttpStatusCode.OK, SimpleResponse(true, "Note Deleted Successfully!", response = null))
                    }
                }
            } catch (e:Exception){
                call.respond(HttpStatusCode.Conflict,SimpleResponse(false, e.message ?: "Some problem Occurred!", response = null))
            }
        }

    }
}