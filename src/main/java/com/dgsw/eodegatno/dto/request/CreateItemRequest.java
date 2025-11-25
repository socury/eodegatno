package com.dgsw.eodegatno.dto.request;

import com.dgsw.eodegatno.domain.ItemEntity;
import com.dgsw.eodegatno.domain.ItemStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class CreateItemRequest {
    private String reporterName;

    private String itemName;

    private LocalDateTime lostDateTime;

    private String lostLocation;

    private ItemStatus status;

    private String contactInfo;

    private String description;

    public ItemEntity toEntity() {
        return ItemEntity.builder()
                .reporterName(reporterName)
                .itemName(itemName)
                .lostDateTime(lostDateTime)
                .lostLocation(lostLocation)
                .status(status)
                .contactInfo(contactInfo)
                .description(description)
                .build();
    }
}
