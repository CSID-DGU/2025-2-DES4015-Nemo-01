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
    @Column(name = "substanceId")
    private Long substanceId;

    @Column(name = "substanceName", nullable = false, unique = true)
    private String substanceName;

    @Column(nullable = false)
    private Boolean isDanger;

    @Column(length = 500)
    private String description;
}