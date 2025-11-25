package com.dgsw.eodegatno.dto.response;

import com.dgsw.eodegatno.domain.ItemEntity;
import com.dgsw.eodegatno.domain.ItemStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class ItemResponse {
    private Long id;

    private String reporterName;

    private String itemName;

    private LocalDateTime lostDateTime;

    private String lostLocation;

    private ItemStatus status;

    private String contactInfo;

    private String description;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    public static ItemResponse from(ItemEntity entity) {
        return new ItemResponse(
                entity.getId(),
                entity.getReporterName(),
                entity.getItemName(),
                entity.getLostDateTime(),
                entity.getLostLocation(),
                entity.getStatus(),
                entity.getContactInfo(),
                entity.getDescription(),
                entity.getCreatedAt(),
                entity.getUpdatedAt()
        );
    }
}
