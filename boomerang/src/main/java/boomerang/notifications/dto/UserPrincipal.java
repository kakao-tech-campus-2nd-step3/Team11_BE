package boomerang.notifications.dto;

import lombok.Getter;
import lombok.ToString;

import java.security.Principal;
import java.util.Objects;

@Getter
@ToString
public class UserPrincipal implements Principal {
    private final String name;

    public UserPrincipal(String name) {
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Name cannot be null or empty");
        }
        this.name = name;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof UserPrincipal)) return false;
        UserPrincipal that = (UserPrincipal) o;
        return Objects.equals(getName(), that.getName());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getName());
    }
}