package com.ecommerce.sw2.Models.Domain;

import com.ecommerce.sw2.Models.Repository.ActionRepository;
import com.ecommerce.sw2.Models.Repository.ProductBackUpRepository;
import com.ecommerce.sw2.Models.Repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Indexed;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.Transient;

/**
 * Created by Mina_Yousry on 15/04/2018.
 */
@Entity(name = "RemoveProduct")
@Indexed
@DiscriminatorValue(value = "remove_product")
public class RemoveProduct extends Action {

//ac

    public RemoveProduct(){
        super();
    }


    @Override
    public Product Do(Product product,ActionRepository actionRepository,ProductRepository productRepository, ProductBackUpRepository productBackUpRepository) {
        this.productBackup.equal(product);
        productRepository.delete(product);
        this.productBackup = productBackUpRepository.save(productBackup);
        actionRepository.save(this);
        return product;
    }

    @Override
    public Product Undo(Long id,ActionRepository actionRepository,ProductRepository productRepository, ProductBackUpRepository productBackUpRepository) {
        Product p = new Product();
        Action action = actionRepository.getOne(id);
        action.productBackup.to(p);
        AddProduct add = new AddProduct();
        return add.Do(p,actionRepository,productRepository,  productBackUpRepository);
    }
}