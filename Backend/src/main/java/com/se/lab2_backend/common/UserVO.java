package com.se.lab2_backend.common;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.lang.Nullable;

@Data
@AllArgsConstructor
public class UserVO {
    private String uuid;
    private String jobNum;
    private String username;
    private String idcardNum;
    @Nullable
    private String phoneNum;
    @Nullable
    private String email;
    private String identity;
    private String major;
    private String majorId;
    private String institute;
    private String instituteId;
    private String status;
    private String statusId;
}
