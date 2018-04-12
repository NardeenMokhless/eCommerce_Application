package com.ecommerce.sw2.Controllers;

import com.ecommerce.sw2.Models.Domain.Store;
import com.ecommerce.sw2.Models.Repository.StoreRepository;
import com.ecommerce.sw2.Models.Services.StoreService;
import com.ecommerce.sw2.Validators.AddStoreFormValidator;
import com.ecommerce.sw2.forms.StoreForm;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
public class StoreOwnerController {

    @Autowired
    private StoreService storeService;

    @Autowired
    AddStoreFormValidator addStoreFormValidator;

    @InitBinder("addStoreFormValidator")
    public void AddStoreFormInitBinder(WebDataBinder binder) {
        binder.addValidators(addStoreFormValidator);
    }

    @RequestMapping(name = "/addstore" , method = RequestMethod.POST)
    public ResponseEntity<?> addStore(@Valid @RequestBody StoreForm storeForm , BindingResult bindingResult)
    {
        if(bindingResult.hasErrors())
            return ResponseEntity.badRequest().body(bindingResult.getAllErrors());

        Store store = storeService.createStore(storeForm);
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("id", store.getId());
        jsonObject.put("name", store.getName());
        return ResponseEntity.ok().body(jsonObject);
    }

}
