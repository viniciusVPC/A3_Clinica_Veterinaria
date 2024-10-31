package petmania.petmania.doutor;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PutMapping;

@RestController
@RequestMapping(path = "api/v1/doutor")
public class DoutorController {

    @Autowired
    private DoutorService doutorService;

    public DoutorController(DoutorService doutorService) {
        this.doutorService = doutorService;
    }

    // GET
    @GetMapping
    public List<Doutor> getDoutores() {
        return doutorService.getDoutores();
    }

    // POST
    @PostMapping
    public void cadastraNovoDoutor(@RequestBody Doutor doutor) {
        doutorService.addNewDoutor(doutor);
    }

    // DELETE
    @DeleteMapping(path = "{idDoutor}")
    public void deleteDoutor(@PathVariable("idDoutor") Long idDoutor) {
        doutorService.deleteDoutor(idDoutor);
    }

    // PUT
    @PutMapping(path = "{idDoutor}")
    public void updateDoutor(@PathVariable("idDoutor") Long idDoutor,
            @RequestParam(required = false) String nome,
            @RequestParam(required = false) LocalDate dataNasc,
            @RequestParam(required = false) String cpf,
            @RequestParam(required = false) String email,
            @RequestParam(required = false) String especialidade) {
        doutorService.updateDoutor(idDoutor, nome, dataNasc, cpf, email, especialidade);
    }
}
