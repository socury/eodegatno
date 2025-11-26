package com.dgsw.eodegatno.controller;

import com.dgsw.eodegatno.dto.request.CreateItemRequest;
import com.dgsw.eodegatno.dto.request.UpdateItemRequest;
import com.dgsw.eodegatno.dto.response.Response;
import com.dgsw.eodegatno.service.ItemService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/lost-items")
@RequiredArgsConstructor
public class ItemController {
    private final ItemService itemService;

    @PostMapping
    public ResponseEntity<Response> createItem(@RequestBody CreateItemRequest request) {
        return itemService.createItem(request);
    }

    @GetMapping
    public ResponseEntity<Response> getAllItems() {
        return itemService.getAllItems();
    }

    @GetMapping("/lost")
    public ResponseEntity<Response> getLostItems() {
        return itemService.getLostItems();
    }

    @GetMapping("/found")
    public ResponseEntity<Response> getFoundItems() {
        return itemService.getFoundItems();
    }

    @PutMapping("/found")
    public ResponseEntity<Response> updateFoundItem(@RequestParam Long id) { return itemService.setFoundItem(id); }

    @GetMapping("/{id}")
    public ResponseEntity<Response> getItemById(@PathVariable Long id) {
        return itemService.getItemById(id);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Response> updateItem(@PathVariable Long id, @RequestBody UpdateItemRequest request) {
        return itemService.updateItem(id, request);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Response> deleteItem(@PathVariable Long id) {
        return itemService.deleteItem(id);
    }

    @GetMapping("/excel")
    public ResponseEntity<ByteArrayResource> exportToExcel() {
        byte[] excelData = itemService.exportItemsToExcel();
        ByteArrayResource resource = new ByteArrayResource(excelData);

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=lost_items.xlsx")
                .contentType(MediaType.parseMediaType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"))
                .contentLength(excelData.length)
                .body(resource);
    }
}
