package com.shop.catalog.web.exception;

import com.shop.catalog.web.controller.CatalogQueryController;
import com.shop.catalog.web.controller.InternalCatalogController;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;

@WebMvcTest({InternalCatalogController.class, CatalogQueryController.class})
public class GlobalExceptionHandlerTest {

}
