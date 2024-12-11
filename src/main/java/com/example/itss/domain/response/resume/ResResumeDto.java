package com.example.itss.domain.response.resume;

import com.example.itss.util.constant.ResumeStatusEnum;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ResResumeDto {
    private long id;
    private String name;
    private String images;
    private String description;
    @JsonProperty("status")
    private ResumeStatusEnum status;
    private String jobTitle;
    private UserResume user;

    private Instant createdAt;
    private Instant updatedAt;
    private String createdBy;
    private String updatedBy;

    @Getter
    @Setter
    @AllArgsConstructor
    public static class UserResume {
        private long id;
        private String name;
    }
}
