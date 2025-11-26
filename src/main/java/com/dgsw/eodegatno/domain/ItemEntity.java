package com.dgsw.eodegatno.domain;

import com.dgsw.eodegatno.dto.request.UpdateItemRequest;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor
@Table(name = "items")
public class ItemEntity extends BaseEntity {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 신고자 이름
    @Column(nullable = false)
    private String reporterName;

    // 분실물 이름
    @Column(nullable = false)
    private String itemName;

    // 분실 날짜 및 시간
    @Column(nullable = false)
    private LocalDateTime lostDateTime;

    // 분실 위치
    @Column(nullable = false)
    private String lostLocation;

    // 분실물 상태
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ItemStatus status;

    // 연락처
    private String contactInfo;

    // 분실물 상세 설명
    private String description;

    @Builder
    public ItemEntity(String reporterName, String itemName, LocalDateTime lostDateTime, String lostLocation, ItemStatus status, String contactInfo, String description) {
        this.reporterName = reporterName;
        this.itemName = itemName;
        this.lostDateTime = lostDateTime;
        this.lostLocation = lostLocation;
        this.status = status;
        this.contactInfo = contactInfo;
        this.description = description;
    }

    public void update(UpdateItemRequest request) {
        this.reporterName = request.getReporterName();
        this.itemName = request.getItemName();
        this.lostDateTime = request.getLostDateTime();
        this.lostLocation = request.getLostLocation();
        this.status = request.getStatus();
        this.contactInfo = request.getContactInfo();
        this.description = request.getDescription();
    }

    public void updateStatus(ItemStatus status) {
        this.status = status;
    }
}
