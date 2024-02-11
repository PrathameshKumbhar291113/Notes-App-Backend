package com.routes

import com.auth.JWTService
import com.data.model.*
import com.repository.UserRepository
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

const val API_VERSION = "/v1"
const val USERS = "$API_VERSION/users"
const val REGISTER_REQUEST = "$USERS/register"
const val LOGIN_REQUEST = "$USERS/login"

fun Route.userRoutes(
    database : UserRepository,
    jwtService: JWTService,
    hashFunction: (String) -> String
){

        post (REGISTER_REQUEST) {
            val registerRequest = try{
                call.receive<UserRegisterRequest>()
            }catch (e: Exception){
                call.respond(HttpStatusCode.BadRequest, SimpleResponse(false, "Missing Some Fields", response = null))
                return@post
            }

            try {

                if (database.isUserEmailExists(registerRequest.email)){
                    call.respond(HttpStatusCode.BadRequest, SimpleResponse(false, "User Already Exist.", response = null))
                }else {
                    var userId : String = ""

                    do {
                        userId = (100000..999999).random().toString()
                    } while (database.isUserExists(userId))

                    val user = User(
                        userId,
                        registerRequest.name,
                        registerRequest.email,
                        hashFunction(registerRequest.password)
                    )

                    val userResponseWithoutPassword = UserResponseWithoutPassword(userName = user.userName, userId = user.userId , userEmail = user.userEmail)

                    val registerResponse = UserRegisterResponse(user = userResponseWithoutPassword, token = jwtService.generateToken(user))

                    database.addUser(user)
                    call.respond(
                        HttpStatusCode.OK,
                        SimpleResponse(
                            isSuccessFull = true,
                            message = "User Successfully Registered.",
                            response = registerResponse
                        )
                    )
                }
            }catch (e: Exception){
                call.respond(HttpStatusCode.Conflict,  SimpleResponse(false, e.message?: "Some error occurred.", response = null))
            }
        }

        post (LOGIN_REQUEST){
            val login = try {
                call.receive<UserLoginRequest>()
            }catch (e: Exception){
                call.respond(HttpStatusCode.BadRequest, SimpleResponse(false, "Missing Some Fields", response = null))
                return@post
            }

            try {
                val user = database.findUserByUserEmail(login.email)
                if (user == null){
                    call.respond(HttpStatusCode.BadRequest,  SimpleResponse(false, "Email Doesn't Exist.", response = null))
                }else{
                    if (user.userPassword == hashFunction(login.password)){
                        val userResponseWithoutPassword = UserResponseWithoutPassword(userName = user.userName, userId = user.userId , userEmail = user.userEmail)
                        val loginResponse = UserLoginResponse(user =  userResponseWithoutPassword , token = jwtService.generateToken(user))
                        call.respond(HttpStatusCode.OK, SimpleResponse(isSuccessFull = true, "Successfully Login.", response = loginResponse))
                    }else{
                        call.respond(HttpStatusCode.BadRequest,  SimpleResponse(false, "Password Doesn't Match.", response = null))
                    }
                }
            }catch (e: Exception){
                call.respond(HttpStatusCode.Conflict,  SimpleResponse(false, e.message?: "Some error occurred.", response = null))
            }

        }
}
