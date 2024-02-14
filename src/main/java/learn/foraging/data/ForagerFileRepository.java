package learn.foraging.data;

import learn.foraging.models.Forage;
import learn.foraging.models.Forager;

import java.io.*;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class ForagerFileRepository implements ForagerRepository {

    private final String filePath;

private static final String HEADER = "id,first_name,last_name,state";


    public ForagerFileRepository(String filePath) {
        this.filePath = filePath;
    }

    @Override
    public List<Forager> findAll() {
        ArrayList<Forager> result = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(getForagerFilePath()))) {

            reader.readLine(); // read header

            for (String line = reader.readLine(); line != null; line = reader.readLine()) {

                String[] fields = line.split(",", -1);
                if (fields.length == 4) {
                    result.add(deserialize(fields));
                }
            }
        } catch (IOException ex) {
            // don't throw on read
        }
        return result;
    }

    @Override
    public Forager findById(String id) {
        return findAll().stream()
                .filter(i -> i.getId().equalsIgnoreCase(id))
                .findFirst()
                .orElse(null);
    }

    @Override
    public List<Forager> findByState(String stateAbbr) {
//        System.out.println(findAll());
        List<Forager> res= findAll().stream()
                .filter(i -> i.getState().equalsIgnoreCase(stateAbbr))
                .collect(Collectors.toList());
//        System.out.println(res);
        return res;
    }

    @Override
    public Forager add(Forager forager) throws DataException {
        List<Forager> allForagers=findAll();

        if(!isValidForager(forager)){
            return null;
        }
        forager.setId(java.util.UUID.randomUUID().toString());
        allForagers.add(forager);
        writeAllForagers(allForagers);
        return forager;


    }

    private boolean isValidForager(Forager forager) {
        if(forager.getFirstName()==null){
            return false;
        }

        if(forager.getLastName()==null){
            return false;
        }

        if(forager.getState()==null){
            return false;
        }


        List<Forager> allForagers=findAll();

        return !allForagers.stream().anyMatch(f->f.getLastName().equalsIgnoreCase(forager.getLastName())
                && f.getFirstName().equalsIgnoreCase(forager.getFirstName())
                && f.getState().equalsIgnoreCase(forager.getState())
        );

    }



    private String getForagerFilePath(){
//        System.out.println("pathhhhh"+filePath);
        return Paths.get(".", "data", "forager_data_test", "foragers.csv").toString();
    }



    //    id,first_name,last_name,state
    private void writeAllForagers(List<Forager> foragers) throws DataException{
        try (PrintWriter writer = new PrintWriter(getForagerFilePath())) {

            writer.println(HEADER);

            for (Forager item : foragers) {
                writer.println(serialize(item));
            }
        } catch (FileNotFoundException ex) {
            throw new DataException(ex);
        }
    }

    private String serialize(Forager item) {
        return String.format("%s,%s,%s,%s",
                item.getId(),
                item.getFirstName(),
                item.getLastName(),
                item.getState());

    }
    private Forager deserialize(String[] fields) {
        Forager result = new Forager();
        result.setId(fields[0]);
        result.setFirstName(fields[1]);
        result.setLastName(fields[2]);
        result.setState(fields[3]);
        return result;
    }
}
