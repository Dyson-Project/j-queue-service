package vn.unicloud.genericqueue.restproxy.controllers

import org.springdoc.api.annotations.ParameterObject
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.server.ResponseStatusException
import vn.unicloud.genericequeue.persistence.entities.Node
import vn.unicloud.genericequeue.persistence.repositories.NodeRepository

@RestController
@RequestMapping("/api/v1/queues")
class QueueController @Autowired constructor(
    private val nodeRepository: NodeRepository
) {
    @GetMapping
    fun list(): List<Any> {
        println(nodeRepository)
        return emptyList();
    }

    @GetMapping("/{queueId}/nodes")
    fun listNode(@ParameterObject pageable: Pageable): ResponseEntity<Page<Node>> {
        val page = nodeRepository.findAll(pageable)
        return ResponseEntity.ok(page)
    }

    @GetMapping("/{queueId}/nodes/{nodeId}")
    fun getNode(@PathVariable queueId: String, @PathVariable nodeId: String): ResponseEntity<Node> {
        val node = nodeRepository.findById(nodeId)
            .orElseThrow { ResponseStatusException(HttpStatus.NOT_FOUND, "Node not found") }
        return ResponseEntity.ok(node)
    }
}