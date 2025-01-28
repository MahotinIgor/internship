package ru.mahotin.web.dto;

public record TaskUpdateDTO(
        String title,
        String description,
        String status,
        Long userId) {
    @Override
    public String toString() {
        return "TaskUpdateDTO{" +
                "title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", status='" + status + '\'' +
                ", userId=" + userId +
                '}';
    }
}
