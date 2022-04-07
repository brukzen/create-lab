package com.blukzen.createlab.accessor;

public interface EntityAccessor {
    void handleInsideLabPortal();

    void handleLabPortal();

    boolean isInsideLabPortal();

    boolean isOnLabPortalCooldown();

    void setLabPortalCooldown();

    float getLabPortalCooldown();

    float getLabPortalTime();
}
