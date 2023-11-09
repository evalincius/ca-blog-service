package com.evalincius.cablogservice.models;

import java.time.ZonedDateTime;

import jakarta.persistence.MappedSuperclass;
import lombok.Data;

@Data
@MappedSuperclass
public abstract class Audit {
    private ZonedDateTime createdAt;
    private ZonedDateTime updatedAt;
}
