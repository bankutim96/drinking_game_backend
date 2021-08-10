package com.drinking.game.backend.domain;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import java.io.Serializable;

@Getter
@Setter
@MappedSuperclass
public abstract class EntityBase implements Serializable {

    @Id
    @Column(length = 36)
    @GeneratedValue(generator = "uuid_generator")
    @GenericGenerator(name = "uuid_generator", strategy = "org.hibernate.id.UUIDGenerator")
    protected String id;
}
