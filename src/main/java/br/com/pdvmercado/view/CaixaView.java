package br.com.pdvmercado.view;

import br.com.pdvmercado.controller.SistemaController;
import br.com.pdvmercado.model.*;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.ArrayList;

/**
 * CaixaView
 *
 * Camada VIEW do padrão MVC.
 *
 * Tela principal do operador de caixa.
 * Permite: buscar produtos, montar carrinho, selecionar pagamento e finalizar venda.
 *
 * Esta é a tela mais importante do sistema PDV.
 */
public class CaixaView extends JFrame {

    private SistemaController sistemaController;

    // Componentes do carrinho
    private JTable tabelaCarrinho;
    private DefaultTableModel modeloTabela;
    private JLabel labelTotal;
    private JLabel labelOperador;

    // Campos de entrada
    private JTextField campoBuscaProduto;
    private JTextField campoQuantidade;
    private JList<String> listaProdutos;
    private DefaultListModel<String> modeloLista;

    // Construtor
    public CaixaView(SistemaController sistemaController) {
        this.sistemaController = sistemaController;
        configurarJanela();
        criarComponentes();
        iniciarVenda();
    }

    // =============================================
    // CONFIGURAÇÃO DA JANELA
    // =============================================

    private void configurarJanela() {
        setTitle("PDV Mercado - Caixa | Operador: " + sistemaController.getNomeUsuarioLogado());
        setSize(950, 650);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        getContentPane().setBackground(new Color(25, 25, 38));
        setLayout(new BorderLayout(10, 10));
    }

    // =============================================
    // CRIAÇÃO DOS COMPONENTES
    // =============================================

    private void criarComponentes() {
        // --- Barra superior ---
        JPanel painelTopo = criarPainelTopo();
        add(painelTopo, BorderLayout.NORTH);

        // --- Painel esquerdo: Busca e produtos ---
        JPanel painelEsquerdo = criarPainelBusca();
        add(painelEsquerdo, BorderLayout.WEST);

        // --- Painel central: Carrinho ---
        JPanel painelCarrinho = criarPainelCarrinho();
        add(painelCarrinho, BorderLayout.CENTER);

        // --- Painel inferior: Ações e pagamento ---
        JPanel painelAcoes = criarPainelAcoes();
        add(painelAcoes, BorderLayout.SOUTH);

        // --- Menu de navegação ---
        criarMenuNavegacao();
    }

    private JPanel criarPainelTopo() {
        JPanel painel = new JPanel(new BorderLayout());
        painel.setBackground(new Color(35, 35, 52));
        painel.setBorder(BorderFactory.createEmptyBorder(12, 20, 12, 20));

        JLabel titulo = new JLabel("🛒 PDV MERCADO");
        titulo.setFont(new Font("Arial", Font.BOLD, 20));
        titulo.setForeground(new Color(100, 200, 120));
        painel.add(titulo, BorderLayout.WEST);

        labelOperador = new JLabel("Operador: " + sistemaController.getNomeUsuarioLogado()
                + "  |  Perfil: " + sistemaController.getPerfilUsuarioLogado());
        labelOperador.setFont(new Font("Arial", Font.PLAIN, 12));
        labelOperador.setForeground(new Color(180, 180, 180));
        painel.add(labelOperador, BorderLayout.EAST);

        return painel;
    }

