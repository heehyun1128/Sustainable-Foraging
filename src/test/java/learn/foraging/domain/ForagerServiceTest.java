package learn.foraging.domain;

import learn.foraging.data.DataException;
import learn.foraging.data.ForagerRepositoryDouble;
import learn.foraging.models.Category;
import learn.foraging.models.Forager;
import learn.foraging.models.Item;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class ForagerServiceTest {

    ForagerService service = new ForagerService(new ForagerRepositoryDouble());


    @Test
    void shouldAdd() throws DataException {
        Forager forager = new Forager();
        forager.setDate(LocalDate.now());
        forager.setFirstName("Anna");
        forager.setLastName("Smith");
        forager.setState("GA");

        Result<Forager> result = service.add(forager);
        assertTrue(result.isSuccess());
        assertNotNull(result.getPayload());
        assertEquals("Anna", result.getPayload().getFirstName());
        assertEquals("Smith", result.getPayload().getLastName());
        assertEquals("GA", result.getPayload().getState());
    }

    @Test
    void shouldNotAddInvalid() throws DataException {
//        no first name
        Forager forager = new Forager();

        forager.setFirstName(null);
        forager.setLastName("Smith");
        forager.setState("GA");

        Result<Forager> result = service.add(forager);
        assertFalse(result.isSuccess());
        assertNull(result.getPayload());
        assertTrue(result.getErrorMessages().contains("First name is required."));

//        no last name
        Forager forager2 = new Forager();

        forager2.setFirstName("Diana");
        forager2.setLastName(null);
        forager2.setState("WA");

        Result<Forager> result2 = service.add(forager2);
        assertFalse(result2.isSuccess());
        assertNull(result2.getPayload());
        assertTrue(result2.getErrorMessages().contains("Last name is required."));

        //        no STATE
        Forager forager3 = new Forager();

        forager3.setFirstName("Ken");
        forager3.setLastName("Lee");
        forager3.setState(null);

        Result<Forager> result3 = service.add(forager3);
        assertFalse(result3.isSuccess());
        assertNull(result3.getPayload());
        assertTrue(result3.getErrorMessages().contains("State is required."));

    }




}