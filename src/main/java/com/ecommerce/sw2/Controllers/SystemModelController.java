package com.ecommerce.sw2.Controllers;

import com.ecommerce.sw2.Models.Domain.SystemModel;
import com.ecommerce.sw2.Models.Services.SystemModelService;
import com.ecommerce.sw2.Validators.AddSystemModelFormValidators;
import com.ecommerce.sw2.forms.AddSystemModelForm;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * Created by Mina_Yousry on 11/04/2018.
 */

@RestController
@CrossOrigin(origins = "http://localhost:4200",allowedHeaders = "*")
public class SystemModelController {
    @Autowired
    private SystemModelService systemModelService;

    @Autowired
    private AddSystemModelFormValidators addSystemModelFormValidators;

    @InitBinder("addSystemModelForm")
    public void AddBrandFormInitBinder(WebDataBinder binder) {
        binder.addValidators(addSystemModelFormValidators);
    }


    @RequestMapping(name = "/addsystemmodel",method = RequestMethod.POST)
    public ResponseEntity<?> addsystemmodel(@Valid @RequestBody AddSystemModelForm addSystemModelForm,BindingResult bindingResult)
    {
       if (bindingResult.hasErrors())
        {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("name","null");
            return ResponseEntity.ok().body(jsonObject);
        }
        SystemModel systemModel = systemModelService.AddSystemModel(addSystemModelForm);
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("id",systemModel.getId());
        jsonObject.put("name",systemModel.getName());
        jsonObject.put("brand",systemModel.getBrand());
        return ResponseEntity.ok().body(jsonObject);
    }
}