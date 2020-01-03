package edu.ufp.esof.projecto.controllers;

import edu.ufp.esof.projecto.services.CadeiraService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/cadeira")
public class CadeiraController {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    private CadeiraService cadeiraService;

    @Autowired
    public CadeiraController(CadeiraService cadeiraService) {
        this.cadeiraService = cadeiraService;
    }
}
