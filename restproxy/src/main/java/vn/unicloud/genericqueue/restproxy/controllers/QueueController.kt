package vn.unicloud.genericqueue.restproxy.controllers

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/v1/queues")
class QueueController {
    @GetMapping
    fun listQueue(): List<Any> {
        return emptyList()
    }
}