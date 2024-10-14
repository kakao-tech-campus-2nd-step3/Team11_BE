package boomerang.mentor.domain;

import lombok.Getter;

@Getter
public enum MentorType {
    ENTIRE("ENTIRE"), SECRETE("SECRETE"), LOCATION("LOCATION"), STEP("STEP");

    private final String name;

    MentorType(String name) {
        this.name = name;
    }
}
