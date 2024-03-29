package com.company.entity;

import com.company.enums.ProfileRole;
import com.company.enums.ProfileStatus;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "profile")
public class ProfileEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "created_date")
    private LocalDateTime createdDate = LocalDateTime.now();

    @Column(nullable = false)
    private String name;

    @Column
    private String surname;

    @Column()
    private String email;

    @Column(nullable = false)
    private String password;
    @Column(unique = true)
    private String phone;

    @Column
    @Enumerated(EnumType.STRING)
    private ProfileStatus status;

    @Column
    @Enumerated(EnumType.STRING)
    private ProfileRole role;

    @Column
    private Boolean visible;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "attach_id")
    private AttachEntity attach;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "moderator")
    private List<ArticleEntity> articleList;

    public ProfileEntity() {
    }

    public ProfileEntity(Integer id) {
        this.id = id;
    }
}
