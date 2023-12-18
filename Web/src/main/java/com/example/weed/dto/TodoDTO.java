package com.example.weed.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TodoDTO {

    private Long id;
    private String title;
    private String content;
    private boolean checked;

}
