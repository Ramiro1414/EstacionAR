package unpsjb.labprog.backend.business;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;
import unpsjb.labprog.backend.model.PatronPatente;

@Service
public class PatronesPatentesService {

    @Autowired
    PatronesPatentesRepository repository;

    public List<PatronPatente> findAll() {
        List<PatronPatente> result = new ArrayList<>();
        repository.findAll().forEach(e -> result.add(e));
        return result;
    }

    public PatronPatente findById(int id) {
        return repository.findById(id).orElse(null);
    }

    @Transactional
    public PatronPatente save(PatronPatente aPatronPatente) {
        return repository.save(aPatronPatente);
    }

    @Transactional
    public void delete(int id) {
        repository.deleteById(id);
    }

    public Page<PatronPatente> findByPage(int page, int size, Sort sort) {
        return repository.findAll(PageRequest.of(page, size, sort));
    }

}
