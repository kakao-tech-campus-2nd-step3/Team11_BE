package boomerang.progress.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.Getter;

@Getter
@Embeddable
public class MainStep1 {

    @Column(name = "sub_step1")
    private Boolean sub_step1;

    @Column(name = "sub_step2")
    private Boolean sub_step2;

}
