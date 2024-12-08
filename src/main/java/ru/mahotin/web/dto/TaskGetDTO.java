package ru.mahotin.web.dto;

public record TaskGetDTO (Long id,
                         String title,
                         String description,
                         Long userId) {
    @Override
    public String toString() {
        return "TaskGetDTO{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", userId=" + userId +
                '}';
    }
}
