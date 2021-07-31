package uz.mehrojbek.appmagazinresiduebot.entity;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import uz.mehrojbek.appmagazinresiduebot.entity.enums.BranchTypeEnum;

import javax.persistence.*;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Enumerated(EnumType.STRING)
    private BranchTypeEnum branchType;

    @Column(nullable = false)
    private String type;

    @Column(nullable = false)
    private String brand;

    @Column(nullable = false)
    private String name;

    private String tariff;

    private Double size;

    @Column(nullable = false)
    private Integer residue;

    private String measurement;

    @Column(nullable = false)
    private String currency;

    private Double priceUsd;

    private Double priceSom;

    @Column(nullable = false)
    private Double sumPrice;
}