    private JPanel criarPainelBusca() {
        JPanel painel = new JPanel();
        painel.setLayout(new BoxLayout(painel, BoxLayout.Y_AXIS));
        painel.setBackground(new Color(35, 35, 52));
        painel.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(new Color(80, 80, 110)),
                "🔍 Buscar Produto",
                0, 0, new Font("Arial", Font.BOLD, 12), new Color(150, 150, 200)));
        painel.setPreferredSize(new Dimension(260, 0));

        JPanel campoBusca = new JPanel(new FlowLayout(FlowLayout.LEFT));
        campoBusca.setOpaque(false);

        campoBuscaProduto = new JTextField(16);
        estilizarCampo(campoBuscaProduto);
        campoBusca.add(campoBuscaProduto);

        JButton botaoBuscar = new JButton("Buscar");
        estilizarBotao(botaoBuscar, new Color(60, 120, 200));
        botaoBuscar.addActionListener(e -> buscarProdutos());
        campoBusca.add(botaoBuscar);

        painel.add(campoBusca);

        // Lista de resultados
        modeloLista = new DefaultListModel<>();
        listaProdutos = new JList<>(modeloLista);
        listaProdutos.setBackground(new Color(45, 45, 65));
        listaProdutos.setForeground(Color.WHITE);
        listaProdutos.setFont(new Font("Arial", Font.PLAIN, 12));
        listaProdutos.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        listaProdutos.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

        JScrollPane scroll = new JScrollPane(listaProdutos);
        scroll.setPreferredSize(new Dimension(250, 200));
        scroll.getViewport().setBackground(new Color(45, 45, 65));
        painel.add(scroll);

        // Campo quantidade
        JPanel painelQtd = new JPanel(new FlowLayout(FlowLayout.LEFT));
        painelQtd.setOpaque(false);
        JLabel lblQtd = new JLabel("Qtd:");
        lblQtd.setForeground(Color.WHITE);
        campoQuantidade = new JTextField("1", 5);
        estilizarCampo(campoQuantidade);
        painelQtd.add(lblQtd);
        painelQtd.add(campoQuantidade);
        painel.add(painelQtd);

        // Botão adicionar ao carrinho
        JButton botaoAdicionar = new JButton("➕ Adicionar ao Carrinho");
        estilizarBotao(botaoAdicionar, new Color(60, 160, 90));
        botaoAdicionar.setAlignmentX(Component.CENTER_ALIGNMENT);
        botaoAdicionar.addActionListener(e -> adicionarAoCarrinho());
        painel.add(botaoAdicionar);

        // Carrega todos os produtos inicialmente
        carregarTodosProdutos();

        return painel;
    }

    private JPanel criarPainelCarrinho() {
        JPanel painel = new JPanel(new BorderLayout());
        painel.setBackground(new Color(30, 30, 45));
        painel.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(new Color(80, 80, 110)),
                "🛒 Carrinho de Compras",
                0, 0, new Font("Arial", Font.BOLD, 12), new Color(150, 150, 200)));

        // Tabela do carrinho
        String[] colunas = {"#", "Produto", "Preço Unit.", "Quantidade", "Subtotal"};
        modeloTabela = new DefaultTableModel(colunas, 0) {
            @Override
            public boolean isCellEditable(int row, int col) {
                return false; // Tabela somente leitura
            }
        };

        tabelaCarrinho = new JTable(modeloTabela);
        tabelaCarrinho.setBackground(new Color(40, 40, 58));
        tabelaCarrinho.setForeground(Color.WHITE);
        tabelaCarrinho.setFont(new Font("Arial", Font.PLAIN, 13));
        tabelaCarrinho.setRowHeight(28);
        tabelaCarrinho.getTableHeader().setBackground(new Color(50, 80, 130));
        tabelaCarrinho.getTableHeader().setForeground(Color.WHITE);
        tabelaCarrinho.getTableHeader().setFont(new Font("Arial", Font.BOLD, 12));
        tabelaCarrinho.setSelectionBackground(new Color(60, 100, 160));
        tabelaCarrinho.setGridColor(new Color(60, 60, 80));

        JScrollPane scrollTabela = new JScrollPane(tabelaCarrinho);
        scrollTabela.getViewport().setBackground(new Color(40, 40, 58));
        painel.add(scrollTabela, BorderLayout.CENTER);

        // Total
        JPanel painelTotal = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        painelTotal.setBackground(new Color(35, 35, 52));
        painelTotal.setBorder(BorderFactory.createEmptyBorder(8, 10, 8, 10));

        JButton btnRemover = new JButton("❌ Remover Item");
        estilizarBotao(btnRemover, new Color(180, 60, 60));
        btnRemover.addActionListener(e -> removerItemCarrinho());
        painelTotal.add(btnRemover);

        labelTotal = new JLabel("TOTAL: R$ 0,00");
        labelTotal.setFont(new Font("Arial", Font.BOLD, 18));
        labelTotal.setForeground(new Color(100, 220, 120));
        painelTotal.add(labelTotal);

        painel.add(painelTotal, BorderLayout.SOUTH);

        return painel;
    }

    private JPanel criarPainelAcoes() {
        JPanel painel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 10));
        painel.setBackground(new Color(35, 35, 52));
        painel.setBorder(BorderFactory.createEmptyBorder(5, 10, 10, 10));

        JButton btnPagamento = new JButton("💳 Pagamento");
        estilizarBotao(btnPagamento, new Color(70, 130, 200));
        btnPagamento.setFont(new Font("Arial", Font.BOLD, 14));
        btnPagamento.addActionListener(e -> abrirPagamento());
        painel.add(btnPagamento);

        JButton btnCancelar = new JButton("🗑️ Cancelar Venda");
        estilizarBotao(btnCancelar, new Color(180, 60, 60));
        btnCancelar.setFont(new Font("Arial", Font.BOLD, 14));
        btnCancelar.addActionListener(e -> cancelarVenda());
        painel.add(btnCancelar);

        JButton btnFecharCaixa = new JButton("🔒 Fechar Caixa");
        estilizarBotao(btnFecharCaixa, new Color(120, 80, 180));
        btnFecharCaixa.setFont(new Font("Arial", Font.BOLD, 14));
        btnFecharCaixa.addActionListener(e -> fecharCaixa());
        painel.add(btnFecharCaixa);

        return painel;
    }

    private void criarMenuNavegacao() {
        JMenuBar menuBar = new JMenuBar();
        menuBar.setBackground(new Color(35, 35, 52));

        JMenu menuSistema = new JMenu("Sistema");
        menuSistema.setForeground(Color.WHITE);

        JMenuItem itemProdutos = new JMenuItem("📦 Gerenciar Produtos");
        itemProdutos.addActionListener(e -> abrirGerenciamentoProdutos());

        JMenuItem itemFuncionarios = new JMenuItem("👥 Gerenciar Funcionários");
        itemFuncionarios.addActionListener(e -> abrirGerenciamentoFuncionarios());

        JMenuItem itemLogout = new JMenuItem("🚪 Logout");
        itemLogout.addActionListener(e -> realizarLogout());

        menuSistema.add(itemProdutos);
        menuSistema.add(itemFuncionarios);
        menuSistema.addSeparator();
        menuSistema.add(itemLogout);

        menuBar.add(menuSistema);
        setJMenuBar(menuBar);
    }

    // =============================================
    // AÇÕES DO CAIXA
    // =============================================

    private void iniciarVenda() {
        sistemaController.getVendaController()
                .iniciarVenda(sistemaController.getLoginController().getUsuarioLogado());
        atualizarCarrinho();
    }

    private void carregarTodosProdutos() {
        modeloLista.clear();
        ArrayList<Produto> produtos = sistemaController.getProdutoController().listarTodos();
        for (Produto p : produtos) {
            modeloLista.addElement(p.getId() + " | " + p.getNome()
                    + " - R$ " + String.format("%.2f", p.getPreco())
                    + " (Est: " + p.getEstoque() + ")");
        }
    }

    private void buscarProdutos() {
        String termo = campoBuscaProduto.getText().trim();
        modeloLista.clear();

        ArrayList<Produto> resultado;
        if (termo.isEmpty()) {
            resultado = sistemaController.getProdutoController().listarTodos();
        } else {
            resultado = sistemaController.getProdutoController().buscarPorNome(termo);
        }

        for (Produto p : resultado) {
            modeloLista.addElement(p.getId() + " | " + p.getNome()
                    + " - R$ " + String.format("%.2f", p.getPreco())
                    + " (Est: " + p.getEstoque() + ")");
        }
    }

    private void adicionarAoCarrinho() {
        String selecionado = listaProdutos.getSelectedValue();
        if (selecionado == null) {
            JOptionPane.showMessageDialog(this, "Selecione um produto na lista.");
            return;
        }

        try {
            int idProduto = Integer.parseInt(selecionado.split("\\|")[0].trim());
            int quantidade = Integer.parseInt(campoQuantidade.getText().trim());

            String resultado = sistemaController.getVendaController()
                    .adicionarItem(idProduto, quantidade);

            atualizarCarrinho();
            carregarTodosProdutos(); // Atualiza lista de produtos

            JOptionPane.showMessageDialog(this, resultado, "Carrinho", JOptionPane.INFORMATION_MESSAGE);

        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Quantidade inválida!");
        }
    }

    private void removerItemCarrinho() {
        int linhaSelecionada = tabelaCarrinho.getSelectedRow();
        if (linhaSelecionada < 0) {
            JOptionPane.showMessageDialog(this, "Selecione um item na tabela para remover.");
            return;
        }
        sistemaController.getVendaController().removerItem(linhaSelecionada);
        atualizarCarrinho();
    }

    /**
     * Atualiza a tabela do carrinho com os itens da venda atual.
     */
    private void atualizarCarrinho() {
        modeloTabela.setRowCount(0);

        Venda vendaAtual = sistemaController.getVendaController().getVendaAtual();
        if (vendaAtual == null) return;

        int i = 1;
        for (ItemVenda item : vendaAtual.getItens()) {
            modeloTabela.addRow(new Object[]{
                    i++,
                    item.getProduto().getNome(),
                    String.format("R$ %.2f", item.getProduto().getPreco()),
                    item.getQuantidade(),
                    String.format("R$ %.2f", item.calcularSubtotal())
            });
        }

        double total = sistemaController.getVendaController().getTotalCarrinho();
        labelTotal.setText(String.format("TOTAL: R$ %.2f", total));
    }

    private void abrirPagamento() {
        if (sistemaController.getVendaController().getVendaAtual() == null ||
                sistemaController.getVendaController().getVendaAtual().getItens().isEmpty()) {
            JOptionPane.showMessageDialog(this, "O carrinho está vazio!");
            return;
        }

        PagamentoView pagamentoView = new PagamentoView(sistemaController, this);
        pagamentoView.setVisible(true);
    }

    private void cancelarVenda() {
        int resposta = JOptionPane.showConfirmDialog(this,
                "Deseja cancelar a venda atual?", "Cancelar Venda",
                JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);

        if (resposta == JOptionPane.YES_OPTION) {
            sistemaController.getVendaController().cancelarVenda();
            iniciarVenda();
            JOptionPane.showMessageDialog(this, "Venda cancelada com sucesso.");
        }
    }

    private void fecharCaixa() {
        String relatorio = sistemaController.getVendaController().fecharCaixa();
        JOptionPane.showMessageDialog(this, relatorio, "Fechamento de Caixa",
                JOptionPane.INFORMATION_MESSAGE);
    }

    private void abrirGerenciamentoProdutos() {
        ProdutoView produtoView = new ProdutoView(sistemaController);
        produtoView.setVisible(true);
    }

    private void abrirGerenciamentoFuncionarios() {
        // Verifica permissão de gerente
        if (!sistemaController.usuarioEhGerente()) {
            JOptionPane.showMessageDialog(this,
                    "Acesso negado! Apenas gerentes podem gerenciar funcionários.",
                    "Sem permissão", JOptionPane.WARNING_MESSAGE);
            return;
        }
        FuncionarioView funcionarioView = new FuncionarioView(sistemaController);
        funcionarioView.setVisible(true);
    }

    private void realizarLogout() {
        sistemaController.getLoginController().realizarLogout();
        LoginView loginView = new LoginView(sistemaController);
        loginView.setVisible(true);
        this.dispose();
    }

    // =============================================
    // MÉTODO PÚBLICO - chamado pela PagamentoView
    // =============================================

    /**
     * Chamado pela PagamentoView após finalizar a venda com sucesso.
     * Reinicia o carrinho para uma nova venda.
     */
    public void vendaFinalizada() {
        iniciarVenda();
        carregarTodosProdutos();
        JOptionPane.showMessageDialog(this, "✅ Venda finalizada com sucesso!\nCarrinho reiniciado.");
    }

    // =============================================
    // ESTILOS
    // =============================================

    private void estilizarCampo(JTextField campo) {
        campo.setBackground(new Color(55, 55, 75));
        campo.setForeground(Color.WHITE);
        campo.setCaretColor(Color.WHITE);
        campo.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(80, 80, 100)),
                BorderFactory.createEmptyBorder(4, 6, 4, 6)));
    }

    private void estilizarBotao(JButton botao, Color cor) {
        botao.setBackground(cor);
        botao.setForeground(Color.WHITE);
        botao.setFont(new Font("Arial", Font.BOLD, 12));
        botao.setBorder(BorderFactory.createEmptyBorder(8, 15, 8, 15));
        botao.setCursor(new Cursor(Cursor.HAND_CURSOR));
        botao.setFocusPainted(false);
    }
}
