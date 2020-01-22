package edu.ufp.esof.projecto.services;

import edu.ufp.esof.projecto.models.*;
import edu.ufp.esof.projecto.models.builders.QuestaoRespondidaBuilder;
import edu.ufp.esof.projecto.repositories.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@Service
public class QuestaoRespondidaService {

    private QuestaoRespondidaRepo questaoRespondidaRepo;
    private MomentoRealizadoRepo momentoRealizadoRepo;
    private DocenteRepo docenteRepo;
    private AlunoRepo alunoRepo;
    private EscalaService escalaService;
    // Falta o Filtro do serviço e no constructor

    @Autowired
    public QuestaoRespondidaService(QuestaoRespondidaRepo questaoRespondidaRepo, EscalaRepo escalaRepo, DocenteRepo docenteRepo, AlunoRepo alunoRepo, MomentoRealizadoRepo momentoRealizadoRepo, EscalaService escalaService) {
        this.questaoRespondidaRepo = questaoRespondidaRepo;
        this.momentoRealizadoRepo = momentoRealizadoRepo;
        this.docenteRepo = docenteRepo;
        this.alunoRepo = alunoRepo;
        this.escalaService = escalaService;
    }

    public Set<QuestaoRespondida> findAll(){
        Set<QuestaoRespondida> questaoRespondidas=new HashSet<>();
        for (QuestaoRespondida qr:this.questaoRespondidaRepo.findAll()) {
            questaoRespondidas.add(qr);
        }
        return questaoRespondidas;
    }

    public Optional<QuestaoRespondida> findById(Long id){
        Optional<QuestaoRespondida> optionalQuestaoRespondida=Optional.empty();
        for (QuestaoRespondida qr:this.questaoRespondidaRepo.findAll()) {
            if(qr.getId().compareTo(id)==0){
                optionalQuestaoRespondida=Optional.of(qr);
                break;
            }
        }
        return optionalQuestaoRespondida;
    }

    public Optional<QuestaoRespondida> updateQuestaoRespondida(Long id, QuestaoRespondida questaoRespondida){
        Optional<QuestaoRespondida> optionalQuestaoRespondida=this.questaoRespondidaRepo.findById(id);
        if(optionalQuestaoRespondida.isPresent()){
            questaoRespondidaRepo.save(questaoRespondida);
            return optionalQuestaoRespondida;
        }
        return Optional.empty();
    }

    public Boolean deleteQuestaoRespondida(Long id){
        Optional<QuestaoRespondida> optionalQuestaoRespondida=this.questaoRespondidaRepo.findById(id);
        if(optionalQuestaoRespondida.isPresent()){
            delete(optionalQuestaoRespondida.get());
            return true;
        }
        return false;
    }

    public void delete(QuestaoRespondida qr){
        if (qr.getMomento() != null){
            qr.getMomento().getQuestoes().remove(qr);
            qr.setMomento(null);
        }
        if (qr.getEscala() != null){
            qr.setEscala(null);
        }
        questaoRespondidaRepo.delete(qr);
    }

    public void create(Questao q){
        for (Aluno a : q.getMomento().getComponente().getAlunos()) {
            for (MomentoRealizado mr : a.getMomentos()) {
                if (mr.getMomento().getId() == q.getMomento().getId()){
                    QuestaoRespondida qr = new QuestaoRespondidaBuilder().setQuestao(q)
                            .setMomento(mr).build();
                    questaoRespondidaRepo.save(qr);
                    momentoRealizadoRepo.save(mr);
                    break;
                }
            }
        }
    }
    
    public Optional<QuestaoRespondida> atribuirNota(NotaRequest nr){
        Optional<Docente> optionalDocente = docenteRepo.findByCode(nr.getDocenteNumero());
        Optional<Aluno> optionalAluno = alunoRepo.findByCode(nr.getAlunoNumero());
        if (optionalAluno.isPresent() && optionalDocente.isPresent()){
            Docente docente = optionalDocente.get();
            Aluno aluno = optionalAluno.get();
            for (Componente c : docente.getComponentes()) {
                if (c.getOferta().getAno() == nr.getAno() &&
                        c.getOferta().getCadeira().getDesignation().equals(nr.getCadeiraNome()) &&
                        c.getType().equals(nr.getComponente())){
                    Escala escala = null;
                    for (Escala e : c.getOferta().getCadeira().getEscalas()) {
                        if (e.getDesignation().equals(nr.getEscalaNome())){
                            escala = e;
                            break;
                        }
                    }
                    if (escala == null){
                        return Optional.empty();
                    }
                    for (Momento m : c.getMomentos()) {
                        if (m.getDesignation().equals(nr.getMomentoNome())){
                            float peso = 0.0f;
                            for (Questao q : m.getQuestoes()) {
                                peso += q.getPesoMomento();
                            }
                            if (peso != 1){
                                return Optional.empty();
                            }
                            for (Questao q : m.getQuestoes()) {
                                if (q.getDesignation().equals(nr.getQuestaoNome())){
                                    Optional<Iterable<QuestaoRespondida>> optionalQuestoesRespondidas = questaoRespondidaRepo.findAllByQuestao(q);
                                    if (optionalQuestoesRespondidas.isPresent()){
                                        for (QuestaoRespondida qr : optionalQuestoesRespondidas.get()) {
                                            if (qr.getMomento().getAluno().getCode().equals(nr.getAlunoNumero())){
                                                qr.setEscala(escala);
                                                questaoRespondidaRepo.save(qr);
                                                return Optional.of(qr);
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        return Optional.empty();
    }
}
