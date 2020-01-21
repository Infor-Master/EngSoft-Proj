package edu.ufp.esof.projecto;

import edu.ufp.esof.projecto.models.*;
import edu.ufp.esof.projecto.models.builders.*;
import edu.ufp.esof.projecto.repositories.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

@Component
//@Transactional
public class Bootstrap implements ApplicationListener<ContextRefreshedEvent> {


    private AlunoRepo alunoRepo;
    private CadeiraRepo cadeiraRepo;
    private ComponenteRepo componenteRepo;
    private CriterioRepo criterioRepo;
    private DocenteRepo docenteRepo;
    private MomentoRealizadoRepo momentoRealizadoRepo;
    private MomentoRepo momentoRepo;
    private OfertaRepo ofertaRepo;
    private QuestaoRepo questaoRepo;
    private QuestaoRespondidaRepo questaoRespondidaRepo;
    private ResultadoAprendizagemRepo resultadoAprendizagemRepo;


    @Autowired
    public Bootstrap(AlunoRepo alunoRepo, CadeiraRepo cadeiraRepo,
                     ComponenteRepo componenteRepo, CriterioRepo criterioRepo,
                     DocenteRepo docenteRepo, MomentoRealizadoRepo momentoRealizadoRepo,
                     MomentoRepo momentoRepo, OfertaRepo ofertaRepo,
                     QuestaoRepo questaoRepo, QuestaoRespondidaRepo questaoRespondidaRepo,
                     ResultadoAprendizagemRepo resultadoAprendizagemRepo) {

        this.alunoRepo = alunoRepo;
        this.cadeiraRepo = cadeiraRepo;
        this.componenteRepo = componenteRepo;
        this.criterioRepo = criterioRepo;
        this.docenteRepo = docenteRepo;
        this.momentoRealizadoRepo = momentoRealizadoRepo;
        this.momentoRepo = momentoRepo;
        this.ofertaRepo = ofertaRepo;
        this.questaoRepo = questaoRepo;
        this.questaoRespondidaRepo = questaoRespondidaRepo;
        this.resultadoAprendizagemRepo = resultadoAprendizagemRepo;
    }

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Override
    public void onApplicationEvent(ContextRefreshedEvent contextRefreshedEvent) {
        logger.debug("Startup");

        Aluno aluno1 = new AlunoBuilder().setName("João").setCode("37190").build();
        //Aluno aluno1 = new Aluno("João", "37190");
        Aluno aluno2 = new Aluno("Luísa", "37151");
        Aluno aluno3 = new Aluno("Rodrigo", "36824");
        Aluno aluno4 = new Aluno("Inês", "36502");

        Cadeira cadeira1 = new Cadeira("Engenharia de Software", "1");
        Cadeira cadeira2 = new Cadeira("Redes de Computadores I", "2");
        Cadeira cadeira3 = new Cadeira("Base de Dados", "3");
        Cadeira cadeira4 = new Cadeira("Laboratório de Programação", "4");

        cadeiraRepo.save(cadeira1);
        cadeiraRepo.save(cadeira2);
        cadeiraRepo.save(cadeira3);
        cadeiraRepo.save(cadeira4);


        alunoRepo.save(aluno1);
        alunoRepo.save(aluno2);
        alunoRepo.save(aluno3);
        alunoRepo.save(aluno4);
        /*
        try {
            alunoRepo.save(aluno4);
        }catch (Exception e){
            logger.error(e.getMessage());
        }
        */
        aluno1.setCode("18");
        alunoRepo.save(aluno1);

        Oferta oferta1 = new Oferta(2020,cadeira1);
        Oferta oferta2 = new Oferta(2019,cadeira1);
        ofertaRepo.save(oferta1);
        ofertaRepo.save(oferta2);
        Docente docente1 = new Docente("Prof", "123");
        docenteRepo.save(docente1);
        //Componente componente1 = new ComponenteBuilder().setType("pratica").setOferta(oferta1)
        //        .setDocente(docente1).addAluno(aluno1).addAluno(aluno2).addAluno(aluno3).build();

        Componente componente1 = new ComponenteBuilder().setType("pratica").setOferta(oferta1)
                .setDocente(docente1).build();


        /*componente1.getAlunos().add(aluno1);
        componente1.getAlunos().add(aluno2);
        componente1.getAlunos().add(aluno3);
        aluno1.getComponentes().add(componente1);
        aluno2.getComponentes().add(componente1);
        aluno3.getComponentes().add(componente1);*/
        //Componente componente1 = new Componente("pratica", oferta1);
        componenteRepo.save(componente1);
        docenteRepo.save(docente1);
        alunoRepo.save(aluno1);
        alunoRepo.save(aluno2);
        alunoRepo.save(aluno3);

        ResultadoAprendizagem ra1 = new RaBuilder().setDesignation("SQL").setOferta(oferta1).build();
        resultadoAprendizagemRepo.save(ra1);
        ofertaRepo.save(oferta1);
        Momento momento1 = new MomentoBuilder().setComponente(componente1).setDesignation("teste1")
                .setPeso(0.70f).build();
        momentoRepo.save(momento1);
        Questao questao1 = new QuestaoBuilder().setDesignation("questao1").setMomento(momento1)
                .setPesoMomento(0.3f).setPesoRA(0.5f).setRa(ra1).build();
        questaoRepo.save(questao1);

        for (Aluno a : componente1.getAlunos()) {
            MomentoRealizado mr = new MomentoRealizadoBuilder().setAluno(a).setMomento(momento1)
                    .build();
            momentoRealizadoRepo.save(mr);
            for (Questao q : momento1.getQuestoes()) {
                QuestaoRespondida qr = new QuestaoRespondidaBuilder().setMomento(mr).setQuestao(q).build();
                questaoRespondidaRepo.save(qr);
            }
            momentoRealizadoRepo.save(mr);
        }

        for (Aluno a : momento1.getComponente().getAlunos()) {
            for (MomentoRealizado mr : a.getMomentos()){
                if (mr.getMomento().getId() == momento1.getId()){
                    QuestaoRespondida qr = new QuestaoRespondidaBuilder().setMomento(mr).setQuestao(questao1).build();
                    mr.getQuestoes().add(qr);
                    questaoRespondidaRepo.save(qr);
                    break;
                }
            }
        }


        questaoRepo.save(questao1);
        momentoRepo.save(momento1);

        Componente componente2 = new Componente("teorica", oferta1);
        Componente componente3 = new Componente("teorica", oferta2);


        cadeiraRepo.save(cadeira1);

        /*componente3.getAlunos().add(aluno1);
        componente3.getAlunos().add(aluno2);
        componente3.getAlunos().add(aluno3);

        componente2.getAlunos().add(aluno1);
        componente2.getAlunos().add(aluno2);

        aluno1.getComponentes().add(componente2);
        aluno2.getComponentes().add(componente2);
        aluno1.getComponentes().add(componente3);
        aluno2.getComponentes().add(componente3);
        aluno3.getComponentes().add(componente3);
        //componente1.setDocente(docente1);

        //ofertaRepo.save(oferta1);
        //componenteRepo.save(componente1);
        Escala criterio1 = new Escala("A", 20.0f);
        Escala criterio2 = new Escala("B", 16.0f);
        cadeira1.getEscalas().add(criterio1);
        cadeira1.getEscalas().add(criterio2);
        criterioRepo.save(criterio1);
        criterioRepo.save(criterio2);
        //ofertaRepo.save(oferta1);
        cadeiraRepo.save(cadeira1);
        docenteRepo.save(docente1);


        cadeiraRepo.save(cadeira1);*/


    }
}