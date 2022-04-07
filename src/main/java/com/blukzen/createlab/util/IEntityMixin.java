package com.blukzen.createlab.util;

public interface IEntityMixin {
    void handleInsideLabPortal();
    void handleLabPortal();
    boolean isInsideLabPortal();
    boolean isOnLabPortalCooldown();
    void setLabPortalCooldown();
    float getLabPortalCooldown();
}
