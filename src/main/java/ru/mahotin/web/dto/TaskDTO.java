package ru.mahotin.web.dto;

public record TaskDTO(
        String title,
        String description,
        Long userId) {
}
