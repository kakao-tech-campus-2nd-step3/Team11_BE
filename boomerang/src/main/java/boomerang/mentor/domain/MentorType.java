package boomerang.mentor.domain;

import lombok.Getter;

@Getter
public enum MentorType {
    LAWYER("lawyer"), REAL_ESTATE_AGENT("realEstateAgent"), PREVIOUS_DAMAGE_RESOLVER("previousDamageResolver");

    private final String name;

    MentorType(String name) {
        this.name = name;
    }
}
