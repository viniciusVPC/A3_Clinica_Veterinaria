package petmania.petmania.cliente;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;
import petmania.petmania.animal.AnimalRepository;

@Service
public class ClienteService {
    private ClienteRepository clienteRepository;

    public ClienteService(ClienteRepository clienteRepository) {
        this.clienteRepository = clienteRepository;
    }

    public List<Cliente> getClientes() {
        return clienteRepository.findAll();
    }

    // Regra de negócio relacionada ao cadastro de um novo cliente.
    // Se já existe um cliente com o mesmo CPF ele joga um erro de "cliente
    // repetido"
    // TODO fazer o erro não pausar o programa e mostrar só uma janelinha avisando
    public void addNewCliente(Cliente cliente) {
        Optional<Cliente> clienteOptional = clienteRepository.findClienteByCPF(cliente.getCPF());
        if (clienteOptional.isPresent()) {
            throw new IllegalStateException("já existe um cliente com esse CPF cadastrado");
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
    // verifica se existe cliente com esse id. Caso não, joga um erro
    // se existe, atualiza as informações do cliente na TABLE
    // o @Transactional torna desnecessário o uso de Querys
    @Transactional
    public void updateCliente(Long idCliente, String nome, String CPF, String email) {
        Cliente cliente = clienteRepository.findById(idCliente)
                .orElseThrow(() -> new IllegalStateException("Cliente com id " + idCliente + " não existe"));
        if (nome != null && nome.length() > 0 && !Objects.equals(cliente.getNome(), nome)) {
            cliente.setNome(nome);
        }
        if (CPF != null && CPF.length() > 0 && !Objects.equals(cliente.getCPF(), CPF)) {
            System.out.println(CPF);
            Optional<Cliente> clienteOptional = clienteRepository.findClienteByCPF(CPF);
            if (clienteOptional.isPresent()) {
                throw new IllegalStateException("já existe um cliente com esse CPF cadastrado");
            }
            cliente.setCPF(CPF);
        }
        if (email != null && email.length() > 0 && !Objects.equals(cliente.getEmail(), email)) {
            Optional<Cliente> clienteOptional = clienteRepository.findClienteByEmail(email);
            if (clienteOptional.isPresent()) {
                throw new IllegalStateException("já existe um cliente com esse Email cadastrado");
            }
            cliente.setEmail(email);
        }
    }

}
