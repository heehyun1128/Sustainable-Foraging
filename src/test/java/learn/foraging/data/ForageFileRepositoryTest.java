package learn.foraging.data;

import learn.foraging.domain.Result;
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
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ForageFileRepositoryTest {

    static final String SEED_FILE_PATH = "./data/forage-seed-2020-06-26.csv";
    static final String TEST_FILE_PATH = "./data/forage_data_test/2020-06-26.csv";
    static final String TEST_DIR_PATH = "./data/forage_data_test";
    static final int FORAGE_COUNT = 54;

    final LocalDate date = LocalDate.of(2020, 6, 26);
    final LocalDate future = LocalDate.of(202000, 6, 26);

    ForageFileRepository repository = new ForageFileRepository(TEST_DIR_PATH);

    @BeforeEach
    void setup() throws IOException {
        Path seedPath = Paths.get(SEED_FILE_PATH);
        Path testPath = Paths.get(TEST_FILE_PATH);
        Files.copy(seedPath, testPath, StandardCopyOption.REPLACE_EXISTING);
    }

    @Test
    void shouldFindByDate() {
        List<Forage> forages = repository.findByDate(date);
        assertEquals(FORAGE_COUNT, forages.size());
    }

    @Test
    void shouldNotFindByFutureDate() {
        List<Forage> forages = repository.findByDate(future);
        assertTrue(forages.isEmpty());
    }

    @Test
    void shouldAdd() throws DataException {
        Forage forage = new Forage();
        forage.setDate(date);
        forage.setKilograms(0.75);

        Item item = new Item();
        item.setId(12);
        forage.setItem(item);

        Forager forager = new Forager();
        forager.setId("AAAA-1111-2222-FFFF");
        forage.setForager(forager);

        forage = repository.add(forage);

        if(forage != null){
            assertEquals(36, forage.getId().length());
        }

    }


    @Test
    void shouldNotAddDuplicatedForages() throws DataException {
        Forage forage = new Forage();
        forage.setDate(date);
        forage.setKilograms(0.75);

        Item item = new Item();
        item.setId(16);
        forage.setItem(item);

        Forager forager = new Forager();
        forager.setId("39fc3738-b9fe-43a8-8886-c23f187c491b");
        forage.setForager(forager);

        forage = repository.add(forage);

        assertNull(forage);
    }

    @Test
    void shouldNotAddInvalidForages() throws DataException {
        Forage forage = new Forage();
        forage.setDate(future);
        forage.setKilograms(0.75);

        Item item = new Item();
        item.setId(12);
        forage.setItem(item);

        Forager forager = new Forager();
        forager.setId("AAAA-1111-2222-FFFF");
        forage.setForager(forager);

        forage = repository.add(forage);

        assertNull(forage);

//        forage with invalid kilograms
        Forage forage2 = new Forage();
        forage2.setDate(date);
        forage2.setKilograms(0);

        Item item2 = new Item();
        item2.setId(12);
        forage2.setItem(item);

        Forager forager2 = new Forager();
        forager2.setId("AAAA-1111-2222-FFFF");
        forage2.setForager(forager2);

        forage2 = repository.add(forage2);

        assertNull(forage2);

        //        forage with invalid kilograms
        Forage forage3 = new Forage();
        forage3.setDate(date);
        forage3.setKilograms(10000);

        Item item3 = new Item();
        item3.setId(12);
        forage3.setItem(item);

        Forager forager3 = new Forager();
        forager3.setId("AAAA-1111-2222-FFFF");
        forage3.setForager(forager3);

        forage3 = repository.add(forage3);

        assertNull(forage3);

//invalid item
        Forage forage4 = new Forage();
        forage4.setDate(date);
        forage4.setKilograms(0.75);

//        Item item4 = new Item();
//        item4.setId(20);
        forage4.setItem(null);

        Forager forager4 = new Forager();
        forager4.setId("AAAA-1111-22222-FFFF");
        forage4.setForager(forager4);

        forage4 = repository.add(forage4);

        assertNull(forage4);

//        invalid forager
        Forage forage5 = new Forage();
        forage5.setDate(date);
        forage5.setKilograms(0.75);

        Item item5 = new Item();
        item5.setId(12);
        forage5.setItem(item);


        forage5.setForager(null);

        forage5 = repository.add(forage5);

        assertNull(forage5);

    }


    @Test
    void shouldUpdateExisting() throws DataException{
        Forage forage=new Forage();
        forage.setId("63d82c24-4767-410f-9935-0198f1cf9ccd");
        forage.setDate(date);

        Forager forager=new Forager();
        forager.setId("0e4707f4-407e-4ec9-9665-baca0aabe88c");

        Item item = new Item();
        item.setId(1);

        forage.setKilograms(10);
        forage.setForager(forager);
        forage.setItem(item);

        boolean success=repository.update(forage);
        assertTrue(success);


        assertEquals(item,forage.getItem());
        assertEquals(10,forage.getKilograms());

    }

    @Test
    void shouldNotUpdateNull() throws DataException{
        Forage forage=new Forage();
        forage.setId("1-2-3-4");
        forage.setDate(date);

        Forager forager=new Forager();
        forager.setId("0e4707f4-407e-4ec9-9665-baca0aabe88c");

        Item item = new Item();
        item.setId(1);

        forage.setKilograms(10);
        forage.setForager(forager);
        forage.setItem(item);

        boolean success=repository.update(forage);
        assertFalse(success);


    }

    @Test
    void shouldNotUpdateWithInvalid() throws DataException{
//        no forager
        Forage forage=new Forage();
        forage.setId("63d82c24-4767-410f-9935-0198f1cf9ccd");
        forage.setDate(date);


        Item item = new Item();
        item.setId(1);

        forage.setKilograms(10);
        forage.setForager(null);
        forage.setItem(item);

        boolean success=repository.update(forage);
        assertFalse(success);

//no item
        Forage forage2=new Forage();
        forage2.setId("63d82c24-4767-410f-9935-0198f1cf9ccd");
        forage2.setDate(date);

        Forager forager2=new Forager();
        forager2.setId("0e4707f4-407e-4ec9-9665-baca0aabe88c");


        forage2.setKilograms(10);
        forage2.setForager(forager2);
        forage2.setItem(null);

        assertFalse(repository.update(forage2));
//        invalid kilo

        Forage forage3=new Forage();
        forage3.setId("63d82c24-4767-410f-9935-0198f1cf9ccd");
        forage3.setDate(date);

        Forager forager3=new Forager();
        forager3.setId("0e4707f4-407e-4ec9-9665-baca0aabe88c");

        Item item3 = new Item();
        item3.setId(1);

        forage3.setKilograms(100000);
        forage3.setForager(forager3);
        forage3.setItem(item);


        assertFalse(repository.update(forage3));

//        duplicate

        Forage forage4=new Forage();
        forage4.setId("63d82c24-4767-410f-9935-0198f1cf9ccd");
        forage4.setDate(date);

        Forager forager4=new Forager();
        forager4.setId("39fc3738-b9fe-43a8-8886-c23f187c491b");

        Item item4 = new Item();
        item4.setId(16);

        forage4.setKilograms(100000);
        forage4.setForager(forager3);
        forage4.setItem(item);


        assertFalse(repository.update(forage4));

    }


}