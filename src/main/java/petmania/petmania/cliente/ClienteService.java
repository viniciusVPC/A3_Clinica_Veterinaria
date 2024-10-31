package petmania.petmania.cliente;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;
import petmania.petmania.animal.Animal;
import petmania.petmania.animal.AnimalRepository;

@Service
public class ClienteService {
    private ClienteRepository clienteRepository;
    private AnimalRepository animalRepository;

    public ClienteService(ClienteRepository clienteRepository, AnimalRepository animalRepository) {
        this.clienteRepository = clienteRepository;
        this.animalRepository = animalRepository;
    }

    public List<Cliente> getClientes() {
        return clienteRepository.findAll();
    }

    // Regra de negócio relacionada ao cadastro de um novo cliente.
    // Verifica se já existe cliente com o mesmo cpf ou email
    // TODO fazer o erro não pausar o programa e mostrar só uma janelinha avisando
    public void addNewCliente(Cliente cliente) {
        Optional<Cliente> clienteCpfOptional = clienteRepository.findClienteByCpf(cliente.getCpf());
        Optional<Cliente> clienteEmailOptional = clienteRepository.findClienteByEmail(cliente.getEmail());
        if (clienteCpfOptional.isPresent()) {
            throw new IllegalStateException("já existe um cliente com esse CPF cadastrado");
        }
        if (clienteEmailOptional.isPresent()) {
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
