package com.ecommerce.sw2.Controllers;

import com.ecommerce.sw2.Models.*;
import com.ecommerce.sw2.Repositories.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.ui.Model;

import java.util.ArrayList;
import java.util.Vector;


//@RequestMapping("/admin")
@Controller
@SessionAttributes("User")
public class AdminController {
    @Autowired
    private AdminRepo adminRepo;
    @Autowired
    private BrandRepo brandRepo;
    @Autowired
    private ModelRepo modelRepo;
    @Autowired
    private ProductRepo productRepo;
    @Autowired
    private SuggestedStoreRepo ssr;
    @Autowired
    private StoreRepo storeRepo;

    @RequestMapping(value = "/admin/adminLogin", method = RequestMethod.GET)
    public String Login(Model model)
    {
        return "adminLogin";
    }

    @RequestMapping(value = "/admin/register",method = RequestMethod.GET)
    public String adminRegister(Model model)
    {
        return "adminRegister";
    }

    @RequestMapping(value = "/admin/register", method = RequestMethod.POST)
    public String addAdmin(
            @RequestParam("Name") String name
            , @RequestParam("Email") String email
            , @RequestParam("Username") String username
            , @RequestParam("password") String password, Model model) {
        // @ResponseBody means the returned String is the response, not a view name
        // @RequestParam means it is a parameter from the GET or POST request
        AdminUser n = new AdminUser(name , email , username , password);
        if (adminRepo.existsById(username))
            return "index";
        adminRepo.save(n);
        return "index";
    }

    @RequestMapping(value = "/admin/adminLogin", method = RequestMethod.POST)
    public String login(
            @RequestParam("Username") String username,
            @RequestParam("password") String password, Model model) {
        AdminUser na = adminRepo.findAdminUserByUsernameAndPassword(username, password);
        if(na != null) {
            if(!model.containsAttribute("User"))
                model.addAttribute("User" , new AdminUser(na.getName(), na.getEmail() , na.getUsername() , ""));
            return "redirect:/";
        }
        else
            return "adminLogin";
    }
    @RequestMapping("/admin/all")
    public @ResponseBody Iterable<AdminUser> getAllUsers() {
        // This returns a JSON or XML with the users
        Iterable<AdminUser> iu = adminRepo.findAll();
        return iu;
    }

    @RequestMapping(value = "/admin/addNewBrand", method = RequestMethod.GET)
    public String openAddBrand(Model model){
        return "AddBrand";
    }

    @RequestMapping(value = "/admin/addNewBrand", method = RequestMethod.POST)
    public String AddNewBrand(@RequestParam("brand")  String brandname, Model model)
    {
        if(brandRepo.existsById(brandname))
            return "NotSavedBrand";
        else {
            Brand brand = new Brand(brandname);
            brandRepo.save(brand);
            return "SaveBrand";
        }
    }

    @GetMapping("/admin/addNewModel")
    public String addnewModel(){
        return "AddModel";
    }

    @PostMapping("/admin/addNewModel")
    public String AddNewModel(
            @RequestParam("model") String modelName
            , @RequestParam("brand")  String brandname) {

        if (modelRepo.existsById(modelName.toLowerCase()))
            return "NotSavedModel";
        else {
            if (!brandRepo.existsById(brandname)) {
                Brand brand = new Brand(brandname);
                brandRepo.save(brand);
            }
            Brand brand = brandRepo.findByName(brandname);
            com.ecommerce.sw2.Models.Model model = new com.ecommerce.sw2.Models.Model(modelName, brand);
            modelRepo.save(model);
            return "SaveModel";
        }
    }


    @GetMapping("/admin/confirmStores")
    public String GetAddNewStore(Model model)
    {
        Iterable<SuggestedStore> stores = ssr.findAll();
        Vector<SuggestedStore> list = new Vector();
        for(SuggestedStore s :stores) list.add(s);
        if (list == null || list.size() <=0) {
            model.addAttribute("found", "0");
            return "suggestedStores";
        }
        model.addAttribute("found","1");
        model.addAttribute("rows",list);
        return "suggestedStores";
    }

    @PostMapping("/admin/confirmStores")
    public String postAddNewStore(@RequestParam("storeName") String sn,
                                  @RequestParam("store_owner_name") String son ,
                                  Model model)
    {
        SuggestedStore s = ssr.findByNameAndStoreOwner(sn , son);
        if(s != null) {
            storeRepo.save(new Store(s.getName(), s.getStoreOwner()));
            System.out.println(s.getName() + " " + sn + "" + son);
            ssr.delete(s);
        }

        Iterable<SuggestedStore> stores = ssr.findAll();
        Vector<SuggestedStore> list = new Vector();
        for (SuggestedStore temp : stores) list.add(temp);
        if (list == null || list.size() <= 0) {
            model.addAttribute("found", "0");
            return "suggestedStores";
        }
        model.addAttribute("found", "1");
        model.addAttribute("rows", list);
        return "suggestedStores";
    }
    @RequestMapping(value = "/admin/home", method = RequestMethod.GET)
    public String getHomepage(Model model){
        return "AdminAfterLogin";
    }

}
