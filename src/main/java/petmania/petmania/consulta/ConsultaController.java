//Cuida da camada API
package petmania.petmania.consulta;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
@RequestMapping(path = "api/v1/consulta")
public class ConsultaController {

    @Autowired
    private ConsultaService consultaService;

    public ConsultaController(ConsultaService consultaService) {
        this.consultaService = consultaService;
    }

    // GET
    @GetMapping
    public List<Consulta> getConsultas() {
        return consultaService.getConsultas();
    }

    // POST
    @PostMapping(path = "/cliente/{idCliente}/animal/{idAnimal}/doutor/{idDoutor}")
    public void criaConsulta(@RequestBody Consulta consulta, @PathVariable Long idCliente, @PathVariable Long idAnimal, @PathVariable Long idDoutor) {
        System.out.println(consulta);
        consultaService.addNewConsulta(consulta, idCliente, idAnimal, idDoutor);
    }

    // DELETE
    @DeleteMapping(path = "{idConsulta}")
    public void deleteConsulta(@PathVariable("idConsulta") Long idConsulta) {
        consultaService.deleteConsulta(idConsulta);
    }

    // PUT
    @PutMapping(path = "{idConsulta}")
    public void updateConsulta(@PathVariable("idConsulta") Long idConsulta,
            @RequestParam(required = false) String tipo,
            @RequestParam(required = false) LocalDateTime horario) {
        consultaService.updateConsulta(idConsulta, tipo, horario);
    }

    // PUT cliente, animal e doutor dentro de consulta
    @PutMapping(path = "{idConsulta}/cliente/{idCliente}/animal/{idAnimal}/doutor/{idDoutor}")
    public void conectaClienteAnimalEDoutorAConsulta(@PathVariable Long idConsulta,
            @PathVariable Long idCliente,
            @PathVariable Long idAnimal,
            @PathVariable Long idDoutor) {
        consultaService.conectaClienteAnimalEDoutorAConsulta(idConsulta, idCliente, idAnimal, idDoutor);
    }
}
