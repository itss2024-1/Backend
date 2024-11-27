package vn.ITSS.teacherexchange.domain;

import jakarta.persistence.*;
import java.util.List;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "criterias")
@Getter
@Setter
public class Criteria {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private String name;
    private String description;
    private String status; // active/inactive
    private Integer priorityLevel;
    private String category;

//    @OneToMany(mappedBy = "criteria", cascade = CascadeType.ALL, orphanRemoval = true)
//    private List<Highlight> highlights;

//    @Column(name = "created_at", nullable = false, updatable = false)
//    private java.time.LocalDateTime createdAt;
//
//    @Column(name = "updated_at")
//    private java.time.LocalDateTime updatedAt;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

//    @PrePersist
//    public void prePersist() {
//        this.createdAt = java.time.LocalDateTime.now();
//    }
//
//    @PreUpdate
//    public void preUpdate() {
//        this.updatedAt = java.time.LocalDateTime.now();
//    }
}
