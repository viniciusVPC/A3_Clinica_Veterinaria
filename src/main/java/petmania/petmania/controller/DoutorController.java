package petmania.petmania.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import petmania.petmania.dto.DoutorDTO;
import petmania.petmania.model.Doutor;
import petmania.petmania.repository.DoutorRepository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import jakarta.validation.Valid;

@Controller
@RequestMapping("/doutores")
public class DoutorController {

    @Autowired
    private DoutorRepository repo;

    @GetMapping("/signup")
    public String mostraFormularioSignUp(Model model) {
        DoutorDTO doutorDto = new DoutorDTO();
        model.addAttribute("doutorDto", doutorDto);
        return "/doutores/add-doutor";
    }

    @PostMapping("/adddoutor")
    public String addDoutor(@Valid @ModelAttribute("doutorDto") DoutorDTO doutorDto, BindingResult result,
            Model model) {

        // TODO verificar data maior que 18 anos

        if (result.hasErrors()) {
            return "/doutores/add-doutor";
        }

        Doutor doutor = new Doutor(doutorDto.getNome(), doutorDto.getDataNasc(), doutorDto.getCpf(),
                doutorDto.getEmail(), doutorDto.getEspecialidade(), null);

        repo.save(doutor);
        return "redirect:/doutores";
    }

    @GetMapping({ "", "/" })
    public String mostraListaDoutores(Model model) {
        var doutores = repo.findAll(Sort.by(Sort.Direction.ASC, "idDoutor"));
        model.addAttribute("doutores", doutores);
        return "/doutores/index";
    }

    @GetMapping("/edit")
    public String mostraFormularioUpdate(@RequestParam Long id, Model model) {
        Doutor doutor = repo.findById(id)
                .orElseThrow(() -> new IllegalStateException("Doutor com id " + id + " não existe."));
        DoutorDTO doutorDto = new DoutorDTO(doutor.getNome(), doutor.getDataNasc(), doutor.getCpf(), doutor.getEmail(),
                doutor.getEspecialidade());
        model.addAttribute("doutor", doutor);
        model.addAttribute("doutorDto", doutorDto);
        return "/doutores/update-doutor";
    }

    @PostMapping("edit")
    public String updateDoutor(@RequestParam Long id, @Valid @ModelAttribute("doutorDto") DoutorDTO doutorDto,
            BindingResult result, Model model) {
        Doutor doutor = repo.findById(id)
                .orElseThrow(() -> new IllegalStateException("Cliente com id " + id + " não existe."));
        if (result.hasErrors()) {
            return "/doutores/update-doutor";
        }

        doutor = new Doutor(doutorDto.getNome(), doutorDto.getDataNasc(), doutorDto.getCpf(),
                doutorDto.getEmail(), doutorDto.getEspecialidade(), null);
        doutor.setIdDoutor(id);
        repo.save(doutor);
        return "redirect:/doutores";
    }

    @GetMapping("/delete/{id}")
    public String deleteDoutor(@PathVariable("id") Long id, Model model) {
        Doutor doutor = repo.findById(id)
                .orElseThrow(() -> new IllegalStateException("Doutor com id " + id + " não existe."));
        repo.delete(doutor);
        return "redirect:/doutores";
    }
}
