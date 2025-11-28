package com.nemo.mixsafe.domain;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "default_substance")
public class DefaultSubstance {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "substance_id")
    private Long substanceId;

    @Column(name = "substance_name", nullable = false, unique = true)
    private String substanceName;

    @Column(name = "substance_name_eng")
    private String substanceNameEng;

    @Column(nullable = false)
    private Boolean isDanger;

    @Column(length = 500)
    private String description;
}