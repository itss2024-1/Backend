package com.example.itss.domain.response.schedule;

import com.example.itss.util.constant.ScheduleStatusEnum;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ResScheduleDto {
    private long id;
    private String name;
    private String description;
    private String time;
    private String phone;
    private long inviteeId;
    private String imageUrl;
    @JsonProperty("status")
    private ScheduleStatusEnum status;
    private UserSchedule user;

    private Instant createdAt;
    private Instant updatedAt;
    private String createdBy;
    private String updatedBy;

    @Getter
    @Setter
    @AllArgsConstructor
    public static class UserSchedule {
        private long id;
        private String name;
    }
}
