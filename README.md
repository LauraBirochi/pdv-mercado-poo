=# PDV Mercado — Projeto Acadêmico de POO em Java (MVC)

Sistema de Ponto de Venda desenvolvido para demonstrar conceitos de
**Programação Orientada a Objetos** utilizando o padrão **MVC** com **Java Swing**.

---

## 🚀 Como executar no NetBeans (Projeto Maven)

### Abrindo o projeto

1. Abra o **NetBeans IDE**
2. Vá em **File → Open Project**
3. Selecione a pasta raiz do projeto (`POO/`) — o NetBeans reconhecerá automaticamente o `pom.xml`
4. Clique em **Open Project**

### Executando

- Clique com o botão direito no projeto → **Run**  
  **ou** pressione **F6**

> O NetBeans Maven detecta automaticamente a classe `Main` configurada no `pom.xml` como `mainClass`.

### Credenciais de Teste

| Perfil  | Login  | Senha    |
|---------|--------|----------|
| Gerente | admin  | admin123 |
| Caixa   | joao   | joao123  |

---

## 📁 Estrutura do Projeto (Maven — NetBeans)

```
POO/
├── pom.xml                            ← Configuração Maven (groupId, mainClass, Java 11)
│
└── src/
    └── main/
        └── java/
            └── br/
                └── com/
                    └── pdvmercado/
                        ├── Main.java                      ← Ponto de entrada
                        │
                        ├── model/
                        │    ├── Produto.java              ← Encapsulamento
                        │    ├── Cliente.java              ← CPF imutável
                        │    ├── Usuario.java              ← Superclasse (herança)
                        │    ├── Caixa.java                ← Subclasse de Usuario
                        │    ├── Gerente.java              ← Subclasse de Usuario
                        │    ├── Pagamento.java            ← Classe abstrata (polimorfismo)
                        │    ├── PagamentoDinheiro.java    ← Subclasse com troco
                        │    ├── PagamentoCartao.java      ← Subclasse com parcelamento
                        │    ├── PagamentoPix.java         ← Subclasse com chave Pix
                        │    ├── Venda.java                ← Carrinho + regras de negócio
                        │    ├── ItemVenda.java            ← Item do carrinho
                        │    └── FechamentoCaixa.java      ← Interface (contrato)
                        │
                        ├── view/
                        │    ├── LoginView.java            ← Tela de login
                        │    ├── CaixaView.java            ← Tela principal do PDV
                        │    ├── PagamentoView.java        ← Seleção de pagamento
                        │    ├── ProdutoView.java          ← Gestão de produtos
                        │    └── FuncionarioView.java      ← Gestão de funcionários
                        │
                        └── controller/
                             ├── LoginController.java      ← Autenticação e usuários
                             ├── ProdutoController.java    ← CRUD de produtos e estoque
                             ├── VendaController.java      ← Fluxo de venda (impl. FechamentoCaixa)
                             └── SistemaController.java    ← Fachada central do sistema
```

### Configuração Maven (`pom.xml`)

| Propriedade    | Valor                  |
|----------------|------------------------|
| `groupId`      | `br.com.pdvmercado`    |
| `artifactId`   | `pdv-mercado`          |
| `version`      | `1.0-SNAPSHOT`         |
| Java Source    | 11                     |
| Main Class     | `br.com.pdvmercado.Main` |

---

## 🎓 Conceitos de POO Aplicados

### 1. Encapsulamento — `Produto.java`
```
Atributos privados: nome, preco, estoque
Métodos de negócio: reduzirEstoque(), adicionarEstoque(), temEstoque()
O estoque NÃO pode ser alterado diretamente (sem setter de estoque)
```

### 2. Encapsulamento — `Cliente.java`
```
Atributos privados: nome, cpf
CPF é imutável após a criação (sem setter)
Métodos: validarCpf(), atualizarNome()
```

### 3. Herança — Hierarquia de Usuário
```
Usuario (superclasse)
├── Caixa      → numeroCaixa, abrirCaixa(), podeRegistrarVenda()
└── Gerente    → departamento, podeCadastrarProduto(), podeGerenciarFuncionarios()
```

### 4. Classe Abstrata + Polimorfismo — Sistema de Pagamentos
```
Pagamento (abstract)    ← processar() e getTipo() são abstratos
├── PagamentoDinheiro   → calcularTroco(), processa com troco
├── PagamentoCartao     → calcularParcela(), débito ou crédito
└── PagamentoPix        → gerarCodigoPix(), chave Pix
```

### 5. Interface — `FechamentoCaixa`
```
Interface FechamentoCaixa
  + fecharCaixa() : String
  + calcularTotalVendas() : double
  + getTotalTransacoes() : int

Implementada por: VendaController
```

---

## 📐 Diagrama UML Textual

