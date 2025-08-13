package com.effectivemobile.cardmanagement.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.tags.Tag;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author Дмитрий Ельцов
 **/
@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI bankApi() {
        return new OpenAPI()
                .info(new Info()
                        .title("Bank Cards Management API")
                        .description("REST API для управления банковскими картами и переводами")
                        .version("1.0")
                        .contact(new Contact()
                                .name("Дмитрий Ельцов")
                                .email("demonmaycry@mail.ru")))
                .addTagsItem(new Tag().name("Cards").description("Операции с картами"))
                .addTagsItem(new Tag().name("Transfers").description("Операции с переводами"));
    }
}
