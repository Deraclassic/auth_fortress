package com.dera.AuthFortress.domain.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum EmailTemplateName {
    ACTIVATE_ACCOUNT("activate_account"),
    UNUSUAL_ACTIVITY("unusual_activity");

    private final String name;
}
