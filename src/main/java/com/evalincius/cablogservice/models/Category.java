package com.evalincius.cablogservice.models;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Document(collection = "category")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Category {
    @Id
    private String id;
}
