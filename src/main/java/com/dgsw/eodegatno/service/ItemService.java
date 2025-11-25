package com.dgsw.eodegatno.service;

import com.dgsw.eodegatno.domain.ItemEntity;
import com.dgsw.eodegatno.domain.ItemStatus;
import com.dgsw.eodegatno.dto.request.CreateItemRequest;
import com.dgsw.eodegatno.dto.request.UpdateItemRequest;
import com.dgsw.eodegatno.dto.response.DataResponse;
import com.dgsw.eodegatno.dto.response.ErrorResponse;
import com.dgsw.eodegatno.dto.response.ItemResponse;
import com.dgsw.eodegatno.dto.response.Response;
import com.dgsw.eodegatno.repository.ItemRepository;
import lombok.RequiredArgsConstructor;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ItemService {
    private final ItemRepository itemRepository;

    /** 분실물 신고 등록 */
    @Transactional
    public ResponseEntity<Response> createItem(CreateItemRequest request) {
        ItemEntity entity = request.toEntity();
        itemRepository.save(entity);
        return new DataResponse<>(201, "분실물 신고가 등록되었습니다.", ItemResponse.from(entity)).toResponseEntity();
    }

    /** 분실물 최신순으로 몽땅 가져오기 */
    public ResponseEntity<Response> getAllItems() {
        List<ItemResponse> items = itemRepository.findAllByOrderByCreatedAtDesc().stream()
                .map(ItemResponse::from)
                .toList();
        return new DataResponse<>(200, "분실물 목록을 조회했습니다.", items).toResponseEntity();
    }

    /** LOST 상태 분실물 최신순으로 가져오기 */
    public ResponseEntity<Response> getLostItems() {
        List<ItemResponse> items = itemRepository.findByStatus(ItemStatus.LOST).stream()
                .map(ItemResponse::from)
                .toList();
        return new DataResponse<>(200, "분실 상태 목록을 조회했습니다.", items).toResponseEntity();
    }

    /** FOUND 상태 분실물 최신순으로 가져오기 */
    public ResponseEntity<Response> getFoundItems() {
        List<ItemResponse> items = itemRepository.findByStatus(ItemStatus.FOUND).stream()
                .map(ItemResponse::from)
                .toList();
        return new DataResponse<>(200, "발견 상태 목록을 조회했습니다.", items).toResponseEntity();
    }

    /** 분실물 한개 가져오기 */
    public ResponseEntity<Response> getItemById(Long id) {
        Optional<ItemEntity> optItem = itemRepository.findById(id);
        if (optItem.isEmpty())
            return new ErrorResponse(404, "분실물을 찾을 수 없습니다.", "Item not found with id: " + id).toResponseEntity();

        ItemEntity item = optItem.get();
        return new DataResponse<>(200, "분실물 상세 정보를 조회했습니다.", ItemResponse.from(item)).toResponseEntity();
    }

    /** 분실물 업데이트 */
    @Transactional
    public ResponseEntity<Response> updateItem(Long id, UpdateItemRequest request) {
        Optional<ItemEntity> optItem = itemRepository.findById(id);
        if (optItem.isEmpty())
            return new ErrorResponse(404, "분실물을 찾을 수 없습니다.", "Item not found with id: " + id).toResponseEntity();

        ItemEntity item = optItem.get();
        item.update(request);
        itemRepository.save(item);
        return new DataResponse<>(200, "분실물 정보가 수정되었습니다.", ItemResponse.from(item)).toResponseEntity();
    }

    /** 분실물 삭제하기 */
    @Transactional
    public ResponseEntity<Response> deleteItem(Long id) {
        if (!itemRepository.existsById(id))
            return new ErrorResponse(404, "분실물을 찾을 수 없습니다.", "Item not found with id: " + id).toResponseEntity();

        itemRepository.deleteById(id);
        return new Response(200, "분실물 신고가 삭제되었습니다.").toResponseEntity();
    }

    /** 분실물 목록을 엑셀로 내보내기 */
    public byte[] exportItemsToExcel() {
        List<ItemEntity> items = itemRepository.findAllByOrderByCreatedAtDesc();
        
        try (Workbook workbook = new XSSFWorkbook(); ByteArrayOutputStream out = new ByteArrayOutputStream()) {
            Sheet sheet = workbook.createSheet("분실물 목록");
            
            CellStyle headerStyle = workbook.createCellStyle();
            Font headerFont = workbook.createFont();
            headerFont.setBold(true);
            headerStyle.setFont(headerFont);
            headerStyle.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
            headerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
            
            Row headerRow = sheet.createRow(0);
            String[] headers = {"ID", "신고자명", "분실물명", "분실일시", "분실위치", "상태", "연락처", "상세설명", "등록일시", "수정일시"};
            for (int i = 0; i < headers.length; i++) {
                Cell cell = headerRow.createCell(i);
                cell.setCellValue(headers[i]);
                cell.setCellStyle(headerStyle);
            }
            
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            int rowNum = 1;
            for (ItemEntity item : items) {
                Row row = sheet.createRow(rowNum++);
                row.createCell(0).setCellValue(item.getId());
                row.createCell(1).setCellValue(item.getReporterName());
                row.createCell(2).setCellValue(item.getItemName());
                row.createCell(3).setCellValue(item.getLostDateTime().format(formatter));
                row.createCell(4).setCellValue(item.getLostLocation());
                row.createCell(5).setCellValue(item.getStatus().toString());
                row.createCell(6).setCellValue(item.getContactInfo() != null ? item.getContactInfo() : "");
                row.createCell(7).setCellValue(item.getDescription() != null ? item.getDescription() : "");
                row.createCell(8).setCellValue(item.getCreatedAt().format(formatter));
                row.createCell(9).setCellValue(item.getUpdatedAt().format(formatter));
            }
            
            for (int i = 0; i < headers.length; i++) {
                sheet.autoSizeColumn(i);
            }
            
            workbook.write(out);
            return out.toByteArray();
        } catch (IOException e) {
            throw new RuntimeException("엑셀 파일 생성 중 오류가 발생했습니다.", e);
        }
    }
}
