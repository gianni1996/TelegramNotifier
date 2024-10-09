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
    private Boolean closed;        
    private String idBoard;        
    private Integer pos;               
    private Boolean subscribed;    
    private String color;          
    private String softLimit;      
    private String type;           
    private DatasourceDTO datasource;     
}
