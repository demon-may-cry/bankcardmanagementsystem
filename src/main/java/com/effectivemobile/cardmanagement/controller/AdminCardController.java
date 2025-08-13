package com.effectivemobile.cardmanagement.controller;

import com.effectivemobile.cardmanagement.dto.CardDto;
import com.effectivemobile.cardmanagement.model.entity.CardStatus;
import com.effectivemobile.cardmanagement.service.CardService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

/**
 * @author Дмитрий Ельцов
 **/
@Tag(name = "Admin Cards", description = "Админ-операции с картами")
@RestController
@RequestMapping("/api/admin/cards")
@PreAuthorize("hasRole('ADMIN')")
@RequiredArgsConstructor
public class AdminCardController {

    private final CardService cardService;

    @Operation(summary = "Все карты (пагинация)")
    @GetMapping
    public Page<CardDto> all(@RequestParam(defaultValue = "0") int page,
                             @RequestParam(defaultValue = "10") int size) {
        return cardService.getAllCards(PageRequest.of(page, size));
    }

    @Operation(summary = "Сменить статус карты")
    @PutMapping("/{id}/status")
    public void setStatus(@PathVariable Long id, @RequestParam CardStatus status) {
        cardService.setStatusAdmin(id, status);
    }
}
