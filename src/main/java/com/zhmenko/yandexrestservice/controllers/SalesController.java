/**
 * NOTE: This class is auto generated by OpenAPI Generator (https://openapi-generator.tech) (5.1.0).
 * https://openapi-generator.tech
 * Do not edit the class manually.
 */
package com.zhmenko.yandexrestservice.controllers;

import com.zhmenko.yandexrestservice.model.Error;
import java.time.OffsetDateTime;
import com.zhmenko.yandexrestservice.model.shop_unit.ShopUnitStatisticResponse;
import com.zhmenko.yandexrestservice.services.SalesService;
import io.swagger.annotations.*;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.*;

@Validated
@Api(value = "sales", description = "the sales API")
@RestController()
@RequestMapping("/sales")
@RequiredArgsConstructor
public class SalesController {
    private final SalesService salesService;
    /**
     * GET /sales
     * Получение списка **товаров**, цена которых была обновлена за последние 24 часа включительно [now() - 24h, now()] от времени переданном в запросе. Обновление цены не означает её изменение. Обновления цен удаленных товаров недоступны. При обновлении цены товара, средняя цена категории, которая содержит этот товар, тоже обновляется. 
     *
     * @param date Дата и время запроса. Дата должна обрабатываться согласно ISO 8601 (такой придерживается OpenAPI). Если дата не удовлетворяет данному формату, необходимо отвечать 400 (required)
     * @return Список товаров, цена которых была обновлена. (status code 200)
     *         or Невалидная схема документа или входные данные не верны. (status code 400)
     */
    @ApiOperation(value = "", nickname = "salesGet", notes = "Получение списка **товаров**, цена которых была обновлена за последние 24 часа включительно [now() - 24h, now()] от времени переданном в запросе. Обновление цены не означает её изменение. Обновления цен удаленных товаров недоступны. При обновлении цены товара, средняя цена категории, которая содержит этот товар, тоже обновляется. ", response = ShopUnitStatisticResponse.class, tags={ "Дополнительные задачи", })
    @ApiResponses(value = { 
        @ApiResponse(code = 200, message = "Список товаров, цена которых была обновлена.", response = ShopUnitStatisticResponse.class),
        @ApiResponse(code = 400, message = "Невалидная схема документа или входные данные не верны.", response = Error.class) })
    @GetMapping(
        value = "",
        produces = { "application/json" }
    )
    public ResponseEntity<ShopUnitStatisticResponse> getSales(@NotNull
                                                                  @ApiParam(value = "Дата и время запроса. Дата должна обрабатываться согласно ISO 8601 (такой придерживается OpenAPI). " +
                                                                          "Если дата не удовлетворяет данному формату, необходимо отвечать 400", required = true)
                                                                  @Valid
                                                                  @RequestParam(value = "date")
                                                                  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
                                                                          OffsetDateTime date) {
        OffsetDateTime previousDay = date.minusDays(1);
        return new ResponseEntity<>(salesService.findUpdatedUnitsBetweenDates(previousDay, date), HttpStatus.OK);
    }

}
