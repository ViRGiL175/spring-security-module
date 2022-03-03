package ru.virgil.example.system;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public abstract class BaseDto {

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

}
