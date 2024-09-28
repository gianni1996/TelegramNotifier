package com.trello_talk.trello_talk.dto.input;

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
