//package learn.foraging.domain;
//
//import learn.foraging.data.ForageFileRepository;
//import learn.foraging.data.ForageRepository;
//import learn.foraging.models.Category;
//import learn.foraging.models.Forage;
//import learn.foraging.models.Item;
//
//import learn.foraging.ui.View;
//
//import java.math.BigDecimal;
//import java.time.LocalDate;
//import java.util.ArrayList;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//import java.util.stream.Collectors;
//
//public class ReportService {
//
//
//    private final ForageFileRepository forageFileRepository;
////
//
//    public ReportService(ForageSforageFileRepository){
//
//        this.forageFileRepository=forageFileRepository;
////        this.view=view;
//    }
//    public List<String> reportKgPerItemOneDay(LocalDate date){
////       List<Forage> forages=forageFileRepository.findByDate(date);
////
//        List<Forage> forages=forageService.findByDate(date);
//        Map<Item, Double> kgPerItem=forages.stream().collect(Collectors.groupingBy(Forage::getItem,Collectors.summingDouble(Forage::getKilograms)));
//        System.out.println("kgPerItem"+kgPerItem.entrySet());
//        List<String> res=new ArrayList<>();
//        for(Map.Entry<Item, Double> entry: kgPerItem.entrySet()){
//            Item item=entry.getKey();
//            System.out.println("item"+item);
//            double kgSum=entry.getValue();
//            String str=item.getName()+": "+kgSum+" kg";
//            res.add(str);
//        }
//
//        return res;
////        view.displayKgPerItems(res);
//    }
//
//    public List<String> reportValPerCatagoryOneDay(LocalDate date){
////
//
//        List<Forage> forages=forageFileRepository.findByDate(date);
//
//        Map<Category,Double> kgPerCategory=forages.stream().collect(Collectors.groupingBy(f->f.getItem().getCategory(),Collectors.summingDouble(Forage::getKilograms)));
//        Map<Category, BigDecimal> totalValPerCategory=new HashMap<>();
//
//        for(Map.Entry<Category, Double> entry: kgPerCategory.entrySet()){
//            Category category=entry.getKey();
//            double kgSum=entry.getValue();
//
//            List<Item> itemsOfCategory=forages.stream().filter(f->f.getItem().getCategory()==category)
//                    .map(Forage::getItem)
//                    .distinct()
//                    .collect(Collectors.toList());
//
//            BigDecimal totolVal=itemsOfCategory.stream().map(item -> BigDecimal.valueOf(kgSum).multiply(item.getDollarPerKilogram()))
//                    .reduce(BigDecimal.ZERO, BigDecimal::add);
//
//            totalValPerCategory.put(category,totolVal);
//
//
//        }
//
//        List<String> res=new ArrayList<>();
//        for(Map.Entry<Category,BigDecimal> entry: totalValPerCategory.entrySet()){
//            Category category=entry.getKey();
//            BigDecimal val=entry.getValue();
//
//            String str=category.name()+": "+"$"+val;
//            res.add(str);
//        }
//        return res;
////        view.displayValPerCatagory(res);
//    }
//
//}
