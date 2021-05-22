package com.example.ztpai.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TicketResponse {
    private Long ticketId;
    private LocalDateTime created_at;
    private String desc;
    private String priority;
    private String status;
    private String title;
    private String type;
    private Long projectId;
    private String projectName;
    private String usersEmails;
}
