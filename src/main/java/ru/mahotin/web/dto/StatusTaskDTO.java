package ru.mahotin.web.dto;
public record StatusTaskDTO(

        long taskid,

        String newStatus) {
        @Override
        public String toString() {
                return "StatusTaskDTO{" +
                        "taskid=" + taskid +
                        ", newStatus='" + newStatus + '\'' +
                        '}';
        }
}
