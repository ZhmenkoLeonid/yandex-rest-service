package com.zhmenko.yandexrestservice;

import com.zhmenko.yandexrestservice.data.UnitRepository;
import com.zhmenko.yandexrestservice.data.UnitRevisionRepository;
import com.zhmenko.yandexrestservice.model.*;
import com.zhmenko.yandexrestservice.model.shop_unit.ShopUnit;
import com.zhmenko.yandexrestservice.model.shop_unit.ShopUnitImport;
import com.zhmenko.yandexrestservice.model.shop_unit.ShopUnitImportRequest;
import com.zhmenko.yandexrestservice.services.DeleteService;
import com.zhmenko.yandexrestservice.services.ImportService;
import com.zhmenko.yandexrestservice.services.NodeStatisticService;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import lombok.AllArgsConstructor;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@SpringBootApplication
@OpenAPIDefinition
@EnableTransactionManagement
@EnableJpaRepositories
@EnableJpaAuditing
public class YandexRestServiceApplication {
    private static final String rootUUID = "40dcacd9-90a7-4bb7-b5b6-1832062d8145";
    private static final String categoryUUID = "98883e8f-0507-482f-bce2-2fb306cf6483";
    private static final String categoryUUID1 = "63610f21-1421-4079-99ab-2700b847f7e1";
    private static final String offerUUID = "dba28f59-a639-4b4c-b326-f69e93be94d4";
    private static final String offerUUID1 = "da58936d-7554-4e07-bb5e-d09b69f11952";

    public static void main(String[] args) {
        SpringApplication.run(YandexRestServiceApplication.class, args);
    }


    @AllArgsConstructor
    @Component
    static class InitClass implements ApplicationRunner {
        private UnitRepository unitRepository;

        private final UnitRevisionRepository unitAudRepository;
        private ImportService importService;
        private DeleteService deleteService;

