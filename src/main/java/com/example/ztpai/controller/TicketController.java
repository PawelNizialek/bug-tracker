package com.example.ztpai.controller;

import com.example.ztpai.dto.TicketRequest;
import com.example.ztpai.dto.TicketResponse;
import com.example.ztpai.dto.UserRequest;
import com.example.ztpai.service.TicketService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.http.ResponseEntity.status;

@RestController
@RequestMapping("api/v1/ticket")
@CrossOrigin
public class TicketController {
    private final TicketService ticketService;

    public TicketController(TicketService ticketService) {
        this.ticketService = ticketService;
    }

    @GetMapping("/all")
    public ResponseEntity<List<TicketResponse>> getAllTickets(){
        return status(HttpStatus.OK).body(ticketService.getAllTickets());
    }

    @GetMapping("/close/{id}")
    public ResponseEntity<?> closeTicket(@PathVariable("id") Long ticketId){
        ticketService.closeProject(ticketId);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("/create-ticket/{projectId}")
    public HttpStatus createTicket(@RequestBody TicketRequest ticket, @PathVariable Long projectId){
        ticketService.createTicket(ticket, projectId);
        return HttpStatus.CREATED;
    }

    @PutMapping("/edit")
    public HttpStatus editTicket(@RequestBody TicketRequest ticket){
        ticketService.editTicket(ticket);
        return HttpStatus.OK;
    }

    @DeleteMapping("/delete/{ticketId}")
    public HttpStatus deleteTicket(@PathVariable("ticketId") Long ticketId){
        return ticketService.deleteTicket(ticketId);
    }

    @PostMapping("/assign/{ticketId}")
    public HttpStatus assignTicket(
            @RequestBody UserRequest userRequest,
            @PathVariable Long ticketId){
        ticketService.assignTicket(userRequest, ticketId);
        return HttpStatus.OK;
    }
}
