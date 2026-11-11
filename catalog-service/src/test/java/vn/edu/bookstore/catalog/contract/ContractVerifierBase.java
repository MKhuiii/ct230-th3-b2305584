package vn.edu.bookstore.catalog.contract;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

import java.math.BigDecimal;
import org.junit.jupiter.api.BeforeEach;
import io.restassured.module.mockmvc.RestAssuredMockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import vn.edu.bookstore.catalog.api.BookController;
import vn.edu.bookstore.catalog.api.error.GlobalProblemHandler;
import vn.edu.bookstore.catalog.domain.BookEntity;
import vn.edu.bookstore.catalog.service.BookNotFoundException;
import vn.edu.bookstore.catalog.service.BookService;

public abstract class ContractVerifierBase {

    @BeforeEach
    void setup() {
        BookService service = mock(BookService.class);
        
        // Sách tồn tại (hợp đồng getBookFound)
        given(service.get("9780134685991")).willReturn(new BookEntity(
                "9780134685991", "Effective Java", "Joshua Bloch", 
                42L, 2018, new BigDecimal("285000.00"), "VND"
        ));
        
        // Sách KHÔNG tồn tại (hợp đồng getBookNotFound mới thêm)
        given(service.get("9789999999999")).willThrow(new BookNotFoundException("9789999999999"));

        RestAssuredMockMvc.standaloneSetup(MockMvcBuilders.standaloneSetup(new BookController(service))
                .setControllerAdvice(new GlobalProblemHandler()));
    }
}
