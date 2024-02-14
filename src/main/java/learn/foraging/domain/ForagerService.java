package learn.foraging.domain;

import learn.foraging.data.DataException;
import learn.foraging.data.ForagerRepository;
import learn.foraging.models.Forage;
import learn.foraging.models.Forager;

import java.util.List;
import java.util.stream.Collectors;


public class ForagerService {

    private final ForagerRepository repository;


    public ForagerService(ForagerRepository repository) {
        this.repository = repository;
    }

    public List<Forager> findByState(String stateAbbr) {
        return repository.findByState(stateAbbr);
    }

    public List<Forager> findByLastName(String prefix) {
        return repository.findAll().stream()
                .filter(i -> i.getLastName().startsWith(prefix))
                .collect(Collectors.toList());
    }

    public List<Forager> findAll(){
        return repository.findAll();
    }

    public Result<Forager> add(Forager forager) throws DataException {
        Result<Forager> result = validate(forager);
        if (!result.isSuccess()) {
            return result;
        }

        result.setPayload(repository.add(forager));

        return result;
    }

    private Result<Forager> validate(Forager forager){
        Result<Forager> result=new Result<>();
        if(forager==null){
            result.addErrorMessage("Forager cannot be null.");
            return result;
        }

//        check firstname
        if(forager.getLastName()==null){
            result.addErrorMessage("Last name is required.");
        }

        if(forager.getFirstName()==null){
            result.addErrorMessage("First name is required.");
        }

//        state is required
        if(forager.getState()==null){
            result.addErrorMessage("State is required.");
        }

        if(isDuplicate(forager)){
            result.addErrorMessage("The combination of first name, last name, and state cannot be duplicated.");
        }

        return result;

    }

    private boolean isDuplicate(Forager forager){
        return repository.findAll().stream()
                .anyMatch(f->f.getLastName().equalsIgnoreCase(forager.getLastName())
                && f.getFirstName().equalsIgnoreCase(forager.getFirstName())
                       && f.getState().equalsIgnoreCase(forager.getState()));

    }
}
