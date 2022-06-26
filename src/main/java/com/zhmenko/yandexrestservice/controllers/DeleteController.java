/**
 * NOTE: This class is auto generated by OpenAPI Generator (https://openapi-generator.tech) (5.1.0).
 * https://openapi-generator.tech
 * Do not edit the class manually.
 */
package com.zhmenko.yandexrestservice.controllers;

import com.zhmenko.yandexrestservice.model.Error;

import java.util.UUID;

import com.zhmenko.yandexrestservice.model.exceptions.UnitNotFoundException;
import com.zhmenko.yandexrestservice.services.DeleteService;
import io.swagger.annotations.*;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Validated
@Api(value = "delete", description = "the delete API")
@RestController
@RequestMapping("/delete")
@RequiredArgsConstructor
public class DeleteController {
    private final DeleteService deleteService;

    /**
     * DELETE /delete/{id}
     * Удалить элемент по идентификатору. При удалении категории удаляются все дочерние элементы. Доступ к статистике (истории обновлений) удаленного элемента невозможен.  Так как время удаления не передается, при удалении элемента время обновления родителя изменять не нужно.  **Обратите, пожалуйста, внимание на этот обработчик. При его некорректной работе тестирование может быть невозможно.**
     *
     * @param id Идентификатор (required)
     * @return Удаление прошло успешно. (status code 200)
     * or Невалидная схема документа или входные данные не верны. (status code 400)
     * or Категория/товар не найден. (status code 404)
     */
    @ApiOperation(value = "", nickname = "deleteIdDelete", notes = "Удалить элемент по идентификатору. При удалении категории удаляются все дочерние элементы. Доступ к статистике (истории обновлений) удаленного элемента невозможен.  Так как время удаления не передается, при удалении элемента время обновления родителя изменять не нужно.  **Обратите, пожалуйста, внимание на этот обработчик. При его некорректной работе тестирование может быть невозможно.** ", tags = {"Базовые задачи",})
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Удаление прошло успешно."),
            @ApiResponse(code = 400, message = "Невалидная схема документа или входные данные не верны.", response = Error.class),
            @ApiResponse(code = 404, message = "Категория/товар не найден.", response = Error.class)})
    @DeleteMapping(
            value = "/{id}",
            produces = {"application/json"}
    )
    public ResponseEntity<Object> deleteById(@ApiParam(value = "Идентификатор", required = true) @PathVariable("id") UUID id) {
        if (!deleteService.deleteById(id)) throw new UnitNotFoundException();
        return new ResponseEntity<>("Удаление прошло успешно.", HttpStatus.OK);
    }

}
