package com.example.itss.controller;

import com.example.itss.domain.Resume;
import com.example.itss.domain.Schedule;
import com.example.itss.domain.response.ResponseDto;
import com.example.itss.domain.response.ResultPaginationDto;
import com.example.itss.domain.response.schedule.ResScheduleDto;
import com.example.itss.service.ScheduleService;
import com.example.itss.util.error.ValidInforException;
import com.turkraft.springfilter.boot.Filter;
import jakarta.validation.Valid;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/schedules")
public class ScheduleController {
    private final ScheduleService scheduleService;

    public ScheduleController(ScheduleService scheduleService) {
        this.scheduleService = scheduleService;
    }

    @PostMapping
    public ResponseEntity<ResponseDto<ResScheduleDto>> createResume(@Valid @RequestBody Schedule schedule) throws ValidInforException {
        return ResponseEntity.status(HttpStatus.CREATED).body(scheduleService.createSchedule(schedule));
    }

    @GetMapping("/all")
    public ResponseEntity<ResponseDto<ResultPaginationDto>> fetchAllSchedule(
            @RequestParam(required = false)  Long userId,
            @Filter Specification<Schedule> spec,
            Pageable pageable) {
        Specification<Schedule> combinedSpec = Specification.where(spec)
                .and(scheduleService.withUserId(userId));
        return ResponseEntity.status(HttpStatus.OK).body(this.scheduleService.fetchAllSchedule(combinedSpec, pageable));
    }

    @GetMapping("/all/invitee")
    public ResponseEntity<ResponseDto<ResultPaginationDto>> fetchAllScheduleByInviteeId(
            @RequestParam(required = false) Long inviteeId,
            @Filter Specification<Schedule> spec,
            Pageable pageable) {
        Specification<Schedule> combinedSpec = Specification.where(spec)
                .and(scheduleService.withInviteeId(inviteeId));
        return ResponseEntity.status(HttpStatus.OK).body(this.scheduleService.fetchAllSchedule(combinedSpec, pageable));
    }

    @PutMapping
    public ResponseEntity<ResponseDto<ResScheduleDto>> updateSchedule(@RequestBody Schedule schedule) throws ValidInforException {
        return ResponseEntity.status(HttpStatus.OK).body(this.scheduleService.updateSchedule(schedule));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSchedule(@PathVariable long id) throws ValidInforException {
        scheduleService.deleteSchedule(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<ResponseDto<ResScheduleDto>> fetchScheduleById(@PathVariable long id) throws ValidInforException {
        return ResponseEntity.status(HttpStatus.OK).body(scheduleService.fetchScheduleById(id));
    }

}
