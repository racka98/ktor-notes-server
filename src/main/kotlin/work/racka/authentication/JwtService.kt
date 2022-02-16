package work.racka.authentication

import com.auth0.jwt.JWT
import com.auth0.jwt.JWTVerifier
import work.racka.data.model.User

class JwtService(val verifier: JWTVerifier) {

    /*val verifier: JWTVerifier = JWT.require(Auth.algorithm)
        .withIssuer(Auth.ISSUER)
        .build()*/


    fun generateToken(user: User): String = JWT
        .create()
        .withSubject(Auth.SUBJECT)
        .withIssuer(Auth.ISSUER)
        .withClaim("email", user.email)
        .sign(Auth.algorithm)
}