        private NodeStatisticService nodeStatisticService;
        @Override
        public void run(ApplicationArguments args) throws Exception {
            initData();
            OffsetDateTime start = OffsetDateTime.of(
                    2022,
                    6,
                    10,
                    0,0,
                    0,
                    0,
                    ZoneOffset.UTC);
            OffsetDateTime end = OffsetDateTime.of(
                    2022,
                    6,
                    12,
                    23,0,
                    0,
                    0,
                    ZoneOffset.UTC);

            List<ShopUnit> byDateBetween = unitRepository.findByDateBetween(start, end);
            System.out.println();
            nodeStatisticService.findStatisticsById(UUID.fromString(offerUUID),null,null);
            deleteService.deleteById(UUID.fromString(categoryUUID1));
            nodeStatisticService.findStatisticsById(UUID.fromString(categoryUUID),null,null);
            nodeStatisticService.findStatisticsById(UUID.fromString(categoryUUID1),null,null);

            ShopUnitImportRequest request2 = new ShopUnitImportRequest();
            List<ShopUnitImport> requestItems2 = new ArrayList<>();
            request2.setUpdateDate(OffsetDateTime.of(2022, 6, 10, 23, 0, 0, 0, ZoneOffset.UTC));

            ShopUnitImport shopUnitImport4 = new ShopUnitImport();
            shopUnitImport4.setId(UUID.fromString(categoryUUID1));
            shopUnitImport4.setName("Стиральные машины");
            shopUnitImport4.setType(ShopUnitType.CATEGORY);
            requestItems2.add(shopUnitImport4);
            request2.setItems(requestItems2);
            importService.importItems(request2);

            nodeStatisticService.findStatisticsById(UUID.fromString(categoryUUID1),null,null);

            ShopUnitImport shopUnitImport5 = new ShopUnitImport();
            shopUnitImport5.setId(UUID.randomUUID());
            shopUnitImport5.setName("ColdPoint");
            shopUnitImport5.setType(ShopUnitType.OFFER);
            shopUnitImport5.setPrice(24999L);
            shopUnitImport5.setParentId(shopUnitImport4.getId());

            requestItems2.clear();
            requestItems2.add(shopUnitImport5);
            importService.importItems(request2);

            nodeStatisticService.findStatisticsById(UUID.fromString(categoryUUID1),null,null);
        }
        private void initData() {
            unitRepository.deleteAll();
            unitAudRepository.deleteAll();

            ShopUnitImportRequest request = new ShopUnitImportRequest();
            List<ShopUnitImport> requestItems = new ArrayList<>();
            request.setUpdateDate(OffsetDateTime.of(2022, 6, 10, 10, 50, 20, 0, ZoneOffset.UTC));

            ShopUnitImport shopUnitImport = new ShopUnitImport();
            shopUnitImport.setId(UUID.fromString(rootUUID));
            shopUnitImport.setName("Товары");
            shopUnitImport.setType(ShopUnitType.CATEGORY);

            requestItems.add(shopUnitImport);
            request.setItems(requestItems);

            importService.importItems(request);

            ShopUnitImportRequest request1 = new ShopUnitImportRequest();
            List<ShopUnitImport> requestItems1 = new ArrayList<>();
            request1.setUpdateDate(OffsetDateTime.of(2022, 6, 11, 15, 20, 40, 0, ZoneOffset.UTC));

            ShopUnitImport shopUnitImport1 = new ShopUnitImport();
            shopUnitImport1.setId(UUID.fromString(categoryUUID));
            shopUnitImport1.setName("Холодильники");
            shopUnitImport1.setType(ShopUnitType.CATEGORY);

            ShopUnitImport shopUnitImport2 = new ShopUnitImport();
            shopUnitImport2.setId(UUID.randomUUID());
            shopUnitImport2.setName("NordFrost");
            shopUnitImport2.setType(ShopUnitType.OFFER);
            shopUnitImport2.setPrice(32999L);
            shopUnitImport2.setParentId(shopUnitImport1.getId());

            ShopUnitImport shopUnitImport3 = new ShopUnitImport();
            shopUnitImport3.setId(UUID.fromString(offerUUID));
            shopUnitImport3.setName("Атлант");
            shopUnitImport3.setType(ShopUnitType.OFFER);
            shopUnitImport3.setPrice(45998L);
            shopUnitImport3.setParentId(shopUnitImport1.getId());

            requestItems1.add(shopUnitImport1);
            requestItems1.add(shopUnitImport2);
            requestItems1.add(shopUnitImport3);
            request1.setItems(requestItems1);
            importService.importItems(request1);


            ShopUnitImportRequest request2 = new ShopUnitImportRequest();
            List<ShopUnitImport> requestItems2 = new ArrayList<>();
            request2.setUpdateDate(OffsetDateTime.of(2022, 6, 10, 23, 0, 0, 0, ZoneOffset.UTC));

            ShopUnitImport shopUnitImport4 = new ShopUnitImport();
            shopUnitImport4.setId(UUID.fromString(categoryUUID1));
            shopUnitImport4.setName("Стиральные машины");
            shopUnitImport4.setType(ShopUnitType.CATEGORY);

            ShopUnitImport shopUnitImport5 = new ShopUnitImport();
            shopUnitImport5.setId(UUID.randomUUID());
            shopUnitImport5.setName("ColdPoint");
            shopUnitImport5.setType(ShopUnitType.OFFER);
            shopUnitImport5.setPrice(24999L);
            shopUnitImport5.setParentId(shopUnitImport4.getId());

            ShopUnitImport shopUnitImport6 = new ShopUnitImport();
            shopUnitImport6.setId(UUID.fromString(offerUUID1));
            shopUnitImport6.setName("Candy");
            shopUnitImport6.setType(ShopUnitType.OFFER);
            shopUnitImport6.setPrice(67998L);
            shopUnitImport6.setParentId(shopUnitImport4.getId());

            requestItems2.add(shopUnitImport4);
            requestItems2.add(shopUnitImport5);
            requestItems2.add(shopUnitImport6);
            request2.setItems(requestItems2);

            importService.importItems(request2);
        }
    }
}
