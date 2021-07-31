package uz.mehrojbek.appmagazinresiduebot.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import uz.mehrojbek.appmagazinresiduebot.entity.Product;
import uz.mehrojbek.appmagazinresiduebot.entity.enums.BranchTypeEnum;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product,Integer> {
    @Query(value = "select distinct brand from product ",nativeQuery = true)
    List<String> getAllBrands();

    List<Product> findAllByBrandAndIdGreaterThan(String brand, Integer id);
    List<Product> findAllByIdGreaterThan(Integer id);

    List<Product> findAllByNameContainingOrBrandContainingOrTariffContaining(String name, String brand, String tariff);
    List<Product> findAllByBranchType(BranchTypeEnum branchType);
}
