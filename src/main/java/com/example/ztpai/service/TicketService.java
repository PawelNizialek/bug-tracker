package com.example.ztpai.service;

import com.example.ztpai.dto.TicketRequest;
import com.example.ztpai.dto.TicketResponse;
import com.example.ztpai.dto.UserRequest;
import com.example.ztpai.exception.ZtpaiAppException;
import com.example.ztpai.model.Project;
import com.example.ztpai.model.Ticket;
import com.example.ztpai.model.User;
import com.example.ztpai.repository.ProjectRepository;
import com.example.ztpai.repository.TicketRepository;
import com.example.ztpai.repository.UserRepository;
import com.example.ztpai.ticket.TicketPriority;
import com.example.ztpai.ticket.TicketStatus;
import com.example.ztpai.ticket.TicketType;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Transactional
public class TicketService {
    private final TicketRepository ticketRepository;
    private final ProjectRepository projectRepository;
    private final UserRepository userRepository;

    public TicketService(TicketRepository ticketRepository, ProjectRepository projectRepository, UserRepository userRepository) {
        this.ticketRepository = ticketRepository;
        this.projectRepository = projectRepository;
        this.userRepository = userRepository;
    }

    private List<TicketResponse> mapTicketToTicketResponse(List<Ticket> tickets){
        List<TicketResponse> ticketResponses = new ArrayList<>();
        for(Ticket ticket: tickets){
            TicketResponse ticketResponse = TicketResponse.builder()
                    .title(ticket.getTitle())
                    .created_at(ticket.getCreated_at())
                    .desc(ticket.getDescription())
                    .priority(ticket.getPriority().toString())
                    .status(ticket.getStatus().toString())
                    .type(ticket.getType().toString())
                    .ticketId(ticket.getTicketId())
                    .projectName(ticket.getProject().getName())
                    .usersEmails(ticket.getUserSet().stream()
                            .map(n -> String.valueOf(n.getEmail()))
                            .collect(Collectors.joining(" "))
                    )
                    .build();
            ticketResponses.add(ticketResponse);
        }
        return ticketResponses;
    }

    public List<TicketResponse> getAllTickets() {
        List<Ticket> tickets = ticketRepository.findAll();
        return mapTicketToTicketResponse(tickets);
    }

    public void createTicket(TicketRequest ticketRequest, Long projectId) {
        Project project = projectRepository.findById(projectId).orElseThrow(
                () -> new ZtpaiAppException("project not found")
        );
        Ticket ticket = Ticket.builder()
                .created_at(LocalDateTime.now())
                .description(ticketRequest.getDesc())
                .priority(TicketPriority.valueOf(ticketRequest.getPriority()))
                .status(TicketStatus.OPEN)
                .title(ticketRequest.getTitle())
                .type(TicketType.valueOf(ticketRequest.getType()))
                .project(project)
                .build();
        ticketRepository.save(ticket);
    }

    public void editTicket(TicketRequest ticketRequest){
        Ticket ticket = ticketRepository.findById(ticketRequest.getTicketId()).orElseThrow(
                () -> new ZtpaiAppException("ticket not found")
        );
        ticket.setTitle(ticketRequest.getTitle());
        ticket.setDescription(ticketRequest.getDesc());
        ticketRepository.save(ticket);
    }

    public HttpStatus deleteTicket(Long ticketId){
        if(ticketRepository.findById(ticketId).isPresent()){
            Ticket ticket = ticketRepository.findById(ticketId).get();
            ticketRepository.delete(ticket);
            return HttpStatus.OK;
        }
        return HttpStatus.NOT_FOUND;
    }

    public void assignTicket(UserRequest userRequest, Long ticketId) {
        User user = userRepository.findById(userRequest.getUserId()).orElseThrow(
                () -> new ZtpaiAppException("user not found")
        );
        Ticket ticket = ticketRepository.findById(ticketId).orElseThrow(
                () -> new ZtpaiAppException("ticket not found")
        );
        List<User> users = ticket.getUserSet();
        users.add(user);
        ticket.setUserSet(users);
        ticketRepository.save(ticket);
    }

    public void closeProject(Long ticketId) {
        Ticket ticket = ticketRepository.findById(ticketId).orElseThrow(
                () -> new ZtpaiAppException("ticket not found")
        );
        ticket.setStatus(TicketStatus.CLOSE);
        ticketRepository.save(ticket);
    }
}
