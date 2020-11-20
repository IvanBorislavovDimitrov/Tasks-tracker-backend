package com.tracker.taskstracker.domain;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;

@MappedSuperclass
public class IdEntity {

    @Id
    @GenericGenerator(name = "uuid-string", strategy = "uuid")
    @GeneratedValue(generator = "uuid-string")
    @Column(name = "id", unique = true, nullable = false, updatable = false)
    private String id;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
