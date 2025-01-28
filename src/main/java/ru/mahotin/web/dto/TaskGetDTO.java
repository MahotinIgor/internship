package ru.mahotin.web.dto;

public record TaskGetDTO (Long id,
                         String title,
                         String description,
                         String status,
                         Long userId) {
    @Override
    public String toString() {
        return "TaskGetDTO{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", status='" + status + '\'' +
                ", userId=" + userId +
                '}';
    }
}
