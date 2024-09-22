package com.telegram_notifier.telegram_notifier.dto.input;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TrelloListInputDTO {
    private String id;             
    private String name;           
    private boolean closed;        
    private String idBoard;        
    private int pos;               
    private boolean subscribed;    
    private String color;          
    private String softLimit;      
    private String type;           
    private Object datasource;     
}
