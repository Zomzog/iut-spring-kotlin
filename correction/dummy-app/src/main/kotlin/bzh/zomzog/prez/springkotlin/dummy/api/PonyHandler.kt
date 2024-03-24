package bzh.zomzog.prez.springkotlin.dummy.api

import bzh.zomzog.prez.springkotlin.dummy.api.dto.PonyDTO
import bzh.zomzog.prez.springkotlin.dummy.api.dto.asDTO
import bzh.zomzog.prez.springkotlin.dummy.domain.AlreadyExistError
import bzh.zomzog.prez.springkotlin.dummy.service.PonyService
import kotlinx.coroutines.flow.map
import org.springframework.http.HttpStatus
import org.springframework.http.HttpStatusCode
import org.springframework.http.client.reactive.ReactorClientHttpConnector
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.client.WebClient
import org.springframework.web.reactive.function.client.bodyToMono
import org.springframework.web.reactive.function.server.*
import org.springframework.web.server.ResponseStatusException
import org.springframework.web.util.UriBuilder
import reactor.core.publisher.Mono
import reactor.netty.http.client.HttpClient
import reactor.util.retry.Retry
import reactor.util.retry.RetryBackoffSpec
import java.time.Duration

@Component
class PonyHandler(val ponyService: PonyService) {

    suspend fun createPony(request: ServerRequest): ServerResponse {
        val dto: PonyDTO = request.awaitBody()
        try {
            val pony = ponyService.createPony(dto.toDomain())
            return ServerResponse.ok().bodyValueAndAwait(pony.asDTO())
        } catch (error: AlreadyExistError) {
            return ServerResponse.status(HttpStatus.CONFLICT).bodyValueAndAwait("")
        }
    }

    suspend fun listPonies(request: ServerRequest) = ponyService.listPonies()
            .map { it.asDTO() }
            .let {
                ServerResponse.ok().bodyAndAwait(it)
            }

    suspend fun findById(request: ServerRequest) = try {
        val id = request.pathVariable("id").toInt()
        ponyService.finfById(id)?.let {
            ServerResponse.ok().bodyValueAndAwait(it.asDTO())
        } ?: ServerResponse.notFound().buildAndAwait()
    } catch (e: Exception) {
        when (e) {
            is IllegalArgumentException ->
                ServerResponse.badRequest().bodyValueAndAwait("invalid id")

            else -> ServerResponse.status(HttpStatus.INTERNAL_SERVER_ERROR).bodyValueAndAwait("Internal error")
        }
    }

    suspend fun deleteById(request: ServerRequest) = try {
        val id = request.pathVariable("id").toInt()
        ponyService.deleteById(id)
        ServerResponse.ok().buildAndAwait()
    } catch (e: Exception) {
        when (e) {
            is IllegalArgumentException ->
                ServerResponse.badRequest().bodyValueAndAwait("invalid id")

            else -> ServerResponse.status(HttpStatus.INTERNAL_SERVER_ERROR).bodyValueAndAwait("Internal error")
        }
    }

    suspend fun updateById(request: ServerRequest) = try {
        val id = request.pathVariable("id").toInt()
        val dto: PonyDTO = request.awaitBody()
        ponyService.updateById(id, dto.toDomain()).let {
            ServerResponse.ok().bodyValueAndAwait(it)
        }
    } catch (e: Exception) {
        when (e) {
            is IllegalArgumentException ->
                ServerResponse.badRequest().bodyValueAndAwait("invalid id")

            else -> ServerResponse.status(HttpStatus.INTERNAL_SERVER_ERROR).bodyValueAndAwait("Internal error ${e.message}")
        }
    }
}