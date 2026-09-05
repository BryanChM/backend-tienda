package com.guepardosport.backend_tienda.dto;

import java.util.Map;

public class RecurrenteWebhookDTO {
    private String event;
    private Map<String, Object> data;

    public String getEvent() { return event; }
    public void setEvent(String event) { this.event = event; }

    public Map<String, Object> getData() { return data; }
    public void setData(Map<String, Object> data) { this.data = data; }
}