package io.github.barbatchov.qrydslflyway.domain;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Data
@Entity
public class User {
    @Id
    @GeneratedValue
    private Integer id;

    @Column
    private String name;

    @Column
    private String password;
}