```
┌─────────────────────────────────────────────────────────────────┐
│                        <<interface>>                            │
│                       FechamentoCaixa                           │
│─────────────────────────────────────────────────────────────────│
│ + fecharCaixa() : String                                        │
│ + calcularTotalVendas() : double                                │
│ + getTotalTransacoes() : int                                    │
└───────────────────────────┬─────────────────────────────────────┘
                            │ implements
                            ▼
┌───────────────────────────────────────────────────────────────┐
│                      VendaController                          │
│───────────────────────────────────────────────────────────────│
│ - produtoController : ProdutoController                       │
│ - vendaAtual : Venda                                          │
│ - historicoVendas : ArrayList<Venda>                          │
│───────────────────────────────────────────────────────────────│
│ + iniciarVenda(operador)                                      │
│ + adicionarItem(id, qtd) : String                             │
│ + selecionarPagamento(pagamento)                              │
│ + finalizarVenda() : String    ← baixa automática de estoque  │
│ + fecharCaixa() : String                                      │
└───────────────────────────────────────────────────────────────┘

┌──────────────────────────────────────┐
│         <<abstract>>                 │
│            Pagamento                 │
│──────────────────────────────────────│
│ - valor : double                     │
│ - descricao : String                 │
│──────────────────────────────────────│
│ + processar() : String  {abstract}   │
│ + getTipo() : String    {abstract}   │
│ + getValor() : double                │
└──────────────────────────────────────┘
         △             △             △
         │             │             │
┌────────────┐ ┌────────────┐ ┌────────────┐
│ Pagamento  │ │ Pagamento  │ │ Pagamento  │
│ Dinheiro   │ │  Cartao    │ │    Pix     │
│────────────│ │────────────│ │────────────│
│-valorReceb.│ │-tipoCartao │ │-chavePix   │
│────────────│ │-parcelas   │ │-codigoPix  │
│+processar()│ │────────────│ │────────────│
│+calcTroco()│ │+processar()│ │+processar()│
└────────────┘ │+calcParc() │ │+getTipo()  │
               └────────────┘ └────────────┘

┌──────────────────────────────────────┐
│             Usuario                  │
│──────────────────────────────────────│
│ - id : int                           │
│ - nome : String                      │
│ - login : String                     │
│ - senha : String  (privado)          │
│ - perfil : String                    │
│──────────────────────────────────────│
│ + autenticar(senha) : boolean        │
│ + getLogin() : String                │
│ + getPerfil() : String               │
└──────────────────────────────────────┘
         △                    △
         │                    │
┌──────────────┐    ┌──────────────────┐
│    Caixa     │    │     Gerente      │
│──────────────│    │──────────────────│
│-numeroCaixa  │    │-departamento     │
│──────────────│    │──────────────────│
│+abrirCaixa() │    │+podeCadastrar()  │
│+podeVender() │    │+podeGerenciar()  │
└──────────────┘    └──────────────────┘
```

---

## 🔄 Fluxo de uma Venda

```
1. Login → LoginView → LoginController.realizarLogin()
2. Abre CaixaView → VendaController.iniciarVenda()
3. Busca produto → ProdutoController.buscarPorNome()
4. Adiciona ao carrinho → VendaController.adicionarItem()
   └── Verifica estoque → Produto.temEstoque()
5. Abre PagamentoView → seleciona forma de pagamento
   └── Cria: PagamentoDinheiro / PagamentoCartao / PagamentoPix (polimorfismo)
6. Confirma → VendaController.finalizarVenda()
   ├── Pagamento.processar()          ← polimorfismo em ação
   └── Produto.reduzirEstoque()       ← baixa automática
7. Gera Cupom → Venda.gerarCupom()
8. Opcional: fecharCaixa() → VendaController.fecharCaixa()
                                      ← implementa FechamentoCaixa
```

---

## 📋 Regras de Negócio Implementadas

| # | Regra | Onde está implementada |
|---|-------|----------------------|
| 1 | Baixa automática de estoque ao finalizar venda | `VendaController.finalizarVenda()` |
| 2 | Estoque não pode ser alterado diretamente | `Produto` — sem setter de estoque |
| 3 | Venda só finaliza com pagamento selecionado | `Venda.finalizar()` |
| 4 | Caixa não pode cadastrar produtos | `ProdutoView` + `SistemaController.usuarioEhGerente()` |
| 5 | Gerente não pode remover a si mesmo | `FuncionarioView.removerFuncionario()` |
| 6 | CPF imutável após cadastro do cliente | `Cliente` — sem setter de CPF |
| 7 | Carrinho só pode ser modificado em venda aberta | `Venda.adicionarItem()` |

---

*Projeto acadêmico — POO em Java com MVC | NetBeans + Maven*
