package learn.foraging.domain;

import learn.foraging.data.DataException;
import learn.foraging.data.ForageRepository;
import learn.foraging.data.ForagerRepository;
import learn.foraging.data.ItemRepository;
import learn.foraging.models.Category;
import learn.foraging.models.Forage;
import learn.foraging.models.Forager;
import learn.foraging.models.Item;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

public class ForageService {

    private final ForageRepository forageRepository;
    private final ForagerRepository foragerRepository;
    private final ItemRepository itemRepository;

    public ForageService(ForageRepository forageRepository, ForagerRepository foragerRepository, ItemRepository itemRepository) {
        this.forageRepository = forageRepository;
        this.foragerRepository = foragerRepository;
        this.itemRepository = itemRepository;
    }

    public List<Forage> findByDate(LocalDate date) {

        Map<String, Forager> foragerMap = foragerRepository.findAll().stream()
                .collect(Collectors.toMap(i -> i.getId(), i -> i));
        Map<Integer, Item> itemMap = itemRepository.findAll().stream()
                .collect(Collectors.toMap(i -> i.getId(), i -> i));

        List<Forage> result = forageRepository.findByDate(date);
        for (Forage forage : result) {
            forage.setForager(foragerMap.get(forage.getForager().getId()));
            forage.setItem(itemMap.get(forage.getItem().getId()));
        }

        return result;
    }

    public Result<Forage> add(Forage forage) throws DataException {
        Result<Forage> result = validate(forage);
        if (!result.isSuccess()) {
            return result;
        }

        result.setPayload(forageRepository.add(forage));

        return result;
    }

    public int generate(LocalDate start, LocalDate end, int count) throws DataException {

        if (start == null || end == null || start.isAfter(end) || count <= 0) {
            return 0;
        }

        count = Math.min(count, 500);

        ArrayList<LocalDate> dates = new ArrayList<>();
        while (!start.isAfter(end)) {
            dates.add(start);
            start = start.plusDays(1);
        }

        List<Item> items = itemRepository.findAll();
        List<Forager> foragers = foragerRepository.findAll();
        Random random = new Random();

        for (int i = 0; i < count; i++) {
            Forage forage = new Forage();
            forage.setDate(dates.get(random.nextInt(dates.size())));
            forage.setForager(foragers.get(random.nextInt(foragers.size())));
            forage.setItem(items.get(random.nextInt(items.size())));
            forage.setKilograms(random.nextDouble() * 5.0 + 0.1);
            forageRepository.add(forage);
        }

        return count;
    }

    private Result<Forage> validate(Forage forage) {

        Result<Forage> result = validateNulls(forage);
        if (!result.isSuccess()) {
            return result;
        }

        validateFields(forage, result);
        if (!result.isSuccess()) {
            return result;
        }

        validateChildrenExist(forage, result);

        return result;
    }

    private Result<Forage> validateNulls(Forage forage) {
        Result<Forage> result = new Result<>();

        if (forage == null) {
            result.addErrorMessage("Nothing to save.");
            return result;
        }

        if (forage.getDate() == null) {
            result.addErrorMessage("Forage date is required.");
        }

        if (forage.getForager() == null) {
            result.addErrorMessage("Forager is required.");
        }

        if (forage.getItem() == null) {
            result.addErrorMessage("Item is required.");
        }
        return result;
    }

    private void validateFields(Forage forage, Result<Forage> result) {
        // No future dates.
        if (forage.getDate().isAfter(LocalDate.now())) {
            result.addErrorMessage("Forage date cannot be in the future.");
        }

        if (forage.getKilograms() <= 0 || forage.getKilograms() > 250.0) {
            result.addErrorMessage("Kilograms must be a positive number less than 250.0");
        }
    }

    private void validateChildrenExist(Forage forage, Result<Forage> result) {

        if (forage.getForager().getId() == null
                || foragerRepository.findById(forage.getForager().getId()) == null) {
            result.addErrorMessage("Forager does not exist.");
        }

        if (itemRepository.findById(forage.getItem().getId()) == null) {
            result.addErrorMessage("Item does not exist.");
        }
    }

    public List<String> reportKgPerItemOneDay(LocalDate date){

        List<Forage> forages=findByDate(date);

        Map<Item, Double> kgPerItem=forages.stream().collect(Collectors.groupingBy(Forage::getItem,Collectors.summingDouble(Forage::getKilograms)));
        System.out.println("kgPerItem"+kgPerItem.entrySet());
        List<String> res=new ArrayList<>();
        for(Map.Entry<Item, Double> entry: kgPerItem.entrySet()){
            Item item=entry.getKey();
            System.out.println("item"+item);
            double kgSum=entry.getValue();
            String str=item.getName()+": "+kgSum+" kg";
            res.add(str);
        }


        return res;
    }

    public List<String> reportValPerCatagoryOneDay(LocalDate date){
//

        List<Forage> forages=findByDate(date);

        Map<Category,Double> kgPerCategory=forages.stream().collect(Collectors.groupingBy(f->f.getItem().getCategory(),Collectors.summingDouble(Forage::getKilograms)));
        Map<Category, BigDecimal> totalValPerCategory=new HashMap<>();

        for(Map.Entry<Category, Double> entry: kgPerCategory.entrySet()){
            Category category=entry.getKey();
            double kgSum=entry.getValue();

            List<Item> itemsOfCategory=forages.stream().filter(f->f.getItem().getCategory()==category)
                    .map(Forage::getItem)
                    .distinct()
                    .collect(Collectors.toList());

            BigDecimal totolVal=itemsOfCategory.stream().map(item -> BigDecimal.valueOf(kgSum).multiply(item.getDollarPerKilogram()))
                    .reduce(BigDecimal.ZERO, BigDecimal::add);

            totalValPerCategory.put(category,totolVal);


        }

        List<String> res=new ArrayList<>();
        for(Map.Entry<Category,BigDecimal> entry: totalValPerCategory.entrySet()){
            Category category=entry.getKey();
            BigDecimal val=entry.getValue();

            String str=category.name()+": "+"$"+val;
            res.add(str);
        }
        return res;
//        view.displayValPerCatagory(res);
    }

}
