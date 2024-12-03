package ru.mahotin.web.dto;

public record TaskUpdateDTO(
        String title,
        String description,
        Long userId) {
    @Override
    public String toString() {
        return "TaskUpdateDTO{" +
                "title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", userId=" + userId +
                '}';
    }
}
