package com.loko.domain;

import java.time.LocalDateTime;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BaseModel  {
    private String id;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;


}
