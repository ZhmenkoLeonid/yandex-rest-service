/**
 * NOTE: This class is auto generated by OpenAPI Generator (https://openapi-generator.tech) (5.1.0).
 * https://openapi-generator.tech
 * Do not edit the class manually.
 */
package com.zhmenko.yandexrestservice.controllers;

import com.zhmenko.yandexrestservice.model.Error;
import com.zhmenko.yandexrestservice.model.ShopUnitImportRequest;
import com.zhmenko.yandexrestservice.services.ImportService;
import io.swagger.annotations.*;
import lombok.RequiredArgsConstructor;
import org.hibernate.stat.QueryStatistics;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.multipart.MultipartFile;
import springfox.documentation.annotations.ApiIgnore;

import javax.validation.Valid;
import javax.validation.constraints.*;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Validated
@Api(value = "imports", description = "the imports API")
@RestController
@RequestMapping("/imports")
@RequiredArgsConstructor
@CrossOrigin
public class ImportsController {
    private final ImportService importService;

    /**
     * POST /imports
     * Импортирует новые товары и/или категории. Товары/категории импортированные повторно обновляют текущие. Изменение типа элемента с товара на категорию или с категории на товар не допускается. Порядок элементов в запросе является произвольным.    - uuid товара или категории является уникальным среди товаров и категорий   - родителем товара или категории может быть только категория   - принадлежность к категории определяется полем parentId   - товар или категория могут не иметь родителя (при обновлении parentId на null, элемент остается без родителя)   - название элемента не может быть null   - у категорий поле price должно содержать null   - цена товара не может быть null и должна быть больше либо равна нулю.   - при обновлении товара/категории обновленными считаются **все** их параметры   - при обновлении параметров элемента обязательно обновляется поле **date** в соответствии с временем обновления   - в одном запросе не может быть двух элементов с одинаковым id   - дата должна обрабатываться согласно ISO 8601 (такой придерживается OpenAPI). Если дата не удовлетворяет данному формату, необходимо отвечать 400.  Гарантируется, что во входных данных нет циклических зависимостей и поле updateDate монотонно возрастает. Гарантируется, что при проверке передаваемое время кратно секундам.
     *
     * @param shopUnitImportRequest (optional)
     * @return Вставка или обновление прошли успешно. (status code 200)
     * or Невалидная схема документа или входные данные не верны. (status code 400)
     */
    @ApiOperation(value = "", nickname = "importsPost", notes = "Импортирует новые товары и/или категории. Товары/категории импортированные повторно обновляют текущие. Изменение типа элемента с товара на категорию или с категории на товар не допускается. Порядок элементов в запросе является произвольным.    - uuid товара или категории является уникальным среди товаров и категорий   - родителем товара или категории может быть только категория   - принадлежность к категории определяется полем parentId   - товар или категория могут не иметь родителя (при обновлении parentId на null, элемент остается без родителя)   - название элемента не может быть null   - у категорий поле price должно содержать null   - цена товара не может быть null и должна быть больше либо равна нулю.   - при обновлении товара/категории обновленными считаются **все** их параметры   - при обновлении параметров элемента обязательно обновляется поле **date** в соответствии с временем обновления   - в одном запросе не может быть двух элементов с одинаковым id   - дата должна обрабатываться согласно ISO 8601 (такой придерживается OpenAPI). Если дата не удовлетворяет данному формату, необходимо отвечать 400.  Гарантируется, что во входных данных нет циклических зависимостей и поле updateDate монотонно возрастает. Гарантируется, что при проверке передаваемое время кратно секундам. ", tags = {"Базовые задачи",})
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Вставка или обновление прошли успешно."),
            @ApiResponse(code = 400, message = "Невалидная схема документа или входные данные не верны.", response = Error.class)})
    @PostMapping(
            produces = {"application/json"},
            consumes = {"application/json"}
    )
    public ResponseEntity<String> postImports(@ApiParam(value = "") @Valid @RequestBody(required = false) ShopUnitImportRequest shopUnitImportRequest) {
        if (!importService.importItems(shopUnitImportRequest))
            return new ResponseEntity<>("Невалидная схема документа или входные данные не верны.", HttpStatus.BAD_REQUEST);
        return new ResponseEntity<>("Вставка или обновление прошли успешно.", HttpStatus.OK);
    }

}
