package learn.foraging.data;

import learn.foraging.models.Forage;
import learn.foraging.models.Forager;
import learn.foraging.models.Item;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ForagerFileRepositoryTest {
    static final String SEED_FILE_PATH = "./data/foragers.csv";
    static final String TEST_FILE_PATH = "./data/forager_data_test/foragers.csv";
    static final String TEST_DIR_PATH = "./data/forager_data_test";
    ForagerFileRepository repository = new ForagerFileRepository(TEST_DIR_PATH);


    @BeforeEach
    void setup() throws IOException {
        Path seedPath = Paths.get(SEED_FILE_PATH);
        Path testPath = Paths.get(TEST_FILE_PATH);
        Files.copy(seedPath, testPath, StandardCopyOption.REPLACE_EXISTING);
    }

    @Test
    void shouldFindAll() {

        List<Forager> all = repository.findAll();
        assertEquals(1000, all.size());
    }

    @Test
    void shouldFindByState() {
        List<Forager> foragers = repository.findByState("TX");
//        System.out.println(foragers.size());
        assertEquals(101, foragers.size());
    }

    @Test
    void shouldAdd() throws DataException {
        Forager forager = new Forager();
        forager.setState("CA");
        forager.setLastName("LastName");
        forager.setFirstName("FirstName");


        forager = repository.add(forager);

        List<Forager> all = repository.findAll();
        assertEquals(1001, all.size());

    }

    @Test
    void shouldNotAddDuplicatedForagers() throws DataException {
        Forager forager = new Forager();
        forager.setState("GA");
        forager.setFirstName("Jilly");
        forager.setLastName("Sisse");


        forager = repository.add(forager);

        assertNull(forager);
    }


    @Test
    void shouldNotAddInvalidForagers() throws DataException {
//        no firstname
        Forager forager = new Forager();
        forager.setFirstName(null);
        forager.setLastName("Last");
        forager.setState("TX");

        forager = repository.add(forager);

        assertNull(forager);

//        no lastname
        Forager forager2 = new Forager();
        forager2.setFirstName("First");
        forager2.setLastName(null);
        forager2.setState("TX");

        forager2 = repository.add(forager2);

        assertNull(forager2);

        //        no state
        Forager forager3 = new Forager();
        forager3.setFirstName("First2");
            forager3.setLastName("Last2");
        forager3.setState(null);

        forager3 = repository.add(forager3);

        assertNull(forager3);
    }


}