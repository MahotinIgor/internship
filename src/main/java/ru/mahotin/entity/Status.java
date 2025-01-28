package ru.mahotin.entity;

import java.util.Arrays;

public enum Status {
    NEW("new"),
    IN_PROGRESS("in_progress"),
    DONE("done");
    private String title;
    Status(String title) {
        this.title = title;
    }
    public String getTitle() {
        return title;
    }
    public static Status fromText(String text) {
        return Arrays.stream(values())
                .filter(s -> s.title.equals(text))
                .findFirst()
                .orElseThrow(()-> new IllegalArgumentException(String.format(
                        "There is no value with title '%s' in Enum",
                        text)
                ));
    }

    @Override
    public String toString() {
        return title;
    }
}
