package petmania.petmania.service;

import java.util.Objects;
import java.util.Optional;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;

import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import petmania.petmania.model.Animal;
import petmania.petmania.model.Cliente;
import petmania.petmania.repository.AnimalRepository;
import petmania.petmania.repository.ClienteRepository;

@Service
public class ClienteService {
    @Autowired
    private ClienteRepository clienteRepository;
    @Autowired
    private AnimalRepository animalRepository;

    public String mostraListaClientes(Model model) {
        var clientes = clienteRepository.findAll(Sort.by(Sort.Direction.ASC, "id"));
        model.addAttribute("clientes", clientes);
        return "/clientes/index";
    }

    public String addCliente(@Valid Cliente cliente, BindingResult result, Model model) {
        if (result.hasErrors()) {
            return "/clientes/add-cliente";
        }
        clienteRepository.save(cliente);
        return "redirect:/clientes";
    }

    // Regra de negócio relacionada ao cadastro de um novo cliente.
    // Verifica se já existe cliente com o mesmo cpf ou email
    // TODO fazer o erro não pausar o programa e mostrar só uma janelinha avisando
    public void addNewCliente(Cliente cliente) {
        Optional<Cliente> clienteOptional = clienteRepository.findClienteByCpf(cliente.getCpf());
        if (clienteOptional.isPresent()) {
            throw new IllegalStateException("já existe um cliente com esse CPF cadastrado");
        }
        clienteOptional = clienteRepository.findClienteByEmail(cliente.getEmail());
        if (clienteOptional.isPresent()) {
            throw new IllegalStateException("já existe um cliente com esse email cadastrado");
        }
        clienteRepository.save(cliente);
    }

    // regra de negócio relacionada à remoção de um cliente do BD.
    // verifica se existe cliente com esse id. Caso não, joga um erro.
    // se existe, deleta o cliente da TABLE
    public void deleteCliente(Long idCliente) {
        boolean existe = clienteRepository.existsById(idCliente);
        if (!existe) {
            throw new IllegalStateException("Cliente com id " + idCliente + " não existe");
        }
        clienteRepository.deleteById(idCliente);
    }

    // regra de negócio relacionada à edição de um cliente já existente.
    // verifica se existe cliente com esse id. Caso não, joga um erro.
    // verifica se já existe cliente com esse cpf ou email
    // o @Transactional torna desnecessário o uso de Querys
    @Transactional
    public void updateCliente(Long idCliente, String nome, String cpf, String email) {
        Cliente cliente = clienteRepository.findById(idCliente)
                .orElseThrow(() -> new IllegalStateException("Cliente com id " + idCliente + " não existe"));
        if (nome != null && nome.length() > 0 && !Objects.equals(cliente.getNome(), nome)) {
            cliente.setNome(nome);
        }
        if (cpf != null && cpf.length() > 0 && !Objects.equals(cliente.getCpf(), cpf)) {
            Optional<Cliente> clienteOptional = clienteRepository.findClienteByCpf(cpf);
            if (clienteOptional.isPresent()) {
                throw new IllegalStateException("já existe um cliente com esse CPF cadastrado");
            }
            cliente.setCpf(cpf);
        }
        if (email != null && email.length() > 0 && !Objects.equals(cliente.getEmail(), email)) {
            Optional<Cliente> clienteOptional = clienteRepository.findClienteByEmail(email);
            if (clienteOptional.isPresent()) {
                throw new IllegalStateException("já existe um cliente com esse Email cadastrado");
            }
            cliente.setEmail(email);
        }
    }

    // Regra de negócio responsável por conectar um animal a um cliente.
    // verifica se existe cliente e animal
    // verifica se cliente já não tem animal com mesmo nome e dataNasc
    @Transactional
    public void conectaAnimalaCliente(Long idCliente, Long idAnimal) {
        Set<Animal> pets = null;
        Cliente cliente = clienteRepository.findById(idCliente)
                .orElseThrow(() -> new IllegalStateException("Cliente com id " + idCliente + " não existe"));
        Animal animal = animalRepository.findById(idAnimal)
                .orElseThrow(() -> new IllegalStateException("Animal com id " + idAnimal + " não existe"));
        pets = cliente.getPets();
        for (Animal pet : pets) {
            if (pet.getNome() == animal.getNome() && pet.getDataNasc() == animal.getDataNasc()) {
                throw new IllegalStateException("Cliente já tem esse animal");
            }
        }
        pets.add(animal);
        cliente.setPets(pets);
    }

}
