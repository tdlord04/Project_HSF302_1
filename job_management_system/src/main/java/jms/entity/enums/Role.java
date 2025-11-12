package jms.entity.enums;

import java.util.List;

public enum Role {
    ADMIN,
    HR,
    MANAGER,
    INTERVIEWER;

    public List<Role> getAssignableRoles() {
        return switch (this) {
            case ADMIN -> List.of(Role.values()); // ADMIN được gán mọi role
            case HR -> List.of(HR, MANAGER, INTERVIEWER); // HR được gán các role thấp hơn
            case MANAGER -> List.of(INTERVIEWER); // MANAGER chỉ được gán INTERVIEWER
            case INTERVIEWER -> List.of(); // INTERVIEWER không được gán ai cả
        };
    }
}