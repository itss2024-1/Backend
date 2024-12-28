package com.example.itss.service;

import com.example.itss.domain.Resume;
import com.example.itss.domain.Schedule;
import com.example.itss.domain.User;
import com.example.itss.domain.response.ResponseDto;
import com.example.itss.domain.response.ResultPaginationDto;
import com.example.itss.domain.response.schedule.ResScheduleDto;
import com.example.itss.repository.ScheduleRepository;
import com.example.itss.util.SecurityUtil;
import com.example.itss.util.error.ValidInforException;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ScheduleService {
    private final ScheduleRepository scheduleRepository;
    private final UserService userService;
    public ScheduleService(ScheduleRepository scheduleRepository, UserService userService) {
        this.scheduleRepository = scheduleRepository;
        this.userService = userService;
    }

    public ResponseDto<ResScheduleDto> createSchedule(Schedule schedule) throws ValidInforException {
        String email = SecurityUtil.getCurrentUserLogin().isPresent()
                ? SecurityUtil.getCurrentUserLogin().get()
                : "";
        User currentUserDB = this.userService.handleGetUserByUsername(email);
        schedule.setUser(currentUserDB);
        Schedule newSchedule = scheduleRepository.save(schedule);
        ResScheduleDto resScheduleDto = this.convertToResScheduleDto(newSchedule);
        return new ResponseDto<>(201, "Tạo lịch trình thành công", resScheduleDto);
    }

    public ResponseDto<ResultPaginationDto> fetchAllSchedule(Specification<Schedule> spec, Pageable pageable) {
        Page<Schedule> pageSchedule = this.scheduleRepository.findAll(spec, pageable);
        ResultPaginationDto rs = new ResultPaginationDto();
        ResultPaginationDto.Meta mt = new ResultPaginationDto.Meta();

        mt.setPage(pageable.getPageNumber());
        mt.setPageSize(pageable.getPageSize());
        mt.setPages(pageSchedule.getTotalPages());
        mt.setTotal((int) pageSchedule.getTotalElements());

        rs.setMeta(mt);

        List<ResScheduleDto> listSchedule = pageSchedule.getContent()
                .stream().map(item -> this.convertToResScheduleDto(item))
                .collect(Collectors.toList());

        rs.setResult(listSchedule);

        return new ResponseDto<>(200, "Fetched all schedules", rs);
    }

    public static Specification<Schedule> withUserName(String userName) {
        return (root, query, criteriaBuilder) -> {
            if (userName == null || userName.isEmpty()) {
                return criteriaBuilder.conjunction();
            }

            // Join the user relationship and compare the name
            Join<Resume, User> userJoin = root.join("user", JoinType.INNER);
            return criteriaBuilder.like(
                    criteriaBuilder.lower(userJoin.get("name")),
                    "%" + userName.toLowerCase() + "%"
            );
        };
    }

    public static Specification<Schedule> withUserId(Long userId) {
        return (root, query, criteriaBuilder) -> {
            if (userId == null) {
                return criteriaBuilder.conjunction();
            }
            return criteriaBuilder.equal(root.get("user").get("id"), userId);
        };
    }

    public static Specification<Schedule> withInviteeId(Long inviteeId) {
        return (root, query, criteriaBuilder) -> {
            if (inviteeId == null) {
                return criteriaBuilder.conjunction();
            }
            return criteriaBuilder.equal(root.get("inviteeId"), inviteeId);
        };
    }

    public ResponseDto<ResScheduleDto> updateSchedule(Schedule schedule) throws ValidInforException {
        Long id = schedule.getId();
        Optional<Schedule> optionalSchedule = this.scheduleRepository.findById(id);
        if (optionalSchedule.isEmpty()) {
            throw new ValidInforException("Schedule not found");
        }

        Schedule existingSchedule = optionalSchedule.get();
        existingSchedule.setName(schedule.getName());
        existingSchedule.setDescription(schedule.getDescription());
//        existingSchedule.setTime(schedule.getTime());
        existingSchedule.setStatus(schedule.getStatus());

        Schedule updatedSchedule = scheduleRepository.save(existingSchedule);

        ResScheduleDto resScheduleDto = this.convertToResScheduleDto(updatedSchedule);
        return new ResponseDto<>(200, "Updated schedule successfully", resScheduleDto);
    }

    public void deleteSchedule(long id) throws ValidInforException {
        Optional<Schedule> optionalSchedule = this.scheduleRepository.findById(id);
        if (optionalSchedule.isEmpty()) {
            throw new ValidInforException("Schedule not found");
        }
        scheduleRepository.deleteById(id);
    }

    public ResponseDto<ResScheduleDto> fetchScheduleById(long id) throws ValidInforException {
        Optional<Schedule> optionalSchedule = this.scheduleRepository.findById(id);
        if (optionalSchedule.isEmpty()) {
            throw new ValidInforException("Schedule not found");
        }

        Schedule schedule = optionalSchedule.get();
        ResScheduleDto resScheduleDto = this.convertToResScheduleDto(schedule);
        return new ResponseDto<>(200, "Fetched schedule", resScheduleDto);
    }
    public ResScheduleDto convertToResScheduleDto(Schedule schedule) {
        ResScheduleDto resScheduleDto = new ResScheduleDto();
        resScheduleDto.setId(schedule.getId());
        resScheduleDto.setName(schedule.getName());
        resScheduleDto.setDescription(schedule.getDescription());
        resScheduleDto.setTime(schedule.getTime());
        resScheduleDto.setPhone(schedule.getPhone());
        resScheduleDto.setInviteeId(schedule.getInviteeId());
        resScheduleDto.setImageUrl(schedule.getImageUrl());
        resScheduleDto.setStatus(schedule.getStatus());
        resScheduleDto.setCreatedAt(schedule.getCreatedAt());
        resScheduleDto.setUpdatedAt(schedule.getUpdatedAt());
        resScheduleDto.setCreatedBy(schedule.getCreatedBy());
        resScheduleDto.setUpdatedBy(schedule.getUpdatedBy());
        resScheduleDto.setUser(new ResScheduleDto.UserSchedule(schedule.getUser().getId(), schedule.getUser().getName()));
        return resScheduleDto;
    }
}
