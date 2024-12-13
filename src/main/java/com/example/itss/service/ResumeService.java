package com.example.itss.service;

import com.example.itss.domain.Resume;
import com.example.itss.domain.User;
import com.example.itss.domain.response.ResponseDto;
import com.example.itss.domain.response.ResultPaginationDto;
import com.example.itss.domain.response.resume.ResResumeDto;
import com.example.itss.domain.response.user.ResUserDto;
import com.example.itss.repository.ResumeRepository;
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
public class ResumeService {
    private final ResumeRepository resumeRepository;
    private final SecurityUtil securityUtil;
    private final UserService userService;

    public ResumeService(ResumeRepository resumeRepository, SecurityUtil securityUtil, UserService userService) {
        this.resumeRepository = resumeRepository;
        this.securityUtil = securityUtil;
        this.userService = userService;
    }

    public ResponseDto<ResResumeDto> createResume(Resume resume) throws ValidInforException {
        String email = SecurityUtil.getCurrentUserLogin().isPresent()
                ? SecurityUtil.getCurrentUserLogin().get()
                : "";
        User currentUserDB = this.userService.handleGetUserByUsername(email);
        resume.setUser(currentUserDB);
        Resume newResume =  resumeRepository.save(resume);
        ResResumeDto resResumeDto = this.convertToResResumeDto(newResume);
        return new ResponseDto<>(201, "Tạo tài khoản thành công", resResumeDto);
    }
    public static Specification<Resume> withUserName(String userName) {
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

    public ResponseDto<ResultPaginationDto> fetchAllResume(Specification<Resume> spec, Pageable pageable) {
        Page<Resume> pageResume = this.resumeRepository.findAll(spec, pageable);
        ResultPaginationDto rs = new ResultPaginationDto();
        ResultPaginationDto.Meta mt = new ResultPaginationDto.Meta();

        mt.setPage(pageable.getPageNumber());
        mt.setPageSize(pageable.getPageSize());
        mt.setPages(pageResume.getTotalPages());
        mt.setTotal((int) pageResume.getTotalElements());

        rs.setMeta(mt);

        List<ResResumeDto> listResume = pageResume.getContent()
                .stream().map(item -> this.convertToResResumeDto(item))
                .collect(Collectors.toList());

        rs.setResult(listResume);

        return new ResponseDto<>(200, "Fetched all resumes", rs);
    }


    public ResponseDto<ResResumeDto> updateResume(Resume resume) throws ValidInforException {
        Optional<Resume> optionalResume = this.resumeRepository.findById(resume.getId());
        if(optionalResume.isEmpty()) {
            throw new ValidInforException("Resume not found");
        }

        Resume currResume = optionalResume.get();

        currResume.setName(resume.getName());
        currResume.setImages(resume.getImages());
        currResume.setDescription(resume.getDescription());
        currResume.setStatus(resume.getStatus());

        Resume newResume =  resumeRepository.save(currResume);

        ResResumeDto resResumeDto = this.convertToResResumeDto(newResume);
        return new ResponseDto<>(201, "Tạo tài khoản thành công", resResumeDto);
    }

    public ResponseDto<ResResumeDto> fetchResumeById(long id) throws ValidInforException {
        Optional<Resume> optionalResume = this.resumeRepository.findById(id);
        if(optionalResume.isEmpty()) {
            throw new ValidInforException("Resume not found");
        }

        Resume resume = optionalResume.get();
        ResResumeDto resResumeDto = this.convertToResResumeDto(resume);
        return new ResponseDto<>(200, "Fetched resume", resResumeDto);
    }

    public ResResumeDto convertToResResumeDto(Resume resume) {
        ResResumeDto resResumeDto = new ResResumeDto();
        resResumeDto.setId(resume.getId());
        resResumeDto.setName(resume.getName());
        resResumeDto.setImages(resume.getImages());
        resResumeDto.setDescription(resume.getDescription());
        resResumeDto.setStatus(resume.getStatus());
        resResumeDto.setJobTitle(resume.getJobTitle());
        resResumeDto.setCreatedAt(resume.getCreatedAt());
        resResumeDto.setUpdatedAt(resume.getUpdatedAt());
        resResumeDto.setCreatedBy(resume.getCreatedBy());
        resResumeDto.setUpdatedBy(resume.getUpdatedBy());
        resResumeDto.setUser(new ResResumeDto.UserResume(resume.getUser().getId(), resume.getUser().getName()));
        return resResumeDto;
    }

}
