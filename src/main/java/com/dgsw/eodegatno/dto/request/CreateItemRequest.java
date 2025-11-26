package com.dgsw.eodegatno.dto.request;

import com.dgsw.eodegatno.domain.ItemEntity;
import com.dgsw.eodegatno.domain.ItemStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
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

    @NotBlank(message = "연락처를 입력해주세요.")
    @Pattern(regexp = "^010\\d{8}$", message = "연락처는 010으로 시작하는 11자리 숫자만 입력 가능합니다.")
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
