package com.loko.domain;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ActivityLog extends BaseModel {
    private User user;
    private String action;
    private String target;
    private String metadata;
    private String ipAddress;
}
