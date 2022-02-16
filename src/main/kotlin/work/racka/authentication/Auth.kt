package work.racka.authentication

import com.auth0.jwt.algorithms.Algorithm
import io.ktor.util.*
import javax.crypto.Mac
import javax.crypto.spec.SecretKeySpec
import kotlin.text.toByteArray

object Auth {
    const val ISSUER = "NoteServer"
    const val SUBJECT = "NoteAuthentication"
    private const val JWT_SECRET_VARIABLE = "JWT_SECRET"
    private const val HASH_KEY_VARIABLE = "HASH_SECRET_KEY"
    private const val HMAC_ALGORITHM = "HmacSHA1"


    private val jwtSecret: String = System.getenv(JWT_SECRET_VARIABLE)
    val algorithm: Algorithm = Algorithm.HMAC512(jwtSecret)

    private val hashKey = System.getenv(HASH_KEY_VARIABLE).toByteArray()
    private val hmacKey = SecretKeySpec(hashKey, HMAC_ALGORITHM)

    fun hash(password: String): String {
        val hmac = Mac.getInstance(HMAC_ALGORITHM)
        hmac.init(hmacKey)
        return hex(hmac.doFinal(password.toByteArray(Charsets.UTF_8)))
    }
}