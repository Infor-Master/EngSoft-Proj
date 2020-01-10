package edu.ufp.esof.projecto.controllers;

import edu.ufp.esof.projecto.models.QuestaoRespondida;
import edu.ufp.esof.projecto.models.ResultadoAprendizagem;
import edu.ufp.esof.projecto.services.QuestaoRespondidaService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping("/questaoRespondida")
public class QuestaoRespondidaController {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    private QuestaoRespondidaService questaoRespondidaService;

    @Autowired
    public QuestaoRespondidaController(QuestaoRespondidaService questaoRespondidaService) {
        this.questaoRespondidaService = questaoRespondidaService;
    }
}
