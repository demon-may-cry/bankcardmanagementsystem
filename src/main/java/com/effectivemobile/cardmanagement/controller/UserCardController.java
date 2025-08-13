package com.effectivemobile.cardmanagement.controller;

import com.effectivemobile.cardmanagement.dto.CardCreateRequest;
import com.effectivemobile.cardmanagement.dto.CardDto;
import com.effectivemobile.cardmanagement.dto.TransferRequest;
import com.effectivemobile.cardmanagement.service.CardService;
import com.effectivemobile.cardmanagement.service.TransferService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

/**
 * @author Дмитрий Ельцов
 **/
@Tag(name = "User Cards", description = "Операции пользователя со своими картами")
@RestController
@RequestMapping("/api/user/cards")
@PreAuthorize("hasRole('USER')")
@RequiredArgsConstructor
public class UserCardController {

    private final CardService cardService;
    private final TransferService transferService;

    @Operation(summary = "Создать карту текущему пользователю")
    @PostMapping
    public CardDto create(@Valid @RequestBody CardCreateRequest req) {
        return cardService.createCard(req);
    }

    @Operation(summary = "Мои карты (пагинация)")
    @GetMapping
    public Page<CardDto> myCards(@RequestParam(defaultValue = "0") int page,
                                 @RequestParam(defaultValue = "10") int size) {
        return cardService.getMyCards(PageRequest.of(page, size));
    }

    @Operation(summary = "Получить мою карту по id")
    @GetMapping("/{id}")
    public CardDto get(@PathVariable Long id) {
        return cardService.getMyCard(id);
    }

    @Operation(summary = "Удалить мою карту по id")
    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        cardService.deleteMyCard(id);
    }

    @Operation(summary = "Перевод между моими картами")
    @PostMapping("/transfer")
    public void transfer(@Valid @RequestBody TransferRequest req) {
        transferService.transfer(req);
    }
}
