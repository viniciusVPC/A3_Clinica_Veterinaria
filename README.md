<h1 align="center">Sistema para controle de clientes, animais e consultas da clinica veterinária fictícia "PetMania".</h1>

<p>Esse projeto está sendo criado para as matérias de Usabilidade, Desenvolvimento Web, Mobile e Jogos e Sistemas Distribuídos e Mobile.
Foi feita a decisão de unir as duas matérias em um projeto, desenvolvendo um FrontEnd para atender os requisitos da primeira e um BackEnd para a segunda.</p>

# Características do Projeto
## FrontEnd
- [ ] Site criado em HTML, estilizado em CSS e automatizado com Javascript.
- [ ] Tela de Login com usuário e senha, separando papéis entre: Administrador, Cliente e Doutor.
- [ ] Menu de navegação separando as telas disponíveis para cada papel: <br>
Cliente: Cadastrar Pet; Marcar consulta; Consultas Marcadas <br>
Doutor: Marcar Consulta; Consultas Marcadas <br>
Administrador: Cadastrar Cliente; Cadastrar Doutor; Cadastrar Pet; Cadastrar Administrador; Marcar Consulta; Consultas Marcadas
- [ ] Mensagens de feedback no rodapé do site
- [ ] Sub-menu de currículos dos criadores do projeto (também criado em html)
- [ ] Comunicação com o servidor através de requests API em JSON

## BackEnd
- [ ] Sistema CRUD programado em Java com JPA para gerenciar clientes, animais, doutores, administradores e consultas.
- [ ] API utilizando a biblioteca Spring Boot com funções de GET, POST, DELETE e PUT.
- [ ] Listar, Criar, Deletar e Ediar Cliente, Animal, Doutor e Administrador.
- [ ] Conectar Animal já criado a cliente já criado.
- [ ] Criar consulta, conectando cliente, animal e doutor à mesma.
- [ ] Múltiplas regras de negócio.

# Classes
- Cliente
  - Long id (Primary Key)
  - String nome
  - LocalDate dataNasc
  - String cpf
  - String email
  - Set<Animal> pets (ManyToMany)
  - Set<Consulta> consultas (OneToMany)

- Animal
  - Long id (Primary Key)
  - String nome
  - LocalDate dataNasc
  - int idade
  - String especie
  - String raca
  - Set<Cliente> donos (ManyToMany)
  - Set<Consulta> consultas (OneToMany) 

- Doutor
  - Long idDoutor (Primary Key)
  - String nome
  - LocalDate dataNasc
  - String cpf
  - String email
  - String especialidade
  - Set<Consulta> consultas (OneToMany)

- Administrador
  - Long id (Primary Key)
  - String nome
  - LocalDate dataNasc
  - String cpf
  - String email

- Consulta
  - Long idConsulta (Primary Key)
  - Cliente cliente (ManyToOne)
  - Animal animal (ManyToOne)
  - Doutor doutor (ManyToOne)
  - String tipo
  - LocalDateTime horario

# Regras de Negócio:
- Um Cliente só pode ser criado se não houver clientes com o mesmo CPF e Email.
- Um Cliente só pode ser modificado se não houver outro cliente com o mesmo CPF e Email.
- Um Doutor só pode ser criado se não houver doutores com o mesmo CPF e Email.
- Um Doutor só pode ser modificado se não houver outro doutor com o mesmo CPF e Email.
- Um Administrador só pode ser criado se não houver administradores com o mesmo CPF e Email.
- Um Animal só pode ser conectado a um cliente se o mesmo não houver um outro animal com mesmo nome e data de nascimento (animal repetido)
- Uma consulta só pode ser criada se o respectivo Animal pertencer ao respectivo Cliente
- Uma consulta só pode ser criada se o seu horário não estiver dentro de um espaço menor que 30min das consultas já marcadas do seu respectivo Doutor (nem 30min antes nem 30min depois)
- Uma consulta só pode ser editada se o seu horário não estiver dentro de um espaço menor que 30min das consultas do respectivo Doutor
