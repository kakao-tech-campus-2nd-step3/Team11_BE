package boomerang.progress.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.Getter;

@Getter
@Embeddable
public class MainStep1 {

    @Column(name = "sub_step1")
    private Boolean subStep1;

    @Column(name = "sub_step2")
    private Boolean subStep2;

    public MainStep1() {
        this.subStep1 = false;
        this.subStep2 = false;
    }

    public void updateSubStep1(boolean status) {
        this.subStep1 = status;
    }

    public void updateSubStep2(boolean status) {
        this.subStep2 = status;
    }


}
