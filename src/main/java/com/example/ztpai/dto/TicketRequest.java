package com.example.ztpai.dto;

import lombok.Data;

@Data
public class TicketRequest {
    private String desc;
    private String priority;
    private String status;
    private String title;
    private String type;
    private Long ticketId;
    private Long projectId;
}
